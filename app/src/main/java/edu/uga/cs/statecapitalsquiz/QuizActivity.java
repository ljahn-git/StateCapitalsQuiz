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
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

public class QuizActivity extends AppCompatActivity {

    private List<StateQuizData.QuizQuestion> questionList;
    private StateQuizData stateQuizData;

    private QuizViewModel quizViewModel;
    private int currentPage = 0;
    long quizId;

    private StateQuizPagerAdapter sqpAdapter;
    private ViewPager2 pager;

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

        quizViewModel = new ViewModelProvider(this).get(QuizViewModel.class);

        if (savedInstanceState != null) {
            currentPage = savedInstanceState.getInt("currentPage", 0);
        }

        pager = findViewById(R.id.viewpager);
        sqpAdapter = new StateQuizPagerAdapter(getSupportFragmentManager(), getLifecycle());

        pager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        pager.setAdapter(sqpAdapter);

        pager.registerOnPageChangeCallback( new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
            }
        });

        stateQuizData = new StateQuizData(this);
        stateQuizData.open();
        new QuizDBCreator().execute();

//        if (quizViewModel.getQuizId() == -1) {
//            stateQuizData = new StateQuizData(this);
//            stateQuizData.open();
//            new QuizDBCreator().execute();
//        } else {
//            quizId = quizViewModel.getQuizId();
//        }



        //currentPage = 0;




        //Pass info from database here (will need to update StateQuizFragment and QuizResultsFragment to accept more info in newInstance method)
        questionList = new ArrayList<StateQuizData.QuizQuestion>();
        //stateQuizData = new StateQuizData(this); //?

//        stateQuizData.open();
//
//        new QuizDBCreator().execute();





        pager.setCurrentItem(currentPage, false);

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


    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("currentPage", ((ViewPager2) findViewById(R.id.viewpager)).getCurrentItem());
    }

    public class QuizDBCreator extends AsyncTask<Void, List<StateQuizData.QuizQuestion>> {

        long quizID;
        @Override
        protected List<StateQuizData.QuizQuestion> doInBackground(Void... params) {


            quizID = quizViewModel.getQuizId();
            if (quizID == -1) {
                quizID = stateQuizData.createRandomQuiz();
            }

            //quizID = quizId;
            //quizViewModel.setQuizId(quizID);
            List<StateQuizData.QuizQuestion> quizQuestions = stateQuizData.getQuizQuestions(quizID);

            return quizQuestions;
        }

        @Override
        protected void onPostExecute(List<StateQuizData.QuizQuestion> quizQuestions) {

            quizViewModel.setQuizId(quizID);
            questionList = quizQuestions;

            for (int i = 0; i < 6; i++) {
                StateQuizFragment sqf = StateQuizFragment.newInstance(i, questionList.get(i), quizID);
                sqpAdapter.addFragment(sqf);
            }

            QuizResultsFragment qrf = QuizResultsFragment.newInstance(quizID);
            sqpAdapter.addFragment(qrf);

            int restoredPage = Math.min(currentPage, sqpAdapter.getItemCount() - 1);
            pager.setCurrentItem(restoredPage, false);

        }

    }

}