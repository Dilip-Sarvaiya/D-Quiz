package com.dilip_sarvaiya_700.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dilip_sarvaiya_700.quiz.Adapters.TestAdapter;
import com.dilip_sarvaiya_700.quiz.Models.QuestionModel;

import java.util.concurrent.TimeUnit;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreTV,timeTV,totalQTV,correctQTV,wrongQTV,unattemptedQTV;
    private Button leaderB,reAttemptB,viewAnsB;
    private long timeTaken;

    private Dialog progressDialog;
    private TextView dialogText;
    private int finalScore;
    private TestAdapter adapter;

    Fragment fragment ;

//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(ScoreActivity.this,TestActivity.class);
//        startActivity(intent);
//        ScoreActivity.this.finish();
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

        getSupportActionBar().setTitle("Result");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        progressDialog = new Dialog(ScoreActivity.this);
        progressDialog.setContentView(R.layout.dialog_layout);
        progressDialog. setCancelable(false);
        progressDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);

        dialogText = progressDialog.findViewById(R.id.dialog_text);
        dialogText.setText("Loading...");

        progressDialog.show();

        init();

        loadData();

        setBookMarks();

        leaderB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this,MainActivity.class);
                intent.putExtra("flag_on","flag_on");
                startActivity(intent);
                ScoreActivity.this.finish();
            }
        });

        viewAnsB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScoreActivity.this,AnswersActivity.class);
                startActivity(intent);
            }
        });
        reAttemptB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reAttempt();
            }
        });
        saveResult();
    }

    private void setBookMarks() {

        for(int i=0; i< DBQuery.g_quesList.size(); i++)
        {
            QuestionModel question = DBQuery.g_quesList.get(i);
            if(question.isBookmarked())
            {
                if(! DBQuery.g_bmIdList.contains(question.getqID()))
                {
                    DBQuery.g_bmIdList.add(question.getqID());
                    DBQuery.myProfile.setBookmarksCount(DBQuery.g_bmIdList.size());
                }
            }
            else
            {
                if(DBQuery.g_bmIdList.contains(question.getqID()))
                {
                    DBQuery.g_bmIdList.remove(question.getqID());
                    DBQuery.myProfile.setBookmarksCount(DBQuery.g_bmIdList.size());
                }
            }
        }
    }


    private void reAttempt() {

        for(int i=0; i<DBQuery.g_quesList.size(); i++)
        {
            DBQuery.g_quesList.get(i).setSelectedAns(-1);
            DBQuery.g_quesList.get(i).setStatus(DBQuery.NOT_VISITED);
        }
        Intent intent = new Intent(ScoreActivity.this,StartTestActivity.class);
        startActivity(intent);
        finish();
    }

    private void saveResult()
    {
        DBQuery.saveResult(finalScore, new MyCompleteListener() {
            @Override
            public void onSuccess() {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure() {
                Toast.makeText(ScoreActivity.this, "Something went wrong ! Please try again later !", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
            }
        });
    }

    private void loadData() {
        int correctQ=0,wrongQ=0,unattemptQ=0;
        for(int i=0; i<DBQuery.g_quesList.size(); i++)
        {
            if(DBQuery.g_quesList.get(i).getSelectedAns() == -1)
            {
                unattemptQ++;
            }
            else
            {
                if(DBQuery.g_quesList.get(i).getSelectedAns() == DBQuery.g_quesList.get(i).getCorrectAns())
                {
                    correctQ++;
                }
                else
                {
                    wrongQ++;
                }
            }
        }
        correctQTV.setText(String.valueOf(correctQ));
        wrongQTV.setText(String.valueOf(wrongQ));
        unattemptedQTV.setText(String.valueOf(unattemptQ));

        totalQTV.setText(String.valueOf(DBQuery.g_quesList.size()));

        finalScore = (correctQ*100)/DBQuery.g_quesList.size();
        scoreTV.setText(String.valueOf(finalScore));

        timeTaken = getIntent().getLongExtra("TIME_TAKEN",0);

        String time =  String.format("%02d:%02d min",
                TimeUnit.MILLISECONDS.toMinutes(timeTaken),
                TimeUnit.MILLISECONDS.toSeconds(timeTaken) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timeTaken))
        );
        timeTV.setText(time);
    }

    private  void init()
    {
        scoreTV = findViewById(R.id.score);
        timeTV = findViewById(R.id.time);
        totalQTV = findViewById(R.id.totalQ);
        correctQTV = findViewById(R.id.correctQ);
        wrongQTV = findViewById(R.id.wrongQ);
        unattemptedQTV = findViewById(R.id.un_attempted);
        leaderB = findViewById(R.id.leaderB);
        reAttemptB = findViewById(R.id.reattemptB);
        viewAnsB = findViewById(R.id.view_answerB);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.nav_home)
        {
            ScoreActivity.this.finish();
        }
        return super.onOptionsItemSelected(item);
    }
}