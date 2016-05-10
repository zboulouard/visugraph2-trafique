import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

class NomLongs extends HashMap{
    public Map data;
    public String nom1;

    public NomLongs() {
	data = new HashMap();
    }

    public void ajouterEntree(String nomcourt, String  nomlong) {
	if ( data.containsKey( nomcourt ) ) {
	    // le sommet est déjà présent
	    //return false;
	}
	else {
	    // le sommet  n'est pas présent
	    data.put( nomcourt,  nomlong);
	    //return true;
	}
    }
    public void LireNomsLongs(String nom_fichier, int type) {
	try {
		File file = new File (nom_fichier);
		try{
			Reader rd_result = new FileReader (file);
		}
		catch(Exception n){
			String nom="";
			String nom2="";
			if (type==0|type==1){ 
				nom=nom_fichier.substring(nom_fichier.length()-10, nom_fichier.length()-5);
				
				nom2=(nom_fichier.substring(0,nom_fichier.length()-10));
				nom2+=(nom.toUpperCase());
				nom2+=nom_fichier.substring(nom_fichier.length()-5, nom_fichier.length());}
			else  {
				nom=nom_fichier.substring(nom_fichier.length()-13,nom_fichier.length()-5);
				nom2=(nom_fichier.substring(0,nom_fichier.length()-10));
				nom2.concat(nom.toUpperCase());
				nom2+=nom_fichier.substring(nom_fichier.length()-5, nom_fichier.length());}
			file = new File(nom2);
			Reader rd_result = new FileReader (file);
			
		}
		
		Reader rd_result = new FileReader (file);
		String ligne_doc = null;
	    LineNumberReader line = new LineNumberReader(rd_result);
		   
	    // lecture des 2 lignes du fichier
	    ligne_doc = line.readLine();
	    ligne_doc = line.readLine();
	    nom1=ligne_doc;
	    ligne_doc = line.readLine();
	      
	    while ( ligne_doc != null ) {
		boolean fini = false;
		StringTokenizer lab = new StringTokenizer(ligne_doc, " " );
		int nb = lab.countTokens();
		
		while ( lab.hasMoreTokens() && !fini) {
		   
		    String nomcourt = lab.nextToken();
		    String nomlong  = lab.nextToken();
	
		    int i = 1;
		    if ( nb > 3 ) {
			while ( lab.hasMoreTokens() && i < nb-2 ) {
			    String morceau = lab.nextToken();
			    nomlong = nomlong.concat(" ");
			    nomlong = nomlong.concat(morceau);	
			    i++;
			}
		    }
		    //System.out.println("nomcourt " + nomcourt + "      nomlong " + nomlong);
		    ajouterEntree( nomcourt, nomlong);
		    fini = true;
		}
		ligne_doc = line.readLine();
		
	    }
	    line.close();
	} 
	catch ( IOException e2 ) {System.err.println("\n Désolé, vous ne disposez pas de noms longs \n" );}
//	    System.err.println("\n Désolé, vous ne disposez pas de noms longs \n" ); 
//	    //System.exit(1);    
//	}
	catch (NullPointerException e3  ) {	} 
    }
}
