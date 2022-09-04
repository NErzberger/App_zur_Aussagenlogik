package com.dhbw.app_zur_aussagenlogik;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Formel;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class FormelTest {

    @Test
    public void testEins(){
        Formel f = new Formel("(a+b)*c");
        Assert.assertTrue(f.toString().equals("(a+b)*c"));
    }
}
