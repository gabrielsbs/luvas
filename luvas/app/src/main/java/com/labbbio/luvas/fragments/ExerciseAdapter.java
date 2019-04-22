package com.labbbio.luvas.fragments;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.labbbio.luvas.R;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<ExerciseItem> exerciseItemArrayList;

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        public TextView titleView;

        public ExerciseViewHolder(@NonNull View itemView) {
            super(itemView);
            titleView = itemView.findViewById(R.id.textView);
        }
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        ExerciseViewHolder evh = new ExerciseViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int position) {
        ExerciseItem currentItem = exerciseItemArrayList.get(position);

        exerciseViewHolder.titleView.setText(currentItem.getExerciseTitle());
    }

    @Override
    public int getItemCount() {
        return exerciseItemArrayList.size();
    }

   public ExerciseAdapter(ArrayList<ExerciseItem> list){
        this.exerciseItemArrayList = list;
   }
}
