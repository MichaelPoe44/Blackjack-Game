import java.util.Random;
import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;


import javax.swing.*;

class Go{
    public static void main(String[] args){
        Blackjack blackjack = new Blackjack();
    }

}

/* TODO
 * add betting
 * money handling
 * add user tracking
 * add split funciton
 * add hints
 * 
 * 
 * 
 */



class Blackjack {

    private class Card{
        String face;
        String suit;

        Card(String face, String suit){
            this.suit = suit;
            this.face = face;
        }

        public int getValue(){
            if ("ATJQK".contains(face)){
                if (face == "A"){
                    return 11;
                }
                return 10;
            }
            return Integer.parseInt(face);
        }

        public boolean isAce(){
            return face == "A";
        }

        public String toString(){
            return face + "-" + suit;
        }

        public String getImagePath(){
            return "./Cards/" + face + "_of_" + suit + ".png";
        }

    }

    // Game states
    private enum STATE{
        GAME,
        MENU
    }
    STATE state = STATE.MENU;

    // game
    ArrayList<Card> deck;
    Random random = new Random();
    int playerBet = 0;

    //make player
    Player user = new Player("Michael Poe", 600);

    //dealer
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;
    Card hiddenCard;

    // player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;

    // game frame
    JFrame frame = new JFrame("BlackJack");
    int boardSize = 700;
    int cardHeight = 154;
    int cardWidth = 110;
    JPanel gamePanel = new JPanel() {
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            try {
                //draw hidden card
                Image hiddenCardImage = new ImageIcon(getClass().getResource("./Cards/back.png")).getImage();
                if (!standButton.isEnabled()){
                    hiddenCardImage = new ImageIcon(getClass().getResource(hiddenCard.getImagePath())).getImage();
                }
                g.drawImage(hiddenCardImage, 20, 70, cardWidth, cardHeight, null);
                
                //draw dealers hand
                for (int i = 0; i < dealerHand.size(); i++){
                    Card card = dealerHand.get(i);
                    Image CardIMG = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(CardIMG, cardWidth + 25 + (cardWidth + 5)*i, 70, cardWidth, cardHeight, null);
                }

                //draw players hand
                for (int i=0; i < playerHand.size(); i++){
                    Card card = playerHand.get(i);
                    Image CardIMG = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                    g.drawImage(CardIMG, 20 + (cardWidth + 5)*i, 370, cardWidth, cardHeight, null);
                }
                String sum = String.valueOf(reducePlayerAce());
                g.setFont(new Font("Arial",Font.PLAIN, 30));
                g.setColor(Color.WHITE);
                g.drawString(sum,20,565);

                
                // draw outcome
                if (!standButton.isEnabled()){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();

                    String message = "";
                    if (playerSum > 21){
                        message = "Player Bust!";
                    }
                    else if (dealerSum > 21){
                        message = "You Win";
                    }
                    else if (playerSum == dealerSum){
                        message = "Tie";
                    }
                    else if (dealerSum < playerSum){
                        message = "You Win";
                    }
                    else if (playerSum < dealerSum){
                        message = "You Lose";
                    }

                    sum = String.valueOf(reduceDealerAce());
                    g.drawString(message,220,300);
                    g.drawString(sum,20, 265);
                    
                }


            } catch (Exception e){ 
                e.printStackTrace();
            }
        }
    };

    JPanel bettingPanel = new JPanel(){
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            try{
                g.setColor(Color.WHITE);
                g.setFont(new Font("Arial",Font.PLAIN, 15));
                g.drawString(user.Username, 5,15);
                g.setFont(new Font("Arial",Font.PLAIN, 30));
                g.drawString("Your Bank:    "+String.valueOf(user.money), 50, 100);
                g.drawString("Bet Amount:   "+String.valueOf(playerBet),50,200);




            }
            catch (Exception e){
                e.printStackTrace();
            }


        }
    };


    //game buttons/ panels
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton standButton = new JButton("Stand");
    JButton doubleButton = new JButton("Double");
    JPanel userPanel = new JPanel();
    JLabel nameLabel = new JLabel(user.Username);
    JLabel moneyLabel = new JLabel("$"+String.valueOf(user.money));

    //menu buttons/ panels
    JButton oneButton = new JButton("1");
    JButton fiveButton = new JButton("5");
    JButton tenButton = new JButton("10");
    JButton twentyFiveButton = new JButton("25");
    JButton oneHundredButton = new JButton("100");


    public Blackjack(){
        
        
        startGame();  
        makeFrame();
        frame.setVisible(true);
    }


    
    public void makeFrame(){

        frame.setSize(boardSize,boardSize);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        if (state == STATE.GAME){
            gamePanel.setLayout(new BorderLayout());
            gamePanel.setBackground(new Color(53,101,77));
            frame.add(gamePanel);

            hitButton.setFocusable(false);
            buttonPanel.add(hitButton);
            standButton.setFocusable(false);
            buttonPanel.add(standButton);
            doubleButton.setFocusable(false);
            buttonPanel.add(doubleButton);
            frame.add(buttonPanel, BorderLayout.SOUTH);

            hitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    Card card = deck.removeLast();
                    playerSum += card.getValue();
                    playerAceCount += card.isAce() ? 1 : 0;
                    playerHand.add(card);
                    gamePanel.repaint();
                    if (reducePlayerAce() >=  21){
                        endGame();
                    }
                }
            });

            doubleButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    endGame();

                    Card card = deck.removeLast();
                    playerSum += card.getValue();
                    playerAceCount += card.isAce()? 1 : 0;
                    playerHand.add(card);
                    gamePanel.repaint();
                    
                }
            });

            standButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    endGame();

                    gamePanel.repaint();
                }
            });
            gamePanel.repaint();


            userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.Y_AXIS));
            userPanel.add(nameLabel);
            userPanel.add(moneyLabel);
            frame.add(userPanel,BorderLayout.EAST);
        }

        else if (state == STATE.MENU){
            bettingPanel.setBackground(new Color(53,101,77));
            frame.add(bettingPanel);

            oneButton.setFocusable(false);
            oneButton.setBounds(20,600,50,50);
            bettingPanel.add(oneButton);



        }
    }











    public void startGame(){
        buildDeck();
        shuffleDeck();

        //dealer
        dealerHand = new ArrayList<Card>();
        dealerSum = 0;
        dealerAceCount = 0;

        hiddenCard = deck.removeLast();
        dealerSum += hiddenCard.getValue();
        dealerAceCount += hiddenCard.isAce() ? 1 : 0;

        Card card = deck.removeLast();
        dealerSum += card.getValue();
        dealerAceCount += card.isAce() ? 1 : 0;
        dealerHand.add(card);

    
        //player
        playerHand = new ArrayList<Card>();
        playerAceCount = 0;
        playerSum = 0;

        for (int i=0; i < 2; i++){
            card = deck.removeLast();
            playerSum += card.getValue();
            playerAceCount += card.isAce() ? 1 : 0;
            playerHand.add(card);
        }

    }

    public void buildDeck(){
        String[] faces = {"A","2","3","4","5","6","7","8","9","T","J","Q","K"};
        String[] suits = {"clubs","diamonds","spades","hearts"};
        deck = new ArrayList<Card>();
        for (int i=0; i < suits.length; i++){
            for (int j=0; j < faces.length; j++){
                Card card = new Card(faces[j],suits[i]);
                deck.add(card);
            }
        }

    }


    public void shuffleDeck(){
        for (int i=0; i < deck.size(); i++){
            int x = random.nextInt(deck.size());
            Card currentCard = deck.get(i);
            Card randomCard = deck.get(x);

            deck.set(i, randomCard);
            deck.set(x, currentCard);
            
        }

    }

    
    public int reducePlayerAce(){
        while (playerSum > 21 && playerAceCount > 0){
            playerSum -= 10;
            playerAceCount -= 1;
        }
        return playerSum;
    }


    public int reduceDealerAce(){
        while (dealerSum > 21 && dealerAceCount > 0){
            dealerSum -= 10;
            dealerAceCount -= 1;
        }
        return dealerSum;
    }

    public void endGame(){
        hitButton.setEnabled(false);
        standButton.setEnabled(false);
        doubleButton.setEnabled(false);
        while (dealerSum < 17){
            Card card = deck.removeLast();
            dealerSum += card.getValue();
            dealerAceCount += card.isAce() ? 1 : 0;
            dealerHand.add(card);
        }
    }


}












