import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.Object.*;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import java.io.*;
;class ControlWindow extends JPanel implements ActionListener {
    
    JFrame fenetre   = new JFrame("Param�tres");
    JFrame vitesse   = new JFrame("Vitesse du morphing");

    /* echelle de valeurs selectionnables pour les diff�rentes cat�gories*/
    JSlider slider1  = new JSlider();
    JSlider slider2  = new JSlider();   /** temperature des forces **/
    JSlider slider3  = new JSlider();   /** Intensit� des liens **/
    JSlider slider4  = new JSlider();   /** densit� de clustering **/
    JSlider slider5  = null;            /** Changement d'instance **/
    JSlider slider6  = new JSlider();   /** distance de r�pulsion **/
    JSlider slider8  = new JSlider();   /** distance d'attraction **/
    JSlider slider7 = new JSlider();    /** slider pour l'intensit� de la couleur de la police **/
    JSlider slider9 = new JSlider();    /** slider pour la taille de la couleur de la police **/
    JSlider slider10 = new JSlider();   /** slider du K-Core **/
    JSlider slider11 = new JSlider();	/** slider de la transitivit� **/
    JSlider slider12 = new JSlider();   /** slider pour g�rer la force d'attirance vers les sommets temporels **/
    JSlider slider13 = new JSlider();   /** slider pour g�rer la vitesse du morphing **/
    JSlider slider14 = new JSlider();  // slider pour paramétrer centralité
    Image imageRelache;
    Image imageEnfonce;
    JPanel panel0, panel, panel1, panel2, panel3, panel6, panel7, panel14;

    /* d�claration des boutons pour le morphing, permettant de passer d'une instance � l'autre.*/
    JButton avance, recule, reset, compteur, evolution1, evolution2, reglage;
    Icon icon1, icon2, icon3;
    Color b = new Color (128,191,212);
    Color c = new Color (200,130,100);
    Color d = new Color (150,130,220);
    Color e = new Color (200,0,150);
    Color f = new Color (150,130,200);
    Color g = new Color (150,200,250);
    JLabel T;
	Font fnt1 = new Font("Time", Font.PLAIN, 6);
    String cpt="0";

    ControlWindow(int instance) {


	fenetre.setSize(180, (instance > 1)?800:600);
	fenetre.setResizable(false);
	vitesse.setBackground(Color.lightGray);
    this.fenetre.setLocation(0, 50);
	Dimension d0 = new Dimension (170,25);
	
	JPanel panel0 = new JPanel(new GridLayout(0, 1) );
	add("Center", panel0 );
    setFont(fnt1);
	panel0.add( new JLabel("    Temp�rature : t"), BorderLayout.CENTER );	// slider pour la force de r�pulsion entre sommets
	panel0.setBackground(b);	// couleur du panneau
	slider2.setMinimum(1);	// valeur minimale du slider
	slider2.setValue(20);
	slider2.setMaximum(40);	// Graduation maximale du slider
	slider2.setPaintTicks(true);	// pour afficher les graduations du slider
	slider2.setPaintLabels(true);
	slider2.setMinorTickSpacing(1);	// espacement entre deux graduations du slider
	slider2.setPreferredSize(d0);		// couleur du label
	panel0.add(slider2);	// fixage du slider sur le panneau

	
	
	
	
	panel0.add( new JLabel("    Coefficient r�pulsion"), BorderLayout.CENTER );
	slider6.setValue(10);
	slider6.setMaximum(20);
	slider6.setMinimum(1);
	slider6.setValue(3);
	slider6.setMinorTickSpacing(1);
	slider6.setSnapToTicks(true);
	slider6.setPaintTicks(true);
	slider6.setPaintTrack(true);
	slider6.setPaintLabels(true);
	slider6.setPreferredSize(d0);
	panel0.add(slider6);

 	panel0.add( new JLabel("    Distance cible attraction"), BorderLayout.CENTER );
	slider8.setValue(10);
	slider8.setMaximum(20);
	slider8.setValue(14);
	slider8.setMinorTickSpacing(1);
	slider8.setSnapToTicks(true);
	slider8.setPaintTicks(true);
	slider8.setPaintTrack(true);
	slider8.setPaintLabels(true);
	slider8.setPreferredSize(d0);
	panel0.add(slider8);
	
	if (instance>1){
		panel0.add( new JLabel("    Force rep�res temporels"), BorderLayout.CENTER );
		slider12.setValue(0);
		slider12.setMaximum(15);
		slider12.setMinimum(0);
		slider12.setMinorTickSpacing(1);
		slider12.setSnapToTicks(true);
		slider12.setPaintTicks(true);
		slider12.setPaintTrack(true);
		slider12.setPaintLabels(true);
		slider12.setPreferredSize(d0);
		panel0.add(slider12);
	    /* recours � un FlowLayout pour permettre d'afficher les deux fl�ches sur la m�me ligne et non pas l'une sous l'autre.*/
	    panel2 = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 1));
	    add("Center", panel2);
	}
	JPanel panel = new JPanel(new GridLayout(0, 1) );
	add("Center", panel );
	panel.setBackground(c);
	panel.add( new JLabel("    Filtrage  "), BorderLayout.CENTER );	// slider pour le filtrage des ar�tes
	slider1.setPaintTicks(true);
	slider1.setPaintLabels(true);
	slider1.setPreferredSize(d0);
	slider1.setValue(0);
	panel.add(slider1);

	/* KCORE*/
	JPanel panel5 = new JPanel(new GridLayout(0, 1) );		
	add("Center", panel5 );
	panel5.setBackground( new Color (200,130,150));
	panel5.add( new JLabel("    KCore  "), BorderLayout.CENTER );	// slider pour le filtrage des sommets		 gp.control.panel7.set
	slider10.setPaintTicks(true);
	slider10.setPaintLabels(true);
	slider10.setValue(0);
	slider10.setPreferredSize(d0);
	
	panel5.add(slider10);
	JPanel panel7 = new JPanel(new GridLayout(0, 1) );
	add("Center", panel7 );
	/** TRANSITIVITE **/
	panel7.setBackground( new Color (200,130,150));
	panel7.add( new JLabel("    Transitivit�  "), BorderLayout.CENTER );	
	slider11.setMinorTickSpacing(1);
	slider11.setSnapToTicks(true);
	slider11.setPaintTicks(true);
	slider11.setPaintTrack(true);
	slider11.setPaintLabels(true);
	slider11.setValue(0);
	slider11.setMinimum(0);
	slider11.setPreferredSize(d0);
	
	panel7.add(slider11);
	//panel7.setVisible(false);
	
	// slider pour l'intensit� des ar�tes
	JPanel panel6 = new JPanel(new GridLayout(0, 1) );
	add("Center", panel6 );
 	panel6.add( new JLabel("    Intensit� des liens"), BorderLayout.CENTER );
 	panel6.setBackground(f);
	slider3.setValue(4);
	slider3.setMaximum(10);
	slider3.setMinorTickSpacing(1);
	slider3.setSnapToTicks(true);
	slider3.setPaintTicks(true);
	slider3.setPaintTrack(true);
	slider3.setPaintLabels(true);
	slider3.setPreferredSize(d0);
	panel6.add(slider3);
	
	JPanel panel3 = new JPanel(new GridLayout(0, 1) );
	add("Center", panel3 );
 	panel3.add( new JLabel("    Intensit� de la police"), BorderLayout.CENTER );
 	panel3.setBackground(e);
	slider7.setValue(6);
	slider7.setMaximum(12);
	slider7.setMinorTickSpacing(1);
	slider7.setSnapToTicks(true);
	slider7.setPaintTicks(true);
	slider7.setPaintTrack(true);
	slider7.setPaintLabels(true);
	//slider3.setBackground(Color.lightGray);
	slider7.setPreferredSize(d0);
	panel3.add(slider7);
	
 	panel3.add( new JLabel("    Taille de la police"), BorderLayout.CENTER );
 	panel3.setBackground(e);
	slider9.setValue(6);
	slider9.setMaximum(12);
	slider9.setMinorTickSpacing(1);
	slider9.setSnapToTicks(true);
	slider9.setPaintTicks(true);
	slider9.setPaintTrack(true);
	slider9.setPaintLabels(true);
	slider9.setPreferredSize(d0);
	panel3.add(slider9);

	// slider pour le clustering
	JPanel panel4 = new JPanel(new GridLayout(0, 1) );
	add("Center", panel4 );
	panel4.setBackground(g);
	panel4.add(new JLabel("   Densit� de clustering"), BorderLayout.CENTER );
	slider4.setMinimum(2);
	slider4.setValue(4);
	slider4.setMaximum(10);
	slider4.setMinorTickSpacing(1);
	slider4.setSnapToTicks(true);
	slider4.setPaintTicks(true);
	slider4.setPaintTrack(true);
	slider4.setPaintLabels(true);
	//slider4.setBackground(Color.lightGray);
	slider4.setPreferredSize(d0);
	panel4.add(slider4);
	
	
	JPanel panel1 = new JPanel(new GridLayout(0, 1) );
	add("Center", panel1 );
	panel1.setBackground(b);

	if ( instance > 1 ) {

	    panel0.add(new JLabel("   Choix d'instances"), BorderLayout.CENTER );
	    slider5 = new JSlider();
	    slider5.setMinimum(0);
	    slider5.setValue(0);
	    slider5.setMaximum(instance);
	    slider5.setMinorTickSpacing(1);
	    slider5.setSnapToTicks(true);
	    slider5.setPaintTicks(true);
	    slider5.setPaintLabels(true);
	    slider5.setPreferredSize(d0);
	    panel0.add(slider5);

	    panel0.add(new JLabel("   Vitesse morphing"), BorderLayout.CENTER );
	    slider13 = new JSlider();
	    slider13.setMinimum(1);
	    slider13.setValue(5);
	    slider13.setMaximum(15);
	    slider13.setMinorTickSpacing(1);
	    slider13.setSnapToTicks(true);
	    slider13.setPaintTicks(true);
	    slider13.setPaintLabels(true);
	    slider13.setPreferredSize(d0);
	    panel0.add(slider13);
	    


	    /* Chemins d'acc�s pour atteindre l'image de la fl�che : � modifier si on installe cette version sur une autre machine!!!*/
	    icon2 = new ImageIcon("reset.gif");
	    reset = new JButton(icon2);
	    Dimension d = new Dimension( icon2.getIconWidth(), icon2.getIconHeight()  );
	    reset.setPreferredSize(d);
	   //panel2.add(reset);
	    String s=System.getenv("TETRALOGIEBIN");
	    s+="/VisuGraph2";
	    File n=new File(s);
	    icon3 = new ImageIcon(n.getAbsolutePath()+"/previous.gif");
	    recule = new JButton(icon3);
	    d = new Dimension(icon3.getIconWidth(), icon3.getIconHeight() );
	    recule.setPreferredSize(d);
	    panel2.add(recule);
	    
	    icon1 = new ImageIcon(n.getAbsolutePath()+"/next.gif");
	    avance = new JButton(icon1);
	    d = new Dimension( icon1.getIconWidth(), icon1.getIconHeight()  );
	    avance.setPreferredSize(d);
	    panel2.add(avance);

	    compteur = new JButton(cpt);
	    panel2.add(compteur );

	    avance.addActionListener(this);
	    recule.addActionListener(this);
	    add("Center", new JPanel());
	    recule.setEnabled(false);
	    reset.setEnabled(false);
	    JPanel pvitesse = new JPanel();
	    pvitesse.setBackground(Color.lightGray);
	    JSlider jvitesse =  new JSlider();
	    vitesse.setBackground(Color.lightGray);
	    jvitesse.setInverted(true);
	    jvitesse.setMinimum(100);
	    jvitesse.setValue(500);
	    jvitesse.setMaximum(1000);
	    jvitesse.setPaintTicks(true);
	    jvitesse.setPaintLabels(true);
	    jvitesse.setMinorTickSpacing(10);

	    pvitesse.add(jvitesse);
	    vitesse.setSize(210,100);
	    vitesse.setResizable(false);
	    vitesse.getContentPane().add(pvitesse);

	}
	fenetre.getContentPane().add(this, BorderLayout.CENTER );

    }

    public void actionPerformed(ActionEvent e) {}


}
