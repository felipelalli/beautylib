package br.eti.fml.beautylib;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Felipe Micaroni Lalli
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ResizeImage ri = new ResizeImage(new File("db/a.jpg"));
        ri.doResize(176, 132, new File("db/b.jpg"));
        ri.doResize(176*2, 132*2, new File("db/c.jpg"));
        ri.doResize(176*5, 132*5, new File("db/d.jpg"));
    }
}
