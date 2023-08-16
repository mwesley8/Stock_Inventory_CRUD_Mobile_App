package com.zybooks.stockinventory.model;

// Declare class to instantiate item object instances
public class Inventory {

    // Class member variables
    public long _mID;

    // Item ID column

    public String mItemID;
    // Item column

    public String mItem;

    // Quantity Column
    public int mQuantity;

    // Public setter member methods
    public void setItemID(String t_ItemID) {
        this.mItemID = t_ItemID;
    }

    public void setItem(String t_Item) {
        this.mItem = t_Item;
    }

    public void setID(long t_ID) {this._mID = t_ID;}

    public void setQuantity(int t_Quantity) {
        this.mQuantity= t_Quantity;
    }

    // Public getter member methods
    public String getItemID() {
        return mItemID;
    }

    public String getItem() {
        return mItem;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public long getID() {return _mID;}
}