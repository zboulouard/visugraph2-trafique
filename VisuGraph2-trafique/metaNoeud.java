import java.util.Vector;

public class metaNoeud extends Sommet {
    //    public String label =new String();
    public Vector sommets  = new Vector() ;
    public Vector aretes   = new Vector();

    metaNoeud(String lab, int x, int y, int z) {
	super(x, y,z, lab,0,0,0,0,0 );
    }

    
}
