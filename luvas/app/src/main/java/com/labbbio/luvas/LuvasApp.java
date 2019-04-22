package com.labbbio.luvas;

import android.app.Application;
import android.support.design.widget.TabLayout;


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

    public void setFontSize(int size){
        this.fontSize = size;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getCardColor() {
        return cardColor;
    }

    public String getHighlightCardColor() {
        return highlightCardColor;
    }

    public void setHighlightCardColor(String highlightCardColor) {
        this.highlightCardColor = highlightCardColor;
    }

    public void setCardColor(String cardColor) {
        this.cardColor = cardColor;
    }
}
