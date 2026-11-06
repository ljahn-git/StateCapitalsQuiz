package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class StateQuizRecyclerAdapter extends RecyclerView.Adapter<StateQuizRecyclerAdapter.StateQuizHolder>
{
    private final Context context;
    private final List<StateQuizData.Quiz> quizList;

    public StateQuizRecyclerAdapter(Context context, List<StateQuizData.Quiz> quizList) {
        this.context = context;
        this.quizList = quizList;
    }

    public static class StateQuizHolder extends RecyclerView.ViewHolder {
        public TextView quizValue1;
        public TextView quizValue2;
        public TextView quizValue3;

        public StateQuizHolder(View itemView) {
            super(itemView);
            quizValue1 = itemView.findViewById(R.id.quizValue1);
            quizValue2 = itemView.findViewById(R.id.quizValue2);
            quizValue3 = itemView.findViewById(R.id.quizValue3);
        }
    }

    @NonNull
    @Override
    public StateQuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_quiz, parent, false);
        return new StateQuizHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StateQuizHolder holder, int position) {
        StateQuizData.Quiz quiz = quizList.get(position);
        String quizDateTime = quiz.getQuizDate();
        String datePart;
        String timePart;
        if (quizDateTime != null && quizDateTime.contains(" ")) {
            String[] parts = quizDateTime.split(" ");
            datePart = parts[0];
            timePart = parts.length > 1 ? parts[1] : "";
        } else {
            datePart = quizDateTime;
            timePart = "";
        }
        holder.quizValue1.setText("Date: " + datePart);
        holder.quizValue2.setText("Time: " + timePart);
        holder.quizValue3.setVisibility(View.VISIBLE);
        holder.quizValue3.setText("Score: " + quiz.getCorrectAnswers() + " / 6");
    }

    @Override
    public int getItemCount() {
        return quizList != null ? quizList.size() : 0;
    }
}
