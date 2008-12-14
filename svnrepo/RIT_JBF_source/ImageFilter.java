import java.io.File;
import java.io.FilenameFilter;
import javax.swing.filechooser.*;

/* ImageFilter.java is a 1.4 example used by FileChooserDemo2.java. */
public class ImageFilter implements FilenameFilter {

    //Accept all directories and all gif, jpg, tiff, or png files.
    public boolean accept(File f, String s) {
	//System.out.println( s );
        String extension = s.substring(s.lastIndexOf(".") + 1);
        if (extension != null) {
	    //	    System.out.println(extension);
            if (extension.equals("e2d") || 
		extension.equals("gbk") ||
		extension.equals("faa") ||
		extension.equals("pdb") ||
		extension.equals("fasta")) {
                    return true;
            } else {
                return false;
            }
        }

        return false;
    }

    //The description of this filter
    public String getDescription() {
        return "Just Images";
    }
}
