package edu.uga.cs.statecapitalsquiz;

import static androidx.core.app.PendingIntentCompat.getActivity;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private List<StateQuizData.QuizQuestion> questionList;
    private StateQuizData stateQuizData;

    private StateQuizPagerAdapter sqpAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_quiz);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ViewPager2 pager = findViewById(R.id.viewpager);
        sqpAdapter = new StateQuizPagerAdapter(getSupportFragmentManager(), getLifecycle());

        //Pass info from database here (will need to update StateQuizFragment and QuizResultsFragment to accept more info in newInstance method)
        questionList = new ArrayList<StateQuizData.QuizQuestion>();
        stateQuizData = new StateQuizData(this); //?

        stateQuizData.open();

        new QuizDBCreator().execute();

        //Put below in DBReader function like ReviewJobLeadsFragment?

        //long quizId = stateQuizData.createRandomQuiz();
        //questionList = stateQuizData.getQuizQuestions(quizId);


        //Add individual questions as parameter for each quiz fragment
//        for (int i = 0; i < 6; i++) {
//            StateQuizFragment sqf = StateQuizFragment.newInstance(i, questionList.get(i));
//            sqpAdapter.addFragment(sqf);
//        }

        //QuizResultsFragment qrf = QuizResultsFragment.newInstance();
        //sqpAdapter.addFragment(qrf);

        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setAdapter(sqpAdapter);
    }

    public class QuizDBCreator extends AsyncTask<Void, List<StateQuizData.QuizQuestion>> {

        long quizID;
        @Override
        protected List<StateQuizData.QuizQuestion> doInBackground(Void... params) {


            long quizId = stateQuizData.createRandomQuiz();
            quizID = quizId;
            List<StateQuizData.QuizQuestion> quizQuestions = stateQuizData.getQuizQuestions(quizId);

            return quizQuestions;
        }

        @Override
        protected void onPostExecute(List<StateQuizData.QuizQuestion> quizQuestions) {
            questionList = quizQuestions;
            for (int i = 0; i < 6; i++) {
                StateQuizFragment sqf = StateQuizFragment.newInstance(i, questionList.get(i), quizID);
                sqpAdapter.addFragment(sqf);
            }

            QuizResultsFragment qrf = QuizResultsFragment.newInstance(quizID);
            sqpAdapter.addFragment(qrf);
        }

    }

}