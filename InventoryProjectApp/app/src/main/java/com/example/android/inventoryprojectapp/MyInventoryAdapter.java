package com.example.android.inventoryprojectapp;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyInventoryAdapter extends CursorAdapter{
    public MyInventoryAdapter(Context context,Cursor cur){
        super(context,cur,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.activity_my_list_item,parent,false);
    }

    @Override
    public void bindView(View view, final Context context, Cursor cursor) {
        int id = cursor.getColumnIndex(MyInevtoryContract.MyInventoryProduct._ID);
        TextView my_product_name = (TextView) view.findViewById(R.id.text_view_list_name);
        final TextView my_product_quantity = (TextView) view.findViewById(R.id.text_view_list_quantity);
        TextView my_product_price = (TextView) view.findViewById(R.id.text_view_list_price);
        int my_nameColnIndex = cursor.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME);
        int my_quantityColnIndex = cursor.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY);
        int my_priceColnIndex = cursor.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE);
        ImageView my_product_image = (ImageView) view.findViewById(R.id.image_view_list_image);
        final long ID = cursor.getLong(id);
        String my_product_Name = cursor.getString(my_nameColnIndex);
        int my_product_Quantity = cursor.getInt(my_quantityColnIndex);
        int my_product_Price = cursor.getInt(my_priceColnIndex);
        my_product_image.setImageURI(Uri.parse(cursor.getString(cursor.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_IMAGE))));
        my_product_name.setText(my_product_Name);
        my_product_quantity.setText(Integer.toString(my_product_Quantity));
        my_product_price.setText(Integer.toString(my_product_Price));
        Button sales = (Button) view.findViewById(R.id.button_sales);
        sales.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                int updatedQuantity = Integer.parseInt(my_product_quantity.getText().toString());
                if(updatedQuantity > 0){
                    updatedQuantity=updatedQuantity-1;
                    my_product_quantity.setText(Integer.toString(updatedQuantity));
                    ContentValues values = new ContentValues();
                    values.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY, updatedQuantity);
                    Uri uri = ContentUris.withAppendedId(MyInevtoryContract.MyInventoryProduct.CONTENT_URI, ID);
                    context.getContentResolver().update(uri, values, null,null);
                }
            }
        }));
    }
}
