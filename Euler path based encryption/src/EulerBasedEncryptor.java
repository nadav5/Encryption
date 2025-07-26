// EulerBasedEncryptor.java
import java.io.*;
import java.util.*;
import javax.swing.*;

public class EulerBasedEncryptor {
    public void encryptFile(File inFile, File outFile, Graph g, int start) throws IOException {
        System.out.println("Encryptor: encryptFile start");
        // 1. פתרון הפאזל
        EulerSolver solver = new EulerSolver();
        List<Integer> circuit = solver.solve(g, start);
        int M = g.getEdges().size();
        // 2. בניית הפרמוטציה P
        int[] P = new int[M + 1];
        for (int i = 0; i < M; i++) {
            P[circuit.get(i)] = i + 1;
        }
        System.out.println("Encryptor: P = " + Arrays.toString(P));

        // 3. כתיבת header
        try (DataOutputStream out = new DataOutputStream(new FileOutputStream(outFile));
             FileInputStream in = new FileInputStream(inFile)) {

            out.writeInt(g.vertexCount());
            out.writeInt(M);
            for (Graph.Edge e : g.getEdges()) {
                out.writeInt(e.u);
                out.writeInt(e.v);
            }
            out.writeInt(start);
            for (int j = 1; j <= M; j++) {
                out.writeInt(P[j]);
            }

            // 4. הצפנת בלוקים
            int bytesPerBlock = (M + 7) / 8;
            byte[] buf = new byte[bytesPerBlock];
            int blockNum = 0;
            while (true) {
                int read = in.read(buf);
                if (read <= 0) break;
                blockNum++;
                byte[] outBuf = permuteBits(buf, P, M);
                System.out.println("Encryptor: block #" + blockNum +
                        " before=" + Arrays.toString(buf) +
                        " after=" + Arrays.toString(outBuf));
                out.write(outBuf, 0, read);
            }
        }

        // 5. הודעת סיום
        JOptionPane.showMessageDialog(null, "Encryption complete!");
        System.out.println("Encryptor: encryptFile done");
    }

    private byte[] permuteBits(byte[] blk, int[] P, int M) {
        int B = blk.length;
        long word = 0, enc = 0;
        // קריאה למילה מלאה
        for (int i = 0; i < B; i++) {
            word |= (long)(blk[i] & 0xFF) << (8 * i);
        }
        int totalBits = B * 8;
        // לולאה על כל הביטים, כולל אלו מחוץ לטווח המוצפן
        for (int i = 0; i < totalBits; i++) {
            int bit = (int)((word >>> i) & 1L);
            int newIndex = (i < M ? P[i + 1] - 1 : i);
            enc |= (long)bit << newIndex;
        }
        // כתיבה חזרה לבייטים
        byte[] out = new byte[B];
        for (int i = 0; i < B; i++) {
            out[i] = (byte)((enc >>> (8 * i)) & 0xFF);
        }
        return out;
    }
}
