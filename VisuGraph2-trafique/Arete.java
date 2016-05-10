import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
/**
 * Represente une arete dans un graphe non oriente et un arc dans 
 * un graphe oriente. On utilise deux sommets pour representer les 
 * extremites de l'arete. Toutefois, un troisieme point est necessaire 
 * dans certains cas, afin de pouvoir representer les aretes paralleles 
 * d'un graphe non simple. Si les deux sommets sont egaux, l'affichage 
 * va generer une boucle.
 */
public class Arete
{
    public static final double PI = 4.0*Math.atan(1.0);
    private Sommet e1; // Les extremites de l'arete
    private Sommet e2;
    private int x;
    private int y;
    private int z;
    private Color couleur;
    private String nom;
    private int id;
    private double valeur = 0;
 
    public double[] Instances = new double[10];
    private boolean visible = true;
    public Color couleurEtiquette = Color.red;
    protected double longueur;
    public boolean marked = false;

    Arete(Sommet e1, Sommet e2 ) {
	if (e1 == null || e2 == null) throw new NullPointerException();
	this.e1 = e1;
	this.e2 = e2;
	
    }
    Arete(Sommet e1, Sommet e2, int x, int y, Color couleur, String nom, int id) {
	if (e1 == null || e2 == null) throw new NullPointerException();
	this.e1 = e1;
	this.e2 = e2;
	this.x = x;
	this.y = y;

	this.couleur = (couleur == null)? Color.black : couleur;
	this.nom = nom;
	this.id = id;
	this.Instances[0] = 0;
    }

    Arete( Sommet e1, Sommet e2, int x, int y, Color couleur, double valeur, int id ) {
	if ( e1 == null || e2 == null ) throw new NullPointerException();
	this.e1 = e1;
	this.e2 = e2;
	this.x = x;
	this.y = y;
	this.couleur = (couleur == null)? Color.black : couleur;
	this.valeur = valeur;
	this.id = id;
    }

    public Sommet getE1() { return e1; }
    public Sommet getE2() { return e2; }
    public void setE1(Sommet ne1) { e1 = ne1; }
    public void setE2(Sommet ne2) { e2 = ne2; }
    public int getX() { return x; }
    public int getY() { return y; }
    public int getZ() { return z; }
    public void setXYZ(int nx, int ny, int nz) { x = nx; y = ny; z=nz;}
   
    public String getNom() { return this.nom; }
    public void setNom(String nom) {  this.nom = nom; }
    public int getID() { return id; }

    public double getVal() { return valeur; }
    public double getValInstance(int i) { 
	return Instances[i];
    }
    public void addVal(double valeur) { this.valeur += valeur; }
    public void setVal(double valeur) { this.valeur  = valeur; }
    public void setValInstance( double val , int i ) {  
	if ( i < Instances.length )  // Instances.insertElementAt(new Double(val), i);      
	    Instances[i] = val;
    }

    public void setInvisible() { this.visible = false; }
    public void setVisible()   { this.visible = true; }
    public boolean getVisible() {  return this.visible; }

    public void setCouleur(Color cl) { this.couleur = cl;}
    public Color getCouleur() { return this.couleur; }

    public final double longueurCourante() {
	double dx =  e1.getX() - e2.getX();
	double dy =  e1.getY() - e2.getY();

	
	return Math.sqrt( dx*dx + dy*dy );

    }
    public final void updateLongueur() {
	longueur = longueurCourante();
    }
    public final double length() {
	return longueur;
    }
    
    /**
       Convertit un objet Arete en chaine affichable.
       @return chaine representant l'objet
    */
    public String toString()
    {
	return "Arete: (Extremite initiale: " + e1 + ", Extremite finale: " + e2 + 
	    ", point de reference=(" + x + "," + y + "), Couleur=" + couleur + 
	    ", nom=" + nom + ")";
    }

    /**
       Affiche l'arete sur la zone d'affichage que represente le contexte 
       graphique passe en parametre. On utilise des parametres additionnels pour 
       cette methode afin de pouvoir stocker ces variables une seule fois pour 
       toutes les aretes, dans l'objet Graphe, et pouvoir les changer facilement.
       @param g contexte graphique a utiliser
       @param grapheComplexe indique a la methode si elle dessine une arete dans un 
       graphe complexe. Si le graphe est complexe, la methode tiendra compte du 
       point de reference (x,y) afin de tracer un arc par lequel passe les 
       deux extremites ainsi que le point de reference. Ce point est calcule par l'objet 
       Graphe de facon a ce que les aretes ne se touchent pas. Dans le cas d'un graphe 
       oriente simple, il faudra activer grapheComplexe si on a une arete 
       reliant e1 a e2 et une autre reliant e2 a e1.
       @param grapheOriente indique si l'ordre des sommets importe. Dans ce cas, 
       il faudra dessiner une pointe de fleche vers le sommet final.
       @param couleur indique la couleur (qui dependera du facteur intensite)
     */
    public synchronized void afficher( Graphics g, 
				       boolean grapheComplexe,
				       boolean grapheOriente, 
				       Color couleur) {
	Graphics2D g2 = (Graphics2D)g;
	
	Color old = g.getColor();
	/* On détermine par BasicStroke, la taille des aretes (au niveau épaisseur des traits */
	g2.setStroke(new BasicStroke(0.0F));
	if (this.visible) {
	   
	    int textPtX = x;
	    int textPtY = y;
	
	    if ( e1.equals(e2) ) {
		g.drawOval( e1.getX()-3*Sommet.RAYON,
			    e1.getY()-6*Sommet.RAYON,
			    6*Sommet.RAYON-1, 
			    6*Sommet.RAYON-1 );
		textPtX = e1.getX();
		textPtY = e1.getY() - 6*Sommet.RAYON;
		if (grapheOriente)
		    afficherFleche( g, new Droite( 1.0, 0.0, textPtX, textPtY, false ) );
	    }
	    else   {
		g.setColor(this.couleur);
		if ((!e1.nom.contains("virtuel")) && (!e2.nom.contains("virtuel"))) {
			int[] lesX = new int[3];
			int[] lesY = new int[3];
			lesX[0]=e1.getX();
			lesY[0]=e1.getY();
			lesX[1]=(e1.getX()+e2.getX())/2;
			lesY[1]=((e1.getY()+e2.getY())/2)+5;
			lesX[2]=e2.getX();
			lesY[2]=e2.getY();
			//g.drawPolyline(lesX,lesY,3);
			g.drawLine(lesX[0],lesY[0], lesX[2], lesY[2]);
		}
			textPtX = (e1.getX() + e2.getX())/2; 
		textPtY = (e1.getY() + e2.getY())/2; 
		int mx = (e1.getX() + e2.getX())/2; 
		int my = (e1.getY() + e2.getY())/2; 
		if (grapheOriente) 
		    afficherFleche(g, new Droite((double)e2.getX()-e1.getX(), 
						 (double)e2.getY()-e1.getY(), 
						 mx, my, true)); 
	    } 
	    g2.setStroke(new BasicStroke(0.0F));
	    g.setColor(old);
	}
    }
    
    /**
     * Affiche la valeur d'une arete.
     @param g le contexte d'affichage utilise
     @param valeur la valeur a afficher
    */
    public synchronized void afficherValeur(Graphics g )
    {
	Graphics2D g2 = (Graphics2D)g;

	Color old = g.getColor();
	if ( this.visible ) {
	    g2.setStroke(new BasicStroke(1.3F));
	    /* coordonnées d'où nous allons afficher les valeurs des liens */
	    int textPtX = (e1.getX() + e2.getX())/2;
	    int textPtY = (e1.getY() + e2.getY())/2;

	    /* nom indique la valeur du lien */
	    if ( nom != null ) {
		g.setColor(couleurEtiquette);
		g.drawString(nom, textPtX, textPtY);
	    }
	    g2.setStroke(new BasicStroke(0.0F));
	    g.setColor(old);
	}
    }

    public void afficherFleche(Graphics g, Droite d)
    {
	Point pt = d.resoudre(-Sommet.RAYON);
	Droite perp = new Droite(-d.getDY(), d.getDX(), (int)pt.getX(), (int)pt.getY(), true);
	Point pt0 = perp.resoudre(-Sommet.RAYON);
	Point pt1 = perp.resoudre(Sommet.RAYON);
	pt = new Point((int)d.getX0(),(int)d.getY0());
	g.drawLine(pt0.x,pt0.y,pt.x,pt.y);
	g.drawLine(pt.x,pt.y,pt1.x,pt1.y);
    }



    /**
     * Verifie si deux vecteurs sont paralleles. Les vecteurs sont specitifies 
     * comme des objets Point.
     @param v1 premier vecteur
     @param v2 second vecteur
     @return resultat du test de parallelisme
     */
    private boolean vecteursParalleles(Point v1, Point v2 )
    {
	// Pour effectuer un test de parallelisme fiable dans tous les cas,
	// il suffit de normaliser les deux vecteurs. Cela evitera des divisions par zero,
	// a moins que l'un des deux vecteurs soit de longueur nulle, ce qui n'est pas normal.
	double v1length = Math.sqrt(v1.getX()*v1.getX() + v1.getY()*v1.getY());
	double v2length = Math.sqrt(v2.getX()*v2.getX() + v2.getY()*v2.getY());
	double v1x = v1.getX()/v1length;
	double v1y = v1.getY()/v1length;
	double v2x = v2.getX()/v2length;
	double v2y = v2.getY()/v2length;
	return v1x == v2x && v1y == v2y;
    }

    /**
     * Affiche une arete sous la forme d'un arc passant par trois points. 
     * La methode calcule tout d'abord un cercle passant par les points 
     * e1, (x,y) et e2, puis les angles de coupe dans le but de tracer un 
     * arc partant du point e1 ou e2 vers le point e2 ou e1, selon la position 
     * de ces points.
     @param g contexte d'affichage utilise
     @param grapheOriente indique si le graphe a tracer est oriente ou non
     */
    private void afficherArc(Graphics g, boolean grapheOriente)
    {
	Droite med1 = mediatrice(new Point(e1.getX(),e1.getY()), new Point(x,y));
	Droite med2 = mediatrice(new Point(x,y), new Point(e2.getX(),e2.getY()));
	Point centre = intersection(med1, med2);
	double rayon = (int)Math.sqrt(Math.pow(centre.getX()-e1.getX(),2.0)+
				   Math.pow(centre.getY()-e1.getY(),2.0));
	int irayon = (int)rayon;
	int i2rayon = (int)(2*rayon);
	double theta1 = Math.atan2(centre.getY()-e1.getY(),e1.getX()-centre.getX());
	double theta2 = Math.atan2(centre.getY()-e2.getY(),e2.getX()-centre.getX());
	double thetamin = theta1 < theta2 ? theta1 : theta2;
	double deltatheta = Math.abs(theta2-theta1);
	g.drawArc((int)centre.getX()-irayon,(int)centre.getY()-irayon,
		  i2rayon-1,i2rayon-1,
		  (int)(thetamin*180./Math.PI),
		  (int)(deltatheta*180./Math.PI));
	// On obtient la pente de la tangente au cercle en utilisant la derivation 
	// implicite de l'equation du cercle.
	if (grapheOriente)
	    {
		double dy = -(x-centre.getX());
		double dx = y-centre.getY();
		afficherFleche(g, new Droite(dx, dy, x, y, true));
	    }
    }

    /**
     * Calcule la mediatrice d'une droite passant par deux points, 
     * retournant l'equation parametrique de cette mediatrice.
     @param p1 premier point
     @param p2 second point
     @return objet Droite contenant les equations parametriques de la mediatrice
     */
    private Droite mediatrice(Point p1, Point p2)
    {
	// On ne calcule pas la pente directement, on calcule plutot le vecteur directeur 
	// (dx,dy). La pente serait dy/dx, mais si dx=0, cela ne fonctionne pas.
	double dx = p1.getX()-p2.getX();
	double dy = p1.getY()-p2.getY();

	// Construction du vecteur perpendiculaire (dy,-dx)
	double temp = dx;
	dx = -dy;
	dy = temp;

	// Calcul du point milieu, qui deviendra le point initial de la droite
	double mx = (p1.getX()+p2.getX())/2;
	double my = (p1.getY()+p2.getY())/2;

	return new Droite(dx,dy,(int)mx,(int)my,false);
    }

    /**
     * Calcul le point d'intersection entre deux droites. Les droites ne 
     * doivent pas etre paralleles.
     @param d1 premiere droite
     @param d2 seconde droite
     @return objet Point contenant le point d'intersection, null si les droites sont paralleles
     */
    private Point intersection(Droite d1, Droite d2)
    {
	if (vecteursParalleles(new Point((int)d1.getDX(),(int)d1.getDY()),
			       new Point((int)d2.getDX(),(int)d2.getDY())))
	    return null;

	double t;
	if (d1.getDX() != d2.getDX())
	    t = ((double)(d2.getX0()-d1.getX0()))/(d1.getDX()-d2.getDX());
	else
	    t = ((double)(d2.getY0()-d1.getY0()))/(d1.getDY()-d2.getDY());

	return d1.resoudre(t);
    }

    /**
       Deux aretes sont egales si et seulement si leurs deux extermites 
       sont egales et qu'elles ont le meme identificateur.
     */
    public boolean equals(Arete autre)
    {
	return ((e1.equals(autre.getE1()) && e2.equals(autre.getE2())) ||
		(e1.equals(autre.getE2()) && e2.equals(autre.getE1()))) &&
	    id == autre.getID();
    }

    /**
       Test d'egalite pour les graphes orientes.
     */
    public boolean equalsOriented(Arete autre)
    {
	return (e1.equals(autre.getE1()) && e2.equals(autre.getE2())) &&
	    id == autre.getID();
    }

    /**
     * indique si deux aretes sont paralleles. Cela correspond au test 
     * d'egalite sans les ID.
     @param autre autre arete a tester
     @param oriente indique si on effectue le test pour un graphe oriente
     */
    public boolean paralleleA(Arete autre, boolean oriente)
    {
	boolean eq = getE1().equals(autre.getE1()) && getE2().equals(autre.getE2());
	return ((!oriente && eq) ||
		(oriente && eq && 
		 getE1().equals(autre.getE2()) && getE2().equals(autre.getE1())));
    }


}

/**
 * Represente une droite parametrique utilisee dans le calcul de l'arc 
 * a afficher. On represente la droite par les deux fonctions suivantes.
 * 
 * x = dx*t+x0
 * y = dy*t+y0
 * ou t varie entre 0 et 1
 */
class Droite
{
    private double dx;
    private double dy;
    private double x0;
    private double y0;
    
    Droite(double dx, double dy, double x0, double y0, boolean unit)
    {
	double length = unit ? Math.sqrt(dx*dx+dy*dy) : 1.0;
	this.dx = dx/length;
	this.dy = dy/length;
	this.x0 = x0;
	this.y0 = y0;
    }
    
    public double getDX() { return dx; }
    public double getDY() { return dy; }
    public double getX0() { return x0; }
    public double getY0() { return y0; }
    
    /**
     * Calcule la position sur la droite. 
     @param t la valeur de la variable t
     @return un objet Point contenant la solution aux deux equations de la droite
     */
    public Point resoudre(double t)
    {
	return new Point((int)(dx*t+x0),(int)(dy*t+y0));
    }

    /**
     * Convertit l'objet Droite en une chaine affichable
     @return objet String contenant la chaine
     */
    public String toString()
    {
	return "Droite: (x=" + dx + "t+" + x0 + ",y=" + dy + "t+" + y0 + ")";
    }
    
}


