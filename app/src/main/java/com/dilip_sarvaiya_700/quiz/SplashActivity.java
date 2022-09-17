package com.dilip_sarvaiya_700.quiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashActivity extends AppCompatActivity {

    private TextView app_name;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        app_name = findViewById(R.id.app_name);


        Typeface typeface = ResourcesCompat.getFont(this,R.font.blacklist1);
        app_name.setTypeface(typeface);

        Animation anim = AnimationUtils.loadAnimation(this,R.anim.myanim);
        app_name.setAnimation(anim);

        FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();

//        final FirebaseDatabase database = FirebaseDatabase.getInstance("https://food-ordering-app-f1e8e-default-rtdb.firebaseio.com/");

        DBQuery.g_firestore = FirebaseFirestore.getInstance();

        new Thread()
        {
            @Override
            public void run() {
                try {
                    sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
//                startActivity(intent);
//                SplashActivity.this.finish();

                if(mAuth.getCurrentUser()!=null)
                {
                    DBQuery.loadData(new MyCompleteListener() {
                        @Override
                        public void onSuccess() {
                            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                            startActivity(intent);
                            SplashActivity.this.finish();
                        }
                        @Override
                        public void onFailure() {
                            Toast.makeText(SplashActivity.this, "Something went wrong ! Please Try Again Later   ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SplashActivity.this.finish();
                }
            }
        }.start();
    }
}