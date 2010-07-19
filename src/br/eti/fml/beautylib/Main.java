package br.eti.fml.beautylib;

import ij.ImagePlus;
import ij.io.FileSaver;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

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
        BufferedImage newImage = new BufferedImage(
                176, 132, BufferedImage.TYPE_INT_RGB);

//        ImagePlus imagePlus = new ImagePlus(param, image);
//        ImageProcessor processor = imagePlus.getProcessor();
//        processor.setInterpolationMethod(ImageProcessor.BICUBIC);
//        ImagePlus newImage = new ImagePlus("new image",
//                processor.resize(176, 132).getBufferedImage());

        Graphics2D g2 = (Graphics2D) newImage.createGraphics();

//        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
//                            RenderingHints.VALUE_ANTIALIAS_ON);

//        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
//                            RenderingHints.VALUE_INTERPOLATION_BILINEAR);

//        g2.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING,
//                            RenderingHints.VALUE_COLOR_RENDER_QUALITY);
//
//        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
//                            RenderingHints.VALUE_RENDER_QUALITY);

        AffineTransform at = new AffineTransform();
        at.scale(176d / 640d, 176d / 640d);

        g2.drawImage(source, new AffineTransformOp(at, g2.getRenderingHints()), 0, 0);

        FileSaver.setJpegQuality(100);

        {
            FileSaver fileSaver = new FileSaver(new ImagePlus("resized", newImage));
            fileSaver.saveAsJpeg(param + "-resized.jpg");
        }
        
        {
            FileSaver fileSaver = new FileSaver(new ImagePlus("just", source2));
            fileSaver.saveAsJpeg(param + "-just.jpg");
        }


    }
}
