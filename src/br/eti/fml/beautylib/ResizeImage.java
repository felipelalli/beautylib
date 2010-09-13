package br.eti.fml.beautylib;

import com.mortennobel.imagescaling.ResampleOp;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

/**
 * It is capable to resize an image from an {@link java.io.InputStream}
 * to an {@link java.io.OutputStream}.
 *
 * @author Felipe Micaroni Lalli
 */
public class ResizeImage {
    
    public enum Type {JPEG, GIF, PNG}
    
    private BufferedImage source;

    private class CustomImageWriteParam extends JPEGImageWriteParam {
        public CustomImageWriteParam() {
            super(Locale.getDefault());
        }

        @Override
        public void setCompressionQuality(float quality) {
            if (quality < 0.0F || quality > 1.0F) {
                throw new IllegalArgumentException("Quality out-of-bounds!");
            }
            this.compressionQuality = 256 - (quality * 256);
        }
    }

    public ResizeImage(BufferedImage source) {
        this.source = source;
    }

    public ResizeImage(InputStream source) throws IOException {
        this.source = ImageIO.read(source);
    }

    public ResizeImage(File source) throws IOException {
        this.source = ImageIO.read(source);
    }

    public void doResize(int width, int height, File fileDestination) throws IOException {
        this.doResize(width, height, new FileOutputStream(fileDestination));
    }

    /**
     * This method generate a JPEG like default
     * @param width
     * @param height
     * @param destination
     * @throws IOException
     */
    public void doResize(int width, int height, OutputStream destination) throws IOException {
        doResize(width, height, destination, Type.JPEG);
    }
    
    public void doResize(int width, int height, OutputStream destination, Type type) throws IOException {
        switch (type) {
            case GIF:
                doResizeGif(width, height, destination);
                break;
            case PNG:
                doResizePng(width, height, destination);
                break;
            default:
                doResizeJpeg(width, height, destination);
                break;
        }
    }
    
    private void doResizeJpeg(int width, int height, OutputStream destination) throws IOException {
        ResampleOp resampleOp = new ResampleOp(width, height);
        BufferedImage rescaled = resampleOp.filter(source, null);

        // Find a jpeg writer
        ImageWriter writer = null;
        Iterator<?> iter = ImageIO.getImageWritersByFormatName("jpeg");
        if (iter.hasNext()) {
            writer = (ImageWriter) iter.next();
        }

        if (writer == null) {
            throw new IOException("jpeg is not supported by this system!");
        } else {
            // Prepare output file
            ImageOutputStream ios = ImageIO.createImageOutputStream(destination);

            writer.setOutput(ios);

            // Set the compression quality
            ImageWriteParam iwparam = new CustomImageWriteParam();
            iwparam.setCompressionMode(ImageWriteParam.MODE_EXPLICIT) ;
            iwparam.setCompressionQuality(0f);

            // Write the image
            writer.write(null, new IIOImage(rescaled, null, null), iwparam);

            // Cleanup
            ios.flush();
            writer.dispose();
            ios.close();
        }
    }
    
    private void doResizeGif(int width, int height, OutputStream o) throws IOException {
        Image img = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = buffImg.getGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        ImageIO.write(buffImg, "gif", o);
        //TODO Should work with Animated GIF 
    }

    private void doResizePng(int width, int height, OutputStream o) throws IOException {
        Image img = source.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage buffImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics g = buffImg.getGraphics();
        g.drawImage(img, 0, 0, width, height, null);
        g.dispose();
        ImageIO.write(buffImg, "png", o);
    }
}
