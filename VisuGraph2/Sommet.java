import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Vector;

/**
 * La classe Sommet represente un sommet dans un graphe. Elle comporte 
 * ce qu'il faut pour positionner et afficher le sommet sur le plan,
 * avec ou sans etiquette de sommet. Les sommets sont representes
 * dans le graphe par des cercles de rayon RAYON, une constante.
 */
public class Sommet {
    public int x; 
    public int y;
    public int z;
	int valR=255;
	int valV=0;
    public int x0; 
    public int y0;
    public int z0;

    public Color couleur;                        // couleur en fonction de la metrique
    public Color couleurClasse = Color.blue;     // couleur apres clustering
    public Color couleurTranst = Color.white;    // couleur dans la SR obtenue par transitivité
    public static  Color couleurTexte = Color.white;
    public Color [] CouleurTab = {Color.red, 
				  Color.blue, 
				  Color.magenta, 
				  Color.orange, 
				  Color.green,
				  Color.white};

    public Color [] CouleurTab2 = {Color.red.darker(), 
				   Color.blue.darker(), 
				   Color.magenta.darker(),
				   Color.orange.darker(), 
				   Color.green.darker(),
				   Color.gray};

    ControlWindow control = null;
    public String nom;     // nom court : peut etre null si pas d'etiquette
    public String nomlong; // nom long  : peut etre null si pas d'etiquette
    public int nbvoisins;
    public int seuil_voisin;
    public int num;
    public int modifx;
    public int modify;
    public static final int RAYON = 5;
    public static int larg = 20;
    public static int haut = 10;
    public int classe;
    public int type;

    private boolean visible = true;
    private int degre = 0;
    public boolean fixe = false;
    
    public Vector voisins;

    public int num_cluster = 0;
    public double metrique;
    public double Metrique[];
    public int niveau = 100;
	Font fnt1 = new Font("Time", Font.PLAIN, 9);
    // pour les effets d'attraction/repulsion
    public double dx, dy, dz;

    // pour figer la periode (le nombre d'instances)
    public int per;

    /* On reattribue des coordonnées à nos sommets dans le cas où on a activé les forces*/
    public final void forced( Sommet other, double factor, boolean colle){
    	//System.out.println("le sommet "+this.nom+" a pour coordonnees : ("+this.x+" ; "+this.y+"; "+this.z+") et ses nouvelles coordonnees sont : ("+dx+" ; "+dy+"; "+dz+")");
    		if (colle) { 
    			dx += ((x-0.00000000000001) - other.x) * factor; dy += ((y-0.000000000000001) - other.y) *factor; dz += ((z+0.000000000000001) - other.z) *factor;colle = false; }
    		else { dx += (x - other.x) *factor; dy += (y - other.y)*factor;dz += (z - other.z)*factor;}

    }
    public final void scaleDelta( double factor ) {
	dx = dx * factor;
	dy = dy * factor;
	dz = dz * factor;
    }
    public final void stabilize() {
	dx = dy = dz = 0.0;
    }
    public final double deltaForce() {
	return Math.sqrt( dx*dx + dy*dy );
    }
    public final void moveDelta( double factor, double w, double h ) {
  	x += dx * factor;
	y += dy * factor;
	z += dz * factor; 
	if (x>w-20) x=(int)w-20;
	if (y>h-100) y=(int)h-100;
	if (x<5) x=5;
	if (y<20) y=20;
    }
   
    protected boolean marked = false;
    protected final void mark() { marked = true;}
    protected final void unmark() {marked = false;}
    protected final boolean marked() {return marked;}
  
    /**
       Construit un objet Sommet. La couleur est noire et il n'y a pas 
       d'etiquette de sommet.
       @param x abcisse du sommet
       @param y ordonnee du sommet
     */
    Sommet(int x, int y, int z) {
	this.x = x;
	this.y = y;
	this.z = z;
	couleur = Color.red;
	nom = null;
	Metrique = new double[10];
    }

    /*
       Construit un objet Sommet. Il n'y a pas d'etiquette de sommet.
       @param x abcisse du sommet
       @param y ordonnee du sommet
       @param couleur couleur du sommet
     */
    Sommet(int x, int y, int z, Color couleur)
    {
	this(x,y,z);
	if (couleur == null)
	    this.couleur = Color.white;
	else
	    this.couleur = couleur;	
    }

    /**
       Construit un objet Sommet. La couleur est par defaut noire.
       @param x abcisse du sommet
       @param y ordonnee du sommet
       @param nom etiquette du sommet
     */
    Sommet(int x, int y,int z, String nom, int nbvoisins, int seuil_voisin, int num, int modifx, int modify) {
	this(x,y,z);
	this.nom = nom;
	this.nbvoisins = nbvoisins;
	this.seuil_voisin=seuil_voisin;
	this.num=num;
	this.modifx=modifx;
	this.modify=modify;
    }

    /**
       Construit un objet Sommet.
       @param x abcisse du sommet
       @param y ordonnee du sommet
       @param couleur couleur du sommet
       @param nom etiquette du sommet
     */
    Sommet(int x, int y, int z, Color couleur, String nom, int nbvoisins,int seuil_voisin, int num) {
	this(x, y, z, couleur);
	this.nom = nom;
	this.nbvoisins = nbvoisins;
	this.seuil_voisin=seuil_voisin;
	this.num=num;
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    
    public Color getCouleur() { return couleur; }
    public String getNom() { return nom; }
    public void setNom(String nom){this.nom=nom;}
    public void setCouleur(Color couleur) { this.couleur = couleur; }
    public void setCouleurTxt(Color couleur) { this.couleurTexte = couleur; }
    public void setInvisible() { this.visible = false; }
    public void setVisible() { this.visible = true; }
    public boolean getVisible() {  return this.visible;  }
   
    public void setDegre(int degre) { this.degre = degre; }
    public int getDegre() {  return this.degre;  }

    /**
       Convertit l'objet Sommet en chaine affichable.
       @return un objet String contenant une chaine decrivant l'objet Sommet
     */
    public String toString()
    {
	String str = "Sommet: (centre en (" + x + "," + y + "," + z + "), de couleur " + couleur;
	str += nom == null ? " ne portant pas d'etiquette)" : 
	    (" avec " + nom + " comme etiquette)");
	return str;
    }

    /**
       Affiche le sommet sur un contexte graphique Java, en respectant 
       les coordonnees du sommet, sa couleur et l'etiquette. 

       Cette methode ne connaissant pas la taille de la zone d'affichage, 
       aucune verification n'est effectuee pour savoir si toutes les informations 
       entrent sur la zone ou ecrasent une etiquette d'un autre sommet.
       @param g le contexte d'affichage Java a utiliser.
       Cette fonction permet de tracer les histogrammes. Pour ce faire, periode correspond à l'instance de morphing, max correspond au maxmétrique
     */
    public synchronized void afficherInstances(Graphics g,
					       boolean Etiquette,
					       boolean Longs,
					       double max,
					       int periode, boolean clustering, int couleur_fond, int repmorph, double IntensitePolice, double taillePolice) {
    per=periode;
	Color old = g.getColor();
	double quantite = 0.0;
	Graphics2D g2 = (Graphics2D)g;


	if ( this.getVisible() ) {
	//	System.out.println("on est dans afficher instance");
	    // instances sous forme de barres
	    for ( int i = 1; i <= periode ; i++ ) {
		quantite = 20*Metrique[i]/Metrique[periode+1] ;
		if ( type == 1 && !clustering )	{g2.setColor(new Color(255,0,0));}
		if ( type == 1 && !clustering )	{g2.setColor(new Color(255,0,0));}
		if (periode ==2){
			if (i==2) {g2.setColor(new Color(0,255,0));}
		}
		else{
			if (i==2) {g2.setColor(new Color(255,180,0));}
			if (periode==3){
				if (i==3) {g2.setColor(new Color(0,255,0));}}
			else if (i==3){g2.setColor(new Color(248,214,5));}
			if (periode==4){
				if( i==4){g2.setColor(new Color(0,255,0));}
			}
			else if (i==4){g2.setColor(new Color(243,255,0));}
			if (periode==5){
				if( i==5){g2.setColor(new Color(0,255,0));}
			}
			else if (i==5){g2.setColor(new Color(214,255,0));}
			if (periode==6){
				if( i==6){g2.setColor(new Color(0,255,0));}
			}
			else if (i==6){g2.setColor(new Color(200,222,80));}
			if (periode==7){
				if( i==7){g2.setColor(new Color(0,255,0));}
			}
			else if (i==7){g2.setColor(new Color(153,222,80));}
			if( i==8){g2.setColor(new Color(0,255,0));}
		}

		if ( quantite == 0.0  && !nom.contains("virtuel")) {
			g2.setColor( Color.gray );
			g.drawLine( x-5 + 5*(i-1), y, x-5 + 5*i , y);
		}
		else {
			if (Math.round(quantite) != 0 ){
				g.fill3DRect( x-5 + 5*(i-1), y - 2*(int)Math.round(quantite),5 , 2*(int)Math.round(quantite), true );}
			else g.fill3DRect( x-5 + 5*(i-1), y - 1,5 , 1, true );
		}
		if ( type == 1 &&  clustering ) {g.setColor(couleurClasse);}
		if ( type == 0 && !nom.contains("virtuel")){ g2.setColor( Color.green );}
		if ( (quantite == 0.0 ) && !nom.contains("virtuel")) {
			g2.setColor( Color.gray);
		    g.drawLine( x-5 + 5*(i-1), y, x-5 + 5*i , y );
		} else {
		    if (Math.round(quantite) != 0 && !nom.contains("virtuel"))
			g.fill3DRect( x-5 + 5*(i-1), y - 2*(int)Math.round(quantite), 5 , 2*(int)Math.round(quantite), true );
		    else
			g.fill3DRect( x-5 + 5*(i-1), y - 1, 5 , 1, true );
		}
	    }
	   	int tail= (int)(taillePolice);
    	Font ft = new Font("Time", Font.PLAIN, tail);
	    if (repmorph==1){
	    	g.setFont(ft);
	    	g.setColor(Color.red);
	    	String Nom = ( Longs && nomlong != null )?nomlong:nom;
	    	if (Nom.contains("Repere"))	g.drawString( Nom, x+5 , y +3*Sommet.RAYON );
	    }
	    if ( !Etiquette ) { // avec ou sans etiquette ?
		String Nom = ( Longs && nomlong != null )?nomlong:nom;
 		g.setFont(ft);
		if (!Nom.contains("Repere")) {
			if (couleur_fond==1) {g.setColor(new Color((int)IntensitePolice,(int)IntensitePolice,(int)IntensitePolice));}
			else {g.setColor(new Color(255-(int)IntensitePolice,255-(int)IntensitePolice,255-(int)IntensitePolice));}
			g.drawString( Nom, x+5 , y + 3*Sommet.RAYON );
		}
		
	    }
	   }
	g.setColor(old); // permet de ne pas alterer l'etat du contexte tel qu'il est recu
	//clustering=false;
    }

    public synchronized void afficher( Graphics g,
				       boolean Etiquette, 
				       double max,
				       boolean Longs, 
				       int forme, 
				       boolean clustering, int rang, int param, int couleur_fond, int repmorph, double IntensitePolice, double taillePolice, int compteur, int pre_instance) {
   	Color old = g.getColor();
	double quantite = 0.0, cl = 0.0;
 	// on récupère le nombre d'instances de notre morphing
	int periode=per;
	Graphics2D g2 = (Graphics2D)g;
	// si on a coché "afficher repere", on rend bien visible les reperes pour chaque instance visualisée indépendement.
	if (this.nom.contains("Repere")) this.setVisible(); 
	if ( this.getVisible() ) {
	    if ( max != 0.0 )
		cl = metrique/max;	   
	    if ( forme == 0 ) {  // sommet sous forme circulaire avec nuances de couleurs
	    	if (clustering) {
	       			g.setColor(new Color ((int)(Math.abs(couleurClasse.getRed())),(int)(Math.abs(couleurClasse.getGreen())),(int)(Math.abs(couleurClasse.getBlue()))));
	       			g.fillOval(x - Sommet.RAYON, y - Sommet.RAYON, 2*Sommet.RAYON, 2*Sommet.RAYON);
	    	}
	    	else {
	    		g.setColor(couleur);
	    		g.fillOval(x - Sommet.RAYON, y - Sommet.RAYON, 2*Sommet.RAYON, 2*Sommet.RAYON);}
	    }
	    else
		// sommet sous forme de cercle dont la taille depend de sa metrique
		if ( forme  == 1) { 
		    if ( type == 1 && !clustering )
		    {g.setColor(couleur);}
		    if ( type == 1 &&  clustering )
			g.setColor(couleurClasse); 
		    if ( type == 0 && !nom.contains("virtuel"))
			{g.setColor(Color.green);}
       		    
		    quantite = 10*Math.sqrt(cl);
		    if ( quantite < 1.0 ) // c.a.d cl < 0.1
			quantite = 1.0;

		    g.fillOval( (int)(x - quantite-1), (int)(y - quantite-1),
				2*Math.round((float)quantite) +1 , 2*Math.round((float)quantite) +1);   
		} else
		    // sommet sous forme de barre

		  if ( this.getVisible() ) {
	// instances sous forme de barres
 	                if (param==1){
 	                	
			   for ( int i = 1; i <= periode ; i++ ) {
				quantite = 20*Metrique[i]/Metrique[periode+1] ;
				if (periode<2) {quantite= 20*metrique/max;}
				if ( type == 1 && !clustering ){
					g2.setColor( Color.red );
					// la barre de l'histogramme correspondant à la deuxième instance sera bleue
					if (i==2)
					g2.setColor(Color.blue);
					// la barre de l'histogramme correspondant à la deuxième instance sera blanche
					if (i==3)
					g2.setColor(Color.white);
					// la barre de l'histogramme correspondant à la deuxième instance sera orange
					if(i==4)
					g2.setColor(Color.orange);
					if(i==5)
					g2.setColor(Color.magenta);
					if(i==6)
					g2.setColor(Color.cyan);
					if(i==7)
					g2.setColor(Color.yellow);
					if(i==8)
					g2.setColor(Color.green);
					if(i==9)
					g2.setColor(Color.orange);
					if ( type == 1 &&  clustering )
					g.setColor(couleurClasse);
					if ( type == 0 )
					g2.setColor( Color.green );
					}

			       if ( type == 1 &&  clustering )
					{g2.setColor(couleurClasse); }
				
					if ( quantite == 0.0  && !nom.contains("virtuel")) {
						g2.setColor( Color.gray );
						g.drawLine( x-5 + 5*(i-1), y, x-5 + 5*i , y );
					}
					else {
						if (Math.round(quantite) != 0 ){
							g.fill3DRect( x-5 + 5*(i-1), y - 2*(int)Math.round(quantite),
								5 , 2*(int)Math.round(quantite), true );}
						else
							g.fill3DRect( x-5 + 5*(i-1), y - 1,
									5 , 1, true );
					}
				}
			}
			if (param==2){
				int degre=255/(periode-1);
				for ( int i = 1; i <= periode ; i++ ) {
					quantite = 20*Metrique[i]/Metrique[periode+1] ;
		
					if (periode<2) {quantite= 20*metrique/max;}
					if ( type == 1 && !clustering ){
						int col=0;
						if (couleur_fond==0) {g2.setColor(new Color(240,240,240));	col=240;}			// la barre de l'histogramme correspondant à la première instance sera rouge					
						if (couleur_fond==1) {g2.setColor(new Color(50,50,50));col=50;}	
//System.out.println(compteur);
if (param==2 && compteur>9) compteur=9;
try{

						if ( type == 1 && i==rang && !clustering )	{g2.setColor(new Color(255/(5-(compteur/2)),0,0));}
						if (periode ==2){
							if (i==2 && i==rang) {
								if (compteur<5)g2.setColor(new Color(255/(compteur),0,0));
								else g2.setColor(new Color(0,0,255/(5-(compteur/2))));}
						}
						else{if (i==2 && i==rang) {
								/*if (compteur<5)g2.setColor(new Color(255/(compteur),0,0));
								else */{g2.setColor(new Color(255/(5-(compteur/2)),180/(5-(compteur/2)),0));}}
						}
						int n1=255; int n2=0; int n3=0;
						if (pre_instance == 0 && i==pre_instance){n1=255/(compteur); if(n1<col){n1=col; n2=col; n3=col;} g2.setColor(new Color(n1,n2,n3));}
						if (pre_instance == 1 && i==pre_instance){n1=255/(compteur);if(n1<col){n1=col; n2=col; n3=col;}g2.setColor(new Color(n1,n2,n3));}
						if (pre_instance == 2 && i==pre_instance){n1=255/(compteur); n2=180/(compteur);if(n1<col && n2<col && n3<col){n1=col; n2=col; n3=col;} g2.setColor(new Color(n1,n2,n3));}
						if (pre_instance == 3 && i==pre_instance){n1=248/(compteur);n2=214/(compteur);n3=5/(compteur);if(n1<col && n2<50 && n3<50){n1=col; n2=col; n3=col;} g2.setColor(new Color(n1,n2,n3));}
						if (pre_instance == 4 && i==pre_instance){n1=243/(compteur);n2=255/(compteur); if(n1<col && n2<col && n3<col){n1=col; n2=col; n3=col;}g2.setColor(new Color(n1,n2,n3));}
						if (pre_instance == 5 && i==pre_instance){n1=214/(compteur);n2=255/(compteur); if(n1<col && n2<col && n3<col){n1=col; n2=col; n3=col;}g2.setColor(new Color(n1,n2,n3));}
						if (pre_instance == 6 && i==pre_instance){n1=200/(compteur);n2=222/(compteur);n3=80/(compteur); if(n1<col && n2<col && n3<col){n1=col; n2=col; n3=col;}g2.setColor(new Color(n1,n2,n3));}
						if (pre_instance == 7 && i==pre_instance){n1=153/(compteur);n2=222/(compteur);n3=80/(compteur); if(n1<col && n2<col && n3<col){n1=col; n2=col; n3=col;}g2.setColor(new Color(n1,n2,n3));}	
							if (periode==3 && i==rang){
								if (i==3 && i==rang){
									/*if (compteur<5)g2.setColor(new Color(255/(compteur),180/(compteur),0));
									else*/ g2.setColor(new Color(0,255/(5-(compteur/2)),0));}}
							else if (i==3 && i==rang){
								/*if (compteur<5)g2.setColor(new Color(255/(compteur),180/(compteur),0));
								else */g2.setColor(new Color(248/(5-(compteur/2)),214/(5-(compteur/2)),5/(5-(compteur/2))));}
							
							if (periode==4 && i==rang){
								if (i==4 && i==rang){
									if (compteur<5)g2.setColor(new Color(248/(compteur),214/(compteur),5/(compteur)));
									else g2.setColor(new Color(0,255/(5-(compteur/2)),0));}
							}
							else if (i==4 && i==rang){
								/*if (compteur<5)g2.setColor(new Color(248/(compteur),214/(compteur),5/(compteur)));
								else */{g2.setColor(new Color(243/(5-(compteur/2)),255/(5-(compteur/2)),0));}}
							
							if (periode==5 && i==rang){
								if (i==5 && i==rang){
									if (compteur<5)g2.setColor(new Color(243/(compteur),255/(compteur),0));
									else {g2.setColor(new Color(0,255/(5-(compteur/2)),0));}}
							}
							else if (i==5 && i==rang) {
								/*if (compteur<5)g2.setColor(new Color(243/(compteur),255/(compteur),0));
								else */g2.setColor(new Color(214/(5-(compteur/2)),255/(5-(compteur/2)),0));}
							
							if (periode==6 && i==rang){
								if (i==6 && i==rang){
									if (compteur<5)g2.setColor(new Color(214/(compteur),255/(compteur),0));
									else g2.setColor(new Color(0,255/(5-(compteur/2)),0));}
							}
							else if(i==6 && i==rang) {
								/*if (compteur<5)g2.setColor(new Color(214/(compteur),255/(compteur),0));
								else */g2.setColor(new Color(200/(5-(compteur/2)),222/(5-(compteur/2)),80/(5-(compteur/2))));}
							
							if (periode==7 && i==rang){
								if (i==7 && i==rang){
									if (compteur<5)g2.setColor(new Color(200/(compteur),222/(compteur),80/(compteur)));
									else g2.setColor(new Color(0,255/(5-(compteur/2)),0));}
							}
							else if(i==7 && i==rang) {
								/*if (compteur<5)g2.setColor(new Color(200/(compteur),222/(compteur),80/(compteur)));
								else */g2.setColor(new Color(153/(5-(compteur/2)),222/(5-(compteur/2)),80/(5-(compteur/2))));}
							
							if(i==8 && i==rang) {
								/*if (compteur <5)g2.setColor(new Color(153/(compteur),222/(compteur),80/(compteur)));
								else*/ g2.setColor(new Color(0,255/(5-(compteur/2)),0));}
						}
					
				catch(Exception n){}}
					if ( type == 1 &&  clustering )
					g.setColor(couleurClasse);
					if ( type == 0 && !nom.contains("virtuel") )
					g2.setColor( Color.green );
					if ( quantite == 0.0  && !nom.contains("virtuel") ) {
						g2.setColor( Color.gray );
						g.drawLine( x-5 + 5*(i-1), y, x-5 + 5*i , y );
					} else {
					if (Math.round(quantite) != 0 ){
					g.fill3DRect( x-5 + 5*(i-1), y - 2*(int)Math.round(quantite),
						5 , 2*(int)Math.round(quantite), true );}
					else{
						g.fill3DRect( x-5 + 5*(i-1), y - 1,
						5 , 1, true );}
					}
				}
			}
			if (param==3){
				double ancienne_quantite=0.0;
				double ancien_rang=0.0;
				/*System.out.println("max : "+max);
				System.out.println("type : "+type);*/
				if (type==0){quantite = 10*metrique/max;}
				else{quantite =10*metrique/max;}
				if (periode<2) {quantite= 20*metrique/max;}
				//*************/** souci niveau compteur (au detail des 10 étapes, barres trop grandes) ************/
				//System.out.println(compteur);
				//if (ancienne_quantite>quantite && ancien_rang!=rang) quantite=ancienne_quantite;2
				//quantite = ((quantite)*(compteur)/20);222
				if ( !nom.contains("virtuel")){
				g2.setColor( Color.green );}
				if ( type == 1 &&  clustering )
				g.setColor(couleurClasse);
				if ( type == 0 && !nom.contains("virtuel") )
				g2.setColor( Color.green );
				{
					g2.setColor( Color.gray );
					if (!nom.contains("virtuel") ){
					g.drawLine( x , y, x+5 , y );
					g2.setColor( Color.green );}
/******/					g.fill3DRect( x-5 + 5, y - 2*(int)Math.round(quantite),
						5 , 2*(int)Math.round(quantite), true );
ancienne_quantite=quantite;
ancien_rang=rang;
				}
			}


			if ( forme  == 3 ) {
			g.setColor(couleurClasse);
			g.fillOval( x - Sommet.RAYON, y - Sommet.RAYON,
					2*Sommet.RAYON, 2*Sommet.RAYON );
			}

	}
	    
	    if ( !Etiquette ) { // avec ou sans etiquette ?
	    	int tail= (int)(taillePolice);
	    	Font ft = new Font("Time", Font.PLAIN, tail);
			g.setFont(ft);
			String Nom = ( Longs && nomlong != null )?nomlong:nom;
			if (!Nom.contains("Repere")) {
				if (couleur_fond==1) {g.setColor(new Color((int)IntensitePolice,(int)IntensitePolice,(int)IntensitePolice));}
				else {g.setColor(new Color(255-(int)IntensitePolice,255-(int)IntensitePolice,255-(int)IntensitePolice));}
				g.drawString( Nom, x+5 , y + 3*Sommet.RAYON );
			}
		    }
	    if (repmorph==1) {
	    	String Nom2 = ( Longs && nomlong != null )?nomlong:nom;
	    	if (Nom2.contains("Repere")) {
	    	   	int tail= (int)(taillePolice);
	        	Font ft = new Font("Time", Font.PLAIN, tail);
	    		g.setFont(ft);
	    		g.setColor(Color.red);
	    		g.drawString( Nom2, x+5 , y + 3*Sommet.RAYON );}
	    	}
	    
	g.setColor(old); // permet de ne pas alterer l'etat du contexte tel qu'il est recu
      }
    }


    /**
       Deplace le sommet en changeant ses coordonnees.
       @param newX nouvelle abcisse
       @param newY nouvelle ordonnee
       @param w largeur de la fenetre
       @param h hauteur de la fenetre
     */
    public synchronized void deplacer(int newX, int newY, int w, int h)
    {
     	if ( newX < 0 &&  newY < 0 ) {
	    x = 10; y = 10; }
	
	else
	    if ( newX < 0 &&  newY > h-6 ) {
	    x = 10; y = h-60; }
	
	    else
		if ( newX > w &&  newY > h-6 ) {
		x = w-10;  y = h-6; }

		else if ( newX > w &&  newY < 0 ) {
		x = w-10;  y = 10; }
	
		else if ( newX > 0 && newX < w  && newY < 0 ) { 
		x = newX ; y = 10; }
	
		else if ( newX <= 0 &&  newY > 0 && newY < h ) { 
		x = 10; y = newY;  }
	
		else if  ( newX > 0 &&  newX < w && newY > h-6 ) { 
		x = newX; y = h-6; }
	
		else if  ( newX > w && newY < h ) { 
		x = w-10; y = newY ; }
		else { 
		    x = newX; y = newY ; }
    }
    /**
       Change la couleur du sommet.
       @param newColor nouvelle couleur
     */
    public synchronized void colorer(Color newColor)
    {
	couleur = newColor;
    }

    /**
       Change l'etiquette du sommet.
       @param newName nouvelle etiquette
     */
    public synchronized void renommer(String newName)
    {
	nom = newName;
    }

    /**
       Deux sommets sont egaux si et seulement si leurs coordonnes sont 
       identiques.
     */
    public boolean equals(Sommet autre)
    {
	return  x == autre.x && y == autre.y && nom == autre.nom;
    }
}


