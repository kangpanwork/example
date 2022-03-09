package com.sanri.test.sometest;

import org.junit.Test;

import java.net.URI;
import java.net.URISyntaxException;

public class UrlResolveMain {

    @Test
    public void testP() throws URISyntaxException {
        URI uri = new URI("https://www.xsbiquge.com/81_81211/");
        URI uri1 = new URI("/81_81211/221000.html");
        System.out.println(uri.resolve(uri1));
    }
}
