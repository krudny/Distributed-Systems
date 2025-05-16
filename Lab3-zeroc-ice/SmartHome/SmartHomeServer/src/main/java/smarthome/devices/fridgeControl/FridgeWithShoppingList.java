package smarthome.devices.fridgeControl;

import SmartHome.*;
import com.zeroc.Ice.Current;

import java.util.ArrayList;
import java.util.List;

public class FridgeWithShoppingList extends Fridge implements SmartHome.FridgeWithShoppingList {

    private List<ShoppingListProductRecord> shoppingList;

    public FridgeWithShoppingList(float temperature) {
        super(temperature);
        this.shoppingList = new ArrayList<ShoppingListProductRecord>();
    }

    @Override
    public ShoppingListProductRecord[] getShoppingList(Current current) throws DeviceIsOffException {
        if(isDeviceTurnOFF(current)){
            throw new DeviceIsOffException();
        }
        return getShoppingList();
    }

    @Override
    public ShoppingListProductRecord addShoppingListProductRecord(Product product, Current current) throws DeviceIsOffException {
        if(isDeviceTurnOFF(current)){
            throw new DeviceIsOffException();
        }
        ShoppingListProductRecord productRecord = new ShoppingListProductRecord(shoppingList.size(), product);
        shoppingList.add(productRecord);
        return productRecord;
    }

    @Override
    public ShoppingListProductRecord removeShoppingListProductRecord(int idx, Current current) throws DeviceIsOffException, IndexOutOfRangeException {
        if(isDeviceTurnOFF(current)){
            throw new DeviceIsOffException();
        }
        if(idx < 0 || idx >= shoppingList.size()){
            throw new IndexOutOfRangeException();
        }
        ShoppingListProductRecord productRecord = shoppingList.get(idx);
        shoppingList.remove(idx);
        return productRecord;
    }

    @Override
    public ShoppingListProductRecord[] clearShoppingList(Current current) throws DeviceIsOffException {
        if(isDeviceTurnOFF(current)){
            throw new DeviceIsOffException();
        }
        shoppingList = new ArrayList<>();
        return getShoppingList();
    }

    private ShoppingListProductRecord[] getShoppingList() {
        return shoppingList.stream().map(productRecord -> new ShoppingListProductRecord(productRecord.id, productRecord.product))
                .toArray(ShoppingListProductRecord[]::new);
    }
}
