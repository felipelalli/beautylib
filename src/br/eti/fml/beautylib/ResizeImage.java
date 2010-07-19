package br.eti.fml.beautylib;

import com.mortennobel.imagescaling.ResampleOp;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;

class CustomImageWriteParam extends JPEGImageWriteParam {
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

/**
 *
 * @author Felipe Micaroni Lalli
 */
public class ResizeImage {
    private BufferedImage source;
    private File fileDestination;

    public ResizeImage(BufferedImage source, File fileDestination) {
        this.source = source;
        this.fileDestination = fileDestination;
    }

    public ResizeImage(File source, File fileDestination) throws IOException {
        this.source = ImageIO.read(source);
        this.fileDestination = fileDestination;
    }

    public void doResize() throws IOException {
        ResampleOp resampleOp = new ResampleOp(176, 132);
        BufferedImage rescaled = resampleOp.filter(source, null);

        // Find a jpeg writer
        ImageWriter writer = null;
        Iterator iter = ImageIO.getImageWritersByFormatName("jpeg");
        if (iter.hasNext()) {
            writer = (ImageWriter) iter.next();
        }

        if (writer != null) {
            // Prepare output file
            ImageOutputStream ios = ImageIO.createImageOutputStream(fileDestination);

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
