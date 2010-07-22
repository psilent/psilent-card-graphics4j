package net.psilent.cardgraphics;

import java.beans.*;

public class SuitEditor extends PropertyEditorSupport
{
    @Override
    public String getAsText()
    {
        CardTypes.Suit s = (CardTypes.Suit) getValue();
        if (s == null)
        {
            return "No Rank Set";
        }

        return s.toString();
    }

    @Override
    public void setAsText(String s)
    {
        setValue(CardTypes.Suit.valueOf(s));
    }

    @Override
    public String[] getTags()
    {
        String [] tags = new String[CardTypes.Suit.values().length];
        
        int i = 0;
        for(CardTypes.Suit s : CardTypes.Suit.values())
        {
            tags[i] = s.toString();
            i++;
        }

        return tags;
    }

    @Override
    public String getJavaInitializationString()
    {
        CardTypes.Suit s = (CardTypes.Suit) getValue();
        if (s == null)
        {
            return "net.psilent.cardgraphics.CardTypes.Suit.CLUBS";
        }

        return "net.psilent.cardgraphics.CardTypes.Suit." + s.toString();
    }
}
