package com.app.android.medicalapp;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static java.security.AccessController.getContext;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText EditTextEmail;
    private EditText EditTextPassword;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        EditTextEmail=(EditText) findViewById(R.id.input_email);
        EditTextPassword=(EditText) findViewById(R.id.input_password);
        findViewById(R.id.link_signup).setOnClickListener(this);
        findViewById(R.id.btn_login).setOnClickListener(this);

        mAuth=FirebaseAuth.getInstance();

    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.link_signup:

                startActivity(new Intent(this,SignUpActivity.class));
                break;

            case R.id.btn_login :
                userLogin();
        }

    }

    private void userLogin() {

        String email=EditTextEmail.getText().toString().trim();
        String password=EditTextPassword.getText().toString().trim();

        if(email.isEmpty())
        {
            EditTextEmail.setError("Email is required");
            EditTextEmail.requestFocus();
            return;
        }
        if(password.isEmpty())
        {
            EditTextPassword.setError("Password is required");
            EditTextPassword.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                  Intent intent=  new Intent(LoginActivity.this, HomeNavActivity.class);
                  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);//so that after user logs in after pressing back button he should not go back to the login page
                  startActivity(intent);
                  finish();
                } else
                {
                    Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
