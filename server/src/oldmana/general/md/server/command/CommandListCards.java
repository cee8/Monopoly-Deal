package oldmana.general.md.server.command;

import oldmana.general.md.server.CommandSender;
import oldmana.general.md.server.card.Card;
import oldmana.general.md.server.card.collection.CardCollection;
import oldmana.general.md.server.card.collection.CardCollectionRegistry;

public class CommandListCards extends Command
{
	public CommandListCards()
	{
		super("listcards", null, new String[] {"/listcards [Collection ID]"}, true);
	}
	
	@Override
	public void executeCommand(CommandSender sender, String[] args)
	{
		if (args.length >= 1)
		{
			if (verifyInt(args[0]))
			{
				CardCollection collection = CardCollectionRegistry.getCardCollection(Integer.parseInt(args[0]));
				sender.sendMessage("List of cards in collection ID " + collection.getID() + "(Count: " + collection.getCardCount() + ")");
				for (Card card : collection.getCards())
				{
					sender.sendMessage("- " + card.getID() + ": " + card.toString());
				}
			}
			else
			{
				sender.sendMessage("Error: Argument is not an integer.");
			}
		}
		else
		{
			sender.sendMessage("Error: Collection ID required.");
		}
	}
}
