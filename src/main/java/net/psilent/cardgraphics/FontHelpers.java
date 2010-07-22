package net.psilent.cardgraphics;

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

public class FontHelpers
{
    public static int getMaxFontSizeForRect(Graphics a_g, Font a_font, String a_text, int a_width, int a_height)
    {
        int minSize     = 0;
        int maxSize     = 1000;
        int currentSize = a_font.getSize();

        while(maxSize - minSize > 2)
        {
            FontMetrics fm = a_g.getFontMetrics(new Font(a_font.getName(), a_font.getStyle(), currentSize));
            int fontWidth = fm.stringWidth(a_text);
            int fontHeight = fm.getHeight();

            if((fontWidth > a_width) || (fontHeight > a_height))
            {
                maxSize = currentSize;
                currentSize = (maxSize + minSize) / 2;
            }
            else
            {
                minSize = currentSize;
                currentSize = (minSize + maxSize) / 2;
            }
        }

        return currentSize;
    }

}
