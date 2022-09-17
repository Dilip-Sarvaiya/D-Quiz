package com.dilip_sarvaiya_700.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import static com.dilip_sarvaiya_700.quiz.DBQuery.g_catList;
import static com.dilip_sarvaiya_700.quiz.DBQuery.g_selected_test_index;
import static com.dilip_sarvaiya_700.quiz.DBQuery.loadQuestions;

public class StartTestActivity extends AppCompatActivity {

    private TextView catName,testNo,totalQ,bestScore,time;
    private Button startTestB;
    private ImageView backB;
    private Dialog progressDialog;
    private TextView dialogText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_test);
        init();

        progressDialog = new Dialog(StartTestActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog. setCancelable(true);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        loadQuestions(new MyCompleteListener(){

            @Override
            public void onSuccess() {
                setData();
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                progressDialog.dismiss();
                Toast.makeText(StartTestActivity.this, "Something went wrong ! Please Try Again Later   ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setData() {
        catName.setText(g_catList.get(DBQuery.g_selected_cat_index).getName());
        testNo.setText("Test No. "+String.valueOf(DBQuery.g_selected_test_index + 1));
        totalQ.setText(String.valueOf(DBQuery.g_quesList.size()));
        bestScore.setText(String.valueOf(DBQuery.g_testList.get(DBQuery.g_selected_test_index).getTopScore()));
        time.setText(String.valueOf(DBQuery.g_testList.get(g_selected_test_index).getTime())+" m");
    }


    public void init()
    {
        catName = findViewById(R.id.st_cat_name);
        testNo = findViewById(R.id.st_test_no);
        totalQ = findViewById(R.id.st_total_ques);
        bestScore = findViewById(R.id.st_best_score);
        time = findViewById(R.id.st_time);

        startTestB = findViewById(R.id.start_testB);
        backB = findViewById(R.id.st_backB);

        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartTestActivity.this.finish();
            }
        });

        startTestB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartTestActivity.this,QuestionsActivity.class);
                startActivity(intent);

            }
        });

    }
}