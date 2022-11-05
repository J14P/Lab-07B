import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Scanner;

public class TicTacToeFrame extends JFrame {
    JPanel mainPnl;
    JPanel btnPnl;

    JOptionPane optionPane;

    TicTacToeTile[][] displayBoard = new TicTacToeTile[3][3];

    private static final int ROW = 3;
    private static final int COL = 3;
    private static String[][] board = new String[ROW][COL];

    JButton quitBtn;

    boolean playing = true;
    private String player = "X";
    int moveCnt = 0;
    int row = -1;
    int col = -1;
    final int MOVES_FOR_WIN = 5;
    final int MOVES_FOR_TIE = 7;

    public TicTacToeFrame()
    {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Tic Tac Toe Game");
        setSize(750,750);
        mainPnl = new JPanel();
        mainPnl.setLayout(new BorderLayout());
        quitBtn = new JButton("Quit");
        quitBtn.addActionListener((ActionEvent e) ->{System.exit(0);});
        quitBtn.setFont(new Font("Arial", Font.PLAIN, 24));
        createBtnPnl();
        initializeBoard();
        mainPnl.add(btnPnl, BorderLayout.CENTER);
        mainPnl.add(quitBtn, BorderLayout.SOUTH);
        add(mainPnl);
        setVisible(true);
    }

    private void createBtnPnl()
    {
        btnPnl = new JPanel();
        btnPnl.setLayout(new GridLayout(3,3));
        btnPnl.setBorder(new TitledBorder(new EtchedBorder(),""));
        for( int r = 0; r < 3; r++)
            for(int c= 0; c < 3; c++)
            {
                displayBoard[r][c] = new TicTacToeTile(r, c);
                displayBoard[r][c].setText(" ");
                displayBoard[r][c].setFont(new Font("Arial", Font.BOLD, 48));
                btnPnl.add(displayBoard[r][c]);

                displayBoard[r][c].addActionListener((ActionEvent ae)->
                {
                    // ae.getSource returns which button got pressed
                    TicTacToeTile tile = (TicTacToeTile) ae.getSource();
                    row = tile.getRow();
                    col = tile.getCol();
                    // get the row and col, use the tictactoetile methods
                    // display the row and col to test the program
                    // is valid move?
                    // once a button is pushed, disable it so that it can't be used for the rest of the game
                    displayBoard[row][col].setEnabled(false);
                    displayBoard[row][col].setBackground(new Color(247, 187, 178));
                    // record the move on the internal board
                    board[row][col] = player;
                    //update the gui, button now shows player
                    displayBoard[row][col].setText(player);
                    //update move counter
                    moveCnt++;
                    //if enough moves check for win, if there is a win display the win dialog and ask the player if they want to play again
                    if (moveCnt >= MOVES_FOR_WIN)
                    {
                        if(isWin(player))
                        {
                            disableButtons();
                            player = "X";
                            int result = JOptionPane.showConfirmDialog(optionPane, "Do you want to play again?", "Player: "+ player +" Wins!", JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.YES_OPTION)
                            {
                                initializeBoard();
                            } else if(result == JOptionPane.NO_OPTION)
                            {
                                System.exit(0);
                            }
                        }
                    }
                    //if enough moves check for tie, if there is a tie display the tie dialog and ask the player if they want to play again
                    if (moveCnt >= MOVES_FOR_TIE) {
                        if(isTie())
                        {
                            disableButtons();
                            player = "X";
                            int result = JOptionPane.showConfirmDialog(optionPane, "Do you want to play again?", "There is a tie!", JOptionPane.YES_NO_OPTION);
                            if (result == JOptionPane.YES_OPTION)
                            {
                                initializeBoard();
                            } else if(result == JOptionPane.NO_OPTION)
                            {
                                System.exit(0);
                            }
                        }
                    }
                    //toggle the player
                    if(player.equals("X"))
                    {
                        player = "O";
                    }
                    else
                    {
                        player = "X";
                    }
                });
            }
    }

    private static boolean isWin(String player)
    {
        if(isColWin(player) || isRowWin(player) || isDiagnalWin(player))
        {
            return true;
        }

        return false;
    }
    private static boolean isColWin(String player)
    {
        // checks for a col win for specified player
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].equals(player) &&
                    board[1][col].equals(player) &&
                    board[2][col].equals(player))
            {
                return true;
            }
        }
        return false; // no col win
    }
    private static boolean isRowWin(String player)
    {
        // checks for a row win for the specified player
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].equals(player) &&
                    board[row][1].equals(player) &&
                    board[row][2].equals(player))
            {
                return true;
            }
        }
        return false; // no row win
    }
    private static boolean isDiagnalWin(String player)
    {
        // checks for a diagonal win for the specified player

        if(board[0][0].equals(player) &&
                board[1][1].equals(player) &&
                board[2][2].equals(player) )
        {
            return true;
        }
        if(board[0][2].equals(player) &&
                board[1][1].equals(player) &&
                board[2][0].equals(player) )
        {
            return true;
        }
        return false;
    }

    // checks for a tie before board is filled.
    // check for the win first to be efficient
    private static boolean isTie()
    {
        boolean xFlag = false;
        boolean oFlag = false;
        // Check all 8 win vectors for an X and O so
        // no win is possible
        // Check for row ties
        for(int row=0; row < ROW; row++)
        {
            if(board[row][0].equals("X") ||
                    board[row][1].equals("X") ||
                    board[row][2].equals("X"))
            {
                xFlag = true; // there is an X in this row
            }
            if(board[row][0].equals("O") ||
                    board[row][1].equals("O") ||
                    board[row][2].equals("O"))
            {
                oFlag = true; // there is an O in this row
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a row win
            }

            xFlag = oFlag = false;

        }
        // Now scan the columns
        for(int col=0; col < COL; col++)
        {
            if(board[0][col].equals("X") ||
                    board[1][col].equals("X") ||
                    board[2][col].equals("X"))
            {
                xFlag = true; // there is an X in this col
            }
            if(board[0][col].equals("O") ||
                    board[1][col].equals("O") ||
                    board[2][col].equals("O"))
            {
                oFlag = true; // there is an O in this col
            }

            if(! (xFlag && oFlag) )
            {
                return false; // No tie can still have a col win
            }
        }
        // Now check for the diagonals
        xFlag = oFlag = false;

        if(board[0][0].equals("X") ||
                board[1][1].equals("X") ||
                board[2][2].equals("X") )
        {
            xFlag = true;
        }
        if(board[0][0].equals("O") ||
                board[1][1].equals("O") ||
                board[2][2].equals("O") )
        {
            oFlag = true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diag win
        }
        xFlag = oFlag = false;

        if(board[0][2].equals("X") ||
                board[1][1].equals("X") ||
                board[2][0].equals("X") )
        {
            xFlag =  true;
        }
        if(board[0][2].equals("O") ||
                board[1][1].equals("O") ||
                board[2][0].equals("O") )
        {
            oFlag =  true;
        }
        if(! (xFlag && oFlag) )
        {
            return false; // No tie can still have a diag win
        }

        // Checked every vector so I know I have a tie
        return true;
    }

    public void initializeBoard()
    {
        player = "X";
        playing = true;
        moveCnt = 0;
        clearBoard();
    }

    private void clearBoard() {
        // sets all the board elements to a space
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                board[row][col] = " ";
            }
        }

        // reset display board as well
        for (int row = 0; row < ROW; row++) {
            for (int col = 0; col < COL; col++) {
                displayBoard[row][col].setText(" ");
                displayBoard[row][col].setEnabled(true);
                displayBoard[row][col].setBackground(new Color(185, 245, 171));
            }
        }

    }

    private void disableButtons()
    {
        for(int row=0; row < ROW; row++)
        {
            for(int col=0; col < COL; col++)
            {
                displayBoard[row][col].setEnabled(false);
                displayBoard[row][col].setBackground(new Color(247, 187, 178));

            }
        }
    }
}
