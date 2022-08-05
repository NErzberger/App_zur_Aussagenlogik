package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IntegrationTest {
    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '1', 'c'};
        char[] expectedFormel = {'(', 'n', 'a', '+', 'c', ')', '*', '(', 'n', 'b', '+', 'c', ')'};

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void testZwei(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '2', 'c'};
        char[] expectedFormel = {'(', 'n', 'c', '+', 'a', '+', 'b', ')', '*', '(', 'n', 'a', '+', 'c', ')', '*', '(', 'n', 'b', '+', 'c', ')'};

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }



    @Test
    public void zweiKlammernTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '*', 'b', ')', '1', '(', 'c', '*', 'd', ')'};
        char[] expectedFormel = {'n', '(', 'a', '*', 'b', ')', '+', '(', 'c', '*', 'd', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, p.pfeileAufloesen(formel));
    }



 /*  @Test
    public void endgegner(){
        Parser p = new Parser();
        char[] formel = {'(', '(', 'a', '+', 'b', ')', '2', 'c', '2', 'b', ')', '1',
                '(', 'a', '*', '(', 'b', '2', 'c', '+', 'd', '*', '(', 'e', '+', 'a', ')', ')', ')'};
        char[] expectedFormel = {'(', 'n', 'c', '+', 'a', '+', 'b', ')', '*', '(', 'n', 'a', '+', 'c', ')', '*', '(', 'n', 'b', '+', 'c', ')'};

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }*/
}
