package com.example.vn0mrky.inventory;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.vn0mrky.inventory.data.InventoryContract;
import com.example.vn0mrky.inventory.data.InventoryDbHelper;

public class AddItemActivity extends AppCompatActivity {
    private EditText mNameEdit;
    private EditText mDescriptionEdit;
    private EditText mQuantityEdit;
    private EditText mPriceEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEdit = (EditText) findViewById(R.id.input_item_name);
        mDescriptionEdit = (EditText) findViewById(R.id.input_item_description);
        mQuantityEdit = (EditText) findViewById(R.id.quantity);
        mPriceEdit = (EditText) findViewById(R.id.price_input);

        final Button saveButton = (Button) findViewById(R.id.add_item_button);
        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                insertItem();
            }
        });
    }

    private void insertItem() {
        String nameString = mNameEdit.getText().toString().trim();
        String descriptionString = mDescriptionEdit.getText().toString().trim();
        int quantity = Integer.parseInt(mQuantityEdit.getText().toString().trim());
        float priceInput = Float.parseFloat(mPriceEdit.getText().toString().trim());
        int price = Math.round(priceInput*100);

        InventoryDbHelper mDbHelper = new InventoryDbHelper(this);

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_NAME, nameString);
        values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_DESCRIPTION, descriptionString);
        values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY, quantity);
        values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_PRICE, price);

        long newRowId = db.insert(InventoryContract.InventoryEntry.TABLE_NAME, null, values);
        if (newRowId == -1) {
            Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.successful_entry, Toast.LENGTH_SHORT).show();
        }
    }
}
