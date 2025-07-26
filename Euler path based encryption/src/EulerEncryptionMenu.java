// EulerEncryptionMenu.java
import javax.swing.*;
import java.awt.*;
import java.io.*;

public class EulerEncryptionMenu extends JFrame {
    private final EulerBasedEncryptor encryptor = new EulerBasedEncryptor();
    private final EulerBasedDecryptor decryptor = new EulerBasedDecryptor();

    public EulerEncryptionMenu() {
        super("Euler-Based Encryption");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 1, 10, 10));
        JButton encBtn = new JButton("Encrypt File");
        JButton decBtn = new JButton("Decrypt File");

        encBtn.addActionListener(e -> runEncrypt());
        decBtn.addActionListener(e -> runDecrypt());

        panel.add(encBtn);
        panel.add(decBtn);
        add(panel);
        setVisible(true);
    }

    private void runEncrypt() {
        File file = chooseFile();
        if (file == null) return;
        Graph g = GraphGenerator.randomEulerian(6);
        int start = 0;
        File out = new File(file.getParent(), "euler_enc_" + file.getName());
        try {
            encryptor.encryptFile(file, out, g, start);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void runDecrypt() {
        File file = chooseFile();
        if (file == null) return;
        File out = new File(file.getParent(), "euler_dec_" + file.getName());
        try {
            decryptor.decryptFile(file, out);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        return chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION
                ? chooser.getSelectedFile() : null;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(EulerEncryptionMenu::new);
    }
}
