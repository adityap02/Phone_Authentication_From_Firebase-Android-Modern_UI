package com.example.app2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class Shop1Activity extends AppCompatActivity {
FirebaseFirestore fStore;
FirebaseAuth fAuth;
TextView user,shopname,tokenview,error;
String token,shop_name,uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop1);
        user = findViewById(R.id.user);
        shopname = findViewById(R.id.shopname);
        fAuth=FirebaseAuth.getInstance();
        fStore= FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        uid=intent.getStringExtra("User_ID");
        shop_name = intent.getStringExtra("shop_name");
        tokenview = findViewById(R.id.currenttoken);
        error = findViewById(R.id.textView6);
        user.setText(uid);
        //shopname.setText(shop_name);
        DocumentReference docref = fStore.collection("agents").document("shop1");
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    token = documentSnapshot.getString("newtoken");
                    int tk = Integer.parseInt(token);
                    tk=+1;
                    String tok = Integer.toString(tk);
                    //welcome = "Welcome " + fname;
                    tokenview.setText(tok);
                    //phone.setText(fAuth.getCurrentUser().getPhoneNumber());
                }else{
                    tokenview.setText("$$");
                    error.setText("Failed to Retrieve Name");
                }
            }
        });


    }
}