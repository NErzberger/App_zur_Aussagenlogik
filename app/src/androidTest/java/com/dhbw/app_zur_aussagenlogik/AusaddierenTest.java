package com.dhbw.app_zur_aussagenlogik;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.dhbw.app_zur_aussagenlogik.core.Parser;

@RunWith(AndroidJUnit4.class)
public class AusaddierenTest {

    @Test
    public void knfTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '*', '(', 'c', '+', 'd', ')'};
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(formel, ausaddiert);
    }

    @Test
    public void einfacheFormeln() {
        Parser p = new Parser();
        char[] formel = {'a','+', '(', 'b', '*', 'c', ')'};
        char[] expectedFormel = {'(', 'a' , '+', 'b', ')', '*', '(', 'a', '+', 'c', ')'};
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void zweiKlammern(){
        Parser p = new Parser();
        char[] formel = {'(', 'a','*', 'b', ')', '+', '(', 'c', '*', 'd', ')'};
        char[] expectedFormel = {'(', 'b', '+', 'c', ')', '*', '(', 'b', '+', 'd', ')', '*', '(', 'a' , '+', 'c', ')', '*', '(', 'a', '+', 'd', ')'};
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void testDrei(){
        Parser p = new Parser();
        char[] formel = {'d', '+','(', '(', 'a', '+', 'b', ')', '*', '(', 'a', '+', 'c', ')', ')'};
        char[] expectedFormel = {'(', 'a', '+', 'b', '+', 'd' ,')', '*', '(', 'a', '+', 'c', '+', 'd', ')' };
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void testVier(){
        Parser p = new Parser();
        char[] formel = {'(', '(', 'a', '+', 'b', ')', '*', '(', 'a', '+', 'c', ')', ')', '+', 'd'};
        char[] expectedFormel = {'(', 'c', '+', 'a', '+', 'd' ,')', '*', '(', 'b', '+', 'a', '+', 'd', ')' };
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void zweiEinfacheOder() {
        Parser p = new Parser();
        char[] formel = {'d', '+', 'a','+', '(', 'b', '*', 'c', ')'};
        char[] expectedFormel = {'(', 'a', '+', 'b', '+', 'd' ,')', '*', '(', 'a', '+', 'c', '+', 'd', ')' };
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void einfacheRekursion() {
        Parser p = new Parser();
        char[] formel = {'a', '+', '(', '(', 'b', '*', 'e', ')', '+', '(', 'c', '*', 'd', ')', ')'};
        char[] expectedFormel = {'(', 'e', '+', 'c', '+', 'a', ')', '*', '(', 'e', '+', 'd', '+', 'a', ')', '*',
                '(', 'b', '+', 'c', '+', 'a', ')', '*', '(', 'b', '+', 'd', '+', 'a', ')'};
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void einfacheRekursionZwei() {
        Parser p = new Parser();
        char[] formel = {'a', '+', '(', '(', 'b', '*', 'c', ')', '*', '(', 'd', '+', 'e', ')', ')'};
        char[] expectedFormel = {'(', 'e', '+', 'c', '+', 'a', ')', '*', '(', 'e', '+', 'd', '+', 'a', ')', '*',
                '(', 'b', '+', 'c', '+', 'a', ')', '*', '(', 'b', '+', 'd', '+', 'a', ')'};
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void mehrfacheRekursion() {
        Parser p = new Parser();
        char[] formel = {'a', '+', '(', '(', 'b', '*', 'c', '*', 'e', ')', '+', '(', '(', 'd', '*', 'a', ')', '+',
                '(', 'd', '*', 'e', ')', '+', '(', 'f', '*', 'a', ')', '+', '(', 'f', '*', 'e', ')', ')', ')', ')'};
        char[] expectedFormel = {'(', 'a', '*', 'b', '*', 'd', ')', '+', '(', 'a', '*', 'b', '*', 'd', '*', 'e', ')',
                '+', '(', 'a', '*', 'b', '*', 'f', ')', '+', '(', 'a', '*', 'b', '*', 'f', '*', 'e', ')', '+',
                '(', 'a', '*', 'c', '*', 'd', ')', '+', '(', 'a', '*', 'c', '*', 'd', '*', 'e', ')', '+',
                '(', 'a', '*', '*', 'f',')', '+', '(', 'a', '*', 'c', '*', 'f', '*', 'e', ')', '+', '(', 'a', '*', 'e', '*', 'd', ')',
                '+', '(', 'a', '*', 'e', '*', 'd', ')', '+', '(', 'a', '*', 'e', '*', 'f', ')', '+', '(', 'a', '*', 'e', '*', 'f', ')'};
        char[] ausaddiert = p.ausaddieren(formel);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void asdf(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '*', '(', 'b', '+', 'c', ')', ')', '+', '(', '(', 'b', '*', 'a', ')', '+', 'c', ')'};
        char[] expected = {'(', 'a', '+', 'c', ')', '*', '(', 'b', '+', 'c', ')'};
        char[] ausaddiert = p.ausaddieren(formel);
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
