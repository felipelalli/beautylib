package br.eti.fml.beautylib;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 *
 * @author Felipe Micaroni Lalli
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ResizeImage ri = new ResizeImage(new FileInputStream("db/a.jpg"));
        ri.doResize(176, 132, new FileOutputStream("db/b.jpg"));
        ri.doResize(176*2, 132*2, new FileOutputStream("db/c.jpg"));
        ri.doResize(176*5, 132*5, new FileOutputStream("db/d.jpg"));
    }
}
