package com.dhbw.app_zur_aussagenlogik.dnf;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Ausmultiplizieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class AusmultiplizierenTest {
    @Test
    public void schonDNF() {
        Parser p = new Parser();
        Formel formel = new Formel("(a*b)+(a*c)");
        Formel expectedFormel = new Formel("(a*b)+(a*c)");
        char[] ausmultipliziert = Ausmultiplizieren.ausmultiplizieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausmultipliziert);
    }

    @Test
    public void schonDNF2() {
        Parser p = new Parser();
        Formel formel = new Formel("a+(b*c)");
        Formel expectedFormel = new Formel("(a)+(b*c)");
        char[] ausmultipliziert = Ausmultiplizieren.ausmultiplizieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausmultipliziert);
    }

    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        Formel formel = new Formel("a*(b+c)");
        Formel expectedFormel = new Formel("(a*b)+(a*c)");
        char[] ausmultipliziert = Ausmultiplizieren.ausmultiplizieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausmultipliziert);
    }

    @Test
    public void zweiKlammernTest(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)*(b+c)");
        Formel expectedFormel = new Formel("(a*b)+(a*c)+(b*b)+(b*c)");
        char[] ausmultipliziert = Ausmultiplizieren.ausmultiplizieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausmultipliziert);
    }

    @Test
    public void zweiKlammern4VarTest() {
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)*(c+d)");
        Formel expectedFormel = new Formel("(a*c)+(a*d)+(b*c)+(b*d)");
        char[] ausmultipliziert = Ausmultiplizieren.ausmultiplizieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausmultipliziert);
    }

    @Test
    public void zweiEinfacheUnd() {
        Parser p = new Parser();
        Formel formel = new Formel("d*a*(b+c)");
        Formel expectedFormel = new Formel("(d*a*b)+(d*a*c)");
        char[] ausmultipliziert = Ausmultiplizieren.ausmultiplizieren(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ausmultipliziert);
    }
}
