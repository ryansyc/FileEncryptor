import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;

/**
 * Aplikasi Enkripsi dan Dekripsi File dengan antarmuka pengguna menggunakan Swing.
 * Memanfaatkan Caesar Cipher untuk mengenkripsi dan mendekripsi file.
 */
public class FileEncryptor extends JFrame {

    private JTextField inputFilePathField;
    private JButton selectInputButton;
    private JButton encryptButton;
    private JButton decryptButton;
    private JTextArea resultArea;
    private int key;  // Kunci pergeseran otomatis untuk Caesar Cipher

    /**
     * Konstruktor untuk membangun antarmuka pengguna dan menambahkan komponen UI.
     */
    public FileEncryptor() {
        setTitle("Alat Enkripsi dan Dekripsi File");
        setSize(500, 300);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 

        JPanel inputPanel = new JPanel(new GridLayout(1, 3, 5, 5));
        inputPanel.add(new JLabel("Path File Input:"));
        inputFilePathField = new JTextField();
        inputFilePathField.setEditable(false); 
        inputPanel.add(inputFilePathField);

        selectInputButton = new JButton("...");
        inputPanel.add(selectInputButton);

        encryptButton = new JButton("Enkripsi");
        decryptButton = new JButton("Dekripsi");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(encryptButton);
        buttonPanel.add(decryptButton);

        resultArea = new JTextArea(5, 30);
        resultArea.setEditable(false); 
        JScrollPane scrollPane = new JScrollPane(resultArea);

        setLayout(new BorderLayout());
        add(inputPanel, BorderLayout.NORTH);
        add(buttonPanel, BorderLayout.CENTER);
        add(scrollPane, BorderLayout.SOUTH);

        selectInputButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    inputFilePathField.setText(selectedFile.getAbsolutePath());
                }
            }
        });

        encryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile(true); 
            }
        });

        decryptButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                processFile(false); 
            }
        });
    }

    /**
     * Memproses file yang dipilih dengan operasi enkripsi atau dekripsi.
     * 
     * @param isEncrypt true untuk enkripsi, false untuk dekripsi
     */
    private void processFile(boolean isEncrypt) {
        String inputFilePath = inputFilePathField.getText();
        if (inputFilePath.isEmpty()) {
            resultArea.setText("Silakan pilih file input terlebih dahulu.");
            return;
        }

        if (isEncrypt) {
            key = new Random().nextInt(25) + 1; 
        }

        String outputFilePath = generateOutputFilePath(inputFilePath, isEncrypt);

        try {
            int shiftKey = isEncrypt ? key : -key;
            processFile(inputFilePath, outputFilePath, shiftKey);
            resultArea.setText("Proses berhasil!\nFile tersimpan di: " + outputFilePath + "\nKunci Pergeseran: " + key);
        } catch (IOException e) {
            resultArea.setText("Terjadi kesalahan saat memproses file: " + e.getMessage());
        }
    }

    /**
     * Metode untuk mengenkripsi atau mendekripsi file menggunakan Caesar Cipher.
     * 
     * @param inputFilePath  Path file input
     * @param outputFilePath Path file output
     * @param shiftKey       Kunci pergeseran untuk enkripsi atau dekripsi
     * @throws IOException Jika terjadi kesalahan saat membaca atau menulis file
     */
    private void processFile(String inputFilePath, String outputFilePath, int shiftKey) throws IOException {
        try (FileInputStream fis = new FileInputStream(inputFilePath);
             FileOutputStream fos = new FileOutputStream(outputFilePath)) {

            int data;
            while ((data = fis.read()) != -1) {
                byte shiftedData = (byte) (data + shiftKey);
                fos.write(shiftedData);
            }
        }
    }

    /**
     * Menghasilkan path untuk file output berdasarkan file input dan operasi enkripsi/dekripsi.
     * 
     * @param inputFilePath Path file input
     * @param isEncrypt     true jika enkripsi, false jika dekripsi
     * @return Path file output yang dihasilkan
     */
    private String generateOutputFilePath(String inputFilePath, boolean isEncrypt) {
        File inputFile = new File(inputFilePath);
        String fileName = inputFile.getName();
        String fileDirectory = inputFile.getParent();
        String suffix = isEncrypt ? "_encrypted" : "_decrypted"; 
        String outputFileName = fileName.replaceFirst("(\\.[^.]+)?$", suffix + "$1");
        return new File(fileDirectory, outputFileName).getAbsolutePath(); 
    }

    /**
     * Main method untuk menjalankan aplikasi.
     * 
     * @param args Parameter command line (tidak digunakan)
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FileEncryptor().setVisible(true);
            }
        });
    }
}
