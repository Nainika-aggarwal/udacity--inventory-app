package com.example.android.inventoryprojectapp;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.app.LoaderManager;
import android.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

public class MyInventoryActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int Product_load=0;
    MyInventoryAdapter myInventoryAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_inventory);
        FloatingActionButton floatingButton = (FloatingActionButton) findViewById(R.id.floating_action_button);
        floatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyInventoryActivity.this, MyEditorActivity.class);
                startActivity(intent);
            }
        });
        ListView my_inventor_list_view=(ListView) findViewById(R.id.list_view);
        View emptyView=findViewById(R.id.emptyLayout);
        my_inventor_list_view.setEmptyView(emptyView);
      myInventoryAdapter= new MyInventoryAdapter(this,null);
        my_inventor_list_view.setAdapter(myInventoryAdapter);
        my_inventor_list_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MyInventoryActivity.this, MyEditorActivity.class);
                Uri myProductUri = ContentUris.withAppendedId(MyInevtoryContract.MyInventoryProduct.CONTENT_URI, id);
                intent.setData(myProductUri);
                startActivity(intent);
            }
        });
        getLoaderManager().initLoader(Product_load, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MyInevtoryContract.MyInventoryProduct._ID,
                MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME,
                MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY,
                MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE,
                MyInevtoryContract.MyInventoryProduct.COLN_IMAGE};

        return new CursorLoader(this,
                MyInevtoryContract.MyInventoryProduct.CONTENT_URI,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
       myInventoryAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        myInventoryAdapter.swapCursor(null);
    }
    private void insert() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME, "Dummy");
        contentValues.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY, 10);
        contentValues.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE, "50");
        contentValues.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT, 40);
        contentValues.put(MyInevtoryContract.MyInventoryProduct.COLN_IMAGE,"android.resource://com.example.android.inventoryprojectapp/drawable/dummy");
        Uri uri = getContentResolver().insert(MyInevtoryContract.MyInventoryProduct.CONTENT_URI, contentValues);
    }

    private void deleteAllProducts() {
        int deletingRows = getContentResolver().delete(MyInevtoryContract.MyInventoryProduct.CONTENT_URI, null, null);
        Log.v("CatalogActivity", deletingRows + " rows deleted from inventor database");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_inventory, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.insert_dummy_data:
                insert();
                return true;
            case R.id.delete_all_data:
                deleteAllProducts();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
