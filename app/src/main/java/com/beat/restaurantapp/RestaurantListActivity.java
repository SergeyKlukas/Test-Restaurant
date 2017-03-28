package com.beat.restaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.beat.restaurantapp.adapter.RestaurantListAdapter;
import com.beat.restaurantapp.models.Restaurant;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class RestaurantListActivity extends AppCompatActivity {

    private RestaurantListAdapter mListAdapter;
    private ListView restaurantList;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(RestaurantListActivity.this, NewRestaurantActivity.class);
                startActivity(intent);
            }
        });

        ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
        mListAdapter = new RestaurantListAdapter(this, restaurants);

        restaurantList = (ListView) findViewById(R.id.restaurant_list);
        restaurantList.setAdapter(mListAdapter);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading Data...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        Query query = FirebaseDatabase.getInstance().getReference()
                .child(Constants.RESTAURANT_DATA);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Restaurant> restaurants = new ArrayList<Restaurant>();
                for (DataSnapshot restaurent : dataSnapshot.getChildren()) {
                    restaurants.add(restaurent.getValue(Restaurant.class));
                }

                if (progressDialog.isShowing())
                    progressDialog.dismiss();
                mListAdapter.setData(restaurants);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if (progressDialog.isShowing())
                    progressDialog.dismiss();
            }
        });

        restaurantList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Restaurant restaurant = (Restaurant)mListAdapter.getItem(position);

                Intent intent = new Intent(RestaurantListActivity.this, DetailRestaurantActivity.class);
                intent.putExtra("restName", restaurant.getStrName());
                intent.putExtra("restCategory", restaurant.getStrCategory());
                intent.putExtra("restPic", restaurant.getPicture());
                startActivity(intent);
            }
        });
    }
}
