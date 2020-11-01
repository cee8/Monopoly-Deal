package oldmana.general.md.client.gui.component;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;

import javax.swing.SwingUtilities;

import oldmana.general.md.client.MDScheduler.MDTask;
import oldmana.general.md.client.card.Card;
import oldmana.general.md.client.card.collection.Deck;
import oldmana.general.md.client.gui.util.CardPainter;
import oldmana.general.md.client.gui.util.GraphicsUtils;
import oldmana.general.md.client.gui.util.TextPainter;
import oldmana.general.md.client.gui.util.TextPainter.Alignment;

public class MDDeck extends MDCardCollectionUnknown
{
	private boolean hovered;
	private MDTask animTask;
	private int animStage;
	
	public MDDeck(Deck deck)
	{
		super(deck, 2);
		MDDeckListener listener = new MDDeckListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		animTask = new MDTask(1, true)
		{
			MDSelection select = new MDSelection();
			{
				select.setSize(scale(120), scale(180));
				select.setLocation(0, scale(10));
				select.setColor(Color.BLUE);
				add(select);
				select.setVisible(false);
			}
			
			@Override
			public void run()
			{
				int lastStage = animStage;
				if (getClient().isInputBlocked() || !getClient().canDraw())
				{
					animStage = 0;
					select.setVisible(false);
				}
				else
				{
					if (hovered)
					{
						animStage = Math.min(animStage + 1, 16);
						select.setVisible(false);
					}
					else
					{
						animStage = Math.max(animStage - 1, 0);
						if (animStage == 0)
						{
							select.setVisible(true);
							select.repaint();
						}
					}
				}
				if (lastStage != animStage)
				{
					repaint();
				}
			}
		};
		getClient().getScheduler().scheduleTask(animTask);
		update();
	}
	
	/*
	@Override
	public void addCard(MDCard card)
	{
		
	}
	
	@Override
	public void removeCard(MDCard card)
	{
		
	}
	*/
	
	@Override
	public void update()
	{
		/*
		if (getCollection().getCardCount() == 0)
		{
			if (face != null)
			{
				remove(face);
				face = null;
			}
		}
		else
		{
			if (face == null)
			{
				face = new MDCard(null);
				face.setScale(2);
				face.setLocation(0, 0);
				add(face);
			}
		}
		*/
		repaint();
	}
	
	@Override
	public Point getLocationOf(int cardIndex)
	{
		return SwingUtilities.convertPoint(this, new Point(0, scale(10)), getClient().getTableScreen());
	}
	
	@Override
	public void paintComponent(Graphics gr)
	{
		super.paintComponent(gr);
		Graphics2D g = (Graphics2D) gr.create();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (getCollection() != null)
		{
			if (getCollection().getCardCount() > 0)
			{
				g.setColor(Color.DARK_GRAY);
				g.fillRoundRect(scale(60), scale(10), scale(60) + (int) Math.ceil(getCollection().getCardCount() * (0.33 * GraphicsUtils.SCALE)), scale(180), scale(20), scale(20));
				g.translate(0, scale(10));
				g.drawImage(Card.getBackGraphics(GraphicsUtils.SCALE * 2), 0, 0, null);
				if (animStage > 0)
				{
					BufferedImage img = GraphicsUtils.createImage(GraphicsUtils.getCardWidth(2) + scale(16), GraphicsUtils.getCardHeight(2) + scale(24));
					Graphics2D g2 = img.createGraphics();
					g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
					g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					g2.rotate(Math.toRadians(animStage * 0.35), 0, GraphicsUtils.getCardHeight(2) + scale(10));
					g2.translate(0, scale(10));
					//CardPainter cp = new CardPainter(null, 2);
					//cp.paint(g2);
					g2.drawImage(Card.getBackGraphics(GraphicsUtils.SCALE * 2), 0, 0, null);
					//g2.rotate(10, MDCard.CARD_SIZE.width, MDCard.CARD_SIZE.height);
					//g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
					//g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
					//g.rotate(Math.toRadians(5), MDCard.CARD_SIZE.width, MDCard.CARD_SIZE.height);
					g.translate(0, scale(-10));
					g.drawImage(img, 0, 0, null);
				}
				else if (getClient().canDraw())
				{
					//g.setColor(Color.YELLOW);
					//g.drawRoundRect(0, 0, MDCard.CARD_SIZE.width * 2, MDCard.CARD_SIZE.height * 2, 20, 20);
				}
			}
			else
			{
				g.setColor(Color.LIGHT_GRAY);
				g.translate(0, scale(10));
				g.fillRoundRect(0, 0, GraphicsUtils.getCardWidth(2), GraphicsUtils.getCardHeight(2), scale(20), scale(20));
				g.setColor(Color.BLACK);
				Font font = GraphicsUtils.getThinMDFont(Font.PLAIN, scale(18));
				g.setFont(font);
				TextPainter tp = new TextPainter("Deck Empty", font, new Rectangle(0, 0, GraphicsUtils.getCardWidth(2), GraphicsUtils.getCardHeight(2)));
				tp.setHorizontalAlignment(Alignment.CENTER);
				tp.setVerticalAlignment(Alignment.CENTER);
				tp.paint(g);
			}
			
			if (getClient().isDebugEnabled())
			{
				g = (Graphics2D) gr.create();
				g.setColor(Color.ORANGE);
				GraphicsUtils.drawDebug(g, "ID: " + getCollection().getID(), scale(30), getWidth(), getHeight() / 2);
			}
		}
		//g.fillRect(60, 0, collection.getCardCount(), 90);
	}
	
	public class MDDeckListener implements MouseListener, MouseMotionListener
	{
		@Override
		public void mouseDragged(MouseEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent event)
		{
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseEntered(MouseEvent event)
		{
			hovered = true;
			repaint();
		}

		@Override
		public void mouseExited(MouseEvent event)
		{
			hovered = false;
			repaint();
		}

		@Override
		public void mousePressed(MouseEvent event)
		{
			//MDMovingCard mc = new MDMovingCard(null, new Point(40, 60), 2, new CardMoney(501, new Random().nextInt(5) + 1), new Point(300, /*(int) (Math.random() * 800)*/750), 2);
			//getParent().add(mc, 1);
			/*
			for (int i = 0 ; i < 5 ; i++)
			{
				for (Player player : getClient().getAllPlayers())
				{
					getClient().moveQueue.addMove(new CardMove(new CardMoney(501, new Random().nextInt(5) + 1), getCollection(), player.getHand(), 0));
				}
			}
			*/
			//getClient().moveQueue.addMove(new CardMove(getClient().getThePlayer().getHand().getCardAt(0), getClient().getThePlayer().getHand(), getClient().getDiscardPile(), -1));
			//getClient().moveQueue.addMove(new CardMove(new CardMoney(501, new Random().nextInt(5) + 1), getCollection(), /*getClient().getOtherPlayers().get(1).getHand()*/getClient().getThePlayer().getHand(), 1));
			//getClient().getEventQueue().addTask(new CardMove(new CardProperty(501, PropertyColor.LIGHT_BLUE, new Random().nextInt(5) + 1, "Property"), getCollection(), getClient().getThePlayer().getPropertySets().get(0), -1));
			
			//getClient().getEventQueue().addTask(new CardMove(new CardActionRent(501, 3, "RENT", PropertyColor.values()), getCollection(), getClient().getThePlayer().getHand(), 0));
			
			if (getClient().canDraw() && !getClient().isInputBlocked())
			{
				getClient().draw();
			}
		}

		@Override
		public void mouseReleased(MouseEvent event)
		{
			// TODO Auto-generated method stub
			
		}
	}
}
