package com.dilip_sarvaiya_700.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import static com.dilip_sarvaiya_700.quiz.DBQuery.getUserData;
import static com.dilip_sarvaiya_700.quiz.DBQuery.loadData;

public class MyProfileActivity extends AppCompatActivity {

    private EditText name,email,phone;
    private LinearLayout editB,button_layout;
    private Button cancelB,saveB;
    private TextView profileText;
    private Toolbar toolbar;
    private String nameStr,phoneStr;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("My Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        name = findViewById(R.id.mp_name);
        email = findViewById(R.id.mp_email);
        phone = findViewById(R.id.mp_phone);

        profileText = findViewById(R.id.profile_text);
        editB = findViewById(R.id.editB);
        cancelB = findViewById(R.id.cancelB);
        saveB = findViewById(R.id.saveB);
        button_layout = findViewById(R.id.button_layout);

        progressDialog = new Dialog(MyProfileActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog. setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Updating Data...");

        disableEditing();

        editB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enableEditing();
            }
        });

        cancelB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                disableEditing();
            }
        });

        saveB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate())
                {
                    saveData();
                }
            }
        });
    }

    private void saveData() {
        progressDialog.show();

        if(phoneStr.isEmpty())
            phoneStr=null;

        DBQuery.saveProfileData(nameStr, phoneStr, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                Toast.makeText(MyProfileActivity.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();

                disableEditing();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(MyProfileActivity.this, "Something went wrong ! Please try again later.", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private boolean validate() {
        nameStr = name.getText().toString();
        phoneStr = phone.getText().toString();

        if(nameStr.isEmpty())
        {
            name.setError("Name can not be empty !");
            return false;
        }

        if(!phoneStr.isEmpty())
        {
            if( !(phoneStr.length() == 10) && (TextUtils.isDigitsOnly(phoneStr)))
            {
                phone.setError("Enter Valid Phone Number");
                return false;
            }
        }
        return true;
    }

    private void enableEditing() {
        name.setEnabled(true);
//        email.setEnabled(false);
        phone.setEnabled(true);

        button_layout.setVisibility(View.VISIBLE);
    }

    private void disableEditing() {
        name.setEnabled(false); 
        email.setEnabled(false);
        phone.setEnabled(false);

        button_layout.setVisibility(View.GONE);

        name.setText(DBQuery.myProfile.getName());
        email.setText(DBQuery.myProfile.getEmail());
        phone.setText(DBQuery.myProfile.getPhone());

        if(DBQuery.myProfile.getPhone()!=null)
            phone.setText(DBQuery.myProfile.getPhone());

        String proileName = DBQuery.myProfile.getName();
        profileText.setText(proileName.toUpperCase().substring(0,1));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home)
        {
            MyProfileActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}