package com.example.android.inventoryprojectapp;


import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MyInevtoryContract {
    private MyInevtoryContract() {}
    public static final String CONTENT_AUTH = "com.example.android.inventoryprojectapp";
    public static final Uri BASE_URI = Uri.parse("content://" + CONTENT_AUTH);
    public static final String PATH_INVENTORY = "Inventory";

    public static final class MyInventoryProduct implements BaseColumns {

        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_URI, PATH_INVENTORY);
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTH + "/" + PATH_INVENTORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTH + "/" + PATH_INVENTORY;
        public final static String TABLE_NAME = "inventory";
        public final static String _ID = BaseColumns._ID;
        public final static String COLN_PRODUCT_NAME ="name";

        public final static String COLN_PRODUCT_QUANTITY= "quantity";

        public final static String COLN_PRODUCT_PRICE = "price";

        public final static String COLN_PRODUCT_DISCOUNT = "discount";

        public static final String COLN_IMAGE = "image";

    }
}
