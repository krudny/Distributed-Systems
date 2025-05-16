import logging

import SmartHome
from interactiveShell.handlers.fridgeHandlers.fridge_commands_handler import FridgeCommandsHandler

logging.basicConfig(level=logging.INFO, format='%(message)s')


class FridgeWithShoppingListCommandsHandler(FridgeCommandsHandler):
    def __init__(self, communicator):
        super().__init__(communicator)
        self.logger = logging.getLogger(__name__)
        self.units = self._initialize_units()

    def handle(self, command):
        proxy = self._get_fridge_with_shopping_list_proxy(command.device)
        if not proxy:
            self.logger.error("Invalid command. Cannot find device.")
            return True

        action_method = self._get_action_method(command.action)
        if action_method:
            return action_method(proxy, command)

        self.logger.error(f"Unsupported action '{command.action}' for FridgeWithShoppingList.")
        return True

    def _initialize_units(self):
        return {
            "ml": SmartHome.ProductUnit.ml,
            "l": SmartHome.ProductUnit.l,
            "g": SmartHome.ProductUnit.g,
            "kg": SmartHome.ProductUnit.kg,
            "notSpecified": SmartHome.ProductUnit.notSpecified
        }

    def _get_fridge_with_shopping_list_proxy(self, device_name):
        device_proxy = self.get_proxy(device_name)
        return SmartHome.FridgeWithShoppingListPrx.checkedCast(device_proxy)

    def _get_action_method(self, action):
        return {
            "getShoppingList": self._handle_get_shopping_list,
            "clearShoppingList": self._handle_clear_shopping_list,
            "addShoppingListProductRecord": self._handle_add_shopping_list_product_record,
            "removeShoppingListProductRecord": self._handle_remove_shopping_list_product_record
        }.get(action)

    def _handle_get_shopping_list(self, proxy, command):
        proxy.ice_twoway()
        try:
            shopping_list = proxy.getShoppingList()
            self.logger.info(f"The shopping list for {command.device}: {shopping_list}")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")
        return True

    def _handle_clear_shopping_list(self, proxy, command):
        proxy.ice_oneway()
        try:
            proxy.clearShoppingList()
            self.logger.info(f"The shopping list for {command.device} is empty.")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")
        return True

    def _handle_add_shopping_list_product_record(self, proxy, command):
        proxy.ice_oneway()
        try:
            product_name = command.arguments[0]
            quantity = command.arguments[1]
            unit = self.units.get(command.arguments[2], "notSpecified")
            product_to_add = SmartHome.Product(product_name, unit, int(quantity))
            added_product = proxy.addShoppingListProductRecord(product_to_add)
            self.logger.info(f"The product: {added_product} added to shopping list.")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")
        return True

    def _handle_remove_shopping_list_product_record(self, proxy, command):
        proxy.ice_oneway()
        try:
            product_idx = command.arguments[0]
            product = proxy.removeShoppingListProductRecord(int(product_idx))
            self.logger.info(f"The product: {product} was removed from shopping list.")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")
        return True
