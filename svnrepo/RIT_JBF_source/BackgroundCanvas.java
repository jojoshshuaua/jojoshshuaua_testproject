import java.awt.*;

public class BackgroundCanvas extends Canvas {
    
    private Color bgColor;
    private Color stringColor;
    private Color titleColor;
    private Electro2D electro2D;
    
    public BackgroundCanvas( Electro2D e ){
	electro2D = e;
	this.setBounds( 0, 0, e.getWidth(), e.getHeight() );
	bgColor = new Color(176, 196, 222);
    }

    public void paint( Graphics g ){}
}
