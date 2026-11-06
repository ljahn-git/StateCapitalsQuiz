package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class ReviewPastQuizzesFragment extends Fragment {

    private RecyclerView recyclerView;
    private StateQuizRecyclerAdapter recyclerAdapter;
    private StateQuizData stateQuizData;
    private List<StateQuizData.Quiz> quizList;

    public ReviewPastQuizzesFragment() {
        // Required empty public constructor
    }

    public static ReviewPastQuizzesFragment newInstance() {
        ReviewPastQuizzesFragment fragment = new ReviewPastQuizzesFragment();
        return fragment;
    }

    /**
     * Inflates the layout
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_review_past_quizzes, container, false);
    }

    /**
     * Sets up the recycler view, home button, calls the asynchronous StateQuizDBReader.
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        Button goToHomeButton = view.findViewById(R.id.goToHomeButton);
        goToHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SplashScreen splashScreen = SplashScreen.newInstance();
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fragmentContainerView, splashScreen);
                fragmentTransaction.commit();
            }
        });

        quizList = new ArrayList<>();
        stateQuizData = new StateQuizData(getActivity());
        stateQuizData.open();
        new StateQuizDBReader().execute();
    }

    /**
     * Asynchronous task that handles retrieving all quizzes from the database.
     */
    private class StateQuizDBReader extends AsyncTask<Void, List<StateQuizData.Quiz>> {
        @Override
        protected List<StateQuizData.Quiz> doInBackground(Void... params) {
            return stateQuizData.getAllQuizzes();
        }

        @Override
        protected void onPostExecute(List<StateQuizData.Quiz> result) {
            quizList = result;
            recyclerAdapter = new StateQuizRecyclerAdapter(getActivity(), quizList);
            recyclerView.setAdapter(recyclerAdapter);
            stateQuizData.close();
        }
    }
}
