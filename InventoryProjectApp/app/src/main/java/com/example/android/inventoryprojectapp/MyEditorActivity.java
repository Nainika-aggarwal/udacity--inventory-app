package com.example.android.inventoryprojectapp;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class MyEditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    private static final int EXISTING_PRODUCT_LOADER = 0;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 1;

    private Uri myProductUri;
    Uri uri;
    private EditText my_prduct_name;

    private EditText my_product_quantity;

    private EditText my_prduct_price;
    private EditText my_product_discount;
    private Button incr;
    private Button decr;
    private int qty;
    private ImageView my_product_image;
    private static final int PICK_IMAGE_REQUEST = 0;
    Button my_product_button;
    private boolean isProductHasChanged = false;

    private View.OnTouchListener productTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            isProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_editor);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        my_prduct_name = (EditText) findViewById(R.id.edit_text_name);
        my_product_quantity= (EditText) findViewById(R.id.edit_text_quantity);
        my_prduct_price = (EditText) findViewById(R.id.edit_text_price);
        my_product_discount = (EditText) findViewById(R.id.edit_text_discount);
        my_product_image = (ImageView) findViewById(R.id.Product_Image);
        my_product_button = (Button) findViewById(R.id.Product_Image_Button);

        incr=(Button) findViewById(R.id.button_incr);
        decr=(Button) findViewById(R.id.button_decr);
        my_prduct_name.setOnTouchListener(productTouchListener);
        my_product_quantity.setOnTouchListener(productTouchListener);
        my_prduct_price.setOnTouchListener(productTouchListener);
        my_product_discount.setOnTouchListener(productTouchListener);

        Intent intent = getIntent();
        myProductUri = intent.getData();

        if (myProductUri == null) {
            setTitle("Add Product");

            invalidateOptionsMenu();
        } else {
            setTitle("Edit Product");

            getLoaderManager().initLoader(EXISTING_PRODUCT_LOADER, null, this);
        }

        incr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty=Integer.parseInt(my_product_quantity.getText().toString());
                qty++;
                my_product_quantity.setText(Integer.toString(qty));
            }
        });
        decr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                qty=Integer.parseInt(my_product_quantity.getText().toString());
                if(qty > 0)
                {
                    qty--;
                    my_product_quantity.setText(Integer.toString(qty));
                }
            }
        });
        my_product_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tryToOpenImageSelector();
                isProductHasChanged = true;
            }
        });
    }
    private void productSaving() {
        String product_name = my_prduct_name.getText().toString().trim();
        String product_qty = my_product_quantity.getText().toString().trim();
        String product_prices = my_prduct_price.getText().toString().trim();
        String product_discount = my_product_discount.getText().toString().trim();
        String checker="0";
        String product_imag = my_product_image.toString().trim();
        if (myProductUri == null &&
                TextUtils.isEmpty(product_name) || product_qty.equals(checker) ||
                TextUtils.isEmpty(product_prices) || TextUtils.isEmpty(product_discount) || uri==null) {
            Toast.makeText(this,"Please Enter All Fields Correctly",Toast.LENGTH_SHORT).show();
            return;
        }

        ContentValues values = new ContentValues();
        values.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME, product_name);
        int quantity = 1;
        if (!TextUtils.isEmpty(product_qty)) {
            quantity = Integer.parseInt(product_qty);
        }
        values.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY, quantity);

        values.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE, product_prices);
        values.put(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT, product_discount);
        values.put(MyInevtoryContract.MyInventoryProduct.COLN_IMAGE, String.valueOf(uri));
        if (myProductUri == null) {
            Uri newUri = getContentResolver().insert(MyInevtoryContract.MyInventoryProduct.CONTENT_URI, values);

            if (newUri == null) {
                Toast.makeText(this, "Error with saving product",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,"Product saved",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int rowsAffected = getContentResolver().update(myProductUri, values, null, null);

            if (rowsAffected == 0 || MyInevtoryContract.MyInventoryProduct.COLN_IMAGE==null) {
                Toast.makeText(this, "Error with updating product",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "product updated",
                        Toast.LENGTH_SHORT).show();
            }
        }
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_my_editor, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if (myProductUri == null) {
            MenuItem deleteOneItemMenuItem = menu.findItem(R.id.delete_data);
            MenuItem orderMenuItem = menu.findItem(R.id.order_now);
            deleteOneItemMenuItem.setVisible(false);
            orderMenuItem.setVisible(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_data:
                productSaving();
                return true;
            case R.id.order_now:
                OrderConfirmationDialog();
                return true;
            case R.id.delete_data:
                DeleteConfirmationDialog();
                return true;
            case android.R.id.home:
                if (!isProductHasChanged) {
                    NavUtils.navigateUpFromSameTask(MyEditorActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                NavUtils.navigateUpFromSameTask(MyEditorActivity.this);
                            }
                        };

                UnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (!isProductHasChanged) {
            super.onBackPressed();
            return;
        }
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        UnsavedChangesDialog(discardButtonClickListener);
    }


    private void OrderConfirmationDialog() {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, "nainikaaggarwal20@gmail.com ");
        intent.putExtra(Intent.EXTRA_SUBJECT, "CONFIRMING");
        intent.putExtra(Intent.EXTRA_TEXT, "Please send this product :"+my_prduct_name.getText().toString().trim()+" \nPrice is :"+my_prduct_price.getText().toString().trim());
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
        Toast.makeText(this,"Thanks for Ordering",Toast.LENGTH_SHORT).show();
    }



    private void UnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Discard your changes and quit editting?");
        builder.setPositiveButton("Discard", discardButtonClickListener);
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void DeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Delete this product?");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteProduct();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    private void deleteProduct() {
        if (myProductUri != null) {
            int rowsDeleted = getContentResolver().delete(myProductUri, null, null);

            if (rowsDeleted == 0) {
                Toast.makeText(this, "Error with deleting product",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "product deleted",
                        Toast.LENGTH_SHORT).show();
            }
        }

        finish();
    }
    public void tryToOpenImageSelector() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
            return;
        }
        openImageSelection();
    }

    private void openImageSelection() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
        } else {
            intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
        }
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openImageSelection();
                    // permission was granted
                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {

            if (resultData != null) {
                uri = resultData.getData();
                String[] filePAth={MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(uri,filePAth,null,null,null);
                cursor.moveToFirst();
                int Col= cursor.getColumnIndex(filePAth[0]);
                String pic= cursor.getString(Col);
                cursor.close();
                my_product_image.setImageBitmap(BitmapFactory.decodeFile(pic));
            } else {
                my_product_image.setImageURI(null);
            }
        }
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] projection = {
                MyInevtoryContract.MyInventoryProduct._ID,
                MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME,
                MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY,
                MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE,
                MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT,
                MyInevtoryContract.MyInventoryProduct.COLN_IMAGE};

        return new CursorLoader(this,
                myProductUri,
                projection,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null || data.getCount() < 1) {
            return;
        }

        if (data.moveToFirst()) {
            int my_product_nameColIndex = data.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_NAME);
            int my_product_quantityColIndex = data.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_QUANTITY);
            int my_product_priceColIndex = data.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_PRICE);
            int my_product_discountColIndex = data.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_PRODUCT_DISCOUNT);
            int my_product_imageColIndex = data.getColumnIndex(MyInevtoryContract.MyInventoryProduct.COLN_IMAGE);
            String my_imag = data.getString(my_product_imageColIndex);
            String my_name = data.getString(my_product_nameColIndex);
            String my_quantity = data.getString(my_product_quantityColIndex);
            String my_price = data.getString(my_product_priceColIndex);
            String my_discount = data.getString(my_product_discountColIndex);
            my_prduct_name.setText(my_name);
            my_product_quantity.setText(my_quantity);
            my_prduct_price.setText(my_price);
            my_product_discount.setText(my_discount);
            uri= Uri.parse(my_imag);
            my_product_image.setImageURI(uri);
            my_product_button.setEnabled(true);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        my_prduct_name.setText("");
        my_product_quantity.setText("");
        my_prduct_price.setText("");
        my_product_discount.setText("");
        my_product_image.setImageURI(uri);
    }
}
