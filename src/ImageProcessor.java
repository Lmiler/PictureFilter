import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class ImageProcessor extends JPanel {

    public static final int COLOR_ADD = 100;
    public static final int CHANGE = 80;
    public static final int NOISE_ADDING = 60;
    public static final int RGB_MIN_VALUE = 0;
    public static final int RGB_MAX_VALUE = 255;

    private BufferedImage originalImage;
    private BufferedImage grayscaleImage;
    private BufferedImage negativeImage;
    private BufferedImage blackAndWhiteImage;
    private BufferedImage mirroredImage;
    private BufferedImage tintImage;
    private BufferedImage eliminateRedImage;
    private BufferedImage eliminateGreenImage;
    private BufferedImage eliminateBlueImage;
    private BufferedImage lighterImage;
    private BufferedImage darkerImage;
    private BufferedImage sepiaImage;
    private BufferedImage noiseImage;
    private BufferedImage vintageImage;


    public ImageProcessor(File file) {

        originalImage = loadImage(file);
        grayscaleImage = grayscale(originalImage);
        negativeImage = negative(originalImage);
        blackAndWhiteImage = blackAndWhite(originalImage);
        mirroredImage = mirror(originalImage);
        eliminateRedImage = colorElimination(originalImage, 'r');
        eliminateBlueImage = colorElimination(originalImage, 'b');
        eliminateGreenImage = colorElimination(originalImage, 'g');
        tintImage = tint(originalImage);
        lighterImage = lighter(originalImage);
        darkerImage = darker(originalImage);
        sepiaImage = sepia(originalImage);
        noiseImage = noise(originalImage);
        vintageImage = vintage(originalImage);
    }

    private BufferedImage loadImage(File file) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(file);
        } catch (IOException ignored) {
        }
        return image;
    }

    private BufferedImage blackAndWhite(BufferedImage image) {
        BufferedImage blackAndWhiteImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                int brightness = (int) (0.299 * color.getRed() + 0.587 * color.getGreen() + 0.114 * color.getBlue());
                if (brightness < RGB_MAX_VALUE / 2) {
                    blackAndWhiteImage.setRGB(x, y, Color.BLACK.getRGB());
                } else {
                    blackAndWhiteImage.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }
        return blackAndWhiteImage;
    }

    private BufferedImage grayscale(BufferedImage image) {
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        grayImage.getGraphics().drawImage(image, 0, 0, null);

        return grayImage;
    }

//    private BufferedImage grayscale(BufferedImage image) {
//        BufferedImage grayscaleImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
//        for (int x = 0; x < image.getWidth(); x++) {
//            for (int y = 0; y < image.getHeight(); y++) {
//                Color color = new Color(image.getRGB(x, y));
//                int average = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
//                Color newColor = new Color(average, average, average);
//                grayscaleImage.setRGB(x, y, newColor.getRGB());
//            }
//        }
//        return grayscaleImage;
//    }

    private BufferedImage negative(BufferedImage image) {
        BufferedImage negImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color currentColor = new Color(image.getRGB(x, y));
                int red = RGB_MAX_VALUE - currentColor.getRed();
                int green = RGB_MAX_VALUE - currentColor.getGreen();
                int blue = RGB_MAX_VALUE - currentColor.getBlue();
                Color updatedColor = new Color(red, green, blue);
                negImage.setRGB(x, y, updatedColor.getRGB());
            }
        }
        return negImage;
    }

    private BufferedImage mirror(BufferedImage image) {
        BufferedImage mirroredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth() / 2; x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color currentColor = new Color(image.getRGB(x, y));
                mirroredImage.setRGB(x, y, new Color(image.getRGB(image.getWidth() - x - 2, y)).getRGB());
                mirroredImage.setRGB(image.getWidth() - x - 2, y, currentColor.getRGB());
            }
        }
        return mirroredImage;
    }

    private BufferedImage tint(BufferedImage image) {
        BufferedImage tintImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                int average = (color.getRed() + color.getGreen() + color.getBlue()) / 3;
                Color newColor = new Color(Math.min(RGB_MAX_VALUE, average + COLOR_ADD), average, average);
                tintImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return tintImage;
    }

    private BufferedImage colorElimination(BufferedImage image, char colorToEliminate) {
        BufferedImage eliminatedImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color color = new Color(image.getRGB(x, y));
                Color newColor = getNewColor(colorToEliminate, color);
                eliminatedImage.setRGB(x, y, newColor.getRGB());
            }
        }
        return eliminatedImage;
    }

    private Color getNewColor(char colorToEliminate, Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        switch (colorToEliminate) {
            case 'r' -> red = RGB_MIN_VALUE;
            case 'g' -> green = RGB_MIN_VALUE;
            case 'b' -> blue = RGB_MIN_VALUE;
        }
        return new Color(red, green, blue);
    }

    private BufferedImage lighter(BufferedImage image) {
        BufferedImage lighterImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color currentColor = new Color(image.getRGB(x, y));
                int red = currentColor.getRed();
                int green = currentColor.getGreen();
                int blue = currentColor.getBlue();
                red = Math.min(red + CHANGE, RGB_MAX_VALUE);
                green = Math.min(green + CHANGE, RGB_MAX_VALUE);
                blue = Math.min(blue + CHANGE, RGB_MAX_VALUE);
                Color updatedColor = new Color(red, green, blue);
                lighterImage.setRGB(x, y, updatedColor.getRGB());
            }
        }
        return lighterImage;
    }

    private BufferedImage darker(BufferedImage image) {
        BufferedImage darkerImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color currentColor = new Color(image.getRGB(x, y));
                int red = currentColor.getRed();
                int green = currentColor.getGreen();
                int blue = currentColor.getBlue();
                red = Math.max(red - CHANGE, RGB_MIN_VALUE);
                green = Math.max(green - CHANGE, RGB_MIN_VALUE);
                blue = Math.max(blue - CHANGE, RGB_MIN_VALUE);
                Color updatedColor = new Color(red, green, blue);
                darkerImage.setRGB(x, y, updatedColor.getRGB());
            }
        }
        return darkerImage;
    }

    private BufferedImage sepia(BufferedImage image) {
        BufferedImage sepiaImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < image.getWidth() - 1; x++) {
            for (int y = 0; y < image.getHeight() - 1; y++) {
                Color currentColor = new Color(image.getRGB(x, y));
                Color sepiaColor = getSepiaColor(currentColor);
                sepiaImage.setRGB(x, y, sepiaColor.getRGB());
            }
        }
        return sepiaImage;
    }

    private static Color getSepiaColor(Color currentColor) {
        int red = (int) ((currentColor.getRed() * 0.393) + (currentColor.getGreen() * 0.769) +
                (currentColor.getBlue() * 0.189));
        int green = (int) ((currentColor.getRed() * 0.349) + (currentColor.getGreen() * 0.686) +
                (currentColor.getBlue() * 0.168));
        int blue = (int) ((currentColor.getRed() * 0.272) + (currentColor.getGreen() * 0.534) +
                (currentColor.getBlue() * 0.131));
        return new Color(Math.min(red, RGB_MAX_VALUE), Math.min(green, RGB_MAX_VALUE),
                Math.min(blue, RGB_MAX_VALUE));
    }

    private BufferedImage noise(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        BufferedImage noisyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Random random = new Random();
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = new Color(image.getRGB(x, y));
                int r = color.getRed();
                int g = color.getGreen();
                int b = color.getBlue();
                int noise = random.nextInt(-1 * NOISE_ADDING, NOISE_ADDING + 1);
                r = Math.min(Math.max(r + noise, RGB_MIN_VALUE), RGB_MAX_VALUE);
                g = Math.min(Math.max(g + noise, RGB_MIN_VALUE), RGB_MAX_VALUE);
                b = Math.min(Math.max(b + noise, RGB_MIN_VALUE), RGB_MAX_VALUE);
                Color noisyColor = new Color(r, g, b);
                noisyImage.setRGB(x, y, noisyColor.getRGB());
            }
        }
        return noisyImage;
    }

    private BufferedImage vintage(BufferedImage image) {
        return sepia(noise(image));
    }

    public BufferedImage getOriginalImage() {
        return originalImage;
    }

    public BufferedImage getGrayscaleImage() {
        return grayscaleImage;
    }

    public BufferedImage getNegativeImage() {
        return negativeImage;
    }

    public BufferedImage getBlackAndWhiteImage() {
        return blackAndWhiteImage;
    }

    public BufferedImage getMirroredImage() {
        return mirroredImage;
    }

    public BufferedImage getEliminateRedImage() {
        return eliminateRedImage;
    }

    public BufferedImage getEliminateBlueImage() {
        return eliminateBlueImage;
    }

    public BufferedImage getEliminateGreenImage() {
        return eliminateGreenImage;
    }

    public BufferedImage getTintImage() {
        return tintImage;
    }

    public BufferedImage getLighterImage() {
        return lighterImage;
    }

    public BufferedImage getDarkerImage() {
        return darkerImage;
    }

    public BufferedImage getSepiaImage() {
        return sepiaImage;
    }

    public BufferedImage getNoiseImage() {
        return noiseImage;
    }

    public BufferedImage getVintageImage() {
        return vintageImage;
    }
}
