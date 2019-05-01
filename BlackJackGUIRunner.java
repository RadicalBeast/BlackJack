/**
 * Write a description of class PairsGUIRunner here.
 *
 * @author Justin Huang
 * @version 4/20/2019
 */
public class BlackJackGUIRunner 
{
    /**
     * Plays the GUI version of BlackJack.
     * @param args is not used.
     */
    public static void main(String[] args) {
        BlackJackBoard board = new BlackJackBoard(7);
        BlackJackGameGUI gui = new BlackJackGameGUI(board);
        gui.displayGame();
    }
}