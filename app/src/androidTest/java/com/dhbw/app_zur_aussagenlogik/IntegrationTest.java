package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Ausaddieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.core.ParserException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;


/*
+ oder
* und
 */


@RunWith(AndroidJUnit4.class)
public class IntegrationTest {

    @Before
    public void setup(){
        Parser.getInstance().setModus(Modi.KNF);
    }

    @Test
    public void einfacherTest() throws ParserException {
        Formel formel = new Formel("(a+b)1c");
        Formel expectedFormel = new Formel("(na+c)*(nb+c)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void testZwei() throws ParserException {
        Formel formel = new Formel("(a+b)2c");
        Formel expectedFormel = new Formel("(na+c)*(nb+c)*(a+b+nc)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void zweiKlammernTest() throws ParserException {
        Formel formel = new Formel("(a+b)1(c*d)");
        Formel expectedFormel = new Formel("(na+c)*(nb+c)*(na+d)*(nb+d)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void zweiKlammernTestZwei() throws ParserException {
        Formel formel = new Formel("(a*b)1(c*d)");
        Formel expectedFormel = new Formel("(na+nb+c)*(na+nb+d)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void beidseitigZweiKlammernTest() throws ParserException {
        Formel formel = new Formel("(a+b)2(c*d)");
        Formel expectedFormel = new Formel("(na+c)*(nb+c)*(na+d)*(nb+d)*(a+b+nc+nd)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void beidseitigZweiKlammernTestZwei() throws ParserException {
        Formel formel = new Formel("(a*b)2(c*d)");
        Formel expectedFormel = new Formel("(na+nb+c)*(na+nb+d)*(a+nc+nd)*(b+nc+nd)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void rekursivZweiKlammernTest() throws ParserException {
        Formel formel = new Formel("e+((a+b)1(c*d))");
        Formel expectedFormel = new Formel("(na+c+e)*(na+d+e)*(nb+c+e)*(nb+d+e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void rekursivZweiKlammernTestZwei() throws ParserException {
        Formel formel = new Formel("e+((a*b)1(c*d))");
        Formel expectedFormel = new Formel("(na+nb+c+e)*(na+nb+d+e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void rekursivBeidseitigZweiKlammernTest() throws ParserException {
        Formel formel = new Formel("e+((a+b)2(c*d))");
        Formel expectedFormel = new Formel("(na+c+e)*(nb+c+e)*(na+d+e)*(nb+d+e)*(a+b+nc+nd+e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

    @Test
    public void rekursivBeidseitigZweiKlammernTestZwei() throws ParserException {
        Formel formel = new Formel("e+((a*b)2(c*d))");
        Formel expectedFormel = new Formel("(na+nb+c+e)*(na+nb+d+e)*(a+nc+nd+e)*(b+nc+nd+e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausaddiert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausaddiert);
    }

}
