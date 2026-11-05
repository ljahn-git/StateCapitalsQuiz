package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class StateQuizData {

    private final StateQuizDBHelper stateQuizDbHelper;
    private SQLiteDatabase db;

    public StateQuizData(Context context) {
        stateQuizDbHelper = StateQuizDBHelper.getInstance(context);
    }

    public void open() {
        db = stateQuizDbHelper.getWritableDatabase();
    }

    public void close() {
        stateQuizDbHelper.close();
        db = null;
    }

    public boolean isDBOpen() {
        return db != null && db.isOpen();
    }

    public long createRandomQuiz() {
        ensureDatabaseOpen();
        return stateQuizDbHelper.createRandomQuiz();
    }

    public List<QuizQuestion> getQuizQuestions(long quizId) {
        ensureDatabaseOpen();
        return stateQuizDbHelper.getQuizQuestions(quizId);
    }

    public Quiz getQuiz(long quizId) {
        ensureDatabaseOpen();
        return stateQuizDbHelper.getQuiz(quizId);
    }

    private void ensureDatabaseOpen() {
        if (db == null || !db.isOpen()) {
            throw new IllegalStateException("The state quiz database is not open");
        }
    }

    public static class Quiz {
        private  long quizID;
        private String quizDate;
        private int correctAnswers;
        private int answeredQuestions;

        Quiz(long quizID, String quizDate, int correctAnswers, int answeredQuestions) {
            this.quizID = quizID;
            this.quizDate = quizDate;
            this.correctAnswers = correctAnswers;
            this.answeredQuestions = answeredQuestions;
        }

        public long getQuizID() {
            return quizID;
        }

        public String getQuizDate() {
            return quizDate;
        }

        public int getAnsweredQuestions() {
            return answeredQuestions;
        }

        public int getCorrectAnswers() {
            return correctAnswers;
        }
    }

    public static final class QuizQuestion {
        private final String state;
        private final String capital;
        private final List<String> choices;
        private final int statehoodYear;

        QuizQuestion(String state, String capital, List<String> choices, int statehoodYear) {
            this.state = state;
            this.capital = capital;
            this.choices = choices;
            this.statehoodYear = statehoodYear;
        }

        public String getState() {
            return state;
        }

        public String getCapital() {
            return capital;
        }

        public List<String> getChoices() {
            return new ArrayList<>(choices);
        }

        public int getStatehoodYear() {
            return statehoodYear;
        }
    }
}
