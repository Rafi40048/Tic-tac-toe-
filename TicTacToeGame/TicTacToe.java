import java.awt.*;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

public class TicTacToe extends JFrame {
    private JButton[][] buttons = new JButton[3][3];
    private JButton restartButton;
    private JLabel statusLabel, timerLabel;
    private TicTacToeGame game;
    private Timer moveTimer;
    private int timeRemaining;

    private Color xColor = new Color(0x4CAF50); // Green
    private Color oColor = new Color(0x2196F3); // Blue
    private Color bgColor = new Color(0xFFF8E1); // Light background
    private Color gridColor = new Color(0xFBC02D); // Yellow

    public TicTacToe() {
        game = new TicTacToeGame();
        setTitle("🎮 Colorful Tic Tac Toe");
        setSize(460, 530);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(bgColor);

        JPanel boardPanel = new JPanel(new GridLayout(3, 3));
        boardPanel.setBackground(bgColor);

        Font font = new Font("Comic Sans MS", Font.BOLD, 40);

        // Create 3x3 board buttons
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                JButton btn = new JButton("");
                btn.setFont(font);
                btn.setBackground(gridColor);
                btn.setFocusPainted(false);
                btn.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 3));
                final int row = i, col = j;

                btn.addActionListener(e -> handleMove(row, col, btn));
                buttons[i][j] = btn;
                boardPanel.add(btn);
            }
        }

        // Status label
        statusLabel = new JLabel("Player X's Turn", SwingConstants.CENTER);
        statusLabel.setFont(new Font("Verdana", Font.BOLD, 18));
        statusLabel.setForeground(xColor);

        // Timer label
        timerLabel = new JLabel("Time: 10s", SwingConstants.CENTER);
        timerLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        timerLabel.setForeground(Color.RED);

        // Restart button
        restartButton = new JButton("Restart 🔄");
        restartButton.setFont(new Font("Arial", Font.BOLD, 16));
        restartButton.setBackground(new Color(0xFF5722)); // Orange
        restartButton.setForeground(Color.WHITE);
        restartButton.addActionListener(e -> resetGame());

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(bgColor);
        bottomPanel.add(statusLabel, BorderLayout.CENTER);
        bottomPanel.add(restartButton, BorderLayout.EAST);
        bottomPanel.add(timerLabel, BorderLayout.WEST);

        add(boardPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        startTimer();
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void handleMove(int row, int col, JButton btn) {
        if (game.makeMove(row, col)) {
            String player = game.getCurrentPlayer();
            btn.setText(player);
            btn.setForeground(player.equals("X") ? xColor : oColor);
            animateButton(btn);

            if (game.checkWin()) {
                statusLabel.setText("🎉 Player " + player + " Wins!");
                statusLabel.setForeground(Color.MAGENTA);
                disableButtons();
                stopTimer();
            } else if (game.checkDraw()) {
                statusLabel.setText("😐 It's a Draw!");
                statusLabel.setForeground(Color.GRAY);
                stopTimer();
            } else {
                game.switchPlayer();
                String nextPlayer = game.getCurrentPlayer();
                statusLabel.setText("Player " + nextPlayer + "'s Turn");
                statusLabel.setForeground(nextPlayer.equals("X") ? xColor : oColor);
                restartTimer();
            }
        }
    }

    private void animateButton(JButton btn) {
        Color original = btn.getBackground();
        btn.setBackground(Color.WHITE);
        new Timer().schedule(new TimerTask() {
            public void run() {
                btn.setBackground(original);
            }
        }, 200);
    }

    private void resetGame() {
        game.resetGame();
        for (JButton[] row : buttons)
            for (JButton btn : row) {
                btn.setText("");
                btn.setEnabled(true);
                btn.setBackground(gridColor);
            }
        statusLabel.setText("Player X's Turn");
        statusLabel.setForeground(xColor);
        restartTimer();
    }

    private void disableButtons() {
        for (JButton[] row : buttons)
            for (JButton btn : row)
                btn.setEnabled(false);
    }

    private void startTimer() {
        timeRemaining = 10;
        updateTimerLabel();
        moveTimer = new Timer();
        moveTimer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                timeRemaining--;
                SwingUtilities.invokeLater(() -> updateTimerLabel());

                if (timeRemaining == 0) {
                    moveTimer.cancel();
                    SwingUtilities.invokeLater(() -> {
                        game.switchPlayer();
                        statusLabel.setText("⏰ Time Up! Player " + game.getCurrentPlayer() + "'s Turn");
                        restartTimer();
                    });
                }
            }
        }, 1000, 1000);
    }

    private void restartTimer() {
        if (moveTimer != null)
            moveTimer.cancel();
        startTimer();
    }

    private void stopTimer() {
        if (moveTimer != null)
            moveTimer.cancel();
        timerLabel.setText("Game Over");
    }

    private void updateTimerLabel() {
        timerLabel.setText("Time: " + timeRemaining + "s");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TicTacToe::new);
    }
}
