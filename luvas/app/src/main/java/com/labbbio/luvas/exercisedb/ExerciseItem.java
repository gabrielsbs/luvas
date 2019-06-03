package com.labbbio.luvas.exercisedb;

import android.provider.BaseColumns;

public class ExerciseItem {

    private int exerciseNumber;
    private String title;
    private String question;
    private String answer;
    private String questionType;


    public ExerciseItem(int number , String question, String answer, String questionType) {
        this.answer = answer;
        this.exerciseNumber = number;
        this.questionType = questionType;
        this.question = question;

        title = "Exercício "+ Integer.toString(exerciseNumber);
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

    public String getQuestionType() {
        return questionType;
    }


    public static final class LastExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "lastExerciseList";
        public static final String COLUMN_LAST_PRELING = "lastPreLing";
        public static final String COLUMN_LAST_POSLING = "lastPosLing";
    }
}
