package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DeMorganTest {

    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        Formel formel = new Formel("n(a*nb)");
        Formel expectedFormel = new Formel("na+b");
        assertArrayEquals(expectedFormel.toCharArray(), p.deMorgan(formel).toCharArray());
    }

    @Test
    public void testZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("a+n(b*nc)+d");
        Formel expectedFormel = new Formel("a+nb+c+d");
        assertArrayEquals(expectedFormel.toCharArray(), p.deMorgan(formel).toCharArray());
    }

    @Test
    public void testDrei(){
        Parser p = new Parser();
        Formel formel = new Formel("a+n(b*nc)*d");
        Formel expectedFormel = new Formel("a+(nb+c)*d");
        assertArrayEquals(expectedFormel.toCharArray(), p.deMorgan(formel).toCharArray());
    }

    @Test
    public void testVier(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)*n(b*nc*n(d+ne))*na");
        Formel expectedFormel = new Formel("(a+b)*(nb+c+(nd*e))*na");
        char[] result = p.deMorgan(formel).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), result);
    }
}
