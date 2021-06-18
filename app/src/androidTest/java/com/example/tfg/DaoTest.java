package com.example.tfg;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.tfg.Data.Answer;
import com.example.tfg.Data.AnswerDao;
import com.example.tfg.Data.Page;
import com.example.tfg.Data.PageDao;
import com.example.tfg.Data.PagesAndAnswers;
import com.example.tfg.Data.Project;
import com.example.tfg.Data.ProjectAndAll;
import com.example.tfg.Data.ProjectDao;
import com.example.tfg.Data.ProjectDao_Impl;
import com.example.tfg.Data.ProjectDatabase;
import com.example.tfg.Data.ProjectRepository;
import com.example.tfg.Data.ProjectViewModel;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

import java.net.PortUnreachableException;
import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class DaoTest {

    private Context context ;
    private ProjectDatabase db;
    private ProjectDao projectDao;
    private AnswerDao answerDao;
    private PageDao pageDao;

    @Rule
    public TestRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp(){
        context = ApplicationProvider.getApplicationContext();
        db = Room.inMemoryDatabaseBuilder(context, ProjectDatabase.class).allowMainThreadQueries().build();

        projectDao = db.projectDao();
        answerDao = db.AnswerDao();
        pageDao = db.pageDao();
    }

    @After
    public void reset(){
        db.close();
    }

    @Test
    public void UT_1_Get_Projects(){
        final int[] size = new int[1];

        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {

            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],0);
    }
    @Test
    public void UT_2_1_test_SaveProject_1(){

        Project prop = new Project ();
        prop.name = "prop";

        projectDao.insertProject(prop);

        final int[] size = new int[1];
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {

            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],1);


    }

    @Test
    public void UT_2_2_test_SaveProject_2(){

        Project prop = new Project ();


        projectDao.insertProject(prop);

        final int[] size = new int[1];
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {

            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],1);


    }

    @Test(expected = NullPointerException.class)
    public void UT_2_3_test_SaveProject_3(){
        Project prop= null;
        projectDao.insertProject(prop);
    }




    @Test
    public void UT_3_1_test_Read_Project_ID_1(){
        Project prop = new Project ();
        prop.name = "prop";

        int  id= (int)projectDao.insertProject(prop);

        Project newProp = projectDao.getProject(id);

        Assert.assertEquals(newProp.name,prop.name);


    }
    @Test
    public void UT_3_2_test_Read_Project_ID_2(){
        Assert.assertNull(  projectDao.getProject(2));
    }
    @Test(expected = NullPointerException.class)
    public void UT_3_3_test_Read_Project_ID_3(){
        Project prop = new Project ();
        prop.name = "prop";
        Integer  id= null;
        projectDao.insertProject(prop);
        projectDao.getProject(id);
    }




    @Test
    public void UT_4_1_test_Insert_Page_1(){
        //PREPARE
        Project prop = new Project ();
        prop.name = "prop";
        prop.id =  (int)projectDao.insertProject(prop);
        final int[] size = new int[1];
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],1);
        Page newPage = new Page();
        newPage.Project_fk=prop.id;
        newPage.img = "sss";
        newPage.quizL = new ArrayList<>();

        pageDao.insertPage(newPage);
        pageDao.getAllPages().observeForever( new  Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> page) {
                size[0] = page.size();
            }
        });

        Assert.assertEquals(size[0],1);

    }
    //no FK_from project to Page
    @Test(expected = SQLiteConstraintException.class)
    public void UT_4_2_test_Insert_Page_2(){
        //PREPARE
        Project prop = new Project ();
        prop.name = "prop";
        prop.id =  (int)projectDao.insertProject(prop);
        final int[] size = new int[1];
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],1);
        Page newPage = new Page();
        newPage.img = "sss";
        newPage.quizL = new ArrayList<>();

        pageDao.insertPage(newPage);
        pageDao.getAllPages().observeForever( new  Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> page) {
                size[0] = page.size();
            }
        });


    }
    //no FK_from project to Page
    @Test(expected = SQLiteConstraintException.class)
    public void UT_4_3_test_Insert_Page_3(){
        //PREPARE

        final int[] size = new int[1];

        Page newPage = new Page();
        newPage.img = "sss";
        newPage.quizL = new ArrayList<>();
        newPage.Project_fk=10;

        pageDao.insertPage(newPage);
        pageDao.getAllPages().observeForever( new  Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> page) {
                size[0] = page.size();
            }
        });

    }



    @Test
    public void UT_5_1_test_Insert_Answer_1()
    {
        //PREPARE
        Project prop = new Project ();
        prop.name = "prop";
        prop.id =  (int)projectDao.insertProject(prop);
        final int[] size = new int[1];
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],1);
        Page newPage = new Page();
        newPage.Project_fk=prop.id;
        newPage.img = "sss";
        newPage.quizL = new ArrayList<>();
        pageDao.insertPage(newPage);
        Answer newAnswer = new Answer();
        pageDao.getAllPages().observeForever( new  Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> page) {
                size[0] = page.size();
                newAnswer.Page_fk = page.get(0).id;
            }
        });
        Assert.assertEquals(size[0],1);
        newAnswer.value=new ArrayList<>();
        answerDao.insertAnswer(newAnswer);
        answerDao.getAllAnswers().observeForever(new Observer<List<Answer>>() {
            @Override
            public void onChanged(List<Answer> answers) {
                size[0]=answers.size();
            }
        });
        Assert.assertEquals(size[0],1);
    }

    @Test(expected = SQLiteConstraintException.class)
    public void UT_5_2_test_Insert_Answer_2()
    {
        //PREPARE
        final int[] size = new int[1];
        Answer newAnswer = new Answer();
        newAnswer.Page_fk = 20;
        newAnswer.value=new ArrayList<>();
        answerDao.insertAnswer(newAnswer);
        answerDao.getAllAnswers().observeForever(new Observer<List<Answer>>() {
            @Override
            public void onChanged(List<Answer> answers) {
                size[0]=answers.size();
            }
        });
       // Assert.assertEquals(size[0],1);
    }



    @Test
    public void UT_6_1_test_Delete_Project_1(){
        Project prop = new Project ();
        prop.name = "prop";
//TEST2
        prop.id =(int) projectDao.insertProject(prop);
        final int[] size = new int[1];
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],1);
        //TEST3
        projectDao.deleteProject(prop);
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        //ASSERT
        Assert.assertEquals(size[0],0);
    }






    @Test
    public void UT_6_2test_Delete_Project_2(){
        Project prop = new Project ();
        prop.name = "prop";
        prop.id =  (int)projectDao.insertProject(prop);
        final int[] size = new int[3];
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> project) {
                size[0] = project.size();
            }
        });
        Assert.assertEquals(size[0],1);
        Page newPage = new Page();
        newPage.Project_fk=prop.id;
        newPage.img = "sss";
        newPage.quizL = new ArrayList<>();
        pageDao.insertPage(newPage);
        Answer newAnswer = new Answer();
        pageDao.getAllPages().observeForever( new  Observer<List<Page>>() {
            @Override
            public void onChanged(List<Page> page) {
                size[1] = page.size();
                if(size[1] == 1)newAnswer.Page_fk = page.get(0).id;
            }
        });
        Assert.assertEquals(size[0],1);
        newAnswer.value=new ArrayList<>();
        answerDao.insertAnswer(newAnswer);
        answerDao.getAllAnswers().observeForever(new Observer<List<Answer>>() {
            @Override
            public void onChanged(List<Answer> answers) {
                size[0]=answers.size();
            }
        });
        //TEST3
        projectDao.deleteProject(prop);
        //OBSERVE
        projectDao.getAllProjects().observeForever( new  Observer<List<Project>>() {
            @Override
            public void onChanged(List<Project> project) {
                size[2] = project.size();
            }
        });
        //ASSERT
        Assert.assertEquals(size[0],0);
        Assert.assertEquals(size[1],0);
        Assert.assertEquals(size[2],0);

    }



    @Test(expected = NullPointerException.class)
    public void UT_6_3_test_Delete_Project_3(){
        Project prop = null;
        projectDao.deleteProject(prop);


    }








}
