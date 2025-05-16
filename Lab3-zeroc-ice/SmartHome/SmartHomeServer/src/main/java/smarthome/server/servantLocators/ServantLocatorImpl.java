package smarthome.server.servantLocators;

import SmartHome.Device;
import SmartHome.LedColour;
import SmartHome.TemperatureLimits;
import com.zeroc.Ice.*;
import com.zeroc.Ice.Object;
import smarthome.devices.lightControl.Lamp;
import smarthome.devices.lightControl.LedLamp;
import smarthome.devices.fridgeControl.FridgeWithIce;
import smarthome.devices.fridgeControl.Fridge;
import smarthome.devices.fridgeControl.FridgeWithShoppingList;
import smarthome.devices.temperatureControl.TemperatureSensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;

public class ServantLocatorImpl implements ServantLocator {

    private static final Logger logger = Logger.getLogger(ServantLocatorImpl.class.getName());
    private final List<String> servantNames = new ArrayList<>();
    private final String serverId;

    public ServantLocatorImpl(String serverId) {
        this.serverId = serverId;
    }

    @Override
    public LocateResult locate(Current current) throws IllegalArgumentException {
        String[] servantParts = current.id.name.split("-", 2);
        String servantName = servantParts[0];
        String servantServerId = servantParts[1];
        ObjectAdapter adapter = current.adapter;

        if (isServantShouldBeLocatedByServer(servantServerId)) {
            Device servant = findExistingServant(adapter, servantName);
            if (servant == null) {
                servantNames.add(Arrays.toString(servantParts));
                servant = createNewServant(servantName);
                adapter.add(servant, new Identity(servantName, servantName));
            }
            return new LocateResult(servant, null);
        }
        throw new IllegalArgumentException("Invalid servant name: " + servantName);
    }

    private boolean isServantShouldBeLocatedByServer(String servantServerId) {
        return servantServerId.equals(serverId);
    }

    private Device findExistingServant(ObjectAdapter adapter, String servantName) {
        return (Device) adapter.find(new Identity(servantName, servantName));
    }

    private Device createNewServant(String servantName) throws IllegalStateException {
        return switch (servantName) {
            case "Fridge" -> new Fridge(3);
            case "FridgeWithIce" -> new FridgeWithIce(-3);
            case "FridgeWithShoppingList" -> new FridgeWithShoppingList(4);
            case "TemperatureSensor" -> new TemperatureSensor(new TemperatureLimits(-0.0F, 30.0F), 22.5F);
            case "Lamp" -> new Lamp(50);
            case "LedLamp" -> new LedLamp(100, LedColour.YELLOW);
            default -> throw new IllegalStateException("Unexpected value: " + servantName);
        };
    }


    @Override
    public void finished(Current current, Object object, java.lang.Object o) {
        logger.info(String.format("Request finished for servant: %s", current.id.name));
    }

    @Override
    public void deactivate(String s) {
        servantNames.clear();
        logger.info("Temperature servant locator deactivated.");
    }

    public void printServants() {
        if (servantNames.isEmpty()) {
            logger.info("Zero devices connected to the server.");
        } else {
            logger.info("Devices connected to the server:");
            for (String name : servantNames) {
                logger.info(String.format("- %s", name));
            }
        }
    }
}
