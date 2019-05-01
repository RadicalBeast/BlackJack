import java.util.List;
import java.util.ArrayList;

/**
 * Write a description of class PairsBoard here.
 *
 * @author Justin Huang
 * @version (a version number or a date)
 */
public class PairsBoard extends Board
{
    /**
     * The size (number of cards) on the board.
     */
    private static final int BOARD_SIZE = 15;

    /**
     * The ranks of the cards for this game to be sent to the deck.
     */
    private static final String[] RANKS = 
        {"ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "jack", "queen", "king"};

    /**
     * The suits of the cards for this game to be sent to the deck.
     */
    private static final String[] SUITS =
        {"spades", "hearts", "diamonds", "clubs"};

    /**
     * The values of the cards for this game to be sent to the deck.
     */
    private static final int[] POINT_VALUES =
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13};

    /**
     * Flag used to control debugging print statements.
     */
    private static final boolean I_AM_DEBUGGING = true;


    /**
     * Creates a new <code>ElevensBoard</code> instance.
     */
     public PairsBoard()  {
        super(BOARD_SIZE, RANKS, SUITS, POINT_VALUES);
     }

    /**
     * Determines if the selected cards form a valid group for removal.
     * In Thirteens, the legal groups are (1) a pair of non-face cards
     * whose values add to 13, and (2) a king.
     * @param selectedCards the list of the indices of the selected cards.
     * @return true if the selected cards form a valid group for removal;
     *         false otherwise.
     */
    @Override
    public boolean isLegal(List<Integer> selectedCards) {
       return findPairSum13(selectedCards).size() > 0;
    }

    /**
     * Determine if there are any legal plays left on the board.
     * In Thirteens, there is a legal play if the board contains
     * (1) a pair of non-face cards whose values add to 13, or (2) a king.
     * @return true if there is a legal play left on the board;
     *         false otherwise.
     */
    @Override
    public boolean anotherPlayIsPossible() {
        List<Integer> cIndexes = cardIndexes();
        return findPairSum13(cIndexes).size() > 0;
    }

    /**
     * Look for an 13-pair in the selected cards.
     * @param selectedCards selects a subset of this board.  It is list
     *                      of indexes into this board that are searched
     *                      to find an 13-pair.
     * @return a list of the indexes of an 13-pair, if an 13-pair was found;
     *         an empty list, if an 13-pair was not found.
     */
    private List<Integer> findPairSum13(List<Integer> selectedCards) {
        List<Integer> foundIndexes = new ArrayList<Integer>();
        List<Integer> emptyIndexes = new ArrayList<Integer>();
        
        if (selectedCards.size() == 0 || selectedCards.size() == 1)
            return emptyIndexes;
            
        int selectedPointValue = cardAt(selectedCards.get(0).intValue()).pointValue();
        
        for (int i = 1; i < selectedCards.size(); i++) {
            int k = selectedCards.get(i).intValue();
            if (selectedPointValue != cardAt(k).pointValue())
                return emptyIndexes;
            else
                foundIndexes.add(new Integer(k));
       }
       foundIndexes.add(new Integer(selectedCards.get(0).intValue()));
       return foundIndexes;
    }

    /**
     * Looks for a legal play on the board.  If one is found, it plays it.
     * @return true if a legal play was found (and made); false othewise.
     */
    public boolean playIfPossible() {
        return playPairSum13IfPossible();
    }

    /**
     * Looks for a pair of non-face cards whose values sum to 13.
     * If found, replace them with the next two cards in the deck.
     * The simulation of this game uses this method.
     * @return true if an 13-pair play was found (and made); false othewise.
     */
    private boolean playPairSum13IfPossible() {
        List<Integer> foundIndexes = cardIndexes();
        List<Integer> cardsToReplace = findPairSum13(foundIndexes);
        if (cardsToReplace.size() > 0) {
            replaceSelectedCards(cardsToReplace);
            if (I_AM_DEBUGGING) {
                System.out.println("13-Pair removed.\n");
            }
            return true;
        } else {
            return false;
        }
    }

    
}
