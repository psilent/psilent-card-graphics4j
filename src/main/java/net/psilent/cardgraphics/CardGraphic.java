package net.psilent.cardgraphics;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.AWTEvent;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.event.HierarchyEvent;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import net.psilent.cardgraphics.CardTypes.Facing;
import net.psilent.cardgraphics.CardTypes.Rank;
import net.psilent.cardgraphics.CardTypes.Suit;

public class CardGraphic extends JLabel
{
    public CardGraphic()
    {
        // Cause processxxxEvent to be called for the following event types
        // even if there are no listeners
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.HIERARCHY_EVENT_MASK);

        initializeCardFaces();

        setPreferredSize(new Dimension(cardFaces[0][0].getIconWidth(), cardFaces[0][0].getIconHeight()));
    }

    public void setHeld(boolean a_held)
    {
        held = a_held;
        repaint();
    }

    public boolean isHeld()
    {
        return held;
    }

    public void setClickable(boolean clickable)
    {
        this.clickable = clickable;
    }

    @Override
    protected void processHierarchyEvent(HierarchyEvent e)
    {        
        if((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0)
        {
            InitializeGraphics();
        }
        super.processHierarchyEvent(e);
    }

    @Override
    protected void processMouseEvent(MouseEvent e)
    {
        if(e.getID() == MouseEvent.MOUSE_PRESSED && this.clickable)
        {
            held = !held;
            repaint();
        }
        super.processMouseEvent(e);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
        super.paintComponent(g);

        if(held)
        {
            Graphics2D g2d = (Graphics2D) g;

            // Darken card with alpha blended overlay
            Composite composite = g2d.getComposite();
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, cardAlpha);
            g2d.setComposite(alphaComposite);
            g2d.setColor(new Color(0,155,0));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cardRectRadius, cardRectRadius);
            g2d.setComposite(composite);

            // Draw 'held' text rectangle and outline
            //g2d.setColor(Color.green);
            g2d.setColor(new Color(0, 120, 0));
            g2d.fillRoundRect(heldRectX, heldRectY, heldRectWidth, heldRectHeight, heldRectRadius, heldRectRadius);
            g2d.setColor(Color.white);
            g2d.drawRoundRect(heldRectX, heldRectY, heldRectWidth, heldRectHeight, heldRectRadius, heldRectRadius);

            // Draw 'held' text
            g2d.setFont(heldFont);
            g2d.setColor(Color.white);
            g2d.drawString(heldText, heldTextX, heldTextY);
        }
    }

    private void InitializeGraphics()
    {
        setText("");

        selectCardImage();

        // Calculate size of 'held' text rectangle
        heldRectWidth = getWidth() / 2;
        heldRectX = getWidth() / 2 - heldRectWidth / 2;
        heldRectHeight = getHeight() / 8;
        heldRectY = getHeight() / 2 - heldRectHeight / 2;

        Font f = new Font(heldFontName, Font.PLAIN, 20);

        Graphics g = getGraphics();
        int fontSize = 1;
        if(g != null)
        {
            fontSize = FontHelpers.getMaxFontSizeForRect(g, f, heldText, heldRectWidth, heldRectHeight);
            heldFont = new Font(heldFontName, Font.PLAIN, fontSize);

            FontMetrics fm = getGraphics().getFontMetrics(heldFont);
            heldTextX = heldRectX + (heldRectWidth - fm.stringWidth(heldText)) / 2;
            heldTextY = heldRectY + fm.getMaxAscent();
        }
    }
    
    private static void initializeCardFaces()
    {
        if (cardFaces[0][0] != null)
            return;

        for (Suit s : Suit.values())
        {
            for (Rank r : Rank.values())
            {
                cardFaces[s.ordinal()][r.ordinal()] = new ImageIcon(CardGraphic.class.getResource("/net/psilent/cardgraphics/images/cards/"
                        + s.toString().toLowerCase() + "_" + Integer.toString(r.ordinal() + 1) + ".png"));
            }
        }

        cardBack = new ImageIcon(CardGraphic.class.getResource("/net/psilent/cardgraphics/images/cards/back.png"));
    }

    private void selectCardImage()
    {
        if(facing == Facing.BACK)
        {
            setIcon(cardBack);
        }
        else
        {
            setIcon(cardFaces[suit.ordinal()][rank.ordinal()]);
        }
    }

    public Facing getFacing()
    {
        return facing;
    }

    public void setFacing(Facing facing)
    {
        if(facing == null)
            throw new IllegalArgumentException("Argument cannot be null.");

        this.facing = facing;
        selectCardImage();
    }

    public Rank getRank()
    {
        return rank;
    }

    public void setRank(Rank rank) 
    {
        if(rank == null)
            throw new IllegalArgumentException("Argument cannot be null.");
        
        this.rank = rank;
        selectCardImage();
    }

    public Suit getSuit()
    {
        return suit;
    }

    public void setSuit(Suit suit)
    {
        if(suit == null)
            throw new IllegalArgumentException("Argument cannot be null.");

        this.suit = suit;
        selectCardImage();
    }

    private boolean held = false;
    private Font heldFont = null;
    private String heldFontName = "arial";
    private int heldRectX = 0;
    private int heldRectY = 0;
    private int heldRectWidth = 0;
    private int heldRectHeight = 0;
    private int heldRectRadius = 15;
    private String heldText = "HELD";
    private int heldTextX = 0;
    private int heldTextY = 0;
    private int cardRectRadius = 25;
    private float cardAlpha = 0.59f;//0.25f;
    private boolean clickable = true;

    private Facing facing = Facing.FRONT;
    private Suit suit = Suit.CLUBS;
    private Rank rank = Rank.ACE;

    private static ImageIcon[][] cardFaces = new ImageIcon[Suit.values().length][Rank.values().length];
    private static ImageIcon cardBack;
}

