import java.io.IOException;

public class BrowserLauncher {

	/**
	 * Attempts to open the default web browser to the given URL.
         *
         * Code was written by Dem Pilafian and was retrieved from Bare Bones
         * Browser Launch for Java on April 16, 2012 by Amanda Fisher.
         * http://www.centerkey.com/java/browser/
         *
	 * @param url The URL to open
	 * @throws IOException If the web browser could not be located or does not run
	 */
	public static void openURL(String url) throws IOException {
            try {
                java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
	}
}
