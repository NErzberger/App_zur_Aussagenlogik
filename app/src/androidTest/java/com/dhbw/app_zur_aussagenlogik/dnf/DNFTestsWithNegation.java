package com.dhbw.app_zur_aussagenlogik.dnf;


import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Ausmultiplizieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class DNFTestsWithNegation {
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
        Formel formel = new Formel("(nb*e+na)*nc*h");
        Formel expectedFormel = new Formel("(nb*e*nc*h)+(na*nc*h)");
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
        Formel formel = new Formel("(nx+c*nd)*(na+nb*ne)+f");
        Formel expectedFormel = new Formel("(nx*na)+(nx*nb*ne)+(c*nd*na)+(c*nd*nb*ne)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    @Test
    public void negationTest27() {
        Formel formel = new Formel("(c*d+nx)*(na+nb*e)+nf");
        Formel expectedFormel = new Formel("(c*d*na)+(c*d*nb*e)+(nx*na)+(nx*nb*e)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest28() {
        Formel formel = new Formel("(nx+nc*d)*(b*ne+na)+nf");
        Formel expectedFormel = new Formel("(nx*b*ne)+(nx*na)+(nc*d*b*ne)+(nc*d*na)+(nf)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest29() {
        Formel formel = new Formel("nf+(x+c*nd)*(na+nb*ne)");
        Formel expectedFormel = new Formel("(nf)+(x*na)+(x*nb*ne)+(c*nd*na)+(c*nd*nb*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }
    
    @Test
    public void negationTest40() {
        Formel formel = new Formel("(nx+c*nd)*(a+nb*e)*(f*g+nh)");
        Formel expectedFormel = new Formel("(nx*a*f*g)+(nx*a*nh)+(c*nd*a*f*g)+(c*nd*a*nh)+(nx*nb*e*f*g)+(nx*nb*e*nh)+(c*nd*nb*e*f*g)+(c*nd*nb*e*nh)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest41() {
        Formel formel = new Formel("(x+nc*d)*(na+b+ne)");
        Formel expectedFormel = new Formel("(x*na)+(x*b)+(x*ne)+(nc*d*na)+(nc*d*b)+(nc*d*ne)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest42() {
        Formel formel = new Formel("(nx+c+nd)*(na+b*e)");
        Formel expectedFormel = new Formel("(nx*na)+(nx*b*e)+(c*na)+(c*b*e)+(nd*na)+(nd*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest43() {
        Formel formel = new Formel("(nx+nc+d)*(a+nb+e)");
        Formel expectedFormel = new Formel("(nx*a)+(nx*nb)+(nx*e)+(nc*a)+(nc*nb)+(nc*e)+(d*a)+(d*nb)+(d*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest44() {
        Formel formel = new Formel("(x+nc*d)+(na+b*e)+f*ns");
        Formel expectedFormel = new Formel("(x)+(nc*d)+(na)+(b*e)+(f*ns)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest45() {
        Formel formel = new Formel("nx+(na*(nb+c))");
        Formel expectedFormel = new Formel("(nx)+(na*nb)+(na*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest46() {
        Formel formel = new Formel("x+(na*(b+nc))+nz");
        Formel expectedFormel = new Formel("(x)+(na*b)+(na*nc)+(nz)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest47() {
        Formel formel = new Formel("nx*(a*(nb+c))+nz");
        Formel expectedFormel = new Formel("(nx*a*nb)+(nx*a*c)+(nz)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest48() {
        Formel formel = new Formel("x*(na*(b+nc))*nz");
        Formel expectedFormel = new Formel("(x*na*b*nz)+(x*na*nc*nz)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest49() {
        Formel formel = new Formel("nx*(a*(nb*c))");
        Formel expectedFormel = new Formel("(nx*a*nb*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest50() {
        Formel formel = new Formel("x*(na+(nb*nc))");
        Formel expectedFormel = new Formel("(x*na)+(x*nb*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest51() {
        Formel formel = new Formel("nx*(a+(nb+nc))");
        Formel expectedFormel = new Formel("(nx*a)+(nx*nb)+(nx*nc)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest52() {
        Formel formel = new Formel("((nb+c)*na)+nx");
        Formel expectedFormel = new Formel("(nb*na)+(c*na)+(nx)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest53() {
        Formel formel = new Formel("((b+nc)*na)+x+nz");
        Formel expectedFormel = new Formel("(b*na)+(nc*na)+(x)+(nz)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void negationTest54() {
        Formel formel = new Formel("((b+nc)*na)*nx+z");
        Formel expectedFormel = new Formel("(b*na*nx)+(nc*na*nx)+(z)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public  void negationTest55() {
        Formel formel = new Formel("nz+((a+nb)*(nc+nd))*(ne+f)+nx");
        Formel expectedFormel = new Formel("(nz)+(a*nc*ne)+(a*nc*f)+(a*nd*ne)+(a*nd*f)+(nb*nc*ne)+(nb*nc*f)+(nb*nd*ne)+(nb*nd*f)+(nx)");
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
        Formel formel = new Formel("nx+(a*ne*(b+nc))");
        Formel expectedFormel = new Formel("(nx)+(a*ne*b)+(a*ne*nc)");
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
