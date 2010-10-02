// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 10/1/2009 1:02:31 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   validate.java

import java.awt.*;
import javax.swing.*;

class AmountTextField extends JTextField
{

    public AmountTextField(String s)
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
        try
        {
            int j = Integer.valueOf(getText().trim()).intValue();
            if(j > 0 && j <= 400)
                lastValue = getText();
            else
                setText(lastValue);
        }
        catch(NumberFormatException numberformatexception)
        {
            setText(lastValue);
        }
        return true;
    }

    String lastValue;
}