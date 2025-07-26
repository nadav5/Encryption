// MagicSquareBasedEncryptor.java
import java.io.*;
import java.util.Random;

public class MagicSquareBasedEncryptor {

    public int[][] generateMagicSquare(int n) {
        System.out.println("[DEBUG] Generating magic square of size " + n);
        if (n % 2 == 1) {
            return generateOddMagicSquare(n);
        } else if (n % 4 == 0) {
            return generateDoublyEvenMagicSquare(n);
        } else {
            throw new IllegalArgumentException("Unsupported size: " + n);
        }
    }

    private int[][] generateOddMagicSquare(int n) {
        System.out.println("[DEBUG] generateOddMagicSquare(" + n + ")");
        int[][] magic = new int[n][n];
        int num = 1, i = 0, j = n / 2;
        while (num <= n * n) {
            magic[i][j] = num++;
            int ni = (i - 1 + n) % n, nj = (j + 1) % n;
            if (magic[ni][nj] != 0) {
                i = (i + 1) % n;
            } else {
                i = ni; j = nj;
            }
        }
        return magic;
    }

    private int[][] generateDoublyEvenMagicSquare(int n) {
        System.out.println("[DEBUG] generateDoublyEvenMagicSquare(" + n + ")");
        int[][] m = new int[n][n];
        int cnt = 1;
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                m[r][c] = cnt++;
        for (int r = 0; r < n; r++)
            for (int c = 0; c < n; c++)
                if ((r % 4 == c % 4) || ((r + c) % 4 == 3))
                    m[r][c] = n*n + 1 - m[r][c];
        return m;
    }

    private int blockBytes(int n) {
        int bits = n * n;
        return (bits + 7) / 8;
    }

    public void encryptFile(File inFile, File outFile, int n) throws IOException {
        System.out.println("[DEBUG] encryptFile start, n=" + n);
        long origLen = inFile.length();
        int bBytes = blockBytes(n);
        int totalBits = bBytes * 8;

        int[][] magic = generateMagicSquare(n);
        System.out.println("[DEBUG] Magic square mapping:");
        printMatrix(magic);

        try (FileInputStream fis = new FileInputStream(inFile);
             DataOutputStream out = new DataOutputStream(new FileOutputStream(outFile))) {

            // Header: n + full magic square + original length
            out.writeByte(n);
            for (int r = 0; r < n; r++)
                for (int c = 0; c < n; c++)
                    out.writeByte(magic[r][c]);
            out.writeLong(origLen);
            System.out.println("[DEBUG] Wrote header (n, mapping, origLen=" + origLen + ")");

            byte[] buf = new byte[bBytes];
            int read, blocks = 0;
            while ((read = fis.read(buf)) != -1) {
                if (read < bBytes)
                    for (int i = read; i < bBytes; i++) buf[i] = 0;
                long word = 0;
                for (int i = 0; i < bBytes; i++)
                    word |= ((long)(buf[i] & 0xFF)) << (8 * i);

                long enc = 0;
                // Permute only first n*n bits, leave the rest in place
                for (int i = 0; i < totalBits; i++) {
                    int bit = (int)((word >>> i) & 1L);
                    int newIndex;
                    if (i < n*n) {
                        int r = i / n, c = i % n;
                        newIndex = magic[r][c] - 1;
                    } else {
                        newIndex = i;
                    }
                    enc |= ((long)bit) << newIndex;
                }

                for (int i = 0; i < bBytes; i++)
                    out.writeByte((int)((enc >> (8 * i)) & 0xFF));
                if (++blocks % 500 == 0)
                    System.out.println("[DEBUG] Encrypted blocks: " + blocks);
            }
            System.out.println("[DEBUG] encryptFile done, total blocks=" + blocks);
        }
    }

    private void printMatrix(int[][] mat) {
        for (int[] row : mat) {
            for (int v : row) System.out.printf("%3d", v);
            System.out.println();
        }
    }
}
