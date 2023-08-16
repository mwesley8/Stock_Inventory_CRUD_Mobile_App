package com.zybooks.stockinventory;

import androidx.appcompat.app.AppCompatActivity;
import com.zybooks.stockinventory.model.*;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zybooks.stockinventory.repo.DataBaseHelper;

import java.util.List;

// Activity to update item in inventory
public class UpdateActivity extends AppCompatActivity {

    // Class member variables
    private static final String PHONE_NUMBER = "5555555555";

    private boolean SEND_TEXT = false;
    private EditText mItemIDView;

    private EditText mItemView;

    private EditText mQuantityView;

    private SQLiteDatabase mDatabase;

    private static final String FILE = "StockInventory.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        // Bundle instance to retrieve information sent from the inventory activity
        Bundle extras = getIntent().getExtras();

        // Declare and assign local variables to the values from the inventory activity
        String itemID = extras.getString("itemID");
        String item = extras.getString("item");
        String quantity = extras.getString("quantity");
        SEND_TEXT = extras.getBoolean("sendText");

        // Find the resource location of the edit text fields
        mItemIDView = findViewById(R.id.update_item_name_edit_text);
        mItemView = findViewById((R.id.update_edit_item));
        mQuantityView = findViewById(R.id.update_quantity_edit_text);

        // Set the edit text fields to the values passed from the inventory activity
        mItemIDView.setText(itemID);
        mItemView.setText(item);
        mQuantityView.setText(quantity);
    }
    @Override public void onBackPressed () { moveTaskToBack (true); }

    // Method call when the user clicks the cancel button
    public void updateCancelOnClick(View view) {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }


    // Method call when the user clicks the update button
    public void updateItemSubmitButton(View view) {

        // Instantiate inventory object
        Inventory item = new Inventory();

        // Instantiate Data Base Helper object instance
        DataBaseHelper db = new DataBaseHelper(getApplicationContext(), FILE);
        // Open the database as a writable.
        mDatabase = db.getWritableDatabase();

        // Retrieve the resource location of the edit text fields
        mItemIDView   = findViewById(R.id.update_item_name_edit_text);
        mItemView     = findViewById(R.id.update_edit_item);
        mQuantityView = findViewById(R.id.update_quantity_edit_text);

        // Retrieve the user input from the text fields
        String curItemID    = mItemIDView.getText().toString();
        String curItem      = mItemView.getText().toString();
        String curQuantity = mQuantityView.getText().toString();

        // Condition to make sure the user is not updating with null values
        if (!curItemID.isEmpty() && !curItem.isEmpty() && !curQuantity.isEmpty()) {
            item.setItemID(curItemID);
            item.setItem(curItem);
            item.setQuantity(Integer.parseInt(curQuantity));

            // Query string and execute to update an item in the inventory
            String queryString = "UPDATE Inventory_Table SET Quantity = ?, Item = ? WHERE Item_ID = ?";
            SQLiteStatement statement = mDatabase.compileStatement(queryString);
            statement.bindLong(1, item.getQuantity());
            statement.bindString(2, item.getItem());
            statement.bindString(3, item.getItemID());
            statement.execute();
            mDatabase.close();

            // Message to user when the item was add to the list
            Toast.makeText(UpdateActivity.this, "Item Updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);

            // Task still executed when the user leaves the activity
            // The activities' global variable is changed by the intent passed from the inventory
            // activity. That value is updated during the permissions request. Additional functionality
            // could be added to store user's response. New changes prevent an application from asking
            // for permission multiple times
            if (SEND_TEXT && item.getQuantity() < 10) {
                // Text message to send when the inventory item updated is below acceptable levels
                // Additional functionality could be added to the database or file in the form of an array\
                // The current specifications do not require storing when a message has been sent.
                String message = curItem + " is below acceptable levels. Please order more " + curItem;

                // Method call to send method
                sendSMS(PHONE_NUMBER, message);
            }
        }
        else {
            Toast.makeText(UpdateActivity.this, "Item Not Updated", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
        }
    }
    // Boiler plate code provided below to send an SMS.
    // I do not know how to test the functionality after this point.
    // Compliments of: https://stackoverflow.com/questions/8578689/sending-text-messages-programmatically-in-android
    private void sendSMS(String PHONE_NUMBER, String message)
    {
        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), PendingIntent.FLAG_IMMUTABLE);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), PendingIntent.FLAG_IMMUTABLE);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));

        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(PHONE_NUMBER, null, message, sentPI, deliveredPI);
    }
}