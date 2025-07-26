import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SudokuBasedDecryptor {

    public void decryptFile(File inFile, File outFile) throws IOException {
        try (FileInputStream in = new FileInputStream(inFile);
             FileOutputStream out = new FileOutputStream(outFile)) {

            // 1) קוראים 81 תאים (1..8)
            int[][] puzzle = readPuzzleFromFile(in);
            System.out.println("=== [Decrypt] לוח פרמוטציה נקרא (1..8) ===");
            printPuzzle(puzzle);

            // 2) מפענחים
            int rowIndex = 0;
            int b;
            int countDec = 0;
            while ((b = in.read()) != -1) {
                countDec++;
                int encryptedByte = b & 0xFF;
                int decryptedByte = 0;

                for (int bitPos = 0; bitPos < 8; bitPos++) {
                    int newIndex = puzzle[rowIndex][bitPos] - 1; // [1..8]-1 => [0..7]
                    int bitVal = (encryptedByte >> newIndex) & 1;
                    decryptedByte |= (bitVal << bitPos);
                }

                out.write(decryptedByte);
                rowIndex = (rowIndex + 1) % 9;
            }
            System.out.println("[Decrypt] סיימתי לפענח " + countDec + " בייטים.");
        }
    }

    private int[][] readPuzzleFromFile(FileInputStream in) throws IOException {
        int[][] puzzle = new int[9][9];
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                puzzle[r][c] = in.read(); // 1..8
            }
        }
        return puzzle;
    }

    private void printPuzzle(int[][] puzzle) {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                System.out.print(puzzle[r][c] + " ");
            }
            System.out.println();
        }
    }
}
