package smarthome.devices;

import SmartHome.Device;
import SmartHome.Mode;
import SmartHome.ModeNotChangeException;
import com.zeroc.Ice.Current;

public class DeviceImpl implements Device {
    private Mode mode = Mode.ON;

    @Override
    public Mode getMode(Current current) {
        return mode;
    }

    @Override
    public Mode setMode(Mode mode, Current current) throws ModeNotChangeException {
        if (mode == this.mode) {
            throw new ModeNotChangeException();
        }
        this.mode = mode;
        return mode;
    }

    @Override
    public boolean isDeviceTurnOFF(Current current){
        return this.mode == Mode.OFF;
    }
}
