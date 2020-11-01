package oldmana.general.md.client.state;

import java.awt.Color;

import oldmana.general.md.client.Player;
import oldmana.general.md.client.card.collection.PropertySet;
import oldmana.general.md.client.gui.component.MDPropertySet;

public class ActionStateStealMonopoly extends ActionState
{
	private PropertySet targetMonopoly;
	
	public ActionStateStealMonopoly(Player player, PropertySet targetMonopoly)
	{
		super(player, targetMonopoly.getOwner());
		this.targetMonopoly = targetMonopoly;
	}
	
	@Override
	public void setup()
	{
		super.setup();
		
		Color selectColor = getActionOwner() == getClient().getThePlayer() ? Color.BLUE : Color.RED;
		
		((MDPropertySet) targetMonopoly.getUI()).enableSelection(selectColor);
	}
	
	@Override
	public void cleanup()
	{
		super.cleanup();
		
		if (targetMonopoly != null)
		{
			((MDPropertySet) targetMonopoly.getUI()).disableSelection();
		}
	}
}
