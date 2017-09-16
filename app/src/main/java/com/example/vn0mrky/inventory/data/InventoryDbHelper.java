package com.example.vn0mrky.inventory.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.vn0mrky.inventory.data.InventoryContract.InventoryEntry;

/**
 * Created by vn0mrky on 8/16/17.
 */

public class InventoryDbHelper extends SQLiteOpenHelper {
    public static final String LOG_TAG = InventoryDbHelper.class.getSimpleName();

    private static final String DATABASE_NAME = "warehouseInventory.db";

    private static final int DATABASE_VERSION = 5;

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public InventoryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + InventoryEntry.TABLE_NAME + "("
                + InventoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + InventoryEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_ITEM_DESCRIPTION + " TEXT, "
                + InventoryEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL DEFAULT 0 CHECK (" + InventoryEntry.COLUMN_ITEM_QUANTITY + " >= 0), "
                + InventoryEntry.COLUMN_ITEM_PRICE + " INTEGER NOT NULL DEFAULT 0 CHECK (" + InventoryEntry.COLUMN_ITEM_PRICE + " >= 0), "
                + InventoryEntry.COLUMN_ITEM_SUPPLIER_EMAIL + " TEXT NOT NULL, "
                + InventoryEntry.COLUMN_ITEM_IMAGE_URI + " TEXT);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }
}
