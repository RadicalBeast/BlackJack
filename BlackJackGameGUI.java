import java.awt.Point;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

/**
 * This class provides a GUI for solitaire games related to Elevens.
 */
public class BlackJackGameGUI extends JFrame implements ActionListener {

    /** Height of the game frame. */
    private static final int DEFAULT_HEIGHT = 400 ;
    /** Width of the game frame. */
    private static final int DEFAULT_WIDTH = 800;
    /** Width of a card. */
    private static final int CARD_WIDTH = 73;
    /** Height of a card. */
    private static final int CARD_HEIGHT = 97;
    /** Row (y coord) of the upper left corner of the first card. */
    private static final int LAYOUT_TOP = 30;
    /** Column (x coord) of the upper left corner of the first card. */
    private static final int LAYOUT_LEFT = 30;
    /** Distance between the upper left x coords of
     *  two horizonally adjacent cards. */
    private static final int LAYOUT_WIDTH_INC = 100;
    /** Distance between the upper left y coords of
     *  two vertically adjacent cards. */
    private static final int LAYOUT_HEIGHT_INC = 125;
    /** y coord of the "Hit" button. */
    private static final int BUTTON_TOP = 30;
    /** x coord of the "Hit" button. */
    private static final int BUTTON_LEFT = 570;
    /** Distance between the tops of the buttons. */
    private static final int BUTTON_HEIGHT_INC = 50;
    /** y coord of the "n undealt cards remain" label. */
    private static final int LABEL_TOP = 210;
    /** x coord of the "n undealt cards remain" label. */
    private static final int LABEL_LEFT = 540;
    /** Distance between the tops of the "n undealt cards" and
     *  the "You lose/win" labels. */
    private static final int LABEL_HEIGHT_INC = 35;

    /** The board  */
    private BlackJackBoard board;

    /** The main panel containing the game components. */
    private JPanel panel;
    /** The Hit button. */
    private JButton hitButton;
    /** The Stay button. */
    private JButton stayButton;
    /** The New Game button. */
    private JButton newButton;
    /** The "number of undealt cards remain" message. */
    private JLabel statusMsg;
    /** The "you've won n out of m games" message. */
    private JLabel totalsMsg;
    /** The card displays. */
    private JLabel[] displayMyCards;
    private JLabel[] displayDealerCards;
    /** The win message. */
    private JLabel winMsg;
    /** The loss message. */
    private JLabel lossMsg;
    /** The coordinates of the card displays. */
    private Point[] myCardCoords;
    private Point[] dealerCardCoords;
    
    /** The number of games won. */
    private int totalWins;
    /** The number of games played. */
    private int totalGames;
    /** flag to show dealer's card **/
    private boolean gameOver = false;


    /**
     * Initialize the GUI.
     * @param gameBoard is a <code>Board</code> subclass.
     */
    public BlackJackGameGUI(BlackJackBoard gameBoard) {
        board = gameBoard;
        totalWins = 0;
        totalGames = 0;
        // Initialize cardCoords 2 row with each row board size
        myCardCoords = new Point[board.size()];
        dealerCardCoords = new Point[board.size()];   

        int x = LAYOUT_LEFT;
        int y = LAYOUT_TOP + LABEL_HEIGHT_INC;
        for (int i = 0; i < board.size(); i++) {
            myCardCoords[i] = new Point(x, y);
            x += LAYOUT_WIDTH_INC;
        }

        x = LAYOUT_LEFT;
        y += LAYOUT_HEIGHT_INC + LABEL_HEIGHT_INC;
        for (int i = 0; i < board.size(); i++) {
            dealerCardCoords[i] = new Point(x, y);
            x += LAYOUT_WIDTH_INC;
        }
    
        initDisplay();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        repaint();
    }

    /**
     * Run the game.
     */
    public void displayGame() {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                setVisible(true);
            }
        });
    }

    /**
     * Draw the display (cards and messages).
     */
    public void repaint() {
       // remove the previous cards
       for (int k = 0; k < board.size(); k++){
           displayMyCards[k].setVisible(false);
           displayDealerCards[k].setVisible(false);
       }
       
       for (int k = 0; k < board.getMyCardSize(); k++) {
            String cardImageFileName =
                imageFileName(board.myCardAt(k), false);
            URL imageURL = getClass().getResource(cardImageFileName);
            if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                displayMyCards[k].setIcon(icon);
                displayMyCards[k].setVisible(true);
            } else {
                throw new RuntimeException(
                    "Card image not found: \"" + cardImageFileName + "\"");
            }
       }
        
       for (int k = 0; k < board.getDealerCardSize(); k++) {
           String cardImageFileName =
                imageFileName(board.dealerCardAt(k), k == 0);
           URL imageURL = getClass().getResource(cardImageFileName);
           if (imageURL != null) {
                ImageIcon icon = new ImageIcon(imageURL);
                displayDealerCards[k].setIcon(icon);
                displayDealerCards[k].setVisible(true);
           } else {
                throw new RuntimeException(
                   "Card image not found: \"" + cardImageFileName + "\"");
               }
       }

       statusMsg.setText(board.deckSize()
            + " undealt cards remain.");
            statusMsg.setVisible(true);
            totalsMsg.setText("You've won " + totalWins
            + " out of " + totalGames + " games.");
            totalsMsg.setVisible(true);
       pack();
       panel.repaint();
    }

    /**
     * Initialize the display.
     */
    private void initDisplay()  {
        panel = new JPanel() {
            public void paintComponent(Graphics g) {
                super.paintComponent(g);
            }
        };

        // If board object's class name follows the standard format
        // of ...Board or ...board, use the prefix for the JFrame title
        String className = board.getClass().getSimpleName();
        int classNameLen = className.length();
        int boardLen = "Board".length();
        String boardStr = className.substring(classNameLen - boardLen);
        if (boardStr.equals("Board") || boardStr.equals("board")) {
            int titleLength = classNameLen - boardLen;
            setTitle(className.substring(0, titleLength));
        }

        int height = DEFAULT_HEIGHT;
        
        this.setSize(new Dimension(DEFAULT_WIDTH, height));
        panel.setLayout(null);
        panel.setPreferredSize(
            new Dimension(DEFAULT_WIDTH - 20, height - 20));
            
        JLabel myCardMsg = new JLabel("Your Cards:");
        panel.add(myCardMsg);
        myCardMsg.setBounds(LAYOUT_LEFT, LAYOUT_TOP, 250, 30);
        
        displayMyCards = new JLabel[board.size()];
        displayDealerCards = new JLabel[board.size()];
      
        for (int k = 0; k < board.size(); k++) {
            displayMyCards[k] = new JLabel();
            panel.add(displayMyCards[k]);
            displayMyCards[k].setBounds(myCardCoords[k].x, myCardCoords[k].y,
                                        CARD_WIDTH, CARD_HEIGHT);
        }
        
        JLabel dealerCardMsg = new JLabel("Dealer's Cards:");
        panel.add(dealerCardMsg);
        dealerCardMsg.setBounds(LAYOUT_LEFT, LAYOUT_TOP + LAYOUT_HEIGHT_INC + LABEL_HEIGHT_INC, 250, 30);
        
        for (int k = 0; k < board.size(); k++) {
            displayDealerCards[k] = new JLabel();
            panel.add(displayDealerCards[k]);
            displayDealerCards[k].setBounds(dealerCardCoords[k].x, dealerCardCoords[k].y,
                                        CARD_WIDTH, CARD_HEIGHT);
        }
        
        hitButton = new JButton();
        hitButton .setText("Hit");
        panel.add(hitButton);
        hitButton.setBounds(BUTTON_LEFT, BUTTON_TOP, 100, 30);
        hitButton.addActionListener(this);

        stayButton = new JButton();
        stayButton.setText("Stay");
        panel.add(stayButton);
        stayButton.setBounds(BUTTON_LEFT, + BUTTON_TOP + BUTTON_HEIGHT_INC, 100, 30);
        stayButton.addActionListener(this);

        newButton = new JButton();
        newButton.setText("New Game");
        panel.add(newButton);
        newButton.setBounds(BUTTON_LEFT, BUTTON_TOP + BUTTON_HEIGHT_INC*2,
                                        100, 30);
        newButton.addActionListener(this);

        statusMsg = new JLabel(
            board.deckSize() + " undealt cards remain.");
        panel.add(statusMsg);
        statusMsg.setBounds(LABEL_LEFT, LABEL_TOP, 250, 30);

        winMsg = new JLabel();
        winMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 300, 30);
        winMsg.setFont(new Font("SansSerif", Font.BOLD, 25));
        winMsg.setForeground(Color.GREEN);
        winMsg.setText("You win!");
        panel.add(winMsg);
        winMsg.setVisible(false);

        totalsMsg = new JLabel("You've won " + totalWins
            + " out of " + totalGames + " games.");
        totalsMsg.setBounds(LABEL_LEFT, LABEL_TOP + 2 * LABEL_HEIGHT_INC,
                                  300, 30);
        panel.add(totalsMsg);

        lossMsg = new JLabel();
        lossMsg.setBounds(LABEL_LEFT, LABEL_TOP + LABEL_HEIGHT_INC, 300, 30);
        lossMsg.setFont(new Font("SanSerif", Font.BOLD, 25));
        lossMsg.setForeground(Color.RED);
        lossMsg.setText("Sorry dealer wins.");
        panel.add(lossMsg);
        lossMsg.setVisible(false);
        
        pack();
        getContentPane().add(panel);
        getRootPane().setDefaultButton(hitButton);
        panel.setVisible(true);
    }

    
    /**
     * Returns the image that corresponds to the input card.
     * Image names have the format "[Rank][Suit].GIF" or "[Rank][Suit]S.GIF",
     * for example "aceclubs.GIF" or "8heartsS.GIF". The "S" indicates that
     * the card is selected.
     *
     * @param c Card to get the image for
     * @param isSelected flag that indicates if the card is selected
     * @return String representation of the image
     */
    private String imageFileName(Card c, boolean hide) {
        String str = "cards/";
        if (c == null || (hide && !gameOver)) {
            return "cards/back1.GIF";
        }
        str += c.rank() + c.suit();
        str += ".GIF";
        return str;
    }

    /**
     * Respond to a button click (on either the "Hit" button
     * or the "Stay" button or "New Game" button).
     * @param e the button click action event
     */
    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(hitButton)) {
            board.dealToMyCard();
            if (board.getMyHandSum() > 21){
                signalLoss();
            }   
            repaint();
        } else if (e.getSource().equals(stayButton)) {
            // dealer turn
            while (board.getDealerHandSum() < 17){
                board.dealToDealerCard();
            }
            if (board.getDealerHandSum() > 21 || board.getDealerHandSum() < board.getMyHandSum())
                signalWin();
            else
                signalLoss();
            repaint();
        }  else if (e.getSource().equals(newButton)) {
            signalNewGame();
            repaint();
        } else {
            signalError();
            return;
        }
    }

    /**
     * Deal with the user clicking on something other than a button or a card.
     */
    private void signalError() {
        Toolkit t = panel.getToolkit();
        t.beep();
    }
    
    /**
     * Display a win.
     */
    private void signalWin() {
        getRootPane().setDefaultButton(newButton);
        winMsg.setVisible(true);
        hitButton.setEnabled(false);
        stayButton.setEnabled(false);
        gameOver = true;
        totalWins++;
        totalGames++;
    }

    /**
     * Display a loss.
     */
    private void signalLoss() {
        getRootPane().setDefaultButton(newButton);
        lossMsg.setVisible(true);
        hitButton.setEnabled(false);
        stayButton.setEnabled(false);
        gameOver = true;
        totalGames++;
    }
    /**
     * Start a new game
     */
    private void signalNewGame(){
        board.newGame();
        hitButton.setEnabled(true);
        stayButton.setEnabled(true);
        getRootPane().setDefaultButton(hitButton);
        winMsg.setVisible(false);
        lossMsg.setVisible(false);
        gameOver = false;
    }
}