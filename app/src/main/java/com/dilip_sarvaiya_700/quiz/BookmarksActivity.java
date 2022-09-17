package com.dilip_sarvaiya_700.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.dilip_sarvaiya_700.quiz.Adapters.AnswersAdapter;
import com.dilip_sarvaiya_700.quiz.Adapters.BookmarksAdapter;
import com.dilip_sarvaiya_700.quiz.Adapters.TestAdapter;

public class BookmarksActivity extends AppCompatActivity {

    private RecyclerView questionsView;
    Toolbar toolbar;

    private Dialog progressDialog;
    private TextView dialogText;
    private TestAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bookmarks);
        toolbar = findViewById(R.id.ba_toolbar);
        questionsView = findViewById(R.id.ba_recycler_view);
        setSupportActionBar(toolbar);


        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Saved Questions");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new Dialog(BookmarksActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog. setCancelable(true);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        questionsView.setLayoutManager(layoutManager);

        DBQuery.loadBookmarks(new MyCompleteListener() {
            @Override
            public void onSuccess() {
                BookmarksAdapter adapter = new BookmarksAdapter(DBQuery.g_bookmarksList);
                questionsView.setAdapter(adapter);
                progressDialog.dismiss();

            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home)
        {
            BookmarksActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}