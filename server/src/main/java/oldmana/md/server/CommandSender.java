package oldmana.md.server;

import oldmana.md.common.Message;

public interface CommandSender
{
	/**
	 * Send a message to this entity.
	 * @param message The message to send
	 */
	void sendMessage(String message);
	
	/**
	 * Send a message to this entity with the given category.
	 * @param message The message to send
	 * @param category The category of the message
	 */
	void sendMessage(String message, String category);
	
	/**
	 * Send a message to this entity and optionally print the message to the console.
	 * @param message The message to send
	 * @param printConsole If true, the message will also be printed to the console
	 */
	default void sendMessage(String message, boolean printConsole)
	{
		sendMessage(message);
		if (printConsole)
		{
			MDServer.getInstance().getConsoleSender().sendMessage(message);
		}
	}
	
	/**
	 * Send a message to this entity.
	 * @param message The message to send
	 */
	default void sendMessage(Message message)
	{
		sendMessage(message.getUnformattedMessage());
	}
	
	/**
	 * Send a message to this entity and optionally print the message to the console.
	 * @param message The message to send
	 * @param printConsole If true, the message will also be printed to the console
	 */
	default void sendMessage(Message message, boolean printConsole)
	{
		sendMessage(message.getUnformattedMessage(), printConsole);
	}
	
	/**
	 * Check if this entity has operator permissions.
	 * @return True if the entity is an operator
	 */
	boolean isOp();
}
