import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

public class ImagePanel extends JPanel {

    private BufferedImage originalImage;
    private BufferedImage currentImage;
    private int mouseX;
    private int mouseY;
    private boolean isVerticalLine;

    public ImagePanel(BufferedImage originalImage) {
        mouseX = -1;
        mouseY = -1;
        isVerticalLine = true;
        this.originalImage = originalImage;
        this.currentImage = originalImage;
        setPreferredSize(new Dimension(originalImage.getWidth(), originalImage.getHeight()));
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                mouseX = e.getX();
                mouseY = e.getY();
                repaint();
            }
        });
    }

    public void setCurrentImage(BufferedImage currentImage) {
        this.currentImage = currentImage;
        repaint();
    }

    public void setIsVerticalLine(boolean isVerticalLine) {
        this.isVerticalLine = isVerticalLine;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int lineCoord = getLineCoord();

        if (isVerticalLine) {
            g.drawImage(currentImage, 0, 0, lineCoord, originalImage.getHeight(),
                    0, 0, lineCoord, originalImage.getHeight(), null);
            g.drawImage(originalImage, lineCoord, 0, originalImage.getWidth(),
                    originalImage.getHeight(), lineCoord, 0, originalImage.getWidth(),
                    originalImage.getHeight(), null);
            g.setColor(Color.GREEN);
            g.drawLine(lineCoord, 0, lineCoord, originalImage.getHeight() - 1);
        } else {
            g.drawImage(currentImage, 0, 0, originalImage.getWidth(), lineCoord, 0, 0,
                    originalImage.getWidth(), lineCoord, null);
            g.drawImage(originalImage, 0, lineCoord, originalImage.getWidth(),
                    originalImage.getHeight(), 0, lineCoord, originalImage.getWidth(),
                    originalImage.getHeight(), null);
            g.setColor(Color.GREEN);
            g.drawLine(0, lineCoord, originalImage.getWidth() - 1, lineCoord);
        }
    }

    private int getLineCoord() {
        int lineCoord = isVerticalLine ? mouseX : mouseY;

        if (isVerticalLine) {
            if (lineCoord < 0) {
                lineCoord = 0;
            }
            if (lineCoord >= originalImage.getWidth()) {
                lineCoord = originalImage.getWidth() - 1;
            }
        } else {
            if (lineCoord < 0) {
                lineCoord = 0;
            }
            if (lineCoord >= originalImage.getHeight()) {
                lineCoord = originalImage.getHeight() - 1;
            }
        }
        return lineCoord;
    }
}
