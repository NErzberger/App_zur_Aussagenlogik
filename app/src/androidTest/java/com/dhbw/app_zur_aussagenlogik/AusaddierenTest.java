package com.dhbw.app_zur_aussagenlogik;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.dhbw.app_zur_aussagenlogik.core.Ausaddieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class AusaddierenTest {

    @Before
    public void setup(){
        Parser.getInstance().setModus(Modi.KNF);
    }


    @Test
    public void knfTest(){
        Formel formel = new Formel("(a+b)*(c+d)");
        Formel ausaddiert = Ausaddieren.ausaddieren(formel);
        boolean equals = formel.toString().equals(ausaddiert.toString());
        assertEquals(formel.toString(), ausaddiert.toString());
    }

    @Test
    public void einfacheFormeln() {
        Parser p = new Parser();
        Formel formel = new Formel("a+(b*c)");
        Formel expectedFormel = new Formel("(a+b)*(a+c)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void zweiKlammern(){
        Formel formel = new Formel("(a*b)+(c*d)");
        Formel expectedFormel = new Formel("(a+c)*(a+d)*(b+c)*(b+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void testDrei(){
        Formel formel = new Formel("d+((a+b)*(a+c))");
        Formel expectedFormel = new Formel("(d+a+b)*(d+a+c)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void testDreiAnders(){
        Formel formel = new Formel("d+((a*b)+(a*c))");
        Formel expectedFormel = new Formel("(d+a+a)*(d+a+c)*(d+b+a)*(d+b+c)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void testVier(){
        Formel formel = new Formel("((a+b)*(a+c))+d");
        Formel expectedFormel = new Formel("(a+b+d)*(a+c+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void zweiEinfacheOder() {
        Formel formel = new Formel("d+a+(b*c)");
        Formel expectedFormel = new Formel("(d+a+b)*(d+a+c)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void einfacheRekursion() {
        Formel formel = new Formel("a+((b*e)+(c*d))");
        Formel expectedFormel = new Formel("(a+b+c)*(a+b+d)*(a+e+c)*(a+e+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void einfacheRekursionZwei() {
        Formel formel = new Formel("a+((b*c)*(d+e))");
        Formel expectedFormel = new Formel("(a+b)*(a+c)*(a+d+e)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void mehrfacheRekursion() {
        Formel formel = new Formel("a+((b*c*e)+((d*a)+(d*e)+(f*a)+(f*e)))");
        Formel expectedFormel = new Formel("(a+b+d+d+f+f)*(a+b+d+d+f+e)*(a+b+a+d+f+f)*(a+b+a+d+f+e)*(a+b+d+d+a+f)*(a+b+d+d+a+e)*(a+b+a+d+a+f)*(a+b+a+d+a+e)*(a+b+d+e+f+f)*(a+b+d+e+f+e)*(a+b+d+e+f+f)*(a+b+d+e+f+e)*(a+b+a+e+a+f)*(a+b+a+e+a+e)*(a+b+a+e+a+f)*(a+b+a+e+a+e)*(a+c+d+d+f+f)*(a+c+d+d+f+e)*(a+c+a+d+f+f)*(a+c+a+d+f+e)*(a+c+d+d+a+f)*(a+c+d+d+a+e)*(a+c+a+d+a+f)*(a+c+a+d+a+e)*(a+c+d+e+f+f)*(a+c+d+e+f+e)*(a+c+d+e+f+f)*(a+c+d+e+f+e)*(a+c+a+e+a+f)*(a+c+a+e+a+e)*(a+c+a+e+a+f)*(a+c+a+e+a+e)*(a+e+d+d+f+f)*(a+e+d+d+f+e)*(a+e+a+d+f+f)*(a+e+a+d+f+e)*(a+e+d+d+a+f)*(a+e+d+d+a+e)*(a+e+a+d+a+f)*(a+e+a+d+a+e)*(a+e+d+e+f+f)*(a+e+d+e+f+e)*(a+e+d+e+f+f)*(a+e+d+e+f+e)*(a+e+a+e+a+f)*(a+e+a+e+a+e)*(a+e+a+e+a+f)*(a+e+a+e+a+e)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void asdf(){
        Formel formel = new Formel("(a*(b+c))+((b*a)+c)");
        Formel expectedFormel = new Formel("(a+b+c)*(a+c)*(b+c)*(a+b+c)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    /*@Test
    public void mehrfacheRekursionZwei() {
        Parser p = new Parser();
        char[] formel = {'(', '(', 'a', '*', 'b', '*', 'c', '*', 'd', ')', '+', 'e', '+', 'a', ')', '+', '(', 'a', '*', 'e', ')', '+',
                '(', 'e', '*', 'd', '*', 'c', '*', 'b', ')', '+', '(', 'd', '*', 'b', ')', '+', '(', 'b', '*', 'a', ')', '+', '(', 'd', '*', 'e', ')'};
        char[] expectedFormel = {};
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }*/
}
