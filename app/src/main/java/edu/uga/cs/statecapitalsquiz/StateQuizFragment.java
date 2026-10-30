package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StateQuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StateQuizFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    TextView joinDateText;
    TextView questionText;
    RadioButton question1;
    RadioButton question2;
    RadioButton question3;
    private int questionNum;

    //Create variables for passed String[]s for states, question 1s, question 2s, and question 3s?

    public StateQuizFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment StateQuizFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StateQuizFragment newInstance(int questionNum) {
        StateQuizFragment fragment = new StateQuizFragment();
        Bundle args = new Bundle();
        args.putInt("questionNum", questionNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionNum = getArguments().getInt("questionNum");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_state_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        joinDateText = view.findViewById(R.id.joinDateText);
        questionText = view.findViewById(R.id.questionText);
        question1 = view.findViewById(R.id.radioButton);
        question2 = view.findViewById(R.id.radioButton2);
        question3 = view.findViewById(R.id.radioButton3);

        // Pull data from database here?
    }

    public static int getNumberOfQuestions() {
        return 6;
    }
}