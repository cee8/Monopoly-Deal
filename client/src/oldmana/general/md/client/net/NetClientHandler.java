package oldmana.general.md.client.net;

import java.awt.Frame;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import oldmana.general.md.client.MDClient;
import oldmana.general.md.client.MDEventQueue.EventTask;
import oldmana.general.md.client.MDScheduler.MDTask;
import oldmana.general.md.client.Player;
import oldmana.general.md.client.card.Card;
import oldmana.general.md.client.card.Card.CardType;
import oldmana.general.md.client.card.CardProperty.PropertyColor;
import oldmana.general.md.client.card.CardAction;
import oldmana.general.md.client.card.CardActionDoubleTheRent;
import oldmana.general.md.client.card.CardActionJustSayNo;
import oldmana.general.md.client.card.CardActionRent;
import oldmana.general.md.client.card.CardMoney;
import oldmana.general.md.client.card.CardProperty;
import oldmana.general.md.client.card.CardRegistry;
import oldmana.general.md.client.card.CardSpecial;
import oldmana.general.md.client.card.collection.Bank;
import oldmana.general.md.client.card.collection.CardCollection;
import oldmana.general.md.client.card.collection.CardCollectionRegistry;
import oldmana.general.md.client.card.collection.Deck;
import oldmana.general.md.client.card.collection.DiscardPile;
import oldmana.general.md.client.card.collection.Hand;
import oldmana.general.md.client.card.collection.PropertySet;
import oldmana.general.md.client.card.collection.VoidCollection;
import oldmana.general.md.client.gui.component.MDUndoButton;
import oldmana.general.md.client.state.ActionState;
import oldmana.general.md.client.state.ActionStateDiscard;
import oldmana.general.md.client.state.ActionStateDoNothing;
import oldmana.general.md.client.state.ActionStateDraw;
import oldmana.general.md.client.state.ActionStateFinishTurn;
import oldmana.general.md.client.state.ActionStatePlay;
import oldmana.general.md.client.state.ActionStateRent;
import oldmana.general.md.client.state.ActionStateStealMonopoly;
import oldmana.general.md.client.state.ActionStateStealProperty;
import oldmana.general.md.client.state.ActionStateTargetPlayer;
import oldmana.general.md.client.state.ActionStateTargetPlayerMonopoly;
import oldmana.general.md.client.state.ActionStateTargetPlayerProperty;
import oldmana.general.md.client.state.ActionStateTargetSelfPlayerProperty;
import oldmana.general.md.client.state.ActionStateTradeProperties;
import oldmana.general.md.net.packet.client.PacketLogin;
import oldmana.general.md.net.packet.client.action.PacketActionAccept;
import oldmana.general.md.net.packet.client.action.PacketActionChangeSetColor;
import oldmana.general.md.net.packet.client.action.PacketActionDiscard;
import oldmana.general.md.net.packet.client.action.PacketActionDraw;
import oldmana.general.md.net.packet.client.action.PacketActionEndTurn;
import oldmana.general.md.net.packet.client.action.PacketActionMoveProperty;
import oldmana.general.md.net.packet.client.action.PacketActionPay;
import oldmana.general.md.net.packet.client.action.PacketActionPlayCardAction;
import oldmana.general.md.net.packet.client.action.PacketActionPlayCardBank;
import oldmana.general.md.net.packet.client.action.PacketActionPlayCardProperty;
import oldmana.general.md.net.packet.client.action.PacketActionPlayCardSpecial;
import oldmana.general.md.net.packet.client.action.PacketActionPlayMultiCardAction;
import oldmana.general.md.net.packet.client.action.PacketActionSelectPlayer;
import oldmana.general.md.net.packet.client.action.PacketActionSelectPlayerProperty;
import oldmana.general.md.net.packet.client.action.PacketActionSelectPlayerMonopoly;
import oldmana.general.md.net.packet.client.action.PacketActionSelectSelfPlayerProperty;
import oldmana.general.md.net.packet.client.action.PacketActionUndoCard;
import oldmana.general.md.net.packet.server.PacketCardActionRentData;
import oldmana.general.md.net.packet.server.PacketCardCollectionData;
import oldmana.general.md.net.packet.server.PacketCardCollectionData.CardCollectionType;
import oldmana.general.md.net.packet.server.PacketCardData;
import oldmana.general.md.net.packet.server.PacketCardPropertyData;
import oldmana.general.md.net.packet.server.PacketDestroyCardCollection;
import oldmana.general.md.net.packet.server.PacketDestroyPlayer;
import oldmana.general.md.net.packet.server.PacketHandshake;
import oldmana.general.md.net.packet.server.PacketKick;
import oldmana.general.md.net.packet.server.PacketMoveCard;
import oldmana.general.md.net.packet.server.PacketMovePropertySet;
import oldmana.general.md.net.packet.server.PacketMoveRevealCard;
import oldmana.general.md.net.packet.server.PacketMoveUnknownCard;
import oldmana.general.md.net.packet.server.PacketPlayerInfo;
import oldmana.general.md.net.packet.server.PacketPlayerStatus;
import oldmana.general.md.net.packet.server.PacketPropertySetColor;
import oldmana.general.md.net.packet.server.PacketPropertySetData;
import oldmana.general.md.net.packet.server.PacketRefresh;
import oldmana.general.md.net.packet.server.PacketStatus;
import oldmana.general.md.net.packet.server.PacketUndoCardStatus;
import oldmana.general.md.net.packet.server.PacketUnknownCardCollectionData;
import oldmana.general.md.net.packet.server.actionstate.PacketActionStateBasic;
import oldmana.general.md.net.packet.server.actionstate.PacketActionStateRent;
import oldmana.general.md.net.packet.server.actionstate.PacketActionStateStealProperty;
import oldmana.general.md.net.packet.server.actionstate.PacketActionStateStealMonopoly;
import oldmana.general.md.net.packet.server.actionstate.PacketActionStateTradeProperties;
import oldmana.general.md.net.packet.server.actionstate.PacketUpdateActionStateAccepted;
import oldmana.general.md.net.packet.server.actionstate.PacketUpdateActionStateRefusal;
import oldmana.general.md.net.packet.server.actionstate.PacketUpdateActionStateTarget;
import oldmana.general.md.net.packet.server.actionstate.PacketActionStateBasic.BasicActionState;
import oldmana.general.mjnetworkingapi.packet.Packet;

public class NetClientHandler
{
	public static int PROTOCOL_VERSION = 2;
	
	private MDClient client;
	
	private Map<Class<? extends Packet>, Method> packetHandlers = new HashMap<Class<? extends Packet>, Method>();
	
	public NetClientHandler(MDClient client)
	{
		this.client = client;
	}
	
	@SuppressWarnings("unchecked")
	public void registerPackets()
	{
		// Client -> Server
		Packet.registerPacket(PacketLogin.class);
		
		// Server -> Client
		Packet.registerPacket(PacketHandshake.class);
		Packet.registerPacket(PacketCardCollectionData.class);
		Packet.registerPacket(PacketCardData.class);
		Packet.registerPacket(PacketCardActionRentData.class);
		Packet.registerPacket(PacketCardPropertyData.class);
		Packet.registerPacket(PacketDestroyCardCollection.class);
		Packet.registerPacket(PacketPropertySetColor.class);
		Packet.registerPacket(PacketStatus.class);
		Packet.registerPacket(PacketKick.class);
		Packet.registerPacket(PacketMoveCard.class);
		Packet.registerPacket(PacketMovePropertySet.class);
		Packet.registerPacket(PacketMoveRevealCard.class);
		Packet.registerPacket(PacketMoveUnknownCard.class);
		Packet.registerPacket(PacketPlayerInfo.class);
		Packet.registerPacket(PacketPropertySetData.class);
		Packet.registerPacket(PacketPlayerStatus.class);
		Packet.registerPacket(PacketDestroyPlayer.class);
		Packet.registerPacket(PacketRefresh.class);
		Packet.registerPacket(PacketUnknownCardCollectionData.class);
		Packet.registerPacket(PacketUndoCardStatus.class);
		
		// Client -> Server
		Packet.registerPacket(PacketActionAccept.class);
		Packet.registerPacket(PacketActionDraw.class);
		Packet.registerPacket(PacketActionEndTurn.class);
		Packet.registerPacket(PacketActionMoveProperty.class);
		Packet.registerPacket(PacketActionChangeSetColor.class);
		Packet.registerPacket(PacketActionPay.class);
		Packet.registerPacket(PacketActionPlayCardAction.class);
		Packet.registerPacket(PacketActionPlayCardBank.class);
		Packet.registerPacket(PacketActionPlayCardProperty.class);
		Packet.registerPacket(PacketActionPlayCardSpecial.class);
		Packet.registerPacket(PacketActionPlayMultiCardAction.class);
		Packet.registerPacket(PacketActionDiscard.class);
		Packet.registerPacket(PacketActionSelectPlayer.class);
		Packet.registerPacket(PacketActionSelectPlayerProperty.class);
		Packet.registerPacket(PacketActionSelectSelfPlayerProperty.class);
		Packet.registerPacket(PacketActionSelectPlayerMonopoly.class);
		Packet.registerPacket(PacketActionUndoCard.class);
		
		// Server -> Client
		Packet.registerPacket(PacketActionStateBasic.class);
		Packet.registerPacket(PacketActionStateRent.class);
		Packet.registerPacket(PacketActionStateStealProperty.class);
		Packet.registerPacket(PacketActionStateTradeProperties.class);
		Packet.registerPacket(PacketActionStateStealMonopoly.class);
		Packet.registerPacket(PacketUpdateActionStateAccepted.class);
		Packet.registerPacket(PacketUpdateActionStateRefusal.class);
		Packet.registerPacket(PacketUpdateActionStateTarget.class);
		
		// Find Packet Handlers
		for (Method m : getClass().getDeclaredMethods())
		{
			Class<?>[] params = m.getParameterTypes();
			if (params.length > 0)
			{
				Class<?> clazz = params[0];
				if (clazz.getSuperclass() == Packet.class)
				{
					packetHandlers.put((Class<? extends Packet>) clazz, m);
				}
			}
		}
	}
	
	public void processPackets(ConnectionThread connection)
	{
		for (Packet packet : connection.getInPackets())
		{
			System.out.println("Packet: " + packet.getClass());
			
			try
			{
				packetHandlers.get(packet.getClass()).invoke(this, packet);
			}
			catch (Exception e)
			{
				System.err.println("Error processing packet type " + packet.getClass().getName());
				e.printStackTrace();
			}
		}
	}
	
	public void handleHandshake(PacketHandshake packet)
	{
		client.createThePlayer(packet.id, packet.name);
	}
	
	public void handleCardData(PacketCardData packet)
	{
		Card card = null;
		if (packet.type == CardType.ACTION.getID())
		{
			card = new CardAction(packet.id, packet.value, packet.name);
		}
		else if (packet.type == CardType.JUST_SAY_NO.getID())
		{
			card = new CardActionJustSayNo(packet.id, packet.value, packet.name);
		}
		else if (packet.type == CardType.DOUBLE_THE_RENT.getID())
		{
			card = new CardActionDoubleTheRent(packet.id, packet.value, packet.name);
			System.out.println("DOUBLE THE RENT");
		}
		else if (packet.type == CardType.SPECIAL.getID())
		{
			card = new CardSpecial(packet.id, packet.value, packet.name);
		}
		else
		{
			card = new CardMoney(packet.id, packet.value);
		}
		card.setDisplayName(packet.displayName);
		card.setFontSize(packet.fontSize);
		card.setDisplayOffsetY(packet.displayOffsetY);
	}
	
	public void handleCardActionRentData(PacketCardActionRentData packet)
	{
		new CardActionRent(packet.id, packet.value, PropertyColor.fromIDs(packet.colors).toArray(new PropertyColor[packet.colors.length]));
	}
	
	public void handleCardPropertyData(PacketCardPropertyData packet)
	{
		new CardProperty(packet.id, PropertyColor.fromIDs(packet.colors), packet.base, packet.value, packet.name);
	}
	
	public void handlePlayerInfo(PacketPlayerInfo packet)
	{
		if (client.getPlayerByID(packet.id) == null)
		{
			Player player = new Player(client, packet.id, packet.name);
			player.setConnected(packet.connected);
			client.addPlayer(player);
		}
	}
	
	public void handlePlayerStatus(PacketPlayerStatus packet)
	{
		Player player = client.getPlayerByID(packet.player);
		player.setConnected(packet.connected);
		player.getUI().repaint();
	}
	
	public void handleDestroyPlayer(PacketDestroyPlayer packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				Player player = client.getPlayerByID(packet.player);
				client.destroyPlayer(player);
			}
		});
	}
	
	public void handleCardCollectionData(PacketCardCollectionData packet)
	{
		if (packet.type == CardCollectionType.BANK.getID())
		{
			Player owner = client.getPlayerByID(packet.owner);
			Bank bank = new Bank(packet.id, owner);
			for (int id : packet.cardIds)
			{
				bank.addCard(CardRegistry.getCard(id));
			}
			owner.setBank(bank);
		}
		else if (packet.type == CardCollectionType.HAND.getID())
		{
			Player player = client.getPlayerByID(packet.owner);
			Hand hand = new Hand(packet.id, player);
			for (int id : packet.cardIds)
			{
				hand.addCard(CardRegistry.getCard(id));
			}
			player.setHand(hand);
		}
		else if (packet.type == CardCollectionType.DISCARD_PILE.getID())
		{
			client.setDiscardPile(new DiscardPile(packet.id, CardRegistry.getCards(packet.cardIds)));
		}
	}
	
	public void handleUnknownCardCollectionData(PacketUnknownCardCollectionData packet)
	{
		if (packet.type == CardCollectionType.HAND.getID())
		{
			Player owner = client.getPlayerByID(packet.owner);
			owner.setHand(new Hand(packet.id, owner, packet.cardCount));
		}
		else if (packet.type == CardCollectionType.DECK.getID())
		{
			client.setDeck(new Deck(packet.id, packet.cardCount));
		}
		else if (packet.type == CardCollectionType.VOID.getID())
		{
			new VoidCollection(packet.id);
		}
	}
	
	public void handlePropertySetData(PacketPropertySetData packet)
	{
		Player owner = client.getPlayerByID(packet.owner);
		PropertySet set = new PropertySet(packet.id, owner, CardRegistry.getPropertyCards(packet.cardIds), PropertyColor.fromID(packet.activeColor));
		owner.addPropertySet(set);
	}
	
	public void handleStatus(PacketStatus packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				client.getTableScreen().getTopbar().setText(packet.text);
				client.getTableScreen().getTopbar().repaint();
			}
		});
	}
	
	public void handleMoveCard(PacketMoveCard packet)
	{
		Card card = CardRegistry.getCard(packet.cardId);
		CardCollection collection = CardCollectionRegistry.getCardCollection(packet.collectionId);
		collection.transferCard(card, packet.index, packet.speed);
	}
	
	public void handleMoveRevealCard(PacketMoveRevealCard packet)
	{
		CardCollection from = CardCollectionRegistry.getCardCollection(packet.from);
		CardCollection to = CardCollectionRegistry.getCardCollection(packet.to);
		from.transferCardTo(CardRegistry.getCard(packet.cardId), to, packet.index, packet.speed);
	}
	
	public void handleMoveUnknownCard(PacketMoveUnknownCard packet)
	{
		CardCollection from = CardCollectionRegistry.getCardCollection(packet.from);
		CardCollection to = CardCollectionRegistry.getCardCollection(packet.to);
		from.transferCardTo(null, to, -1, packet.speed);
	}
	
	public void handleActionStateBasic(PacketActionStateBasic packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				System.out.println("BASIC ACTION STATE " + packet.type);
				Player player = client.getPlayerByID(packet.player);
				if (packet.type == BasicActionState.DO_NOTHING.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateDoNothing());
				}
				else if (packet.type == BasicActionState.DRAW.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateDraw(player));
					if (player == client.getThePlayer())
					{
						client.getWindow().setAlert(true);
					}
				}
				else if (packet.type == BasicActionState.PLAY.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStatePlay(player, packet.data));
				}
				else if (packet.type == BasicActionState.DISCARD.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateDiscard(player));
				}
				else if (packet.type == BasicActionState.FINISH_TURN.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateFinishTurn(player));
				}
				else if (packet.type == BasicActionState.TARGET_PLAYER.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateTargetPlayer(player));
				}
				else if (packet.type == BasicActionState.TARGET_PLAYER_PROPERTY.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateTargetPlayerProperty(player));
				}
				else if (packet.type == BasicActionState.TARGET_SELF_PLAYER_PROPERTY.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateTargetSelfPlayerProperty(player));
				}
				else if (packet.type == BasicActionState.TARGET_PLAYER_MONOPOLY.getID())
				{
					client.getGameState().setCurrentActionState(new ActionStateTargetPlayerMonopoly(player));
				}
				client.setAwaitingResponse(false);
				client.getTableScreen().repaint();
			}
		});
	}
	
	public void handleActionStateRent(PacketActionStateRent packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				client.getGameState().setCurrentActionState(new ActionStateRent(client.getPlayerByID(packet.renter), client.getPlayersByIDs(packet.rented), 
						packet.amount));
				client.getTableScreen().repaint();
			}
		});
	}
	
	public void handleDestroyCardCollection(PacketDestroyCardCollection packet)
	{
		CardCollection collection = CardCollectionRegistry.getCardCollection(packet.id);
		if (collection instanceof PropertySet)
		{
			client.getEventQueue().addTask(new EventTask()
			{
				@Override
				public void start()
				{
					Player player = collection.getOwner();
					player.destroyPropertySet((PropertySet) collection);
					player.getUI().getPropertySets().repaint();
				}
			});
		}
	}
	
	public void handlePropertySetColor(PacketPropertySetColor packet)
	{
		PropertySet set = (PropertySet) CardCollectionRegistry.getCardCollection(packet.id);
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				set.setEffectiveColor(packet.color > -1 ? PropertyColor.fromID(packet.color) : null);
				set.getUI().repaint();
			}
		});
	}
	
	public void handleActionStateTradeProperties(PacketActionStateTradeProperties packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				client.setAwaitingResponse(false);
				client.getGameState().setCurrentActionState(new ActionStateTradeProperties((CardProperty) CardRegistry.getCard(packet.selfCard), 
						(CardProperty) CardRegistry.getCard(packet.otherCard)));
				client.getTableScreen().repaint();
			}
		});
	}
	
	public void handleActionStateStealProperty(PacketActionStateStealProperty packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				client.setAwaitingResponse(false);
				client.getGameState().setCurrentActionState(new ActionStateStealProperty(client.getPlayerByID(packet.thief), 
						(CardProperty) CardRegistry.getCard(packet.card)));
				client.getTableScreen().repaint();
			}
		});
	}
	
	public void handleActionStateStealMonopoly(PacketActionStateStealMonopoly packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
					{
				client.setAwaitingResponse(false);
				client.getGameState().setCurrentActionState(new ActionStateStealMonopoly(client.getPlayerByID(packet.thief), 
						(PropertySet) CardCollectionRegistry.getCardCollection(packet.collection)));
				client.getTableScreen().repaint();
			}
		});
	}
	
	public void handleUpdateActionStateAccepted(PacketUpdateActionStateAccepted packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				ActionState state = client.getGameState().getCurrentActionState();
				Player player = client.getPlayerByID(packet.target);
				if (state != null && state.isTarget(player))
				{
					client.setAwaitingResponse(false);
					state.setAccepted(player, packet.accepted);
					player.getUI().repaint();
				}
				else
				{
					System.err.println("Server sent illegal action state update (acceptance)");
				}
			}
		});
	}
	
	public void handleUpdateActionStateRefusal(PacketUpdateActionStateRefusal packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				ActionState state = client.getGameState().getCurrentActionState();
				Player player = client.getPlayerByID(packet.target);
				if (state != null && state.isTarget(player))
				{
					client.setAwaitingResponse(false);
					state.setRefused(player, packet.refused);
					player.getUI().repaint();
				}
				else
				{
					System.err.println("Server sent illegal action state update (refusal)");
				}
			}
		});
	}
	
	public void handleUpdateActionStateTarget(PacketUpdateActionStateTarget packet)
	{
		client.getEventQueue().addTask(new EventTask()
		{
			@Override
			public void start()
			{
				ActionState state = client.getGameState().getCurrentActionState();
				if (state != null)
				{
					client.setAwaitingResponse(false);
					Player player = client.getPlayerByID(packet.target);
					if (!packet.isTarget)
					{
						state.setTarget(player, false);
					}
				}
				else
				{
					System.err.println("Server sent illegal action state update (target)");
				}
			}
		});
	}
	
	public void handleUndoCardStatus(PacketUndoCardStatus packet)
	{
		MDUndoButton undoButton = MDClient.getInstance().getTableScreen().getUndoButton();
		if (packet.cardId > -1)
		{
			undoButton.setUndoCard(CardRegistry.getCard(packet.cardId));
		}
		else
		{
			undoButton.removeUndoCard();
		}
	}
}
