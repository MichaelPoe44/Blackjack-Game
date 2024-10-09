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
 * add betting//
 * money handling//
 * add user tracking//
 * Add login screen oops
 * add split funciton
 * add hints//
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

        public int getFace(){
            if (face.equals("A")){
                return 1;
            }
            else if ("TJQK".contains(face)){
                return 10;
            }
            return Integer.valueOf(face);

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

    // data holder
    Data data;

    // game
    ArrayList<Card> deck;
    Random random = new Random();
    int playerBet = 0;

    //make player
    Player user = new Player("Michael");

    //make hint solver
    Solver hint = new Solver();
    String hintString;

    //dealer
    ArrayList<Card> dealerHand;
    int dealerSum;
    int dealerAceCount;
    Card hiddenCard;

    // player
    ArrayList<Card> playerHand;
    int playerSum;
    int playerAceCount;
    ArrayList<Card> secondHand;
    boolean playingSecondHand;
    //int secondPlayerSum;
    //int SecondPlayerAceCount;

    // game frame
    JFrame frame = new JFrame("BlackJack");
    int boardSize = 700;
    int cardHeight = 154; // ratio is height is 1.4 times width
    int cardWidth = 110;
    int splitCardHeight = 98;
    int splitCardWidth = 70;

    // main panel to use card layout
    JPanel mainPanel;
    CardLayout cardLayout = new CardLayout();




    // game and bet panel creating
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

                String sum;
                //draw players hand if no split
                if (secondHand.isEmpty()){

                    
                    for (int i=0; i < playerHand.size(); i++){
                        Card card = playerHand.get(i);
                        Image CardIMG = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                        g.drawImage(CardIMG, 20 + (cardWidth + 5)*i, 370, cardWidth, cardHeight, null);
                    }
                    //draw players total
                    sum = String.valueOf(reducePlayerAce());
                    g.setFont(new Font("Arial",Font.PLAIN, 30));
                    g.setColor(Color.WHITE);
                    g.drawString(sum,20,565);
                }

                else if (!secondHand.isEmpty()){
                    for (int i=0; i < playerHand.size(); i++){
                        Card card = playerHand.get(i);
                        Image CardIMG = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                        g.drawImage(CardIMG, 20 + (splitCardWidth + 5)*i, 320, splitCardWidth, splitCardHeight, null);
                    }

                    for (int i=0; i < secondHand.size(); i++){
                        Card card = secondHand.get(i);
                        Image CardIMG = new ImageIcon(getClass().getResource(card.getImagePath())).getImage();
                        g.drawImage(CardIMG, 20 + (splitCardWidth + 5)*i, 430, splitCardWidth, splitCardHeight, null);
                    }
                    //draw players total
                    sum = String.valueOf(reducePlayerAce());
                    g.setFont(new Font("Arial",Font.PLAIN, 30));
                    g.setColor(Color.WHITE);
                    g.drawString("Top hand:", 20, 565);
                    g.drawString(">",0,380);
                    g.drawString(sum,170,565);
                }


                //draw hint
                if (!hintString.isEmpty()){
                    g.drawString(hintString,200,565);
                    hintString = "";
                }
                // draw outcome
                if (!standButton.isEnabled()){
                    dealerSum = reduceDealerAce();
                    playerSum = reducePlayerAce();

                    String message = "";
                    if (playerSum > 21){
                        message = "Player Bust!";
                        results(false,false);
                    }
                    else if (dealerSum > 21){
                        message = "You Win";
                        results(true,false);
                    }
                    else if (playerSum == dealerSum){
                        message = "Tie";
                        results(false,true);
                    }
                    else if (dealerSum < playerSum){
                        message = "You Win";
                        results(true,false);
                    }
                    else if (playerSum < dealerSum){
                        message = "You Lose";
                        results(false,false);
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


    //game buttons/ panels/ labels
    JPanel buttonPanel = new JPanel();
    JButton hitButton = new JButton("Hit");
    JButton standButton = new JButton("Stand");
    JButton doubleButton = new JButton("Double");
    JButton playAgainButton = new JButton("Play Again");
    JButton hintButton = new JButton("Hint");
    JButton splitButton = new JButton("Split");
    JPanel userPanel = new JPanel();
    JLabel nameLabel = new JLabel(user.Username);
    JLabel moneyLabel = new JLabel("Bank: $"+String.valueOf(user.money));
    JLabel currentBet = new JLabel("Current Bet: $"+String.valueOf(playerBet));

    //menu buttons/ panels
    JButton playButton = new JButton("Play");
    JButton resetButton = new JButton("reset");
    JButton oneButton = new JButton("$1");
    JButton fiveButton = new JButton("$5");
    JButton tenButton = new JButton("$10");
    JButton twentyFiveButton = new JButton("$25");
    JButton oneHundredButton = new JButton("$100");
    ActionListener betListener = new ActionListener() {
        public void actionPerformed(ActionEvent e){
            
            int value = Integer.valueOf(e.getActionCommand());
            if ((playerBet + value) <= user.money){
                playerBet += value;
            }   
            bettingPanel.repaint();
        }
    };

    //////// constructor//////////////////////////
    public Blackjack(){
        
        makeFile();
        startGame();  
        makeFrame();
        frame.setVisible(true);
    }


    public void makeFile(){
        data = new Data(user);
    }
    
    public void makeFrame(){

        frame.setSize(boardSize,boardSize);
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                data.endGame(user);
            }
        });
        
        
        mainPanel = new JPanel(cardLayout);
        frame.add(mainPanel);

        makeGamePanel();
        makeBettingPanel();
        
        cardLayout.show(mainPanel, "betting");
        
    }


    public void makeGamePanel(){
        gamePanel.setLayout(new BorderLayout());
        gamePanel.setBackground(new Color(53,101,77));
        mainPanel.add(gamePanel, "game");
        

        hitButton.setFocusable(false);
        hintButton.setFocusable(false);
        standButton.setFocusable(false);
        doubleButton.setFocusable(false);

        buttonPanel.add(hintButton);
        buttonPanel.add(hitButton);
        buttonPanel.add(standButton);
        buttonPanel.add(doubleButton);
        buttonPanel.add(splitButton);
        buttonPanel.add(playAgainButton);
        playAgainButton.setVisible(false);
        gamePanel.add(buttonPanel, BorderLayout.SOUTH);


        hitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                Card card = deck.removeLast();
                playerSum += card.getValue();
                playerAceCount += card.isAce() ? 1 : 0;
                playerHand.add(card);
                gamePanel.repaint();
                hintButton.setEnabled(false);
                splitButton.setEnabled(false);
                
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
                    if (!secondHand.isEmpty() && !playingSecondHand){
                        //switch player hand and secondary hand
                        playingSecondHand = true;
                    }
                    else {
                        endGame();
                    }
                    

                    gamePanel.repaint();
                }
            });
            
            playAgainButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    hitButton.setEnabled(true);
                    standButton.setEnabled(true);
                    doubleButton.setEnabled(true);
                    hintButton.setEnabled(true);
                    splitButton.setEnabled(true);
                    playAgainButton.setVisible(false);
                    if (!secondHand.isEmpty()){
                        secondHand.clear();
                    }
                    startGame();
                    gamePanel.repaint();
                    cardLayout.show(mainPanel, "betting");
                    
                }
            });
            
            hintButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){
                    int dealer = dealerHand.get(0).getFace();
                    int[] player = {playerHand.get(0).getFace(), playerHand.get(1).getFace()};
                    hintString = hint.process(dealer, player);
                    /////
                    System.out.println(String.format("D: %d", dealer));
                    System.out.println(player[0]);
                    System.out.println(player[1]);
                    hintButton.setEnabled(false);
                    gamePanel.repaint();
                }
            });

            splitButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e){

                    
                    Card card = playerHand.removeLast();
                    playerSum -= card.getValue();
                    playerAceCount -= card.isAce()? 1 : 0;
                    secondHand.add(card);

                    card = deck.getLast();
                    playerSum += card.getValue();
                    playerAceCount += card.isAce()? 1 : 0;
                    playerHand.add(card);

                    card = deck.getLast();
                    //playerSum += card.getValue;
                    //playerAceCount += card.isAce()? 1 : 0;
                    secondHand.add(card);

                    splitButton.setEnabled(false);
                    gamePanel.repaint();
                }
            });




            userPanel.setLayout(new BoxLayout(userPanel,BoxLayout.Y_AXIS));
            userPanel.add(nameLabel);
            userPanel.add(moneyLabel);
            userPanel.add(currentBet);
            gamePanel.add(userPanel,BorderLayout.EAST);
            gamePanel.repaint();
    }



    public void makeBettingPanel(){
        bettingPanel.setBackground(new Color(53,101,77));
        bettingPanel.setLayout(null);
        mainPanel.add(bettingPanel, "betting");

        playButton.setFocusable(false);
        playButton.setBounds(500,150,100,100);
        playButton.setFont(new Font("Arial",Font.PLAIN, 35));
        playButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                betMade();
                if ((playerBet * 2) > user.money){
                    doubleButton.setEnabled(false);
                }
                cardLayout.show(mainPanel,"game");

            }
        });
        bettingPanel.add(playButton);

        resetButton.setFocusable(false);
        resetButton.setBounds(350,450,70,70);
        bettingPanel.add(resetButton);
        resetButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e){
                playerBet = 0;
                bettingPanel.repaint();
            }
        });


        oneButton.setFocusable(false);
        oneButton.setBounds(150,350, 70,70);
        bettingPanel.add(oneButton);

        fiveButton.setFocusable(false);
        fiveButton.setBounds(250,350,70,70);
        bettingPanel.add(fiveButton);

        tenButton.setFocusable(false);
        tenButton.setBounds(350,350,70,70);
        bettingPanel.add(tenButton);

        twentyFiveButton.setFocusable(false);
        twentyFiveButton.setBounds(150,450,70,70);
        bettingPanel.add(twentyFiveButton);

        oneHundredButton.setFocusable(false);
        oneHundredButton.setBounds(250,450,70,70);
        bettingPanel.add(oneHundredButton);

        oneButton.addActionListener(betListener);
        fiveButton.addActionListener(betListener);
        tenButton.addActionListener(betListener);
        twentyFiveButton.addActionListener(betListener);
        oneHundredButton.addActionListener(betListener);

        oneButton.setActionCommand("1");
        fiveButton.setActionCommand("5");
        tenButton.setActionCommand("10");
        twentyFiveButton.setActionCommand("25");
        oneHundredButton.setActionCommand("100");
    }






    public void startGame(){
        
        buildDeck();
        shuffleDeck();
        hintString = "";

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

        //for if a split occurs 
        secondHand = new ArrayList<Card>();
        playingSecondHand = false;

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
        hintButton.setEnabled(false);
        splitButton.setEnabled(false);
        while (dealerSum < 17){
            Card card = deck.removeLast();
            dealerSum += card.getValue();
            dealerAceCount += card.isAce() ? 1 : 0;
            dealerHand.add(card);
        }
    }

    public void handleMoneyLables(){
        moneyLabel.setText("Bank: $"+String.valueOf(user.money));
        currentBet.setText("Current Bet: $"+String.valueOf(playerBet));
    }

    public void betMade(){
        user.money -= playerBet;
        handleMoneyLables();

    }

    public void results(boolean win, boolean tie){
        
        if (tie){
            user.money += playerBet;
        }
        else if (win){
            user.money = user.money + (playerBet * 2);
        }

        if (secondHand == null){
            System.out.println("no second hand detected");
            playerBet = 0;
        }
        
        
        handleMoneyLables();
        playAgainButton.setVisible(true);
    
    }



    
}












