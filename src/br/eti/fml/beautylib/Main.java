package br.eti.fml.beautylib;

import java.io.File;
import java.io.IOException;

/**
 *
 * @author Felipe Micaroni Lalli
 */
public class Main {
    public static void main(String[] args) throws IOException {
        ResizeImage ri = new ResizeImage(new File("db/a.jpg"), new File("db/b.jpg"));
        ri.doResize();
    }
}
