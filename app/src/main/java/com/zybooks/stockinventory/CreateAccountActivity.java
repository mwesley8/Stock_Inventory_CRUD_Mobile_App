package com.zybooks.stockinventory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zybooks.stockinventory.repo.DataBaseHelper;

public class CreateAccountActivity extends AppCompatActivity {

    // Class member variables
    private static final String FILE = "StockInventory.db";

    private EditText mUserNameEditView;

    private EditText mUserPasswordEditView1;

    private EditText mUserPasswordEditView2;

    private Button mButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        // Retrieve the resource location of the edit text fields
        mButton = findViewById(R.id.login_button2);
        mUserNameEditView = findViewById(R.id.username_edit_text);
        mUserPasswordEditView1 = findViewById(R.id.password_edit_text2);
        mUserPasswordEditView2 = findViewById(R.id.password_edit_text);

        // Add on text change listener for the username edit text field
        mUserNameEditView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 3) {
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
        mUserPasswordEditView1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 3) {
                    mButton.setVisibility(View.INVISIBLE);
                }
                else {
                    mButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        // Add on text change listener for the re password edit text field
        mUserPasswordEditView2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() < 3) {
                    mButton.setVisibility(View.INVISIBLE);
                }
                else {
                    mButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // Method called with the submit button is pressed
    public void cancelOnClick(View view) {
        // Start the Login activity
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    // Method called with the Create button is pressed
    public void createAccountOnClick(View view) {

        // Retrieve the resource location of the edit text fields
        mUserNameEditView = findViewById(R.id.username_edit_text);
        mUserPasswordEditView1 = findViewById(R.id.password_edit_text2);
        mUserPasswordEditView2 = findViewById(R.id.password_edit_text);

        // Retrieve the user input from the text fields
        String username = mUserNameEditView.getText().toString();
        String password = mUserPasswordEditView1.getText().toString();
        String password2 = mUserPasswordEditView2.getText().toString();


        // Instantiate Data Base Helper object instance
        DataBaseHelper mDatabaseHelper = new DataBaseHelper(getApplicationContext(), FILE);

        // Open the database as a writable.
        SQLiteDatabase mDatabase = mDatabaseHelper.getWritableDatabase();

        // Instantiate a new Content object
        ContentValues iv = new ContentValues();
        iv.put("User", username);
        iv.put("Password", password);

        // check to mack sure that the passwords match
        if (password.equals(password2)) {
            // local variable to retrieve the return value of the query statement.
            long insert = mDatabase.insert("User_Table", null, iv);
            if (insert != -1) {
                // When the return value indicates the user was added
                Toast.makeText(CreateAccountActivity.this, "User Added", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, LoginActivity.class);
                // Start the login activity
                startActivity(intent);
                mDatabase.close();
                finish();
            } else {
                // When the return value indicates the user was not added
                Toast.makeText(CreateAccountActivity.this, "Password Does Not Match", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, CreateAccountActivity.class);
                startActivity(intent);
                finish();
            }
        }
    }
}