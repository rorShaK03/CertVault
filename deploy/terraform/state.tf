terraform {
  backend "s3" {
    key = "states/certvault.tfstate"
    endpoints = {
      s3 = "http://storage.yandexcloud.net"
    }
    bucket = "certvault-terraform-state"
    region = "ru-central1"

    skip_region_validation = true
    skip_credentials_validation = true
    skip_requesting_account_id  = true
    skip_s3_checksum            = true
  }
}