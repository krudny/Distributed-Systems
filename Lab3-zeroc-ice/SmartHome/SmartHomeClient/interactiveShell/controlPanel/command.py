class Command:
    def __init__(self, full_command):
        split_command = full_command.split(" ")
        if split_command[0] == "quit":
            self.action = "exitControlPanel"
        else:
            self.device = split_command[0]
            self.action = split_command[1]
            self.arguments = split_command[2:]
