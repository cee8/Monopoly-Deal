package oldmana.general.md.client.gui.component;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;

import javax.swing.JComponent;
import javax.swing.SwingUtilities;

import oldmana.general.md.client.MDClient;
import oldmana.general.md.client.Player;

public class MDCreateSet extends MDComponent
{
	public MDCreateSet(Player player)
	{
		super();
		setSize(60, 90);
		MDClient client = MDClient.getInstance();
		setLocation(SwingUtilities.convertPoint(player.getUI().getPropertySets(), new Point(player.getUI().getPropertySets().getNextPropertySetLocX(), 0), 
				client.getTableScreen()));
	}
	
	@Override
	public void paintComponent(Graphics gr)
	{
		Graphics2D g = (Graphics2D) gr;
		g.setColor(Color.LIGHT_GRAY);
		g.fillRect(0, 0, getWidth() - 1, getHeight() - 1);
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		g.setColor(Color.BLACK);
		g.drawOval(scale(15), scale(30), scale(30), scale(30));
		g.setColor(Color.GREEN);
		g.fillRect(scale(27), scale(35), scale(6), scale(20));
		g.fillRect(scale(20), scale(42), scale(20), scale(6));
	}
}
