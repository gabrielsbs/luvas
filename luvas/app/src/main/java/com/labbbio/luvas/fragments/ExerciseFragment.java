package com.labbbio.luvas.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.exercisedb.ExerciseItem;

public class ExerciseFragment extends Fragment {

    private String TAG = "ExerciseFragment";

    private SQLiteDatabase database;
    private TextView question;
    private EditText answer;
    private Button send;

    private String tableName;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise,container, false);

        database = ((MainActivity)this.getActivity()).getDatabase();

        question = view.findViewById(R.id.textview_question);
        answer = view.findViewById(R.id.answer);
        send = view.findViewById(R.id.button_send);

        getQuestionText();

        tableName= getArguments().getString("tableName");

        return view;
    }

    private void getQuestionText() {
        int x = 0;
        String[] column = new String[]{ExerciseItem.ExerciseEntry.COLUMN_NUMBER, ExerciseItem.ExerciseEntry.COLUMN_QUESTION, ExerciseItem.ExerciseEntry.COLUMN_ANSWER};
        Cursor cursor = database.query(tableName,column,null,null,null,null,null);
        int numberIndex = cursor.getColumnIndex(column[0]);
        int questionIndex =cursor.getColumnIndex(column[1]);
        int answerIndex = cursor.getColumnIndex(column[2]);
        if(cursor.moveToNext()){
           // question = cursor.getString(questionIndex);
        }
    }
}
