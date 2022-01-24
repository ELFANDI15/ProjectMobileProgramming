package com.example.mygym;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class registerActivity extends AppCompatActivity {
    EditText registEmail, registPass1, registPass2;
    Button createAcc;
    String email, pass1, pass2, kode;

    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registEmail = findViewById(R.id.registEmail);
        registPass1 = findViewById(R.id.registPass1);
        registPass2 = findViewById(R.id.registPass2);
        createAcc = findViewById(R.id.createAcc);


        mAuth = FirebaseAuth.getInstance();

        createAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNew();
            }
        });



    }



    private void createNew() {
        email = registEmail.getText().toString();
        pass1 = registPass1.getText().toString();
        pass2 = registPass2.getText().toString();

        mAuth.createUserWithEmailAndPassword(email, pass2)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Toast.makeText(registerActivity.this, "Register Sucess.", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), loginActivity.class);
                            startActivity(intent);

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(registerActivity.this, "Register failed.", Toast.LENGTH_SHORT).show();

                        }

                        // ...
                    }
                });
    }
}