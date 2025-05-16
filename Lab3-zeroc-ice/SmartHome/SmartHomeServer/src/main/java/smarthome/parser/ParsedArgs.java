package smarthome.parser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ParsedArgs {
    private final int serverID;
    private final String[] iceArgs;
}