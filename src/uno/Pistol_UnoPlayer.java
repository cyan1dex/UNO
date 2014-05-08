
package uno;
import java.util.List;

public class Pistol_UnoPlayer implements UnoPlayer {

	// public enum Positions {Number, Draw2, Reverse, Skip, Wild, W4};

    public int play(List<Card> hand, Card upCard, UnoPlayer.Color calledColor, GameState state) {
        //Determine # of all colors in my hand.
        int totalRed = 0;
        int totalBlue = 0;
        int totalYellow = 0;
        int totalGreen = 0;
        int worry = 0;
        for (int i = 0; i < hand.size(); i++){
            switch (hand.get(i).getColor()){
                case RED:
                    totalRed++;
                    break;
                case BLUE:
                    totalBlue++;
                    break;
                case YELLOW:
                    totalYellow++;
                    break;
                case GREEN:
                    totalGreen++;
                    break;
                case NONE:
                    worry++;
                    break;
            }
        }
        //Count the number of cards in play.
        int ofGreen[] = new int[13];
        int ofRed[] = new int[13];
        int ofBlue[] = new int[13];
        int ofYellow[] = new int[13];
        int ofWild[] = new int[2];
		List<Card> playedCards = state.getPlayedCards();

        for (int i = 0; i < playedCards.size(); i++){
            if (playedCards.get(i).getColor() == UnoPlayer.Color.RED){
                if (playedCards.get(i).getNumber() != -1){
                    ofRed[playedCards.get(i).getNumber()]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.SKIP){
                    ofRed[10]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.REVERSE){
                    ofRed[11]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.DRAW_TWO){
                    ofRed[12]++;
                }
            }
            if (playedCards.get(i).getColor() == UnoPlayer.Color.GREEN){
                if (playedCards.get(i).getNumber() != -1){
                    ofGreen[playedCards.get(i).getNumber()]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.SKIP){
                    ofGreen[10]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.REVERSE){
                    ofGreen[11]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.DRAW_TWO){
                    ofGreen[12]++;
                }
            }
            if (playedCards.get(i).getColor() == UnoPlayer.Color.BLUE){
                if (playedCards.get(i).getNumber() != -1){
                    ofBlue[playedCards.get(i).getNumber()]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.SKIP){
                    ofBlue[10]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.REVERSE){
                    ofBlue[11]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.DRAW_TWO){
                    ofBlue[12]++;
                }
            }
            if (playedCards.get(i).getColor() == UnoPlayer.Color.YELLOW){
                if (playedCards.get(i).getNumber() != -1){
                    ofYellow[playedCards.get(i).getNumber()]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.SKIP){
                    ofYellow[10]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.REVERSE){
                    ofYellow[11]++;
                }
                if (playedCards.get(i).getRank() == UnoPlayer.Rank.DRAW_TWO){
                    ofYellow[12]++;
                }
            }
            if (playedCards.get(i).getRank() == UnoPlayer.Rank.WILD){
                ofWild[0]++;
            }
            if (playedCards.get(i).getRank() == UnoPlayer.Rank.WILD_D4){
                ofWild[1]++;
            }
        }

        //CALCULATE THE NUMBER OF UNKNOWABLE CARDS.
        int cardsUnknown = 0;
        cardsUnknown = 108 - (playedCards.size() + hand.size());

		UnoPlayer.Color upCardColor = upCard.getColor();
        //Handle the most recent called color.
        if (calledColor != UnoPlayer.Color.NONE){
            upCardColor = calledColor;
        }

        //Calculate the average number of cards in a hand.
        double averageHand = 0;
		int[] upcomingPlayerCards = state.getNumCardsInHandsOfUpcomingPlayers();
        for (int b = 0; b < upcomingPlayerCards.length; b++){
            averageHand += upcomingPlayerCards[b];
        }
        averageHand = averageHand / upcomingPlayerCards.length;
        
//-----------------------------------------------------------------------------
//..NUMBER CARDS...............................................................
//-----------------------------------------------------------------------------
        double highestCardRank = 0;
        int which = -100;
        double ratio = 0;
        for (int i = 0; i < hand.size(); i++){
            double currentCardRank = 0;
//Color is the same && card is a number
			Card current = hand.get(i);
            if ((current.getColor() == upCardColor) && (current.getRank() == UnoPlayer.Rank.NUMBER)){
                currentCardRank += (10 + current.getNumber());
                if (current.getNumber() != 0 && current.getNumber() != 1){
                    int b = current.getNumber();
                    currentCardRank -= ((8 - (ofRed[b] + ofBlue[b] + ofGreen[b] + ofYellow[b]))/2);
                }
                if (current.getNumber() == 0){
                    currentCardRank -= ((4 - (ofRed[0] + ofBlue[0] + ofGreen[0] + ofYellow[0]))/2);
                }
                switch (current.getColor()){
                case RED:
                    currentCardRank += (hand.size() / totalRed);
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case BLUE:
                    currentCardRank += (hand.size() / totalBlue);
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case GREEN:
                    currentCardRank += (hand.size() / totalGreen);
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case YELLOW:
                    currentCardRank += (hand.size() / totalYellow);
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                }
            }

//Number is the same, color is different
            if ((current.getNumber() != -1) && (current.getNumber() == upCard.getNumber())
                    && (current.getColor() != upCard.getColor())){
                currentCardRank += (10 + current.getNumber());
                if (current.getNumber() != 0 && current.getNumber() != 1){
                    int b = current.getNumber();
                    currentCardRank -= ((8 - (ofRed[b] + ofBlue[b] + ofGreen[b] + ofYellow[b]))/2);
                }
                if (current.getNumber() == 0){
                    currentCardRank -= ((4 - (ofRed[0] + ofBlue[0] + ofGreen[0] + ofYellow[0]))/2);
                }
                switch (current.getColor()){
                case RED:
                    currentCardRank += (hand.size() / totalRed);
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case BLUE:
                    currentCardRank += (hand.size() / totalBlue);
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case GREEN:
                    currentCardRank += (hand.size() / totalGreen);
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case YELLOW:
                    currentCardRank += (hand.size() / totalYellow);
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                }
            }

//-----------------------------------------------------------------------------
//..CARDS OF PAIN AND SUFFERING................................................
//-----------------------------------------------------------------------------

//Color is the same && card is a reverse
            if ((current.getRank() == UnoPlayer.Rank.REVERSE) && (current.getColor() == upCardColor)){
                currentCardRank += 15;
                if (upcomingPlayerCards[2] < averageHand){
                    currentCardRank+= 5;
                }
                currentCardRank -= ((8 - (ofRed[11] + ofBlue[11] + ofGreen[11] + ofYellow[11]))/2);
                switch (current.getColor()){
                case RED:
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    if (ratio != 0){
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalRed))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    }
                    break;
                case BLUE:                    
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalBlue))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    break;
                case GREEN:
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalGreen))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    break;
                case YELLOW:
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalYellow))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    break;
                }
            }
            
//Color is different && card is a reverse
            if ((current.getRank() == UnoPlayer.Rank.REVERSE) && (current.getRank() == upCard.getRank()) 
                    && (current.getColor() != upCard.getColor())){
                currentCardRank += 15;
                if (upcomingPlayerCards[2] < averageHand){
                    currentCardRank+= 5;
                }
                currentCardRank -= ((8 - (ofRed[11] + ofBlue[11] + ofGreen[11] + ofYellow[11]))/2);
                switch (current.getColor()){
                case RED:
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalRed))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    break;
                case BLUE:                    
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalBlue))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    break;
                case GREEN:
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalGreen))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    break;
                case YELLOW:
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalYellow))/2;
                    currentCardRank -= (upcomingPlayerCards[2] * ratio);
                    break;
                }
            }  
            
//Color is the same && card is a skip
            if ((current.getRank() == UnoPlayer.Rank.SKIP) && (current.getColor() == upCardColor)){
                currentCardRank += 15;
                if (upcomingPlayerCards[1] < averageHand){
                    currentCardRank+= 5;
                }
                currentCardRank -= ((8 - (ofRed[10] + ofBlue[10] + ofGreen[10] + ofYellow[10]))/2);
                switch (current.getColor()){
                case RED:
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalRed))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                case BLUE:                    
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalBlue))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                case GREEN:
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalGreen))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                case YELLOW:
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalYellow))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                }

            }
//Card is a skip && color is different
            if ((current.getRank() == UnoPlayer.Rank.SKIP) && (current.getRank() == upCard.getRank()) &&
                    (current.getColor() != upCard.getColor())){
                currentCardRank += 15;
                if (upcomingPlayerCards[1] < averageHand){
                    currentCardRank+= 5;
                }
                currentCardRank -= ((8 - (ofRed[10] + ofBlue[10] + ofGreen[10] + ofYellow[10]))/2);
                switch (current.getColor()){
                case RED:
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalRed))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                case BLUE:                    
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalBlue))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                case GREEN:
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalGreen))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                case YELLOW:
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank += ((upcomingPlayerCards[0] * ratio)+
                            (hand.size() / totalYellow))/2;
                    currentCardRank -= (upcomingPlayerCards[1] * ratio);
                    break;
                }

            }
//Color is the same & card is a D2
            if ((current.getRank() == UnoPlayer.Rank.DRAW_TWO) && (current.getColor() == upCardColor)){
                currentCardRank += (15 - (upcomingPlayerCards[0]/3));
                if (upcomingPlayerCards[0] < averageHand){
                    currentCardRank+= 5;
                }
                currentCardRank -= ((8 - (ofRed[12] + ofBlue[12] + ofGreen[12] + ofYellow[12]))/2);
                switch (current.getColor()){
                case RED:
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    currentCardRank += (hand.size() / totalRed);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case BLUE:
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank += (hand.size() / totalBlue);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case GREEN:
                    currentCardRank += (hand.size() / totalGreen);
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case YELLOW:
                    currentCardRank += (hand.size() / totalYellow);
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                }

            }            
//Card is a D2 && color is different
            if ((current.getRank() == UnoPlayer.Rank.DRAW_TWO) && (current.getRank() == upCard.getRank()) && 
                    (current.getColor() != upCard.getColor())){
                currentCardRank += (15 - (upcomingPlayerCards[0]/3));
                if (upcomingPlayerCards[0] < averageHand){
                    currentCardRank+= 5;
                }
                currentCardRank -= ((8 - (ofRed[12] + ofBlue[12] + ofGreen[12] + ofYellow[12]))/2);
                switch (current.getColor()){
                case RED:
                    currentCardRank += (hand.size() / totalRed);
                    ratio = colorRatio(ofRed, totalRed, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case BLUE:
                    currentCardRank += (hand.size() / totalBlue);
                    ratio = colorRatio(ofBlue, totalBlue, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case GREEN:
                    currentCardRank += (hand.size() / totalGreen);
                    ratio = colorRatio(ofGreen, totalGreen, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                case YELLOW:
                    currentCardRank += (hand.size() / totalYellow);
                    ratio = colorRatio(ofYellow, totalYellow, cardsUnknown);
                    currentCardRank -= (upcomingPlayerCards[0] * ratio);
                    break;
                }

            }            
//Hello World! I mean, uh, Wild!
            if (current.getRank() == UnoPlayer.Rank.WILD){
                currentCardRank += 6;
                currentCardRank -= (4 - ofWild[0])/2;
            }
            if (current.getRank() == UnoPlayer.Rank.WILD_D4){
                currentCardRank += 5;
                if (upcomingPlayerCards[0] < averageHand){
                    currentCardRank+= 5;
                    currentCardRank -= (4 - ofWild[1])/2;
                }
            }
//Determine if current card is better than the best card found thus far
            if (currentCardRank > highestCardRank){
                highestCardRank = currentCardRank;
                which = i;
            }
        
    } //end for loop
if (which == -100){
    return -1;
}
 else
     return which;
} //end method

    private static double colorRatio(int ofColor[], int myTotalColor, int cardsUnknown){
        double totalPlayed = 0;
        double ratio = 0.00;
        //Count the number of cards of that color that have been played.
        for (int i = 0; i < ofColor.length; i++){
            totalPlayed += ofColor[i];
        }
        //Find the number remaining in every color and divide it by the number of
        //cards remaining?
        totalPlayed = 25 - (totalPlayed + myTotalColor);
        ratio = (totalPlayed / cardsUnknown);

        return ratio;
    }

    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * the value Color.NONE under any circumstances.
     */
    public UnoPlayer.Color callColor(List<Card> hand) {

        double bestColor = 0;
        double currentColor;
        UnoPlayer.Color which = UnoPlayer.Color.NONE;

        int totalRed = 0;
        int totalBlue = 0;
        int totalYellow = 0;
        int totalGreen = 0;
        int totalPain = 0;
        for (int i = 0; i < hand.size(); i++){
            switch (hand.get(i).getColor()){
                case RED:
                    totalRed++;
                    break;
                case BLUE:
                    totalBlue++;
                    break;
                case YELLOW:
                    totalYellow++;
                    break;
                case GREEN:
                    totalGreen++;
                    break;
                case NONE:
                    totalPain++;
                    break;
            }
        }
        int compareColor[] = {totalRed, totalBlue, totalYellow, totalGreen};
        for (int i = 0; i < compareColor.length; i++){
            if (compareColor[i] > bestColor){
                bestColor = compareColor[i];
            }
        }
        if (bestColor == totalRed){
            return UnoPlayer.Color.RED;
        }
        if (bestColor == totalGreen){
            return UnoPlayer.Color.GREEN;
        }
        if (bestColor == totalBlue){
            return UnoPlayer.Color.BLUE;
        }
        if (bestColor == totalYellow){
            return UnoPlayer.Color.YELLOW;
        }
        return null;
    }

}
