package com.labbbio.luvas.model;

import java.io.Serializable;

public class ExerciseOptions implements Serializable {
    private int currentQuestion;
    private AnswerOption answerOption;

    public ExerciseOptions(int currentQuestion, AnswerOption answerOption) {
        this.currentQuestion = currentQuestion;
        this.answerOption = answerOption;
    }

    public int getcurrentQuestion() {
        return currentQuestion;
    }

    public AnswerOption getAnswerOption() {
        return answerOption;
    }

    public void setcurrentQuestion(int currentQuestion) {
        this.currentQuestion = currentQuestion;
    }

    public void setAnswerOption(AnswerOption answerOption) {
        this.answerOption = answerOption;
    }

    public void incrementQuestionNumber(){
        this.currentQuestion++;
    }
}
