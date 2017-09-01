package com.example.vn0mrky.inventory;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

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
    }

    private void insertItem() {
        String nameString = mNameEdit.getText().toString().trim();
        String descriptionString = mDescriptionEdit.getText().toString().trim();
        int quantity = Integer.parseInt(mQuantityEdit.getText().toString().trim());
        int price = Integer.parseInt(mPriceEdit.getText().toString().trim());
    }
}
