package oldmana.general.md.server.card.action;

import oldmana.general.md.server.Player;
import oldmana.general.md.server.card.CardAction;

public class CardActionIGotNothinToHide extends CardAction
{
	public CardActionIGotNothinToHide()
	{
		super(2, "I Got Nothin' To Hide");
		setDisplayName("I GOT", "NOTHIN'", "TO HIDE");
		setFontSize(7);
		setDisplayOffsetY(0);
		setRevocable(false);
		setMarksPreviousUnrevocable(false);
	}
	
	@Override
	public void playCard(Player player)
	{
		
	}
}
