import java.util.List;
import java.util.ArrayList;

/**
 * This class represents a Board that can be used in a collection
 * of solitaire games similar to Elevens.  The variants differ in
 * card removal and the board size.
 */
public class BlackJackBoard {

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
        {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 10, 10, 10};

    /**
     * Flag used to control debugging print statements.
     */
    private static final boolean I_AM_DEBUGGING = true;

    /**
     * my cards on this board.
     */
    private Card[] myCards;
    int myCardNumber = 0;

    /**
      * my cards on this board.
    */
   private Card[] dealerCards;
   int dealerCardNumber = 0;

    /**
     * The deck of cards being used to play the current game.
    */
    private Deck deck;

    /**
     * Creates a new <code>ElevensBoard</code> instance.
     */
     public BlackJackBoard (int cardSize)  {
        this(cardSize, RANKS, SUITS, POINT_VALUES);
     }

    /**
     * Creates a new <code>Board</code> instance.
     * @param size the number of cards in the board
     * @param ranks the names of the card ranks needed to create the deck
     * @param suits the names of the card suits needed to create the deck
     * @param pointValues the integer values of the cards needed to create
     *                    the deck
     */
    public BlackJackBoard(int size, String[] ranks, String[] suits, int[] pointValues) {
        myCards = new Card[size];
        dealerCards = new Card[size];
        deck = new Deck(ranks, suits, pointValues);
        if (I_AM_DEBUGGING) {
            System.out.println(deck);
            System.out.println("----------");
        }
        dealMyCards();
    }

    /**
     * Start a new game by shuffling the deck and
     * dealing some cards to this board.
     */
    public void newGame() {
       dealMyCards();
    }

    /**
     * Accesses the size of the board.
     * Note that this is not the number of cards it contains,
     * which will be smaller near the end of a winning game.
     * @return the size of the board
     */
    
    public int getMyCardSize(){
        return myCardNumber;
    }

    public int getDealerCardSize(){
        return dealerCardNumber;
    }

    /**
     * Deal a card to the kth position in this board.
     * If the deck is empty, the kth card is set to null.
     * @param k the index of the card to be dealt.
     */
    public void dealToMyCard() {
        if (myCardNumber >= size())
            return;
        myCards[myCardNumber] = deck.deal();
        myCardNumber++;
    }

    public void dealToDealerCard (){
        if (dealerCardNumber >= size())
            return;
        dealerCards[dealerCardNumber] = deck.deal();
        dealerCardNumber++;
    }
    
    /**
     * Accesses the deck's size.
     * @return the number of undealt cards left in the deck.
     */
    public int deckSize() {
        return deck.size();
    }

    /**
     * Accesses the size of the cards per row.
     * @return the size of the board
     */
    public int size() {
        return myCards.length;
    }
    
    /**
     * Accesses a card on the board.
     * @return the card at position k on the board.
     * @param k is the board position of the card to return.
     */
    public Card myCardAt(int k) {
        return myCards[k];
    }
    
    public Card dealerCardAt(int k){
        return dealerCards[k];
    }
    
    public int getMyHandSum(){
        return getHandSum(myCards, myCardNumber);
    }
    
    public int getDealerHandSum(){
        return getHandSum(dealerCards, dealerCardNumber);
    }
    
    private int getHandSum(Card[] cards, int size){
        int handSum =0;
        int cardNum;
        int numAces = 0;
        
        for (int c = 0; c < size; c++){
            cardNum = cards[c].pointValue();
            if (cardNum == 1) { // Ace
                numAces++;
                handSum +=11;;
            } else {
                handSum += cardNum;
            }
        }
        
        while (handSum > 21 && numAces > 0){
            handSum -= 10;
            numAces--;
        }
        return handSum;
    }
    
    /**
     * Deal cards to this board to start the game.
     */
    private void dealMyCards() {
        if (myCardNumber >= size())
            return;
        myCards[0] = deck.deal();
        dealerCards[0] = deck.deal();
        myCards[1] = deck.deal();
        dealerCards[1] = deck.deal();
        myCardNumber = 2;
        dealerCardNumber = 2;
    }
}