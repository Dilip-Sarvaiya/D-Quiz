package com.dilip_sarvaiya_700.quiz;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_Pass extends AppCompatActivity {

    Button req_pass_reset;
    EditText email;
    private FirebaseAuth mAuth;

    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        req_pass_reset = findViewById(R.id.req_pass_reset);
        email = findViewById(R.id.email);

        progressDialog = new Dialog(Forgot_Pass.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog. setCancelable(true);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        mAuth = FirebaseAuth.getInstance();

        req_pass_reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    progressDialog.show();
                    login();
                }
            }
        });
    }

    private void login()
    {
        // calling sendPasswordResetEmail
        // open your email and write the new
        // password and then you can login
        String email_str = email.getText().toString().trim();
        mAuth.sendPasswordResetEmail(email_str).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
                    // if isSuccessful then done message will be shown
                    // and you can change the password
                    Toast.makeText(Forgot_Pass.this,"Email sent successfully !!",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(Forgot_Pass.this, LoginActivity.class);
                    startActivity(intent);
                    Forgot_Pass.this.finish();
                    progressDialog.dismiss();
                }
                else {
                    Toast.makeText(Forgot_Pass.this,"Something went wrong ! Please Try Again Later",Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Forgot_Pass.this, "Something went wrong ! Please Try Again Later", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validate() {

        String emailStr = email.getText().toString().trim();
        if(emailStr.isEmpty())
        {
            email.setError("Enter Your E-Mail ID");
            return false;
        }
        if(!emailStr.isEmpty())
        {
            if( ! Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                email.setError("Enter proper E-Mail ID");
                return false;
            }
        }
        return true;
    }
}