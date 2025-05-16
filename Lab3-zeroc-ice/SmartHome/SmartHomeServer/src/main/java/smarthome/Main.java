package smarthome;

import lombok.extern.java.Log;
import smarthome.parser.ArgsParser;
import smarthome.parser.ParsedArgs;
import smarthome.server.SmartHomeServer;


@Log
public class Main {
    public static void main(String[] args) {
        ParsedArgs parsedArgs = ArgsParser.parse(args);
        if (parsedArgs != null) {
            startServer(parsedArgs.getServerID(), parsedArgs.getIceArgs());
        }
    }

    private static void startServer(int serverID, String[] iceArgs) {
        log.info("Server starting with ServerID: " + serverID);
        SmartHomeServer server = new SmartHomeServer(serverID, iceArgs);
        server.runServerLoop();
    }
}
