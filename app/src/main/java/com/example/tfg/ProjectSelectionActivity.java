package com.example.tfg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Data.LocalStorageAccess;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectAndAll;
import com.example.tfg.Data.ProjectViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class ProjectSelectionActivity  extends AppCompatActivity

{

    // ui components
    private RecyclerView mRecyclerView;

    // vars
    private projectRecyclerAdapter mNoteRecyclerAdapter;
    private ProjectViewModel mProjectViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mProjectViewModel = new ViewModelProvider(this).get(ProjectViewModel.class);
        setContentView(R.layout.project_select_activity);
        mRecyclerView = findViewById(R.id.recycler_view);


        initRecyclerView();

        retrieveNotes();


    }

    private void retrieveNotes() {

        mProjectViewModel.getAllData().observe(this, new  Observer<List<ProjectAndAll>>() {

            @Override
            public void onChanged(List<ProjectAndAll> projectAndAlls) {
               mNoteRecyclerAdapter.setProjects(projectAndAlls);
            }
        });


    }



    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mNoteRecyclerAdapter = new projectRecyclerAdapter(mProjectViewModel.getLocalStorageAccess());

        //listener con funcionalisdades onMove y onSwipe
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                Project actual = mNoteRecyclerAdapter.getProjectAt(  viewHolder.getAdapterPosition()).getProject();
                                mProjectViewModel.deleteProject(actual);
                                mProjectViewModel.getLocalStorageAccess().deleteImageFromStorage(actual.name);
                                Toast.makeText(ProjectSelectionActivity.this,"Delete", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(ProjectSelectionActivity.this,"No Delete", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(ProjectSelectionActivity.this);
                builder.setMessage("Delete").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }


        }).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
       //listener onclick para cada item
        mNoteRecyclerAdapter.setOnItemClickListener(new projectRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProjectAndAll projectAndAll) {
                // TODO Auto-generated method stub
                Log.i("ProjectSelection", "called onItemClick");
                Intent i = new Intent(getApplicationContext(),plantillaActivity.class);
                i.putExtra("MODE",1);
                startActivity(i);
            }
        });

    }









}
