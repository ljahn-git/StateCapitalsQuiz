package edu.uga.cs.statecapitalsquiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

    StateQuizData stateQuizData;
    TextView questionNumberText;
    TextView joinDateText;
    TextView questionText;

    RadioGroup radioGroup;
    RadioButton question1;
    RadioButton question2;
    RadioButton question3;
    private int questionNum;
    int selectedAnswer;
    String stateName;
    String stateJoinDate;
    String capital;
    String choice1;
    String choice2;
    String choice3;
    private String[] questionArray;

    long quizID;

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
    public static StateQuizFragment newInstance(int questionNum, StateQuizData.QuizQuestion question, long quizId) {
        StateQuizFragment fragment = new StateQuizFragment();
        Bundle args = new Bundle();
        args.putInt("questionNum", questionNum);

        String[] questionArray = new String[6];

        questionArray[0] = question.getState();
        questionArray[1] = question.getCapital();
        questionArray[2] = question.getChoices().get(0);
        questionArray[3] = question.getChoices().get(1);
        questionArray[4] = question.getChoices().get(2);
        questionArray[5] = question.getStatehoodYear() + "";
        args.putStringArray("questionData", questionArray);

        args.putLong("quizID", quizId);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            questionNum = getArguments().getInt("questionNum");
            questionArray = getArguments().getStringArray("questionData");
            quizID = getArguments().getLong("quizID");
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

        stateName = questionArray[0];
        capital = questionArray[1];
        choice1 = questionArray[2];
        choice2 = questionArray[3];
        choice3 = questionArray[4];
        stateJoinDate = questionArray[5];

        questionNumberText = view.findViewById(R.id.questionNumberText);
        joinDateText = view.findViewById(R.id.joinDateText);
        questionText = view.findViewById(R.id.questionText);
        question1 = view.findViewById(R.id.radioButton);
        question2 = view.findViewById(R.id.radioButton2);
        question3 = view.findViewById(R.id.radioButton3);
        radioGroup = view.findViewById(R.id.radioGroup);

        questionNumberText.setText("Question " + (questionNum + 1) + ":");
        joinDateText.setText(stateName + " joined the union in " + stateJoinDate);
        questionText.setText("What is the capital of " + stateName+ "?");
        question1.setText("1. " + choice1);
        question2.setText("2. " + choice2);
        question3.setText("3. " + choice3);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId != -1) {
                    //RadioButton selectedRadioButton = group.findViewById(checkedId);
                    selectedAnswer = checkedId;
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle bundle) {
        super.onSaveInstanceState(bundle);

//        int selectedId = radioGroup.getCheckedRadioButtonId();
//
//        if(selectedId != -1) {
//            RadioButotn selectedRadioButton =
//        }

        StateQuizData.Quiz temp = stateQuizData.getQuiz(quizID);
        if (questionArray[2 + selectedAnswer].equals(capital)) {
            stateQuizData.updateQuiz(quizID, temp.getAnsweredQuestions() + 1, temp.getCorrectAnswers() + 1);
        } else {
            stateQuizData.updateQuiz(quizID, temp.getAnsweredQuestions() + 1, temp.getCorrectAnswers());
        }

    }

}