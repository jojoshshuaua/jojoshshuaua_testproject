// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/13/2009 10:25:59 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   D:\Dave\Java\Electrophoresis\Acrylamide.java


public class Acrylamide
{

    Acrylamide(String s, double d)
    {
        percentGel = "0.0%";
        suppressor = 1;
        percentGel = s;
        concentration = d;
        setSuppressor(concentration);
    }

    public double getConc()
    {
        return concentration;
    }

    public void setSuppressor(double d)
    {
        if(d > 12D)
        {
            suppressor = 6;
            return;
        }
        if(d > 10D)
        {
            suppressor = 3;
            return;
        }
        if(d > 7.5D)
        {
            suppressor = 2;
            return;
        } else
        {
            suppressor = 1;
            return;
        }
    }

    private double concentration;
    public String percentGel;
    public int suppressor;
}