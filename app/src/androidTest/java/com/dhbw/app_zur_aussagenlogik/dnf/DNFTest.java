package com.dhbw.app_zur_aussagenlogik.dnf;
import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.core.Ausmultiplizieren;
import com.dhbw.app_zur_aussagenlogik.core.Formel;
import com.dhbw.app_zur_aussagenlogik.core.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DNFTest {

	@Test
	public void test1() {
		Formel formel = new Formel("c*(a+b*e)");
		Formel expectedFormel = new Formel("(c*a)+(c*b*e)");
		Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
		assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
	}
	
	@Test
	public void test2() {
		Formel formel = new Formel("c*(a+b*e)*d");
		Formel expectedFormel = new Formel("(c*a*d)+(c*b*e*d)");
		Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
		assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
	}
	
	@Test
	public void test3() {
		Formel formel = new Formel("c*d*(a+b*e)");
		Formel expectedFormel = new Formel("(c*d*a)+(c*d*b*e)");
		Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
		assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
	}
	
	@Test
	public void test4() {
		Formel formel = new Formel("c*d*(a+b*e)+f");
		Formel expectedFormel = new Formel("(c*d*a)+(c*d*b*e)+(f)");
		Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
		assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
	}

	@Test
	public void test5() {
		Formel formel = new Formel("x+c*d*(a+b*e)+f");
		Formel expectedFormel = new Formel("(x)+(c*d*a)+(c*d*b*e)+(f)");
		Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
		assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
	}
	
	@Test
	public void test6() {
		Formel formel = new Formel("(x+c*d)*(a+b*e)+f");
		Formel expectedFormel = new Formel("(x*a)+(x*b*e)+(c*d*a)+(c*d*b*e)+(f)");
		Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
		assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
	}
	
	@Test
	public void test7() {
		Formel formel = new Formel("c*d*(a+b*e)+f+s");
		Formel expectedFormel = new Formel("(c*d*a)+(c*d*b*e)+(f)+(s)");
		Formel result = Ausmultiplizieren.ausmultiplizieren(formel);
		assertArrayEquals(expectedFormel.getFormel(), result.getFormel());
	}
	
}
