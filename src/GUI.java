import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class GUI extends JFrame {

    private ImageProcessor imageProcessor;
    private BufferedImage currentImage;
    private ImagePanel imagePanel;
    private boolean isVerticalLine;

    public GUI() {
        isVerticalLine = true;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Image Filter");
        JFileChooser fileChooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image and GIF files", "jpg", "jpeg", "png", "gif");
        fileChooser.setFileFilter(filter);
        int result = fileChooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            System.out.println("Selected file: " + selectedFile.getAbsolutePath());

            imageProcessor = new ImageProcessor(selectedFile);
            currentImage = imageProcessor.getOriginalImage();

            initGUI();
        } else {
            System.out.println("No file selected");
        }
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void initGUI() {
        JComboBox<String> filterComboBox = getStringJComboBox();

        JRadioButton verticalButton = new JRadioButton("Vertical Line", true);
        JRadioButton horizontalButton = new JRadioButton("Horizontal Line", false);
        ButtonGroup lineGroup = new ButtonGroup();
        lineGroup.add(verticalButton);
        lineGroup.add(horizontalButton);

        lineOrientationSelection(verticalButton, horizontalButton);

        JPanel controlPanel = getJPanel(filterComboBox, verticalButton, horizontalButton);

        imagePanel = new ImagePanel(imageProcessor.getOriginalImage());

        add(controlPanel, BorderLayout.NORTH);
        add(imagePanel, BorderLayout.CENTER);

        pack();
    }

    private void lineOrientationSelection(JRadioButton verticalButton, JRadioButton horizontalButton) {
        ActionListener lineOrientationListener = e -> {
            if (verticalButton.isSelected()) {
                isVerticalLine = true;
                imagePanel.setIsVerticalLine(true);
            } else if (horizontalButton.isSelected()) {
                isVerticalLine = false;
                imagePanel.setIsVerticalLine(false);
            }
            imagePanel.repaint();
        };
        verticalButton.addActionListener(lineOrientationListener);
        horizontalButton.addActionListener(lineOrientationListener);
    }

    private JPanel getJPanel(JComboBox<String> filterComboBox, JRadioButton verticalButton, JRadioButton horizontalButton) {
        JPanel controlPanel = new JPanel(new GridLayout(2, 1));
        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        comboPanel.add(new JLabel("Select Filter:"));
        comboPanel.add(filterComboBox);
        controlPanel.add(comboPanel);
        JPanel linePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        linePanel.add(new JLabel("Line Orientation:"));
        linePanel.add(verticalButton);
        linePanel.add(horizontalButton);
        controlPanel.add(linePanel);
        return controlPanel;
    }

    private JComboBox<String> getStringJComboBox() {
        String[] filterTypes = {"Original", "B & W", "Grayscale", "Tint", "Mirror", "Color elimination - Red",
                "Color elimination - Blue", "Color elimination - Green",
                "Negative", "Sepia", "Lighter", "Darker", "Noise", "Vintage"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterTypes);
        filterComboBox.setSelectedIndex(0);
        filterComboBox.addActionListener(e -> {
            String selectedFilter = (String) filterComboBox.getSelectedItem();
            switch (selectedFilter) {
                case "Original" -> currentImage = imageProcessor.getOriginalImage();
                case "Grayscale" -> currentImage = imageProcessor.getGrayscaleImage();
                case "Negative" -> currentImage = imageProcessor.getNegativeImage();
                case "B & W" -> currentImage = imageProcessor.getBlackAndWhiteImage();
                case "Mirror" -> currentImage = imageProcessor.getMirroredImage();
                case "Color elimination - Red" -> currentImage = imageProcessor.getEliminateRedImage();
                case "Color elimination - Blue" -> currentImage = imageProcessor.getEliminateBlueImage();
                case "Color elimination - Green" -> currentImage = imageProcessor.getEliminateGreenImage();
                case "Tint" -> currentImage = imageProcessor.getTintImage();
                case "Lighter" -> currentImage = imageProcessor.getLighterImage();
                case "Darker" -> currentImage = imageProcessor.getDarkerImage();
                case "Sepia" -> currentImage = imageProcessor.getSepiaImage();
                case "Noise" -> currentImage = imageProcessor.getNoiseImage();
                case "Vintage" -> currentImage = imageProcessor.getVintageImage();
            }
            imagePanel.setCurrentImage(currentImage);
        });
        return filterComboBox;
    }
}
