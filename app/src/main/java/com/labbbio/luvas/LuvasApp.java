package com.labbbio.luvas;

import android.app.Application;


public class LuvasApp extends Application {
    private String backgroundColor = "#1d1d1e";
    private String cardColor = "#f5f021";
    private String textColor = "#f5f021";
    private String highlightCardColor = "#abad68";
    private int fontSize = 40;

    public String getTextColor() {
        return textColor;
    }

    public void setTextColor(String textColor) {
        this.textColor = textColor;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }


    public int getFontSize(){
        return fontSize;
    }


    public String getBackgroundColor() {
        return backgroundColor;
    }


    public String getCardColor() {
        return cardColor;
    }

    public String getHighlightCardColor() {
        return highlightCardColor;
    }

}
