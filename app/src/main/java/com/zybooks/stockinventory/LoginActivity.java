package com.zybooks.stockinventory;

import static android.content.Intent.FLAG_ACTIVITY_NO_HISTORY;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zybooks.stockinventory.model.*;
import com.zybooks.stockinventory.repo.*;

import java.util.ArrayList;
import java.util.List;

// Activity to login user
public class LoginActivity extends AppCompatActivity {

    // Class member variables
    private static final String FILE = "User.db";

    EditText mUserNameEditView;
    EditText mUserPasswordEditView;
    Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Retrieve the resource location of the edit text fields
        mButton = findViewById(R.id.activity_login_button);
        mUserNameEditView = findViewById(R.id.login_username_edit_text);
        mUserPasswordEditView = findViewById(R.id.login_password_edit_text);

        // Add on text change listener for the username edit text field
        mUserPasswordEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 5) {
                    mButton.setVisibility(View.INVISIBLE);
                } else {
                    mButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Add on text change listener for the password edit text field
        mUserNameEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 5) {
                    mButton.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // Method when user clicks create account button
    public void onCreateAccountClick(View view) {
        Intent intent = new Intent(this, CreateAccountActivity.class);
        startActivity(intent);
    }

    // Method when user clicks the login button
    public void onLoginClick(View view) {

        // List to hold the return value of the query
        List<Users> mUserList;

        //  Local variable for username and password match
        boolean found = false;

        // Fined the resource location for the name and password edit text fields
        mUserNameEditView = findViewById(R.id.login_username_edit_text);
        mUserPasswordEditView = findViewById(R.id.login_password_edit_text);

        // Assign user input values
        String username = mUserNameEditView.getText().toString();
        String password = mUserPasswordEditView.getText().toString();

        // Assign list to the return value of the query
        mUserList = getUsers();

        // Check for a match in the list
        for (Users u : mUserList) {
            // When a match is found
            if (u.getUserName().equals(username) && u.getPassWord().equals(password)) {
                Toast.makeText(LoginActivity.this, "Logging in: " + username, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, InventoryActivity.class);
                intent.addFlags(FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                found = true;
                break;
            }
        }

        // Condition when a match was not found
        if (!found) {
            Toast.makeText(LoginActivity.this, "No Credentials For: " + username, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
        finish();
    }

    // Method to retrieve users from the database
    public List<Users> getUsers() {

        // List to hold users
        List<Users> userInventory = new ArrayList<>();

        // Instantiate Data Base Helper object instance
        DataBaseHelper db = new DataBaseHelper(getApplicationContext(), FILE);

        // Open the database as a writable.
        SQLiteDatabase mDatabase = db.getWritableDatabase();

        // Query statement to retrieve users
        String queryString = "SELECT * FROM User_Table";

        // Instantiate and assign cursor object with return from query
        Cursor cursor = mDatabase.rawQuery(queryString, null);

        // Iterate over the cursor object
        if (cursor.moveToFirst()) {
            do {
                Users user = new Users();
                user.set_ID(cursor.getLong(0));
                user.setUserName(cursor.getString(1));
                user.setPassWord(cursor.getString(2));;

                userInventory.add(user);

            } while (cursor.moveToNext());
        }

        // I am not a fan of raw queries. I like SQL execute. However, it is nice to get a return
        // value that gives an indication if the query was successfully executed
        // Close the cursor object
        cursor.close();
        // Close the database
        mDatabase.close();
        // Return the list to the user
        return userInventory;
    }

    // Not used for the application ,but can be used for testing
    public void addUser(Users user) {


        DataBaseHelper mDatabaseHelper = new DataBaseHelper(getApplicationContext(), FILE);

        SQLiteDatabase mDatabase = mDatabaseHelper.getWritableDatabase();

        ContentValues iv = new ContentValues();

        iv.put("User", user.getUserName());
        iv.put("Password", user.getPassWord());

        long insert = mDatabase.insert("User_Table", null, iv);

        if (insert != -1) {
            Toast.makeText(LoginActivity.this, "User Added", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(LoginActivity.this, "User Not Added", Toast.LENGTH_SHORT).show();
        }
    }
}