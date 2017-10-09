package com.example.android.inventoryprojectapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyInventoryHelper extends SQLiteOpenHelper{
    public static final String LOG = MyInventoryHelper.class.getSimpleName();

    private static final String DB_NAME = "Inventory.db";

    private static final int DB_VERSION = 1;


    public MyInventoryHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String SQL_CREATE_INVENTORY_TABLE = "CREATE TABLE " + MyInevtoryContract.MyInventoryProduct.TABLE_NAME + " ("
                + MyInevtoryContract.MyInventoryProduct._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME + " TEXT NOT NULL, "
                + MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY + " INTEGER NOT NULL, "
                + MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE + " TEXT NOT NULL, "
                + MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT + " TEXT , "
                + MyInevtoryContract.MyInventoryProduct.COLN_IMAGE + " TEXT);";

        db.execSQL(SQL_CREATE_INVENTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ MyInevtoryContract.MyInventoryProduct.TABLE_NAME);
    }
}
