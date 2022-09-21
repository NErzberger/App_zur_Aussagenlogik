package com.dhbw.app_zur_aussagenlogik;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.dhbw.app_zur_aussagenlogik.core.Ausaddieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.core.ParserException;

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
        Formel expectedFormel = new Formel("(a+c)*(b+c)*(a+d)*(b+d)");
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
        Formel expectedFormel = new Formel("(d+a+a)*(d+b+a)*(d+a+c)*(d+b+c)");
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
        Formel expectedFormel = new Formel("(a+b+c)*(a+e+c)*(a+b+d)*(a+e+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void einfacheRekursion2() {
        Formel formel = new Formel("a+(b*e)+(c*d)");
        Formel expectedFormel = new Formel("(a+b+c)*(a+b+d)*(a+e+c)*(a+e+d)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void einfacheRekursion3() {
        Formel formel = new Formel("(b*e)+(c*d)+a");
        Formel expectedFormel = new Formel("(b+c+a)*(e+c+a)*(b+d+a)*(e+d+a)");
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
        Formel formel = new Formel("a+((b*c*e)+((d*a)+(f*a)+(f*e)))");
        Formel expectedFormel = new Formel("(a+b+d+f+f)*(a+c+d+f+f)*(a+e+d+f+f)*(a+b+a+f+f)*" +
                "(a+c+a+f+f)*(a+e+a+f+f)*(a+b+d+f+e)*(a+c+d+f+e)*(a+e+d+f+e)*(a+b+a+f+e)*" +
                "(a+c+a+f+e)*(a+e+a+f+e)*(a+b+d+a+f)*(a+c+d+a+f)*(a+e+d+a+f)*(a+b+a+a+f)*" +
                "(a+c+a+a+f)*(a+e+a+a+f)*(a+b+d+a+e)*(a+c+d+a+e)*(a+e+d+a+e)*(a+b+a+a+e)*" +
                "(a+c+a+a+e)*(a+e+a+a+e)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void asdf(){
        Formel formel = new Formel("(a*(b+c))+((b*a)+c)");
        Formel expectedFormel = new Formel("(a+b+c)*(b+c+b+c)*(a+a+c)*(b+c+a+c)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }

    @Test
    public void asdf3(){
        Formel formel = new Formel("(a*(b+c))+(b*a)+c");
        Formel expectedFormel = new Formel("(a+b+c)*(b+c+b+c)*(a+a+c)*(b+c+a+c)");
        char[] ausaddiert = Ausaddieren.ausaddieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausaddiert);
    }
/*
    n = '\u00AC'
    + = '\u22C1'
    * = '\u2227'
    1 = '\u2192'
    2 = '\u2194'
    }
*/
    @Test
    public void asdf2() throws ParserException {
        Formel formel = new Formel("(a*(b+c))+((b*a)+c)");
        String expectedFormel = "(a\u22C1b\u22C1c)\u2227(b\u22C1c)\u2227(a\u22C1c)";
        String ausaddiert = Parser.getInstance().parseFormula("(a*(b+c))+((b*a)+c)");
        assertEquals(expectedFormel, ausaddiert);
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
