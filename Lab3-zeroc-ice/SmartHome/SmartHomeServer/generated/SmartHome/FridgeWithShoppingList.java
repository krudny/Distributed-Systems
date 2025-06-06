//
// Copyright (c) ZeroC, Inc. All rights reserved.
//
//
// Ice version 3.7.10
//
// <auto-generated>
//
// Generated from file `server.ice'
//
// Warning: do not edit this file.
//
// </auto-generated>
//

package SmartHome;

public interface FridgeWithShoppingList extends Fridge
{
    ShoppingListProductRecord[] getShoppingList(com.zeroc.Ice.Current current)
        throws DeviceIsOffException;

    ShoppingListProductRecord addShoppingListProductRecord(Product Pproduct, com.zeroc.Ice.Current current)
        throws DeviceIsOffException;

    ShoppingListProductRecord removeShoppingListProductRecord(int idx, com.zeroc.Ice.Current current)
        throws DeviceIsOffException,
               IndexOutOfRangeException;

    ShoppingListProductRecord[] clearShoppingList(com.zeroc.Ice.Current current)
        throws DeviceIsOffException;

    /** @hidden */
    static final String[] _iceIds =
    {
        "::Ice::Object",
        "::SmartHome::Device",
        "::SmartHome::Fridge",
        "::SmartHome::FridgeWithShoppingList",
        "::SmartHome::TemperatureSensor"
    };

    @Override
    default String[] ice_ids(com.zeroc.Ice.Current current)
    {
        return _iceIds;
    }

    @Override
    default String ice_id(com.zeroc.Ice.Current current)
    {
        return ice_staticId();
    }

    static String ice_staticId()
    {
        return "::SmartHome::FridgeWithShoppingList";
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_getShoppingList(FridgeWithShoppingList obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(com.zeroc.Ice.OperationMode.Idempotent, current.mode);
        inS.readEmptyParams();
        ShoppingListProductRecord[] ret = obj.getShoppingList(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ShoppingListHelper.write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_addShoppingListProductRecord(FridgeWithShoppingList obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        Product iceP_Pproduct;
        iceP_Pproduct = Product.ice_read(istr);
        inS.endReadParams();
        ShoppingListProductRecord ret = obj.addShoppingListProductRecord(iceP_Pproduct, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ShoppingListProductRecord.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_removeShoppingListProductRecord(FridgeWithShoppingList obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        com.zeroc.Ice.InputStream istr = inS.startReadParams();
        int iceP_idx;
        iceP_idx = istr.readInt();
        inS.endReadParams();
        ShoppingListProductRecord ret = obj.removeShoppingListProductRecord(iceP_idx, current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ShoppingListProductRecord.ice_write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /**
     * @hidden
     * @param obj -
     * @param inS -
     * @param current -
     * @return -
     * @throws com.zeroc.Ice.UserException -
    **/
    static java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceD_clearShoppingList(FridgeWithShoppingList obj, final com.zeroc.IceInternal.Incoming inS, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        com.zeroc.Ice.Object._iceCheckMode(null, current.mode);
        inS.readEmptyParams();
        ShoppingListProductRecord[] ret = obj.clearShoppingList(current);
        com.zeroc.Ice.OutputStream ostr = inS.startWriteParams();
        ShoppingListHelper.write(ostr, ret);
        inS.endWriteParams(ostr);
        return inS.setResult(ostr);
    }

    /** @hidden */
    final static String[] _iceOps =
    {
        "addShoppingListProductRecord",
        "clearShoppingList",
        "getMode",
        "getShoppingList",
        "getTemperature",
        "getTemperatureSensorLimits",
        "ice_id",
        "ice_ids",
        "ice_isA",
        "ice_ping",
        "isDeviceTurnOFF",
        "removeShoppingListProductRecord",
        "setMode",
        "setTemperature",
        "setTemperatureLimits"
    };

    /** @hidden */
    @Override
    default java.util.concurrent.CompletionStage<com.zeroc.Ice.OutputStream> _iceDispatch(com.zeroc.IceInternal.Incoming in, com.zeroc.Ice.Current current)
        throws com.zeroc.Ice.UserException
    {
        int pos = java.util.Arrays.binarySearch(_iceOps, current.operation);
        if(pos < 0)
        {
            throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
        }

        switch(pos)
        {
            case 0:
            {
                return _iceD_addShoppingListProductRecord(this, in, current);
            }
            case 1:
            {
                return _iceD_clearShoppingList(this, in, current);
            }
            case 2:
            {
                return Device._iceD_getMode(this, in, current);
            }
            case 3:
            {
                return _iceD_getShoppingList(this, in, current);
            }
            case 4:
            {
                return TemperatureSensor._iceD_getTemperature(this, in, current);
            }
            case 5:
            {
                return TemperatureSensor._iceD_getTemperatureSensorLimits(this, in, current);
            }
            case 6:
            {
                return com.zeroc.Ice.Object._iceD_ice_id(this, in, current);
            }
            case 7:
            {
                return com.zeroc.Ice.Object._iceD_ice_ids(this, in, current);
            }
            case 8:
            {
                return com.zeroc.Ice.Object._iceD_ice_isA(this, in, current);
            }
            case 9:
            {
                return com.zeroc.Ice.Object._iceD_ice_ping(this, in, current);
            }
            case 10:
            {
                return Device._iceD_isDeviceTurnOFF(this, in, current);
            }
            case 11:
            {
                return _iceD_removeShoppingListProductRecord(this, in, current);
            }
            case 12:
            {
                return Device._iceD_setMode(this, in, current);
            }
            case 13:
            {
                return TemperatureSensor._iceD_setTemperature(this, in, current);
            }
            case 14:
            {
                return TemperatureSensor._iceD_setTemperatureLimits(this, in, current);
            }
        }

        assert(false);
        throw new com.zeroc.Ice.OperationNotExistException(current.id, current.facet, current.operation);
    }
}
