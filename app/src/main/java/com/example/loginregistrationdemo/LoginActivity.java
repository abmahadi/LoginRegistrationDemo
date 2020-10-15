package com.example.loginregistrationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {

    private EditText editText_email;
    private EditText editText_password;
    private TextView textView_signIn;
    TextView snacksbar_view;
    LottieAnimationView progressbar;

    CheckBox rememberMe;

    private Button button_logIn;
    private FirebaseAuth mAuth;
    SessionManager sessionManager;
    ConnectionDetect connectionDetect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        init();
        progressbar.cancelAnimation();

        if(sessionManager.checkRememberMe()){
            HashMap<String,String> getRememberMeDetails = sessionManager.getRememberingData();
            editText_email.setText(getRememberMeDetails.get(SessionManager.KEY_EMAIL));
            editText_password.setText(getRememberMeDetails.get(SessionManager.KEY_PASSWORD));
        }

        button_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // userLogIn();
                if (connectionDetect.isConnectingToInternet()) {
                    userLogIn();
                } else {

                    Snackbar snackbar = Snackbar
                            .make(snacksbar_view,
                                    "Please check internet",
                                    Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });

        textView_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignInActivity.class));
            }
        });


    }

    private void userLogIn() {

//        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,R.style.Theme_AppCompat_DayNight_Dialog);
//        progressDialog.setIndeterminate(true);
//        progressDialog.setMessage("Authenticating...");
//        progressDialog.show();
        progressbar.playAnimation();


        String email=editText_email.getText().toString().trim();
        String password= editText_password.getText().toString().trim();

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editText_email.setError("Enter an valid email address");
            editText_email.requestFocus();
//            progressDialog.dismiss();
            progressbar.cancelAnimation();
            return;
        }



        //checking the validity of the password
        if(password.isEmpty())
        {
            editText_password.setError("Enter a valid password");
            editText_password.requestFocus();
//            progressDialog.dismiss();
            progressbar.cancelAnimation();
            return;
        }
        if(rememberMe.isChecked()){
           // SessionManager sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSOIN_REMEMBERME);
            sessionManager.creatRememberMeSession(email,password);
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
               // progressDialog.dismiss();
                progressbar.cancelAnimation();

                if(task.isSuccessful()){
                    //open activity
                    startActivity(new Intent(LoginActivity.this,AfterLoginActivity.class));


                }else{
                    new AlertDialog.Builder(LoginActivity.this)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setTitle("warning")
                            .setMessage("The email/password does not match with our credentials")
                            .setNegativeButton("ok",null).show();
                   // Toast.makeText(LoginActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }

            }
        });


    }

    private void init() {

        editText_email = findViewById(R.id.editText_Email);
        editText_password = findViewById(R.id.editText_Password);
        textView_signIn = findViewById(R.id.textView_signIn);
        button_logIn = findViewById(R.id.Button_Login);
        snacksbar_view = findViewById(R.id.snackbar_view);
        progressbar = findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();
        connectionDetect = new ConnectionDetect(this);
        rememberMe = findViewById(R.id.checkbox_remember_me);
        sessionManager = new SessionManager(LoginActivity.this, SessionManager.SESSOIN_REMEMBERME);

    }
}
