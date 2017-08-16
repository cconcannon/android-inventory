package com.example.vn0mrky.inventory.data;

import android.provider.BaseColumns;

/**
 * Created by vn0mrky on 8/16/17.
 */

public final class InventoryContract {
    private InventoryContract() {}

    public static final class InventoryEntry implements BaseColumns {
        public static final String TABLE_NAME = "items";
        public static final String _ID = BaseColumns._ID;
        public static final String COLUMN_ITEM_NAME = "name";
        public static final String COLUMN_ITEM_DESCRIPTION = "description";
        public static final String COLUMN_ITEM_QUANTITY = "quantity";
    }
}
