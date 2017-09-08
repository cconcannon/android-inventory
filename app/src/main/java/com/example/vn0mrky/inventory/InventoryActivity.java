package com.example.vn0mrky.inventory;

import android.app.LoaderManager;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.vn0mrky.inventory.data.InventoryContract;
import com.example.vn0mrky.inventory.data.InventoryContract.InventoryEntry;
import com.example.vn0mrky.inventory.data.InventoryDbHelper;

import static com.example.vn0mrky.inventory.data.InventoryDbHelper.LOG_TAG;


public class InventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final int INVENTORY_LOADER = 0;
    private static final String LOG_TAG = InventoryActivity.class.getSimpleName();

    private InventoryDbHelper mDbHelper;
    InventoryCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(InventoryActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        ListView inventoryListView = (ListView) findViewById(R.id.inventory_list);
        View emptyView = findViewById(R.id.empty_view);
        inventoryListView.setEmptyView(emptyView);

        mCursorAdapter = new InventoryCursorAdapter(this, null);
        inventoryListView.setAdapter(mCursorAdapter);

        inventoryListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(InventoryActivity.this, AddItemActivity.class);
                Uri currentItemUri = ContentUris.withAppendedId(InventoryEntry.CONTENT_URI, l);
                intent.setData(currentItemUri);
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(INVENTORY_LOADER, null, this);
    }

    public void sold_an_item(Uri itemUri) {
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertDummyItem();
                return true;

            case R.id.action_delete_all_entries:
                deleteInventory();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertDummyItem() {
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_ITEM_NAME, "Hoka One One Tracer");
        values.put(InventoryEntry.COLUMN_ITEM_DESCRIPTION, "A lightweight, yet full-cushioned road running shoe");
        values.put(InventoryEntry.COLUMN_ITEM_QUANTITY, 1);
        values.put(InventoryEntry.COLUMN_ITEM_PRICE, 9900);

        getContentResolver().insert(InventoryEntry.CONTENT_URI, values);
    }

    private void deleteInventory() {
//        int rowsDeleted = getContentResolver().delete(InventoryEntry.CONTENT_URI, InventoryEntry.COLUMN_ITEM_NAME, null);
        Toast.makeText(this, R.string.deleted_all, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        String[] projection = {
                InventoryEntry._ID,
                InventoryEntry.COLUMN_ITEM_NAME,
                InventoryEntry.COLUMN_ITEM_QUANTITY,
                InventoryEntry.COLUMN_ITEM_DESCRIPTION,
                InventoryEntry.COLUMN_ITEM_PRICE
        };

        return new CursorLoader(this,
                InventoryEntry.CONTENT_URI,
                projection,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        mCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }
}
