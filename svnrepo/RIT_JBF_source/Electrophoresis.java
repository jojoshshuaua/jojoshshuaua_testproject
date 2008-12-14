import javax.swing.*;
import java.awt.*;
import java.nio.Buffer;


/*
*
* Electrophoresis
*
*/

/* Method Electrophoresis
* Purpose Class constructor
* Allocate the necessary panels for the application
* Maintain communication between them
* Author D. Mix 08/15/1996
* Modified
*/
public class Electrophoresis extends Panel
{
Parameters paramPanel;
Simulation simPanel;
ProteinData dataPanel;
Plot plotPanel;

CardLayout leftLayout;
CardLayout rightLayout;

Panel buttonPanel = new Panel();
Panel masterPanel = new Panel();
Panel leftPanel = new Panel();
Panel rightPanel = new Panel();

//Protein stdsArray[];
//Protein sample, dye1, dye2;

public static void main( String args[] ){
	Frame f = new Frame();
	f.setBounds(0, 0, 550, 450);
	
	Electrophoresis el = new Electrophoresis();
	el.init();
	f.add(el);
	f.show();
}

public Electrophoresis(){
	this.init();
}

public void init()
{
this.setBounds( 0, 0, 550, 450);
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

buttonPanel.add(new Button("Set Parameters"));
buttonPanel.add(new Button("Plot Results"));
buttonPanel.add(new Button("Simulation"));
buttonPanel.add(new Button("Display Info"));

rightLayout = new CardLayout();
rightPanel.setLayout(rightLayout);
rightPanel.add("simulation",simPanel);
rightPanel.add("data",dataPanel);

leftLayout = new CardLayout();
leftPanel.setLayout(leftLayout);
leftPanel.add("parameters",paramPanel);
leftPanel.add("dataplot",plotPanel);

masterPanel.setLayout(new GridLayout(1,2,5,5));
masterPanel.add(leftPanel);
masterPanel.add(rightPanel);

setLayout(new BorderLayout());
add("North",buttonPanel);
add("Center",masterPanel);
}

/* Method action
* Purpose Catch keyboard or mouse events and pass them
* them to a handler
* Author D. Mix 08/15/1996
* Modified
*/
public boolean action(Event evt, Object arg)
{
if(evt.target instanceof Button)
return handleButton(arg);

return false;
}

/* Method handleButton
* Purpose Determine which button was pressed
* and call the corresponding layout
* Author D. Mix 08/15/1996
* Modified
*/
public boolean handleButton(Object arg)
{
if("Set Parameters".equals (arg))
{
leftLayout.show(leftPanel,"parameters");
return true;
}
else if("Display Info".equals (arg))
{
rightLayout.show(rightPanel,"data");
return true;
}
else if("Simulation".equals (arg))
{
rightLayout.show(rightPanel,"simulation");
return true;
}
else if("Plot Results".equals (arg))
{
leftLayout.show(leftPanel,"dataplot");
return true;
}

return false;
}

/* Method addStandard
* Purpose call the addStandard method in the Simulation class
* runs the animation to add the standard
* Author D. Mix 08/15/1996
* Modified
*/
public void addStandard()
{
//System.out.println("Electro.addSample");
simPanel.addStandard();
}

/* Method addSample
* Purpose call the addSample method in the Simulation class
* runs the animation to add the sample
* Author D. Mix 08/15/1996
* Modified
*/
public void addSample()
{
//System.out.println("Electro.addSample");
simPanel.addSample();
}

/* Method startRun
* Purpose call the startRun method in the Simulation class
* Author D. Mix 08/15/1996
* Modified
*/
public void startRun(Protein stdsArray[],Protein sample, Protein dye1,
Protein dye2) {
// maintain a copy of the Proteins
//System.out.println("Electro.startRun");
simPanel.startRun(stdsArray, sample, dye1, dye2);
}
public void stopRun()
{
//System.out.println("Electro.stopRun");
simPanel.stopRun();
}

public void setPlotData(Protein stdsArray[],Protein sample, Protein dye)
{
plotPanel.setResults(stdsArray, sample, dye);
}

public void setAnimationSpeed(String setting)
{
//System.out.println(setting);
simPanel.setPause(setting);
}

public void displayProtein(Protein proteinToDisplay)
{
//System.out.println("displayProtein");
//System.out.println(proteinToDisplay.name);
//System.out.println(proteinToDisplay.fullName);
//dataPanel.setColor(Color.blue);
dataPanel.displayData(proteinToDisplay);
}

public void playClick1(){}
public void playTone5(){}

}
