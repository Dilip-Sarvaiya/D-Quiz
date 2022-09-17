package com.dilip_sarvaiya_700.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dilip_sarvaiya_700.quiz.Adapters.AnswersAdapter;
import com.dilip_sarvaiya_700.quiz.Adapters.TestAdapter;

public class AnswersActivity extends AppCompatActivity {

    RecyclerView answersView;
    private Toolbar toolbar;

    private Dialog progressDialog;
    private TextView dialogText;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answers);

        toolbar = findViewById(R.id.aa_toolbar);
        answersView = findViewById(R.id.aa_recycler_view);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("ANSWERS");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        progressDialog = new Dialog(AnswersActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog. setCancelable(true);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        answersView.setLayoutManager(layoutManager);

        AnswersAdapter adapter = new AnswersAdapter(DBQuery.g_quesList);
        answersView.setAdapter(adapter);
        progressDialog.dismiss();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home)
        {
            AnswersActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}