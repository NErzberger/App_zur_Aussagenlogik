package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class PfeileAufloesenTest {

    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '*', 'b', ')', '1', 'c'};
        char[] expectedFormel = {'n', '(', 'a', '*', 'b', ')', '+', 'c'};
        assertArrayEquals(expectedFormel, p.pfeileAufloesen(formel));
    }

    @Test
    public void zweiPfeileTest(){
        Parser p = new Parser();
        char[] formel = {'(','(', 'a', '*', 'b', ')', '1', 'c', ')', '1', 'd'};
        char[] expectedFormel = {'n', '(','n', '(', 'a', '*', 'b', ')', '+', 'c', ')', '+', 'd'};
        assertArrayEquals(expectedFormel, p.pfeileAufloesen(formel));
    }

    @Test
    public void zweiKlammernTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '*', 'b', ')', '1', '(', 'c', '*', 'd', ')'};
        char[] expectedFormel = {'n', '(', 'a', '*', 'b', ')', '+', '(', 'c', '*', 'd', ')'};
        assertArrayEquals(expectedFormel, p.pfeileAufloesen(formel));
    }

    @Test
    public void testVier(){
        Parser p = new Parser();
        char[] formel = {'a', '1', 'b', '1', 'c', '1', 'd', '1', 'e'};
        char[] expectedFormel = {'n', 'a', '+', 'n', 'b', '+', 'n', 'c', '+', 'n', 'd', '+', 'e'};
        assertArrayEquals(expectedFormel, p.pfeileAufloesen(formel));
    }

    @Test
    public void negativTest(){
        Parser p = new Parser();
        char[] formel = {'n', 'a', '1', 'n', 'b', '1', 'n', 'c', '1', 'n', 'd', '1', 'n', 'e'};
        char[] expectedFormel = {'n', 'n', 'a', '+', 'n', 'n', 'b', '+', 'n', 'n', 'c', '+', 'n', 'n', 'd', '+', 'n', 'e'};
        assertArrayEquals(expectedFormel, p.pfeileAufloesen(formel));
    }

    @Test
    public void beidseitig(){
        Parser p = new Parser();
        char[] formel = {'a', '2', 'b'};
        char[] expectedFormel = {'(', 'n', 'a', '+', 'b', ')', '*', '(', 'n', 'b', '+', 'a', ')'};
        assertArrayEquals(expectedFormel, p.pfeileAufloesen(formel));
    }
}
