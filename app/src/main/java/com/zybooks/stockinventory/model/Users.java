package com.zybooks.stockinventory.model;

// Class to instantiate user object instance
public class Users {
    // Primary Key For Table
    public long _ID;

    // UserName
    public String mUserName;

    // PassWord Column
    public String mPassWord;

    // Public setter member methods
    public void setUserName(String t_Username) {
        this.mUserName = t_Username;
    }

    public void setPassWord(String t_PassWord) {
        this.mPassWord = t_PassWord;
    }

    public void set_ID(long t_ID) {this._ID = t_ID;}

    // Public getter member methods
    public String getUserName() {
        return mUserName;
    }

    public String getPassWord() {
        return mPassWord;
    }

    public long getID() {return _ID;}
}
