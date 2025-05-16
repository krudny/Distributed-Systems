package smarthome.server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;

import java.util.logging.Logger;

import smarthome.server.servantLocators.ServantLocatorImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SmartHomeServer {
    private static final Logger logger = Logger.getLogger(SmartHomeServer.class.getName());
    private static final String CATEGORY_PREFIX = "Device";
    private static final String ADAPTER_NAME = "adapter";
    private final Communicator communicator;
    private final ObjectAdapter adapter;
    private final ServantLocatorImpl servantLocator;

    public SmartHomeServer(int serverID, String[] iceArgs) {
        communicator = Util.initialize(iceArgs);
        String connectionEndpoints = createConnectionEndpoints(serverID);
        adapter = communicator.createObjectAdapterWithEndpoints(ADAPTER_NAME, connectionEndpoints);
        logger.info(String.format("Adapter with endpoints successfully added %s", connectionEndpoints));

        servantLocator = new ServantLocatorImpl(String.valueOf(serverID));
        adapter.addServantLocator(servantLocator, CATEGORY_PREFIX);
        logger.info("ServantLocator added to adapter.");

        logger.info("Server successfully initialized.");
    }

    private String createConnectionEndpoints(int serverID) {
        String serverIdString = String.valueOf(serverID);
        String portSuffix = serverID < 10 ? "0" + serverIdString : serverIdString;
        return "tcp -h 127.0.0.XX -p 100## : udp -h 127.0.0.XX -p 100##"
                .replace("XX", serverIdString)
                .replace("##", portSuffix);
    }

    public void runServerLoop() {
        adapter.activate();
        boolean quitRequest = false;
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        logger.info("Server processing loop started...");

        while (!quitRequest) {
            String clientRequest;
            try {
                clientRequest = in.readLine();
            } catch (IOException e) {
                logger.severe(String.format("Exception during read client line. %s", e.getMessage()));
                break;
            }

            switch (clientRequest) {
                case "quit":
                    quitRequest = true;
                    handleQuitRequest();
                    break;
                case "devices":
                    servantLocator.printServants();
                    break;
                default:
                    logger.info("Invalid command. Available commands [ devices | quit ]");
            }
        }
        handleExit();
    }

    private void handleQuitRequest(){
        try {
            adapter.deactivate();
            communicator.shutdown();
            communicator.destroy();
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    private void handleExit(){
        logger.info("Server closed.");
        System.exit(1);
    }

}
