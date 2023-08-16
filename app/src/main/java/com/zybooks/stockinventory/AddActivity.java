package com.zybooks.stockinventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.zybooks.stockinventory.model.*;
import com.zybooks.stockinventory.repo.*;

// Activity to add item to inventory
public class AddActivity extends AppCompatActivity {

    // Class member variables
    private EditText mItemID_View;
    private EditText mItem_View;
    private EditText mQuantity_View;

    private TextView mTest;

    private InventoryActivity mInventoryActivity;

    private SQLiteDatabase mDatabase;

    private static final String FILE = "StockInventory.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

    }

    // Method called with the submit button is pressed
    public void addItemSubmitButton(View view) {

        // Instantiate Inventory item object instance
        Inventory item = new Inventory();

        // Instantiate Data Base Helper object instance
        DataBaseHelper db = new DataBaseHelper(AddActivity.this, FILE);

        // Open the database as a writable.
        mDatabase = db.getWritableDatabase();

        // Retrieve the resource location of the edit text fields
        mItemID_View   = findViewById(R.id.add_item_name_edit_text);
        mItem_View     = findViewById(R.id.add_edit_item);
        mQuantity_View = findViewById(R.id.add_quantity_edit_text);

        // Retrieve the user input from the text fields
        String curItemID    = mItemID_View.getText().toString();
        String curItem      = mItem_View.getText().toString();
        String curQuantity = mQuantity_View.getText().toString();

        // Check to make sure that each field does have user input
        if (!curItemID.isEmpty() && !curItem.isEmpty() && !curQuantity.isEmpty()) {
            // set object instance attributes
            item.setItemID(curItemID);
            item.setItem(curItem);
            item.setQuantity(Integer.parseInt(curQuantity));

            // Instantiate a new Content object
            ContentValues iv = new ContentValues();

            // Assign content object attributes to inventory object instance
            iv.put("Item_ID", item.getItemID());
            iv.put("Item", item.getItem());
            iv.put("Quantity", item.getQuantity());

            // Loca variable assigned to the return value of the query
            long insert = mDatabase.insert("Inventory_Table", null, iv);

            // Check the return value
            if (insert != -1) {
                // Output to user when the item was added
                Toast.makeText(AddActivity.this, "Item Added", Toast.LENGTH_SHORT).show();
            }
            else {
                // Output to user when the item was not added
                // Generally will not show unles the application cannot open the database
                Toast.makeText(AddActivity.this, "Item Not Added", Toast.LENGTH_SHORT).show();
            }

            // Declare new intent to state inventory activity after the new item was added
            Intent intent = new Intent(this, InventoryActivity.class);
            // Start the new activity
            startActivity(intent);
            // Destroy the activity so the user cannot press the back button to access the activity
            finish();
        }
        else {
            // Condition when a field is left blank
            Toast.makeText(AddActivity.this, "Item Not Added", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, InventoryActivity.class);
            // Start the Inventory activity
            startActivity(intent);
            // Activity will be destroyed. The current activity requires an intent passed during
            // creation
            finish();
        }
    }

    public void addItemCancelButton(View view) {
        // Method called when the cancel button is pressed on the add activity screen
        Intent intent = new Intent(this, InventoryActivity.class);
        // Start inventory activity
        startActivity(intent);
        // Destroy the activity so the user cannot press the back button to access the activity
        finish();
    }
}