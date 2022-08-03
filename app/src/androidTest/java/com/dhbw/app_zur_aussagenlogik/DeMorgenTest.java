package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DeMorgenTest {

    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        char[] formel = {'n', '(', 'a', '*', 'n', 'b', ')'};
        char[] expectedFormel = {'n', 'a', '+', 'b'};
        assertArrayEquals(expectedFormel, p.deMorgen(formel));
    }

    @Test
    public void testZwei(){
        Parser p = new Parser();
        char[] formel = {'a', '+', 'n', '(', 'b', '*', 'n', 'c', ')', '+', 'd'};
        char[] expectedFormel = {'a', '+', 'n', 'b', '+', 'c', '+', 'd'};
        assertArrayEquals(expectedFormel, p.deMorgen(formel));
    }

    @Test
    public void testDrei(){
        Parser p = new Parser();
        char[] formel = {'a', '+', 'n', '(', 'b', '*', 'n', 'c', ')', '*', 'd'};
        char[] expectedFormel = {'a', '+', '(', 'n', 'b', '+', 'c', ')', '*', 'd'};
        assertArrayEquals(expectedFormel, p.deMorgen(formel));
    }

    @Test
    public void testVier(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '*', 'n', '(', 'b', '*', 'n', 'c', '*', 'n', '(', 'd', '+', 'n', 'e', ')', ')', '*', 'n', 'a'};
        char[] expectedFormel = {'(', 'a', '+', 'b', ')', '*', '(', 'n', 'b', '+', 'c', '+', 'n', 'd', '*', 'e', ')', '*', 'n', 'a'};
        assertArrayEquals(expectedFormel, p.deMorgen(formel));
    }
}
