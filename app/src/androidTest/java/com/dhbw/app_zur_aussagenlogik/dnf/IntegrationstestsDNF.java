package com.dhbw.app_zur_aussagenlogik.dnf;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.Modi;
import com.dhbw.app_zur_aussagenlogik.core.Ausmultiplizieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;
import com.dhbw.app_zur_aussagenlogik.core.ParserException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IntegrationstestsDNF {

   /*
    n = '\u00AC'
    + = '\u22C1'
    * = '\u2227'
    1 = '\u2192'
    2 = '\u2194'
    }
*/

    @Before
    public void setup(){
        Parser.getInstance().setModus(Modi.DNF);
    }


    @Test
    public void negation1() throws ParserException {
        Formel formel = new Formel("na1(nb+c)");
        Formel expectedFormel = new Formel("(a)+(nb)+(c)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest1() throws ParserException {
        Formel formel = new Formel("c1(na+nb*ne)");
        Formel expectedFormel = new Formel("(nc)+(na)+(nb*ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest2() throws ParserException {
        Formel formel = new Formel("(a+nb*e)1nc");
        Formel expectedFormel = new Formel("(na*b)+(ne)+(nc)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest3() throws ParserException {
        Formel formel = new Formel("(na+b)1c1ne");
        Formel expectedFormel = new Formel("(na*b)+(nc)+(ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
/*
    @Test
    public void negationTest4() throws ParserException {
        Formel formel = new Formel("c2e2(na+nb)");
        Formel expectedFormel = new Formel("(c*e*na)+(c*e*nb)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
*/
    @Test
    public void negationTest5() throws ParserException {
        Formel formel = new Formel("c*nd*(na+nb*ne)");
        Formel expectedFormel = new Formel("(na*c*nd)+(nb*c*nd*ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest6() throws ParserException {
        Formel formel = new Formel("(a+nb*ne)*c*nd");
        Formel expectedFormel = new Formel("(a*c*nd)+(nb*c*nd*ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public  void negationTest7() throws ParserException {
        Formel formel = new Formel("nd*(a+b*e)*nc");
        Formel expectedFormel = new Formel("(a*nc*nd)+(b*nc*nd*e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest8() throws ParserException {
        Formel formel = new Formel("c*nd*(b*ne+na)");
        Formel expectedFormel = new Formel("(b*c*nd*ne)+(na*c*nd)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest9() throws ParserException {
        Formel formel = new Formel("(nb*e+na)*nc*d");
        Formel expectedFormel = new Formel("(nb*nc*d*e)+(na*nc*d)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest10() throws ParserException {
        Formel formel = new Formel("c*(na+nb*ne)+nd");
        Formel expectedFormel = new Formel("(na*c)+(nb*c*ne)+(nd)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public  void negationTest11() throws ParserException {
        Formel formel = new Formel("(a+nb*e)*nc+nd");
        Formel expectedFormel = new Formel("(a*nc)+(nb*nc*e)+(nd)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest12() throws ParserException {
        Formel formel = new Formel("(a+b)*nc*ne+nd");
        Formel expectedFormel = new Formel("(a*nc*ne)+(b*nc*ne)+(nd)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest13() throws ParserException {
        Formel formel = new Formel("nc*ne*(na+nb)+d");
        Formel expectedFormel = new Formel("(na*nc*ne)+(nb*nc*ne)+(d)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest14() throws ParserException {
        Formel formel = new Formel("d+c*(na+nb*ne)");
        Formel expectedFormel = new Formel("(d)+(na*c)+(nb*c*ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest15() throws ParserException {
        Formel formel = new Formel("nd+(a+nb*e)*nc");
        Formel expectedFormel = new Formel("(nd)+(a*nc)+(nb*nc*e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest16() throws ParserException {
        Formel formel = new Formel("d+(na+nb)*c*ne");
        Formel expectedFormel = new Formel("(d)+(na*c*ne)+(nb*c*ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest17() throws ParserException {
        Formel formel = new Formel("d+nc*e*(na+nb)");
        Formel expectedFormel = new Formel("(d)+(na*nc*e)+(nb*nc*e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public void negationTest18() throws ParserException {
        Formel formel = new Formel("(na+nb*ne)*c*e+nd");
        Formel expectedFormel = new Formel("(na*c*e)+(nd)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public  void negationTest19() throws ParserException {
        Formel formel = new Formel("c*(na+nb*ne)*d+d");
        Formel expectedFormel = new Formel("(na*c*d)+(nb*c*d*ne)+(d)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public void negationTest20() throws ParserException {
        Formel formel = new Formel("nd+nc*(a+b*ne)*nd");
        Formel expectedFormel = new Formel("(nd)+(a*nc*nd)+(b*nc*nd*ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public void negationTest21() throws ParserException {
        Formel formel = new Formel("nd+nc*(a+nb*e)*nd+nd");
        Formel expectedFormel = new Formel("(nd)+(a*nc*nd)+(nb*nc*nd*e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public void negationTest22() throws ParserException {
        Formel formel = new Formel("nd+d+nc*(na+nb*e)*d");
        Formel expectedFormel = new Formel("(nd)+(d)+(na*nc*d)+(nb*nc*d*e)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public  void negationTest23() throws ParserException {
        Formel formel = new Formel("nc*(a+nb*e)*d+nd+ne");
        Formel expectedFormel = new Formel("(a*nc*d)+(nb*nc*d*e)+(nd)+(ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public void negationTest24() throws ParserException {
        Formel formel = new Formel("(na+nb*e)*c*ne+nd+ne");
        Formel expectedFormel = new Formel("(na*c*ne)+(nd)+(ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public  void negationTest25() throws ParserException {
        Formel formel = new Formel("c*d*(a+nb*ne)+nd+ne");
        Formel expectedFormel = new Formel("(a*c*d)+(nb*c*d*ne)+(nd)+(ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public void negationTest26() throws ParserException {
        Formel formel = new Formel("(ne+c*nd)*(na+nb*ne)+a");
        Formel expectedFormel = new Formel("(na*ne)+(na*c*nd)+(nb*ne)+(nb*c*nd*ne)+(a)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }
    @Test
    public void negationTest27() throws ParserException {
        Formel formel = new Formel("(c*d+ne)*(na+nb*e)+ne");
        Formel expectedFormel = new Formel("(na*c*d)+(na*ne)+(nb*c*d*e)+(ne)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }

    @Test
    public void negationTest28() throws ParserException {
        Formel formel = new Formel("(na+nc*d)*(b*ne+na)+nb");
        Formel expectedFormel = new Formel("(na*b*ne)+(b*nc*d*ne)+(na)+(na*nc*d)+(nb)");
        expectedFormel = Parser.getInstance().zeichenersetzungZurueck(expectedFormel);
        formel = Parser.getInstance().zeichenersetzungZurueck(formel);
        String ausmultipliziert = Parser.getInstance().parseFormula(formel.toString());
        assertEquals(expectedFormel.toString(), ausmultipliziert);
    }


}