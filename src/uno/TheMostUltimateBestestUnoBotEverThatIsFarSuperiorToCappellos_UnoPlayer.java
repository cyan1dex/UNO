 package uno;

import java.util.ArrayList;
import java.util.List;

public class TheMostUltimateBestestUnoBotEverThatIsFarSuperiorToCappellos_UnoPlayer implements UnoPlayer {

	public Color[] color;
	public int[] number;
	public List<Integer> playable;
	private Card upCard;
	private Color calledColor;
	List<Card> hand;
	
	@Override
	public int play(List<Card> hand, Card upCard, Color calledColor,
			GameState state) {
		this.hand=hand;
		this.upCard=upCard;
		this.calledColor=calledColor;
		
		
		mostColor();
		leastNumber();
		findPlayable();
	
		if(playable.isEmpty())return -1;
		
		Card card;
		
		for(Color c:color){
			for(int n:number){
				card=new Card(c, n);
				if(hand.contains(card)&&playable.contains(hand.indexOf(card)))return hand.indexOf(card);
			}
		}
		if(state.getNumCardsInHandsOfUpcomingPlayers()[0]<hand.size()){
			card=new Card(Color.NONE, Rank.DRAW_TWO);
			if(hand.contains(card))return hand.indexOf(card);
		}
		return playable.get(0);
	}

	
	
	private void findPlayable() {
		playable=new ArrayList<Integer>();
		for(int i=0;i<hand.size();i++){
			if(hand.get(i).canPlayOn(upCard, calledColor))playable.add(i);
		}
		
	}
	
	public void mostColor()
	{
		//red=0
		//green=1
		//blue=2
		//yellow=3
		int[] arr=new int[4];
		
		for(Card c: hand){
			switch(c.getColor()){
			case RED:
			{
				arr[0]++;
				break;
			}
			case GREEN:
			{
				arr[1]++;
				break;
			}
			case BLUE:
			{
				arr[2]++;
				break;
			}
			case YELLOW:
			{
				arr[3]++;
				break;
			}
			default:
				break;
			}
		}
		
		color=new Color[4];
		for(int i=0;i<4;i++){
			int highest=0;
			for(int j=0;j<4;j++){
				if(arr[j]>arr[highest]){
					highest=j;
				}
			}
			switch(highest){
			case 0:
			{
				color[i]=Color.RED;
				break;
			}
			case 1:
			{
				color[i]=Color.GREEN;
				break;
			}
			case 2:
			{
				color[i]=Color.BLUE;
				break;
			}
			case 3:
			{
				color[i]=Color.YELLOW;
				break;
			}
			}
			arr[highest]=-1;
			
		}
		
		
	}

	public void leastNumber(){
		int[] arr=new int[10];
		
		for(Card c:hand){
			if(c.getRank().equals(Rank.NUMBER)){
				arr[c.getNumber()]++;
			}
		}
		number=new int[10];
		for(int i=0;i<10;i++){
			int lowest=0;
			for(int j=0;j<10;j++){
				if(arr[j]<arr[lowest]){
					lowest=j;
				}
			}
			number[i]=lowest;
			arr[lowest]=100;
			
		}

		
	}

	@Override
	public Color callColor(List<Card> hand) {
		// TODO Auto-generated method stub
		return color[0];
	}


}