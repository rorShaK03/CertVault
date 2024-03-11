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
  service_account_key_file = "/key.json"
}

resource "yandex_vpc_network" "certvault-network" {
  name = "certvault-network"
}

resource "yandex_vpc_subnet" "certvault-network-a" {
  name           = "certvault-network-a"
  zone           = "ru-central1-a"
  network_id     = yandex_vpc_network.certvault-network.id
  v4_cidr_blocks = ["192.168.10.0/25"]
}

resource "yandex_vpc_subnet" "certvault-network-b" {
  name           = "certvault-network-b"
  zone           = "ru-central1-b"
  network_id     = yandex_vpc_network.certvault-network.id
  v4_cidr_blocks = ["192.168.10.128/25"]
}

data "yandex_compute_image" "container-optimized-image" {
  family = "container-optimized-image"
}

resource "yandex_compute_instance_group" "ig-with-coi" {
  name = "ig-with-coi"
  service_account_id = var.app_instance_sa_id
  instance_template {
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
      subnet_ids = [yandex_vpc_subnet.certvault-network-a.id, yandex_vpc_subnet.certvault-network-b.id]
      nat = true
    }
    metadata = {
      docker-compose = file("/docker-compose.yaml")
      user-data = file("/user-data.yaml")
    }
  }
  scale_policy {
    fixed_scale {
      size = 2
    }
  }
  allocation_policy {
    zones = ["ru-central1-a", "ru-central1-b"]
  }
  deploy_policy {
    max_unavailable = 2
    max_creating = 2
    max_expansion = 2
    max_deleting = 2
  }
}