package com.dhbw.app_zur_aussagenlogik.dnf;



import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.Modi;
import com.dhbw.app_zur_aussagenlogik.core.Ausmultiplizieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;


import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;



@RunWith(AndroidJUnit4.class)
public class DNFTestsWithNegation {



    @Before
    public void setup(){
        Parser.getInstance().setModus(Modi.DNF);
    }

    @Test
    public void negation1() {
        Formel formel = new Formel("na*(nb+c)");
        Formel expectedFormel = new Formel("(na*nb)+(na*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest1() {
        Formel formel = new Formel("c*(na+nb*ne)");
        Formel expectedFormel = new Formel("(c*na)+(c*nb*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest2() {
        Formel formel = new Formel("(a+nb*e)*nc");
        Formel expectedFormel = new Formel("(a*nc)+(nb*e*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest3() {
        Formel formel = new Formel("(na+b)*c*ne");
        Formel expectedFormel = new Formel("(na*c*ne)+(b*c*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest4() {
        Formel formel = new Formel("c*e*(na+nb)");
        Formel expectedFormel = new Formel("(c*e*na)+(c*e*nb)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest5() {
        Formel formel = new Formel("c*nh*(na+nb*ne)");
        Formel expectedFormel = new Formel("(c*nh*na)+(c*nh*nb*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest6() {
        Formel formel = new Formel("(a+nb*ne)*c*nh");
        Formel expectedFormel = new Formel("(a*c*nh)+(nb*ne*c*nh)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest7() {
        Formel formel = new Formel("nh*(a+b*e)*nc");
        Formel expectedFormel = new Formel("(nh*a*nc)+(nh*b*e*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest8() {
        Formel formel = new Formel("c*nh*(b*ne+na)");
        Formel expectedFormel = new Formel("(c*nh*b*ne)+(c*nh*na)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest9() {
        Formel formel = new Formel("(nb*e+na)*nc*d");
        Formel expectedFormel = new Formel("(nb*e*nc*d)+(na*nc*d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest10() {
        Formel formel = new Formel("c*(na+nb*ne)+nf");
        Formel expectedFormel = new Formel("(c*na)+(c*nb*ne)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest11() {
        Formel formel = new Formel("(a+nb*e)*nc+nf");
        Formel expectedFormel = new Formel("(a*nc)+(nb*e*nc)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest12() {
        Formel formel = new Formel("(a+b)*nc*ne+nf");
        Formel expectedFormel = new Formel("(a*nc*ne)+(b*nc*ne)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest13() {
        Formel formel = new Formel("nc*ne*(na+nb)+f");
        Formel expectedFormel = new Formel("(nc*ne*na)+(nc*ne*nb)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest14() {
        Formel formel = new Formel("f+c*(na+nb*ne)");
        Formel expectedFormel = new Formel("(f)+(c*na)+(c*nb*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest15() {
        Formel formel = new Formel("nf+(a+nb*e)*nc");
        Formel expectedFormel = new Formel("(nf)+(a*nc)+(nb*e*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest16() {
        Formel formel = new Formel("f+(na+nb)*c*ne");
        Formel expectedFormel = new Formel("(f)+(na*c*ne)+(nb*c*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest17() {
        Formel formel = new Formel("f+nc*e*(na+nb)");
        Formel expectedFormel = new Formel("(f)+(nc*e*na)+(nc*e*nb)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest18() {
        Formel formel = new Formel("(na+nb*ne)*c*e+nf");
        Formel expectedFormel = new Formel("(na*c*e)+(nb*ne*c*e)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public  void negationTest19() {
        Formel formel = new Formel("c*(na+nb*ne)*h+f");
        Formel expectedFormel = new Formel("(c*na*h)+(c*nb*ne*h)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest20() {
        Formel formel = new Formel("nf+nc*(a+b*ne)*nh");
        Formel expectedFormel = new Formel("(nf)+(nc*a*nh)+(nc*b*ne*nh)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest21() {
        Formel formel = new Formel("nf+nc*(a+nb*e)*nh+nd");
        Formel expectedFormel = new Formel("(nf)+(nc*a*nh)+(nc*nb*e*nh)+(nd)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest22() {
        Formel formel = new Formel("nf+d+nc*(na+nb*e)*h");
        Formel expectedFormel = new Formel("(nf)+(d)+(nc*na*h)+(nc*nb*e*h)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public  void negationTest23() {
        Formel formel = new Formel("nc*(a+nb*e)*h+nd+nf");
        Formel expectedFormel = new Formel("(nc*a*h)+(nc*nb*e*h)+(nd)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest24() {
        Formel formel = new Formel("(na+nb*e)*c*nh+nd+nf");
        Formel expectedFormel = new Formel("(na*c*nh)+(nb*e*c*nh)+(nd)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public  void negationTest25() {
        Formel formel = new Formel("c*h*(a+nb*ne)+nd+nf");
        Formel expectedFormel = new Formel("(c*h*a)+(c*h*nb*ne)+(nd)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest26() {
        Formel formel = new Formel("(ng+c*nd)*(na+nb*ne)+f");
        Formel expectedFormel = new Formel("(ng*na)+(c*nd*na)+(ng*nb*ne)+(c*nd*nb*ne)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest27() {
        Formel formel = new Formel("(c*d+ng)*(na+nb*e)+nf");
        Formel expectedFormel = new Formel("(c*d*na)+(ng*na)+(c*d*nb*e)+(ng*nb*e)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest28() {
        Formel formel = new Formel("(ng+nc*d)*(b*ne+na)+nf");
        Formel expectedFormel = new Formel("(ng*b*ne)+(nc*d*b*ne)+(ng*na)+(nc*d*na)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest29() {
        Formel formel = new Formel("nf+(g+c*nd)*(na+nb*ne)");
        Formel expectedFormel = new Formel("(nf)+(g*na)+(c*nd*na)+(g*nb*ne)+(c*nd*nb*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest40() {
        Formel formel = new Formel("(ng+c*nd)*(a+nb*e)*(f*g+nh)");
        Formel expectedFormel = new Formel("(ng*a*f*g)+(c*nd*a*f*g)+(ng*a*nh)+(c*nd*a*nh)+(ng*nb*e*f*g)+(c*nd*nb*e*f*g)+(ng*nb*e*nh)+(c*nd*nb*e*nh)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest41() {
        Formel formel = new Formel("(g+nc*d)*(na+b+ne)");
        Formel expectedFormel = new Formel("(g*na)+(nc*d*na)+(g*b)+(nc*d*b)+(g*ne)+(nc*d*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest42() {
        Formel formel = new Formel("(ng+c+nd)*(na+b*e)");
        Formel expectedFormel = new Formel("(ng*na)+(c*na)+(nd*na)+(ng*b*e)+(c*b*e)+(nd*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest43() {
        Formel formel = new Formel("(ng+nc+d)*(a+nb+e)");
        Formel expectedFormel = new Formel("(ng*a)+(nc*a)+(d*a)+(ng*nb)+(nc*nb)+(d*nb)+(ng*e)+(nc*e)+(d*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest44() {
        Formel formel = new Formel("(g+nc*d)+(na+b*e)+f*ns");
        Formel expectedFormel = new Formel("(g)+(nc*d)+(na)+(b*e)+(f*ns)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest45() {
        Formel formel = new Formel("ng+(na*(nb+c))");
        Formel expectedFormel = new Formel("(ng)+(na*nb)+(na*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest46() {
        Formel formel = new Formel("g+(na*(b+nc))+nd");
        Formel expectedFormel = new Formel("(g)+(na*b)+(na*nc)+(nd)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest47() {
        Formel formel = new Formel("ng*(a*(nb+c))+nd");
        Formel expectedFormel = new Formel("(ng*a*nb)+(ng*a*c)+(nd)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest48() {
        Formel formel = new Formel("g*(na*(b+nc))*nd");
        Formel expectedFormel = new Formel("(g*na*b*nd)+(g*na*nc*nd)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest49() {
        Formel formel = new Formel("ng*(a*(nb*c))");
        Formel expectedFormel = new Formel("(ng*a*nb*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest50() {
        Formel formel = new Formel("g*(na+(nb*nc))");
        Formel expectedFormel = new Formel("(g*na)+(g*nb*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest51() {
        Formel formel = new Formel("ng*(a+(nb+nc))");
        Formel expectedFormel = new Formel("(ng*a)+(ng*nb)+(ng*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest52() {
        Formel formel = new Formel("((nb+c)*na)+ng");
        Formel expectedFormel = new Formel("(nb*na)+(c*na)+(ng)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest53() {
        Formel formel = new Formel("((b+nc)*na)+g+nd");
        Formel expectedFormel = new Formel("(b*na)+(nc*na)+(g)+(nd)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest54() {
        Formel formel = new Formel("((b+nc)*na)*ng+d");
        Formel expectedFormel = new Formel("(b*na*ng)+(nc*na*ng)+(d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest55() {
        Formel formel = new Formel("ni+((a+nb)*(nc+nd))*(ne+f)+ng");
        Formel expectedFormel = new Formel("(ni)+(a*nc*ne)+(nb*nc*ne)+(a*nd*ne)+(nb*nd*ne)+(a*nc*f)+(nb*nc*f)+(a*nd*f)+(nb*nd*f)+(ng)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest56() {
        Formel formel = new Formel("nh*(a+nb)*(nc+d+ne)");
        Formel expectedFormel = new Formel("(nh*a*nc)+(nh*a*d)+(nh*a*ne)+(nh*nb*nc)+(nh*nb*d)+(nh*nb*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest57() {
        Formel formel = new Formel("ng+(a*ne*(b+nc))");
        Formel expectedFormel = new Formel("(ng)+(a*ne*b)+(a*ne*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest58() {
        Formel formel = new Formel("nr*(na+b+h)*(c+nd+e)*(g+j+nk)");
        Formel expectedFormel = new Formel("(nr*na*c*g)+(nr*na*c*j)+(nr*na*c*nk)+(nr*na*nd*g)+(nr*na*nd*j)+(nr*na*nd*nk)+(nr*na*e*g)+(nr*na*e*j)+(nr*na*e*nk)+(nr*b*c*g)+(nr*b*c*j)+(nr*b*c*nk)+(nr*b*nd*g)+(nr*b*nd*j)+(nr*b*nd*nk)+(nr*b*e*g)+(nr*b*e*j)+(nr*b*e*nk)+(nr*h*c*g)+(nr*h*c*j)+(nr*h*c*nk)+(nr*h*nd*g)+(nr*h*nd*j)+(nr*h*nd*nk)+(nr*h*e*g)+(nr*h*e*j)+(nr*h*e*nk)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

}
