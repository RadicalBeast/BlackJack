
/**
 * Write a description of class PairsGUIRunner here.
 *
 * @author (your name)
 * @version (a version number or a date)
 */
public class PairsGUIRunner 
{
    /**
     * Plays the GUI version of Thirteens.
     * @param args is not used.
     */
    public static void main(String[] args) {
        Board board = new PairsBoard();
        CardGameGUI gui = new CardGameGUI(board);
        gui.displayGame();
    }
}
