// EulerBasedDecryptor.java
import java.io.*;
import java.util.*;
import javax.swing.*;

public class EulerBasedDecryptor {
    public void decryptFile(File inFile, File outFile) throws IOException {
        System.out.println("Decryptor: decryptFile start");
        try (DataInputStream in = new DataInputStream(new FileInputStream(inFile));
             FileOutputStream out = new FileOutputStream(outFile)) {

            // 1. קריאת header
            int V = in.readInt();
            int M = in.readInt();
            System.out.println("Decryptor: V=" + V + ", M=" + M);
            Graph g = new Graph(V);
            for (int i = 0; i < M; i++) {
                int u = in.readInt(), v = in.readInt();
                g.addEdge(u, v);
            }
            int start = in.readInt();
            int[] P = new int[M + 1];
            for (int j = 1; j <= M; j++) {
                P[j] = in.readInt();
            }
            System.out.println("Decryptor: P = " + Arrays.toString(P));

            // 2. חישוב inv
            int[] inv = new int[M + 1];
            for (int j = 1; j <= M; j++) {
                inv[P[j]] = j;
            }
            System.out.println("Decryptor: inv = " + Arrays.toString(inv));

            // 3. פענוח בלוקים
            int bytesPerBlock = (M + 7) / 8;
            byte[] buf = new byte[bytesPerBlock];
            int blockNum = 0;
            while (true) {
                int read = in.read(buf);
                if (read <= 0) break;
                blockNum++;
                byte[] dec = invertBits(buf, inv, M);
                System.out.println("Decryptor: block #" + blockNum +
                        " encrypted=" + Arrays.toString(buf) +
                        " decrypted=" + Arrays.toString(dec));
                out.write(dec, 0, read);
            }
        }

        // 4. הודעת סיום
        JOptionPane.showMessageDialog(null, "Decryption complete!");
        System.out.println("Decryptor: decryptFile done");
    }

    private byte[] invertBits(byte[] blk, int[] inv, int M) {
        int B = blk.length;
        long enc = 0, dec = 0;
        // קריאה למילה מוצפנת מלאה
        for (int i = 0; i < B; i++) {
            enc |= (long)(blk[i] & 0xFF) << (8 * i);
        }
        int totalBits = B * 8;
        // לולאה על כל הביטים, כולל מחוץ ל-M
        for (int i = 0; i < totalBits; i++) {
            int bit = (int)((enc >>> i) & 1L);
            int newIndex = (i < M ? inv[i + 1] - 1 : i);
            dec |= (long)bit << newIndex;
        }
        // כתיבה חזרה לבייטים
        byte[] out = new byte[B];
        for (int i = 0; i < B; i++) {
            out[i] = (byte)((dec >>> (8 * i)) & 0xFF);
        }
        return out;
    }
}
