package com.zybooks.stockinventory.repo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import androidx.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import com.zybooks.stockinventory.model.*;

public class DataBaseHelper extends SQLiteOpenHelper {

    // Static member variables. Not used, but recommend by a free code camp video
    // The link below is to the video that demonstrated CRUD for an android application.
    // It allowed me to create and work with a database for a while. Then my application stopped working.
    // https://www.youtube.com/watch?v=312RhjfetP8&t=2865s
    // I had to include the database context (wrapper) to open and close the database.

    public static final String INVENTORY_TABLE = "Inventory_Table";
    public static final String COLUMN_ID = "_ID";
    public static final String ITEM_ID = "Item_ID";
    public static final String ITEM = "Item";
    public static final String Quantity = "Quantity";

    public static final String USER_TABLE = "User_Table";
    public static final String COL_ID = "_ID";
    public static final String USER = "User";
    public static final String PASS = "Password";


    // Public class constructor
    public DataBaseHelper(Context context, String name) {
        super(context, name, null, 1);
    }

    // Call the first time the database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {

        // SQL query to create the inventory table
        String createInventoryTable = "CREATE TABLE Inventory_Table (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "Item_ID TEXT, Item TEXT, Quantity INT)";

        // Instantiate SQL statement object and compile string
        SQLiteStatement statement = db.compileStatement(createInventoryTable);
        // Execute query statement
        statement.execute();

        // SQL query to create the user table
        String createUserTable = "CREATE TABLE User_Table (_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "User TEXT, Password TEXT)";

        // Instantiate SQL statement object and compile string
        statement = db.compileStatement(createUserTable);
        // Execute query statement
        statement.execute();

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

