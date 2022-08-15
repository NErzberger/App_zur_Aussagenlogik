package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class AusmultiplizierenTest {

// klappt nicht
    @Test
    public void schonDNF() {
        Parser p = new Parser();
        char[] formel = {'(', 'a', '*', 'b', ')', '+', '(', 'a', '*', 'c', ')'};
        char[] expectedFormel = {'(', 'a', '*', 'b', ')', '+', '(', 'a', '*', 'c', ')'};
        char[] ausmultipliziert = p.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel, ausmultipliziert);

    }

    @Test
    public void schonDNF2() {
        Parser p = new Parser();
        char[] formel = {'a', '+', '(', 'b', '*', 'c', ')'};
        char[] expectedFormel = {'a', '+', '(', 'b', '*', 'c', ')'};
        char[] ausmultipliziert = p.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel, ausmultipliziert);

    }


    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        char[] formel = {'a','*', '(', 'b', '+', 'c', ')'};
        char[] expectedFormel = {'(', 'a','*', 'b', ')', '+', '(', 'a', '*', 'c', ')'};
        char[] ausmultipliziert = p.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel, ausmultipliziert);
    }

    @Test
    public void zweiKlammernTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '*', '(', 'b', '+', 'c', ')'};
        char[] expectedFormel = {'(', 'a','*', 'b', ')', '+', '(', 'a', '*', 'c', ')', '+', '(', 'b', '*', 'b', ')', '+', '(', 'b', '*', 'c', ')'};
        char[] ausmultipliziert = p.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel, ausmultipliziert);
        // b* b muss noch rausfallen
    }

    @Test
    public void zweiKlammern4VarTest() {
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '*', '(', 'c', '+', 'd', ')'};
        char[] expectedFormel = {'(', 'a', '*', 'c', ')', '+', '(', 'a', '*', 'd', ')', '+', '(', 'b', '*', 'c', ')', '+', '(', 'b', '*', 'd', ')'};
        char[] ausmultipliziert = p.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel, ausmultipliziert);

    }

    //funktioniert noch nicht
    @Test
    public void zweiEinfacheUnd() {
        Parser p = new Parser();
        char[] formel = {'d', '*', 'a', '*', '(', 'b', '+', 'c', ')'};
        char[] expectedFormel = {'(', 'd', '*', 'a', '*', 'b', ')', '+', '(','(', 'a', '*', 'c', '*', 'd', ')', ')'};
        char[] ausmultipliziert = p.ausmultiplizieren(formel); //{'(', 'd', '*', 'a', ')', '+', '(', 'd', '*', 'b', ')', '+', '(','d',  '*', 'c', ')'};
        assertArrayEquals(expectedFormel, ausmultipliziert);
    }
}
