package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.Normalizer;

@RunWith(AndroidJUnit4.class)
public class PfeileAufloesenTest {

    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        Formel formel = new Formel("(a*b)1c");
        Formel expectedFormel = new Formel("(n(a*b)+c)");
        assertArrayEquals(expectedFormel.getFormel(), p.pfeileAufloesen(formel).getFormel());
    }

    @Test
    public void einfacherTest2(){
        Parser p = new Parser();
        Formel formel = new Formel("c1(a*b)");
        Formel expectedFormel = new Formel("(nc+(a*b))");
        assertArrayEquals(expectedFormel.getFormel(), p.pfeileAufloesen(formel).getFormel());
    }

    @Test
    public void zweiPfeileTest(){
        Parser p = new Parser();
        Formel formel = new Formel("((a*b)1c)1d");
        Formel expectedFormel = new Formel("(n(n(a*b)+c)+d)");
        assertArrayEquals(expectedFormel.getFormel(), p.pfeileAufloesen(formel).getFormel());
    }

    @Test
    public void zweiPfeileTest2(){
        Parser p = new Parser();
        Formel formel = new Formel("d1(c1(a*b))");
        Formel expectedFormel = new Formel("(nd+((nc+(a*b))))");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }


    @Test
    public void testVier(){
        Parser p = new Parser();
        Formel formel = new Formel("a1b1c1d1e");
        Formel expectedFormel = new Formel("(n(n(n(na+b)+c)+d)+e)");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void testFünf(){
        Parser p = new Parser();
        Formel formel = new Formel("a1b*c");
        Formel expectedFormel = new Formel("(na+b*c)");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void testSechs(){
        Parser p = new Parser();
        Formel formel = new Formel("a*c1b");
        Formel expectedFormel = new Formel("(n(a*c)+b)");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void testSieben(){
        Parser p = new Parser();
        Formel formel = new Formel("a*c1b*c");
        Formel expectedFormel = new Formel("(n(a*c)+b*c)");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void zweiKlammernTest(){
        Parser p = new Parser();
        Formel formel = new Formel("(a*b)1(c*d)");
        Formel expectedFormel = new Formel("(n(a*b)+(c*d))");
        char[] ergebnis = p.pfeileAufloesen(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ergebnis);
    }



    @Test
    public void negativTest(){
        Parser p = new Parser();
        Formel formel = new Formel("na1nb1nc1nd1ne");
        Formel expectedFormel = new Formel("(n(n(n(nna+nb)+nc)+nd)+ne)");
        assertArrayEquals(expectedFormel.getFormel(), p.pfeileAufloesen(formel).getFormel());
    }

    @Test
    public void beidseitig(){
        Parser p = new Parser();
        Formel formel = new Formel("a2b");
        Formel expectedFormel = new Formel("(((na+b))*((nb+a)))");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void beidseitigZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)2c");
        Formel expectedFormel = new Formel("(n(a+b)+c)*(nc+a+b)");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void beidseitigDrei(){
        Parser p = new Parser();
        Formel formel = new Formel("((a+b)2c)2e");
        Formel expectedFormel = new Formel("(n((n(a+b)+c)*(nc+a+b))+e)*(ne+((n(a+b)+c)*(nc+a+b)))");
        char[] aufgelöst = p.pfeileAufloesen(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), aufgelöst);
    }

    @Test
    public void beidseitigVier(){
        Parser p = new Parser();
        Formel formel = new Formel("(a2(b+c)2d)2e");
        Formel expectedFormel = new Formel("(n((na+b+c)*(n(n(b+c)+a)+d)*(nd+n(b+c)+a))+e)*" +
                "(ne+((na+b+c)*(n(n(b+c)+a)+d)*(nd+n(b+c)+a)))");
        char[] aufgelöst = p.pfeileAufloesen(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), aufgelöst);
    }

    @Test
    public void beidseitigFünf(){
        Parser p = new Parser();
        Formel formel = new Formel("a2b*c");
        Formel expectedFormel = new Formel("(((na+b*c))*((nb*c+a)))");
        Formel result = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void beidseitigZweiKlammern(){
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)2(c+d)");
        Formel expectedFormel = new Formel("(n(a+b)+c+d)*(n(c+d)+a+b)");
        char[] ergebnis = p.pfeileAufloesen(formel).getFormel();
        assertArrayEquals(expectedFormel.getFormel(), ergebnis);
    }

    @Test
    public void beidseitigZweiKlammernZwei(){
        Parser p = new Parser();
        Formel formel = new Formel("(a2b)2(c2d)");
        Formel expectedFormel = new Formel("(n((na+b)*(nb+a))+((nc+d)*(nd+c)))*(n((nc+d)*(nd+c))+((na+b)*(nb+a)))");
        Formel ergebnis = p.pfeileAufloesen(formel);
        assertArrayEquals(expectedFormel.getFormel(), ergebnis.getFormel());
    }
}
