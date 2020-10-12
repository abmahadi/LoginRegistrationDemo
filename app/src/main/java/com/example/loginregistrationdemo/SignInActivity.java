package com.example.loginregistrationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static java.lang.Integer.parseInt;

public class SignInActivity extends AppCompatActivity {

    private EditText editText_name;
    private EditText editText_email;
    private EditText editText_password;
    private EditText editText_confirm_password;
    private EditText editText_mobile;
    private TextView textView_logIn;

    private Button button_signIn;

    DatabaseReference databaseReference;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        init();

        button_signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();

            }
        });
        textView_logIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignInActivity.this,LoginActivity.class));
            }
        });

    }

    private void signIn() {

        final ProgressDialog progressDialog = new ProgressDialog(SignInActivity.this,R.style.Theme_AppCompat_DayNight_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String name = editText_name.getText().toString().trim();
        final String email=editText_email.getText().toString().trim();
        final String password= editText_password.getText().toString().trim();
        final String mobile = editText_mobile.getText().toString().trim();
        String confirmPassword = editText_confirm_password.getText().toString().trim();

        if(name.isEmpty())
        {
            editText_name.setError("Enter a valid Name");
            editText_name.requestFocus();
            progressDialog.dismiss();
            return;
        }

        if(email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            editText_email.setError("Enter an valid email address");
            editText_email.requestFocus();
            progressDialog.dismiss();
            return;
        }



        //checking the validity of the password
        if(password.isEmpty())
        {
            editText_password.setError("Enter a valid password");
            editText_password.requestFocus();
            progressDialog.dismiss();
            return;
        }
        if(password.equals(confirmPassword) ==false){
            editText_confirm_password.setError("password not matching");
            editText_confirm_password.requestFocus();
            progressDialog.dismiss();
            return;

        }
        if(mobile.isEmpty() || mobile.length() !=11 )
        {
            editText_mobile.setError("Enter a valid Mobile Number");
            editText_mobile.requestFocus();
            progressDialog.dismiss();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful()){
                    //save value in database
                    User newUser = new User(name,email,mobile);
                    databaseReference.push().setValue(newUser);

                    Toast.makeText(SignInActivity.this, "Successfull", Toast.LENGTH_SHORT).show();

                }else {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(SignInActivity.this, "User is already registared", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(SignInActivity.this, ""+task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }



    private void init() {
        editText_name = findViewById(R.id.editText_name);
        editText_email = findViewById(R.id.editText_Email_signin);
        editText_password = findViewById(R.id.editText_password_signin);
        editText_confirm_password = findViewById(R.id.editText_confirm_password_signin);
        editText_mobile = findViewById(R.id.editText_mobile);
        textView_logIn = findViewById(R.id.textView_logIn);

        button_signIn = findViewById(R.id.button_signIn);
        databaseReference= FirebaseDatabase.getInstance().getReference("UserData");
        mAuth = FirebaseAuth.getInstance();
    }
}
