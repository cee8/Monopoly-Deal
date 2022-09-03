package oldmana.md.server.card.control.condition.logic;

import oldmana.md.server.Player;
import oldmana.md.server.card.Card;
import oldmana.md.server.card.control.condition.AbstractButtonCondition;

import java.util.List;

public class ConditionOr extends ConditionLogical
{
	public ConditionOr(AbstractButtonCondition... conditions)
	{
		super(conditions);
	}
	
	public ConditionOr(List<AbstractButtonCondition> conditions)
	{
		super(conditions);
	}
	
	@Override
	public boolean evaluate(Player player, Card card)
	{
		for (AbstractButtonCondition condition : getConditions())
		{
			if (condition.evaluate(player, card))
			{
				return true;
			}
		}
		return false;
	}
}