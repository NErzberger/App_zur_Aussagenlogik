package com.dhbw.app_zur_aussagenlogik.dnf;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Ausmultiplizieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class IntegrationstestsDNF {
    @Test
    public void negation1() {
        Parser p = new Parser();
        Formel formel = new Formel("na1(nb+c)");
        Formel expectedFormel = new Formel("(nna)+(nb)+(c)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest1() {
        Parser p = new Parser();
        Formel formel = new Formel("c1(na+nb*ne)");
        Formel expectedFormel = new Formel("(nc)+(na)+(nb*ne)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest2() {
        Parser p = new Parser();
        Formel formel = new Formel("(a+nb*e)1nc");
        Formel expectedFormel = new Formel("(na*b)+(ne)+(nc)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest3() {
        Parser p = new Parser();
        Formel formel = new Formel("(na+b)1c1ne");
        Formel expectedFormel = new Formel("(a*nb)+(c)+(ne)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest4() {
        Parser p = new Parser();
        Formel formel = new Formel("c2e2(na+nb)");
        Formel expectedFormel = new Formel("(c*e*na)+(c*e*nb)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest5() {
        Parser p = new Parser();
        Formel formel = new Formel("c*nh*(na+nb*ne)");
        Formel expectedFormel = new Formel("(c*nh*na)+(c*nh*nb*ne)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest6() {
        Parser p = new Parser();
        Formel formel = new Formel("(a+nb*ne)*c*nh");
        Formel expectedFormel = new Formel("(a*c*nh)+(nb*ne*c*nh)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest7() {
        Parser p = new Parser();
        Formel formel = new Formel("nh*(a+b*e)*nc");
        Formel expectedFormel = new Formel("(nh*a*nc)+(nh*b*e*nc)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest8() {
        Parser p = new Parser();
        Formel formel = new Formel("c*nh*(b*ne+na)");
        Formel expectedFormel = new Formel("(c*nh*b*ne)+(c*nh*na)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest9() {
        Parser p = new Parser();
        Formel formel = new Formel("(nb*e+na)*nc*h");
        Formel expectedFormel = new Formel("(nb*e*nc*h)+(na*nc*h)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest10() {
        Parser p = new Parser();
        Formel formel = new Formel("c*(na+nb*ne)+nf");
        Formel expectedFormel = new Formel("(c*na)+(c*nb*ne)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest11() {
        Parser p = new Parser();
        Formel formel = new Formel("(a+nb*e)*nc+nf");
        Formel expectedFormel = new Formel("(a*nc)+(nb*e*nc)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest12() {
        Parser p = new Parser();
        Formel formel = new Formel("(a+b)*nc*ne+nf");
        Formel expectedFormel = new Formel("(a*nc*ne)+(b*nc*ne)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest13() {
        Parser p = new Parser();
        Formel formel = new Formel("nc*ne*(na+nb)+f");
        Formel expectedFormel = new Formel("(nc*ne*na)+(nc*ne*nb)+(f)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest14() {
        Parser p = new Parser();
        Formel formel = new Formel("f+c*(na+nb*ne)");
        Formel expectedFormel = new Formel("(f)+(c*na)+(c*nb*ne)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest15() {
        Parser p = new Parser();
        Formel formel = new Formel("nf+(a+nb*e)*nc");
        Formel expectedFormel = new Formel("(nf)+(a*nc)+(nb*e*nc)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest16() {
        Parser p = new Parser();
        Formel formel = new Formel("f+(na+nb)*c*ne");
        Formel expectedFormel = new Formel("(f)+(na*c*ne)+(nb*c*ne)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest17() {
        Parser p = new Parser();
        Formel formel = new Formel("f+nc*e*(na+nb)");
        Formel expectedFormel = new Formel("(f)+(nc*e*na)+(nc*e*nb)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest18() {
        Parser p = new Parser();
        Formel formel = new Formel("(na+nb*ne)*c*e+nf");
        Formel expectedFormel = new Formel("(na*c*e)+(nb*ne*c*e)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public  void negationTest19() {
        Parser p = new Parser();
        Formel formel = new Formel("c*(na+nb*ne)*h+f");
        Formel expectedFormel = new Formel("(c*na*h)+(c*nb*ne*h)+(f)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest20() {
        Parser p = new Parser();
        Formel formel = new Formel("nf+nc*(a+b*ne)*nh");
        Formel expectedFormel = new Formel("(nf)+(nc*a*nh)+(nc*b*ne*nh)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest21() {
        Parser p = new Parser();
        Formel formel = new Formel("nf+nc*(a+nb*e)*nh+nd");
        Formel expectedFormel = new Formel("(nf)+(nc*a*nh)+(nc*nb*e*nh)+(nd)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest22() {
        Parser p = new Parser();
        Formel formel = new Formel("nf+d+nc*(na+nb*e)*h");
        Formel expectedFormel = new Formel("(nf)+(d)+(nc*na*h)+(nc*nb*e*h)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public  void negationTest23() {
        Parser p = new Parser();
        Formel formel = new Formel("nc*(a+nb*e)*h+nd+nf");
        Formel expectedFormel = new Formel("(nc*a*h)+(nc*nb*e*h)+(nd)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest24() {
        Parser p = new Parser();
        Formel formel = new Formel("(na+nb*e)*c*nh+nd+nf");
        Formel expectedFormel = new Formel("(na*c*nh)+(nb*e*c*nh)+(nd)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public  void negationTest25() {
        Parser p = new Parser();
        Formel formel = new Formel("c*h*(a+nb*ne)+nd+nf");
        Formel expectedFormel = new Formel("(c*h*a)+(c*h*nb*ne)+(nd)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest26() {
        Parser p = new Parser();
        Formel formel = new Formel("(nx+c*nd)*(na+nb*ne)+f");
        Formel expectedFormel = new Formel("(nx*na)+(nx*nb*ne)+(c*nd*na)+(c*nd*nb*ne)+(f)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest27() {
        Parser p = new Parser();
        Formel formel = new Formel("(c*d+nx)*(na+nb*e)+nf");
        Formel expectedFormel = new Formel("(c*d*na)+(c*d*nb*e)+(nx*na)+(nx*nb*e)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest28() {
        Parser p = new Parser();
        Formel formel = new Formel("(nx+nc*d)*(b*ne+na)+nf");
        Formel expectedFormel = new Formel("(nx*b*ne)+(nx*na)+(nc*d*b*ne)+(nc*d*na)+(nf)");
        Formel pfeileAufgelöst = p.pfeileAufloesen(formel);
        Formel deMorgen = p.deMorgan(pfeileAufgelöst);
        Formel result = Ausmultiplizieren.ausmultiplizieren(deMorgen);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }


}