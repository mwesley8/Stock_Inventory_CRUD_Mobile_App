package com.zybooks.stockinventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.zybooks.stockinventory.model.Inventory;
import com.zybooks.stockinventory.repo.DataBaseHelper;

// Activity to remove item from inventory
public class RemoveActivity extends AppCompatActivity {

    // Class member variables
    private EditText mItemIDView;

    private EditText mItemView;

    private String mQuantity;

    private static final String FILE = "StockInventory.db";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove);

        // Bundle instance to retrieve information sent from the inventory activity
        Bundle extras = getIntent().getExtras();

        // Declare and assign local variables to the values from the inventory activity
        String mItemID = extras.getString("itemID");
        String mItem = extras.getString("item");
        mQuantity = extras.getString("quantity");

        // Should not be empty
        if (!mItemID.isEmpty()) {
            // Find the resource location of the edit text fields
            mItemIDView = findViewById(R.id.remove_item_name_edit_text);
            mItemView = findViewById((R.id.remove_edit_item));

            // Set the edit text fields to the values passed from the inventory activity
            mItemIDView.setText(mItemID);
            mItemView.setText(mItem);
        }
    }

    // Method call when the user clicks the cancel button
    public void removeCancelOnClick(View view) {
        Intent intent = new Intent(this, InventoryActivity.class);
        startActivity(intent);
    }

    // Method call when the user clicks the submit button
    public void removeSubmitButton(View view) {

        // Instantiate an Inventory object instance
        Inventory item = new Inventory();

        // Instantiate Data Base Helper object instance
        DataBaseHelper db = new DataBaseHelper(RemoveActivity.this, FILE);
        // Open the database as a writable.
        SQLiteDatabase mDatabase = db.getWritableDatabase();

        // Retrieve the resource location of the edit text fields
        mItemIDView   = findViewById(R.id.remove_item_name_edit_text);
        mItemView     = findViewById(R.id.remove_edit_item);

        // Retrieve the user input from the text fields
        String curItemID    = mItemIDView.getText().toString();
        String curItem      = mItemView.getText().toString();


        // Condition to make sure that the user is not trying to enter null values
        if (!curItemID.isEmpty() && !curItem.isEmpty()) {
            // Assign object attributes
            item.setItemID(curItemID);
            item.setItem(curItem);
            item.setQuantity(Integer.parseInt(mQuantity));

            // Query statement to delete an item from the inventory database
            String queryString = "DELETE FROM Inventory_Table WHERE Item_ID = ?";
            SQLiteStatement statement = mDatabase.compileStatement(queryString);
            statement.bindString(1, item.getItemID());
            statement.execute();

            // Output to user that indicates the item was removed from the database
            Toast.makeText(RemoveActivity.this, "Item Removed", Toast.LENGTH_SHORT).show();
            mDatabase.close();
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
        }
        else {
            // Condition when the item was not deleted
            // Testing was done to ensure the data base query was correct
            // This block will execute when the application is not able to open the database
            Toast.makeText(RemoveActivity.this, "Item Not Deleted", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, InventoryActivity.class);
            startActivity(intent);
        }
        finish();
    }
}