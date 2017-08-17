package com.example.vn0mrky.inventory;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.vn0mrky.inventory.data.InventoryContract.InventoryEntry;
import com.example.vn0mrky.inventory.data.InventoryDbHelper;


public class InventoryActivity extends AppCompatActivity {

    private InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inventory);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertItem();
                Intent intent = new Intent(InventoryActivity.this, AddItemActivity.class);
                startActivity(intent);
            }
        });

        mDbHelper = new InventoryDbHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void insertItem() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(InventoryEntry.COLUMN_ITEM_NAME, "Hoka One One Tracer");
        values.put(InventoryEntry.COLUMN_ITEM_DESCRIPTION, "A lightweight, yet full-cushioned road running shoe");
        values.put(InventoryEntry.COLUMN_ITEM_QUANTITY, 1);

        long newRowId = db.insert(InventoryEntry.TABLE_NAME, null, values);
        Log.i("row ID:", Long.toString(newRowId));
        Toast.makeText(this, "Row ID: " + Long.toString(newRowId), Toast.LENGTH_SHORT).show();
    }
}
