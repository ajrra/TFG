package com.example.tfg;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tfg.Data.Answer;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectAndAll;
import com.example.tfg.Data.ProjectViewModel;
import com.example.tfg.Data.XcelConverter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
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
        setContentView(R.layout.answer_selection);
        mRecyclerView = findViewById(R.id.recycler_view_ans);
        Bundle extras = this.getIntent().getExtras();
        int projectId = extras.getInt("project");
        mProject = mProjectViewModel.getAllProject(projectId);
        mNoteRecyclerAdapter = new AnswerRecyclerAdapter(mProjectViewModel.getLocalStorageAccess());
        FloatingActionButton but = findViewById(R.id.fab);
        but.setOnClickListener(v->buttonShare());
        initRecyclerView();

        retrieveNotes();


    }

    private void buttonShare(){

        File msg = new  File (this.getApplicationContext().getExternalFilesDir("external_files_dir"),mProject.getProject().name+".csv");
        if(!msg.getParentFile().exists())msg.mkdirs();
        try {
            msg.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!msg.exists()){
            Toast.makeText(this,"no file",Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            FileWriter fileWriter = new FileWriter(msg);
            List <Answer> data =  mNoteRecyclerAdapter.getAnswers();
            XcelConverter conv = new XcelConverter();
            for(Answer x : data)
                fileWriter.append(conv.listBooleanToString(x.value));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
       Uri msgUri =  FileProvider.getUriForFile(this, "com.example.tfg.fileprovider", msg);
       /* new Thread(){
            public void run(){


            }


        }.run();*/
        Intent send = new Intent(Intent.ACTION_SEND);
        send.setType("text/csv");
        send.putExtra(Intent.EXTRA_STREAM, msgUri);
        send.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(send,"Share csv with..."));


    }

    private void retrieveNotes() {

        mProjectViewModel.getAnswersFromPage(mProject.getListPagesAndAnswers().get(0).getPage().id)
                .observe(this, (Observer<List<Answer>>) project -> mNoteRecyclerAdapter.setAnswers(project));


    }

    private void initRecyclerView(){

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(linearLayoutManager);

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
                                Answer actual = mNoteRecyclerAdapter.getAnswerAt(  viewHolder.getAdapterPosition());
                                mProjectViewModel.deleteAnswer(actual);

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
