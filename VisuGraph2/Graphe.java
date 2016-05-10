import java.awt.Color;import java.io.*;
import java.awt.Graphics;
import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.io.Writer;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import java.io.*;
import java.nio.*;
import java.nio.charset.*;
import java.util.ResourceBundle;
import java.util.Locale;
import java.lang.Object;
import java.net.URLDecoder;
/**
 * Represente un graphe avec un ensemble de sommets et un ensemble d'aretes.
 * le graphe peut etre simple ou complexe, oriente ou non oriente. La classe
 * Graphe contient des methodes pour afficher et manipuler des graphes.
 */
class Graphe extends JPanel {

	public int largeur=800;
	public int longueur=830;
    public Vector sommets;
    public Vector aretes;
    private boolean oriente;
    private int complexite = 0; // indique le nombre d'aretes avec aretes paralleles
    public boolean multi = false;
    public Sommet centre = null;
    public double[][] matrice;
    public int typeMat = 0;
    public double seuil;
    public double intensite = 2;
    public boolean stress = false;
    public boolean noms = true;
    public boolean masque = false;
    public boolean connexe = false;
    public boolean clustering = false;
    public double maxar = 0;   //  Pour les aretes, maximum des instances du morphing.
    public int M = 0;
    public boolean longs = false; //  affichage des noms longs des sommets
    public boolean sans = true;   //  sans affichage des labels  des sommets

    double maxMetrique1 = 0, maxMetrique2 = 0;
    double minMetrique1 = 0, minMetrique2 = 0;
    double maxArete = 0;
    double maxArete2 = 0;
    double minArete = 0;
    int typeGraphe = 1;
    int rang = 1;
    int pre_instance = 1;
    static final int flag = 0;
    int cercle = 2;
    int instance = 0;
    // **********************************************
    Graphe(int nbsommets) {
	sommets = new Vector(nbsommets);
	aretes = new Vector();
	seuil = 0.0;
    }

    Graphe(Vector sommet, Vector arete ) {
	sommets = sommet;
	aretes = arete;
    }

    Graphe(boolean oriente)
    {
	sommets = new Vector();
	aretes = new Vector();
	this.oriente = oriente;
	seuil = 0.0;
    }
    
    public void Verifie_format_matrice(File file){
    	try {
	        Reader rd_result = new FileReader (file);
		    String ligne_doc = null;
		    LineNumberReader line = new LineNumberReader(rd_result);
	 	    line = new LineNumberReader(rd_result);
	 	    ligne_doc = line.readLine();

	 	    /*while (file.canWrite()){
	 	    	
	 	    	PrintStream pfos= new PrintStream(fos);
	 	    	pfos.println(ligne_doc+" * *");
	 	    }*/

	 	 //  ligne_doc = line.readLine();
	
	 	    if (!ligne_doc.contains("CLASSE")|!ligne_doc.contains("POIDS"))
	 	    {	ligne_doc+="CLASSE POIDS \n";
	 	    	JOptionPane.showMessageDialog(null,"La matrice n'est pas conforme. veuillez l'enregistrer de nouveau sous le tableur 2D","ATTENTION" ,JOptionPane.INFORMATION_MESSAGE);
	 	    	System.exit(0);
	 	/*    	String nom_mat=file.getName();
	 	    	System.out.println("on cree un nouveau fichier");
	 	 	   FileOutputStream fos = new FileOutputStream(file);
		    	PrintStream pfos= new PrintStream(fos);
	 	    	pfos.println(ligne_doc+" aaaaaaaaaaaaaa");
		 	    fos.close();*/
		 	    
	 	   // 	FileWriter correction = new FileWriter(nom_mat+"BON");
	 	    	//correction.write(ligne_doc);
/*	 	    	FileWriter correct =new FileWriter(file);
	 	    	correct.write(ligne_doc);
	 	    	ligne_doc = line.readLine();
	 	    	while ( (ligne_doc = line.readLine()) != null) {
	 	    		if (ligne_doc.charAt(ligne_doc.length()-1)==' ')
	 	    		ligne_doc +="0 2 \n";
	 	    		else ligne_doc +=" 0 2\n";
	 	    		correct.write(ligne_doc);
	 	    	}
	 			   // line.close();
	 	    	line.close();
	 	    	correct.close(); */
	 	   	}
	    }
		   
		catch( IOException e2 ) {
		    System.err.println(e2); System.exit(1);

		}
		catch (NullPointerException e3  ) {
		    System.out.println("\nNullPointerException dans isntance 0: " + e3 );
		}
    }
    public void enregistre( String nom_fichier, int periode  ){
    	boolean non_enregistre=true;
    	try{
    		String InstanceFile = nom_fichier.concat(".")+periode;
	    	instance = periode;
		    File file = new File (InstanceFile);
		    Reader rd_result = new FileReader (file);
		    String ligne_doc = null;
		    LineNumberReader line = new LineNumberReader(rd_result);
		    // Lecture de la premiere ligne du fichier matrice pour generer les sommets
	            ligne_doc = line.readLine();
		    StringTokenizer labc = new StringTokenizer(ligne_doc);
		    String lab = labc.nextToken();
		    int nb=labc.countTokens();
		    int i=0;
			while(labc.hasMoreTokens()){
				i+=1;
				if (i==nb && !labc.nextToken().equals("POIDS")) {non_enregistre=true; }
				else labc.nextToken();
			}
			if (non_enregistre==true){
				for (int t=1; t<periode+1; t++){
					File f= new File (nom_fichier.concat(".")+t);
					Reader r = new FileReader(f);
					File f2 = new File(nom_fichier.concat(".")+t+t);
					Writer out = new FileWriter (f2);
					 String ligne = null;
					 LineNumberReader lin = new LineNumberReader(r);
				    ligne = lin.readLine();
				    out.write(ligne+" CLASSE "+"POIDS"+"\n");
				    while ( ligne != null ){
						ligne = lin.readLine();
				    if (ligne!=null)out.write(ligne+" 0 "+"1"+"\n");}
				    lin.close();
				    out.close();
				    f.delete();
				    f2.renameTo(f);
				}
			}
	    }catch(Exception n){}
    	
    }
    /**
     * Construit un graphe a partir de sa matrice d'adjacence sous forme d'un fichier texte.
     */
    public void ChargerMatriceSymetrique( String nom_fichier, int periode ) {
    	    	//System.out.println("Largeur : "+largeur+" longueur : "+longueur);
	int nb_aretes = 0;
	int ptx=0;
	int pty=0;
	int ptz=0;
	String InstanceFile = nom_fichier.concat(".")+periode;
	instance = periode;
	System.out.println("Lecture des sommets ...." );
	try {

	    File file = new File (InstanceFile);
	    Reader rd_result = new FileReader (file);
	    String ligne_doc = null;
	    LineNumberReader line = new LineNumberReader(rd_result);
	    // Lecture de la premiere ligne du fichier matrice pour generer les sommets
            ligne_doc = line.readLine();
	    StringTokenizer labc = new StringTokenizer(ligne_doc);
	    String lab = labc.nextToken();
	    
	    while ( lab != null  && !lab.equals("CLASSE") ) {
		// Creation du sommet correspondant a la colonne en cours
		/* pt.x et pt.y sont les valeurs des coordonn��es qui seront attribu��es aux sommets. En prenant comme
		valeur fixe 520 (largeur) et 550 (hauteur), on s'assure de laisser de l'espace pour afficher les noms
		des sommets. */
		ptx = 10 + (int)(520*Math.random() );
		pty = 10 + (int)(550*Math.random() );
		ptz = 10 + (int)(550*Math.random() );

		Sommet s = new Sommet(ptx, pty,ptz,lab.toLowerCase(),0,0,0 ,0,0);
		
		s.type = 1;
		s.metrique = 0;
		ajouterSommet(s);

		try {
		    lab = labc.nextToken();
		} catch (NoSuchElementException ex) {
		    break;
		}
		M++; // compteur nbre de sommet 
	    }

	    line.close(); // fin de lecture du fichier

	    //int perimetre=2300/(instance);
	    int perimetre=((largeur+longueur)*2)/(instance);
	    int p=perimetre;
	    int res=0;
	    CalculRepere(largeur, longueur, perimetre);
	    System.out.println("nombre M de sommet : " + M);
	    if (instance>1)M+=instance;
	    System.out.println(M + " sommets d�finis");
	    matrice = new double[M][M];

	    // a ce stade, nous avons cree tous les (M) sommets du graphe
	    // prochaine etape : creation des aretes entre ces sommets
	    // lecture du fichier ligne par ligne
	    if ( instance > 1 ) multi = true;

	    for ( int inst = 1; inst <= instance; inst++ ) {
		InstanceFile = nom_fichier.concat(".")+inst;
		file = new File (InstanceFile);
		rd_result = new FileReader (file);
		line = new LineNumberReader(rd_result);

		ligne_doc = line.readLine();
		ligne_doc = line.readLine();


		int l = 0; // compteur sur les lignes
		// Traitement d'une ligne du fichier
		int T=1;
		while ( ligne_doc != null && l < M+1-instance) {
		    Sommet s = getSommet(l);
		    s.Metrique[inst] = 0.0;
		    int j = 0; // compteur sur les colonnes
		    StringTokenizer label = new StringTokenizer(ligne_doc);
		    String str = label.nextToken();
		    int arret=0;
		    if (instance>1) arret=M+1-instance-2;
		    else arret=M+1-instance;
		    while ( label.hasMoreTokens() && j < arret) {
		    //System.out.println(j);
			// lecture des valeurs dans la ligne courante <=> des liens
			Sommet ss = getSommet(j);
			str = label.nextToken();
		   	str = str.replace(',', '.');
			try {
			    double val = Double.parseDouble(str);
	
			    if ( val != 0.0 ) {
				if ( s.Metrique[inst] <= val && !s.nom.contains("virtuel")){
				    s.Metrique[inst] = val;
				}
				if (j <= l ) {
			    
				    int indice = areteDansGraphe( s, ss );
				    if ( indice != -1 ) {
					Arete a = getArete(indice);
					a.addVal(val);
					a.setValInstance(val, inst);
				    if (val==16.0 && inst!=1) System.out.println("on a atteind 16");
				    for (int i=0; i<=inst; i++){
				      if (maxar<val) maxar=val;
				    }
					String nom;
					val = a.getVal();
					if ( val < 1.0 )  nom = String.valueOf(val);
					else  nom = ""+(String.valueOf((int)val));
					a.setNom(nom);

				    } else if ( !s.equals(ss) ) {
					Arete a = new Arete( s, ss, s.x, ss.x, null,
							     val, nb_aretes++ );
					ajouterArete(a);
					a.setValInstance( val, inst );
					String nom;
					val = a.getVal();
					if ( val < 1.0 )  nom = String.valueOf(val);
					else  nom = String.valueOf((int)val);
					a.setNom(nom);

					}
				    }

				}

			}

			catch ( NumberFormatException ex ) {
			    System.out.println("Lecture de la matrice : Format des valeurs incompatible");
			}
			j++;
		    }
		    l++;
		    ligne_doc = line.readLine();
		    T+=1;
		}
		line.close(); // fin de lecture du fichier
	    }
	}
	catch( IOException e2 ) {
	    System.err.println(e2); System.exit(1);

	}
	catch (NullPointerException e3  ) {
	    System.out.println("\nNullPointerException dans multi: " + e3 );
	}
//*********************
	double max = 0.0;
	int k = 1;

	for (int j = 0; j < nombreSommets(); j++ ) {
	    Sommet v =  getSommet(j);
	    v.metrique = 0.0;
	    Sommet SVirtuel;
	    for ( k = 1; k <= periode ; k++ ) {
	    	SVirtuel = getSommet (M-(periode-k+1));
		if ( v.metrique <= v.Metrique[k] )
		    v.metrique = v.Metrique[k];
		if ( max <= v.Metrique[k] ) max = v.Metrique[k];
		if (periode>1){
			if(v.Metrique[k] !=0){
				Arete aVirtuelle = new Arete(SVirtuel, v,SVirtuel.x,v.x, null,v.Metrique[k],nb_aretes++);
				ajouterArete(aVirtuelle);
				aVirtuelle.setValInstance(v.Metrique[k], k );
				aVirtuelle.setInvisible();
				aVirtuelle.setValInstance( v.Metrique[k], k );}
		}
		}
	    }


	for (int j = 0; j < nombreSommets(); j++ ) {
	    Sommet v =  getSommet(j);
	   v.Metrique[periode+1] = max;
	}
	System.out.println( "\n\n" );
	cercle = 1;
	if (periode>1) cercle=2;
	System.gc();
	maximumArete();

    }

    public void CalculRepere(int largeur, int longueur, int perimetre){
    	int pt1=0;
    	int pt2=0;
    	
    	if (instance>1){
    	int n=3;
    	double teta=(Math.PI/instance);
    	for (int i = 1; i < instance+1 ; i++){
    		pt1=(int)(((largeur-50)/2)*(1+(0.95*Math.sin(teta))));
    		pt2=(int)(((longueur-75)/2)*(1-0.95*Math.cos(teta)));
    		Sommet s = new Sommet(pt1,pt2,10, "sommet_virtuel_"+i,0,0,0,0,0);
    		ajouterSommet(s);
			s.fixe = true;
    		teta=n*(Math.PI/instance);
    		n+=2;
    	}
    	}

    }
    public void CalculRepere2(int large, int longu){
    	int pt1=0;
    	int pt2=0;
    	
    	if (instance>1){
    	int n=3;
    	double teta=(Math.PI/instance);
    	for (int i = 1; i < instance+1 ; i++){
    		String st=""+i;
    		pt1=(int)(((large-50)/2)*(1+(0.95*Math.sin(teta))));
    		pt2=(int)(((longu-75)/2)*(1-0.95*Math.cos(teta)));
    		for (int j =0; j < nombreSommets(); j++){
    			Sommet s=getSommet(j);
    			
    			if (s.nom.contains(st) && (s.nom.contains("virtuel")|s.nom.contains("Repere"))) {s.x=pt1; s.y=pt2;}
     		}

    		teta=n*(Math.PI/instance);
    		n+=2;
    		st="";
    	}
    	}

    }


    /**
     * Construit un graphe a partir de sa matrice sous forme d'un fichier texte.
     */
    public void ChargerMatriceAsymetrique( String nom_fichier, int periode ) {
      	int M = 0;
    	int N = 0;
    	int ptx=0;
    	int pty=0;
    	int ptz=0;
		int nb_aretes = 0;
    	double max = 0.0;
    	String InstanceFile = nom_fichier.concat(".")+periode;
    	instance = periode;
    	try {
    	    File file = new File (InstanceFile);
    	    Reader rd_result = new FileReader (file);
    	    String ligne_doc = null;
    	    LineNumberReader line = new LineNumberReader(rd_result);
    	    ligne_doc = line.readLine();

    	    StringTokenizer labc = new StringTokenizer(ligne_doc);
    	    String lab = labc.nextToken();
    	    /*Lecture des sommets en colonne */
    	    while ( lab != null  && !lab.equals("CLASSE") ) {
	    		ptx = 10 + (int)(730*Math.random() );
	    		pty = 10 + (int)(750*Math.random() );
	    		ptz = 10 + (int)(750*Math.random() );
	
	    		// Creation du sommet correspondant a la colonne en cours
	    		Sommet s = new Sommet(ptx, pty, ptz, lab,0,0,0,0 ,0);
	    		s.type = 0;
	    		s.metrique = 0;
	    		ajouterSommet(s);
	    		try{
	    		    lab = labc.nextToken();
	     		} catch (NoSuchElementException ex) {
	    		    break;
	    		}
	    		N++;
    	    }
    	    System.out.println("Fin de lecture des " + N + " sommets en colonnes.... " );

    	    // Cr�ation des sommets en ligne
    	    while ( (ligne_doc = line.readLine()) != null ) {
	    		labc = new StringTokenizer(ligne_doc);
	    		lab = labc.nextToken();
	    		ptx = 10 + (int)(730*Math.random() );
	    		pty = 10 + (int)(750*Math.random() );
	    		ptz = 10 + (int)(750*Math.random() );
	    		Sommet s = new Sommet(ptx, pty, ptz, lab,0,0,0 ,0,0);
	       		s.type = 1;
	    		s.metrique = 0;
	    		ajouterSommet(s);
	    	//	s.setInvisible();
	    		M++;
    	    }
    	    line.close();
    	    int perimetre=((largeur+longueur)*2)/(instance);
    	    int p=perimetre;
    	    CalculRepere(largeur, longueur, perimetre);
    	    if (instance>1){M+=instance;}
    	    System.out.println("Fin de lecture des " + M + " sommets en lignes.... " );
    	    matrice = new double[M][N];	  

    	    // a ce stade, nous avons cree tous les (N) sommets "colonnes" et sommets "lignes"
    	    // prochaine etape : creation des aretes entre ces sommets

    	    // lecture des lignes de la matrice ligne par ligne
    	    for ( int inst = 1; inst <= instance; inst++ ) {
    		InstanceFile = nom_fichier.concat(".")+inst;
    		//System.out.println("Traitement du fichier : " + InstanceFile);

    		file = new File (InstanceFile);
    		rd_result = new FileReader (file);
    		line = new LineNumberReader(rd_result);

    		ligne_doc = line.readLine();
    		ligne_doc = line.readLine();
    		int l = 0;   // leme ligne <=> leme sommet du graphe
    		
    		// lecture des cases de chaque ligne (case par case)
    		while ( ligne_doc != null && l < M+1-instance) {
    		    StringTokenizer ligne = new StringTokenizer(ligne_doc);
    		    lab = ligne.nextToken();
    		    //System.out.println("autre: "+ lab);
    		    int j = 0;
    		    Sommet s;
    		    if (instance >1) s = getSommet(N+l+instance); // N+l sommet dont le label=lab 
    		    else  s = getSommet(N+l); 
    		    
    		    
    		    while ( ligne.hasMoreTokens() && j < N  && j < N+1-instance) {
    			Sommet ss = getSommet(j); // jieme colonne

    			String str = ligne.nextToken();
    			str = str.replace(',', '.');
    			try {
    				
    			    double val = Double.parseDouble(str);
    			    // une case non nulle => creation d'une arete
    			    if ( val != 0.0 ) {
    			    	if (instance>1){
    			    		s.Metrique[inst] += val;
    			    		ss.Metrique[inst] += val;
    			    		if (max <= ss.Metrique[inst]) max=ss.Metrique[inst];
    			    		if (max <= s.Metrique[inst]) max=s.Metrique[inst];
    			    	
    			    	}
    			    	else{
		    				if ( s.Metrique[inst] <= val )
		    				    s.Metrique[inst] = val;	
		    				if ( ss.Metrique[inst] <= val )
		    				    ss.Metrique[inst] = val;
		    				if ( max <= val ) max = val;
		    				
    			    	}
    			    	
    			    	int indice = areteDansGraphe( s, ss );
    					if ( indice != -1 ) {
    					    Arete a = getArete(indice);
    					    a.addVal(val);
    					    a.setValInstance(val, inst);
    					    String nom;
    					    val = a.getVal();
    					    if ( val < 1.0 )
    						nom = String.valueOf(val);
    					    else
    						nom = String.valueOf((int)val);
    					    a.setNom(nom);
    					} else {
    					    Arete a = new Arete( s, ss, s.x, ss.x, null, val, nb_aretes++ );
    					    ajouterArete(a);
    					    a.setValInstance(val, inst);

    					    String nom;
    					    val = a.getVal();
    					    if ( val < 1.0 ) 
    						nom = String.valueOf(val);
    					    else
    						nom = String.valueOf((int)val);
    					    a.setNom(nom);
    					}
    			    }
    			} catch (NumberFormatException ex) {
    			    System.out.println ("Lecture de la matrice : Format des valeurs incompatible");
    			}
    			j++;
    		    }
    		    l++;
    		    ligne_doc = line.readLine();
    		}
    		line.close(); // fin de lecture du fichier
    	    }
    	}
    	catch( IOException e2 ) {
    	    System.err.println(e2); System.exit(1);
    	}
    	catch (NullPointerException e3  ) {
    	    System.out.println("\nNullPointerException : " + e3 );
    	}

    	int k = 1;

    	for (int j = 0; j < nombreSommets(); j++ ) {
    	    Sommet v =  getSommet(j);
       	    v.metrique = 0.0;
    	    Sommet SVirtuel;
    	    for ( k = 1; k <= periode ; k++ ) {
    	    	SVirtuel = getSommet (M+N-(k));
    		if ( v.metrique <= v.Metrique[k] )
    		    v.metrique = v.Metrique[k];
    	//	if ( max <= v.Metrique[k] ) max = v.Metrique[k];
    		if (periode>1){
	    		if(v.Metrique[k] !=0){
		    		Arete aVirtuelle = new Arete(SVirtuel, v,SVirtuel.x,v.x, null,v.Metrique[k], nb_aretes++ );
		    		ajouterArete(aVirtuelle);
		    		aVirtuelle.setValInstance(v.Metrique[k], k );
		    		aVirtuelle.setInvisible();
		    		aVirtuelle.setValInstance( v.Metrique[k], k );}
		    	}
    		}
    	 }

   	
    	for (int j = 0; j < nombreSommets(); j++ ) {
    	    Sommet v =  getSommet(j);
    	   v.Metrique[periode+1] = max;
    	}

      	cercle = 1;
      	if (periode>1) cercle=2;
    	System.gc();
    	maximumArete();

        }
        
  
    public boolean isEmpty() { return ( sommets.size() == 0); }
    public int nombreSommets() { return sommets.size(); }
    public int nombreAretes() { return aretes.size(); }
    public Sommet getSommet(int i) { return (Sommet)sommets.elementAt(i); }
    public Arete getArete(int i) { return (Arete)aretes.elementAt(i); }
    public boolean getOriente() { return oriente; }
    

    /**
     * ajoute un sommet dans le graphe. La methode verifie si aucun 
     * sommet de l'ensemble des sommets du graphe n'est equivalent au sommet
     * a ajouter, puis fait l'ajout si ce n'est pas le cas.
    */
   
    public void ajouterSommet(Sommet sommet)
    {
	for (int i = 0; i < sommets.size(); i++)
	    if (sommet.equals((Sommet)sommets.elementAt(i)))
		return;
	sommets.addElement(sommet);
    }
    
    /**
     * ajoute un ensemble de sommets.
     @param sommetsA sommets a ajouter
    */
    public void ajouterSommets(Sommet[] sommetsA)
    {
	for (int i = 0; i < sommetsA.length; i++)
	    ajouterSommet(sommetsA[i]);
    }
    
    /**
     * Ajoute une arete dans le graphe. La methode verifie si l'arete se trouve 
     * deja dans le graphe avant d'effectuer l'ajout.
     @param arete arete a ajouter
    */
    public void ajouterArete(Arete arete)
    {

	for (int i = 0; i < aretes.size(); i++)
	    if ((oriente && arete.equalsOriented((Arete)aretes.elementAt(i))) ||
		(!oriente && arete.equals((Arete)aretes.elementAt(i))))
		return;
	ajouterSommet(arete.getE1());
	ajouterSommet(arete.getE2());
	for (int i = 0; i < sommets.size(); i++) {
	    Sommet s = (Sommet)sommets.elementAt(i);
	    if ( s.equals(arete.getE1()) )
		arete.setE1(s);
	    else if ( s.equals(arete.getE2()) )
		arete.setE2(s);
	}
	Sommet s1 = arete.getE1();
	Sommet s2 = arete.getE2();
	int mx = (s1.getX()+s2.getX())/2;
	int my = (s1.getY()+s2.getY())/2;
	int p = 0;
	for ( int i = 0; i < aretes.size(); i++ ) {
	    Arete a = (Arete)aretes.elementAt(i);
	    if ( a.paralleleA(arete, false) && a.getID() != arete.getID() )
		p++;
	}
	my += 4*Sommet.RAYON*p*(int)Math.pow(-1.0,p+1);
	arete.setXYZ(mx,my, 0);
	aretes.addElement(arete);
	
	if (p > 0 ) complexite++;
    }

    /**
     * ajoute un ensemble d'aretes.
     @param aretesA aretes a ajouter
    */
    public void ajouterAretes(Arete[] aretesA)
    {
	for (int i = 0; i < aretesA.length; i++)
	    ajouterArete(aretesA[i]);
    }
    
    /**
     * ajoute un graphe dans ce graphe.
     @param graphe le graphe a ajouter
    */
    public void ajouterGraphe(Graphe graphe)
    {
	int ns = graphe.nombreSommets();
	for (int i = 0; i < ns; i++)
	    ajouterSommet(graphe.getSommet(i));

	int na = graphe.nombreAretes();
	for (int i = 0; i < na; i++) 
	    ajouterArete(graphe.getArete(i));
    }


    /**
     * Calcule le degre d'un sommet d'indice "indice" dans le 
     * graphe. Le degre constitue le nombre d'aretes liees a ce sommet.
     @param indice l'indice du sommet dont on cherche le degre
     */
    public int degre(int indice)
    {
	Sommet s = (Sommet)sommets.elementAt(indice);
	int deg = 0;
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = (Arete)aretes.elementAt(i);
	    if ( a.getVisible() ) {
		Sommet s1 = a.getE1();
		Sommet s2 = a.getE2();
		
		if ( s.equals( s1) )
		    deg++;	    
		if ( s.equals(s2) )
		    deg++; 
	    }
	}
	return deg;
    }
    public void initDegre() {
	int ns = this.nombreSommets();
	for (int i = 0; i < ns; i++) {
	    Sommet s = this.getSommet(i);
	    s.setDegre((int)degre(i)/2);
	}
    }

    /**
     * Calcule le demi-degre interieur d'un sommet. Ce qui constitue le nombre 
     * d'aretes entrant dans ce sommet. La methode lance une exception si le 
     * graphe n'est pas oriente.
     @param indice indice du sommet dont on cherche le demi-degre interieur
     @exception NotOrientedGraphException
    */
    public int demiDegreInterieur(int indice)
    {
	if (!oriente)
	    throw new NotOrientedGraphException("Cannot calculate half-degrees");
	
	Sommet s = (Sommet)sommets.elementAt(indice);
	int deg = 0;
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = (Arete)aretes.elementAt(i);
	    if (s.equals(a.getE2()))
		deg++;
	}
	return deg;	
    }
    
    /**
     * Calcule le demi-degre exterieur d'un sommet. Ce qui constitue le nombre 
     * d'aretes sortant dans ce sommet. La methode lance une exception si le 
     * graphe n'est pas oriente.
     @param indice indice du sommet dont on cherche le demi-degre exterieur
     @exception NotOrientedGraphException
    */
    public int demiDegreExterieur( Sommet s )
    {
	if ( !oriente )
	    throw new NotOrientedGraphException(" graphe non oriente !!");
	
	//Sommet s = (Sommet)sommets.elementAt(indice);
	int deg = 0;
	for ( int i = 0; i < aretes.size(); i++ ) {
	    Arete a = (Arete)aretes.elementAt(i);
	    if ( s.equals(a.getE1()) )
		deg++;
	}
	return deg;	
    }
    public Sommet pere(Sommet s)
    {
	Sommet pere = null;
	if ( !oriente )
	    throw new NotOrientedGraphException("graphe non oriente !!");
	for ( int i = 0; i < aretes.size(); i++ ) {
	    Arete a = (Arete)aretes.elementAt(i);
	    Sommet s1 = a.getE1();
	    Sommet s2 = a.getE2();
	    
	    if ( s.equals(s2) ) 
		pere = s1;
	    
	} return pere;
	
    }
    /**
     * Retire le sommet du graphe ainsi que toutes ses aretes incidentes.
     @param indice indice du sommet a supprimer
    */
    public void supprimerSommet(int indice) {
	Sommet s = (Sommet)sommets.elementAt(indice);
	int[] ind = new int[aretes.size()];
	int indi = 0;
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = (Arete)aretes.elementAt(i);
	    if ( s.equals(a.getE1()) || s.equals(a.getE2()) )
		ind[indi++] = i;
	}
	for (int i = indi-1; i >= 0; i--)
	    supprimerArete(ind[i]);
	
	sommets.removeElementAt(indice);
    }
    
    /**
     * Retire l'arete du graphe.
     @param indice indice de l'arete a retirer
    */
    public void supprimerArete(int indice) {
	Arete a = (Arete)aretes.elementAt(indice);
	int p = 0;
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a2 = (Arete)aretes.elementAt(i);
	    if (a == a2)
		continue;
	    if (a.paralleleA(a2,false) && a.getID() != a2.getID())
		p++;
	}
	if (p == 1)
	    complexite--;
	aretes.removeElementAt(indice);
    }
    
    
    /**
     * Teste si le graphe est simple ou non. Pour ce faire, la methode 
     * verifie s'il y a des aretes paralleles ou des boucles. S'il n'y en a aucune, 
     * le graphe est simple
     @return le resultat du test de simplicite du graphe
    */
    public boolean grapheSimple()
    {
	if (complexite > 0)
	    return false;
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = (Arete)aretes.elementAt(i);
	    if (a.getE1().equals(a.getE2()))
		return false; // Une boucle a ete trouvee.
	}
	return true;
    }
    
    /**
     * Convertit le graphe en chaine affichable
     @return chaine representant l'objet
    */
    public String toString()
    {
	String str = "Graphe: (";
	str += "Sommets: (";
	for (int i = 0; i < sommets.size(); i++) {
	    if (i > 0)
		str += ", ";
	    str += sommets.elementAt(i);
	}
	str += "), Aretes: (";
	for (int i = 0; i < aretes.size(); i++) {
	    if (i > 0)
		str += ", ";
	    str += aretes.elementAt(i);
	}
	str += "))";
	return str;
    }
    
    /**
     * Retourne le graphe sous-jacent d'un graphe oriente ou un graphe non oriente 
     * construit a partir d'un graphe non oriente. 
     @return objet Graphe resultant de la methode
    */
    public Graphe sousJacent()
    {
	Graphe gr = new Graphe(!oriente);
	gr.ajouterGraphe(this);
	return gr;
    }
    
    /**
     * Retourne l'indice d'un sommet. Si ce sommet ne fait pas partie du graphe, 
     * retourne -1. Les comparaisons sont effectuees avec le test 
     * d'egalite et non pas avec l'operateur ==.
     */
    public int indiceSommet(Sommet s)
    {
	if (s == null)
	    return -1;
	for (int i = 0; i < sommets.size(); i++) {
	    Sommet s2 = (Sommet)sommets.elementAt(i);
	    if ( s2.equals(s) )
		return i;
	}
	return -1;
    }
    
    /*
     * Retourne l'indice d'une arete du graphe. Si l'arete ne fait pas partie du graphe, 
     * la methode retourne -1.
     */
    public int indiceArete(Arete a)
    {
	if (a == null)
	    return -1;
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a2 = (Arete)aretes.elementAt(i);
	    if (a2.equals(a))
		return i;
	}
	return -1;
    }
    

    /**
     * Teste si une arete definie par deux sommets fait partie du graphe
     */
    public int areteDansGraphe(Sommet s1, Sommet s2 ) {
	for (int i = 0; i < nombreAretes(); i++) {
	    Arete arete = getArete(i);
	    Sommet s_or = arete.getE1();
	    Sommet s_ar = arete.getE2();
	    if ( ( s1.equals(s_or) && s2.equals(s_ar) ) || 
		 ( s2.equals(s_or) && s1.equals(s_ar) )  )
		return i;
	    
	}
	return -1;
    }
    public int areteDansGraphe(Arete a ) {
	for (int i = 0; i < nombreAretes(); i++) {
	    if ( a.equals(aretes) )		
		return i;	    
	}
	return -1;
    }
    /**
     * methode utilisee pour obtenir un objet Point qui contiendra 
     * des coordonnes aleatoires non occupees par un autre sommet du graphe.
     @param maxw longueur de la zone de selection
     @param maxh largeur de la zone de selection
     @return point choisi aleatoirement
     */
    public Point nouvellePosition(int maxw, int maxh)
    {

	if (maxw <= 0 || maxh <= 0) {
	    maxw = getSize().width;
	    maxh = getSize().height;
	    throw new IllegalArgumentException("la taille de la fen��tre doit ��tre positive.");
	    
	}
	Random generator = new Random();
	Point pt;

	do {
	    int a =Math.abs(generator.nextInt()) % maxw;
	    if (a < 100) a+=50;
	    if (a > (maxw-100)) a-=10;
	    int b =Math.abs(generator.nextInt()) % maxh;
	    if (b <100) b+=50;
	    if (b > (maxh-100)) b-=10;
	    pt = new Point(a,b);
	}
	while (hitTest(pt, true) != null);
	return pt;
    }
    
    /**
     * Effectue un test de tous les sommets afin de savoir quel sommet se trouve en un 
     * point donne, ou dans son voisinage.
     @param pt point a tester
     @param large teste au voisinage des coordonnees du sommet, permettant ainsi une gestion d'un 
     *            clic de souris
     @return sommet se trouvant au point teste, null s'il ne s'y trouve aucun sommet
     */
    public Sommet hitTest( Point pt, boolean large )
    {
	for ( int i = 0; i < sommets.size(); i++ ) {
	    Sommet s = getSommet(i);
	    if ( s.getVisible()) {
		if (large) {
		    double dist = Math.sqrt(Math.pow(pt.x - s.getX(), 2.0)+
					    Math.pow(pt.y - s.getY(), 2.0));
		    if ( dist <= 10 )
			return s;
		} else {
		    if ( s.getX() == pt.x && s.getY() == pt.y  )
			return s;
		}
	    }
	}
	return null;
    }
    
    /**
     * retourne l'etiquette d'une arete du graphe apres l'avoir convertie 
     * en une valeur numerique.
     @param indice indice de l'arete a traiter
     @return etiquette convertie en valeur numerique, -1 si la conversion n'est pas possible.
     */
    public double nGetArete(int indice)
    {
	try {
		//return Integer.parseInt(getArete(indice).getNom());
	    return getArete(indice).getVal();
	    }
	catch (NumberFormatException e) {
		return -1;
	    }
    }

    /**
     * retourne l'etiquette d'un sommet du graphe apres l'avoir convertie en valeur numerique.
     @param indice indice du sommet a traiter
     @return etiquette du sommet en valeur numerique, -1 si la conversion est impossible.
     */
    public int nGetSommet(int indice)
    {
	try {
		return Integer.parseInt(getSommet(indice).getNom());
	    }
	catch (NumberFormatException e) {
		return -1;
	    }
    }
    /**
     * Affiche le graphe sur un contexte d'affichage, sans toutefois verifier
     * si la surface d'affichage est suffisamment grande pour contenir tout le graphe.
     * Aucune verification n'est faite pour savoir si du texte des etiquettes se
     * chevauchent.
     @param g contexte d'affichage a utiliser
    */
    public void reordonner(Sommet s, int w, int h ) {
	int x = s.getX();
	int y = s.getY();

	if ( x <= 0 &&  y <= 0 ) {
	    s.x = 40; s.y = 10; }
	else
	    if ( x <= 0 &&  y > h-80 ) {
		s.x = 40; s.y = h-80; }
	    else 
		if ( x > w &&  y > h-80 ) { 
		    s.x = w-40;  s.y = h-80; }
		else 
		    if ( x > w &&  y < 0 ) {
		    s.x = w-40;  s.y = 10; }
		else if ( x > 0 && x < w  && y <= 0 ) { 
		    s.x = x ; s.y = 10; }
		else if ( x <= 0 &&  y > 0 && y < h ) { 
		    s.x = 40; s.y = y;  }
		else if  ( x > 0 &&  x < w && y > h ) { 
		    s.x = x; s.y = h-80; } 
		else if  ( x > w && y < h ) { 
		    s.x = w-20; s.y = y ; }
		else { 
		    s.x = x; s.y = y ; }

    }
    public void maximumMetrique(int instance) {
	maxMetrique1 = 0.0;
	maxMetrique2 = 0.0;
	for ( int i = 0; i < sommets.size(); i++) {
	    Sommet s = getSommet(i);
	    if ( maxMetrique1 <= s.Metrique[instance] && s.type == 1 && s.getVisible() ) // maximum sur les lignes
		maxMetrique1 = s.Metrique[instance];	    
	    if ( maxMetrique2 <= s.Metrique[instance] && s.type == 0 && s.getVisible() ) // maximum sur les colonnes
		maxMetrique2 = s.Metrique[instance]; 
	}
	}
    public void maximumMetrique() {
	double max1 = 0.0, max2 = 0.0;
	double cl = 0.0;

	for (int i = 0; i < sommets.size(); i++) {
	    Sommet s = getSommet(i);
	    if ( max1 <= s.metrique && s.type == 1 && s.getVisible() ) // maximum sur les lignes
		max1 = s.metrique;	    
	    if ( max2 <= s.metrique && s.type == 0 && s.getVisible() ) // maximum sur les colonnes
		max2 = s.metrique;
	}

	for ( int i = 0; i < sommets.size(); i++ ) {
	    Sommet s = getSommet(i);
	    if ( s.getVisible() ) {
		if ( typeMat == 0 ) {
		    if ( s.type == 1 ) {
			cl = (double)(s.metrique )/(double)(max1);
			cl = (double)(1 + Math.pow(cl, 1/2) - Math.pow((1 - cl), 2 ))/2;
			s.couleur = new Color( (int)(255*cl), 0, 0 );
		    }
		    else {
			cl = (double)(s.metrique )/(double)(max2);
			cl = (double)(1 + Math.pow(cl, 1/2) - Math.pow((1 - cl), 2 ))/2;
			s.couleur = new Color( 0, (int)(255*cl), 0 );
		    }
		} else 
		    if ( s.type == 1 ) {
			cl = (double)((double)(s.metrique)/(double)max1);
			cl = (double)(1 + Math.pow(cl, 1/2) - Math.pow((1 - cl), 2 ))/2;
			s.couleur = new Color( (int)(255*cl), 0, 0 );
		    }
	    }
	}
	maxMetrique1 = max1;
	maxMetrique2 = max2;

    }
    public void maximumArete() {

	double maxi = 0;
	double min = 999999;
  //   if (instance == 1){
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = getArete(i);
	    if ( maxi < a.getVal() && a.getVisible() )  maxi = a.getVal();
	    if ( min  > a.getVal() && a.getVisible() )  min  = a.getVal();
	}

	maxArete = maxi;
	minArete = min;
    }

    /* Calcule le degre d'un sommet dans un graphe donne */

    public int getDegre(Sommet s) {
	this.getVoisins(s, true, this.seuil );
	return(s.voisins.size());

    }

    public int maxVoisin(Graphe g){
    	int max=0;
    	for (int i = 0; i < g.nombreSommets(); i++){
    		Sommet s = g.getSommet(i);
    		if (!s.nom.contains("virtuel")&&!s.nom.contains("repere")){
    		if (aDesVoisins(s,true, 0 )) {
    			if (s.voisins.size()>max) max=s.voisins.size();
    		}}}
    	return max;
    }
    /* Attribue une couleur pour chaque arete en fonction de sa valeur dans l'intervalle
       des valeurs [min, max] et de l'intensit�� utilisee. Gere aussi la couleur des aretes dans le morphing
    */
    public synchronized void SetCouleursAretes(double intensite, int fond) {
  	double cl = 0.0;
	double metrique= 0.0;

	int v=0;
	int val1 =0; int val2=0;
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = getArete(i);
	    if ( maxArete < a.getVal())  maxArete = a.getVal();
	}

	for ( int i = 0; i < aretes.size(); i++ ) {
	    /* Pour chaque arete, on va prendre la valeur de l'arete (y compris celle additionnant les autres instances) et
	       on va la normaliser de facon �� trouver un juste ��quilibre dans l'affichage, au niveau intensit�� */
	    Arete a = (Arete)aretes.elementAt(i);
	    if ( a.getVisible()) {
               metrique = (double)(a.getVal()/maxArete);

	       /* on regle l'intensit�� d'affichage. C'est ici que se g��re la diff��rence d'intensit�� du slider intensite */
	       //int v = (int)(250 - 200*cl);
	       //int v = (int)(240 - 200*cl);

	if (fond == 0){
			cl = (double)( 1.0 + Math.pow( metrique, 1/intensite) - Math.pow(1 - metrique, intensite ) )/2.0;
			val1=230; val2=225; }
	else {
			cl = (double)( 1.0 + Math.pow(1 - metrique, intensite)- Math.pow(metrique, 1/intensite ) )/2.0;
			val1=240; val2=200;}


	       v = (int)(val1 - val2*cl);
	       a.setCouleur(new Color( v, v, v ));
	       Sommet S1 = a.getE1();
	       Sommet S2 = a.getE2();
	       if ((S1.nom.contains("virtuel") | S2.nom.contains("virtuel"))) a.setInvisible();
	       if (S1.nom.contains("virtuel")) S1.setInvisible();
	       if (S2.nom.contains("virtuel")) S2.setInvisible();

	    }
	}
    }

    	public void AfficheRepere(int repmorph){
    		for ( int i = 0; i < sommets.size(); i++ ) {
    			Sommet s = (Sommet)sommets.elementAt(i);
    		    if (repmorph==1){
    		    	if (s.nom.contains("virtuel")){
    		    		String inter=s.nom.substring(15,16);
    		    		s.setNom("Repere"+inter);
    		    		s.setVisible(); 
    		    		repaint();}
    		   	}
    		    	if (repmorph==0){
    		    	if (s.nom.contains("Repere")){
    		    		String inter=s.nom.substring(6,7);
    		    		s.setNom("Sommet_virtuel "+inter);
    		    		s.setInvisible();
    		    		repaint();}
    		   	}
    		}
    		    
    	}
    public void paintComponent(Graphics g, int param, int repmorph, int couleur_fond, double IntensitePolice,double taillePolice, int compteur) {
    	super.paintComponent(g);
    	AfficheRepere(repmorph);
	//SetCouleursAretes(intensite, Couleur_fond, affichage morphing);
	if (couleur_fond == 1) SetCouleursAretes(intensite, 1);
	if (couleur_fond == 0) SetCouleursAretes(intensite, 0);

	for ( int i = 0; i < aretes.size(); i++ ) {
	    Arete a = (Arete)aretes.elementAt(i);
	    a.afficher( g, false, oriente, a.getCouleur() );

	}

	for ( int i = 0; i < sommets.size(); i++ ) {
	    Sommet s = (Sommet)sommets.elementAt(i);
	    if (s.nom.contains("virtuel")) s.setInvisible();
	    if ( typeGraphe == 1 ) {
	  	if ( s.type == 1 )
		    s.afficher( g, sans, maxMetrique1, longs, cercle, clustering, rang, param, couleur_fond, repmorph,IntensitePolice, taillePolice, compteur, pre_instance);
		if ( s.type == 0 )
		    s.afficher(g, sans, maxMetrique2, longs, cercle, clustering, rang, param, couleur_fond,repmorph,IntensitePolice, taillePolice, compteur, pre_instance);
	    }
	    else { // typeGraphe == 2
		if ( s.type == 1 )
		    s.afficherInstances( g, sans, longs, maxMetrique1, instance, clustering, couleur_fond, repmorph,IntensitePolice, taillePolice);
		if ( s.type == 0 )
		    s.afficherInstances( g, sans, longs, maxMetrique2, instance, clustering, couleur_fond,repmorph,IntensitePolice, taillePolice);
	    }
	}
	if ( stress )
	    for ( int i = 0; i < aretes.size(); i++ ) {
		Arete a = (Arete)aretes.elementAt(i);
		a.afficherValeur( g );
	    }
    }
  
    public void getVoisins( Sommet autre, boolean flag, double seuil ) {
	int na =  aretes.size();
	int j = 0;
	autre.voisins = new Vector();

	for (int i = 0; i < na; i++ ) {
	    Arete a = (Arete)aretes.elementAt(i);

	    Sommet s1 = a.getE1();
	    Sommet s2 = a.getE2();
	    if ( a.getVal() >= seuil ) {
		if ( !flag ) {  // graphe non orient��
		    if ( autre.equals(s1) && !autre.voisins.contains(s2) && (!autre.nom.contains("virtuel"))&& (!s2.nom.contains("virtuel")) && (!autre.nom.contains("Repere"))&& (!s2.nom.contains("Repere"))) {
		    	autre.voisins.addElement(s2);
		    	j++;
		    }
		    else if ( autre.equals(s2) && !autre.voisins.contains(s1) && (!autre.nom.contains("virtuel")) && (!s1.nom.contains("virtuel"))&& (!autre.nom.contains("Repere"))&& (!s1.nom.contains("Repere")) ) {
		    	j++;
		    	autre.voisins.addElement(s1);
		    }
		    autre.voisins.setSize(j);
		} else  {      // graphe  orient��
		    if ( autre.equals(s1) && !autre.equals(s2) &&
			 !autre.voisins.contains(s2) && (!autre.nom.contains("virtuel"))&& (!s2.nom.contains("virtuel"))) {
			autre.voisins.addElement(s2);
			j++;
		    }
		    autre.voisins.setSize(j);
		}
	    }
	}
    } // fin getVoisins

    public void getTOUSVoisins( Sommet autre, boolean flag, double seuil ) {
    	int na =  aretes.size();
    	int j = 0;
    	autre.voisins = new Vector();

    	for (int i = 0; i < na; i++ ) {
    	    Arete a = (Arete)aretes.elementAt(i);

    	    Sommet s1 = a.getE1();
    	    Sommet s2 = a.getE2();
    	    if ( a.getVal() >= seuil ) {
    		if ( !flag ) {  // graphe non orient�
    		    if ( autre.equals(s1)  ) {
    			autre.voisins.addElement(s2);
    			j++;
    		    }
    		    else if ( autre.equals(s2) ) {
    			j++;
    			autre.voisins.addElement(s1);
    		    }
    		    autre.voisins.setSize(j);
    		} else  {      // graphe  orient�
    		    if ( autre.equals(s1) && !autre.equals(s2)) {
    			autre.voisins.addElement(s2);
    			j++;
    		    }
    		    autre.voisins.setSize(j);
    		}
    	    }
    	}
        } // fin getVoisins
        
    public boolean aDesVoisins(Sommet autre, boolean flag, double seuil ) {
	int na =  aretes.size();
	int j = 0;
	autre.voisins = new Vector();

	for (int i = 0; i < na; i++ ) {
	    Arete a = (Arete)aretes.elementAt(i);

	    Sommet s1 = a.getE1();
	    Sommet s2 = a.getE2();
	    if ( a.getVal() >= seuil ) {
		if ( !flag ) {  // graphe non orient��
		    if ( autre.equals(s1) && !autre.voisins.contains(s2) ) {
			autre.voisins.addElement(s2);
			j++;
		    }
		    else if ( autre.equals(s2) && !autre.voisins.contains(s1) ) {
			j++;
			autre.voisins.addElement(s1);
		    }
		    autre.voisins.setSize(j);
		} else  {      // graphe  orient��
		    if ( autre.equals(s1) && !autre.equals(s2) &&
			 !autre.voisins.contains(s2)) {
			autre.voisins.addElement(s2);
			j++;
		    }
		    autre.voisins.setSize(j);
		}
	    }
	}
	if (j!= 0) return true;
	else return false;
    } // fin getVoisins




    // Algorithme Prim pour determiner un MST : Maximal Spanning Tree
    public Graphe determineArbrePrim() {
	Graphe arbre = new Graphe(false);
	Vector sommets_depart = new Vector();
	Vector sommets_arrivee = new Vector();
	double maxi = 0.0;
	Arete grande_arete = null;
	
	// recherche de la plus grande arete
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = ((Arete)aretes.elementAt(i));
	    if ( a.getVal() > maxi ) {
		maxi = a.getVal();
		grande_arete = a;
	    }
	}
	for (int i = 0; i < sommets.size(); i++) {
	    Sommet s = getSommet(i);
	    sommets_arrivee.addElement(s);
	}

	Sommet s1 = grande_arete.getE1();
	Sommet s2 = grande_arete.getE2();

	sommets_depart.addElement(s1);
	sommets_depart.addElement(s2);

	sommets_arrivee.removeElement(s1);
	sommets_arrivee.removeElement(s2);

	grande_arete.marked = true;
	arbre.ajouterArete(grande_arete);
	// s1.fixe = true;
	// s2.fixe = true;

	while ( !sommets_arrivee.isEmpty() ) {
	    Arete areteMax = null;
	    int sens = 0;
	    double max = 0.0;
	    for ( int i = 0; i < nombreAretes() ; i++ ) {
		Arete arete = getArete(i);
		
		if ( arete.getVisible() && !arete.marked ) {
		    s1  = arete.getE1();
		    s2  = arete.getE2();
		    if ( sommets_depart.contains(s1)  && !sommets_arrivee.contains(s1) &&
			 sommets_arrivee.contains(s2) && !sommets_depart.contains(s2)  &&
			 s1.getVisible() && s2.getVisible() && arete.getVal() >= max ) {
			max = arete.getVal();
			areteMax = arete;
			sens = 1;
		    } else 
			if ( sommets_depart.contains(s2)  && !sommets_depart.contains(s1)  &&
			     sommets_arrivee.contains(s1) && !sommets_arrivee.contains(s2) &&
			     s1.getVisible() && s2.getVisible() && arete.getVal() >= max ) {
			    max = arete.getVal();
			    areteMax = arete;
			    sens = 2;
			}
		}
	    }
	    
	    if ( areteMax != null ) {
		if ( sens == 1 ) {
		    sommets_depart.addElement(areteMax.getE2());
		    sommets_arrivee.removeElement(areteMax.getE2());
		    
		} 
		if ( sens == 2 ) {
		    sommets_depart.addElement(areteMax.getE1());
		    sommets_arrivee.removeElement(areteMax.getE1());
		} 
		areteMax.marked = true;
		areteMax.setVisible();
		arbre.ajouterArete(areteMax);
		
	    } else {
		System.out.println(" Le Graphe n'est pas connexe" );
		return (this);
	    }
	}
	arbre.maximumArete();
	arbre.stress = this.stress;
	arbre.noms = this.noms;
	
	return (arbre);
    }


    
    // Algorithme Prim pour determiner un MST : Minimal Spanning Tree
    public Graphe determineArbrePrim2() {
	Graphe arbre = new Graphe(false);
	Vector sommets_depart = new Vector();
	Vector sommets_arrivee = new Vector();
	double mini = 999999.0;
	Arete petite_arete = null;
	// recherche de la plus petite arete
	for (int i = 0; i < aretes.size(); i++) {
	    Arete a = ((Arete)aretes.elementAt(i));
	    if ( a.getVal() < mini ){
		mini = a.getVal();
		petite_arete = a;
	    }
	}
	for (int i = 0; i < sommets.size(); i++) {
	    Sommet s = getSommet(i);
	    sommets_arrivee.addElement(s);
	}
	Sommet s1 = petite_arete.getE1();
	Sommet s2 = petite_arete.getE2();

	sommets_depart.addElement(s1);
	sommets_depart.addElement(s2);

	sommets_arrivee.removeElement(s1);
	sommets_arrivee.removeElement(s2);

	petite_arete.marked = true;
	arbre.ajouterArete(petite_arete);
	// s1.fixe = true;
	// s2.fixe = true;

	while ( !sommets_arrivee.isEmpty() ) {
	   
	    Arete areteMin = null;
	    int sens = 0;
	    double min = 999999.0;
	    for ( int i= 0; i < nombreAretes() ; i++ ) {
		Arete arete = getArete(i);
		s1  = arete.getE1();
		s2  = arete.getE2();
		if ( sommets_depart.contains(s1)  && !sommets_arrivee.contains(s1) &&
		     sommets_arrivee.contains(s2) && !sommets_depart.contains(s2)  &&
		     s1.getVisible() && s2.getVisible() && arete.getVal() < min ) {
		    min = arete.getVal();
		    areteMin = arete;
		    sens = 1;
		} else 
		    if ( sommets_depart.contains(s2)  && !sommets_depart.contains(s1)  &&
			 sommets_arrivee.contains(s1) && !sommets_arrivee.contains(s2) &&
			 s1.getVisible() && s2.getVisible() && arete.getVal() < min ) {
			min = arete.getVal();
			areteMin = arete;
			sens = 2;
		    }
	    }
	    if ( areteMin != null ) {
		if ( sens == 1 ) {
		    sommets_depart.addElement(areteMin.getE2());
		    sommets_arrivee.removeElement(areteMin.getE2());
		}
		if ( sens == 2 ) {
		    sommets_depart.addElement(areteMin.getE1());
		    sommets_arrivee.removeElement(areteMin.getE1());
		}
		
		areteMin.setVisible();
		arbre.ajouterArete(areteMin);
	    } else {
		System.out.println( " Le Graphe n'est pas connexe" );  
		return (this);
	    }
	}
	arbre.maximumArete();
	arbre.stress = this.stress;
	arbre.noms = this.noms;
	
	return (arbre);
    }
    
    static void echangerElements( double[] t, int m, int n ) {
	double temp = t[m];
	
	t[m] = t[n];
	t[n] = temp;
    }
    
    static int partition( double[] t, int m, int n ) {
	double v = t[m];                 // valeur pivot
	int i = m-1;
	int j = n+1;                  // indice final du pivot
	
	while ( true ) {
	    do {
		j--;
	    } while ( t[j] > v );
	    do {
		i++;
	    } while ( t[i] < v);
	    if ( i < j ) {
		echangerElements(t, i, j);
	    } else {
		return j;
	    }
	}
    }
    
    static void triRapide( double[] t, int m, int n ) {
	if ( m < n) {
	    int p = partition(t, m, n); 
	    triRapide(t, m, p);
	    triRapide(t, p+1, n);
	}
    }
    
    // Algorithme Kruskal pour determiner un MST : Maximal Spanning Tree
    public Graphe Kruskal() {
	Graphe arbre = new Graphe(false);

	return arbre;
    }
    
    public void visite() {
	for ( int i = 0; i < nombreSommets(); i++) {
	    Sommet s = getSommet(i);
	    if (s.marked())
		System.out.println("sommet " + i + " d�j� vu");
	    else {
		System.out.println("sommet " + i + " atteint");
		s.mark();
	    }
	}
    }
    
    public void AugmenteAttirance(Sommet srep){
 	    for ( int i= 0; i < nombreAretes() ; i++ ) {
 	    	Arete arete = getArete(i);
			Sommet u  = arete.getE1();
			Sommet v  = arete.getE2();
		    if ((v.nom==srep.nom)| (u.nom==srep.nom)){
		    	v.metrique*=2;
		    	u.metrique*=2;
		    	
		    	for (int j=0 ; j<instance ; j++){
		    			double val=arete.getVal();
		    			double val2=arete.getValInstance(j);
		    			arete.setValInstance(val2*2, j);
		    			arete.setVal(val*2);
		    			repaint();
		    	}
			}
	    }
    }

    
    /* retire du graphe  tous les sommets de degre 0. Ceci reduit la taille du graphe
       par consequent accelere les differents algorithmes utilises */
    public void nettoyage() {
	for (int i = 0; i < sommets.size(); i++) {
	    Sommet v = this.getSommet(i);
	    getVoisins(v, false, this.seuil );
	    
	    if ( this.degre(this.indiceSommet(v)) == 0 && v.getVisible()) {
		v.setInvisible();
		System.out.println(this.indiceSommet(v) + " visible = " + v.getVisible() + 
				   "  nom : " + v.nom + 
				   "  degre =  " +  this.degre(this.indiceSommet(v)));
	    }
	    if ( this.degre(this.indiceSommet(v)) == 0 && !v.getVisible() ) {
		v.setVisible();
		System.out.println(this.indiceSommet(v) + " visible = " + v.getVisible() + 
				   "  nom : " + v.nom + 
				   "  degre =  " +  this.degre(this.indiceSommet(v)));
	    }
	}
    } 

    static String tronque(String chaine, int nbDecimales)
    {
	String avant;
	String apres;
	
	StringTokenizer st = new StringTokenizer(chaine,","); 
	avant = st.nextToken(); 
	if (st.hasMoreTokens()) apres = st.nextToken(); 
	else return avant;
	
	if (nbDecimales <= 0) 
	    { 
		if (avant.equals("-0")) avant = "0"; 
		return avant; 
	    } 
	else if (apres.length() <= nbDecimales) return chaine;
	return chaine.substring( 0, chaine.length() - apres.length() + nbDecimales);
    }
  
    /**
       Trier l'ensemble des aretes en fonction de leurs poids
    */
    public void TrierAretes() {
	Vector aretesTriees = new Vector();
	aretesTriees = triInsertion(aretes);
	aretes = (Vector)aretesTriees.clone();
	
    }

    public static Vector triInsertion(Vector v) {
	Vector res = new Vector();  // le vecteur r��sultat
	int i;    // indice pour le parcours de v
	int p;    // indice pour la position d'insertion dans res
	for (i=0; i < v.size(); i++) { 
	    // on range dans elti le i��me ��l��ment de v
	    Arete a1 = (Arete)v.elementAt(i);
	    Double elti = new Double(a1.getVal());
	    // recherche de la position p de elti dans le vecteur resultat
	    for (p = 0; p < res.size(); p++) {
		Arete a2 = (Arete)res.elementAt(p);
		Double el  = new Double(a2.getVal());
		if ( elti.doubleValue() < el.doubleValue() )
		    break;
	    }
		// insertion de elti �� la position p dans le vecteur resultat
	    res.insertElementAt(a1, p);
	}
	return res;
    }
    
    public void SauvegarderPosition() {
	for ( int i = 0; i < nombreSommets(); i++) {
	    Sommet s = getSommet(i);
	    s.x0 = s.x;
	    s.y0 = s.y;
	}
    }
    
}




  

