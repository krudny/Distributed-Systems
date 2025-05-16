import Ice
import sys
import logging

from interactiveShell.controlPanel.control_panel import ControlPanel

logging.basicConfig(level=logging.INFO, format='%(message)s')

def add_client_config_to_args(args):
    return args + ["--Ice.Config=config.client"]

def main(args):
    args_with_client_config = add_client_config_to_args(args)
    try:
        with Ice.initialize(args_with_client_config) as communicator:
            control_panel = ControlPanel(communicator)
            control_panel.run_client_loop()
    except Exception as e:
        logging.error("Exception: " + str(e))
        return 1
    return 0


if __name__ == "__main__":
    sys.exit(main(sys.argv))
