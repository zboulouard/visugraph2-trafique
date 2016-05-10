/**
 * Permet la manipulation simple de matrices.
 */

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.StringTokenizer;
import java.util.Vector;

public class Matrice
{
    private double[][] m;    // Tableau a deux dimensions rectangulaire
    public Vector labels;   // liste des labels des lignes
   
    /**
     * Construit la matrice a partir d'un tableau a deux dimensions. Le 
     * tableau doit etre rectangulaire, c'est-a-dire que chacune de ses lignes 
     * doit comporter un meme nombre d'elements. Pour i et j de 0 
     * a m.length-1, m[i].length==m[j].length.
     @param m le tableau a utiliser
     */
    
    Matrice(double[][] m)
    {
	testRectangulaire(m);
	this.m = m;
    }

    /**
     * Construit une matrice a partir de dimensions. Les dimensions doivent 
     * etre positives et non nulles.
     @param M nombre de lignes de la matrice 
     @param N nombre de colonnes de la matrice 
     */

    Matrice(int M, int N)
    {
	if (M <= 0 || N <= 0)
	    throw new BadMatrixDimensionsException("Dimensions must be non-null.");
	m = new double[M][N];
	for (int i = 0; i < m.length; i++)
	    for (int j = 0; j < m[i].length; j++)
		m[i][j] = 0.0;
    }
    /**
     * Construit une matrice a partir d'un fichier texte. Ce fichier est une representation
     * de la matrice d'adjacence d'un graphe.
     */
    Matrice( String nom_fichier) { 
	int M = 0;
	labels = new Vector();
	double[][] mat= new double[M][M];

	try {
	    File file = new File (nom_fichier);
	    Reader rd_result = new FileReader (file);
	   
	    String ligne_doc = null;
	    LineNumberReader line = new LineNumberReader(rd_result);
	    
	    while ( (ligne_doc = line.readLine()) != null )
		M++;
	    M--;

	    mat = new double[M][M];
	    // lecture de fichier ligne par ligne
	    ligne_doc = line.readLine();
	    
	    int l = 0;
	    while ( ligne_doc != null && l < mat.length ) {
		l++;
		int j = 0;
		int k = line.getLineNumber() - 1;
		StringTokenizer lab = new StringTokenizer(ligne_doc);
		String s = lab.nextToken();
		labels.addElement(s);

		while( lab.hasMoreTokens() && j < mat[k].length  ) {

		    // chargement de la ligne dans tab
		    String str = lab.nextToken();
		    try {
			double a = Integer.parseInt(str);
			mat[k][j++] = a;	
		    } 
		    catch(NumberFormatException ex) {
			System.out.println ("NumberFormatException"); 
		    }
		}
		ligne_doc = line.readLine();	
	    }
	    
	    line.close();
	} 
	catch( IOException e2 ) {
	    System.err.println(e2); System.exit(1); 
	    
	}
	catch (NullPointerException e3  ) {
	    System.out.println("\nNullPointerException : " + e3 );
	}

	this.m = mat;
    }
    /**
     * Construit une matrice a partir d'un fichier texte. Ce fichier est une representation
     * de la matrice d'incidence d'un graphe.
     */
    Matrice( String nom_fichier, int M, int N) { 
	
	double[][] mat = new double[M][N];
	
	labels  = new Vector();  // labels des sommets en lignes

	try {
	    File file = new File (nom_fichier);
	    Reader rd_result = new FileReader (file);
	  
	    String ligne_doc = null;
	    LineNumberReader line = new LineNumberReader(rd_result);
	    
	    // lecture des labels des sommets en colonnes
	    ligne_doc = line.readLine();
	    int i = 0;
	    StringTokenizer labc = new StringTokenizer(ligne_doc);
	    while ( i < N ) {
		String s = labc.nextToken();
		labels.addElement(s);
		i++;
	    }
	    int l = 0;
	    ligne_doc = line.readLine();
	    // lecture des labels des sommets en lignes
	    while ( ligne_doc != null && l < mat.length ) {
		l++;
		int j = 0;
		int k = line.getLineNumber() - 2;
		StringTokenizer lab = new StringTokenizer(ligne_doc);
		String s = lab.nextToken();
	
		labels.addElement(s);
		
		while( lab.hasMoreTokens() && j < mat[k].length  ) {

		    // chargement de la ligne dans tab
		    String str = lab.nextToken();
		    try {
			double a = Integer.parseInt(str);
			mat[k][j++] = a;	
		    } 
		    catch(NumberFormatException ex) {
			System.out.println ("NumberFormatException"); 
		    }
		}
		ligne_doc = line.readLine();	
	    }
	 
	    line.close();
	} 
	catch( IOException e2 ) {
	    System.err.println(e2); System.exit(1); 
	    
	}
	catch (NullPointerException e3  ) {
	    System.out.println("\nNullPointerException : " + e3 );
	}
	
	
	double[][] Total = new double[M+N][M+N];
	
	for ( int i = 0; i < N; i++ ) 
	    for (int j = 0; j < N; j++ ) 
		Total[i][j] =  0.0;

	for ( int i = N; i < M+N; i++) 
	    for (int j = N; j < M+N; j++ ) 
		Total[i][j] = 0.0;

	for ( int i = N; i < M+N; i++) 
	    for (int j = 0; j < N; j++) 
		Total[i][j] = mat[i-N][j];

	for ( int i = 0; i < N; i++) 
	    for (int j = N; j < M+N; j++ ) 
		Total[i][j] = mat[j-N][i];

	
	this.m = Total;
    }
    
    /*
     * Construit une matrice a partir d'une autre, en copiant les 
     * données de l'autre objet.
     */
    Matrice(Matrice mat)
    {
	this(mat.getM(), mat.getN());
	for (int i = 0; i < m.length; i++)
	    for (int j = 0; j < m[0].length; j++)
		set(i, j , mat.get(i, j));
    }
    
    /**
     * Teste si un tableau a deux dimensions est rectangulaire. 
     * Si le tableau n'est pas rectangulaire, une exception 
     * est lancee.
     @param m le tableau a tester
     @exception NotRectangularArrayException
     */
    private void testRectangulaire(double[][] m)
    {
	int M = m.length;
	if (M == 0)
	    throw new BadMatrixDimensionsException("Matrice Nulle.");
	else if (M == 1)
	    return;  // une ligne, matrice rectangulaire.
	int N = m[0].length;
	for (int i = 1; i < M; i++)
	    {
		if (m[i].length != N)
		    throw new NotRectangularArrayException("The " + i + "th row has a different length.");
	    }
    }
   /* 
      Le calcul de la fermeture transitive d'un graphe représenté par une matrice d'adjacence se 
      fait en répétant le produit de matrices effectué pour calculer le nombres de chemins ( en 
      changeant le + par un | et le * par un & ) jusqu'à obtenir une matrice dont la valeur ne 
      change plus.
   */
    
    public double get(int i, int j) { return m[i][j]; }
    public void set(int i, int j, double v) { m[i][j] = v; }
    public int getM() { return m.length; }
    public int getN() { return m[0].length; }
    public double[][] getMatrice() {return m;}
    /**
     * Retourne la matrice identite de MxM.
     @param M dimension de la matrice identite
     @return matrice identite resultante
     */
    public static Matrice identite(int M)
    {
	Matrice mat = new Matrice(M, M);
	for (int i = 0; i < M; i++)
	    mat.set(i,i,1.0);
	return mat;
    }

    /**
     * Transpose la matrice, retournant un nouvel objet Matrice resultat.
     @return resultat de la transposee
     */
    public Matrice transpose(Matrice matrice)
    {
	Matrice mat = new Matrice(matrice.m[0].length, matrice.m.length);
	for (int i = 0; i < matrice.m.length; i++)
	    for (int j = 0; j < matrice.m[0].length; j++)
		mat.set(j,i, matrice.get(i,j));
	return mat;
    }

    /**
     * Produit de deux matrices, retournant un nouvel objet Matrice resultat.
     @return resultat de la produit
    */
    public Matrice produit( Matrice matrice1, Matrice matrice2 )
    {
	Matrice produit = new Matrice(matrice1.m.length, matrice1.m.length);
	for (int i = 0; i < matrice1.m.length; i++)
	    for (int j = 0; j < matrice2.m.length; j++) {
		for (int k = 0; k < matrice2.m.length; k++) 
		    produit.m[i][j] += matrice1.m[i][k]*matrice2.m[i][k];
	    }
	return produit;

    }
    /**
     * Convertit la matrice en une chaine affichable
     */
    public String toString()
    {
	String str = "Matrice: [";
	for (int i = 0; i < m.length; i++)
	    {
		if (i > 0)
		    str += " ";
		str += "[";
		for (int j = 0; j < m[0].length; j++)
		    {
			if (j > 0)
			    str += " ";
			str += m[i][j];
		    }
		str += "]";
	    }
	str += "]";
	return str;
    }

    /**
     * Verifie si deux matrices sont egales en testant chaque element.
     */
    public boolean equals(Matrice mat)
    {
	if (getM() != mat.getM() || getN() != mat.getN())
	    return false;
	for (int i = 0; i < m.length; i++)
	    for (int j = 0; j < m[0].length; j++)
		if (m[i][j] != mat.get(i,j))
		    return false;
	return true;
    }
}

class NotRectangularArrayException extends RuntimeException
{
    NotRectangularArrayException(String msg)
    {
	super(msg);
    }
}

class BadMatrixDimensionsException extends RuntimeException
{
    BadMatrixDimensionsException(String msg)
    {
	super(msg);
    }
}
