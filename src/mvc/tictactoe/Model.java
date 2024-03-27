package mvc.tictactoe;

import com.mrjaffesclass.apcs.messenger.*;

/**
 * The model represents the data that the app uses.
 * @author Roger Jaffe
 * @version 1.0
 */
public class Model implements MessageHandler {

  // Messaging system for the MVC
  private final Messenger mvcMessaging;

  // Model's data variables
  private boolean whoseMove;
  private boolean gameOver;
  private String[][] board;

  /**
   * Model constructor: Create the data representation of the program
   * @param messages Messaging class instantiated by the Controller for 
   *   local messages between Model, View, and controller
   */
  public Model(Messenger messages) {
    mvcMessaging = messages;
    this.board = new String[3][3];
  }
  
  /**
   * Initialize the model here and subscribe to any required messages
   */
  public void init() {
    this.newGame();
    this.mvcMessaging.subscribe("playerMove", this);
    this.mvcMessaging.subscribe("newGame", this);
  }
  
  /**
   * Reset the state for a new game
   */
  private void newGame() {
    for (int row = 0; row < this.board.length; row++) {
        for (int col = 0; col < this.board[0].length; col++) {
            this.board[row][col] = "";
        }
    }
    this.whoseMove = false;
    this.gameOver = false;
  }
  
   private String isWinner() {
        // Check the rows and columns for a tic tac toe
        for (int i = 0; i < 3; i++) {
            if (board[i][0].equals(board[i][1]) && board[i][0].equals(board[i][2]) && !board[i][0].equals("")) {
                return board[i][0];
            }
            if (board[0][i].equals(board[1][i]) && board[0][i].equals(board[2][i]) && !board[0][i].equals("")) {
                return board[0][i];
            }
        }
        
        
        // Check the diagonals
        if (board[0][0].equals(board[1][1]) && board[0][0].equals(board[2][2]) && !board[0][0].equals("")) {
            return board[0][0];
        }
        if (board[0][2].equals(board[1][1]) && board[0][2].equals(board[2][0]) && !board[0][2].equals("")) {
            return board[0][2];
        }
        
        // If we haven't found it, then return a blank string
        return "";

    }
  
   
   private boolean isTie() {
    for (int i = 0; i < 3; i++) {
        for (int k = 0; k < 3; k++) {
            if (board[i][k].equals("")) {
                return false;
            }
        }
    }
    return true;
   }
   
  @Override
  public void messageHandler(String messageName, Object messagePayload) {
    // Display the message to the console for debugging
    if (messagePayload != null) {
      System.out.println("MSG: received by model: "+messageName+" | "+messagePayload.toString());
    } else {
      System.out.println("MSG: received by model: "+messageName+" | No data sent");
    }
    
    // playerMove message handler
    if (messageName.equals("playerMove")) {
      // Get the position string and convert to row and col
      String position = (String)messagePayload;
      Integer row = new Integer(position.substring(0,1));
      Integer col = new Integer(position.substring(1,2));
      // If square is blank...
      if (this.board[row][col].equals("") && !this.gameOver) {
        // ... then set X or O depending on whose move it is
        if (this.whoseMove) {
          this.board[row][col] = "X";
        } else {
          this.board[row][col] = "O";
        }
        
        String winner = this.isWinner();
        boolean tie = this.isTie();
        if (!winner.equals("")) {
            this.mvcMessaging.notify("gameOver", winner + " WON THE GAME!");
            this.gameOver = true;
        } else if (tie) {
            this.mvcMessaging.notify("gameOver", "TIE GAME!");
            this.gameOver = true;
        }
        // Send the boardChange message along with the new board 
        this.mvcMessaging.notify("boardChange", this.board);
        
        this.whoseMove = !this.whoseMove;
        this.mvcMessaging.notify("whoseTurn", this.whoseMove);
      }
      
    // newGame message handler
    } else if (messageName.equals("newGame")) {
      // Reset the app state
      this.newGame();
      // Send the boardChange message along with the new board 
      this.mvcMessaging.notify("boardChange", this.board);
    }
  }


  /**
   * Getter function for variable 1
   * @return Value of variable1
   */

  /**
   * Setter function for variable 1
   * @param v New value of variable1
   */
  
  /**
   * Getter function for variable 1
   * @return Value of variable2
   */
  
  /**
   * Setter function for variable 2
   * @param v New value of variable 2
   */

}
