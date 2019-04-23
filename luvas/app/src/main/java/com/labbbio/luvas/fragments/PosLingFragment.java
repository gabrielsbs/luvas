package com.labbbio.luvas.fragments;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.exercisedb.ExerciseAdapter;
import com.labbbio.luvas.exercisedb.ExerciseDBHelper;
import com.labbbio.luvas.exercisedb.ExerciseItem;

import java.util.ArrayList;

public class PosLingFragment extends Fragment {

    private SQLiteDatabase database;
    private RecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExerciseItem> exerciseItems = new ArrayList<>();

    private int lastExercise;

    String TAG = "POSLINGFRAGMENT";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pos_ling, container, false);

        ExerciseDBHelper dbHelper = new ExerciseDBHelper(this.getContext());
        database = ((MainActivity)this.getActivity()).getDatabase();

        setRecyclerView(view);
        createExerciseList();
        addExercisestoDB();

        return view;


    }


    public void createExerciseList(){
        for(int i = 1; i<=26; i++){
            String answer = Character.toString((char) (i+64));
            String question = "Digite a letra \""+answer+"\"";
            exerciseItems.add(new ExerciseItem(i,question,answer));
        }
    }

    public void addExercisestoDB(){
        for(ExerciseItem item: exerciseItems){
            int qNumber = item.getExerciseNumber();
            String question = item.getQuestion();
            String answer = item.getAnswer();
            ContentValues cv = new ContentValues();
            cv.put(ExerciseItem.ExerciseEntry.COLUMN_NUMBER,qNumber);
            cv.put(ExerciseItem.ExerciseEntry.COLUMN_QUESTION,question);
            cv.put(ExerciseItem.ExerciseEntry.COLUMN_ANSWER,answer);
            database.insert(ExerciseItem.ExerciseEntry.POSLING_TABLE_NAME,null,cv);
        }
    }


    public void setRecyclerView(View view){

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExerciseAdapter(exerciseItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
               // changeItem(position, "Clicked");
                callExerciseFragment(position+1);
                updateLastExercise();
                getLastExercise();

            }
        });
    }

    public int getLastExercise(){
        int x = 0;
        String[] column = new String[]{ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING};
        Cursor cursor = database.query(ExerciseItem.LastExerciseEntry.TABLE_NAME,column,null,null,null,null,null);
        int ex = cursor.getColumnIndex(ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING);
        if(cursor.moveToNext()){
            x = cursor.getInt(ex);
            Log.d(TAG,"LastExercise = "+x);

        }
        return x;
    }

    public void updateLastExercise(){
        ContentValues cv = new ContentValues();
        lastExercise = lastExercise+1;
        String command = "UPDATE " +
                ExerciseItem.LastExerciseEntry.TABLE_NAME +
                " SET " + ExerciseItem.LastExerciseEntry.COLUMN_LAST_POSLING +
                " = "+ lastExercise;

        database.execSQL(command);
    }

    public void callExerciseFragment(int questionNumber){
        ((MainActivity) this.getActivity()).exerciseFragmentStart(ExerciseItem.ExerciseEntry.POSLING_TABLE_NAME, questionNumber);
    }
}
