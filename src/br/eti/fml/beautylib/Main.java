package br.eti.fml.beautylib;

import ij.ImagePlus;
import ij.io.FileSaver;
import ij.process.ImageProcessor;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;

/**
 *
 * @author Felipe Micaroni Lalli
 */
public class Main {
    public static void main(String[] args) throws IOException {
        //String param = args[0];
        String param = "db/a.jpg";

        BufferedImage source = ImageIO.read(new File(param));
        BufferedImage source2 = ImageIO.read(new File(param));
//        ImagePlus imagePlus = new ImagePlus(param, image);
//        ImageProcessor processor = imagePlus.getProcessor();
//        processor.setInterpolationMethod(ImageProcessor.BICUBIC);
//        ImagePlus newImage = new ImagePlus("new image",
//                processor.resize(176, 132).getBufferedImage());

        Graphics2D g2 = (Graphics2D) source.getGraphics();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                            RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
                            RenderingHints.VALUE_COLOR_RENDER_QUALITY);         

        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                            RenderingHints.VALUE_RENDER_QUALITY);

        g2.scale(176d / 640d, 176d / 640d);

        FileSaver.setJpegQuality(100);

        {
            FileSaver fileSaver = new FileSaver(new ImagePlus("resized", source));            
            fileSaver.saveAsJpeg(param + "-resized.jpg");
        }
        
        {
            FileSaver fileSaver = new FileSaver(new ImagePlus("just", source2));
            fileSaver.saveAsJpeg(param + "-just.jpg");
        }


    }
}
