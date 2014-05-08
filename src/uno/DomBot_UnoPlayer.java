package uno;

import java.util.List;


public class DomBot_UnoPlayer implements UnoPlayer {
	
	public int play(List<Card> hand, Card upCard,  Color calledColor,GameState state) {

        //Count the number of each color of card
       int blueNum = 0;
       int redNum = 0;
       int greenNum = 0;
       int yellowNum = 0;
       
       for(int i = 0; i < hand.size(); i++)
       {
           if (hand.get(i).getColor().equals(Color.BLUE))
                   ++blueNum;
           else if (hand.get(i).getColor().equals(Color.GREEN))
                   ++greenNum;
           else if (hand.get(i).getColor().equals(Color.RED))
                   ++redNum;
           else if (hand.get(i).getColor().equals(Color.YELLOW))
                    ++yellowNum;
      }
      
       int[] colors = {blueNum, redNum, greenNum, yellowNum};
        int mostColor = 0;
        
        for (int i=0; i<colors.length; i++)
        {
        	if (colors[i] > mostColor)
        	{
        		mostColor = colors[i];
        	}
        }
        
        //Dumping Wild_D4 Cards from hand
        for (int i = 0; i < hand.size(); i++){
            if(hand.get(i).getRank().equals(Rank.WILD_D4)) 
                return i;
            }

        //Dump Wild Cards from hand
       for (int i = 0; i < hand.size(); i++) {
           if((hand.get(i).getRank().equals(Rank.WILD))) {
               return i;
           }
        }

        //Dump D2 Cards from hand
       for (int i = 0; i < hand.size(); i++) {
           if((hand.get(i).getRank().equals(Rank.DRAW_TWO)) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor) || (hand.get(i).getRank() == upCard.getRank())))) {
               return i;
           }
       }
       
       //Dump Skip Cards from hand
       for (int i = 0; i < hand.size(); i++) {
           if ((hand.get(i).getRank().equals(Rank.SKIP)) && (((hand.get(i).getColor() == upCard.getColor()) || (hand.get(i).getColor() == calledColor)) || (hand.get(i).getRank() == upCard.getRank())))
            {
        	   return i;
            }
       }
       
       for (int i = 0; i < hand.size(); i++) {
           if ((hand.get(i).getRank().equals(Rank.REVERSE)) && ((hand.get(i).getColor() == upCard.getColor()) || (hand.get(i).getColor() == calledColor || (hand.get(i).getRank() == upCard.getRank()))))
                   return i;
           }
        
        // With a number on the field, place the card with that number of the color with the most cards
       for (int i = 0; i < hand.size(); i++) {
           if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.RED)) && (redNum == mostColor)) {
               return i;
           }
            else if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.BLUE)) && (blueNum == mostColor)) {
                return i ;
            }
            else if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.GREEN)) && (greenNum == mostColor)) {
                return i ;
                
            }
            else if ((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1) && (hand.get(i).getColor().equals(Color.YELLOW)) && (yellowNum == mostColor)){
             return i ;
            }
               
       }
      
       // With a color placed down, play card of that color with the highest number value
       
        
             for(int i = 0; i < hand.size(); i++) 
             {
                 if((hand.get(i).getNumber() == 9)&& (hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))
                 {
                     return i;
                 }
            }
             for(int i = 0; i < hand.size(); i++) 
             {
                 if((hand.get(i).getNumber() == 8)&& (hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))
                 {
                     return i;
                 }
             }
             for(int i = 0; i < hand.size(); i++) 
              {
                 if((hand.get(i).getNumber() == 7)&& ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor))))
                 {
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++)
              {
                 if((hand.get(i).getNumber() == 6) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor))))
                 {
                     return i;
                 }
              }
            
              for(int i = 0; i < hand.size(); i++)
              {
                 if((hand.get(i).getNumber() == 5) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor))))
                 {
                     return i; 
                 }
              }
            
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 4)&& ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 3)&& ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 2) && ((hand.get(i).getColor().equals(upCard.getColor()) || (hand.get(i).getColor().equals(calledColor))))){
                     return i;
                 }
            }
              for(int i = 0; i < hand.size(); i++)
                  if((hand.get(i).getNumber() == 1) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;}
                 
            
              for(int i = 0; i < hand.size(); i++) {
                 if((hand.get(i).getNumber() == 0) && ((hand.get(i).getColor().equals(upCard.getColor())) || (hand.get(i).getColor().equals(calledColor)))){
                     return i;
                 }
            }
       //Match number
        for(int i = 0; i < hand.size(); i++) {
            if((hand.get(i).getNumber() == upCard.getNumber()) && (hand.get(i).getNumber() != -1)){
                return i;
            }
                  }
       
        return -1;

       }
   
public Color callColor(List<Card> hand) {
 //Counting the number of each card color for a wild card
	 int blueNum = 0;
     int redNum = 0;
     int greenNum = 0;
     int yellowNum = 0;
     for( int i = 0; i < hand.size(); i++)
     {
         if (hand.get(i).equals(Color.BLUE))
                 ++blueNum;
         else if (hand.get(i).equals(Color.GREEN))
                 ++greenNum;
         else if (hand.get(i).equals(Color.RED))
                 ++redNum;
         else if (hand.get(i).equals(Color.YELLOW))
                  ++yellowNum;
    }
     int[] colors = {blueNum, redNum, greenNum, yellowNum};
      int mostColor = 0;
      
      for (int i=0; i<colors.length; i++) 
      {
          if (colors[i] > mostColor) 
          {
              mostColor = colors[i];
          }
      }
      if(mostColor == blueNum)
          return Color.BLUE;
      else if (mostColor == redNum)
          return Color.RED;
      else if (mostColor == greenNum)
          return Color.GREEN;
      else if (mostColor == yellowNum)
          return Color.YELLOW;
  
         return Color.YELLOW;
    
    }
}
