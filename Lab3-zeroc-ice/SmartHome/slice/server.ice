#ifndef SMARTHOME
#define SMARTHOME

module SmartHome
{
  // Exceptions
  exception DeviceNotFoundException {};
  exception DeviceIsOffException {};
  exception ModeNotChangeException {};
  exception IndexOutOfRangeException {};
  exception BrightnessOutOfRangeException {};
  exception TemperatureOutOfSupportedScaleException {};
  exception TemperatureOutOfLimitException {};
  exception TemperatureLowerLimitIsGreaterThanUpperLimitException {};
  exception LackOfIceCubesException{};
  exception QuantityOfIceCubesMustBeGreaterThanZeroException{};

  // Enums
  enum Mode { ON, OFF };
  enum LedColour { WHITE, YELLOW, RED, GREEN, BLUE, ORANGE, PINK };
  enum ProductUnit { ml, l, g, kg, notSpecified };

  // Structs
  struct TemperatureLimits {
      float lowerLimit;
      float upperLimit;
  };

  struct Product {
      string name;
      ProductUnit unit;
      int quantity;
  };

  struct ShoppingListProductRecord {
      int id;
      Product product;
  };

  sequence<ShoppingListProductRecord> ShoppingList;

  // Interfaces
  interface Device {
      idempotent Mode getMode();
      idempotent Mode setMode(Mode mode) throws ModeNotChangeException;
      idempotent bool isDeviceTurnOFF();
  };

  interface Lamp extends Device {
      idempotent int getBrightness();
      idempotent LedColour getColour();
      idempotent int setBrightness(int brightnessPercentage) throws BrightnessOutOfRangeException, DeviceIsOffException;
  };

  interface LedLamp extends Lamp {
      idempotent LedColour setLedColour(LedColour colour) throws DeviceIsOffException;
  };

  interface TemperatureSensor extends Device {
      idempotent float getTemperature();
      idempotent float setTemperature(float temperature) throws TemperatureOutOfLimitException;
      idempotent TemperatureLimits getTemperatureSensorLimits();
      idempotent TemperatureLimits setTemperatureLimits(TemperatureLimits temperatureLimits) throws TemperatureOutOfSupportedScaleException, TemperatureLowerLimitIsGreaterThanUpperLimitException;
  };

  interface Fridge extends TemperatureSensor {
  };

  interface FridgeWithShoppingList extends Fridge {
      idempotent ShoppingList getShoppingList() throws DeviceIsOffException;
      ShoppingListProductRecord addShoppingListProductRecord(Product product) throws DeviceIsOffException;
      ShoppingListProductRecord removeShoppingListProductRecord(int idx) throws IndexOutOfRangeException, DeviceIsOffException;
      ShoppingList clearShoppingList() throws DeviceIsOffException;
  };

  interface FridgeWithIce extends Fridge {
      idempotent int getCubesMakerCapacity();
      idempotent int getCubesQuantity() throws DeviceIsOffException;
      int takeIceCubes(int cubesQuantity) throws LackOfIceCubesException, QuantityOfIceCubesMustBeGreaterThanZeroException, DeviceIsOffException;
      int makeIceCubes(int cubesQuantity) throws QuantityOfIceCubesMustBeGreaterThanZeroException, DeviceIsOffException;
  };
};

#endif
