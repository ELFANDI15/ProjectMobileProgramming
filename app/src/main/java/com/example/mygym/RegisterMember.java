package com.example.mygym;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class RegisterMember extends AppCompatActivity {
    EditText emailRegis, nameRegis;
    Button daftar, uploadBtn;
    ImageView uploadPhoto;
    FirebaseDatabase rootNode;
    DatabaseReference reference;

    FirebaseStorage storage;
    StorageReference storageReference;

    final String randomKey = UUID.randomUUID().toString();
    private Uri mImageUri;

    private  static  final  int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().hide();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_member);

        emailRegis = findViewById(R.id.registerMemberEmail);
        nameRegis = findViewById(R.id.registerMemberName);
        daftar = findViewById(R.id.daftar);

        uploadBtn = findViewById(R.id.uploadBtn);
        uploadPhoto = findViewById(R.id.uploadPhoto);

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });

        daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rootNode = FirebaseDatabase.getInstance("https://mygym-15bdb-default-rtdb.asia-southeast1.firebasedatabase.app/");
                reference = rootNode.getReference("users");

                String email = emailRegis.getText().toString();
                String name = nameRegis.getText().toString();
                UserHelperClass userHelperClass = new UserHelperClass(email,name,randomKey);


                reference.child(name).setValue(userHelperClass);
                uploadImage();

            }
        });
    }

    public  void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null){
            mImageUri = data.getData();

            Picasso.with(this).load(mImageUri).into(uploadPhoto);
        }
    }

    public void uploadImage(){


        StorageReference riversRef = storageReference.child("Image/" + randomKey);

        final ProgressDialog pd = new ProgressDialog(RegisterMember.this);
        pd.setTitle("Upload file...");
        pd.show();

        riversRef.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(RegisterMember.this, "File Uploaded", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle unsuccessful uploads
                        // ...
                        pd.dismiss();
                        Toast.makeText(RegisterMember.this, "Failed to Upload File", Toast.LENGTH_LONG).show();

                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100.00 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Uploading File...");
                    }
                });
    }
}