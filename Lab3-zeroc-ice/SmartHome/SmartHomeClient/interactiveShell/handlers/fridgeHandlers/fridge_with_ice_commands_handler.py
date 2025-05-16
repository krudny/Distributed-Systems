import logging

import SmartHome
from interactiveShell.handlers.fridgeHandlers.fridge_commands_handler import FridgeCommandsHandler

logging.basicConfig(level=logging.INFO, format='%(message)s')


class FridgeWithIceCommandsHandler(FridgeCommandsHandler):
    def __init__(self, communicator):
        super().__init__(communicator)
        self.logger = logging.getLogger(__name__)

    def handle(self, command):
        proxy = self._get_fridge_with_ice_proxy(command.device)
        if not proxy:
            self.logger.error("Invalid command. Cannot find device.")
            return True

        action_method = self._get_action_method(command.action)
        if action_method:
            return action_method(proxy, command)

        self.logger.error(f"Unsupported action '{command.action}' for FridgeWithIce.")
        return True

    def _get_fridge_with_ice_proxy(self, device_name):
        device_proxy = self.get_proxy(device_name)
        return SmartHome.FridgeWithIcePrx.checkedCast(device_proxy)

    def _get_action_method(self, action):
        return {
            "getCubesMakerCapacity": self._handle_get_cubes_maker_capacity,
            "getCubesQuantity": self._handle_get_cubes_quantity,
            "makeIceCubes": self._handle_make_ice_cubes,
            "takeIceCubes": self._handle_take_ice_cubes
        }.get(action)

    def _handle_get_cubes_maker_capacity(self, proxy, command):
        proxy.ice_twoway()
        cubes_maker_capacity = proxy.getCubesMakerCapacity()
        self.logger.info(f"The maximum capacity for {command.device} is {cubes_maker_capacity} ice cubes.")
        return True

    def _handle_get_cubes_quantity(self, proxy, command):
        proxy.ice_twoway()
        cubes_maker_quantity = proxy.getCubesQuantity()
        self.logger.info(f"The {command.device} has {cubes_maker_quantity} ice cubes.")
        return True

    def _handle_make_ice_cubes(self, proxy, command):
        proxy.ice_oneway()
        try:
            cubes_to_make_quantity = int(command.arguments[0])
            produced_ice_cubes = proxy.makeIceCubes(cubes_to_make_quantity)
            self.logger.info(f"After production {cubes_to_make_quantity} ice cubes. The {command.device} has"
                             f" {produced_ice_cubes} ice cubes ready to use.")
        except SmartHome.QuantityOfIceCubesMustBeGraterThanZeroException:
            self.logger.error("Quantity of ice cubes must be greater than zero.")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")
        return True

    def _handle_take_ice_cubes(self, proxy, command):
        proxy.ice_oneway()
        try:
            cubes_quantity = int(command.arguments[0])
            given_cubes_quantity = proxy.takeIceCubes(cubes_quantity)
            self.logger.info(f"The {command.device} gave you {given_cubes_quantity} ice cubes.")
        except SmartHome.LackOfIceCubesException:
            self.logger.error("Lack of ice cubes. Make the ice cubes first.")
        except SmartHome.QuantityOfIceCubesMustBeGraterThanZeroException:
            self.logger.error("Quantity of ice cubes must be greater than zero.")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")
        return True
