import java.awt.image.BufferedImage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        int modeSelection = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the mode:");
        System.out.println("1. GUI Mode");
        System.out.println("2. Main Method Mode");
        modeSelection = scanner.nextInt();
        if (modeSelection == 1) {
            GUImethod(args);
        } else if (modeSelection == 2) {
            mainMethod(args);
        } else {
            System.out.println("Invalid mode selection");
        }

    }

    public static void mainMethod(String[] args) {
        String imagePath;
        String cryptedImagePath;
        String logFile = "log.txt";
        String rgbLogFile = "rgbLog.txt";
        int modeSelection = 0;
        Scanner scanner = new Scanner(System.in);
        System.out.println("Select the mode: 1-Encrypt 2-Decrypt");
        modeSelection = scanner.nextInt();

        // Load the image and save the RGB values to a log file
        ImageProcessor.saveMessageToFile("Start of the program", logFile, true);

        // ImageProcessor.saveMessageToFile(" ", logFile, true);

        if (modeSelection == 1) {
            System.out.println("Enter the path of the image to encrypt:");
            imagePath = scanner.next();
            ImageProcessor.saveMessageToFile("Image path: " + imagePath, logFile, true);
            // Get crypt image path
            System.out.println("Enter the path of the crypted image:");
            cryptedImagePath = scanner.next();
            // get the rgb values from the image
            int[][][] rgbArray = ImageProcessor.getRgbValuesFromImageFile(imagePath);
            if (rgbArray != null) {
                // Save the RGB values to a log file
                ImageProcessor.saveRgbValuesToFile(imagePath, rgbLogFile);
                for (int i = 0; i < rgbArray.length; i++) {
                    for (int j = 0; j < rgbArray[0].length; j++) {
                        ImageProcessor.saveMessageToFile(
                                "RGB values: " + rgbArray[i][j][0] + " " + rgbArray[i][j][1] + " " + rgbArray[i][j][2],
                                rgbLogFile, false);
                    }
                    ImageProcessor.saveMessageToFile("", rgbLogFile, true);
                }
                System.out.println("RGB values have been successfully saved to rgbLog.txt.");
                ImageProcessor.saveMessageToFile("RGB values have been successfully saved to rgbLog.txt.", logFile,
                        true);
            }

            // Encrypt the message
            int[][][] encryptedRgbArray = Crypt.encrypt(rgbArray);
            if (encryptedRgbArray != null) {
                // Generate the encrypted image
                Crypt.generateCryptedImage(encryptedRgbArray, cryptedImagePath);
                System.out.println("Message has been successfully encrypted and the image saved as cryptedImage.png.");
                ImageProcessor.saveMessageToFile(
                        "Message has been successfully encrypted and the image saved as cryptedImage.png.", logFile,
                        true);

            }
        }

        else if (modeSelection == 2) {
            System.out.println("Enter the path of the image to decrypt:");
            cryptedImagePath = scanner.next();
            // Decrypt the message
            int[][][] cryptedRgbArray = ImageProcessor.getRgbValuesFromImageFile(cryptedImagePath);
            char[] message = Crypt.deCrypt(cryptedRgbArray);
            System.out.println("Decrypted message: " + String.valueOf(message));
            ImageProcessor.saveMessageToFile("Decrypted message: " + String.valueOf(message), logFile, true);
            for (int i = 0; i < cryptedRgbArray.length; i++) {
                for (int j = 0; j < cryptedRgbArray[0].length; j++) {
                    ImageProcessor.saveMessageToFile("Crypted RGB values: " + cryptedRgbArray[i][j][0] + " "
                            + cryptedRgbArray[i][j][1] + " " + cryptedRgbArray[i][j][2], rgbLogFile, false);
                }
                ImageProcessor.saveMessageToFile("", rgbLogFile, true);
            }
        }

        else {
            System.out.println("Invalid mode selection");
        }

    }

    public static void GUImethod(String[] args) {
        boolean isRunning = true;
        while (isRunning) {
            // Define the paths for the image and the log file
            String logFile = "log.txt";
            String rgbLogFile = "rgbLog.txt";
            int[] modeSelection = { 0 }; // Use an array to make it effectively final for inner classes
            Scanner scanner = new Scanner(System.in);
            TextInputWindow window = new TextInputWindow();
            TextDisplayWindow messageWindow = new TextDisplayWindow("Program Starts", "");
            // Create the buttons
            JButton encryptButton = new JButton("Encrypt");
            JButton decryptButton = new JButton("Decrypt");
            JButton exitButton = new JButton("Exit");

            // Add action listeners to the buttons
            encryptButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle encrypt button click
                    System.out.println("Encrypt button clicked");
                    ImageProcessor.saveMessageToFile("Encrypt button clicked", logFile, true);
                    modeSelection[0] = 1;
                    // Add your encryption logic here
                }
            });

            decryptButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle decrypt button click
                    System.out.println("Decrypt button clicked");
                    ImageProcessor.saveMessageToFile("Decrypt button clicked", logFile, true);
                    modeSelection[0] = 2;
                    // Add your decryption logic here
                }
            });

            exitButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Handle exit button click
                    System.out.println("Exit button clicked");
                    System.exit(0); // Exit the application

                }
            });

            // Create a panel for the buttons
            JPanel buttonPanel = new JPanel();
            buttonPanel.add(encryptButton);
            buttonPanel.add(decryptButton);
            buttonPanel.add(exitButton);

            // Create the frame and image panel
            JFrame frame = new JFrame();
            frame.setVisible(false);// Set the frame to be invisible initially
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(300, 100); // Set the size of the frame
            ImageProcessor.saveMessageToFile("Window opened", logFile, true);
            ImageProcessor.saveMessageToFile("Window size: 300x100", logFile, true);

            // Add the button panel to the frame
            frame.setLayout(new BorderLayout());
            frame.add(buttonPanel, BorderLayout.SOUTH);

            // Make the frame visible
            frame.setVisible(true);

            // The rest of your logic will go here
            // For demonstration, we can just print the mode selection
            while (modeSelection[0] == 0) {
                // wait for a button press
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (modeSelection[0] == 1) {
                System.out.println("Enter the path of the image to encrypt:");
                String firstEnter = "Enter the path of the image to encrypt:";
                String imagePath = window.display(firstEnter);
                ImageProcessor.saveMessageToFile("Encrypting Image at path: " + imagePath, logFile, true);
                ImageDisplay imagePanel = new ImageDisplay(imagePath);
                frame.add(imagePanel, BorderLayout.CENTER);
                frame.setSize(ImageProcessor.getWidth(imagePath), ImageProcessor.getHeight(imagePath));
                frame.setVisible(true);

                String secondString = "Enter the path of the crypted image:";
                String cryptedImagePath = window.display(secondString);
                frame.setVisible(false);

                int[][][] rgbArray = ImageProcessor.getRgbValuesFromImageFile(imagePath);

                int[][][] encryptedRgbArray = Crypt.encrypt(rgbArray);
                if (encryptedRgbArray != null) {
                    Crypt.generateCryptedImage(encryptedRgbArray, cryptedImagePath);
                    System.out.println(
                            "Message has been successfully encrypted and the image saved as cryptedImage.png.");
                    ImageProcessor.saveMessageToFile(
                            "Message has been successfully encrypted and the image saved as cryptedImage.png.", logFile,
                            true);
                    imagePanel = new ImageDisplay(cryptedImagePath);
                    frame.add(imagePanel, BorderLayout.CENTER);
                    frame.setSize(ImageProcessor.getWidth(cryptedImagePath),
                            ImageProcessor.getHeight(cryptedImagePath));
                    frame.setVisible(true);
                }

            } else if (modeSelection[0] == 2) {
                String secondString = "Enter the path of the crypted image:";
                String cryptedImagePath = window.display(secondString);
                // System.out.println("Enter the path of the image to decrypt:");
                // String cryptedImagePath = scanner.next();
                int[][][] cryptedRgbArray = ImageProcessor.getRgbValuesFromImageFile(cryptedImagePath);
                char[] message = Crypt.deCrypt(cryptedRgbArray);
                System.out.println("Decrypted message: " + String.valueOf(message));
                messageWindow.updateMessage("Decrypted Message: " + String.valueOf(message));
                // messageWindow.updateMessage("Decrypted Message: " + toString(message));
                messageWindow.display();
            } else {
                System.out.println("Invalid mode selection");
            }

        }
    }
}

class ImageProcessor {
    public static void saveRgbValuesToFile(String imagePath, String logFilePath) {
        File imageFile = new File(imagePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            // exit the program
            System.exit(0);
            return;
        }

        if (image != null) {
            int width = image.getWidth();
            int height = image.getHeight();
            int[][][] rgbArray = new int[height][width][3];

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = image.getRGB(x, y);
                    int red = (pixel >> 16) & 0xff;
                    int green = (pixel >> 8) & 0xff;
                    int blue = pixel & 0xff;
                    rgbArray[y][x][0] = red;
                    rgbArray[y][x][1] = green;
                    rgbArray[y][x][2] = blue;
                }
            }

            try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath))) {
                for (int y = 0; y < height; y++) {
                    writer.print("{ ");
                    for (int x = 0; x < width; x++) {
                        writer.printf("(%d,%d,%d) ", rgbArray[y][x][0], rgbArray[y][x][1], rgbArray[y][x][2]);
                    }
                    writer.print(" }");
                    writer.println();
                }
            } catch (IOException e) {

                e.printStackTrace();
                // exit the program
                System.exit(0);
            }
        }
    }

    public static int getWidth(String imagePath) {
        File imageFile = new File(imagePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);

            return 0;
        }
        return image.getWidth();
    }

    public static int getHeight(String imagePath) {
        File imageFile = new File(imagePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return 0;
        }
        return image.getHeight();
    }

    // save any String to the log file and always new line method
    // add time and date after the message
    public static void saveMessageToFile(String message, String logFilePath, boolean newLine) {
        if (newLine) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
                writer.println("\n" + message + " " + java.time.LocalTime.now() + " " + java.time.LocalDate.now());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
                writer.print(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static int[][][] getRgbValuesFromImageFile(String imagePath) {
        File imageFile = new File(imagePath);
        BufferedImage image = null;
        try {
            image = ImageIO.read(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        int[][][] rgbArray = new int[height][width][3];

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int pixel = image.getRGB(x, y);
                int red = (pixel >> 16) & 0xff;
                int green = (pixel >> 8) & 0xff;
                int blue = pixel & 0xff;
                rgbArray[y][x][0] = red;
                rgbArray[y][x][1] = green;
                rgbArray[y][x][2] = blue;
            }
        }

        return rgbArray;
    }
}

class Crypt {
    public static char[] getMessage() {
        TextInputWindow window = new TextInputWindow();
        String labelText = "Please enter the message:";
        String message = window.display(labelText);
        if (message.length() > 255) {
            System.out.println("Message is too long");
            // save the message to the log file
            ImageProcessor.saveMessageToFile("Got Error: Message is too long", "log.txt", true);
            return null;
        }
        char[] messageArray = new char[message.length()];
        for (int i = 0; i < message.length(); i++) {
            messageArray[i] = message.charAt(i);
        }
        // save the messageArray to the log file
        ImageProcessor.saveMessageToFile("Message is: " + String.valueOf(messageArray), "log.txt", true);
        return messageArray;
    }

    public static int[][][] encrypt(int[][][] rgbArray) {
        char[] messageArray = getMessage();
        if (messageArray == null)
            return null;

        int height = rgbArray.length;
        int width = rgbArray[0].length;
        int[][][] cryptedArray = new int[height][width][3];
        // copy rgbArray to cryptedArray
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                cryptedArray[y][x][0] = rgbArray[y][x][0];
                cryptedArray[y][x][1] = rgbArray[y][x][1];
                cryptedArray[y][x][2] = rgbArray[y][x][2];
            }
        }
        int messageLength = messageArray.length;
        cryptedArray[0][0][0] = messageLength; // message length
        if (messageLength >= 255) {
            System.out.println("Message is too long");
            ImageProcessor.saveMessageToFile("Got Error: Message is too long", "log.txt", true);
            return null;
        }

        int randomBlueValue = (int) (Math.random() * 254 + 1); // Select a random blue value between 1 and 255
        while (messageLength == randomBlueValue) { // check if the random blue value is equal to the message length
            randomBlueValue = (int) (Math.random() * 254 + 1);
        }
        // System.out.println("Random blue value could be the : " + randomBlueValue);

        for (int i = 0; i < height; i++) {
            for (int k = 0; k < width; k++) {
                if (rgbArray[i][k][2] == randomBlueValue && rgbArray[i][k][2] != 255) {
                    // As you see, the blue value of this pixel and your target blue value are the
                    // same. Therefore, you must change the blue value of this pixel by increasing
                    // the value by 1.
                    cryptedArray[i][k][2]++;
                }
                if (rgbArray[i][k][2] == randomBlueValue && rgbArray[i][k][2] == 255) {
                    cryptedArray[i][k][2]--;
                }
            }
        }

        cryptedArray[0][0][1] = randomBlueValue; // set random blue value
        System.out.println("Random blue value is : " + randomBlueValue);
        // save the random blue value to the log file
        ImageProcessor.saveMessageToFile("Random blue value: " + randomBlueValue, "log.txt", true);

        // randomly choose pixel from the image and save the x and y values of the pixel
        // to the 2d array and each pixel has to be different from each other
        int[][] randomPixelArray = new int[messageLength][2];
        for (int i = 0; i < messageLength; i++) {
            randomPixelArray[i][0] = (int) (Math.random() * (width - 1) + 1);
            randomPixelArray[i][1] = (int) (Math.random() * (height - 1) + 1);
            for (int k = 0; i > 0 && k < i; k++) {
                if (randomPixelArray[i][0] == randomPixelArray[k][0]
                        && randomPixelArray[i][1] == randomPixelArray[k][1]) {
                    i--;
                    break;
                }
            }
        }

        int[] randomValueArray = new int[messageLength];
        for (int i = 0; i < messageLength; i++) {
            // generate random numbers between 1 and 255 and save to randomValueArray array
            randomValueArray[i] = (int) (Math.random() * 254 + 1);
            for (int k = 0; k < i; k++) {
                if (randomValueArray[i] == randomValueArray[k]) {
                    i--;
                    break;
                }
            }
        }

        // Sort the randomValueArray array
        int[] SortedRandomValueArray = sortArray(randomValueArray, messageLength);
        // print random value array to the log file
        ImageProcessor.saveMessageToFile("Random Value Array: ", "log.txt", false);
        for (int i = 0; i < messageLength; i++) {
            ImageProcessor.saveMessageToFile(SortedRandomValueArray[i] + " ", "log.txt", false);
        }
        ImageProcessor.saveMessageToFile("", "log.txt", true);
        // print message array to the log file
        ImageProcessor.saveMessageToFile("Message Array: ", "log.txt", false);
        for (int i = 0; i < messageLength; i++) {
            ImageProcessor.saveMessageToFile(messageArray[i] + " ", "log.txt", false);
        }
        ImageProcessor.saveMessageToFile("", "log.txt", true);
        // Hide the message
        for (int i = 0; i < messageLength; i++) {
            int x = randomPixelArray[i][0];
            int y = randomPixelArray[i][1];
            cryptedArray[y][x][0] = SortedRandomValueArray[i];
            cryptedArray[y][x][1] = (int) messageArray[i];
            cryptedArray[y][x][2] = randomBlueValue;
        }
        /*
         * // print random value array
         * for (int i = 0; i < messageLength; i++) {
         * System.out.print(SortedRandomValueArray[i] + " ");
         * }
         * System.out.print("\n");
         * // print message array
         * for (int i = 0; i < messageLength; i++) {
         * System.out.print(messageArray[i] + " ");
         * }
         * System.out.print("\n");
         */
        return cryptedArray;
    }

    public static int[] sortArray(int[] array, int length) {
        int[] sortedArray = Arrays.copyOf(array, length);
        Arrays.sort(sortedArray);
        ImageProcessor.saveMessageToFile("Sorted Array: ", "log.txt", false);
        for (int i = 0; i < length; i++) {
            ImageProcessor.saveMessageToFile(sortedArray[i] + " ", "log.txt", false);
        }
        ImageProcessor.saveMessageToFile("", "log.txt", true);
        return sortedArray;

    }

    public static void generateCryptedImage(int[][][] rgbArray, String outputPath) {
        int height = rgbArray.length;
        int width = rgbArray[0].length;
        BufferedImage cryptedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = (rgbArray[y][x][0] << 16) | (rgbArray[y][x][1] << 8) | rgbArray[y][x][2];
                cryptedImage.setRGB(x, y, rgb);
            }
        }
        File cryptedImageFile = new File(outputPath);
        try {
            ImageIO.write(cryptedImage, "png", cryptedImageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageProcessor.saveMessageToFile("Crypted image created: ", "log.txt", true);
        ImageProcessor.saveMessageToFile("Crypted image path: " + outputPath, "log.txt", true);
    }

    // decryption method
    public static char[] deCrypt(int[][][] rgbArray) {
        int messageLength = rgbArray[0][0][0];
        int randomBlueValue = rgbArray[0][0][1];
        char[] messageArray = new char[messageLength];
        int[][] receivedValues = new int[messageLength][2];// [0] is randomValue, [1] is message

        int messageIndex = 0;
        // receive randomValues and message from the image
        for (int i = 0; i < rgbArray.length; i++) {
            for (int j = 0; j < rgbArray[0].length; j++) {
                if (rgbArray[i][j][2] == randomBlueValue && messageIndex < messageLength) {
                    receivedValues[messageIndex][0] = rgbArray[i][j][0];
                    receivedValues[messageIndex][1] = rgbArray[i][j][1];
                    messageIndex++;
                }
            }
        }

        int[] bufferArray = new int[messageLength];
        for (int i = 0; i < messageLength; i++) {
            bufferArray[i] = receivedValues[i][0];
        }
        int[] SortedBufferArray = sortArray(bufferArray, messageLength);

        for (int i = 0; i < messageLength; i++) {
            for (int j = 0; j < messageLength; j++) {
                if (receivedValues[j][0] == SortedBufferArray[i]) {
                    messageArray[i] = (char) receivedValues[j][1];
                    break;
                }
            }
        }
        // print random value array
        for (int i = 0; i < messageLength; i++) {
            System.out.print(SortedBufferArray[i] + " ");
        }
        System.out.print("\n");
        // print message array
        for (int i = 0; i < messageLength; i++) {
            System.out.print(messageArray[i] + " ");
        }
        System.out.print("\n");
        // print random value array to the log file
        ImageProcessor.saveMessageToFile("Sorted Buffer Array: ", "log.txt", false);
        for (int i = 0; i < messageLength; i++) {
            ImageProcessor.saveMessageToFile(SortedBufferArray[i] + " ", "log.txt", false);
        }
        ImageProcessor.saveMessageToFile("", "log.txt", true);
        // print message array to the log file
        ImageProcessor.saveMessageToFile("Decrypted Message: ", "log.txt", false);
        for (int i = 0; i < messageLength; i++) {
            ImageProcessor.saveMessageToFile(messageArray[i] + " ", "log.txt", false);
        }
        ImageProcessor.saveMessageToFile("", "log.txt", true);

        return messageArray;
    }
}

class ImageDisplay extends JPanel {
    private BufferedImage image;

    public ImageDisplay(String imagePath) {
        try {
            image = ImageIO.read(new File(imagePath));
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (image != null) {
            g.drawImage(image, 0, 0, this);
        }
    }
}

class TextInputWindow {
    private JFrame frame;
    private JTextField textField;
    private String inputText;

    public TextInputWindow() {
        frame = new JFrame();
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 250);
        frame.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));

        textField = new JTextField();
        panel.add(textField);

        JButton okButton = new JButton("OK");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inputText = textField.getText();
                frame.dispose();
            }
        });
        panel.add(okButton);

        frame.add(panel, BorderLayout.CENTER);
    }

    public String display(String labelText) {
        frame.setTitle(labelText);
        frame.setVisible(true);

        // Wait for the window to be disposed
        while (frame.isDisplayable()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        return inputText;
    }
}

public class TextDisplayWindow {
    private JFrame frame;
    private JLabel label;

    public TextDisplayWindow(String title, String message) {
        // Create the frame
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(600, 200);
        frame.setLayout(new BorderLayout());

        // Create the label with the message
        label = new JLabel(message, SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.PLAIN, 16));

        // Add the label to the frame
        frame.add(label, BorderLayout.CENTER);
    }

    // Method to show the window
    public void display() {
        frame.setVisible(true);
    }

    // Method to update the message
    public void updateMessage(String newMessage) {
        label.setText(newMessage);
    }
}