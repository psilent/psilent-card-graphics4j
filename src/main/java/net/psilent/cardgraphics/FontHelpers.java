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
