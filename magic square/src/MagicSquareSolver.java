// MagicSquareSolver.java
public class MagicSquareSolver {

    public int[][] solve(int[][] puzzle, int n) {
        int[][] sol = new int[n][n];
        for (int i = 0; i < n; i++)
            System.arraycopy(puzzle[i], 0, sol[i], 0, n);
        return backtrack(sol, n, 0, 0) ? sol : null;
    }

    private boolean backtrack(int[][] sq, int n, int r, int c) {
        if (r == n) return isMagicSquare(sq, n);
        int nr = (c+1==n)? r+1 : r, nc = (c+1)%n;
        if (sq[r][c] != 0) return backtrack(sq, n, nr, nc);
        for (int num = 1; num <= n*n; num++) {
            if (canPlace(sq, n, r, c, num)) {
                sq[r][c] = num;
                if (backtrack(sq, n, nr, nc)) return true;
                sq[r][c] = 0;
            }
        }
        return false;
    }

    private boolean canPlace(int[][] sq, int n, int r, int c, int num) {
        for (int i=0;i<n;i++)for(int j=0;j<n;j++)
            if (sq[i][j] == num) return false;
        return true;
    }

    private int magicConstant(int n) { return n*(n*n+1)/2; }

    private boolean isMagicSquare(int[][] sq, int n) {
        int m = magicConstant(n), sum;
        for (int i=0;i<n;i++){
            sum = 0; for(int j=0;j<n;j++) sum += sq[i][j];
            if (sum != m) return false;
        }
        for (int j=0;j<n;j++){
            sum = 0; for(int i=0;i<n;i++) sum += sq[i][j];
            if (sum != m) return false;
        }
        sum = 0; for(int i=0;i<n;i++) sum += sq[i][i];
        if (sum != m) return false;
        sum = 0; for(int i=0;i<n;i++) sum += sq[i][n-1-i];
        return sum == m;
    }
}
