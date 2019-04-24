package com.labbbio.luvas.fragments;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.exercisedb.ExerciseItem;


public class ExerciseFragment extends Fragment {

    private String TAG = "ExerciseFragment";

    private SQLiteDatabase database;
    private TextView questionView;
    private EditText answerView;
    private ImageButton send;

    private String tableName;
    private String answer;
    private int questionNumber;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_exercise,container, false);

        database = ((MainActivity)this.getActivity()).getDatabase();

        questionView = view.findViewById(R.id.textview_question);
        answerView = view.findViewById(R.id.answer);
        send = view.findViewById(R.id.button_send);

        tableName= getArguments().getString("tableName");
        questionNumber = getArguments().getInt("questionNumber");

        getQuestionText();
        
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"Button pressed");
                String answerTried =  answerView.getText().toString();
                answerView.getText().clear();
                
                if(answerTried.equals(answer)){
                    goToNextQuestion();
                }else{
                    wrongAnswer();

                }
                   
            }
        });

        return view;
    }

    private void wrongAnswer() {
        Toast.makeText(this.getContext(), "Reposta Errada", Toast.LENGTH_SHORT).show();
    }

    private void goToNextQuestion() {
        PosLingFragment.getInstance().updateLastExercise();
        ((MainActivity) this.getActivity()).exerciseFragmentStart(tableName,questionNumber+1);
    }

    private void getQuestionText() {
        int x = 0;
        String[] column = new String[]{ExerciseItem.ExerciseEntry.COLUMN_NUMBER, ExerciseItem.ExerciseEntry.COLUMN_QUESTION, ExerciseItem.ExerciseEntry.COLUMN_ANSWER};
        String query = "SELECT * FROM " + tableName + " WHERE " + ExerciseItem.ExerciseEntry.COLUMN_NUMBER + " = ?";
        //Cursor cursor = database.query(tableName,column,null,null,null,null,null);
        Cursor cursor = database.rawQuery(query, new String[]{Integer.toString(questionNumber)});
        int numberIndex = cursor.getColumnIndex(column[0]);
        int questionIndex =cursor.getColumnIndex(column[1]);
        int answerIndex = cursor.getColumnIndex(column[2]);
        if(cursor.moveToNext()){
            String text = cursor.getString(questionIndex);
            Log.d(TAG,text);
            questionView.setText(text);
            answer = cursor.getString(answerIndex);
        }
    }
}
