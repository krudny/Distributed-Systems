import logging

import SmartHome

logging.basicConfig(level=logging.INFO, format='%(message)s')


class DeviceCommandsHandler:
    def __init__(self, communicator):
        self.communicator = communicator
        self.logger = logging.getLogger(__name__)
        self.modes = self._initialize_modes()

    def handle(self, command):
        proxy = self._get_device_proxy(command.device)
        if not proxy:
            self.logger.error("Invalid command. Cannot find device.")
            return True

        action_method = self._get_action_method(command.action)
        if action_method:
            return action_method(proxy, command)

        self.logger.error(f"Unsupported action '{command.action}' for device.")
        return True

    def _initialize_modes(self):
        return {
            "ON": SmartHome.Mode.ON,
            "OFF": SmartHome.Mode.OFF
        }

    def _get_device_proxy(self, device_name):
        device_proxy = self.get_proxy(device_name)
        return SmartHome.DevicePrx.checkedCast(device_proxy)

    def _get_action_method(self, action):
        return {
            "getMode": self._handle_get_mode,
            "setMode": self._handle_set_mode
        }.get(action)

    def _handle_get_mode(self, proxy, command):
        proxy.ice_twoway()
        mode = proxy.getMode()
        self.logger.info(f"The {command.device} is currently in the {mode} mode")
        return True

    def _handle_set_mode(self, proxy, command):
        proxy.ice_oneway()
        mode = command.arguments[0]
        if mode in self.modes:
            try:
                mode_enum = self.modes.get(mode)
                set_mode = proxy.setMode(mode_enum)
                self.logger.info(f"Set {command.device} mode to {set_mode}")
            except SmartHome.ModeNotChangeException:
                self.logger.info("The new mode is the same as the current one, not changing.")
        else:
            self.logger.error("ModuleNotFoundError: Unknown mode value.")
        return True

    def get_proxy(self, device_name):
        proxy = self.communicator.propertyToProxy(device_name + ".Proxy")
        return proxy
