/**
 * Adapter for the list of exercises
 */

package com.labbbio.luvas.adapter;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.labbbio.luvas.R;
import com.labbbio.luvas.model.Exercise;

import java.util.ArrayList;

public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.ExerciseViewHolder> {
    private ArrayList<Exercise> exerciseItemArrayList;
    private OnItemClickListener mListener;
    private int lastExercise;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class ExerciseViewHolder extends RecyclerView.ViewHolder{
        public TextView titleView;

        public ExerciseViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            titleView = itemView.findViewById(R.id.textView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ExerciseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.exercise_item, parent, false);
        ExerciseViewHolder evh = new ExerciseViewHolder(v,mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExerciseViewHolder exerciseViewHolder, int position) {
        Exercise currentItem = exerciseItemArrayList.get(position);
        exerciseViewHolder.titleView.setText(currentItem.getExerciseTitle());
        int exercisePosition = position +1;
        if(exercisePosition<=lastExercise){
            ((CardView)(exerciseViewHolder.titleView.getParent()).getParent()).setCardBackgroundColor(Color.parseColor("#84ff0a"));
        }else if(exercisePosition == lastExercise+1){
            ((CardView)(exerciseViewHolder.titleView.getParent()).getParent()).setCardBackgroundColor(Color.parseColor("#f5f021"));
        }
        else{
            ((CardView)(exerciseViewHolder.titleView.getParent()).getParent()).setCardBackgroundColor(Color.parseColor("#abad68"));
        }
    }

    @Override
    public int getItemCount() {
        return exerciseItemArrayList.size();
    }

    public void setLastExercise(int lastExercise) {
        this.lastExercise = lastExercise;
    }

    public ExerciseAdapter(ArrayList<Exercise> list){
        this.exerciseItemArrayList = list;
   }
}
