// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 3/13/2009 10:23:08 AM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   D:\Dave\Java\Electrophoresis\ProteinData.java

import java.awt.*;

public class ProteinData extends Panel
{

    public void displayData(Protein protein)
    {
        name.setText(protein.name);
        fullName.setText(protein.fullName);
        abbr.setText(protein.abbr);
        mw = String.valueOf(protein.mw);
        molwt.setText(mw);
        double d = Math.log(protein.mw) / Math.log(10D);
        logMolWt.setText(String.valueOf(d));
    }

    ProteinData(Electrophoresis electrophoresis)
    {
        mw = "0";
        titlePanel = new Panel();
        namePanel = new Panel();
        fullNamePanel = new Panel();
        abbrPanel = new Panel();
        molWtPanel = new Panel();
        logMolWtPanel = new Panel();
        parent = electrophoresis;
        setLayout(new GridLayout(6, 1));
        titlePanel.setBackground(Color.lightGray);
        namePanel.setBackground(Color.lightGray);
        fullNamePanel.setBackground(Color.lightGray);
        abbrPanel.setBackground(Color.lightGray);
        molWtPanel.setBackground(Color.lightGray);
        logMolWtPanel.setBackground(Color.lightGray);
        titlePanel.add(new Label("PROTEIN DATA"));
        namePanel.add(new Label("Identifier"));
        name = new TextField(15);
        namePanel.add(name);
        fullNamePanel.add(new Label("Protein Name"));
        fullName = new TextField(28);
        fullNamePanel.add(fullName);
        abbrPanel.add(new Label("Abbreviation"));
        abbr = new TextField(10);
        abbrPanel.add(abbr);
        molWtPanel.add(new Label("Molecular Wt"));
        molwt = new TextField(10);
        molWtPanel.add(molwt);
        logMolWtPanel.add(new Label("Log Mol Wt"));
        logMolWt = new TextField(10);
        logMolWtPanel.add(logMolWt);
        name.setEditable(false);
        fullName.setEditable(false);
        abbr.setEditable(false);
        molwt.setEditable(false);
        logMolWt.setEditable(false);
        add(titlePanel);
        add(namePanel);
        add(fullNamePanel);
        add(abbrPanel);
        add(molWtPanel);
        add(logMolWtPanel);
    }

    Electrophoresis parent;
    String mw;
    TextField name;
    TextField fullName;
    TextField abbr;
    TextField molwt;
    TextField logMolWt;
    Panel titlePanel;
    Panel namePanel;
    Panel fullNamePanel;
    Panel abbrPanel;
    Panel molWtPanel;
    Panel logMolWtPanel;
}