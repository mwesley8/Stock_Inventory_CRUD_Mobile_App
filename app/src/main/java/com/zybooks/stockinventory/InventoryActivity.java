package com.zybooks.stockinventory;

import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.Manifest.permission.SEND_SMS;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.zybooks.stockinventory.model.*;
import com.zybooks.stockinventory.repo.*;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

// Special thanks too:
// https://stackoverflow.com/questions/48862542/how-to-use-button-and-recycleview-together

// Solved the external database problem.
// https://medium.com/@mostasim/read-external-database-with-sqliteopenhelper-on-android-e87d66891f4e

// Activity to facilitate CRUD functionality
public class InventoryActivity extends AppCompatActivity {

    // Class member variables
    private boolean          SEND_TEXT = false;

    private final int        REQUEST_WRITE_CODE = 1;
    private InventoryAdapter mInventoryAdapter;
    private RecyclerView     mRecyclerView;

    public DataBaseHelper    mDatabaseHelper;

    public SQLiteDatabase    mSQLDatabase;

    // File path to database.
    private static final String FILE = "StockInventory.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        // Get the recycle view resource location
        mRecyclerView = findViewById(R.id.inventory_recycler_view);

        // Instantiate a new Recycler View object instance assigned to a Grid layout with 1 card
        // in each row
        RecyclerView.LayoutManager gridLayoutManager =
        new GridLayoutManager(getApplicationContext(), 1);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        // Show the Inventory
        updateUI(getInventory());
    }

    //@Override
    protected void onResume() {
        super.onResume();

        List<Inventory> items;

        items = getInventory();
        updateUI(items);
    }

    // Override method when the activity is created to show the app bar menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.appbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // Override method when an action item is pressed in the app bar menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        // Instantiate a new intent for the login activity
        Intent logoutIntent = new Intent(this, LoginActivity.class);

        // Condition when the logout button is pressed
        if (item.getItemId() == R.id.action_logout) {
            startActivity(logoutIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // Method called to check for SMS permission
    private boolean hasFilePermissions(final Activity activity, final String permission,
                                       int rationaleMessageId, final int requestCode) {
        // String set to the request for permission to send SMS
        String sendPermission = SEND_SMS;

        if (ContextCompat.checkSelfPermission(this, sendPermission)
            != PackageManager.PERMISSION_GRANTED) {

            showPermissionRationaleDialog(activity, rationaleMessageId, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions(activity,
                            new String[] { permission }, REQUEST_WRITE_CODE);
                }
            });
            return false;
        }
        else {
            return true;
        }
    }

    // Method called to show rationale dialog box to user
    private static void showPermissionRationaleDialog(Activity activity, int messageId,
                                                      DialogInterface.OnClickListener onClickListener) {
        // Show dialog explaining why permission is needed
        new AlertDialog.Builder(activity)
                .setTitle(R.string.permission_needed)
                .setMessage(messageId)
                .setNegativeButton(R.string.permission_denied, onClickListener)
                .setPositiveButton(R.string.permission_granted, onClickListener)
                .create()
                .show();
    }

    // Override method call to retrieve the user response
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_WRITE_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission granted!
                    Toast.makeText(InventoryActivity.this, "Settings Updated", Toast.LENGTH_SHORT).show();
                    SEND_TEXT = true;
                } else {
                    // Permission denied!
                    Toast.makeText(InventoryActivity.this, "Settings Unchanged", Toast.LENGTH_SHORT).show();
                    SEND_TEXT = false;
                }
                return;
            }
        }
    }

    // Method call to pass the inventory list to the adapter and assign the adapter to the
    // Recycler View holder
    private void updateUI(List<Inventory> inventoryList) {
        mInventoryAdapter = new InventoryAdapter(inventoryList);
        mRecyclerView.setAdapter(mInventoryAdapter);
    }

    // Class for the recycler view holder
    private class InventoryHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener
    {

        // Class member variables
        private Inventory mItem;
        private final TextView mItemTextView;
        private final TextView mItemIDView;

        private final TextView mQuantityView;

        private final Button mButton;


        // Class constructor
        public InventoryHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.recycler_view_items, parent, false));

            // Retrieve the resource location of the edit text fields
            mItemTextView = itemView.findViewById(R.id.inventory_text_view);
            mItemIDView = itemView.findViewById(R.id.inventory_text_view1);
            mQuantityView = itemView.findViewById(R.id.inventory_text_view2);
            mButton = itemView.findViewById(R.id.button_view);

            // Assign on Click listeners to each edit text field
            mItemTextView.setOnClickListener(this);
            mItemIDView.setOnClickListener(this);
            mQuantityView.setOnClickListener(this);
            mButton.setOnClickListener(this);
        }

        // Method call to assign the values to the resource location
        public void bind(Inventory item, int position) {
            mItem = item;

            mItemTextView.setText(mItem.getItem());
            mItemIDView.setText(mItem.getItemID());
            mQuantityView.setText(Integer.toString(mItem.getQuantity()));
        }

        // Method call when a button is pressed in the recycler view
        @Override
        public void onClick(View view) {

            List <Inventory> mItemsList;
            Inventory mItem;
            TextView mItem_Text_View;

            // Start Activity with the selected item
            Intent intentUpdate = new Intent(InventoryActivity.this, UpdateActivity.class);
            Intent intentRemove = new Intent(InventoryActivity.this, RemoveActivity.class);

            // Resource location for the header. Updated when the user clicks/touch the item_id in
            // the row.
            mItem_Text_View = findViewById(R.id.activity_Title);

            // Instantiate Data Base Helper object instance
            mDatabaseHelper = new DataBaseHelper(getApplicationContext(), FILE);

            // Open the database as a writable.
            mSQLDatabase = mDatabaseHelper.getReadableDatabase();

            // Assign list to the return list from the database query
            mItemsList = getInventory();

            // Get the absolute position in the recycler view
            int position = getBindingAdapterPosition();

            // Assign the inventory object instance to the return object in the recycler view
            mItem = mItemsList.get(position);

            // Check if the item_id text view field was pressed
            if (view.getId() == R.id.inventory_text_view1) {

                mItem_Text_View.setText(mItem.getItem() + ": " + mItem.getQuantity());
            }

            // Check if the item text view field was pressed
            else if (view.getId() == R.id.inventory_text_view) {

                intentUpdate.putExtra("itemID", mItem.getItemID());
                intentUpdate.putExtra("item", mItem.getItem());
                intentUpdate.putExtra("quantity", Integer.toString(mItem.getQuantity()));
                intentUpdate.putExtra("sentText", SEND_TEXT);
                startActivity(intentUpdate);
            }

            // Check if the quantity text view field was pressed
            else if (view.getId() == R.id.inventory_text_view2){

                intentUpdate.putExtra("itemID", mItem.getItemID());
                intentUpdate.putExtra("item", mItem.getItem());
                intentUpdate.putExtra("quantity", Integer.toString(mItem.getQuantity()));
                startActivity(intentUpdate);
            }

            // Check if the trash can button was pressed
            else if (view.getId() == R.id.button_view){

                intentRemove.putExtra("itemID", mItem.getItemID());
                intentRemove.putExtra("item", mItem.getItem());
                intentRemove.putExtra("quantity", Integer.toString(mItem.getQuantity()));
                startActivity(intentRemove);
            }
        }
    }

    // Adapter class for the recycler view
    private class InventoryAdapter extends RecyclerView.Adapter<InventoryHolder> {

        // List to store the items retrieved from the database query
        private final List<Inventory> mInventoryList;

        // Assign the class member variable to value passed in the constructor
        public InventoryAdapter(List<Inventory> items) {
            mInventoryList = items;
        }

        @NonNull
        @Override
        public InventoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(getApplicationContext());
            return new InventoryHolder(layoutInflater, parent);
        }

        @Override
        public void onBindViewHolder(InventoryHolder holder, int position){
            holder.bind(mInventoryList.get(position), position);
        }

        @Override
        public int getItemCount() {
            return mInventoryList.size();
        }
    }

    // Method call when the add floating action button is pressed
    public void inventoryAddOnClick(View view) {
        Intent intent = new Intent(this, AddActivity.class);
        startActivity(intent);
    }

    // Method to add item to the inventory
    // Was afraid to put it in a different class because of the database issue and ownership
    // Also used for testing
    public boolean addItem(Inventory item) {

        mDatabaseHelper = new DataBaseHelper(getApplicationContext(), FILE);

        mSQLDatabase = mDatabaseHelper.getWritableDatabase();

        ContentValues iv = new ContentValues();

        iv.put("Item_ID", item.getItemID());
        iv.put("Item", item.getItem());
        iv.put("Quantity", item.getQuantity());

        long insert = mSQLDatabase.insert("Inventory_Table", null, iv);

        if (insert != -1) {
            Toast.makeText(InventoryActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
            return true;
        }
        else {
            Toast.makeText(InventoryActivity.this, "Item Not Added", Toast.LENGTH_SHORT).show();
            return false;
        }
    }
    //Used for testing and to update the application on resume
    public List<Inventory> getInventory() {

        List<Inventory> returnInventory = new ArrayList<>();

        mDatabaseHelper = new DataBaseHelper(getApplicationContext(), FILE);

        mSQLDatabase = mDatabaseHelper.getReadableDatabase();

        String queryString = "SELECT * FROM Inventory_Table";

        Cursor cursor = mSQLDatabase.rawQuery(queryString, null);

        if (cursor.moveToFirst()) {
            do {
                Inventory mItem = new Inventory();
                mItem.setID(cursor.getLong(0));
                mItem.setItemID(cursor.getString(1));
                mItem.setItem(cursor.getString(2));
                mItem.setQuantity(cursor.getInt(3));

                returnInventory.add(mItem);

            } while (cursor.moveToNext());
        }
        else {

        }

        cursor.close();
        mSQLDatabase.close();
        return returnInventory;
    }

    // Method call when the user clicks the add floating action button
    public void onUpdateButtonClick(View view){

        hasFilePermissions(this, SEND_SMS, R.string.text_rationale, REQUEST_WRITE_CODE);

    }
}