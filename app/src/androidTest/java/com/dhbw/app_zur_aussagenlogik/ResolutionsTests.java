package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.core.Resolution;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ResolutionsTests {

    @Before
    public void setup(){
        Parser.getInstance().setModus(Modi.RESOLUTION);
    }


    @Test
    public void test1(){
        Resolution r = new Resolution();
        Formel formel = new Formel("(a+c+nb)*(e+na+a+a+na+na)");
        Formel expectedFormel = new Formel("{a,nb,c}");
        Formel result = r.klauselschreibweise(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test2(){
        Resolution r = new Resolution();
        Formel formel = new Formel("(a+e+d+nc+na)*(na+a)*(b+e+nc)*(a+nc+nc+a+na)");
        Formel expectedFormel = new Formel("{b,nc,e}");
        Formel result = r.klauselschreibweise(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

}
