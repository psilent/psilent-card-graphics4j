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

