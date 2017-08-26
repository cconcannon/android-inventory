package com.example.vn0mrky.inventory;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.vn0mrky.inventory.data.InventoryContract;

/**
 * Created by cconcannon on 8/26/17.
 */

public class InventoryCursorAdapter extends CursorAdapter {
    public InventoryCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return LayoutInflater.from(context).inflate(R.layout.list_item, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView nameTextView = (TextView) view.findViewById(R.id.name);
        TextView summaryTextView = (TextView) view.findViewById(R.id.summary);
        Button soldButton = (Button) view.findViewById(R.id.sold_button);

        int nameColumnIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_NAME);
        int quantityIndex = cursor.getColumnIndex(InventoryContract.InventoryEntry.COLUMN_ITEM_QUANTITY);

        String itemName = cursor.getString(nameColumnIndex);
        int quantity = cursor.getInt(quantityIndex);
        String quantityString = "In Stock: " + quantity;

        if (quantity == 0) {
            soldButton.setVisibility(View.INVISIBLE);
        }

        nameTextView.setText(itemName);
        summaryTextView.setText(quantityString);
    }
}
