package com.example.tfg;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Data.LocalStorageAccess;
import com.example.tfg.Data.Project;

import java.util.ArrayList;

public class projectRecyclerAdapter extends RecyclerView.Adapter<projectRecyclerAdapter.ViewHolder> {

    private static final String TAG = "projectRecyclerAdapter";

    private ArrayList<Project> mProjects = new ArrayList<>();
    private OnNoteListener mOnNoteListener;
    private LocalStorageAccess localStorage ;


    public projectRecyclerAdapter(ArrayList<Project> mProjects, OnNoteListener mOnNoteListener,LocalStorageAccess localStorage) {
        this.mProjects = mProjects;
        this.mOnNoteListener = mOnNoteListener;
        this.localStorage = localStorage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ViewHolder(view, mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{

            holder.projectName.setText(mProjects.get(position).name);
           // Bitmap image = localStorage.loadImageFromStorage()
        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage() );
        }
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imagePage ;
        TextView projectName ;
        RelativeLayout parent;
        OnNoteListener mOnNoteListener;


        public ViewHolder(@NonNull View itemView ,OnNoteListener onNoteListener) {
            super(itemView);
            imagePage = itemView.findViewById(R.id.project_item_image);
            projectName = itemView.findViewById(R.id.text_project);
            parent = itemView.findViewById(R.id.parent_layout);
            mOnNoteListener = onNoteListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + getAdapterPosition());
            mOnNoteListener.onNoteClick(getAdapterPosition());
        }


    }
    public interface OnNoteListener{
        void onNoteClick(int position);
    }
}
