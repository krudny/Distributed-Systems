package smarthome.devices.lightControl;

import SmartHome.DeviceIsOffException;
import SmartHome.LedColour;
import com.zeroc.Ice.Current;

public class LedLamp extends Lamp implements SmartHome.LedLamp {

    private LedColour colour;

    public LedLamp(int brightness, LedColour colour) {
        super(brightness);
        this.colour = colour;
    }

    @Override
    public LedColour setLedColour(LedColour colour, Current current) throws DeviceIsOffException {
        if(isDeviceTurnOFF(current)){
            throw new DeviceIsOffException();
        }
        this.colour = colour;
        return this.colour;
    }
}
