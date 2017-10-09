package com.example.android.inventoryprojectapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public class MyInventoryProvider extends ContentProvider{
    public static final String LOG = MyInventoryProvider.class.getSimpleName();

    private static final int MyInventory = 100;

    private static final int MyInventory_ID = 101;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(MyInevtoryContract.CONTENT_AUTH, MyInevtoryContract.PATH_INVENTORY, MyInventory);

        uriMatcher.addURI(MyInevtoryContract.CONTENT_AUTH, MyInevtoryContract.PATH_INVENTORY + "/#", MyInventory_ID);
    }

    private MyInventoryHelper myInventoryHelper;
    @Override
    public boolean onCreate() {
        myInventoryHelper = new MyInventoryHelper(getContext());
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase DB = myInventoryHelper.getReadableDatabase();

        Cursor cursor;

        int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case MyInventory:
                cursor = DB.query(MyInevtoryContract.MyInventoryProduct.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case MyInventory_ID:
                selection = MyInevtoryContract.MyInventoryProduct._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };

                cursor = DB.query(MyInevtoryContract.MyInventoryProduct.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        final int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case MyInventory:
                return MyInevtoryContract.MyInventoryProduct.CONTENT_LIST_TYPE;
            case MyInventory_ID:
                return MyInevtoryContract.MyInventoryProduct.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + matcher);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        final int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case MyInventory:
                return insertMyProduct(uri,values);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }
    private Uri insertMyProduct(Uri uri, ContentValues values) {
        String my_product_name = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME);
        if (my_product_name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }
        Integer my_product_quantity = values.getAsInteger(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY);
        if(my_product_quantity!= null && my_product_quantity<0){
            throw new IllegalArgumentException("Product requires a quantity");
        }
        String my_product_price = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE);
        if(my_product_price == null){
            throw new IllegalArgumentException("Product requires a price");
        }
        String my_product_discount = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT);
        if(my_product_discount == null){
            throw new IllegalArgumentException("Product requires contact info");
        }
        String my_product_image = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_IMAGE);
        if(my_product_image == null){
            throw new IllegalArgumentException("Product needs an image");
        }
        SQLiteDatabase db = myInventoryHelper.getWritableDatabase();

        long id = db.insert(MyInevtoryContract.MyInventoryProduct.TABLE_NAME, null, values);
        if (id == -1) {
            Log.e(LOG, "Failed to insert row for " + uri);
            return null;
        }

        getContext().getContentResolver().notifyChange(uri, null);

        return ContentUris.withAppendedId(uri, id);
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase db = myInventoryHelper.getWritableDatabase();

        int deleting_rows;

        final int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case MyInventory:
                deleting_rows = db.delete(MyInevtoryContract.MyInventoryProduct.TABLE_NAME, selection, selectionArgs);
                break;
            case MyInventory_ID:
                selection = MyInevtoryContract.MyInventoryProduct._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                deleting_rows = db.delete(MyInevtoryContract.MyInventoryProduct.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        if (deleting_rows != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return deleting_rows;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        final int matcher = uriMatcher.match(uri);
        switch (matcher) {
            case MyInventory:
                return MyProduct_update(uri, values, selection, selectionArgs);
            case MyInventory_ID:
                selection = MyInevtoryContract.MyInventoryProduct._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                return MyProduct_update(uri, values, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }
    private int MyProduct_update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (values.containsKey(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME)) {
            String my_product_name = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME);
            if (my_product_name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }
        if (values.containsKey(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY)){
            Integer my_product_quantity = values.getAsInteger(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY);
            if(my_product_quantity!= null && my_product_quantity<0){
                throw new IllegalArgumentException("Product requires a quantity");
            }
        }
        if (values.containsKey(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT)){
            String my_product_disocunt = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT);
            if(my_product_disocunt==null){
                throw  new IllegalArgumentException("Product requires a price");
            }
        }
        if(values.containsKey(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT)){
            String my_product_disc = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT);
            if(my_product_disc == null){
                throw new IllegalArgumentException("Product requires discount");
            }
        }
        if(values.containsKey(MyInevtoryContract.MyInventoryProduct.COLN_IMAGE)){
            String my_product_image = values.getAsString(MyInevtoryContract.MyInventoryProduct.COLN_IMAGE);
            if(my_product_image == null){
                throw new IllegalArgumentException("Product requires an image");
            }
        }
        if (values.size() == 0) {
            return 0;
        }

        SQLiteDatabase db = myInventoryHelper.getWritableDatabase();

        int rowsUpdated = db.update(MyInevtoryContract.MyInventoryProduct.TABLE_NAME, values, selection, selectionArgs);

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }
}
