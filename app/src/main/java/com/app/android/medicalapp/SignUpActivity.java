package com.app.android.medicalapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText EditTextName;
    private EditText EditTextEmail;
    private EditText EditTextPassword;
    private EditText EditTextReEnterPassword;
    //private ProgressBar progressBar;


    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mAuth = FirebaseAuth.getInstance();
        EditTextName = (EditText) findViewById(R.id.input_name);
        EditTextEmail = (EditText)findViewById(R.id.input_email);
        EditTextPassword = (EditText)findViewById(R.id.input_password);
        EditTextReEnterPassword= (EditText)findViewById(R.id.input_re_enter_password);
      //  progressBar=(ProgressBar)findViewById(R.id.progressbar);





        findViewById(R.id.link_login).setOnClickListener(this);
        findViewById(R.id.btn_signup).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {

        switch(view.getId())
        {
            case R.id.link_login:

                startActivity(new Intent(this,LoginActivity.class));
                break;

            case R.id.btn_signup:

                RegisterUser();

                break;
        }

    }

    private void RegisterUser() {


        //perform validations of the input data
        String name=EditTextName.getText().toString().trim();
         String email = EditTextEmail.getText().toString().trim();
         String password=EditTextPassword.getText().toString().trim();
         String re_enterPassword=EditTextReEnterPassword.getText().toString().trim();

         if(name.isEmpty())
         {
             EditTextName.setError("Name is required");
             EditTextName.requestFocus();
             return;
         }
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
        if(re_enterPassword.isEmpty())
        {
            EditTextReEnterPassword.setError("PASSWORD is required");
            EditTextReEnterPassword.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            EditTextEmail.setError("Entered Email is not valid");
            EditTextEmail.requestFocus();
            return;
        }
        if(password.length()<6)
        {
            EditTextPassword.setError("Password length should be more than 6 characters");
            EditTextPassword.requestFocus();
            return;
        }
        if(!password.equals(re_enterPassword))
        {
            EditTextReEnterPassword.setError("Passwords do not match!Try again");
            EditTextReEnterPassword.requestFocus();
            return;
        }

       // progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                  //  progressBar.setVisibility(View.GONE);
                if(task.isSuccessful())
                {


                    Toast.makeText(getApplicationContext(),"User Registered Successfully",Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent( SignUpActivity.this ,LoginActivity.class);
                    startActivity(intent);
                }
                else
                {
                    if(task.getException() instanceof FirebaseAuthUserCollisionException)
                    {
                        Toast.makeText(getApplicationContext(),"This email is already in use",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });





    }
}
