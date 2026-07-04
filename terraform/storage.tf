resource "azurerm_storage_account" "test" {
  name                     = "umeshdevopssa4521"
  resource_group_name      = azurerm_resource_group.test.name
  location                 = azurerm_resource_group.test.location
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_storage_container" "test" {
  name                  = "appcontainer"
  storage_account_name  = azurerm_storage_account.test.name
  container_access_type = "private"
}
