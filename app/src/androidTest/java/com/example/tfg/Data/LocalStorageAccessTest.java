package com.example.tfg.Data;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import static org.junit.Assert.*;

public class LocalStorageAccessTest {


    @Spy
    LocalStorageAccess localdb =  new LocalStorageAccess(ApplicationProvider.getApplicationContext());


    @Before
    public void setUp() throws Exception {

    }


    @Test
    public void saveToInternalStorage() {



    }

    @Test
    public void deleteImageFromStorage() {
    }

    @Test
    public void loadImageFromStorage() {
    }
}