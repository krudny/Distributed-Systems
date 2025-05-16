import logging

import SmartHome
from interactiveShell.handlers.device_commands_handler import DeviceCommandsHandler

logging.basicConfig(level=logging.INFO, format='%(message)s')


class LampCommandsHandler(DeviceCommandsHandler):
    def __init__(self, communicator):
        super().__init__(communicator)
        self.logger = logging.getLogger(__name__)

    def handle(self, command):
        proxy = self._get_lamp_proxy(command.device)
        if not proxy:
            self.logger.error("Invalid command. Cannot find device.")
            return True

        action_method = self._get_action_method(command.action)
        if action_method:
            return action_method(proxy, command)

        self.logger.error(f"Unsupported action '{command.action}' for Lamp.")
        return True

    def _get_lamp_proxy(self, device_name):
        device_proxy = self.get_proxy(device_name)
        return SmartHome.LampPrx.checkedCast(device_proxy)

    def _get_action_method(self, action):
        return {
            "getBrightness": self._handle_get_brightness,
            "setBrightness": self._handle_set_brightness,
            "getColour": self._handle_get_colour,
        }.get(action)

    def _handle_get_brightness(self, proxy, command):
        proxy.ice_twoway()
        brightness = proxy.getBrightness()
        self.logger.info(f"The {command.device} has brightness set on {brightness}%")
        return True

    def _handle_set_brightness(self, proxy, command):
        proxy.ice_oneway()
        try:
            brightness = int(command.arguments[0])
            set_brightness = proxy.setBrightness(brightness)
            self.logger.info(f"Set {command.device} brightness to {set_brightness}%")
        except SmartHome.BrightnessOutOfRangeException:
            self.logger.error("BrightnessOutOfRangeException: The range for brightness is from 0 to 100%")
        except SmartHome.DeviceIsOffException:
            self.logger.error("DeviceIsOffException: First, turn on the device.")
        return True

    def _handle_get_colour(self, proxy, command):
        proxy.ice_twoway()
        colour = proxy.getColour()
        self.logger.info(f"The {command.device} has {colour} colour set.")
        return True

