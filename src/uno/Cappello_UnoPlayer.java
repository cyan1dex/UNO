/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package uno;

import java.util.List;

/**
 *
 * @author backtrack
 */
public class Cappello_UnoPlayer implements UnoPlayer
{
    /**
     * <p>This method is called when it's your turn and you need to
     * choose what card to play.</p>
     *
     * <p>The hand parameter tells you what's in your hand. You can call
     * getColor(), getRank(), and getNumber() on each of the cards it
     * contains to see what it is. The color will be the color of the card,
     * or "Color.NONE" if the card is a wild card. The rank will be
     * "Rank.NUMBER" for all numbered cards, and another value (e.g.,
     * "Rank.SKIP," "Rank.REVERSE," etc.) for special cards. The value of
     * a card's "number" only has meaning if it is a number card. 
     * (Otherwise, it will be -1.)</p>
     *
     * <p>The upCard parameter works the same way, and tells you what the 
     * up card (in the middle of the table) is.</p>
     *
     * <p>The calledColor parameter only has meaning if the up card is a
     * wild, and tells you what color the player who played that wild card
     * called.</p>
     *
     * <p>Finally, the state parameter is a GameState object on which you
     * can invoke methods if you choose to access certain detailed
     * information about the game (like who is currently ahead, what colors
     * each player has recently called, etc.)</p>
     *
     * <p>You must return a value from this method indicating which card you
     * wish to play. If you return a number 0 or greater, that means you
     * want to play the card at that index. If you return -1, that means
     * that you cannot play any of your cards (none of them are legal plays)
     * in which case you will be forced to draw a card (this will happen
     * automatically for you.)</p>
     */
    
    /*
    *return the position of the skip card, or -1
    */
    public int PosOfSkip(List<Card> hand)
    {
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getRank() == Rank.SKIP)
                return i;
        }
        return -1;
    }
    
    public int PosOfReverse(List<Card> hand)
    {
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getRank() == Rank.REVERSE)
                return i;
        }
        return -1;
    }
        
            
    public int PosOfDraw2(List<Card> hand)
    {
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getRank() == Rank.DRAW_TWO)
                return i;
        }
        return -1;
    }
        
    public int PosOfWild(List<Card> hand)
    {
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getRank() == Rank.WILD)
                return i;
        }
        return -1;
    }
            
    public int PosOfWild4(List<Card> hand)
    {
        for (int i = 0; i < hand.size(); i++) {
            if(hand.get(i).getRank() == Rank.WILD_D4)
                return i;
        }
        return -1;
    }
    
    
    public int play(List<Card> hand, Card upCard, Color calledColor, GameState state)
    {
        int[] playerCounts = state.getNumCardsInHandsOfUpcomingPlayers();
        
        //Determine if a non-denominator card should be played
        if(playerCounts[0] <=3)
        {
            int play = -1;
            if(PosOfDraw2(hand) != -1)
                play = PosOfDraw2(hand) ;
            else if(this.PosOfSkip(hand) != -1)
                play = PosOfSkip(hand) ;
            else if(playerCounts[2] > playerCounts[0] && this.PosOfReverse(hand) != -1)
                play = PosOfReverse(hand) ;
            
            if(play != -1 && hand.get(play).canPlayOn(upCard, calledColor))
                return play;
        }
        
        
        
       for(int pos = 0; pos < hand.size(); pos++)
       {
           if(hand.get(pos).canPlayOn(upCard, calledColor))
               return pos;
       }
       return -1;
    }

    /**
     * <p>This method will be called when you have just played a
     * wild card, and is your way of specifying which color you want to 
     * change it to.</p>
     *
     * <p>You must return a valid Color value from this method. You must
     * not return the value Color.NONE under any circumstances.</p>
     */
    public Color callColor(List<Card> hand)
    {
        int blue = 0;
        int green = 1;
        int yellow = 2;
        int red = 3;
        
        int [] colors = new int[4];
        
        for(Card c : hand)
        {
           if(c.getColor() == Color.BLUE)
               colors[blue]++;
           else if(c.getColor()== Color.RED)
               colors[red]++;
           else if(c.getColor() == Color.YELLOW)
               colors[yellow]++;
           else if(c.getColor() == Color.GREEN)
               colors[green]++;
        }
        
        int maxPos = 0;
        int max = 0;
        
        for(int i = 0; i < colors.length; i++)
        {
            if(colors[i] > max)
            {
                maxPos = i;
                max = colors[i];
            }
        }
        
        switch(maxPos)
        {
            case 0:
            {
                return Color.BLUE;
            }
            case  1:
            {
                return Color.GREEN;
            }
            case 2:
            {
                return Color.YELLOW;
            }
            case 3:
            {
                return Color.RED;
            }
            default:
            return Color.BLUE;
        }
    }
}
