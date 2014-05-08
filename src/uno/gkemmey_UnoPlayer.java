package uno;

import java.util.List;
import java.util.ArrayList;

public class gkemmey_UnoPlayer implements UnoPlayer {

    public int play(List<Card> hand, Card upCard, Color calledColor, GameState state) {
        ArrayList<Card> playable = new ArrayList<Card>();
        for(int i = 0; i < hand.size(); i++) {
            if(hand.get(i).canPlayOn(upCard, calledColor)) {
                playable.add(hand.get(i));
            }
        }

        if(playable.isEmpty()) {
            return -1;
        }
        else {
            int[] weights = new int[playable.size()];

            //fill with 0s
            for(int i = 0; i < weights.length; i++) {
                weights[i] = 0;
            }

            calculateCardWeights(hand, upCard, calledColor, state, playable, weights);

            int index = 0;
            for(int i = 1; i < weights.length; i++) {
                if(weights[i] > weights[index]) {
                    index = i;
                }
            }

            return hand.indexOf(playable.get(index));
        }
    }

    public Color callColor(List<Card> hand) {
        int[] colors = {0, 0, 0, 0}; //green, blue, red, yellow
        int[] points = {0, 0, 0, 0};

        calculateColorIHaveTheMostOfAndPointsInEachColor(hand, colors, points);
        
        int index = 0;
        for(int i = 1; i < colors.length; i++) {
            if(colors[i] > colors[index]) {
                index = i;
            }
            else if(colors[i] == colors[index]) {
                if(points[i] > points[index]) {
                    index = i;
                }
            }
        }

        switch(index) {
            case 0: {
                return UnoPlayer.Color.GREEN;
            }
            case 1: {
                return UnoPlayer.Color.BLUE;
            }
            case 2: {
                return UnoPlayer.Color.RED;
            }
            case 3: {
                return UnoPlayer.Color.YELLOW;
            }
            default: {
                return UnoPlayer.Color.GREEN;
            }
        }
    }

//-------------------------------------HELPER METHODS------------------------------
    private void calculateCardWeights(List<Card> hand, Card upCard, Color calledColor, GameState state, ArrayList<Card> playable, int[] weights){
        int numWildD4 = calculateNumberOfCardTypeInHand(hand, UnoPlayer.Rank.WILD_D4);
        int numWild = calculateNumberOfCardTypeInHand(hand, UnoPlayer.Rank.WILD);
        int numD2 = calculateNumberOfCardTypeInHand(hand, UnoPlayer.Rank.DRAW_TWO);
        int numSkip = calculateNumberOfCardTypeInHand(hand, UnoPlayer.Rank.SKIP);
        int numReverse = calculateNumberOfCardTypeInHand(hand, UnoPlayer.Rank.REVERSE);

        int numGreen = calculateNumberOfColorInHand(hand, UnoPlayer.Color.GREEN);
        int numBlue = calculateNumberOfColorInHand(hand, UnoPlayer.Color.BLUE);
        int numRed = calculateNumberOfColorInHand(hand, UnoPlayer.Color.RED);
        int numYellow = calculateNumberOfColorInHand(hand, UnoPlayer.Color.YELLOW);

        int smallestHandSize = calculateHandSizeOfOtherPlayer(state, "least");
        int numPlayable = playable.size();

        int numPlayableReverse = calculateNumberOfPlayableCardType(playable, UnoPlayer.Rank.REVERSE);
        int numPlayableSkip = calculateNumberOfPlayableCardType(playable, UnoPlayer.Rank.SKIP);
        int numPlayableNumber = calculateNumberOfPlayableCardType(playable, UnoPlayer.Rank.NUMBER);

        //the card weights i just kind of guessed at with a some testing and tinkering back in forth
        for(int i = 0; i < weights.length; i++){
            if(playable.get(i).getRank() == UnoPlayer.Rank.WILD_D4){
                if(smallestHandSize == numWildD4 + numWild && numWild == 0){ //we want to play this if the smallest hand size = the number of wilds, but not if we have a regular wild because other players with more cards means more ways to mess with me
                    weights[i] += 15;
                }
                if(state.getNumCardsInHandsOfUpcomingPlayers()[0] <= 2 && numD2 == 0){ //we want to force someone who has almost won to draw 4
                    weights[i] += 30;
                }
            }
            else if(playable.get(i).getRank() == UnoPlayer.Rank.WILD){
                if(smallestHandSize == numWildD4 + numWild){ //really want to play this if smallest hand size = number of wilds i have
                    weights[i] += 30;
                }
            }
            else if(playable.get(i).getRank() == UnoPlayer.Rank.SKIP){
                weights[i] += 9; //this card is good at any time and at 20 pts lets give it a chance to go right from the get go
                if(state.getNumCardsInHandsOfUpcomingPlayers()[0] <= 3){
                    weights[i] += 30; //if someone is getting low lets make sure they are skipped
                }
                if(state.getNumCardsInHandsOfUpcomingPlayers()[1] == 0){ //if we don't have to we don't want to skip someone if the guy after him has one card left
                    weights[i] = 0;
                }
            }
            else if(playable.get(i).getRank() == UnoPlayer.Rank.DRAW_TWO){
                if(state.getNumCardsInHandsOfUpcomingPlayers()[0] <= 2){ //just a good chance if guy has small hand size
                    weights[i] += 21;
                }
            }
            else if(playable.get(i).getRank() == UnoPlayer.Rank.REVERSE){
                weights[i] += 9; //this is kind of a screwy card and at 20pts lets give it a chance to just be played if it can regardless
                if(state.getNumCardsInHandsOfUpcomingPlayers()[0] <=2){ //we really wany yo play it if the guy to go next doesen't have a lot of cards
                    weights[i] += 9;
                }
                if(state.getNumCardsInHandsOfUpcomingPlayers()[2] == 1){
                    weights[i] = 0; //we don't want to send it back if the guy who went before me has 1 card in his hand
                }
            }
            else if(playable.get(i).getRank() == UnoPlayer.Rank.NUMBER){
                switch(playable.get(i).getNumber()){
                    case 0: weights[i] += 0; break; //i tested the idea of giving 0 high points because it keeps color or giving it no points because its a card you can get stuck with with no real loss--giving it no points proved more effective
                    case 9: weights[i] += 9; break; //weighted numbers with their actual number--we want to ditch high point cards first
                    case 8: weights[i] += 8; break;
                    case 7: weights[i] += 7; break;
                    case 6: weights[i] += 6; break;
                    case 5: weights[i] += 5; break;
                    case 4: weights[i] += 4; break;
                    case 3: weights[i] += 3; break;
                    case 2: weights[i] += 2; break;
                    case 1: weights[i] += 1; break;
                    default: weights[i] += 0;
                }
                switch(playable.get(i).getColor()){
                    case GREEN:
                        if(numGreen == 1){
                            weights[i] += 3; //if this is our last card of Color X lets drop that so we can be done with that color
                        }
                        else{
                            weights[i] += numGreen; break;
                        }
                    case BLUE:
                        if(numBlue == 1){
                            weights[i] += 3;
                        }
                        else{
                            weights[i] += numBlue; break;
                        }
                    case RED:
                        if(numRed == 1){
                            weights[i] += 3;
                        }
                        else{
                            weights[i] += numRed; break;
                        }
                    case YELLOW:
                        if(numYellow == 1){
                            weights[i] += 3;
                        }
                        else{
                            weights[i] += numYellow; break;
                        }
                    default: weights[i] += 0;
                }
                if(playable.get(i).getColor() == calledColor){ //cant take away three points if its less than three
                    weights[i] -= 3; //lets avoid keeping a color if someone just called it
                }
            }
        }
    }


    private int calculateNumberOfCardTypeInHand(List<Card> hand, UnoPlayer.Rank rank) {
        int total = 0;
        for(int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getRank() == rank) {
                total++;
            }
        }

        return total;
    }


    private int calculateNumberOfColorInHand(List<Card> hand, UnoPlayer.Color color) {
        int total = 0;
        for(int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getColor() == color) {
                total++;
            }
        }

        return total;
    }


    private int calculateHandSizeOfOtherPlayer(GameState state, String maxOrMin) {
        int index = 0;
        int[] handSizes = state.getNumCardsInHandsOfUpcomingPlayers();
        
        if(maxOrMin.equals("least")) {
            for(int i = 1; i < handSizes.length; i++) {
                if(handSizes[i] < handSizes[index]) {
                    index = i;
                }
            }
        }

        if(maxOrMin.equals("greatest")) {
            for(int i = 1; i < handSizes.length; i++) {
                if(handSizes[i] > handSizes[index]) {
                    index = i;
                }
            }
        }

        return index;
    }


    private int calculateNumberOfPlayableCardType(ArrayList<Card> playable, UnoPlayer.Rank rank) {
        int total = 0;
        for(int i = 0; i < playable.size(); i++) {
            if(playable.get(i).getRank() == rank) {
                total++;
            }
        }

        return total;
    }
    

    private void calculateColorIHaveTheMostOfAndPointsInEachColor(List<Card> hand, int[] colors, int[] points) {
        for(int i = 0; i < hand.size(); i++) {
            switch(hand.get(i).getColor()) {
                case GREEN: {
                    colors[0]++;
                    points[0] += hand.get(i).forfeitCost();
                    break;
                }
                case BLUE: {
                    colors[1]++;
                    points[1] += hand.get(i).forfeitCost();
                    break;
                }
                case RED: {
                    colors[2]++;
                    points[2] += hand.get(i).forfeitCost();
                    break;
                }
                case YELLOW: {
                    colors[3]++;
                    points[3] += hand.get(i).forfeitCost();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }

}
