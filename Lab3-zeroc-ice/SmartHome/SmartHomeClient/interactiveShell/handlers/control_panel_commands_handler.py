import logging

logging.basicConfig(level=logging.INFO, format='%(message)s')


class ControlPanelHandler:
    def __init__(self, communicator):
        self.communicator = communicator
        self.logger = logging.getLogger(__name__)

    def handle(self, command):
        if command.action == "exitControlPanel":
            self.logger.info("Client control panel stopping.")
            self.communicator.waitForShutdown()
            self.communicator.destroy()
            return False
        return True
