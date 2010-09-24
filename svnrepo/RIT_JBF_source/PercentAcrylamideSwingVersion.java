/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Amanda Fisher
 */

import javax.swing.*;

public class PercentAcrylamideSwingVersion extends JComboBox {

    public PercentAcrylamideSwingVersion() {

        super();
        addItem( "5" );
	addItem( "7.5" );
	addItem( "10" );
	addItem( "15" );
	addItem( "18" );
	addItem( "4 - 15" );
	addItem( "4 - 20" );
	addItem( "8 - 16" );
	addItem( "10 - 20" );
	setSelectedItem( "15" );

    }

}
