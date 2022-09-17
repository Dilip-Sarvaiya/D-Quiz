package com.dilip_sarvaiya_700.quiz.Adapters;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.dilip_sarvaiya_700.quiz.DBQuery;
import com.dilip_sarvaiya_700.quiz.QuestionsActivity;
import com.dilip_sarvaiya_700.quiz.R;

import static com.dilip_sarvaiya_700.quiz.DBQuery.ANSWERED;
import static com.dilip_sarvaiya_700.quiz.DBQuery.NOT_VISITED;
import static com.dilip_sarvaiya_700.quiz.DBQuery.REVIEW;
import static com.dilip_sarvaiya_700.quiz.DBQuery.UNANSWERED;

public class QuestionGridAdapter extends BaseAdapter {


    private int numOfQues;
    private Context context;

    public QuestionGridAdapter(Context context,int numOfQues) {
        this.context = context;
        this.numOfQues = numOfQues;
    }

    @Override
    public int getCount() {
        return numOfQues;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View myView;

        if (convertView == null) {
            myView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ques_grid_item, parent, false);
        } else {
            myView = convertView;
        }

        myView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(context instanceof QuestionsActivity)
                    ((QuestionsActivity)context).goToQuestion(position);
            }
        });
        TextView quesTV =myView.findViewById(R.id.ques_num);
        quesTV.setText(String.valueOf(position+1));

        switch (DBQuery.g_quesList.get(position).getStatus())
        {
            case NOT_VISITED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.grey)));
                break;

            case UNANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.red
                )));
                break;

            case ANSWERED:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.green)));
                break;

            case REVIEW:
                quesTV.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(myView.getContext(),R.color.pink)));
                break;

            default:
                break;
        }

        return  myView;
    }
}
