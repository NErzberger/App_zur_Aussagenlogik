package com.dhbw.app_zur_aussagenlogik;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;

@RunWith(AndroidJUnit4.class)
public class AusaddierenTest {

    @Test
    public void einfacheFormeln() {
        Parser p = new Parser();
        char[] formel = {'a','+', '(', 'b', '*', 'c', ')'};
        char[] expectedFormel = {'(', 'a' , '+', 'b', ')', '*', '(', 'a', '+', 'c', ')'};
        assertArrayEquals(expectedFormel, p.ausaddieren(formel));

    }

    @Test
    public void zweiEinfacheOder() {
        Parser p = new Parser();
        char[] formel = {'d', '+', 'a','+', '(', 'b', '*', 'c', ')'};
        char[] expectedFormel = {'(', 'a', '+', 'b', '+', 'd' ,')', '*', '(', 'a', '+', 'c', '+', 'd', ')' };
        assertArrayEquals(expectedFormel, p.ausaddieren(formel));
    }

    @Test
    public void einfacheRekursion() {
        Parser p = new Parser();
        char[] formel = {'a', '+', '(', '(', 'b', '*', 'e', ')', '+', '(', 'c', '*', 'd', ')', ')'};
        char[] expectedFormel = {'(', 'b', '+', 'c', '+', 'a', ')', '*', '(', 'b', '+', 'd', '+', 'a', ')', '*',
                '(', 'e', '+', 'c', '+', 'a', ')', '*', '(', 'e', '+', 'd', '+', 'a', ')'};
        assertArrayEquals(expectedFormel, p.ausaddieren(formel));
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
        assertArrayEquals(expectedFormel, p.ausaddieren(formel));
    }

    @Test
    public void mehrfacheRekursionZwei() {
        Parser p = new Parser();
        char[] formel = {'(', '(', 'a', '*', 'b', '*', 'c', '*', 'd', ')', '+', 'e', 'a', ')', '+', '(', 'a', '*', 'e', ')', '+',
                '(', 'e', '*', 'd', '*', 'c', '*', 'b', ')', '+', '(', 'd', '*', 'b', ')', '+', '(', 'b', '*', 'a', ')', '+', '(', 'd', '*', 'e', ')'};
        char[] expectedFormel = {};
        assertArrayEquals(expectedFormel, p.ausaddieren(formel));
    }
}
