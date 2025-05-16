import logging

import SmartHome
from interactiveShell.handlers.device_commands_handler import DeviceCommandsHandler

logging.basicConfig(level=logging.INFO, format='%(message)s')


class TemperatureSensorCommandsHandler(DeviceCommandsHandler):
    def __init__(self, communicator):
        super().__init__(communicator)
        self.logger = logging.getLogger(__name__)

    def handle(self, command):
        proxy = self._get_temperature_sensor_proxy(command.device)
        if not proxy:
            self.logger.error("Invalid command. Cannot find device.")
            return True

        action_method = self._get_action_method(command.action)
        if action_method:
            return action_method(proxy, command)

        self.logger.error(f"Unsupported action '{command.action}' for TemperatureSensor.")
        return True

    def _get_temperature_sensor_proxy(self, device_name):
        device_proxy = self.get_proxy(device_name)
        return SmartHome.TemperatureSensorPrx.checkedCast(device_proxy)

    def _get_action_method(self, action):
        return {
            "getTemperature": self._handle_get_temperature,
            "setTemperature": self._handle_set_temperature,
            "getTemperatureSensorLimits": self._handle_get_temperature_sensor_limits,
            "setTemperatureLimits": self._handle_set_temperature_limits
        }.get(action)

    def _handle_get_temperature(self, proxy, command):
        proxy.ice_twoway()
        temperature = proxy.getTemperature()
        self.logger.info(f"The {command.device} has the {temperature} C")
        return True

    def _handle_set_temperature(self, proxy, command):
        proxy.ice_oneway()
        temperature = command.arguments[0]
        try:
            set_temperature = proxy.setTemperature(float(temperature))
            self.logger.info(f"Set {command.device} temperature to {set_temperature} C")
        except SmartHome.TemperatureOutOfLimitException:
            self.logger.error("TemperatureOutOfLimitException: The temperature exceeded the set limit.")
        return True

    def _handle_get_temperature_sensor_limits(self, proxy, command):
        proxy.ice_twoway()
        temperature_limits = proxy.getTemperatureSensorLimits()
        self.logger.info(f"The {command.device} temperature limits are set to {temperature_limits}")
        return True

    def _handle_set_temperature_limits(self, proxy, command):
        proxy.ice_oneway()
        temperature_lower_limit = command.arguments[0]
        temperature_upper_limit = command.arguments[1]
        temperature_limits = SmartHome.TemperatureLimits(float(temperature_lower_limit), float(temperature_upper_limit))
        try:
            proxy.setTemperatureLimits(temperature_limits)
            self.logger.info(f"Set {command.device} temperature limits. Lower limit: {temperature_lower_limit} Upper limit: {temperature_upper_limit}")
        except SmartHome.TemperatureLowerLimitIsGreaterThanUpperLimitException:
            self.logger.error("Temperature lower limit cannot be greater than upper limit.")
        except SmartHome.TemperatureOutOfSupportedScaleException:
            self.logger.error("Temperature limits cannot exceed the sensor's supported scale. Supported scale: "
                              "Lower limit: -30 C, Upper limit: 40 C")
        return True
