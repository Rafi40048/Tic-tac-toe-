public class TicTacToeGame implements GameLogic {
    private String[][] board;
    private String currentPlayer;

    public TicTacToeGame() {
        board = new String[3][3];
        currentPlayer = "X";
    }

    public String getCurrentPlayer() {
        return currentPlayer;
    }

    public void switchPlayer() {
        currentPlayer = currentPlayer.equals("X") ? "O" : "X";
    }

    public boolean makeMove(int row, int col) {
        if (board[row][col] == null || board[row][col].isEmpty()) {
            board[row][col] = currentPlayer;
            return true;
        }
        return false;
    }

    public String getCell(int row, int col) {
        return board[row][col];
    }

    @Override
    public boolean checkWin() {
        for (int i = 0; i < 3; i++) {
            if (equal(board[i][0], board[i][1], board[i][2]))
                return true;
            if (equal(board[0][i], board[1][i], board[2][i]))
                return true;
        }
        if (equal(board[0][0], board[1][1], board[2][2]))
            return true;
        if (equal(board[0][2], board[1][1], board[2][0]))
            return true;
        return false;
    }

    private boolean equal(String a, String b, String c) {
        return a != null && a.equals(b) && b.equals(c);
    }

    @Override
    public boolean checkDraw() {
        for (String[] row : board)
            for (String cell : row)
                if (cell == null || cell.isEmpty())
                    return false;
        return !checkWin();
    }

    @Override
    public void resetGame() {
        for (int i = 0; i < 3; i++)
            for (int j = 0; j < 3; j++)
                board[i][j] = "";
        currentPlayer = "X";
    }
}
