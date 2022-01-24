package com.example.mygym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class homeActivity extends AppCompatActivity {
    TextView username, locationTxt;
    Button logout, verivBtn, regisBtn;
    ImageView userImage;

    EditText kodeVeriv;
    String kode;
    loginActivity log = new loginActivity();

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    FusedLocationProviderClient fusedLocationProviderClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        kodeVeriv = findViewById(R.id.kode);
        verivBtn = findViewById(R.id.verivBtn);
        username = findViewById(R.id.username);
        locationTxt = findViewById(R.id.location);
        regisBtn = findViewById(R.id.registerMember);
        userImage = findViewById(R.id.accImg);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(homeActivity.this);

        verivBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verifikasi();
            }
        });

        regisBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(homeActivity.this, RegisterMember.class);
                startActivity(intent1);
            }
        });


        logout = findViewById(R.id.logout);

        GoogleSignInAccount signInAccount = GoogleSignIn.getLastSignedInAccount(this);
        if (signInAccount != null) {
            username.setText(signInAccount.getDisplayName());
            Glide.with(this).load(String.valueOf(signInAccount.getPhotoUrl())).into(userImage);
        }
//
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions gso = new GoogleSignInOptions.
                        Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).
                        build();

                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(homeActivity.this, gso);
                googleSignInClient.signOut();
                Intent intent = new Intent(homeActivity.this, loginActivity.class);
                startActivity(intent);
            }
        });

        getLastLocation();


    }


    private  void getLastLocation(){
        if(ContextCompat.checkSelfPermission(homeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {

                    if (location != null){

                        Geocoder geocoder = new Geocoder(homeActivity.this, Locale.getDefault());
                        List<Address> addresses = null;


                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                            locationTxt.setText(addresses.get(0).getAddressLine(0));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            });

        }else{
            askPermition();
        }
    }

    private void askPermition() {

        ActivityCompat.requestPermissions(homeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 100);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == 100){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                getLastLocation();
            }else {
                Toast.makeText(homeActivity.this, "Location Denied", Toast.LENGTH_LONG).show();

            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    private void verifikasi() {
        kode = kodeVeriv.getText().toString();

        if(kode.equals("43C65")){
            Intent intent = new Intent(homeActivity.this, veriv.class);
            startActivity(intent);
        }else{
            Toast.makeText(homeActivity.this, "Kode Salah.", Toast.LENGTH_LONG).show();
        }
    }




}