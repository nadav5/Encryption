import java.util.*;

public class SudokuGenerator {
    private final int SIZE = 9;
    private int[][] board = new int[SIZE][SIZE];

    /**
     * במקום למלא סודוקו אמיתי, נייצר 9 שורות, בכל שורה פרמוטציה אקראית של הספרות 1..8.
     * העמודה התשיעית (index=8) גם נכניס משהו 1..8 (או 0..8), לא משנה – רק לא 9.
     */
    public int[][] generateRowPermutationBoard() {
        Random rand = new Random();
        for (int r = 0; r < SIZE; r++) {
            // צור רשימת 1..8 מעורבבת
            List<Integer> rowVals = new ArrayList<>();
            for (int i = 1; i <= 8; i++) {
                rowVals.add(i);
            }
            Collections.shuffle(rowVals, rand);

            // שים אותם בשורה
            for (int c = 0; c < 8; c++) {
                board[r][c] = rowVals.get(c);
            }
            // עמודה תשיעית – נקבע גם ערך כלשהו 1..8
            board[r][8] = rand.nextInt(8) + 1;
        }
        return deepCopyBoard(board);
    }

    /**
     * החזרת עותק של הלוח הנוכחי (אם תרצה).
     */
    public int[][] getBoard() {
        return board;
    }

    /**
     * הדפסת הלוח
     */
    public void printBoard() {
        for (int r = 0; r < SIZE; r++) {
            for (int c = 0; c < SIZE; c++) {
                System.out.print(board[r][c] + " ");
            }
            System.out.println();
        }
    }

    /**
     * יצירת deep copy
     */
    public int[][] deepCopyBoard(int[][] src) {
        int[][] copy = new int[SIZE][SIZE];
        for (int r = 0; r < SIZE; r++) {
            System.arraycopy(src[r], 0, copy[r], 0, SIZE);
        }
        return copy;
    }
}
