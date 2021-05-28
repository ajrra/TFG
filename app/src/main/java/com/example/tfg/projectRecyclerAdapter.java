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
import com.example.tfg.Data.ProjectAndAll;

import java.util.ArrayList;
import java.util.List;

public class projectRecyclerAdapter extends RecyclerView.Adapter<projectRecyclerAdapter.ViewHolder> {

    private static final String TAG = "projectRecyclerAdapter";
    private OnItemClickListener listener;
    private List<ProjectAndAll> mProjects = new ArrayList<>();
    private LocalStorageAccess localStorage ;


    public projectRecyclerAdapter(LocalStorageAccess localStorage) {
        this.localStorage = localStorage;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.project_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try{
            ProjectAndAll actual = mProjects.get(position);
            holder.projectName.setText(actual.getProject().name);
            holder.imagePage.setImageBitmap(localStorage.loadImageFromStorage(actual.getProject().name));

        }catch (NullPointerException e){
            Log.e(TAG, "onBindViewHolder: Null Pointer: " + e.getMessage() );
        }
    }

    @Override
    public int getItemCount() {
        return mProjects.size();
    }

    public void setProjects(List<ProjectAndAll> projects){
        mProjects = projects;
        notifyDataSetChanged();

    }

    public ProjectAndAll getProjectAt(int pos){
        return mProjects.get(pos);
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imagePage ;
        TextView projectName ;


        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            imagePage = itemView.findViewById(R.id.project_item_image);
            projectName = itemView.findViewById(R.id.text_project);

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            Log.d(TAG, "onClick: " + getAdapterPosition());
            if (listener !=null && position != RecyclerView.NO_POSITION){
                listener.onItemClick(mProjects.get(position));
            }
        }


    }
    public interface OnItemClickListener{
        void onItemClick(ProjectAndAll projectAndAll);
    }

    public void setOnItemClickListener(OnItemClickListener listener ){
        this.listener= listener;
    }

}
