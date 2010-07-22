package net.psilent.cardgraphics;

import java.beans.*;

public class RankEditor extends PropertyEditorSupport
{
    @Override
    public String getAsText()
    {
        CardTypes.Rank r = (CardTypes.Rank) getValue();
        if (r == null)
        {
            return "No Rank Set";
        }

        return r.toString();
    }

    @Override
    public void setAsText(String s)
    {
        setValue(CardTypes.Rank.valueOf(s));
    }

    @Override
    public String[] getTags()
    {
        String [] tags = new String[CardTypes.Rank.values().length];

        int i = 0;
        for(CardTypes.Rank r : CardTypes.Rank.values())
        {
            tags[i] = r.toString();
            i++;
        }
        
        return tags;
    }

    @Override
    public String getJavaInitializationString()
    {
        CardTypes.Rank r = (CardTypes.Rank) getValue();
        if (r == null)
        {
            return "net.psilent.cardgraphics.CardTypes.Rank.ACE";
        }

        return "net.psilent.cardgraphics.CardTypes.Rank." + r.toString();
    }
}
