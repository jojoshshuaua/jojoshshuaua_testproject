// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/1/2009 1:00:56 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   validate.java

import java.awt.*;
import javax.swing.*;

class ConcenTextField extends JTextField
{

    public ConcenTextField(String s)
    {
        super(s);
    }

    public boolean keyUp(Event event, int i)
    {
        String s = getText().trim();
        if(s.equals(""))
        {
            lastValue = getText();
            return true;
        }
        if(i == 9 || i == 10 || event.id == 404)
            return false;
        if(i >= 48 && i <= 57 || i == 46 || i == 8 || i == 127)
            try
            {
                float f = Float.valueOf(getText().trim()).floatValue();
                if((double)f >= 0.0D && (double)f <= 1.0D)
                {
                    lastValue = getText();
                    return true;
                }
            }
            catch(NumberFormatException numberformatexception)
            {
                setText(lastValue);
            }
        setText(lastValue);
        return true;
    }

    String lastValue;
}