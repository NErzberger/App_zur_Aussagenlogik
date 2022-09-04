package com.dhbw.app_zur_aussagenlogik;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.dhbw.app_zur_aussagenlogik.core.Ausaddieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

import java.util.Arrays;

@RunWith(AndroidJUnit4.class)
public class AusaddierenTest {

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
        Formel expectedFormel = new Formel("(b+c)*(b+d)*(a+c)*(a+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void testDrei(){
        Formel formel = new Formel("d+((a+b)*(a+c))");
        Formel expectedFormel = new Formel("(a+b+d)*(a+c+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void testVier(){
        Formel formel = new Formel("((a+b)*(a+c))+d");
        Formel expectedFormel = new Formel("(c+a+d)*(b+a+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void zweiEinfacheOder() {
        Formel formel = new Formel("d+a+(b*c)");
        Formel expectedFormel = new Formel("(a+b+d)*(a+c+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void einfacheRekursion() {
        Formel formel = new Formel("a+((b*e)+(c*d))");
        Formel expectedFormel = new Formel("(e+c+a)*(e+d+a)*(b+c+a)*(b+d+a)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void einfacheRekursionZwei() {
        Formel formel = new Formel("a+((b*c)*(d+e))");
        Formel expectedFormel = new Formel("(b+a)*(c+a)*(d+e+a)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void mehrfacheRekursion() {
        Formel formel = new Formel("a+((b*c*e)+((d*a)+(d*e)+(f*a)+(f*e)))");
        Formel expectedFormel = new Formel("(a*b*d)+(a*b*d*e)+(a*b*f)+(a*b*f*e)+(a*c*d)+(a*c*d*e)+(a*f)+(a*c*f*e)+(a*e*d)+(a*e*d)+(a*e*f)+(a*e*f)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void asdf(){
        Formel formel = new Formel("(a*(b+c))+((b*a)+c)");
        Formel expectedFormel = new Formel("(a+c)*(b+c)");
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
