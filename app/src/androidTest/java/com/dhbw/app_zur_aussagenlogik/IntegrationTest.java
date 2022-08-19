package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Ausaddieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

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
        Formel formel = new Formel("(a+b)1c");
        Formel expectedFormel = new Formel("(c+nb)*(c+na)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    /*
    Test läuft noch nicht, weil beim Ausaddieren der zweite Block mit ausaddiert wird, was falsch ist.
     */
    @Test
    public void testZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)2c");
        Formel expectedFormel = new Formel("(c+nb)*(nc+a+b)*(c*na)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void zweiKlammernTest(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)1(c*d)");
        Formel expectedFormel = new Formel("(nb+c)*(nb+d)*(na+c)*(na+d)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void zweiKlammernTestZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("(a*b)1(c*d)");
        Formel expectedFormel = new Formel("(nb+c+na)*(nb+d+na)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void beidseitigZweiKlammernTest(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)2(c*d");
        Formel expectedFormel = new Formel("(nb+c)*(nb+d)*(na+c)*(na+d)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void beidseitigZweiKlammernTestZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("(a*b)2(c*d)");
        Formel expectedFormel = new Formel("n(a*b)+(c*d)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void rekursivZweiKlammernTest(){
        Parser p = new Parser();
        Formel formel = new Formel("e+((a+b)1(c*d))");
        Formel expectedFormel = new Formel("(nb+c+e)*(nb+d+e)*(na+c+e)*(na+d+e)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void rekursivZweiKlammernTestZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("e+((a*b)1(c*d))");
        Formel expectedFormel = new Formel("(nb+c+na+e)*(nb+d+na+e)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void rekursivBeidseitigZweiKlammernTest(){
        Parser p = new Parser();
        Formel formel = new Formel("e+((a+b)2(c*d))");
        Formel expectedFormel = new Formel("(nb+c+e)*(nb+d+e)*(na+c+e)*(na+d+e)*(nc+nd+a+b+e)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
    }

    @Test
    public void rekursivBeidseitigZweiKlammernTestZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("e+((a*b)2(c*d))");
        Formel expectedFormel = new Formel("n(a*b)+(c*d)");

        char[] aufgelöstePfeile = p.pfeileAufloesen(formel).toCharArray();
        char[] deMorgan = p.deMorgan(new Formel(aufgelöstePfeile)).toCharArray();
        char[] ausaddiert = Ausaddieren.ausaddieren(new Formel(deMorgan)).toCharArray();
        assertArrayEquals(expectedFormel.toCharArray(), ausaddiert);
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
