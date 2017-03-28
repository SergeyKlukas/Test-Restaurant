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
import android.widget.TextView;
import android.widget.Toast;

import com.beat.restaurantapp.models.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by beat on 3/26/2017.
 */

public class DetailRestaurantActivity extends AppCompatActivity {

    private ImageView imgRestaurant;
    private TextView edtName;
    private TextView edtCategory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_detail);

        edtName = (TextView) findViewById(R.id.restaurant_name);
        edtCategory = (TextView) findViewById(R.id.restaurant_category);
        imgRestaurant = (ImageView) findViewById(R.id.image_new);

        edtName.setText(getIntent().getStringExtra("restName"));
        edtCategory.setText(getIntent().getStringExtra("restCategory"));

        Bitmap imageBitmap = null;
        try {
            imageBitmap = decodeFromFirebaseBase64(getIntent().getStringExtra("restPic"));
            imgRestaurant.setImageBitmap(imageBitmap);
        } catch (IOException e) {
        }

    }

    private Bitmap decodeFromFirebaseBase64(String image) throws IOException {
        byte[] decodedByteArray = android.util.Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }
}
