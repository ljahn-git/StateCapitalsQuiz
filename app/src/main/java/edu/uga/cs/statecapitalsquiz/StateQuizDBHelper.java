package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * This is a SQLiteOpenHelper class, which Android uses to create, upgrade, delete an SQLite database
 * in an app.
 *
 * This class is a singleton, following the Singleton Design Pattern.
 * Only one instance of this class will exist.  To make sure, the
 * only constructor is private.
 * Access to the only instance is via the getInstance method.
 */
public class StateQuizDBHelper extends SQLiteOpenHelper {

    private static final String DEBUG_TAG = "StateQuizDBHelper";
    private static final int QUESTIONS_PER_QUIZ = 6;

    private static final String DB_NAME = "statequiz.db";
    private static final int DB_VERSION = 2;

    public static final String TABLE_QUESTIONS = "questions";
    public static final String QUESTIONS_COLUMN_ID = "_id";
    public static final String QUESTIONS_COLUMN_STATE = "state";
    public static final String QUESTIONS_COLUMN_CAPITAL = "capital";
    public static final String QUESTIONS_COLUMN_SECOND_CITY = "second_city";
    public static final String QUESTIONS_COLUMN_THIRD_CITY = "third_city";
    public static final String QUESTIONS_COLUMN_STATEHOOD_YEAR = "statehood_year";

    public static final String TABLE_QUIZZES = "quizzes";
    public static final String QUIZZES_COLUMN_ID = "_id";
    public static final String QUIZZES_COLUMN_DATE = "quiz_date";
    public static final String QUIZZES_COLUMN_CORRECT_ANSWERS = "correct_answers";
    public static final String QUIZZES_COLUMN_ANSWERED_COUNT = "answered_count";
    public static final String[] QUIZZES_QUESTION_COLUMNS = {
            "question1_id",
            "question2_id",
            "question3_id",
            "question4_id",
            "question5_id",
            "question6_id"
    };

    private final Context context;

    private static StateQuizDBHelper helperInstance;

    private static final String CREATE_QUESTIONS =
            "CREATE TABLE " + TABLE_QUESTIONS + " ("
                    + QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUESTIONS_COLUMN_STATE + " TEXT NOT NULL UNIQUE, "
                    + QUESTIONS_COLUMN_CAPITAL + " TEXT NOT NULL, "
                    + QUESTIONS_COLUMN_SECOND_CITY + " TEXT NOT NULL, "
                    + QUESTIONS_COLUMN_THIRD_CITY + " TEXT NOT NULL, "
                    + QUESTIONS_COLUMN_STATEHOOD_YEAR + " INTEGER NOT NULL"
                    + ")";

    private static final String CREATE_QUIZZES =
            "CREATE TABLE " + TABLE_QUIZZES + " ("
                    + QUIZZES_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUIZZES_COLUMN_DATE + " TEXT NOT NULL, "
                    + QUIZZES_QUESTION_COLUMNS[0] + " INTEGER NOT NULL REFERENCES "
                    + TABLE_QUESTIONS + "(" + QUESTIONS_COLUMN_ID + "), "
                    + QUIZZES_QUESTION_COLUMNS[1] + " INTEGER NOT NULL REFERENCES "
                    + TABLE_QUESTIONS + "(" + QUESTIONS_COLUMN_ID + "), "
                    + QUIZZES_QUESTION_COLUMNS[2] + " INTEGER NOT NULL REFERENCES "
                    + TABLE_QUESTIONS + "(" + QUESTIONS_COLUMN_ID + "), "
                    + QUIZZES_QUESTION_COLUMNS[3] + " INTEGER NOT NULL REFERENCES "
                    + TABLE_QUESTIONS + "(" + QUESTIONS_COLUMN_ID + "), "
                    + QUIZZES_QUESTION_COLUMNS[4] + " INTEGER NOT NULL REFERENCES "
                    + TABLE_QUESTIONS + "(" + QUESTIONS_COLUMN_ID + "), "
                    + QUIZZES_QUESTION_COLUMNS[5] + " INTEGER NOT NULL REFERENCES "
                    + TABLE_QUESTIONS + "(" + QUESTIONS_COLUMN_ID + "), "
                    + QUIZZES_COLUMN_CORRECT_ANSWERS
                    + " INTEGER NOT NULL DEFAULT 0 CHECK ("
                    + QUIZZES_COLUMN_CORRECT_ANSWERS + " BETWEEN 0 AND 6), "
                    + QUIZZES_COLUMN_ANSWERED_COUNT
                    + " INTEGER NOT NULL DEFAULT 0 CHECK ("
                    + QUIZZES_COLUMN_ANSWERED_COUNT + " BETWEEN 0 AND 6)"
                    + ")";

    private StateQuizDBHelper(Context context) {
        super( context, DB_NAME, null, DB_VERSION );
        this.context = context;
    }

    public synchronized static StateQuizDBHelper getInstance(Context context) {
        if( helperInstance == null ) {
            helperInstance = new StateQuizDBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    public long createRandomQuiz() {
        SQLiteDatabase db = getWritableDatabase();
        List<Long> questionIds = new ArrayList<>(QUESTIONS_PER_QUIZ);
        db.beginTransaction();
        try (Cursor cursor = db.rawQuery(
                "SELECT " + QUESTIONS_COLUMN_ID
                        + " FROM " + TABLE_QUESTIONS
                        + " ORDER BY RANDOM() LIMIT " + QUESTIONS_PER_QUIZ,
                null)) {
            while (cursor.moveToNext()) {
                questionIds.add(cursor.getLong(0));
            }

            if (questionIds.size() != QUESTIONS_PER_QUIZ) {
                throw new IllegalStateException(
                        "A quiz requires at least " + QUESTIONS_PER_QUIZ + " questions");
            }

            ContentValues values = new ContentValues();
            values.put(QUIZZES_COLUMN_DATE,
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US)
                            .format(new Date()));
            for (int i = 0; i < QUESTIONS_PER_QUIZ; i++) {
                values.put(QUIZZES_QUESTION_COLUMNS[i], questionIds.get(i));
            }

            long quizId = db.insertOrThrow(TABLE_QUIZZES, null, values);
            db.setTransactionSuccessful();
            return quizId;
        } finally {
            db.endTransaction();
        }
    }

    public List<StateQuizData.QuizQuestion> getQuizQuestions(long quizId) {
        SQLiteDatabase db = getReadableDatabase();
        List<Long> questionIds = new ArrayList<>(QUESTIONS_PER_QUIZ);
        try (Cursor cursor = db.query(
                TABLE_QUIZZES,
                QUIZZES_QUESTION_COLUMNS,
                QUIZZES_COLUMN_ID + " = ?",
                new String[]{Long.toString(quizId)},
                null,
                null,
                null)) {
            if (!cursor.moveToFirst()) {
                throw new IllegalArgumentException("Quiz not found: " + quizId);
            }
            for (int i = 0; i < QUESTIONS_PER_QUIZ; i++) {
                questionIds.add(cursor.getLong(i));
            }
        }

        List<StateQuizData.QuizQuestion> questions =
                new ArrayList<>(QUESTIONS_PER_QUIZ);
        for (long questionId : questionIds) {
            try (Cursor cursor = db.query(
                    TABLE_QUESTIONS,
                    new String[]{
                            QUESTIONS_COLUMN_STATE,
                            QUESTIONS_COLUMN_CAPITAL,
                            QUESTIONS_COLUMN_SECOND_CITY,
                            QUESTIONS_COLUMN_THIRD_CITY,
                            QUESTIONS_COLUMN_STATEHOOD_YEAR
                    },
                    QUESTIONS_COLUMN_ID + " = ?",
                    new String[]{Long.toString(questionId)},
                    null,
                    null,
                    null)) {
                if (!cursor.moveToFirst()) {
                    throw new IllegalStateException(
                            "Question record not found: " + questionId);
                }

                String state = cursor.getString(0);
                String capital = cursor.getString(1);
                List<String> choices = new ArrayList<>(3);
                choices.add(capital);
                choices.add(cursor.getString(2));
                choices.add(cursor.getString(3));
                Collections.shuffle(choices);
                questions.add(new StateQuizData.QuizQuestion(
                        state, capital, choices, cursor.getInt(4)));
            }
        }
        return questions;
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL( CREATE_QUESTIONS );
        db.execSQL(CREATE_QUIZZES);
        seedStateData(db);
        Log.d(DEBUG_TAG, "Table " + TABLE_QUESTIONS + " created");
        Log.d(DEBUG_TAG, "Table " + TABLE_QUIZZES + " created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE " + TABLE_QUESTIONS + " RENAME TO questions_legacy");
            db.execSQL(CREATE_QUESTIONS);
            db.execSQL("INSERT INTO " + TABLE_QUESTIONS + " ("
                    + QUESTIONS_COLUMN_ID + ", "
                    + QUESTIONS_COLUMN_STATE + ", "
                    + QUESTIONS_COLUMN_CAPITAL + ", "
                    + QUESTIONS_COLUMN_SECOND_CITY + ", "
                    + QUESTIONS_COLUMN_THIRD_CITY + ", "
                    + QUESTIONS_COLUMN_STATEHOOD_YEAR + ") "
                    + "SELECT _id, state, capital, city2, city3, CAST(date AS INTEGER) "
                    + "FROM questions_legacy");
            db.execSQL("DROP TABLE questions_legacy");
            db.execSQL(CREATE_QUIZZES);
            seedStateData(db);
        }
        Log.d(DEBUG_TAG, "Database upgraded from " + oldVersion + " to " + newVersion);
    }

    private void seedStateData(SQLiteDatabase db) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                context.getAssets().open("state_capitals.csv"), StandardCharsets.UTF_8))) {
            String line = reader.readLine();
            int lineNumber = 1;
            while ((line = reader.readLine()) != null) {
                lineNumber++;
                String[] fields = line.split(",", -1);
                if (fields.length < 5) {
                    throw new IllegalStateException(
                            "Invalid state capitals CSV row at line " + lineNumber);
                }

                int statehoodYear;
                try {
                    statehoodYear = Integer.parseInt(fields[4]);
                } catch (NumberFormatException e) {
                    throw new IllegalStateException(
                            "Invalid statehood year at CSV line " + lineNumber, e);
                }

                ContentValues values = new ContentValues();
                values.put(QUESTIONS_COLUMN_STATE, fields[0]);
                values.put(QUESTIONS_COLUMN_CAPITAL, fields[1]);
                values.put(QUESTIONS_COLUMN_SECOND_CITY, fields[2]);
                values.put(QUESTIONS_COLUMN_THIRD_CITY, fields[3]);
                values.put(QUESTIONS_COLUMN_STATEHOOD_YEAR, statehoodYear);
                db.insertWithOnConflict(
                        TABLE_QUESTIONS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Unable to load state capitals data", e);
        }
    }
}