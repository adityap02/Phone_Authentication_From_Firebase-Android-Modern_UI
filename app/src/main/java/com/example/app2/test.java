package com.example.app2;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class test extends AppCompatActivity {

   FirebaseAuth firebaseAuth;
    FirebaseFirestore fStore;
   EditText fname,email,address;
    Button registerbtn;
    String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

firebaseAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        //setContentView(R.layout.activity_register);

      //email.findViewById(R.id.fullname);
        registerbtn = findViewById(R.id.cirRegisterButton);
      /*
        email.findViewById(R.id.editTextEmail);
        email.findViewById(R.id.editTextEmail);
        email.findViewById(R.id.editTextEmail);
        address.findViewById(R.id.editTextAddress);
      */  //registerbtn.findViewById(R.id.cirRegisterButton);

       // firebaseAuth = FirebaseAuth.getInstance();
        //fStore = FirebaseFirestore.getInstance();
    }
}