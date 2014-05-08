package uno;

import java.util.List;

public class jnedza_UnoPlayer implements UnoPlayer {

    /**
     * Object Oriented Implementation Notes 
     * 
     * New Method Call Format:
     *  public int play(List<Card> hand, Card upCard, Color calledColor,
            GameState state);
     *  public Color callColor(List<Card> hand);
     * 
     * Card Object Methods
     *      public UnoPlayer.Color getColor()
     *      public UnoPlayer.Rank getRank()
     *      public int getNumber()
     * 
     * UnoPlayer Reminders
     *     public enum Color { RED, YELLOW, GREEN, BLUE, NONE }
     *     public enum Rank { NUMBER, SKIP, REVERSE, DRAW_TWO, WILD, WILD_D4 }
     * 
     */
    
    /**
     * -------------------------------------------------------------------------
     * OBJECT-ORIENTED MODIFICATIONS
     *     play is now passed hand, a List of Card objects. References to handColors,
     * handRanks, and handNumbers are now: <Card>.getColor(), <Card>.getRank(),
     * and <Card>.getNumber(). upCard is now passed as a Card object. Otherwise,
     * gameplay behavior remains the same as noted.
     * -------------------------------------------------------------------------
     * 
     * ::Previous Trimmed Notes::
     * 
     * play - This method is called when it's your turn and you need to
     * choose what card to play.
     *
     *
     * You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)
     */
        
        // Method: Program parses each card in hand for desirability rating, 
        // each card gaining Points based on its desirability for the situation.
        // The card with the most points is the most desirable, and gets played.

public int play(List<Card> hand, Card upCard, Color calledColor, GameState state){
        
        int desire[] = new int[hand.size()];
        //Array created as large as current hand size; archives desirability.
        //Note: Illegal plays should be given a -1 sentinel value in desire[]
        int play = -1;
        //play referenced in final For Loop to determine highest desirability.
        // == the # of card to play from hand.

        // Meaning of -1; if play doesn't get a higher value (as in, the
        // desirability of all cards is -1, meaning they are all illegal), then
        // -1 will be the return value for "No legal plays".

        for (int i=0; i<desire.length; i++){
            //All plays are, unless given desire points, illegal.
            desire[i] = -1;
        }

        for (int i=0; i<desire.length; i++){
        //Desirability parsing Loop. i==curent card
            if ( (hand.get(i).getColor() == upCard.getColor() && upCard.getColor() != Color.NONE)
                    // ^^ Matching color cards of any type are available to play.
                    || (hand.get(i).getNumber() == upCard.getNumber() && upCard.getNumber() != -1)
                    // ^^ Number cards 0-9 with matching values are available to play.
                    || (hand.get(i).getRank() == Rank.SKIP && upCard.getRank() == Rank.SKIP)
                    || (hand.get(i).getRank() == Rank.REVERSE && upCard.getRank() == Rank.REVERSE)
                    // ^^ Matching skips and reverses, even if not same color, available.
                    || hand.get(i).getRank() == Rank.WILD 
                    || hand.get(i).getRank() == Rank.WILD_D4
                    // ^^ Wilds are always available to play.
                    || (hand.get(i).getRank() == Rank.DRAW_TWO && upCard.getRank() == Rank.DRAW_TWO)
                    || (hand.get(i).getColor() == calledColor && calledColor != Color.NONE)
                    ){
                //Entering this loop indicates a legal play, and further if's
                //set desirability based on card type.
                desire[i] += 1;
                //This marks any legal play as an option by increasing it from
                //the -1 value indicating illegal plays.
                if ( hand.get(i).getNumber() != -1 &&
                        hand.get(i).getNumber() == upCard.getNumber() 
                        ) {
                    //Prefers higher number cards; skips if card is not numbered.
                    desire[i] += hand.get(i).getNumber();
                    if (hand.get(i).getColor() == upCard.getColor()
                            && hand.get(i).getNumber() == upCard.getNumber()){
                        //Reduces desirability if Color AND number match;
                        //Prefer to use cards that only match in one respect.
                        desire[i] = desire[i] / 2;
                    }
                }

                if (hand.get(i).getRank() == Rank.DRAW_TWO && hand.get(i).getColor() == upCard.getColor()){
                    //High desire to play if possible.
                    desire[i] += 8;
                    if (hand.get(i).getRank() == upCard.getRank()){
                        //Lower desire if matching both rank and color.
                        desire[i] -= 3;
                    }
                }

                if (hand.get(i).getRank() == Rank.WILD_D4) {
                    //Wild_d4's are used as quickly as possible.
                    return i;
                }
                if (hand.get(i).getRank() == Rank.WILD) {
                    //Wild's will be held at relatively low desire.
                    desire [i] += 2;
                }
                if (hand.get(i).getRank() == Rank.REVERSE && upCard.getRank() == Rank.REVERSE) {
                    desire[i] +=7;
                    if (hand.get(i).getColor() == upCard.getColor()){
                        //Lowers desire to play a Reverse that matches both
                        //Reverse and Color; prefers just reverse, or just color.
                        desire [i] -= 2;
                    }
                }
                if (hand.get(i).getRank() == Rank.SKIP && upCard.getRank() == Rank.SKIP) {
                    desire[i] +=7;
                    if (hand.get(i).getColor() == upCard.getColor()){
                        //Lowers desire to play a Skip that matches both
                        //Skip and Color; prefers just skip, or just color.
                        desire [i] -= 2;
                    }
                }
            } //close legal play if statement
 else {}
        } //close legal play for loop (hand iterate)
        int tempdesire = -1;

        for (int i=0; i<desire.length; i++) {
        //Final parsing For Loop; finds highest legal desirability and plays.
            if (desire[i] > tempdesire){
                tempdesire = desire[i];
                play = i;
            }
        }

        //If no legal plays, play should still be -1.
        return play;
    }


    /**
     * callColor - This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to
     * change it to.
     *
     * You must return a valid Color value from this method. You must not
     * return the value Color.NONE under any circumstances.
     */

public Color callColor(List<Card> hand){
        //Plays wild card in color that you have most in your hand; fallback
        //color is yellow if it just can't choose.
    
        // OOP Update: 
        //      Adds up number values of cards instead of count number of cards.
        //      
        int blue = 0, red = 0, green = 0, yellow = 0;
        
        for (int i=0; i<hand.size(); i++){
            if (hand.get(i).getColor() == Color.BLUE){
                if (hand.get(i).getRank() == Rank.NUMBER
                        ){
                    blue += hand.get(i).getNumber();
                }
            }
            
            if (hand.get(i).getColor() == Color.GREEN){
                if (hand.get(i).getRank() == Rank.NUMBER
                        ){
                    green += hand.get(i).getNumber();
                }
            }
            
            if (hand.get(i).getColor() == Color.RED){
                if (hand.get(i).getRank() == Rank.NUMBER
                        ){
                    red += hand.get(i).getNumber();
                }
            }
            
            if (hand.get(i).getColor() == Color.YELLOW){
                if (hand.get(i).getRank() == Rank.NUMBER
                        ){
                    yellow += hand.get(i).getNumber();
                }
            }
        }

        if (red > blue && red > green && red > yellow){
            return Color.RED;
            }
            else {
                if (blue > green && blue > yellow){
                    return Color.BLUE;
                    }
                else
                    if (green > yellow){
                    return Color.GREEN;
                    }
                else return Color.YELLOW;
            }

    }

}
