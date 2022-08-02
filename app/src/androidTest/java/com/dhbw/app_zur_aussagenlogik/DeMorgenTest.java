package com.dhbw.app_zur_aussagenlogik;

import static org.junit.Assert.assertArrayEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.dhbw.app_zur_aussagenlogik.parser.Parser;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DeMorgenTest {
    
    @Test
    public void einfacherTest(){
        Parser p = new Parser();
        char[] formel = {'n', '(', 'a', '*', 'n', 'b', ')'};
        char[] expectedFormel = {'n', 'a', '+', 'b'};
        assertArrayEquals(expectedFormel, p.deMorgen(formel));
    }
}
