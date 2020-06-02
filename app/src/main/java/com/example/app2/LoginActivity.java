package com.example.app2;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import io.opencensus.tags.Tag;

public class LoginActivity extends AppCompatActivity {
    public static final String TAG = "TAG";
    EditText phone,code;
    Button LoginBtn;
    TextView resend,state;
    ProgressBar progressbar;
    FirebaseAuth fAuth;
    String verificationID;
    PhoneAuthProvider.ForceResendingToken token;
    //Layout one;
  //  LinearLayout one;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       setContentView(R.layout.activity_main);

       fAuth = FirebaseAuth.getInstance();
       phone= findViewById(R.id.phone);
       code = findViewById(R.id.codeEnter);
       LoginBtn= findViewById(R.id.cirLoginButton);
       resend = findViewById(R.id.resend);
       progressbar= findViewById(R.id.progressbar);
       state=findViewById(R.id.state);
   //    one.findViewById(R.id.linearLayout1);


       LoginBtn.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View v) {
               if(!phone.getText().toString().isEmpty()&& phone.getText().toString().length() == 10){
                String phNum = "+91"+phone.getText().toString();
                Log.d(TAG,"Phone Number is " + phNum);
            //    one.setVisibility(View.VISIBLE);

                requestOtp(phNum);



               }
               else{
                   phone.setError("Phone Number Not Valid");
               }

           }
       });


    }

    private void requestOtp(String phNum) {

        PhoneAuthProvider.getInstance().verifyPhoneNumber(phNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);
                verificationID=s;
                token=forceResendingToken;


            }

            @Override
            public void onCodeAutoRetrievalTimeOut(String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginActivity.this,"Cannot Send OTP"+e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

}
/*

    public void onLoginClick(View View){
        startActivity(new Intent(this,RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right,R.anim.stay);

    }*/
