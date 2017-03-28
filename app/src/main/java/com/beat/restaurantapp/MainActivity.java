package com.beat.restaurantapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.beat.restaurantapp.models.UserDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView textViewEmail;
    private Button logoutButton;
    private Button restaurantButton;

    private EditText editTextName, editTextAddress, editTextAge;
    private Button saveInfo;

    private ProgressDialog progressDlg;

    private DatabaseReference dbReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logoutButton = (Button) findViewById(R.id.logoutButton);
        restaurantButton = (Button) findViewById(R.id.restaurantPanel);
        textViewEmail = (TextView) findViewById(R.id.userEmail);
        editTextName = (EditText) findViewById(R.id.nameTextField);
        editTextAddress = (EditText) findViewById(R.id.addressTextField);
        editTextAge = (EditText) findViewById(R.id.ageTextField);
        saveInfo = (Button) findViewById(R.id.saveInfoButton);

        logoutButton.setOnClickListener(this);
        saveInfo.setOnClickListener(this);
        restaurantButton.setOnClickListener(this);

        progressDlg = new ProgressDialog(this);
        progressDlg.setMessage("Loading...");
        progressDlg.show();

        dbReference = FirebaseDatabase.getInstance().getReference();
        dbReference.child("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDetails curUser = new UserDetails();
                curUser = dataSnapshot.getValue(UserDetails.class);

                if (curUser != null) {
                    editTextName.setText(curUser.getName());
                    editTextAddress.setText(curUser.getAddress());
                    editTextAge.setText(curUser.getAge() + "");
                    String name = curUser.getEmail().toString().trim();
                    String welcomeMessage = "Hello, " + name;
                    textViewEmail.setText(welcomeMessage);
                }
                progressDlg.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view == logoutButton) {
            FirebaseAuth.getInstance().signOut();
        }
        if (view == saveInfo){
            saveInfo();
        }
        if (view == restaurantButton){
            startActivity(new Intent(getApplicationContext(), RestaurantListActivity.class));
            finish();
        }
    }

    public void saveInfo() {

        String name = editTextName.getText().toString();
        String address = editTextAddress.getText().toString();
        int age = Integer.parseInt(editTextAge.getText().toString());

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        UserDetails userInfo = new UserDetails(user.getEmail(), name, address, age);
        dbReference.child("Users").child(user.getUid()).setValue(userInfo);

        Toast.makeText(this, "Information Saved", Toast.LENGTH_LONG).show();

    }
}
