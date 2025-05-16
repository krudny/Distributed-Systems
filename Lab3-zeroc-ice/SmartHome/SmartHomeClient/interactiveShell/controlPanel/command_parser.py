import logging

from interactiveShell.handlers.control_panel_commands_handler import ControlPanelHandler
from interactiveShell.handlers.device_commands_handler import DeviceCommandsHandler
from interactiveShell.handlers.lightHandlers.lamp_handler import LampCommandsHandler
from interactiveShell.handlers.lightHandlers.led_lamp_handler import LedLampCommandsHandler
from interactiveShell.handlers.temperatureHandlers.fridge_with_ice_commands_handler import \
    FridgeWithIceCommandsHandler
from interactiveShell.handlers.temperatureHandlers.fridge_with_shopping_list_commands_handler import \
    FridgeWithShoppingListCommandsHandler
from interactiveShell.handlers.temperatureHandlers.temperature_sensor_commands_handler import \
    TemperatureSensorCommandsHandler

logging.basicConfig(level=logging.INFO, format='%(message)s')


class CommandParser:
    def __init__(self, communicator):
        self.logger = logging.getLogger(__name__)
        self.command_handlers = {
            "getMode": DeviceCommandsHandler(communicator),
            "setMode": DeviceCommandsHandler(communicator),

            "getBrightness": LampCommandsHandler(communicator),
            "getColour": LampCommandsHandler(communicator),
            "setBrightness": LampCommandsHandler(communicator),

            "setLedColour": LedLampCommandsHandler(communicator),

            "getTemperature": TemperatureSensorCommandsHandler(communicator),
            "setTemperature": TemperatureSensorCommandsHandler(communicator),
            "getTemperatureSensorLimits": TemperatureSensorCommandsHandler(communicator),
            "setTemperatureLimits": TemperatureSensorCommandsHandler(communicator),

            "getCubesMakerCapacity": FridgeWithIceCommandsHandler(communicator),
            "getCubesQuantity": FridgeWithIceCommandsHandler(communicator),
            "takeIceCubes": FridgeWithIceCommandsHandler(communicator),
            "makeIceCubes": FridgeWithIceCommandsHandler(communicator),

            "getShoppingList": FridgeWithShoppingListCommandsHandler(communicator),
            "addShoppingListProductRecord": FridgeWithShoppingListCommandsHandler(communicator),
            "removeShoppingListProductRecord": FridgeWithShoppingListCommandsHandler(communicator),
            "clearShoppingList": FridgeWithShoppingListCommandsHandler(communicator),

            "exitControlPanel": ControlPanelHandler(communicator),
        }

    def parse_command(self, command):
        if command:
            handler = self.command_handlers.get(command.action)
            if handler:
                return handler.handle(command)
            else:
                self.logger.error("Invalid command: SmartHome functionality not found.")
        else:
            self.logger.error("Exception: command not found.")
        return True
