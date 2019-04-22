package com.labbbio.luvas.fragments;

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

}
