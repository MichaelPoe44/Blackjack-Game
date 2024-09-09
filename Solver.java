



class Solver{

    
    static String Hit = "Hit";
    static String Stand = "Stand";
    static String Bust = "Bust";
    static String Blackjack = "Blackjack!";
    static String Split = "Split";
    static String DoubleH = "Double if possible, otherwise hit";
    static String DoubleS = "Double if possible, otherwise stand";
    static String SplitD = "Split if you can double down after split, otherwise hit";



    


    public String process(int dealer, int[] player){
        int total = 0;
        int sameValue = 0;
        int nonAceCard = 0;
        // looks if player has an ace
        boolean hasAce = (player[0] == 1 || player[1] == 1);
        // looks if player has same value cards
        boolean isSame = (player[0] == player[1]);


        if (isSame){
            sameValue = player[0];
            if (sameValue == 8 || sameValue == 1){
                return Split;
            }
            else if (sameValue == 10){
                return Stand;
            }
        }
    

        else if (hasAce){

            if (player[0] == 1){
                nonAceCard = player[1];
            }
            else if (player[1] == 1){
                nonAceCard = player[0];
            }

            if (nonAceCard == 10) return Blackjack;
            else if (nonAceCard == 8) return Stand;
            
        }

        else{
            // total only gets set to a number if there is no ace
            total = player[0] + player[1];
            if (total <= 8 && total > 0 ){
                return Hit;
            }
            else if (total == 11){
                return DoubleH;
            }
            else if(total == 17){
                return Stand;
            }

        }
        // make the hitting and other stuff into functions

        switch (dealer){
            case 1:

                if (isSame){
                    if (sameValue == 9){
                        return Stand;
                    }
                    else return Hit;
                }
                
                else if (hasAce){
                    return Hit;
                }
                
                else {
                    return Hit;
                }
                    
        
            case 2:
                if (isSame){
                    if (sameValue == 2 || sameValue == 3 || sameValue == 6){
                        return SplitD;
                    }
                    else if (sameValue == 4){
                        return Hit;
                    }
                    else if (sameValue == 5){
                        return DoubleH;
                    }
                    else {
                        return Split;
                    }

                }
                
                else if (hasAce){
                    if (nonAceCard == 7){
                        return Stand;
                    }
                    else{
                        return Hit;
                    }
                }

                else{
                    if (total == 9 || total == 12){
                        return Hit;
                    }
                    else if (total == 10){
                        return DoubleH;
                    }
                    else{
                        return Stand;
                    }

                }

            case 3:

                if (isSame){
                    if (sameValue == 2 || sameValue == 3){
                        return SplitD;
                    }
                    else if (sameValue == 4){
                        return Hit;
                    }
                    else if (sameValue == 5){
                        return DoubleH;
                    }
                    else {
                        return Split;
                    }
                }
                
                else if (hasAce){
                    if (nonAceCard == 6){
                        return DoubleH;
                    }
                    else if (nonAceCard == 7){
                        return DoubleS;
                    }
                    else {
                        return Hit;
                    }
                }

                else {
                    if (total == 9 || total == 10){
                        return DoubleH;
                    }
                    else if (total == 12){
                        return Hit;
                    }
                    else {
                        return Stand;
                    }
                }


            case 4:

                if (isSame) {
                    if (sameValue == 4){
                        return Hit;
                    }
                    else if (sameValue == 5){
                        return DoubleH;
                    }
                    else {
                        return Split;
                    }
                }

                else if (hasAce){
                    if (nonAceCard == 2 || nonAceCard == 3){
                        return Hit;
                    }
                    else if (nonAceCard == 7){
                        return DoubleS;
                    }
                    else {
                        return DoubleH;
                    }
                }

                else {
                    if (total == 9 || total == 10){
                        return DoubleH;
                    }
                    else {
                        return Stand;
                    }
                }

            case 5:
                
                if (isSame){
                    if (sameValue == 4){
                        return SplitD;
                    }
                    else if (sameValue == 5){
                        return DoubleH;
                    }
                    else {
                        return Split;
                    }
                }

                else if (hasAce){
                    if (nonAceCard == 7){
                        return DoubleS;
                    }
                    else {
                        return DoubleH;
                    }
                }

                else {
                    if (total == 9 || total == 10){
                        return DoubleH;
                    }
                    else {
                        return Stand;
                    }
                }

            case 6:
                if (isSame){
                    if (sameValue == 4){
                        return SplitD;
                    }
                    else if (sameValue == 5){
                        return DoubleH;
                    }
                    else {
                        return Split;
                    }
                }
            
                else if (hasAce){
                    if (nonAceCard == 7){
                        return DoubleS;
                    }
                    else {
                        return DoubleH;
                    }
                }

                else {
                    if (total == 9 || total == 10){
                        return DoubleH;
                    }
                    else {
                        return Stand;
                    }
                }

            case 7:
                if (isSame){
                    if (sameValue == 4 || sameValue == 6){
                        return Hit; 
                    }
                    else if (sameValue == 5){
                        return DoubleH;
                    }
                    else if (sameValue == 9){
                        return Stand;
                    }
                    else {
                        return Split;
                    }
                    
                }
                
                else if (hasAce){
                    if (nonAceCard == 7){
                        return Stand;
                    }
                    else {
                        return Hit;
                    }
                }

                else {
                    if (total == 10){
                        return DoubleH;
                    }
                    else {
                        return Hit;
                    }
                }

            case 8:
                if (isSame){
                    if (sameValue == 5){
                        return DoubleH;
                    }
                    else if (sameValue == 9){
                        return Split;
                    }
                    else {
                        return Hit;
                    }
                }

                else if (hasAce){
                    if (nonAceCard == 7){
                        return Stand;
                    }
                    else {
                        return Hit;
                    }
                }

                else {
                    if (total == 10){
                        return DoubleH;
                    }
                    else {
                        return Hit;
                    }
                }

            case 9:
               if (isSame){
                    if (sameValue == 5){
                        return DoubleH;
                    }
                    else if (sameValue == 9){
                        return Split;
                    }
                    else {
                        return Hit;
                    }
                }

                else if (hasAce){
                    return Hit;
                }

                else {
                    if (total == 10){
                        return DoubleH;
                    }
                    else {
                        return Hit;
                    }
                }
            
            case 10:
                if (isSame){
                    if (sameValue == 9){
                        return Stand;
                    }
                    else {
                        return Hit;
                    }
                }

                else if (hasAce){
                    return Hit;
                }

                else {
                    return Hit;
                }
        }

        return "";
    }


}



