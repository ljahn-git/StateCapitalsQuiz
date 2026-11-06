package edu.uga.cs.statecapitalsquiz;

import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {
    private boolean[] questionCorrect = new boolean[6];

    public void answerQuestion(int questionNum, boolean correct) {
        questionCorrect[questionNum] = correct;
    }

    public boolean isQuestionCorrect(int questionNum) {
        return questionCorrect[questionNum];
    }

    public int getCorrectAnswers() {
        int count = 0;
        for (boolean correct : questionCorrect) {
            if (correct) {
                count++;
            }
        }
        return count;
    }
}
