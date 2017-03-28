package com.beat.restaurantapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.beat.restaurantapp.models.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * Created by beat on 3/26/2017.
 */

public class NewRestaurantActivity extends AppCompatActivity {

    private FloatingActionButton btnSelectImage;
    private Button btnAdd;
    private ImageView imgRestaurant;
    private EditText edtName;
    private EditText edtCategory;

    private Bitmap mCropImage;
    private Uri mCropImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_new);

        imgRestaurant   = (ImageView) findViewById(R.id.image_new);
        btnAdd          = (Button) findViewById(R.id.restaurant_add_btn);
        edtName         = (EditText) findViewById(R.id.restaurant_name);
        edtCategory     = (EditText) findViewById(R.id.restaurant_category);

        btnSelectImage = (FloatingActionButton) findViewById(R.id.picker_fab);
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.startPickImageActivity(NewRestaurantActivity.this);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (edtName.getText().toString().isEmpty()) {
                    Toast.makeText(NewRestaurantActivity.this, "Please input the restaurant name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (edtCategory.getText().toString().isEmpty()) {
                    Toast.makeText(NewRestaurantActivity.this, "Please input the restaurant category", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (imgRestaurant.getDrawable() == null) {
                    Toast.makeText(NewRestaurantActivity.this, "Please select the restaurant image", Toast.LENGTH_SHORT).show();
                    return;
                }

                Restaurant restaurant = new Restaurant();
                restaurant.setStrName(edtName.getText().toString());
                restaurant.setStrCategory(edtCategory.getText().toString());

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ((BitmapDrawable)imgRestaurant.getDrawable()).getBitmap().compress(Bitmap.CompressFormat.PNG, 100, baos);
                restaurant.setPicture(Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT));

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference()
                        .child(Constants.RESTAURANT_DATA)
                        .child(restaurant.getStrName());

                ref.setValue(restaurant);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(NewRestaurantActivity.this, "su", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Toast.makeText(NewRestaurantActivity.this, "er", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

    }

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                startCropImageActivity(imageUri);
            }
        } else if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mCropImageUri = result.getUri();
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(mCropImageUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                mCropImage = BitmapFactory.decodeStream(imageStream);
                imgRestaurant.setImageBitmap(mCropImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(NewRestaurantActivity.this, error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if (requestCode == CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE) {
            if (mCropImageUri != null && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startCropImageActivity(mCropImageUri);
            } else {
                Toast.makeText(this, "Cancelling, required permissions are not granted", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void startCropImageActivity(Uri imageUri) {
        CropImage.activity(imageUri).start(this);
    }
}
