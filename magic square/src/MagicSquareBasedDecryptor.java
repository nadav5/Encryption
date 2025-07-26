// MagicSquareBasedDecryptor.java
import java.io.*;

public class MagicSquareBasedDecryptor {

    public void decryptFile(File inFile, File outFile) throws IOException {
        System.out.println("[DEBUG] decryptFile start");
        try (DataInputStream in = new DataInputStream(new FileInputStream(inFile));
             FileOutputStream fos = new FileOutputStream(outFile)) {

            int n = in.readByte();
            System.out.println("[DEBUG] Read size n=" + n);
            if (n != 4 && n != 5) throw new IOException("Unsupported size: " + n);

            int[][] magic = new int[n][n];
            for (int r = 0; r < n; r++)
                for (int c = 0; c < n; c++)
                    magic[r][c] = in.readByte();
            System.out.println("[DEBUG] Read magic square:");
            printMatrix(magic);

            long origLen = in.readLong();
            System.out.println("[DEBUG] Original length=" + origLen);

            int bBytes = (n*n + 7) / 8;
            int totalBits = bBytes * 8;
            byte[] buf = new byte[bBytes];
            long written = 0;
            int blocks = 0;
            while (written < origLen) {
                int read = in.read(buf);
                if (read == -1) break;
                long enc = 0;
                for (int i = 0; i < bBytes; i++)
                    enc |= ((long)(buf[i] & 0xFF)) << (8 * i);

                long dec = 0;
                for (int i = 0; i < totalBits; i++) {
                    int bit;
                    if (i < n*n) {
                        // find inverse mapping
                        int target = -1;
                        outer: for (int r = 0; r < n; r++)
                            for (int c = 0; c < n; c++)
                                if (magic[r][c] - 1 == i) {
                                    target = r*n + c;
                                    break outer;
                                }
                        bit = (int)((enc >>> i) & 1L);
                        dec |= ((long)bit) << target;
                    } else {
                        bit = (int)((enc >>> i) & 1L);
                        dec |= ((long)bit) << i;
                    }
                }

                byte[] outBlock = new byte[bBytes];
                for (int i = 0; i < bBytes; i++)
                    outBlock[i] = (byte)((dec >> (8 * i)) & 0xFF);
                int toWrite = (int)Math.min(bBytes, origLen - written);
                fos.write(outBlock, 0, toWrite);
                written += toWrite;
                if (++blocks % 500 == 0)
                    System.out.println("[DEBUG] Decrypted blocks: " + blocks);
            }
            System.out.println("[DEBUG] decryptFile done, written=" + written + " bytes");
        }
    }

    private void printMatrix(int[][] mat) {
        for (int[] row : mat) {
            for (int v : row) System.out.printf("%3d", v);
            System.out.println();
        }
    }
}
