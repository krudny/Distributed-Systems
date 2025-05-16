package smarthome.devices.fridgeControl;

import SmartHome.*;
import smarthome.devices.temperatureControl.TemperatureSensor;

public class Fridge extends TemperatureSensor implements SmartHome.Fridge {

    public Fridge(float temperature ) {
        super(new TemperatureLimits(-10.0F, 10.0F), temperature);
    }

}
