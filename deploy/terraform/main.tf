terraform {
  required_providers {
    yandex = {
      source = "yandex-cloud/yandex"
    }
  }
  required_version = ">= 0.13"
}

provider "yandex" {
  zone = "ru-central1-a"
  folder_id = var.folder_id
  service_account_key_file = "/key.json"
}


locals {
  zones = [
    {
      name = "ru-central1-a"
      ip_prefix = "192.168.10.0/25"
    },
    {
      name = "ru-central1-b"
      ip_prefix = "192.168.10.128/25"
    }]
}

resource "yandex_vpc_network" "certvault-network" {
  name = "certvault-network"
}

resource "yandex_vpc_subnet" "subnets" {
  for_each = {for zone in local.zones : zone.name => zone}
  name           = format("%s-%s", "certvault-subnet", each.value.name)
  zone           = each.value.name
  network_id     = yandex_vpc_network.certvault-network.id
  v4_cidr_blocks = [each.value.ip_prefix]
}

data "yandex_compute_image" "container-optimized-image" {
  family = "container-optimized-image"
}

resource "yandex_compute_instance_group" "ig-with-coi" {
  depends_on = [yandex_mdb_postgresql_cluster.postgres_cluster, yandex_lockbox_secret_version.last_version]
  name = "ig-with-coi"
  instance_template {
    service_account_id = var.app_instance_sa_id
    platform_id = "standard-v3"
    resources {
      memory = 2
      cores  = 2
    }
    boot_disk {
      mode = "READ_WRITE"
      initialize_params {
        image_id = data.yandex_compute_image.container-optimized-image.id
      }
    }
    network_interface {
      network_id = yandex_vpc_network.certvault-network.id
      subnet_ids = [for subnet in yandex_vpc_subnet.subnets : subnet.id]
    }
    metadata = {
      docker-compose = file("/docker-compose.yaml")
      user-data = sensitive(file("/user-data.yaml"))
    }
  }
  scale_policy {
    fixed_scale {
      size = 2
    }
  }
  allocation_policy {
    zones = [for zone in local.zones : zone.name]
  }
  deploy_policy {
    max_unavailable = 1
    max_creating = 2
    max_expansion = 0
    max_deleting = 1
  }
  service_account_id = var.ig_deploy_sa_id
}

resource "yandex_mdb_postgresql_cluster" "postgres_cluster" {
  name                = "postgres_cluster"
  environment         = "PRODUCTION"
  network_id          = yandex_vpc_network.certvault-network.id

  config {
    version = "16"
    resources {
      resource_preset_id = "s3-c2-m8"
      disk_type_id       = "network-ssd"
      disk_size          = 10
    }
  }

  host {
    zone             = local.zones.0.name
    name             = "host1"
    subnet_id        = yandex_vpc_subnet.subnets[local.zones.0.name].id
    assign_public_ip = false
  }
}

resource "yandex_mdb_postgresql_database" "certVault" {
  cluster_id = yandex_mdb_postgresql_cluster.postgres_cluster.id
  name       = "certVault"
  owner      = "owner"
  depends_on = [
    yandex_mdb_postgresql_user.owner
  ]
}

resource "yandex_mdb_postgresql_user" "owner" {
  cluster_id = yandex_mdb_postgresql_cluster.postgres_cluster.id
  name       = "owner"
  password   = random_password.postgres_password.result
}

resource "random_password" "postgres_password" {
  length = 24
  special = false
}

resource "yandex_lockbox_secret" "my_secret" {
  name = "CertVault"
}

resource "yandex_lockbox_secret_version" "last_version" {
  secret_id = yandex_lockbox_secret.my_secret.id
  entries {
    key        = "POSTGRES_LOGIN"
    text_value = yandex_mdb_postgresql_user.owner.name
  }
  entries {
    key        = "POSTGRES_PASSWORD"
    text_value = random_password.postgres_password.result
  }
}

resource "yandex_lb_target_group" "backend-target-group" {
  depends_on = [
    yandex_compute_instance_group.ig-with-coi
  ]
  name = "backend-target-group"
  dynamic "target" {
    for_each = yandex_compute_instance_group.ig-with-coi.instances
    iterator = instance
    content {
      subnet_id = instance.value.network_interface.0.subnet_id
      address   = instance.value.network_interface.0.ip_address
    }
  }
}

resource "yandex_lb_network_load_balancer" "certvault-nlb" {
  name = "certvault-nlb"
  listener {
    name = "certvault-nlb-listener"
    port = 8080
  }
  attached_target_group {
    target_group_id = yandex_lb_target_group.backend-target-group.id
    healthcheck {
      name = "certvault-api-healthcheck"
      http_options {
        port = 8080
        path = "/api/v1/status"
      }
    }
  }
}