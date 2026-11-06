package edu.uga.cs.statecapitalsquiz;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizResultsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizResultsFragment extends Fragment {

    private QuizViewModel quizViewModel;
    StateQuizData stateQuizData;
    long quizId;
    TextView quizScoreText;
    Button goToHomeButton;
    private int correctAnswers;

    public QuizResultsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment QuizResultsFragment.
     */
    public static QuizResultsFragment newInstance(long quizID) {
        QuizResultsFragment fragment = new QuizResultsFragment();
        Bundle args = new Bundle();
        args.putLong("quizId", quizID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            quizId = getArguments().getLong("quizId");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_quiz_results, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        quizViewModel = new ViewModelProvider(requireActivity()).get(QuizViewModel.class);
        correctAnswers = quizViewModel.getCorrectAnswers();

        quizScoreText = view.findViewById(R.id.quizScoreText);
        goToHomeButton = view.findViewById(R.id.goToHomeButton);

        quizScoreText.setText(correctAnswers + "/6");
        if (correctAnswers > 4) {
            quizScoreText.setTextColor(Color.GREEN);
        } else if (correctAnswers > 2) {
            quizScoreText.setTextColor(Color.BLACK);
        } else {
            quizScoreText.setTextColor(Color.RED);
        }

        goToHomeButton.setOnClickListener(v -> requireActivity().finish());

        stateQuizData = new StateQuizData(getActivity());
        stateQuizData.open();
        new QuizResultWriter().execute();
    }

    private class QuizResultWriter extends AsyncTask<Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            stateQuizData.updateQuiz(quizId, correctAnswers);
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            stateQuizData.close();
        }
    }
}