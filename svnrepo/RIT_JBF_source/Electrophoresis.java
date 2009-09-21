// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 4/29/2009 2:30:00 PM
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   D:\Dave\Java\Electrophoresis\Electrophoresis.java

import java.applet.*;
import java.awt.*;
import java.io.PrintStream;
import java.net.MalformedURLException;
import java.net.URL;

public class Electrophoresis extends Applet
{

    public void addStandard()
    {
        simPanel.addStandard();
    }

    public void stopRun()
    {
        playClick1();
        simPanel.stopRun();
    }

    public void displaySim()
    {
        rightLayout.show(rightPanel, "simulation");
    }

    public void playClick1()
    {
        click1.play();
    }

    public void playHarp()
    {
        harp.play();
    }

    public void playTone6()
    {
        tone6.play();
    }

    public void playTone2()
    {
        tone2.play();
    }

    public void playDrip()
    {
        drip.play();
    }

    public boolean action(Event event, Object obj)
    {
        if(event.target instanceof Button)
            return handleButton(obj);
        else
            return false;
    }

    public boolean handleButton(Object obj)
    {
        if("Set Parameters".equals(obj))
        {
            leftLayout.show(leftPanel, "parameters");
            playThinBeep();
            return true;
        }
        if("Protein Info".equals(obj))
        {
            rightLayout.show(rightPanel, "data");
            playThinBeep();
            return true;
        }
        if("Simulation".equals(obj))
        {
            rightLayout.show(rightPanel, "simulation");
            playThinBeep();
            return true;
        }
        if("Plot Results".equals(obj))
        {
            leftLayout.show(leftPanel, "dataplot");
            playThinBeep();
            return true;
        }
        if("How To...".equals(obj))
        {
            playSpaceMusic();
            try
            {
                howURL = new URL(getCodeBase(), "Electro_How.html");
            }
            catch(MalformedURLException _ex)
            {
                System.out.println("Bad URL: " + howURL);
                return true;
            }
            if(howURL != null)
                getAppletContext().showDocument(howURL);
            return true;
        }
        if("Definitions".equals(obj))
        {
            playCrunch();
            try
            {
                defURL = new URL(getCodeBase(), "Electro_Def.html");
            }
            catch(MalformedURLException _ex)
            {
                System.out.println("Bad URL: " + defURL);
                return true;
            }
            if(defURL != null)
                getAppletContext().showDocument(defURL);
            return true;
        } else
        {
            return false;
        }
    }

    public void init()
    {
        LoadSounds();
        hiThere.play();
        paramPanel = new Parameters(this);
        paramPanel.setBackground(Color.gray);
        simPanel = new Simulation(this);
        dataPanel = new ProteinData(this);
        dataPanel.setBackground(Color.white);
        plotPanel = new Plot(this);
        buttonPanel.setBackground(Color.white);
        masterPanel.setBackground(Color.gray);
        rightPanel.setBackground(Color.black);
        leftPanel.setBackground(Color.black);
        buttonPanel.add(new Button("How To..."));
        buttonPanel.add(new Button("Set Parameters"));
        buttonPanel.add(new Button("Plot Results"));
        buttonPanel.add(new Button("Simulation"));
        buttonPanel.add(new Button("Protein Info"));
        buttonPanel.add(new Button("Definitions"));
        rightLayout = new CardLayout();
        rightPanel.setLayout(rightLayout);
        rightPanel.add("simulation", simPanel);
        rightPanel.add("data", dataPanel);
        leftLayout = new CardLayout();
        leftPanel.setLayout(leftLayout);
        leftPanel.add("parameters", paramPanel);
        leftPanel.add("dataplot", plotPanel);
        masterPanel.setLayout(new GridLayout(1, 2, 5, 5));
        masterPanel.add(leftPanel);
        masterPanel.add(rightPanel);
        setLayout(new BorderLayout());
        add("North", buttonPanel);
        add("Center", masterPanel);
        paramPanel.setDefaults();
    }

    public void playTone5()
    {
        tone5.play();
    }

    public void playTrain()
    {
        train.play();
    }

    public void playDing()
    {
        ding.play();
    }

    public void setAcrylaminde(Acrylamide acrylamide)
    {
        simPanel.setAcrylamide(acrylamide);
    }

    public void playTone7()
    {
        tone7.play();
    }

    public void playClick2()
    {
        click2.play();
    }

    public void playTone3()
    {
        tone3.play();
    }

    public void playCrunch()
    {
        crunch.play();
    }

    public void startRun(Protein aprotein[], Protein protein, Protein protein1, Protein protein2)
    {
        simPanel.startRun(aprotein, protein, protein1, protein2);
    }

    public void setPlotData(Protein aprotein[], Protein protein, Protein protein1)
    {
        plotPanel.setResults(aprotein, protein, protein1);
    }

    public void displayData()
    {
        rightLayout.show(rightPanel, "data");
    }

    public void playTone4()
    {
        tone4.play();
    }

    public Electrophoresis()
    {
        buttonPanel = new Panel();
        masterPanel = new Panel();
        leftPanel = new Panel();
        rightPanel = new Panel();
    }

    public void playClock_20__20__20__20_()
    {
        clock_20__20__20__20_.play();
    }

    public void play_20__20__20__20_()
    {
        _20__20__20__20_.play();
    }

    public void addSample()
    {
        simPanel.addSample();
    }

    private void LoadSounds()
    {
        gong = getAudioClip(getCodeBase(), "gong.au");
        train = getAudioClip(getCodeBase(), "Train.au");
        _20__20__20__20_ = getAudioClip(getCodeBase(), "    .au");
        drip = getAudioClip(getCodeBase(), "Drip.au");
        hiThere = getAudioClip(getCodeBase(), "Hello.au");
        tone4 = getAudioClip(getCodeBase(), "0.au");
        tone1 = getAudioClip(getCodeBase(), "1.au");
        tone2 = getAudioClip(getCodeBase(), "2.au");
        tone5 = getAudioClip(getCodeBase(), "3.au");
        tone3 = getAudioClip(getCodeBase(), "4.au");
        tone6 = getAudioClip(getCodeBase(), "7.au");
        tone7 = getAudioClip(getCodeBase(), "8.au");
        click1 = getAudioClip(getCodeBase(), "Button_1.au");
        click2 = getAudioClip(getCodeBase(), "Button_2.au");
        harp = getAudioClip(getCodeBase(), "Harp.au");
        spaceMusic = getAudioClip(getCodeBase(), "Spacemus.au");
        clock_20__20__20__20_ = getAudioClip(getCodeBase(), "Clock.au");
        thinBeep = getAudioClip(getCodeBase(), "ThinBeep.au");
        ding = getAudioClip(getCodeBase(), "Ding.au");
        calculate = getAudioClip(getCodeBase(), "Computer.au");
        crunch = getAudioClip(getCodeBase(), "Crunch.au");
    }

    public void setAnimationSpeed(String s)
    {
        simPanel.setPause(s);
    }

    public void playThinBeep()
    {
        thinBeep.play();
    }

    public void playTone1()
    {
        tone1.play();
    }

    public void playSpaceMusic()
    {
        spaceMusic.play();
    }

    public void displayProtein(Protein protein)
    {
        dataPanel.displayData(protein);
    }

    public void playCalculate()
    {
        calculate.play();
    }

    URL howURL;
    URL defURL;
    AudioClip gong;
    AudioClip _20__20__20__20_;
    AudioClip train;
    AudioClip drip;
    AudioClip hiThere;
    AudioClip tone1;
    AudioClip tone2;
    AudioClip tone3;
    AudioClip tone4;
    AudioClip tone5;
    AudioClip tone6;
    AudioClip tone7;
    AudioClip click1;
    AudioClip click2;
    AudioClip harp;
    AudioClip spaceMusic;
    AudioClip clock_20__20__20__20_;
    AudioClip thinBeep;
    AudioClip ding;
    AudioClip calculate;
    AudioClip crunch;
    Parameters paramPanel;
    Simulation simPanel;
    ProteinData dataPanel;
    Plot plotPanel;
    CardLayout leftLayout;
    CardLayout rightLayout;
    Panel buttonPanel;
    Panel masterPanel;
    Panel leftPanel;
    Panel rightPanel;
}