package edu.uga.cs.statecapitalsquiz;

import androidx.lifecycle.ViewModel;

public class QuizViewModel extends ViewModel {
    private boolean[] questionCorrect = new boolean[6];
    private boolean[] questionsAnswered = new boolean[6];

    private int answeredQuestions = 0;
    private int correctAnswers = 0;

    private long quizId = -1;

    public void setQuizId(long quizId) {
        this.quizId = quizId;
    }

    public long getQuizId() {
        return quizId;
    }
    public boolean isQuestionAnswered(int questionNum) {
        return questionsAnswered[questionNum];
    }

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
