package com.example.app2;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
    EditText name,email,address;
    Button registerbtn;
    String userID;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        name = findViewById(R.id.editTextName);
        email = findViewById(R.id.editTextEmail);
        address = findViewById(R.id.editTextAddress);
        registerbtn = findViewById(R.id.cirRegisterButton);
        progressBar = findViewById(R.id.progressBar2);

        firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();

        userID = firebaseAuth.getCurrentUser().getUid();
        final DocumentReference docref=fStore.collection("users").document(userID);


        registerbtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !address.getText().toString().isEmpty() ){
                    progressBar.setVisibility(View.VISIBLE);
                    String fullname = name.getText().toString();
                    String mail = email.getText().toString();
                    String add = address.getText().toString();
                    Map<String,Object>user = new HashMap<>();
                    user.put("Name",fullname);
                    user.put("Email",mail);
                    user.put("Address",add);
                    docref.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                startActivity(new Intent(getApplicationContext(),MainActivity.class));
                                overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                                finish();
                            }
                            else{
                       Toast.makeText(RegisterActivity.this,"Data is Not Inserted",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }else{
                    Toast.makeText(RegisterActivity.this,"All Fields are empty",Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}