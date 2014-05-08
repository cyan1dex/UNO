package uno;
import java.util.ArrayList;
import java.util.List;


public class BehennaBot_UnoPlayer implements UnoPlayer{

	List<Card> hand;
	@Override
	public int play(List<Card> hand, Card upCard, Color calledColor,
			GameState state)
	{
		this.hand = hand;
		List<Integer> posCards = new ArrayList<Integer>();
		
		for(int i = 0; i < hand.size(); i++){
			if(hand.get(i).canPlayOn(upCard, calledColor)){
				posCards.add(i);
			}	
		}
		return chooseCard(posCards, state);
	}

	@Override
	public Color callColor(List<Card> hand)
	{
		return greatestNumOfColor(hand);
	}

	public int chooseCard(List<Integer> posCards, GameState state)
	{
		if(posCards.size() == 0){
			return -1;
		}else if(whosWinning(state) == 0 && posOfActionCard(posCards).size() != 0)
		{
			return posOfActionCard(posCards).get(0);
		}
		return highestValCard(posCards);
	}
	public boolean endClose(GameState state)
	{
		for(int i = 0; i < state.getNumCardsInHandsOfUpcomingPlayers().length; i++){
			if(state.getNumCardsInHandsOfUpcomingPlayers()[i] < 5)
			{
				return true;
			}
		}
		return false;
	}
	public int highestValCard(List<Integer> posCards){
		int maxVal = -1;
		int posOfMaxVal = -1;
		for(int i = 0; i < posCards.size(); i++)
		{
			if(hand.get(posCards.get(i)).forfeitCost() > maxVal){
				maxVal = hand.get(posCards.get(i)).forfeitCost();
				posOfMaxVal = posCards.get(i);
			}
		}
		return posOfMaxVal;
	}
	public int whosWinning(GameState state){
		int maxVal = -1;
		int posOfPlayer = -1;
		for(int i = 0; i < state.getTotalScoreOfUpcomingPlayers().length; i++){
			if(state.getTotalScoreOfUpcomingPlayers()[i] > maxVal)
			{
				maxVal = state.getTotalScoreOfUpcomingPlayers()[i];
				posOfPlayer = i;
			}
		}
		return posOfPlayer;
	}
	public List<Integer> posOfActionCard(List<Integer> posCards)
	{
		List<Integer> pos = new ArrayList<Integer>();
		
		for(int i = 0; i < posCards.size(); i++){
			if(hand.get(posCards.get(i)).getColor().equals(Color.NONE)){
				pos.add(posCards.get(i));
			}
		}
		return pos;
	}
	public Color greatestNumOfColor(List<Card> hand)
	{
		int redAmount = 0;
		int blueAmount = 0;
		int greenAmount = 0;
		int yellowAmount = 0;
		for(int i = 0; i < hand.size(); i++)
		{
			switch(hand.get(i).getColor()){
			case RED: redAmount++;
			break;
			case BLUE: blueAmount++;
			break;
			case GREEN: greenAmount++;
			break;
			case YELLOW: yellowAmount++;
			break;
			default:
				break;
			}
		}
		
		if(redAmount >= blueAmount && redAmount >= greenAmount && redAmount >= yellowAmount)
		{
			return Color.RED;
		}else if(blueAmount >= greenAmount && blueAmount >= yellowAmount)
		{
			return Color.BLUE;
		}else if(greenAmount >= yellowAmount)
		{
			return Color.GREEN;
		}else
		{
			return Color.YELLOW;
		}
		
		
	}
}
