import logging

import SmartHome
from interactiveShell.handlers.lightHandlers.lamp_handler import LampCommandsHandler

logging.basicConfig(level=logging.INFO, format='%(message)s')


class LedLampCommandsHandler(LampCommandsHandler):
    def __init__(self, communicator):
        super().__init__(communicator)
        self.logger = logging.getLogger(__name__)
        self.colours = self._initialize_colours()

    def handle(self, command):
        proxy = self._get_led_lamp_proxy(command.device)
        if not proxy:
            self.logger.error("Invalid command. Cannot find device.")
            return True

        if command.action == "setLedColour":
            return self._handle_set_led_colour(proxy, command)

        self.logger.error(f"Unsupported action '{command.action}' for LedLamp.")
        return True

    def _initialize_colours(self):
        return {
            "WHITE": SmartHome.LedColour.WHITE,
            "YELLOW": SmartHome.LedColour.YELLOW,
            "RED": SmartHome.LedColour.RED,
            "GREEN": SmartHome.LedColour.GREEN,
            "BLUE": SmartHome.LedColour.BLUE,
            "ORANGE": SmartHome.LedColour.ORANGE,
            "PINK": SmartHome.LedColour.PINK
        }

    def _get_led_lamp_proxy(self, device_name):
        device_proxy = self.get_proxy(device_name)
        return SmartHome.LedLampPrx.checkedCast(device_proxy)

    def _handle_set_led_colour(self, proxy, command):
        proxy.ice_oneway()
        requested_colour = command.arguments[0]

        if requested_colour not in self.colours:
            self.logger.error("ColourNotFoundError: Unknown colour value.")
            return True

        try:
            colour_enum = self.colours[requested_colour]
            set_colour = proxy.setLedColour(colour_enum)
            self.logger.info(f"Set {command.device} colour to {set_colour}.")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")

        return True
