package com.dhbw.app_zur_aussagenlogik.dnf;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Ausmultiplizieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DNFTestByConcept {

    @Test
    public void test1() {
        Formel formel = new Formel("c*(a+b*e)");
        Formel expectedFormel = new Formel("(c*a)+(c*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test2() {
        Formel formel = new Formel("(a+b*e)*c");
        Formel expectedFormel = new Formel("(a*c)+(b*e*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test3() {
        Formel formel = new Formel("(a+b)*c*e");
        Formel expectedFormel = new Formel("(a*c*e)+(b*c*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test4() {
        Formel formel = new Formel("c*e*(a+b)");
        Formel expectedFormel = new Formel("(c*e*a)+(c*e*b)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test5() {
        Formel formel = new Formel("c*h*(a+b*e)");
        Formel expectedFormel = new Formel("(c*h*a)+(c*h*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test6() {
        Formel formel = new Formel("(a+b*e)*c*h");
        Formel expectedFormel = new Formel("(a*c*h)+(b*e*c*h)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test7() {
        Formel formel = new Formel("h*(a+b*e)*c");
        Formel expectedFormel = new Formel("(h*a*c)+(h*b*e*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test8() {
        Formel formel = new Formel("c*h*(b*e+a)");
        Formel expectedFormel = new Formel("(c*h*b*e)+(c*h*a)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test9() {
        Formel formel = new Formel("(b*e+a)*c*h");
        Formel expectedFormel = new Formel("(b*e*c*h)+(a*c*h)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test10() {
        Formel formel = new Formel("c*(a+b*e)+f");
        Formel expectedFormel = new Formel("(c*a)+(c*b*e)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test11() {
        Formel formel = new Formel("(a+b*e)*c+f");
        Formel expectedFormel = new Formel("(a*c)+(b*e*c)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test12() {
        Formel formel = new Formel("(a+b)*c*e+f");
        Formel expectedFormel = new Formel("(a*c*e)+(b*c*e)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test13() {
        Formel formel = new Formel("c*e*(a+b)+f");
        Formel expectedFormel = new Formel("(c*e*a)+(c*e*b)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test14() {
        Formel formel = new Formel("f+c*(a+b*e)");
        Formel expectedFormel = new Formel("(f)+(c*a)+(c*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test15() {
        Formel formel = new Formel("f+(a+b*e)*c");
        Formel expectedFormel = new Formel("(f)+(a*c)+(b*e*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test16() {
        Formel formel = new Formel("f+(a+b)*c*e");
        Formel expectedFormel = new Formel("(f)+(a*c*e)+(b*c*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test17() {
        Formel formel = new Formel("f+c*e*(a+b)");
        Formel expectedFormel = new Formel("(f)+(c*e*a)+(c*e*b)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test18() {
        Formel formel = new Formel("(a+b*e)*c*e+f");
        Formel expectedFormel = new Formel("(a*c*e)+(b*e*c*e)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test19() {
        Formel formel = new Formel("c*(a+b*e)*h+f");
        Formel expectedFormel = new Formel("(c*a*h)+(c*b*e*h)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test20() {
        Formel formel = new Formel("f+c*(a+b*e)*h");
        Formel expectedFormel = new Formel("(f)+(c*a*h)+(c*b*e*h)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test21() {
        Formel formel = new Formel(" f+c*(a+b*e)*h+d");
        Formel expectedFormel = new Formel("(f)+(c*a*h)+(c*b*e*h)+(d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test22() {
        Formel formel = new Formel("f+d+c*(a+b*e)*h");
        Formel expectedFormel = new Formel("(f)+(d)+(c*a*h)+(c*b*e*h)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test23() {
        Formel formel = new Formel("c*(a+b*e)*h+d+f");
        Formel expectedFormel = new Formel("(c*a*h)+(c*b*e*h)+(d)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test24() {
        Formel formel = new Formel("(a+b*e)*c*h+d+f");
        Formel expectedFormel = new Formel("(a*c*h)+(b*e*c*h)+(d)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test25() {
        Formel formel = new Formel("c*h*(a+b*e)+d+f");
        Formel expectedFormel = new Formel("(c*h*a)+(c*h*b*e)+(d)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test26() {
        Formel formel = new Formel("(g+c*d)*(a+b*e)+f");
        Formel expectedFormel = new Formel("(g*a)+(c*d*a)+(g*b*e)+(c*d*b*e)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test27() {
        Formel formel = new Formel("(c*d+g)*(a+b*e)+f");
        Formel expectedFormel = new Formel("(c*d*a)+(g*a)+(c*d*b*e)+(g*b*e)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test28() {
        Formel formel = new Formel("(g+c*d)*(b*e+a)+f");
        Formel expectedFormel = new Formel("(g*b*e)+(c*d*b*e)+(g*a)+(c*d*a)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test29() {
        Formel formel = new Formel("f+(g+c*d)*(a+b*e)");
        Formel expectedFormel = new Formel("(f)+(g*a)+(c*d*a)+(g*b*e)+(c*d*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test30() {
        Formel formel = new Formel("(a*b*e)*(g+c*d)+f");
        Formel expectedFormel = new Formel("(a*b*e*g)+(a*b*e*c*d)+(f)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test31() {
        Formel formel = new Formel("(g+c*d)*(a+b*e)+f*s");
        Formel expectedFormel = new Formel("(g*a)+(c*d*a)+(g*b*e)+(c*d*b*e)+(f*s)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test32() {
        Formel formel = new Formel("a*b");
        Formel expectedFormel = new Formel("(a*b)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test33() {
        Formel formel = new Formel("a*b*c");
        Formel expectedFormel = new Formel("(a*b*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test34() {
        Formel formel = new Formel("a*b*c*d");
        Formel expectedFormel = new Formel("(a*b*c*d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test35() {
        Formel formel = new Formel("a+b");
        Formel expectedFormel = new Formel("(a)+(b)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test36() {
        Formel formel = new Formel("a+b+c");
        Formel expectedFormel = new Formel("(a)+(b)+(c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test37() {
        Formel formel = new Formel("a+b+c+d");
        Formel expectedFormel = new Formel("(a)+(b)+(c)+(d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test38() {
        Formel formel = new Formel("a*b+c");
        Formel expectedFormel = new Formel("(a*b)+(c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test39() {
        Formel formel = new Formel("a+c*b+d*e");
        Formel expectedFormel = new Formel("(a)+(c*b)+(d*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test40() {
        Formel formel = new Formel("(i+c*d)*(a+b*e)*(f*g+h)");
        Formel expectedFormel = new Formel("(i*a*f*g)+(c*d*a*f*g)+(i*a*h)+(c*d*a*h)+(i*b*e*f*g)+(c*d*b*e*f*g)+(i*b*e*h)+(c*d*b*e*h)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test41() {
        Formel formel = new Formel("(g+c*d)*(a+b+e)");
        Formel expectedFormel = new Formel("(g*a)+(c*d*a)+(g*b)+(c*d*b)+(g*e)+(c*d*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test42() {
        Formel formel = new Formel("(g+c+d)*(a+b*e)");
        Formel expectedFormel = new Formel("(g*a)+(c*a)+(d*a)+(g*b*e)+(c*b*e)+(d*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test43() {
        Formel formel = new Formel("(g+c+d)*(a+b+e)");
        Formel expectedFormel = new Formel("(g*a)+(c*a)+(d*a)+(g*b)+(c*b)+(d*b)+(g*e)+(c*e)+(d*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test44() {
        Formel formel = new Formel("(g+c*d)+(a+b*e)+f*s");
        Formel expectedFormel = new Formel("(g)+(c*d)+(a)+(b*e)+(f*s)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test45() {
        Formel formel = new Formel("g+(a*(b+c))");
        Formel expectedFormel = new Formel("(g)+(a*b)+(a*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test45_1() {
        Formel formel = new Formel("g+(a*e*(b+c))");
        Formel expectedFormel = new Formel("(g)+(a*e*b)+(a*e*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test46() {
        Formel formel = new Formel("g+(a*(b+c))+d");
        Formel expectedFormel = new Formel("(g)+(a*b)+(a*c)+(d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test47() {
        Formel formel = new Formel("g*(a*(b+c))+d");
        Formel expectedFormel = new Formel("(g*a*b)+(g*a*c)+(d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test48() {
        Formel formel = new Formel("g*(a*(b+c))*d");
        Formel expectedFormel = new Formel("(g*a*b*d)+(g*a*c*d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test49() {
        Formel formel = new Formel("g*(a*(b*c))");
        Formel expectedFormel = new Formel("(g*a*b*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test50() {
        Formel formel = new Formel("g*(a+(b*c))");
        Formel expectedFormel = new Formel("(g*a)+(g*b*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test51() {
        Formel formel = new Formel("g*(a+(b+c))");
        Formel expectedFormel = new Formel("(g*a)+(g*b)+(g*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test52() {
        Formel formel = new Formel("((b+c)*a)+g");
        Formel expectedFormel = new Formel("(b*a)+(c*a)+(g)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test53() {
        Formel formel = new Formel("((b+c)*a)+g+d");
        Formel expectedFormel = new Formel("(b*a)+(c*a)+(g)+(d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test54() {
        Formel formel = new Formel("((b+c)*a)*g+d");
        Formel expectedFormel = new Formel("(b*a*g)+(c*a*g)+(d)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test55() {
        Formel formel = new Formel("h+((a+b)*(c+d))*(e+f)+g");
        Formel expectedFormel = new Formel("(h)+(a*c*e)+(b*c*e)+(a*d*e)+(b*d*e)+(a*c*f)+(b*c*f)+(a*d*f)+(b*d*f)+(g)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test56() {
        Formel formel = new Formel("h*(a+b)*(c+d+e)");
        Formel expectedFormel = new Formel("(h*a*c)+(h*a*d)+(h*a*e)+(h*b*c)+(h*b*d)+(h*b*e)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test561() {
        Formel formel = new Formel("(a+b)*(c+d+e)*(a+b)");
        Formel expectedFormel = new Formel("(a*c*a)+(b*c*a)+(a*c*b)+(b*c*b)+(a*d*a)+(b*d*a)+" +
                "(a*d*b)+(b*d*b)+(a*e*a)+(b*e*a)+(a*e*b)+(b*e*b)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test57() {
        Formel formel = new Formel("g+(a*e*(b+c))");
        Formel expectedFormel = new Formel("(g)+(a*e*b)+(a*e*c)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test58() {
        Formel formel = new Formel("r*(a+b+h)*(c+d+e)*(g+j+k)");
        Formel expectedFormel = new Formel("(r*a*c*g)+(r*a*c*j)+(r*a*c*k)+(r*a*d*g)+(r*a*d*j)+" +
                "(r*a*d*k)+(r*a*e*g)+(r*a*e*j)+(r*a*e*k)+(r*b*c*g)+(r*b*c*j)+(r*b*c*k)+(r*b*d*g)+" +
                "(r*b*d*j)+(r*b*d*k)+(r*b*e*g)+(r*b*e*j)+(r*b*e*k)+(r*h*c*g)+(r*h*c*j)+(r*h*c*k)+" +
                "(r*h*d*g)+(r*h*d*j)+(r*h*d*k)+(r*h*e*g)+(r*h*e*j)+(r*h*e*k)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test59() {
        Formel formel = new Formel("(a+b)*(c+d)*(e+f)*(g+h)");
        Formel expectedFormel = new Formel("(a*c*e*g)+(b*c*e*g)+(a*c*e*h)+(b*c*e*h)+(a*c*f*g)+" +
                "(b*c*f*g)+(a*c*f*h)+(b*c*f*h)+(a*d*e*g)+(b*d*e*g)+(a*d*e*h)+(b*d*e*h)+(a*d*f*g)+" +
                "(b*d*f*g)+(a*d*f*h)+(b*d*f*h)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

    @Test
    public void test60() {
        Formel formel = new Formel("(a*b+c)*(d+e+f)*(g+h+i)*(j+k+l)");
        Formel expectedFormel = new Formel("(a*b*d*g*j)+(c*d*g*j)+(a*b*d*g*k)+(c*d*g*k)+(a*b*d*g*l)+" +
                "(c*d*g*l)+(a*b*d*h*j)+(c*d*h*j)+(a*b*d*h*k)+(c*d*h*k)+(a*b*d*h*l)+(c*d*h*l)+" +
                "(a*b*d*i*j)+(c*d*i*j)+(a*b*d*i*k)+(c*d*i*k)+(a*b*d*i*l)+(c*d*i*l)+(a*b*e*g*j)+" +
                "(c*e*g*j)+(a*b*e*g*k)+(c*e*g*k)+(a*b*e*g*l)+(c*e*g*l)+(a*b*e*h*j)+(c*e*h*j)+" +
                "(a*b*e*h*k)+(c*e*h*k)+(a*b*e*h*l)+(c*e*h*l)+(a*b*e*i*j)+(c*e*i*j)+(a*b*e*i*k)+" +
                "(c*e*i*k)+(a*b*e*i*l)+(c*e*i*l)+(a*b*f*g*j)+(c*f*g*j)+(a*b*f*g*k)+(c*f*g*k)+" +
                "(a*b*f*g*l)+(c*f*g*l)+(a*b*f*h*j)+(c*f*h*j)+(a*b*f*h*k)+(c*f*h*k)+(a*b*f*h*l)+" +
                "(c*f*h*l)+(a*b*f*i*j)+(c*f*i*j)+(a*b*f*i*k)+(c*f*i*k)+(a*b*f*i*l)+(c*f*i*l)");
        Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
        assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
    }

}




