package com.example.tfg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import com.example.tfg.Data.Answer;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectAndAll;
import com.example.tfg.Data.ProjectViewModel;

import java.util.List;

public class AnswerReviewSelectionActivity extends AppCompatActivity

{

    // ui components
    private RecyclerView mRecyclerView;

    // vars
    private AnswerRecyclerAdapter mNoteRecyclerAdapter;
    private ProjectViewModel mProjectViewModel;
    private ProjectAndAll mProject;

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

        mProjectViewModel.getAnswersFromPage(mProject.getListPagesAndAnswers().get(0).getPage().id)
                .observe(this, (Observer<List<Answer>>) project -> mNoteRecyclerAdapter.setAnswers(project));


    }



    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        //mNoteRecyclerAdapter = new ProjectRecyclerAdapter(mProjectViewModel.getLocalStorageAccess());

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
                                //Project actual = mNoteRecyclerAdapter.getProjectAt(  viewHolder.getAdapterPosition());
                                //mProjectViewModel.deleteProject(actual);
                                //   mProjectViewModel.getLocalStorageAccess().deleteImageFromStorage(actual.name);
                                Toast.makeText(AnswerReviewSelectionActivity.this,"Delete", Toast.LENGTH_SHORT).show();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                Toast.makeText(AnswerReviewSelectionActivity.this,"No Delete", Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(AnswerReviewSelectionActivity.this);
                builder.setMessage("Delete").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }


        }).attachToRecyclerView(mRecyclerView);
        mRecyclerView.setAdapter(mNoteRecyclerAdapter);
        //listener onclick para cada item

    }









}
