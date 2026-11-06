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

/**
 * Activity that houses the ViewPager and subsequent quiz and result fragments.
 */
public class QuizActivity extends AppCompatActivity {

    private List<StateQuizData.QuizQuestion> questionList;
    private StateQuizData stateQuizData;

    private QuizViewModel quizViewModel;
    private int currentPage = 0;
    long quizId;

    private StateQuizPagerAdapter sqpAdapter;
    private ViewPager2 pager;

    /**
     * Inflates the layout, sets up the pager, and executes the asynchronous quiz question retrieval
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
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

        //Pass info from database here (will need to update StateQuizFragment and QuizResultsFragment to accept more info in newInstance method)
        //questionList = new ArrayList<StateQuizData.QuizQuestion>();


        pager.setCurrentItem(currentPage, false);

    }

    /**
     * Saves the index of the current page when the page is destroyed
     * @param bundle Bundle in which to place your saved state.
     *
     */
    @Override
    protected void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("currentPage", ((ViewPager2) findViewById(R.id.viewpager)).getCurrentItem());
    }

    /**
     * Asynchronous task that retrieves the quiz questions and subsequently sets up the quiz fragments
     */
    public class QuizDBCreator extends AsyncTask<Void, List<StateQuizData.QuizQuestion>> {

        long quizID;

        /**
         * Creates a new random quiz unless user was in the middle of an old one. After, quiz questions
         * are retrieved from the database.
         * @param params
         * @return quizQuestions
         */
        @Override
        protected List<StateQuizData.QuizQuestion> doInBackground(Void... params) {


            quizID = quizViewModel.getQuizId();
            if (quizID == -1) {
                quizID = stateQuizData.createRandomQuiz();
            }

            List<StateQuizData.QuizQuestion> quizQuestions = stateQuizData.getQuizQuestions(quizID);

            return quizQuestions;
        }

        /**
         * Runs after doInBackground. Creates the quiz fragments with their question instances, followed
         * by the quiz result fragment. Also restores the old current page if last quiz was interrupted.
         * @param quizQuestions
         */
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