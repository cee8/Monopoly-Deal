package oldmana.md.server.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import oldmana.general.mjnetworkingapi.packet.Packet;
import oldmana.md.net.packet.server.PacketCardPropertyData;
import oldmana.md.server.card.type.CardType;

public class CardProperty extends Card
{
	public static CardTemplate RAINBOW_WILD;
	
	private List<PropertyColor> colors;
	private boolean base;
	
	@Override
	public void applyTemplate(CardTemplate template)
	{
		super.applyTemplate(template);
		colors = template.getColorList("colors");
		base = template.getBoolean("base");
	}
	
	public boolean isSingleColor()
	{
		return colors.size() == 1;
	}
	
	public boolean isBiColor()
	{
		return colors.size() == 2;
	}
	
	/**If a card is a base, it may be a color without having supporting properties
	 * 
	 * @return True if card is base
	 */
	public boolean isBase()
	{
		return base;
	}
	
	/**If there are multiple colors on the card, the first color is returned
	 * 
	 * @return First color of the card
	 */
	public PropertyColor getColor()
	{
		return colors.get(0);
	}
	
	public List<PropertyColor> getColors()
	{
		return colors;
	}
	
	public boolean hasColor(PropertyColor color)
	{
		return colors.contains(color);
	}
	
	public void setColors(PropertyColor... colors)
	{
		this.colors = new ArrayList<PropertyColor>(Arrays.asList(colors));
	}
	
	@Override
	public Packet getCardDataPacket()
	{
		byte[] types = new byte[colors.size()];
		for (int i = 0 ; i < types.length ; i++)
		{
			types[i] = colors.get(i).getID();
		}
		return new PacketCardPropertyData(getID(), getName(), getValue(), types, isBase(), getDescription().getID());
	}
	
	@Override
	public CardTypeLegacy getTypeLegacy()
	{
		return CardTypeLegacy.PROPERTY;
	}
	
	@Override
	public String toString()
	{
		String str = "CardProperty (" + colors.size() + " Color" + (colors.size() != 1 ? "s" : "") + ": ";
		for (PropertyColor color : colors)
		{
			str += color.getLabel() + "/";
		}
		str = str.substring(0, str.length() - 1);
		str += ") (Base: " + isBase() + ")";
		str += " (Name: " + getName() + ")";
		str += " (" + getValue() + "M)";
		return str;
	}
	
	
	/**
	 * Shortcut utility for creating properties easily.
	 */
	public static CardProperty create(int value, String name, PropertyColor... colors)
	{
		return CardType.PROPERTY.createCard(createTemplate(value, name, colors));
	}
	
	/**
	 * Shortcut utility for creating properties easily.
	 */
	public static CardProperty create(int value, String name, boolean base, PropertyColor... colors)
	{
		return CardType.PROPERTY.createCard(createTemplate(value, name, base, colors));
	}
	
	public static CardTemplate createTemplate(int value, String name, PropertyColor... colors)
	{
		return createTemplate(value, name, true, colors);
	}
	
	public static CardTemplate createTemplate(int value, String name, boolean base, PropertyColor... colors)
	{
		CardTemplate template = CardType.PROPERTY.getDefaultTemplate().clone();
		template.put("value", value);
		template.put("name", name);
		template.putColors("colors", colors);
		template.put("base", base);
		return template;
	}
	
	public static List<CardProperty> getPropertyCards(int[] ids)
	{
		List<CardProperty> props = new ArrayList<CardProperty>();
		List<Card> cards = Card.getCards(ids);
		for (Card card : cards)
		{
			props.add((CardProperty) card);
		}
		return props;
	}
	
	private static CardType<CardProperty> createType()
	{
		CardType<CardProperty> type = new CardType<CardProperty>(CardProperty.class, "Property");
		type.addExemptReduction("colors");
		type.addExemptReduction("base");
		type.addExemptReduction("value");
		CardTemplate dt = type.getDefaultTemplate();
		dt.put("name", "Generic Property");
		dt.putStrings("description", "Property cards can be played on your table. They are used to rent on and contribute " +
				"towards winning the game. They can be used to pay rent, either by choice or forcibly if you do not have the " +
				"money to pay the rent.");
		dt.put("revocable", true);
		dt.put("clearsRevocableCards", false);
		dt.putColors("colors", PropertyColor.RAILROAD);
		dt.put("base", true);
		
		RAINBOW_WILD = new CardTemplate(dt);
		RAINBOW_WILD.put("value", 0);
		RAINBOW_WILD.put("name", "Property Wild Card");
		RAINBOW_WILD.putStrings("description", "A property card that can be paired with any color. " +
				"It cannot be used for rent if you don't have another property with the color. "
				+ "This card cannot be stolen with Sly Deals, Forced Deals, or rent.");
		RAINBOW_WILD.putColors("colors", PropertyColor.getVanillaColors());
		RAINBOW_WILD.put("base", false);
		type.addTemplate(RAINBOW_WILD, "Rainbow Wild");
		return type;
	}
}