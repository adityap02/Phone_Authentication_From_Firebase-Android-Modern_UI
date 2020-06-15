package com.example.app2;
import android.content.Intent;
import android.os.Build;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.concurrent.TimeUnit;

import io.opencensus.tags.Tag;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText phone,code;
    Button LoginBtn;
    TextView resend,state;
    ProgressBar progressbar,progressBar1;
    FirebaseAuth fAuth;
    String verificationID;
    PhoneAuthProvider.ForceResendingToken token;
    PhoneAuthProvider.OnVerificationStateChangedCallbacks callbacks;
    TextInputLayout inputotp;
    Boolean verificationInProgress =false;
    FirebaseFirestore fStore;
  //  LinearLayout one;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);
        fStore = FirebaseFirestore.getInstance();

       fAuth = FirebaseAuth.getInstance();
       phone= findViewById(R.id.phone);
       code = findViewById(R.id.codeEnter);
       LoginBtn= findViewById(R.id.cirLoginButton);
       resend = findViewById(R.id.resend);
       progressbar= findViewById(R.id.progressbar1);
       state=findViewById(R.id.state);
       inputotp = findViewById(R.id.inputotp);
       progressBar1 = findViewById(R.id.progressbar2);
   //    one.findViewById(R.id.linearLayout1);


       LoginBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
            if(!verificationInProgress){
                if(!phone.getText().toString().isEmpty()&& phone.getText().toString().length() == 10){
                    String phNum = "+91"+phone.getText().toString();
                    Log.d(TAG,"Phone Number is " + phNum);
                    //    one.setVisibility(View.VISIBLE);
                    LoginBtn.setVisibility(View.INVISIBLE);
                    progressbar.setVisibility(View.VISIBLE);
                    state.setVisibility(View.VISIBLE);
                    state.setText("Sending OTP...");

                    requestOtp(phNum);



                }
                else{
                    phone.setError("Phone Number Not Valid");
                }
            }
            else{
                String userOTP  =code.getText().toString();
                if(userOTP.length() == 6){
                    LoginBtn.setVisibility(View.INVISIBLE);
                progressBar1.setVisibility(View.VISIBLE);
                    PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationID,userOTP);
                    verifyAuth(credential);


                }
                else{
                    code.setError("Please Enter Valid OTP.");
                }

            }

           }
       });
    resend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            String phnum = "+91"+phone.getText().toString();
            requestOtp(phnum);
        }
    });

    }

    @Override
    protected void onStart() {
        super.onStart();
        if(fAuth.getCurrentUser()!=null){
            progressbar.setVisibility(View.VISIBLE);
            state.setText("Checking");
            state.setVisibility(View.VISIBLE);
            checkUserProfile();

        }

    }


    private void verifyAuth(PhoneAuthCredential credential) {
        fAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
           if(task.isSuccessful()){
               Toast.makeText(LoginActivity.this,"Authentication is Successful",Toast.LENGTH_SHORT).show();
               checkUserProfile();


           }
           else{
               Toast.makeText(LoginActivity.this,"Authentication is Failed",Toast.LENGTH_SHORT).show();
           }
            }
        });
    }


    private void checkUserProfile() {
        DocumentReference docRef = fStore.collection("users").document(fAuth.getCurrentUser().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    finish();
                   // overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                }else{
                    startActivity(new Intent(getApplicationContext(),RegisterActivity.class));
                    finish();
                //    overridePendingTransition(R.anim.slide_in_right,R.anim.stay);
                }
            }
        });

    }

    private void requestOtp(String phNum) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phNum, 10L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                progressbar.setVisibility(View.GONE);
                state.setVisibility(View.GONE);
                inputotp.setVisibility(View.VISIBLE);
                code.setVisibility(View.VISIBLE);
                phone.setEnabled(false);
                LoginBtn.setVisibility(View.VISIBLE);
                LoginBtn.setText("Verify");
                verificationInProgress=true;
                verificationID=s;
                token=forceResendingToken;


            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {

                super.onCodeAutoRetrievalTimeOut(s);
                Toast.makeText(LoginActivity.this,"Resend OTP",Toast.LENGTH_SHORT).show();
                resend.setVisibility(View.VISIBLE);

            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                verifyAuth(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this,"Cannot Send OTP"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

/*
    private void resendVerificationCode(String phoneNumber,PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                callbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }*/

}
/*

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }*/
