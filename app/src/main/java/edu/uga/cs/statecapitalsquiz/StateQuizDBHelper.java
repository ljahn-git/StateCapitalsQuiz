package edu.uga.cs.statecapitalsquiz;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Class ported from JobsTrackerSQLite. Variable names were changed.
 * TODO: Remove unnecessary variables.
 */

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

    public static final String QUESTIONS_COLUMN_URL = "";
    private static final String DEBUG_TAG = "StateQuizDBHelper";

    private static final String DB_NAME = "statequiz.db";
    private static final int DB_VERSION = 1;

    // Define all names (strings) for table and column names.
    // This will be useful if we want to change these names later.
    public static final String TABLE_QUESTIONS = "questions";
    public static final String QUESTIONS_COLUMN_ID = "_id";
    public static final String QUESTIONS_COLUMN_STATE = "state";
    public static final String QUESTIONS_COLUMN_CAPITAL = "capital";
    public static final String QUESTIONS_COLUMN_CITY2 = "city2";
    public static final String QUESTIONS_COLUMN_CITY3 = "city3";
    public static final String QUESTIONS_COLUMN_DATE = "date";

    // This is a reference to the only instance for the helper.
    private static StateQuizDBHelper helperInstance;

    // A Create table SQL statement to create a table for job leads.
    // Note that _id is an auto increment primary key, i.e. the database will
    // automatically generate unique id values as keys.
    private static final String CREATE_QUESTIONS =
            "create table " + TABLE_QUESTIONS + " ("
                    + QUESTIONS_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + QUESTIONS_COLUMN_STATE + " TEXT, "
                    + QUESTIONS_COLUMN_CAPITAL + " TEXT, "
                    + QUESTIONS_COLUMN_CITY2 + " TEXT, "
                    + QUESTIONS_COLUMN_CITY3 + " TEXT, "
                    + QUESTIONS_COLUMN_DATE + " TEXT"
                    + ")";

    // Note that the constructor is private!
    // So, it can be called only from
    // this class, in the getInstance method.
    private StateQuizDBHelper( Context context ) {
        super( context, DB_NAME, null, DB_VERSION );
    }

    // Access method to the single instance of the class.
    // It is synchronized, so that only one thread can executes this method, at a time.
    public synchronized static StateQuizDBHelper getInstance( Context context ) {
        // check if the instance already exists and if not, create the instance
        if( helperInstance == null ) {
            helperInstance = new StateQuizDBHelper( context.getApplicationContext() );
        }
        return helperInstance;
    }

    // We must override onCreate method, which will be used to create the database if
    // it does not exist yet.
    @Override
    public void onCreate( SQLiteDatabase db ) {
        db.execSQL( CREATE_QUESTIONS );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUESTIONS + " created" );
    }

    // We should override onUpgrade method, which will be used to upgrade the database if
    // its version (DB_VERSION) has changed.  This will be done automatically by Android
    // if the version will be bumped up, as we modify the database schema.
    @Override
    public void onUpgrade( SQLiteDatabase db, int oldVersion, int newVersion ) {
        db.execSQL( "drop table if exists " + TABLE_QUESTIONS );
        onCreate( db );
        Log.d( DEBUG_TAG, "Table " + TABLE_QUESTIONS + " upgraded" );
    }
}