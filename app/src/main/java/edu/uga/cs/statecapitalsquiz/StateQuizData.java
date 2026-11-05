package edu.uga.cs.statecapitalsquiz;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Class ported from JobsTrackerSQLite. Some variable names were changed.
 * TODO: Replace the rest of the variables.
 */

/**
 * This class is facilitates storing and restoring job leads stored.
 */
public class StateQuizData {

    public static final String DEBUG_TAG = "QuestionsData";

    // this is a reference to our database; it is used later to run SQL commands
    private SQLiteDatabase   db;
    private SQLiteOpenHelper stateQuizDbHelper;
    private static final String[] allColumns = {
            StateQuizDBHelper.QUESTIONS_COLUMN_ID,
            StateQuizDBHelper.QUESTIONS_COLUMN_STATE,
            StateQuizDBHelper.QUESTIONS_COLUMN_CAPITAL,
            StateQuizDBHelper.QUESTIONS_COLUMN_CITY2,
            StateQuizDBHelper.QUESTIONS_COLUMN_CITY3,
            StateQuizDBHelper.QUESTIONS_COLUMN_DATE
    };

    public StateQuizData( Context context ) {
        //stateQuizDbHelper = StateQuizDBHelper.getInstance( context );
        stateQuizDbHelper = StateQuizDBHelper.getInstance( context );
    }

    // Open the database
    public void open() {
        db = stateQuizDbHelper.getWritableDatabase();
        Log.d( DEBUG_TAG, "StateQuizData: db open" );
    }

    // Close the database
    public void close() {
        if( stateQuizDbHelper != null ) {
            stateQuizDbHelper.close();
            Log.d(DEBUG_TAG, "StateQuizData: db closed");
        }
    }

    public boolean isDBOpen()
    {
        return db.isOpen();
    }

    // Retrieve all job leads and return them as a List.
    // This is how we restore persistent objects stored as rows in the job leads table in the database.
    // For each retrieved row, we create a new JobLead (Java POJO object) instance and add it to the list.
    public List<Questions> retrieveAllJobLeads() {
        ArrayList<Questions> questions = new ArrayList<>();
        Cursor cursor = null;
        int columnIndex;

        try {
            // Execute the select query and get the Cursor to iterate over the retrieved rows
            cursor = db.query( StateQuizDBHelper.TABLE_QUESTIONS, allColumns,
                    null, null, null, null, null );

            // collect all job leads into a List
            if( cursor != null && cursor.getCount() > 0 ) {

                while( cursor.moveToNext() ) {

                    if( cursor.getColumnCount() >= 5) {

                        // get all attribute values of this job lead
                        columnIndex = cursor.getColumnIndex( StateQuizDBHelper.QUESTIONS_COLUMN_ID );
                        long id = cursor.getLong( columnIndex );
                        columnIndex = cursor.getColumnIndex( StateQuizDBHelper.QUESTIONS_COLUMN_STATE );
                        String state = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( StateQuizDBHelper.QUESTIONS_COLUMN_CAPITAL );
                        String capital = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( StateQuizDBHelper.QUESTIONS_COLUMN_CITY2 );
                        String city2 = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( StateQuizDBHelper.QUESTIONS_COLUMN_CITY3 );
                        String city3 = cursor.getString( columnIndex );
                        columnIndex = cursor.getColumnIndex( StateQuizDBHelper.QUESTIONS_COLUMN_DATE );
                        String date = cursor.getString( columnIndex );

                        // create a new JobLead object and set its state to the retrieved values
                        Question question = new Question( state, capital, city2, city3, date );
                        question.setId(id); // set the id (the primary key) of this object
                        // add it to the list
                        questions.add( question );
                        Log.d(DEBUG_TAG, "Retrieved JobLead: " + question);
                    }
                }
            }
            if( cursor != null )
                Log.d( DEBUG_TAG, "Number of records from DB: " + cursor.getCount() );
            else
                Log.d( DEBUG_TAG, "Number of records from DB: 0" );
        }
        catch( Exception e ){
            Log.d( DEBUG_TAG, "Exception caught: " + e );
        }
        finally{
            // we should close the cursor
            if (cursor != null) {
                cursor.close();
            }
        }
        // return a list of retrieved job leads
        return questions;
    }

    // Store a new job lead in the database.
    public Question storeQuestion( Question jobLead ) {

        // Prepare the values for all of the necessary columns in the table
        // and set their values to the variables of the JobLead argument.
        // This is how we are providing persistence to a JobLead (Java object) instance
        // by storing it as a new row in the database table representing job leads.
        ContentValues values = new ContentValues();
        values.put( StateQuizDBHelper.QUESTIONS_COLUMN_STATE, question.getState());
        values.put( StateQuizDBHelper.QUESTIONS_COLUMN_CAPITAL, question.getCapital() );
        values.put( StateQuizDBHelper.QUESTIONS_COLUMN_CITY2, question.getCity2() );
        values.put( StateQuizDBHelper.QUESTIONS_COLUMN_CITY3, question.getCity3() );
        values.put( StateQuizDBHelper.QUESTIONS_COLUMN_DATE, question.getDate() );

        // Insert the new row into the database table;
        // The id (primary key) is automatically generated by the database system
        // and returned as from the insert method call.
        long id = db.insert( StateQuizDBHelper.TABLE_QUESTIONS, null, values );

        // store the id (the primary key) in the JobLead instance, as it is now persistent
        question.setId( id );

        Log.d( DEBUG_TAG, "Stored new question with id: " + String.valueOf( question.getId() ) );

        return question;
    }


}
