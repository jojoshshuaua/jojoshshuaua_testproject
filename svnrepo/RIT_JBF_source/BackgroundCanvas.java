import javax.swing.*;

public class BackgroundCanvas {

    private Electro2D electro2D;
    private JPanel background;

    public BackgroundCanvas( Electro2D e ){
	electro2D = e;
	background = new JPanel();
        background.add(electro2D);
    }

}
