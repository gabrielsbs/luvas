package com.labbbio.luvas.exercisedb;

import android.content.ContentValues;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.labbbio.luvas.exercisedb.ExerciseItem.ExerciseEntry;
import com.labbbio.luvas.exercisedb.ExerciseItem.LastExerciseEntry;

public class ExerciseDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "exerciselist.db";
    public static final int DATABASE_VERSION = 1;

    public ExerciseDBHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        final String SQL_CREATE_POSLING_EXERCISELIST_TABLE = "CREATE TABLE " +
                ExerciseEntry.POSLING_TABLE_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ExerciseEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                ExerciseEntry.COLUMN_QUESTION + " TEXT NOT NULL, " +
                ExerciseEntry.COLUMN_ANSWER + " TEXT NOT NULL" +
                ");";
        final String SQL_CREATE_PRELING_EXERCISELIST_TABLE = "CREATE TABLE " +
                ExerciseEntry.PRELING_TABLE_NAME + " (" +
                ExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ExerciseEntry.COLUMN_NUMBER + " INTEGER NOT NULL, " +
                ExerciseEntry.COLUMN_QUESTION + " TEXT NOT NULL, " +
                ExerciseEntry.COLUMN_ANSWER + " TEXT NOT NULL" +
                ");";

        final String SQL_CREATE_LASTEXERCISELIST_TABLE = "CREATE TABLE " +
                LastExerciseEntry.TABLE_NAME + " (" +
                LastExerciseEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                LastExerciseEntry.COLUMN_LAST_PRELING+ " INTEGER NOT NULL, " +
                LastExerciseEntry.COLUMN_LAST_POSLING + " INTEGER NOT NULL " +
                ");";

        db.execSQL(SQL_CREATE_POSLING_EXERCISELIST_TABLE);
        db.execSQL(SQL_CREATE_PRELING_EXERCISELIST_TABLE);
        db.execSQL(SQL_CREATE_LASTEXERCISELIST_TABLE);

        ContentValues cv = new ContentValues();
        cv.put(LastExerciseEntry.COLUMN_LAST_PRELING,0);
        cv.put(LastExerciseEntry.COLUMN_LAST_POSLING,0);
        db.insert( LastExerciseEntry.TABLE_NAME,null,cv);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.POSLING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.PRELING_TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + LastExerciseEntry.TABLE_NAME);
        onCreate(db);
    }
}
