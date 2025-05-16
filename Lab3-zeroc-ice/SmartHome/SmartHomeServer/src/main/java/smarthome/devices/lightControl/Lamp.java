package smarthome.devices.lightControl;

import SmartHome.*;
import com.zeroc.Ice.Current;
import smarthome.devices.DeviceImpl;

public class Lamp extends DeviceImpl implements SmartHome.Lamp {
    private int brightness;
    private final LedColour colour = LedColour.WHITE;

    public Lamp(int brightness) {
        this.brightness = brightness;
    }

    @Override
    public int getBrightness(Current current) {
        return brightness;
    }

    @Override
    public int setBrightness(int brightnessPercentage, Current current) throws BrightnessOutOfRangeException, DeviceIsOffException {
        if(isDeviceTurnOFF(current)){
            throw new DeviceIsOffException();
        }
        if(brightnessPercentage < 0 || brightnessPercentage > 100){
            throw new BrightnessOutOfRangeException();
        }
        brightness = brightnessPercentage;
        return brightness;
    }

    @Override
    public LedColour getColour(Current current) {
        return colour;
    }
}
