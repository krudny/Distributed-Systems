package smarthome.parser;

import lombok.extern.java.Log;

import java.util.Arrays;

@Log
public class ArgsParser {

    public static ParsedArgs parse(String[] args) {
        if (args.length < 1) {
            log.severe("IllegalArgument: Expected at least one argument - ServerID");
            return null;
        }

        try {
            int serverID = Integer.parseInt(args[0]);
            if (serverID < 0 || serverID > 99) {
                log.severe("IllegalArgument: ServerID must be in range [0, 99]");
                return null;
            }
            String[] iceArgs = Arrays.copyOfRange(args, 1, args.length);
            return new ParsedArgs(serverID, iceArgs);

        } catch (NumberFormatException e) {
            log.severe("IllegalArgument: ServerID must be a number.");
            return null;
        }
    }
}