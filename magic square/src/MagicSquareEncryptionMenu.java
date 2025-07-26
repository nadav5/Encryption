// MagicSquareEncryptionMenu.java
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;

public class MagicSquareEncryptionMenu extends JFrame {
    private final MagicSquareBasedEncryptor encryptor = new MagicSquareBasedEncryptor();
    private final MagicSquareBasedDecryptor decryptor = new MagicSquareBasedDecryptor();

    public MagicSquareEncryptionMenu() {
        super("Magic Square Encryption");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        panel.setBackground(new Color(230, 240, 255));

        JButton encryptBtn = new JButton("Encrypt File");
        encryptBtn.setFont(new Font("Arial", Font.BOLD, 16));
        encryptBtn.addActionListener(e -> encryptFile());

        JButton decryptBtn = new JButton("Decrypt File");
        decryptBtn.setFont(new Font("Arial", Font.BOLD, 16));
        decryptBtn.addActionListener(e -> decryptFile());

        panel.add(encryptBtn);
        panel.add(decryptBtn);
        add(panel);
        setVisible(true);
    }

    private void encryptFile() {
        File file = chooseFile("Select file to encrypt");
        if (file == null) return;
        String[] options = {"Even", "Odd"};
        int choice = JOptionPane.showOptionDialog(
                this, "Choose magic square type:", "Square Type",
                JOptionPane.DEFAULT_OPTION, JOptionPane.QUESTION_MESSAGE,
                null, options, options[0]
        );
        if (choice < 0) return;
        int n = (choice == 0) ? 4 : 5;
        File outFile = new File(file.getParent(), "magic_encrypted_" + file.getName());
        try {
            encryptor.encryptFile(file, outFile, n);
            JOptionPane.showMessageDialog(this, "Encryption complete!\nOutput: " + outFile.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Encryption failed: " + ex.getMessage());
        }
    }

    private void decryptFile() {
        File file = chooseFile("Select file to decrypt");
        if (file == null) return;
        File outFile = new File(file.getParent(), "magic_decrypted_" + file.getName());
        try {
            decryptor.decryptFile(file, outFile);
            JOptionPane.showMessageDialog(this, "Decryption complete!\nOutput: " + outFile.getAbsolutePath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Decryption failed: " + ex.getMessage());
        }
    }

    private File chooseFile(String title) {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle(title);
        return chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION
                ? chooser.getSelectedFile() : null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(MagicSquareEncryptionMenu::new);
    }
}
