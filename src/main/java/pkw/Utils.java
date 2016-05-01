package pkw;

import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Elimas on 2016-05-01.
 */
public class Utils {

    public static BufferedImage cropImageSquare(byte[] image) throws IOException {
        // Get a BufferedImage object from a byte array
        InputStream in = new ByteArrayInputStream(image);
        BufferedImage originalImage = ImageIO.read(in);

        // Get image dimensions
        int height = originalImage.getHeight();
        int width = originalImage.getWidth();

        // The image is already a square
        if (height == width) {
            return originalImage;
        }

        // Compute the size of the square
        int squareSize = (height > width ? width : height);

        // Coordinates of the image's middle
        int xc = width / 2;
        int yc = height / 2;

        // Crop
        BufferedImage croppedImage = originalImage.getSubimage(
                xc - (squareSize / 2), // x coordinate of the upper-left corner
                yc - (squareSize / 2), // y coordinate of the upper-left corner
                squareSize,            // widht
                squareSize             // height
        );

        return croppedImage;
    }

    public static void saveUploadedImage(MultipartFile image, String dirName, String filename) throws IOException {
        File dir = new File("upload/" + dirName + "/");
        dir.mkdirs();
        BufferedImage squareImage = Utils.cropImageSquare(image.getBytes());
        ImageIO.write(squareImage, "jpg", new File(dir, filename + ".jpg"));
    }
}
