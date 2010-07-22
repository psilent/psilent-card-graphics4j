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
