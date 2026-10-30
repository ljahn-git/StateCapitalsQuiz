package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StateQuizRecyclerAdapter extends RecyclerView.Adapter<StateQuizRecyclerAdapter.StateQuizHolder>
{
    //private final Context context;
    //private List<StateQuiz> values;
    //private List<StateQuiz> originalValues;

//    public StateQuizRecyclerAdapter(Context context, List<StateQuiz> stateQuizList) {
//        this.context = context;
//        this.values = stateQuizList;
//        this.originalValues = new ArrayList<StateQuiz>(stateQuizList);
//    }

//    public void sync() {
//        originalValues = new ArrayList<StateQuiz> (values);
//    }
//
    public static class StateQuizHolder extends RecyclerView.ViewHolder {

        //Declare layout elements for displayed info (probably textviews)
        public StateQuizHolder(View itemView) {
            super(itemView);

            //Find layout elements in layout file
        }
    }

    @NonNull
    @Override
    public StateQuizHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.state_quiz, parent, false);
        return new StateQuizHolder(view);
    }

    @Override
    public void onBindViewHolder(StateQuizHolder holder, int position) {
        //StateQuiz stateQuiz = values.get(position);

        //Set values like holder.value.setText();
    }

    @Override
    public int getItemCount() {
//        //if (values != null) {
//            return values.size();
//        } else {
            return 0;
//        }
    }

    //Filter method for potential search feature?
}
