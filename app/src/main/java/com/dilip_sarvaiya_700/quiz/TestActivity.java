package com.dilip_sarvaiya_700.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.dilip_sarvaiya_700.quiz.Adapters.TestAdapter;

public class TestActivity extends AppCompatActivity {

    RecyclerView test_recycler_view;
    private Toolbar toolbar;

    private Dialog progressDialog;
    private TextView dialogText;
    private TestAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               onBackPressed();
            }
        });

        getSupportActionBar().setTitle(DBQuery.g_catList.get(DBQuery.g_selected_cat_index).getName());

        test_recycler_view = findViewById(R.id.test_recycler_view);

        progressDialog = new Dialog(TestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog. setCancelable(true);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        test_recycler_view.setLayoutManager(layoutManager);

//        loadTestData();

        DBQuery.loadTestData(new MyCompleteListener() {
            @Override
            public void onSuccess() {

                DBQuery.loadMyScores(new MyCompleteListener() {
                    @Override
                    public void onSuccess() {
                        adapter = new TestAdapter(DBQuery.g_testList);
                        test_recycler_view.setAdapter(adapter);
                        progressDialog.dismiss();
                    }

                    @Override
                    public void onFailure() {
                        progressDialog.dismiss();
                        Toast.makeText(TestActivity.this, "Something went wrong ! Please Try Again Later   ", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(TestActivity.this, "Something went wrong ! Please Try Again Later   ", Toast.LENGTH_SHORT).show();
            }
        });

    }



//    @Override
//    public void onBackPressed()
//    {
//        // code here to show dialog
//
////        Intent intent = new Intent(TestActivity.this,MainActivity.class);
////        startActivity(intent);
//        Intent intent = new Intent(TestActivity.this,MainActivity.class);
//        startActivity(intent);
//        TestActivity.this.finish();
//    }

//    private void loadTestData() {
//        testList = new ArrayList<>();
//        testList.add(new TestModel("1",50,20));
//        testList.add(new TestModel("2",80,20));
//        testList.add(new TestModel("3",0,25));
//        testList.add(new TestModel("4",10,40));
//    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home)
        {
            TestActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}