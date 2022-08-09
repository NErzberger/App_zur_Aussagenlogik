package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;


/*
+ oder
* und
 */


@RunWith(AndroidJUnit4.class)
public class IntegrationTest {
    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '1', 'c'};
        char[] expectedFormel = {'(', 'c', '+', 'n', 'b', ')', '*', '(', 'c', '+', 'n', 'a', ')'};

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    /*
    Test läuft noch nicht, weil beim Ausaddieren der zweite Block mit ausaddiert wird, was falsch ist.
     */
    @Test
    public void testZwei(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '2', 'c'};
        char[] expectedFormel = {'(', 'c', '+', 'n', 'b', ')', '*', '(', 'n', 'c', '+', 'a', '+', 'b', ')', '*', '(', 'c', '+', 'n', 'a', ')'};

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void zweiKlammernTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '1', '(', 'c', '*', 'd', ')'};
        char[] expectedFormel = {'(', 'n', 'b', '+', 'c', ')', '*', '(', 'n', 'b', '+', 'd', ')', '*', '(', 'n', 'a', '+', 'c', ')', '*', '(', 'n', 'a', '+', 'd', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void zweiKlammernTestZwei(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '*', 'b', ')', '1', '(', 'c', '*', 'd', ')'};
        char[] expectedFormel = {'(', 'n', 'b', '+', 'c', '+', 'n', 'a', ')', '*', '(', 'n', 'b', '+', 'd', '+', 'n', 'a', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void beidseitigZweiKlammernTest(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '+', 'b', ')', '2', '(', 'c', '*', 'd', ')'};
        char[] expectedFormel = {'(', 'n', 'b', '+', 'c', ')', '*', '(', 'n', 'b', '+', 'd', ')', '*', '(', 'n', 'a', '+', 'c', ')', '*', '(', 'n', 'a', '+', 'd', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void beidseitigZweiKlammernTestZwei(){
        Parser p = new Parser();
        char[] formel = {'(', 'a', '*', 'b', ')', '2', '(', 'c', '*', 'd', ')'};
        char[] expectedFormel = {'n', '(', 'a', '*', 'b', ')', '+', '(', 'c', '*', 'd', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void rekursivZweiKlammernTest(){
        Parser p = new Parser();
        char[] formel = { 'e', '+', '(', '(', 'a', '+', 'b', ')', '1', '(', 'c', '*', 'd', ')', ')'};
        char[] expectedFormel = {'(', 'n', 'b', '+', 'c', '+', 'e', ')', '*', '(', 'n', 'b', '+', 'd', '+', 'e', ')', '*', '(', 'n', 'a', '+', 'c', '+', 'e', ')', '*', '(', 'n', 'a', '+', 'd', '+', 'e', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void rekursivZweiKlammernTestZwei(){
        Parser p = new Parser();
        char[] formel = {'e', '+', '(', '(', 'a', '*', 'b', ')', '1', '(', 'c', '*', 'd', ')', ')'};
        char[] expectedFormel = {'(', 'n', 'b', '+', 'c', '+', 'n', 'a', '+', 'e', ')', '*', '(', 'n', 'b', '+', 'd', '+', 'n', 'a', '+', 'e', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void rekursivBeidseitigZweiKlammernTest(){
        Parser p = new Parser();
        char[] formel = {'e', '+', '(', '(', 'a', '+', 'b', ')', '2', '(', 'c', '*', 'd', ')', ')'};
        char[] expectedFormel = {'(', 'n', 'b', '+', 'c', '+', 'e', ')', '*', '(', 'n', 'b', '+', 'd', '+', 'e', ')', '*', '(', 'n', 'a', '+', 'c', '+', 'e', ')', '*',
                '(', 'n', 'a', '+', 'd', '+', 'e', ')', '*', '(', 'n', 'c', '+', 'n', 'd', '+', 'a', '+', 'b', '+', 'e', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
    }

    @Test
    public void rekursivBeidseitigZweiKlammernTestZwei(){
        Parser p = new Parser();
        char[] formel = {'e', '+', '(', '(', 'a', '*', 'b', ')', '2', '(', 'c', '*', 'd', ')', ')'};
        char[] expectedFormel = {'n', '(', 'a', '*', 'b', ')', '+', '(', 'c', '*', 'd', ')'};
        char[] aufgelöstePfeile = p.pfeileAufloesen(formel);
        char[] deMorgan = p.deMorgan(aufgelöstePfeile);
        char[] ausaddiert = p.ausaddieren(deMorgan);
        assertArrayEquals(expectedFormel, ausaddiert);
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
