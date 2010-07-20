package br.eti.fml.beautylib;

import com.mortennobel.imagescaling.ResampleOp;
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

    public void doResize(int width, int height, OutputStream destination) throws IOException {
        ResampleOp resampleOp = new ResampleOp(width, height);
        BufferedImage rescaled = resampleOp.filter(source, null);

        // Find a jpeg writer
        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
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
}
