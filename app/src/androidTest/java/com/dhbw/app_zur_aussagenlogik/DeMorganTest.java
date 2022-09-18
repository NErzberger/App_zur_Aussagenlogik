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
    public void aPlusB(){
        Parser p = new Parser();
        Formel formel = new Formel("na+b");
        Formel expectedFormel = new Formel("na+b");
        Formel result = p.deMorgan(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void aPlusBMitKlammer(){
        Parser p = new Parser();
        Formel formel = new Formel("n(a+b)");
        Formel expectedFormel = new Formel("na*nb");
        Formel result = p.deMorgan(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        Formel formel = new Formel("n(a*nb)");
        Formel expectedFormel = new Formel("na+b");
        Formel result = p.deMorgan(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void testZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("a+n(b*nc)+d");
        Formel expectedFormel = new Formel("a+nb+c+d");
        Formel result = p.deMorgan(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void testDrei(){
        Parser p = new Parser();
        Formel formel = new Formel("a+n(b*nc)*d");
        Formel expectedFormel = new Formel("a+(nb+c)*d");
        Formel result = p.deMorgan(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void testVier(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)*n(b*nc*n(d+ne))*na");
        Formel expectedFormel = new Formel("(a+b)*(nb+c+d+ne)*na");
        Formel result = p.deMorgan(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void testFünf(){
        Parser p = new Parser();
        Formel formel = new Formel("n(a*b)+(c*d)");
        Formel expectedFormel = new Formel("na+nb+(c*d)");
        Formel result = p.deMorgan(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
}
