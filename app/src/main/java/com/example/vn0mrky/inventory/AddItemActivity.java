package com.example.vn0mrky.inventory;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.util.Log;

import com.example.vn0mrky.inventory.data.InventoryContract;
import com.example.vn0mrky.inventory.data.InventoryDbHelper;

public class AddItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_ITEM_LOADER = 0;
    private Uri mCurrentItemUri;
    private EditText mNameEdit;
    private EditText mDescriptionEdit;
    private EditText mQuantityEdit;
    private EditText mPriceEdit;
    private Button mSaveButton;
    private Button mOrderMoreButton;
    private Button mDeleteButton;
    private boolean mEntryChanged = false;

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mEntryChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        mNameEdit = (EditText) findViewById(R.id.input_item_name);
        mDescriptionEdit = (EditText) findViewById(R.id.input_item_description);
        mQuantityEdit = (EditText) findViewById(R.id.quantity);
        mPriceEdit = (EditText) findViewById(R.id.price_input);
        mOrderMoreButton = (Button) findViewById(R.id.order_more_button);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle("Add Item to Inventory");
            mDeleteButton.setVisibility(View.INVISIBLE);
        } else {
            setTitle("Edit Existing Item");
            getLoaderManager().initLoader(EXISTING_ITEM_LOADER, null, this);
        }

        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }

    private void saveItem() {
        ContentValues values = new ContentValues();

        String nameString = mNameEdit.getText().toString().trim();
        String descriptionString = mDescriptionEdit.getText().toString().trim();
        String quantityString = mQuantityEdit.getText().toString().trim();
        String priceString = mPriceEdit.getText().toString().trim();

        if (nameString.length() < 1 || descriptionString.length() < 1 || quantityString.length() < 1 || priceString.length() < 1) {
            Toast.makeText(this, R.string.data_incomplete, Toast.LENGTH_SHORT).show();
            return;
        } else {
            int quantity = Integer.parseInt(mQuantityEdit.getText().toString().trim());
            int price = Integer.parseInt(mPriceEdit.getText().toString().trim());

            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_NAME, nameString);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_DESCRIPTION, descriptionString);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY, quantity);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_PRICE, price);
        }

        Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
        if (newUri == null) {
            Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, R.string.successful_entry, Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_ITEM_NAME,
                InventoryContract.InventoryEntry.COLUMN_ITEM_DESCRIPTION,
                InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_ITEM_PRICE
        };

        return new CursorLoader(this, mCurrentItemUri, projection, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1 ) {
            return;
        }

        if (data.moveToFirst()) {
            int nameColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_NAME);
            int descriptionColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_DESCRIPTION);
            int quantityColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY);
            int priceColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_PRICE);

            String name = data.getString(nameColumnIndex);
            String description = data.getString(descriptionColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            int price = data.getInt(priceColumnIndex);

            mNameEdit.setText(name);
            mDescriptionEdit.setText(description);
            mQuantityEdit.setText(Integer.toString(quantity));
            mPriceEdit.setText(Integer.toString(price));
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEdit.setText("");
        mDescriptionEdit.setText("");
        mPriceEdit.setText("");
        mQuantityEdit.setText("");
    }
}
