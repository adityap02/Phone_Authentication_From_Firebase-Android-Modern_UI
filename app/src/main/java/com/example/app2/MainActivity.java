package com.example.app2;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth fAuth;
    FirebaseFirestore fStore;
    String welcome,fname;
    TextView name,phone;
    Button Shop1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        name =findViewById(R.id.textView);
        //phone= findViewById(R.id.textView2);
        Shop1 = findViewById(R.id.button2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        DocumentReference docref=fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        docref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
            if(documentSnapshot.exists()){
                fname = documentSnapshot.getString("Name");
                welcome = "Welcome " + fname;
                name.setText(welcome);
               // phone.setText(fAuth.getCurrentUser().getPhoneNumber());
            }else{
                name.setError("Failed to Retrieve Name");
            }
            }
        });

        Shop1.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
               tokenactivity();
            }
        });



    }

   private void tokenactivity() {
        String uid = fAuth.getCurrentUser().getUid();
        String Shop = "shop1";
        Intent intent = new Intent(MainActivity.this,Shop1Activity.class);
        intent.putExtra("User_ID",uid);
      intent.putExtra("shop_name",Shop);
        startActivity(intent);
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(),LoginActivity.class));
        finish();
    }
}