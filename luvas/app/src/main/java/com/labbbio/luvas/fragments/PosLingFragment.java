package com.labbbio.luvas.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labbbio.luvas.R;

import java.util.ArrayList;

public class PosLingFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ArrayList<ExerciseItem> exerciseItems = new ArrayList<>();
        View view = inflater.inflate(R.layout.fragment_pos_ling, container, false);

        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));
        exerciseItems.add(new ExerciseItem(1,"Digite a letra A","A"));



        mRecyclerView = view.findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this.getContext());
        mAdapter = new ExerciseAdapter(exerciseItems);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;


    }
}
