package com.labbbio.luvas.exercisedb;

import android.provider.BaseColumns;

public class ExerciseItem {

    private int exerciseNumber;
    private String title;
    private String question;
    private String answer;


    public ExerciseItem(int number , String question, String answer) {
        this.answer = answer;
        this.exerciseNumber = number;

        this.question = question;

        title = "Atividade "+ Integer.toString(exerciseNumber);
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public int getExerciseNumber() {
        return exerciseNumber;
    }

    public String getExerciseTitle() {
        return title;
    }

    public static final class ExerciseEntry implements BaseColumns{
        public static final String PRELING_TABLE_NAME = "preLingExerciseList";
        public static final String POSLING_TABLE_NAME = "posLingExerciseList";
        public static final String COLUMN_NUMBER = "number";
        public static final String COLUMN_QUESTION = "question";
        public static final String COLUMN_ANSWER = "answer";
    }

    public static final class LastExerciseEntry implements BaseColumns{
        public static final String TABLE_NAME = "lastExerciseList";
        public static final String COLUMN_LAST_PRELING = "lastPreLing";
        public static final String COLUMN_LAST_POSLING = "lastPosLing";
    }




}
