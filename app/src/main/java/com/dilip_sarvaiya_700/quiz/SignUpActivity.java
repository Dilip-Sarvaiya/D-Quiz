package com.dilip_sarvaiya_700.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;

import static android.content.ContentValues.TAG;

public class SignUpActivity extends AppCompatActivity {

    private EditText full_name,email_id,password,confirmPass;
    Button signupB;
    ImageView backB;

    private FirebaseAuth mAuth;

    private String emailStr,passStr,confirmPassstr,namestr;

    private Dialog progressDialog;

    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        full_name = findViewById(R.id.full_name);
        email_id = findViewById(R.id.email_id);
        password = findViewById(R.id.password_signup);
        confirmPass = findViewById(R.id.confirmPass_signup);

        signupB = findViewById(R.id.signupB);
        backB = findViewById(R.id.backB);

        progressDialog = new Dialog(SignUpActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog.setCancelable(true);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Registering user...");

        mAuth = FirebaseAuth.getInstance();

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        signupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    signupNewUser();
                }
            }
        });
    }

    private boolean validate() {

        namestr = full_name.getText().toString().trim();
        emailStr = email_id.getText().toString().trim();
        passStr = password.getText().toString().trim();
        confirmPassstr = confirmPass.getText().toString().trim();

        if(namestr.isEmpty())
        {
            full_name.setError("Enter Your Name");
            return false;
        }
        if(emailStr.isEmpty())
        {
            email_id.setError("Enter Your E-Mail ID");
            return false;
        }
        if(!emailStr.isEmpty())
        {
            if( ! Patterns.EMAIL_ADDRESS.matcher(emailStr).matches()) {
                email_id.setError("Enter proper E-Mail ID");
                return false;
            }
        }
        if(passStr.isEmpty())
        {
            password.setError("Enter Your Password");
            return false;
        }
        if(confirmPassstr.isEmpty())
        {
            confirmPass.setError("Enter Your Confirm Password");
            return false;
        }
        if(passStr.compareTo(confirmPassstr) != 0)
        {
            Toast.makeText(this, "Password and Confirm Password should be same !", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        full_name.setText("");
        email_id.setText("");
        password.setText("");
        confirmPass.setText("");
    }

    private void signupNewUser() {
        progressDialog.show();
        mAuth.createUserWithEmailAndPassword(emailStr,passStr)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            //Sign-in success and update sign'd user information

                            Toast.makeText(SignUpActivity.this, "Sign Up Successfull", Toast.LENGTH_SHORT).show();
                            DBQuery.createUserData(emailStr,namestr, new MyCompleteListener() {
                                @Override
                                public void onSuccess() {
                                    DBQuery.loadData(new MyCompleteListener() {
                                        @Override
                                        public void onSuccess() {
                                            progressDialog.dismiss();
                                            Intent intent = new Intent(SignUpActivity.this,MainActivity.class);
                                            intent.putExtra("signed_up","signed_up");
                                            startActivity(intent);
                                            SignUpActivity.this.finish();
                                        }

                                        @Override
                                        public void onFailure() {
                                            Toast.makeText(SignUpActivity.this, "Something went wrong ! Please Try Again Later", Toast.LENGTH_SHORT).show();
                                            progressDialog.dismiss();
                                        }
                                    });
                                }

                                @Override
                                public void onFailure() {
                                    Toast.makeText(SignUpActivity.this, "Something went wrong ! Please Try Again Later", Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                }
                            });
                        }
                        else
                        {
                            //If Sign in fails display the message
                            progressDialog.dismiss();
                            Toast.makeText(SignUpActivity.this, "Authentication failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {

    }
}