package com.example.tfg;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Data.Answer;
import com.example.tfg.Data.LocalStorageAccess;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectAndAll;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnswerRecyclerAdapter extends RecyclerView.Adapter<AnswerRecyclerAdapter.ViewHolder> {

    private static final String TAG = "projectRecyclerAdapter";
    private List<Answer> mAnswers = new ArrayList<>();
    private LocalStorageAccess localStorage ;


    public AnswerRecyclerAdapter(LocalStorageAccess localStorage) {
        this.localStorage = localStorage;
    }

    @NonNull
    @Override
    public AnswerRecyclerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new AnswerRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AnswerRecyclerAdapter.ViewHolder holder, int position) {
        try{
            Answer actual = mAnswers.get(position);
            holder.projectName.setText(String.valueOf(actual.id));

        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage() );
        }
    }

    @Override
    public int getItemCount() {
        return mAnswers.size();
    }

    public void setAnswers(List<Answer> projects){
        mAnswers = projects;
        notifyDataSetChanged();

    }

    public List<Answer> getAnswers(){
        return mAnswers;
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        //ImageView imagePage ;
        TextView projectName ;


        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
          //  imagePage = itemView.findViewById(R.id.project_item_image);
            projectName = itemView.findViewById(R.id.text_project);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick: " + getAdapterPosition());
        }


    }




}
