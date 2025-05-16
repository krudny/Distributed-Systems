package smarthome.devices.temperatureControl;

import SmartHome.*;
import com.zeroc.Ice.Current;
import smarthome.devices.DeviceImpl;

public class TemperatureSensor extends DeviceImpl implements SmartHome.TemperatureSensor {
    private final TemperatureLimits maximumSupportedLimitsBySensor;
    private TemperatureLimits temperatureLimits;
    private float temperature;

    public TemperatureSensor(TemperatureLimits temperatureLimits, float temperature) {
        this.maximumSupportedLimitsBySensor = new TemperatureLimits(-30.0F, 40.0F);
        this.temperatureLimits = temperatureLimits;
        this.temperature = temperature;
    }

    @Override
    public float getTemperature(Current current) {
        return temperature;
    }

    @Override
    public float setTemperature(float temperature, Current current) throws TemperatureOutOfLimitException {
        if(!isTemperatureWithinLimits(temperature)){
            throw new TemperatureOutOfLimitException();
        }
        this.temperature = temperature;
        return this.temperature;
    }

    @Override
    public TemperatureLimits getTemperatureSensorLimits(Current current) {
        return temperatureLimits;
    }

    @Override
    public TemperatureLimits setTemperatureLimits(TemperatureLimits limits, Current current)
            throws TemperatureOutOfSupportedScaleException, TemperatureLowerLimitIsGreaterThanUpperLimitException {
        if(!isUpperLimitIsGreaterThanLowerLimit(limits)){
            throw new TemperatureLowerLimitIsGreaterThanUpperLimitException();
        }
        if(!isSensorCanHandleLimits(limits)){
            throw new TemperatureOutOfSupportedScaleException();
        }
        this.temperatureLimits = limits;
        return this.temperatureLimits;
    }

    private boolean isTemperatureWithinLimits(float temperature) {
        return (temperature >= temperatureLimits.lowerLimit && temperature <= temperatureLimits.upperLimit);
    }

    private boolean isUpperLimitIsGreaterThanLowerLimit(TemperatureLimits limits){
        return limits.lowerLimit <= limits.upperLimit;
    }

    private boolean isSensorCanHandleLimits(TemperatureLimits limits){
        return (limits.upperLimit <= this.maximumSupportedLimitsBySensor.upperLimit &&
                limits.lowerLimit >= this.maximumSupportedLimitsBySensor.lowerLimit);
    }
}
