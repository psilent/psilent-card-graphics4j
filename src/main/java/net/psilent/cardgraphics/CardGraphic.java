//   Copyright 2010 Arnold B. Spence
//
//   Licensed under the Apache License, Version 2.0 (the "License");
//   you may not use this file except in compliance with the License.
//   You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
//   Unless required by applicable law or agreed to in writing, software
//   distributed under the License is distributed on an "AS IS" BASIS,
//   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//   See the License for the specific language governing permissions and
//   limitations under the License.

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

/**
 * A playing card graphic derived from a JLabel which can be placed on a form at design-time and
 * is the main class of this package.
 * <p>
 * The card can display either a FRONT or BACK {@link net.psilent.cardgraphics.CardTypes.Facing  Facing},
 * a {@link net.psilent.cardgraphics.CardTypes.Rank  Rank} (ACE, ONE, TWO, etc.) and a standard
 * {@link net.psilent.cardgraphics.CardTypes.Suit  Suit}.
 * <p>
 * When clicked, the card is highlighted and the word "HELD" is displayed over it. Clicking again
 * clears it. Toggling held-mode by clicking can be enabled or disabled by calling {@link net.psilent.cardgraphics.CardGraphic#setClickable setClickable}(false).
 *
 * @author Arnold B. Spence
 */
public class CardGraphic extends JLabel
{
    private static final long serialVersionUID = 1L;
    
    /**
     * Loads card image graphics, initializes the size of the widget to match the images and
     * enables mouse and hierarchy events.
     */
    public CardGraphic()
    {
        // Cause processxxxEvent to be called for the following event types
        // even if there are no listeners
        enableEvents(AWTEvent.MOUSE_EVENT_MASK);
        enableEvents(AWTEvent.HIERARCHY_EVENT_MASK);

        initializeCardFaces();

        setPreferredSize(new Dimension(cardFaces[0][0].getIconWidth(), cardFaces[0][0].getIconHeight()));
    }

    /**
     * Sets whether or not the card displays the held-mode highlighting.
     * @param a_held    When set to true, the card is highlighted and overlaid with the word "HELD".
     */
    public void setHeld(boolean a_held)
    {
        held = a_held;
        repaint();
    }

    /**
     * Returns true if held-mode highlighting is on.
     * @return true if held-mode highlighting is on.
     */
    public boolean isHeld()
    {
        return held;
    }

    /**
     * Sets whether the card responds to mouse clicks for setting held-mode.
     * @param clickable true if the card should toggle held-mode when clicked.
     */
    public void setClickable(boolean clickable)
    {
        this.clickable = clickable;
    }

    /**
     * The hierarchy event is used in order to know when the widget has a valid graphics context
     * and it is the earliest point we can make calculations for for the "held" mode graphics.
     */
    @Override
    protected void processHierarchyEvent(HierarchyEvent e)
    {        
        if((e.getChangeFlags() & HierarchyEvent.SHOWING_CHANGED) != 0)
        {
            InitializeGraphics();
        }
        super.processHierarchyEvent(e);
    }

    /**
     * Allows the card to receive mouse clicks.
     */
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

    /**
     * Renders the highlighting and "HELD" text when in held-mode.
     */
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
            g2d.setColor(new Color(0, 155, 0));
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

    /**
     * Calculates the dimensions needed for rendering the held-mode highlighting
     * and initializes the card image.
     */
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
    
    /**
     * Loads the card image resources into a two dimensional array that can be
     * selected by <code>Suit</code> and <code>Rank</code>.
     */
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


    /**
     * Selects the appropriate image based on the current <code>Facing</code>, <code>Suit</code> and <code>Rank</code>.
     */
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

