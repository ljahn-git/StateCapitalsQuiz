package edu.uga.cs.statecapitalsquiz;

import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {
    private boolean[] questionsAnswered = new boolean[6];

    private int answeredQuestions = 0;
    private int correctAnswers = 0;

    public boolean isQuestionAnswered(int questionNum) {
        return questionsAnswered[questionNum];
    }

    public void answerQuestion(int questionNum, boolean correct) {
        if(!questionsAnswered[questionNum]) {
            questionsAnswered[questionNum] = true;
            answeredQuestions++;
            if (correct) {
                correctAnswers++;
            }
        }
    }

    public int getAnsweredQuestions() {
        return answeredQuestions;
    }

    public int getCorrectAnswers() {
        return correctAnswers;
    }

}
