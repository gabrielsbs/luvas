/**
 * PosLingFragment: Fragment that contains the list of the pos-linguistic exercises.
 * The exercises must be completed in order. The app blocks if the user tries to do otherwise.
 */

package com.labbbio.luvas.fragments;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.labbbio.luvas.adapter.ExerciseAdapter;

import com.labbbio.luvas.MainActivity;
import com.labbbio.luvas.R;
import com.labbbio.luvas.model.AnswerOption;
import com.labbbio.luvas.model.Exercise;
import com.labbbio.luvas.model.ExerciseOptions;

import java.util.ArrayList;

public class PosLingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private ExerciseAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<Exercise> exerciseItems;

    private int lastExercise = 10;

    String TAG = "POSLINGFRAGMENT";

    private static PosLingFragment instance = null;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
    }

    public static PosLingFragment getInstance() {
        return instance;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pos_ling, container, false);
        exerciseItems = ((MainActivity) this.getActivity()).getPosExerciseItemsItems();

        setRecyclerView(view);
        if(exerciseItems.size() == 0)
            exerciseItems = ((MainActivity) this.getActivity()).getPosExerciseItemsItems();
        return view;
    }


    public void setRecyclerView(View view) {

        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExerciseAdapter(exerciseItems);
        mAdapter.setLastExercise(lastExercise);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ExerciseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (position <= lastExercise)
                    callExerciseFragment(position + 1);
                else
                    lockedQuestion();
            }
        });
    }

    public void lockedQuestion() {
        int question = lastExercise + 1;
        Toast.makeText(this.getContext(), "Ainda não, faça a questão " + question + " primeiro", Toast.LENGTH_SHORT).show();
    }

    public int getLastExercise(){
        return lastExercise;
    }


    //Call the MainActivity function that start the ExerciseFragment
    public void callExerciseFragment(int questionNumber) {
        Log.d(TAG, "LastExercise = " + lastExercise);
        ((MainActivity) this.getActivity()).exerciseFragmentStart("PosLing" ,questionNumber);
    }

}
