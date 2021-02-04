package com.example.tfg;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Data.LocalStorageAccess;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ProjectSelectionActivity  extends AppCompatActivity  implements
    projectRecyclerAdapter.OnNoteListener,
        View.OnClickListener

{

    // ui components
    private RecyclerView mRecyclerView;

    // vars
    private ArrayList<Project> mNotes = new ArrayList<>();
    private projectRecyclerAdapter mNoteRecyclerAdapter;
    private ProjectViewModel mProjectViewModel;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_select_activity);

        initRecyclerView();




    }

    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(mRecyclerView);
        mNoteRecyclerAdapter = new projectRecyclerAdapter(mNotes, this, new LocalStorageAccess(this.getApplicationContext()));
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
    }

   /* private void retrieveNotes() {

       mProjectViewModel.getAllData().observe();
        mNoteRepository.retrieveNotesTask().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Project> notes) {
                if(mNotes.size() > 0){
                    mNotes.clear();
                }
                if(notes != null){
                    mNotes.addAll(notes);
                }
                mNoteRecyclerAdapter.notifyDataSetChanged();
            }
        });
    }




*/


    @Override
    public void onNoteClick(int position) {

    }

    @Override
    public void onClick(View v) {

    }

    ItemTouchHelper.SimpleCallback itemTouchHelperCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

        }
    };



}
