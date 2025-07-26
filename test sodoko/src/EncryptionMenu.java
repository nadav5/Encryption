import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class EncryptionMenu extends JFrame {

    private SudokuBasedEncryptor encryptor = new SudokuBasedEncryptor();
    private SudokuBasedDecryptor decryptor = new SudokuBasedDecryptor();

    public EncryptionMenu() {
        setTitle("Sudoku Based Encryption - RowPermutation (No 9)");
        createMainMenu();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(500, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void createMainMenu() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 1, 10, 10));
        panel.setBackground(new Color(230, 240, 255));

        JLabel title = new JLabel("RowPermutation - Main Menu", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));
        panel.add(title);

        JButton encryptButton = new JButton("Encrypt File (Choose...)");
        encryptButton.setFont(new Font("Arial", Font.BOLD, 16));
        encryptButton.addActionListener(e -> encryptFile());
        panel.add(encryptButton);

        JButton decryptButton = new JButton("Decrypt File (Choose...)");
        decryptButton.setFont(new Font("Arial", Font.BOLD, 16));
        decryptButton.addActionListener(e -> decryptFile());
        panel.add(decryptButton);

        getContentPane().removeAll();
        add(panel);
        revalidate();
        repaint();
    }

    private void encryptFile() {
        File file = chooseFile();
        if (file != null) {
            File outFile = new File(file.getParentFile(), "rowperm_encrypted_" + file.getName());
            try {
                encryptor.encryptFile(file, outFile);
                JOptionPane.showMessageDialog(this, "Encryption done!\nOutput file: " + outFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Encryption failed: " + e.getMessage());
            }
        }
    }

    private void decryptFile() {
        File file = chooseFile();
        if (file != null) {
            File outFile = new File(file.getParentFile(), "rowperm_decrypted_" + file.getName());
            try {
                decryptor.decryptFile(file, outFile);
                JOptionPane.showMessageDialog(this, "Decryption done!\nOutput file: " + outFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Decryption failed: " + e.getMessage());
            }
        }
    }

    private File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        return (result == JFileChooser.APPROVE_OPTION) ? chooser.getSelectedFile() : null;
    }

    public static void main(String[] args) {
        new EncryptionMenu();
    }
}
