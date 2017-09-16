package com.example.vn0mrky.inventory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Button;
import android.util.Log;

import com.example.vn0mrky.inventory.data.InventoryContract;
import com.example.vn0mrky.inventory.data.InventoryDbHelper;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class AddItemActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int EXISTING_ITEM_LOADER = 0;
    private static final int PICK_IMAGE_REQUEST = 0;
    private static final String STATE_URI = "STATE_URI";
    private static final String LOG_TAG = AddItemActivity.class.getSimpleName();

    private Uri mCurrentItemUri;
    private Uri mImageUri;
    private EditText mNameEdit;
    private EditText mDescriptionEdit;
    private EditText mQuantityEdit;
    private EditText mPriceEdit;
    private EditText mEmailEdit;
    private Button mImageButton;
    private Button mSaveButton;
    private Button mOrderMoreButton;
    private Button mDeleteButton;
    private boolean mEntryChanged = false;
    private Button mIncreaseButton;
    private Button mDecreaseButton;
    private ImageView mImageView;

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
        mEmailEdit = (EditText) findViewById(R.id.email_input);
        mOrderMoreButton = (Button) findViewById(R.id.order_more_button);
        mImageView = (ImageView) findViewById(R.id.item_image);
        mImageButton = (Button) findViewById(R.id.image_button);
        mSaveButton = (Button) findViewById(R.id.save_button);
        mDeleteButton = (Button) findViewById(R.id.delete_button);
        mIncreaseButton = (Button) findViewById(R.id.quantity_increase);
        mDecreaseButton = (Button) findViewById(R.id.quantity_decrease);

        mNameEdit.setOnTouchListener(mTouchListener);
        mDescriptionEdit.setOnTouchListener(mTouchListener);
        mQuantityEdit.setOnTouchListener(mTouchListener);
        mPriceEdit.setOnTouchListener(mTouchListener);
        mEmailEdit.setOnTouchListener(mTouchListener);

        Intent intent = getIntent();
        mCurrentItemUri = intent.getData();

        if (mCurrentItemUri == null) {
            setTitle("Add Item to Inventory");
            mDeleteButton.setVisibility(View.INVISIBLE);
            mOrderMoreButton.setVisibility(View.INVISIBLE);
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
        mDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteConfirmation();
            }
        });
        mIncreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseQuantity();
            }
        });
        mDecreaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseQuantity();
            }
        });
        mOrderMoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendEmail();
            }
        });
        mImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openImageSelector();
            }
        });
    }

    private void increaseQuantity() {
        int qty = Integer.parseInt(mQuantityEdit.getText().toString());
        qty ++;
        mQuantityEdit.setText("" + qty);
    }

    private void decreaseQuantity() {
        int qty = Integer.parseInt(mQuantityEdit.getText().toString());
        if (qty <= 0) {
            Toast.makeText(this, R.string.quantity_not_negative, Toast.LENGTH_SHORT).show();
            qty = 0;
        } else {
            qty --;
        }
        mQuantityEdit.setText("" + qty);
    }

    private void saveItem() {
        ContentValues values = new ContentValues();

        String nameString = mNameEdit.getText().toString().trim();
        String descriptionString = mDescriptionEdit.getText().toString().trim();
        String quantityString = mQuantityEdit.getText().toString().trim();
        String priceString = mPriceEdit.getText().toString().trim();
        String emailString = mEmailEdit.getText().toString().trim();
        String imageUriString;
        if (mImageUri == null) {
            imageUriString = "";
        } else {
            imageUriString = mImageUri.toString();
        }

        if (nameString.length() < 1 || descriptionString.length() < 1 || quantityString.length() < 1 || priceString.length() < 1 || emailString.length() < 1) {
            Toast.makeText(this, R.string.data_incomplete, Toast.LENGTH_SHORT).show();
            return;
        } else {
            int quantity = Integer.parseInt(mQuantityEdit.getText().toString().trim());
            if (quantity < 0) {
                quantity = 0;
            }
            int price = Integer.parseInt(mPriceEdit.getText().toString().trim());

            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_NAME, nameString);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_DESCRIPTION, descriptionString);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY, quantity);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_PRICE, price);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_SUPPLIER_EMAIL, emailString);
            values.put(InventoryContract.InventoryEntry.COLUMN_ITEM_IMAGE_URI, imageUriString);
        }

        if (mCurrentItemUri == null) {
            Uri newUri = getContentResolver().insert(InventoryContract.InventoryEntry.CONTENT_URI, values);
            if (newUri == null) {
                Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.successful_entry, Toast.LENGTH_SHORT).show();
                finish();
            }
        } else {
            int rowsAffected = getContentResolver().update(mCurrentItemUri, values, null, null);

            if (rowsAffected == 0) {
                Toast.makeText(this, R.string.data_error, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.successful_update, Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    public void openImageSelector() {
        Intent intent = new Intent (Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    private void deleteConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_confirmation);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteItem();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (dialogInterface != null) {
                    dialogInterface.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteItem() {
        if (mCurrentItemUri != null) {
            int rowsDeleted = getContentResolver().delete(mCurrentItemUri, null, null);
            if (rowsDeleted == 0) {
                Toast.makeText(this, R.string.error_deleting, Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, R.string.successful_delete, Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }

    private void sendEmail() {
        String address = mEmailEdit.getText().toString().trim();
        String productName = mNameEdit.getText().toString().trim();
        Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", address, null));
        String emailSubject = getString(R.string.order_request_for) + " " + productName;
        intent.putExtra(Intent.EXTRA_SUBJECT, emailSubject);
        startActivity(Intent.createChooser(intent, ""));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (resultData != null) {
                mImageUri = resultData.getData();
                mImageView.setImageBitmap(getBitmapFromUri(mImageUri));
            }
        }
    }

    public Bitmap getBitmapFromUri(Uri uri) {
        if (uri == null || uri.toString().isEmpty()) {
            return null;
        }

        InputStream input = null;

        try {
            input = this.getContentResolver().openInputStream(uri);
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            int targetW = mImageView.getWidth();
            int targetH = mImageView.getHeight();
            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;
            int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;

            input = this.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(input, null, bmOptions);
            input.close();

            return bitmap;

        } catch (FileNotFoundException fne) {
            Log.e(LOG_TAG, "Failed to load image", fne);
            return null;
        } catch (Exception e) {
            Log.e(LOG_TAG, "Failed to load image", e);
            return null;
        } finally {
            try {
                input.close();
            } catch (IOException ioe) {
                Log.e(LOG_TAG, "IO Exception", ioe);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                InventoryContract.InventoryEntry._ID,
                InventoryContract.InventoryEntry.COLUMN_ITEM_NAME,
                InventoryContract.InventoryEntry.COLUMN_ITEM_DESCRIPTION,
                InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY,
                InventoryContract.InventoryEntry.COLUMN_ITEM_PRICE,
                InventoryContract.InventoryEntry.COLUMN_ITEM_SUPPLIER_EMAIL,
                InventoryContract.InventoryEntry.COLUMN_ITEM_IMAGE_URI
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
            int supplierEmailColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_SUPPLIER_EMAIL);
            int imageUriColumnIndex = data.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_IMAGE_URI);

            String name = data.getString(nameColumnIndex);
            String description = data.getString(descriptionColumnIndex);
            int quantity = data.getInt(quantityColumnIndex);
            int price = data.getInt(priceColumnIndex);
            String supplierEmail = data.getString(supplierEmailColumnIndex);
            String imageUriString = data.getString(imageUriColumnIndex);

            mNameEdit.setText(name);
            mDescriptionEdit.setText(description);
            mQuantityEdit.setText(Integer.toString(quantity));
            mPriceEdit.setText(Integer.toString(price));
            mEmailEdit.setText(supplierEmail);
            if (imageUriString == null || imageUriString.isEmpty()) {
                mImageView.setImageResource(R.drawable.no_image);
            } else {
                mImageUri = Uri.parse(imageUriString);
                Bitmap imageBitmap = getBitmapFromUri(mImageUri);
                mImageView.setImageBitmap(imageBitmap);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mNameEdit.setText("");
        mDescriptionEdit.setText("");
        mPriceEdit.setText("");
        mQuantityEdit.setText("");
        mEmailEdit.setText("");
        mImageView.setImageResource(R.drawable.no_image);
    }
}
