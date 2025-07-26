import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class SudokuBasedEncryptor {

    public void encryptFile(File inFile, File outFile) throws IOException {
        // 1) יוצרים לוח פרמוטציות של 1..8 (אין 9)
        SudokuGenerator generator = new SudokuGenerator();
        int[][] puzzle = generator.generateRowPermutationBoard();

        System.out.println("=== [Encrypt] לוח פרמוטציה (1..8) לכל שורה ===");
        printPuzzle(puzzle);

        // 2) כותבים את הלוח לקובץ
        try (FileInputStream in = new FileInputStream(inFile);
             FileOutputStream out = new FileOutputStream(outFile)) {

            writePuzzleToFile(out, puzzle);
            System.out.println("[Encrypt] כתבתי 81 תאים ללוח.");

            // 3) הצפנת הקובץ
            int rowIndex = 0;
            int b;
            int countBytes = 0;
            while ((b = in.read()) != -1) {
                countBytes++;
                int originalByte = b & 0xFF;
                int encryptedByte = 0;

                for (int bitPos = 0; bitPos < 8; bitPos++) {
                    int bitVal = (originalByte >> bitPos) & 1;
                    // puzzle[rowIndex][bitPos] הוא בין 1..8, ייחודי לאותה שורה
                    int newIndex = puzzle[rowIndex][bitPos] - 1;
                    encryptedByte |= (bitVal << newIndex);
                }

                out.write(encryptedByte);
                rowIndex = (rowIndex + 1) % 9;
            }
            System.out.println("[Encrypt] סיימתי לכתוב " + countBytes + " בייטים מוצפנים.");
        }
    }

    private void writePuzzleToFile(FileOutputStream out, int[][] puzzle) throws IOException {
        for (int r = 0; r < 9; r++) {
            for (int c = 0; c < 9; c++) {
                out.write(puzzle[r][c]); // 1..8
            }
        }
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
