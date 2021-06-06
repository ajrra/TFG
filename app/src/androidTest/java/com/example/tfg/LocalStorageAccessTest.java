package com.example.tfg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.tfg.Data.LocalStorageAccess;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.internal.matchers.Null;

import java.io.InputStream;

import static org.junit.Assert.*;
@RunWith(JUnit4.class)
public class LocalStorageAccessTest {


    @Spy
    LocalStorageAccess localdb =  new LocalStorageAccess(InstrumentationRegistry.getInstrumentation().getTargetContext());
    Bitmap bm ;

    @Before
    public void setUp() throws Exception {
        Context ctx = InstrumentationRegistry.getInstrumentation().getContext();
        InputStream is = ctx.getResources().getAssets().open("Test1.jpg");
        bm = BitmapFactory.decodeStream(is);


    }


    @Test
    public void saveToInternalStorage() {

        Assert.assertNull(localdb.loadImageFromStorage("prop"));
        localdb.saveToInternalStorage(bm,"prop");
        Assert.assertNotNull(localdb.loadImageFromStorage("prop"));
        localdb.deleteImageFromStorage("prop");
        Assert.assertNull(localdb.loadImageFromStorage("prop"));
    }

    @Test
    public void saveToInternalStorage2(){
        bm=null;
        localdb.saveToInternalStorage(bm,"prop");
        Assert.assertNull(localdb.loadImageFromStorage("prop"));

    }


}