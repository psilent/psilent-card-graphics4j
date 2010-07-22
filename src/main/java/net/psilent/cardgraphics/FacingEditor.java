package net.psilent.cardgraphics;

import java.beans.*;

public class FacingEditor extends PropertyEditorSupport {

    public FacingEditor()
    {

    }

    @Override
    public String getAsText()
    {
        CardTypes.Facing f = (CardTypes.Facing) getValue();
        if (f == null)
        {
            return "No Rank Set";
        }

        return f.toString();
    }

    @Override
    public void setAsText(String s)
    {
        setValue(CardTypes.Facing.valueOf(s));
    }

    @Override
    public String[] getTags()
    {
        String [] tags = new String[CardTypes.Facing.values().length];

        int i = 0;
        for(CardTypes.Facing f : CardTypes.Facing.values())
        {
            tags[i] = f.toString();
            i++;
        }

        return tags;
    }

    @Override
    public String getJavaInitializationString()
    {
        CardTypes.Facing f = (CardTypes.Facing) getValue();
        if (f == null)
        {
            return "net.psilent.cardgraphics.CardTypes.Facing.FRONT";
        }

        return "net.psilent.cardgraphics.CardTypes.Facing." + f.toString();
    }
}

