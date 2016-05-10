import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.ButtonGroup;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

class MenuItemListener implements ActionListener {

	private String forme[][]= new String[2][6];  /** On cr�e une matrice qui comportera toutes formes possibles de sommets. 
													 Nous codons 'O' si la forme est selectionn�e dans le menu, 'N' si elle ne l'est pas **/
    private GraphPanel gp;
    JMenuBar barreMenus;
    JMenu Animation, Labels, Representation, Clustering, icones, edition, ecran, Morphing, Centrality, Aide;
    JMenuItem parametres, fdp, fdpm, fdp2, fdp2m, Random, Requete, arret, circulaire, cluster, stochastique,export, centrer,
	MST, mst, metaGraphe, origine, imprimer, quitter,
	sans, court, longs, pcercle, cercle, histog, classe, sauver, HF, HV, masquage, barre, valien, 
	PV, blanc, noir, Seuil, Seuil2, Repere, aid, init, Degree, Gen_Degree, Proximity, Gen_Proximity, Intermediarity, Gen_Intermediarity;

    public MenuItemListener(GraphPanel gp) {
      	
	this.gp = gp;

	if (gp.control.slider5 != null )
	    gp.control.slider5.addChangeListener(new NouvelleTache());

	barreMenus = new JMenuBar();
 /* Conception du menu � base d'items et de boutons radios */
	edition = new JMenu("Edition");
/*
 * On souligne la premi�re lettre de Edition, montrant ainsi une t�te de liste
 * d'item
 */
	edition.setMnemonic(KeyEvent.VK_E);
	// barreMenus.add(edition);
	

	Centrality = new JMenu("Centrality");
	Centrality.addActionListener(this);
	
	Degree = new JMenuItem("Degree Centrality");
	Degree.addActionListener(this);
	Centrality.add(Degree);
	Centrality.addSeparator();
	
	Gen_Degree= new JMenuItem("Gen_Degree ");
	Gen_Degree.addActionListener(this);
	Centrality.add(Gen_Degree);
	Centrality.addSeparator();
	
	Proximity= new JMenuItem("proximity Centrality");
	Proximity.addActionListener(this);
	Centrality.add(Proximity);
	Centrality.addSeparator();
	
	Gen_Proximity= new JMenuItem("Gen_proximity ");
	Gen_Proximity.addActionListener(this);
	Centrality.add(Gen_Proximity);
	Centrality.addSeparator();
	
	Intermediarity= new JMenuItem("Intermedirity Centrality");
	Intermediarity.addActionListener(this);
	Centrality.add(Intermediarity);
	Centrality.addSeparator();
	
	Gen_Intermediarity= new JMenuItem("Gen_Intermediarity");
	Gen_Intermediarity.addActionListener(this);
	Centrality.add(Gen_Intermediarity);
	Centrality.addSeparator();


	imprimer = new JMenuItem("Imprimer");
	imprimer.addActionListener(this);
	imprimer.setMnemonic(KeyEvent.VK_I);
	edition.add(imprimer);

	Animation = new JMenu("Animation");
	Animation.addActionListener(this);
	Animation.setMnemonic(KeyEvent.VK_A);

	parametres = new JMenuItem("Param�tres");
	parametres.addActionListener(this);
	Animation.add(parametres);
	Animation.addSeparator();
	fdp = new JMenuItem("Forces semi param�tr�es"); // mon FDP
	fdp.addActionListener(this);
	Animation.add(fdp);
	
	/*fdpm = new JMenuItem("Force morphing"); // mon FDP
	fdpm.addActionListener(this);
	Animation.add(fdpm);*/

	fdp2 = new JMenuItem("Forces param�tr�es"); // mon FDP
	fdp2.addActionListener(this);
	Animation.add(fdp2);
	if ( gp.gr.typeMat!=0 &&  gp.gr.typeMat!=1){
		fdp2m = new JMenuItem("Force morphing V1"); // mon FDP
		fdp2m.addActionListener(this);
		Animation.add(fdp2m);
	}
	Requete = new JMenuItem("Requ�tage");
	Requete.addActionListener(this);
	Animation.add(Requete);
	
	arret = new JMenuItem("Arr�ter");   // arret de l'animation
	arret.addActionListener(this);
	Animation.add(arret);
	Animation.addSeparator();

	sauver =  new JMenuItem("Enregistrer");   // arret de l'animation
	sauver.addActionListener(this);
	// Animation.add(sauver);
	// Animation.addSeparator();

	quitter =  new JMenuItem("Quitter");
	quitter.addActionListener(this);
	Animation.add(quitter);

	barreMenus.add(Animation);

	Representation = new JMenu("Repr�sentation"); // menu Representation
	Representation.addActionListener(this);
	Representation.setMnemonic(KeyEvent.VK_R);
	Representation.addSeparator();
	circulaire = new JMenuItem("Circulaire");
	circulaire.addActionListener(this);
	Representation.add(circulaire);

	Random = new JMenuItem("Al�atoire");
	Random.addActionListener(this);
	Representation.add(Random);
	
	/*
	 * Gestion de l'individualit� de la coche : si on coche "Histo Fixe", on ne
	 * peut pas cocher "Histo Variable" et vice - versa
	 */
	ButtonGroup groupeRep = new ButtonGroup();
	groupeRep.add(metaGraphe);
	groupeRep.add(circulaire);

/*	PV = new JMenuItem("Proches voisins");
	PV.addActionListener(this);
	Representation.add(PV);
*/
	Seuil = new JMenuItem ("Transitivit� globale");
	Seuil.addActionListener(this);
	Representation.add(Seuil);

/*	Seuil2 = new JMenuItem ("Transitivit� par instance");
	Seuil2.addActionListener(this);
	Representation.add(Seuil2);*/

	origine = new JMenuItem("Graphe initial");
	origine.addActionListener(this);
	origine.addChangeListener(new NouvelleTache());
	Representation.add(origine);
	if ( gp.gr.typeMat!=0 && gp.gr.typeMat!=1){	
		init = new JMenuItem("Graphe temporel");
		init.addActionListener(this);
		init.addChangeListener(new NouvelleTache());
		Representation.add(init);
	}
	barreMenus.add(Representation);
	barreMenus.add(Centrality);

	Clustering = new JMenu("Clustering");
	Clustering.addActionListener(this);
	Clustering.setMnemonic(KeyEvent.VK_C);

	stochastique = new JMenuItem("Stochastique");
	stochastique.addActionListener(this);
	stochastique.addChangeListener( new NouvelleTache() );
	Clustering.add(stochastique);
	
	metaGraphe = new JMenuItem("Graphe de Clusters");
	metaGraphe.addActionListener(this);
	metaGraphe.addChangeListener(new NouvelleTache());
	Clustering.add(metaGraphe);
	
	export = new JMenuItem("Exporter Classes MCL");
	export.addActionListener(this);
	export.addChangeListener( new NouvelleTache() );
	Clustering.add(export);

	barreMenus.add(Clustering);

	JMenu menu = new JMenu("Sommets");
	menu.setMnemonic('S');

	FishEyeMenu subMenu = null;
	char label = ' ';
	JMenuItem item;
	for ( int i = 0; i < gp.Items.length; i++ ) {
	    if ( label != Character.toUpperCase(gp.Items[i].charAt(0)) ) {
    	label  = Character.toUpperCase(gp.Items[i].charAt(0));
		subMenu = new FishEyeMenu(gp.Items[i].substring(0, 1).toUpperCase());
		menu.add(subMenu);
	    }
	    item = new JMenuItem( gp.Items[i] );
	    item.setLocation(menu.getLocation().x + 20, subMenu.getLocation().y );
	    item.addActionListener(this);
	    item.addChangeListener(new NouvelleTache());
	    subMenu.add(item);
	}
	

	ecran = new JMenu("Ecran");
	ecran.setMnemonic('E');
	barreMenus.add(ecran);

	noir = new JCheckBoxMenuItem("Noir ");
	noir.addActionListener(this);
	ecran.add(noir);

	blanc = new JCheckBoxMenuItem("Blanc ");
	blanc.addActionListener(this);
	ecran.add(blanc);

	centrer = new JMenuItem("Centrer ");
	centrer.addActionListener(this);
	ecran.add(centrer);
	
	barreMenus.add(menu);
	Labels = new JMenu("Labels");
	Labels.setMnemonic('L');
	barreMenus.add(Labels);

	sans = new JCheckBoxMenuItem("Sans");
	sans.addActionListener(this);
	Labels.add(sans);
	
	court = new JCheckBoxMenuItem("Court");
	court.addActionListener(this);       
	Labels.add(court);
	
	longs = new JCheckBoxMenuItem("Long");
	longs.addActionListener(this);            
	Labels.add(longs);

	valien = new JCheckBoxMenuItem("Valeurs des liens");
	valien.addActionListener(this);
	Labels.add(valien);

	icones = new JMenu("Ic�nes");
	icones.setMnemonic(KeyEvent.VK_I);
	barreMenus.add(icones);

	cercle = new JCheckBoxMenuItem("Cercles");
	cercle.addActionListener(this);
	icones.add(cercle);
	if (gp.gr.instance==1) cercle.setSelected(true);
	
	pcercle = new JCheckBoxMenuItem("Nuances");
	pcercle.addActionListener(this);
	icones.add(pcercle);
	
	if ( gp.gr.typeMat!=0 &&  gp.gr.typeMat!=1){
		histog = new JMenuItem("Histogrammes");
		histog.addActionListener(this);
		icones.add(histog);
	
	/*
	 * Boutons permettant l'affichage des histogrammes lors du morphing. Soit chaque
	 * barre de l'histogramme repr�sente une instance (Histo Fixe), soit seule la
	 * barre de l'histogramme, correpondant � l'instance en cours, change de
	 * couleur.
	 */
		HF = new JCheckBoxMenuItem("     Histo fixe");
		HF.addActionListener(this);
		icones.add(HF);
	
		HV = new JCheckBoxMenuItem("     Histo variable");
		HV.addActionListener(this);
		icones.add(HV);
		if (gp.gr.instance>1) HV.setSelected(true);
	}
	barre = new JCheckBoxMenuItem("Barres");
	barre.addActionListener(this);
	icones.add(barre);

	classe = new JCheckBoxMenuItem("Classes");
	classe.addActionListener(this);
	icones.add(classe);

	masquage= new JCheckBoxMenuItem("Masquage"); /*
													 * On masque tous les
													 * sommets non reli�s
													 */
	masquage.addActionListener(this);
	icones.add(masquage);

/*
 * Gestion de l'individualit� de la coche : si on coche "Histo Fixe", on ne peut
 * pas cocher "Histo Variable" et vice - versa
 */
/*	ButtonGroup group0 = new ButtonGroup();
	group0.add(HV);
	group0.add(HF);*/

	ButtonGroup group1 = new ButtonGroup();
	group1.add(noir);
	group1.add(blanc);
	
	if ( gp.gr.typeMat!=0 && gp.gr.typeMat!=1){	
		/* Affichage ou non des rep�res temporels dans le morphing de graphe */
		Morphing = new JMenu("Morphing"); // menu Morphing
		Morphing.setMnemonic('M');
		barreMenus.add(Morphing);
		
		Repere = new JCheckBoxMenuItem("Affichage des rep�res ");
		Repere.addActionListener(this);
		Morphing.add(Repere);
		Repere.setSelected(true);
	}	
	Aide = new JMenu("Aide"); // menu Morphing
	Aide.setMnemonic('D');
	barreMenus.add(Aide);
	
	aid = new JCheckBoxMenuItem(" ? ");
	aid.addActionListener(this);
	Aide.add(aid);

/*
 * Gestion de l'individualit� de la coche : si on coche "sans", on ne peut pas
 * cocher "court" ou "longs" et vice - versa
 */
	ButtonGroup group2 = new ButtonGroup();
	group2.add(sans);
	group2.add(court);
	group2.add(longs);
     }

    public void Dessiner(int cercle){
		if ( gp.lArbre1 != null )  {gp.lArbre1.cercle  = gp.gr.cercle;}
		if ( gp.lArbre2 != null )  {gp.lArbre2.cercle  = gp.gr.cercle;}
		if ( gp.mg != null )       {gp.mg.cercle = gp.gr.cercle;}
		if ( gp.connexe )          {gp.cnx.cercle = gp.gr.cercle;}
		if ( gp.morphing != null ) {gp.morphing.cercle = gp.gr.cercle;}
    }
    public void defaut(){
   
    	if (gp.gr.typeMat==1 | gp.gr.typeMat==0){ 					/** cas des matrices NON temporelles **/
	    	if (pcercle.isSelected()) {
	    		pcercle.setSelected(true);
	    	}
	    	else pcercle.setSelected(false);
	    	if ((pcercle.isSelected()==false) && (classe.isSelected()==false) && (barre.isSelected()==false)) {
	    			cercle.setSelected(true);
	    			gp.gr.cercle = 1;
	    			Dessiner(gp.gr.cercle);
	    	}
		    if (cercle.isSelected()){
		    	gp.gr.cercle = 1;
				Dessiner(gp.gr.cercle);
			}
		    if (pcercle.isSelected()){
		    	gp.gr.cercle = 0;
				Dessiner(gp.gr.cercle);
			}
		    if (classe.isSelected()){
		    	 gp.gr.clustering=true;
		    }
		    if (barre.isSelected())
		    {
		    	gp.param=3;
				gp.gr.cercle = 2;
				if (gp.classon == true) gp.gr.cercle = 3;
				Dessiner(gp.gr.cercle);
		    	}}
	    	else{							/** Cas des matrices temporelles **/
	      		if (pcercle.isSelected()) {
	      			pcercle.setSelected(true);
		    		gp.param=0;
		    		try{ gp.morphing.cercle=0;
		    		Dessiner(gp.morphing.cercle);}catch(Exception n){}
		    	}
	      		if (gp.morphing!=null && HV.isSelected()==false) {gp.morphing.clustering=false;}
			    if ((cercle.isSelected()==false)  && (barre.isSelected()==false)&&(pcercle.isSelected()==false)) {
			   		pcercle.setSelected(false);
			   		
		    		HV.setSelected(true);
		    		if (gp.morphing!=null)gp.morphing.cercle = 2;
		    		gp.param=2;
		    		gp.gr.cercle=2;
		    		if (gp.morphing!=null)Dessiner(gp.morphing.cercle);
			    }
				    if (cercle.isSelected()){
				    	HV.setSelected(false);
				     	try{gp.morphing.cercle = 1;Dessiner(gp.morphing.cercle);}catch(Exception n){}
					}
				    if (classe.isSelected()){
				    	if (HF.isSelected()) HF.setSelected(false);
				    	HV.setSelected(false);
				    	gp.gr.clustering=true;
				    	try{gp.morphing.clustering=true;}catch(Exception n){}
				    }
				    if (HF.isSelected()){
				    	HV.setSelected(false);
				    	pcercle.setSelected(false); cercle.setSelected(false); barre.setSelected(false);
				    	gp.param=1;
				    	try{gp.morphing.cercle=2;
				    	Dessiner(gp.morphing.cercle);}catch(Exception n){}
				    }
				    if (barre.isSelected())
				    {
				    	gp.param=3;
				    	HV.setSelected(false);
						if (gp.gr.clustering==false)gp.gr.cercle=2;
						if (gp.classon == true) {try{gp.morphing.cercle = 3;
						Dessiner(gp.morphing.cercle);}catch(Exception n){}}
						else if (gp.gr.instance >=1){
							try{gp.morphing.cercle=2;
							Dessiner(gp.morphing.cercle);}catch(Exception n){}
						}
				    	}
		
	    	}

    	
	    }
     class NouvelleTache implements ChangeListener {
	public void stateChanged(ChangeEvent e) {

	    if ( !gp.mst1 && !gp.mst2 && !gp.mcl && !gp.connexe && gp.control.slider5 != null) {
		if ( gp.control.slider5.getValue() == 0 ){
		    	gp.frame.setTitle( "VisuGraph : " + gp.titre + "                 Graphe initial");
	
		}else{
		    	gp.frame.setTitle( "VisuGraph : " + gp.titre + "                 Graphe d'instance : "+ gp.control.slider5.getValue());
	
	    	}
	    }
	    if ( gp.mst1 && !gp.mst2 && !gp.mcl && !gp.connexe && gp.lArbre1 != null)
		{gp.frame.setTitle("Arbre de poids maximal");}

	    if ( !gp.mst1 &&  gp.mst2 && !gp.mcl && !gp.connexe  && gp.lArbre2 != null)
		{gp.frame.setTitle ("Arbre de poids minimal");}

	    if ( !gp.mst1 && !gp.mst2 && gp.mcl && !gp.connexe )
		{gp.frame.setTitle ("Graphe de classe" );}

	    if ( ( gp.connexe && !gp.mst1 && !gp.mst2 && !gp.mcl ) ||
		 ( gp.connexe &&  gp.mst1 && !gp.mst2 && !gp.mcl ) ||
		 ( gp.connexe && !gp.mst1 &&  gp.mst2 && !gp.mcl ) ||
		 ( gp.connexe && !gp.mst1 && !gp.mst2 &&  gp.mcl )) {
		if ( gp.individuChoisi != null )
		    {gp.frame.setTitle ("Sous-Graphe de :" + gp.individuChoisi );}
		else
		    {gp.frame.setTitle ("Sous-Graphe de :"  );}
		
	    }
	    gp.repaint();
	}

    }
    public void actionPerformed(ActionEvent e) {
	
	Object source = e.getSource(); // Extrait l'�l�ment du menu s�lectionn�
	Dimension d = gp.getSize();
	
	/*
	 * if( source == sauver ) gp.SauvegarderGraphe(gp.gr); else
	 */
	if( source == imprimer) gp.imprimer();
	else if( source == quitter )  {
	    gp.frame.dispose();
	    gp.control.fenetre.dispose();
	    System.exit(0); // Quitte le programme
	}
	else if (source == valien ) {
	    gp.on = valien.isSelected();
	    gp.gr.stress = gp.on;
	    if ( gp.lArbre1  != null )  gp.lArbre1.stress = gp.on;
	    if ( gp.lArbre2  != null )  gp.lArbre2.stress = gp.on;
	    if ( gp.mg       != null )       gp.mg.stress = gp.on;
	    if ( gp.cnx      != null )      gp.cnx.stress = gp.on;
	    if ( gp.morphing != null ) gp.morphing.stress = gp.on;
	}
	else if (source == centrer){
		/*if (centrer.isSelected()==false) {centrer.setSelected(false);gp.zoom=0;}
		else{
		/*	JOptionPane.showMessageDialog(null,"Cliquer sur la zone que vous voulez agrandir", "Zoom" ,JOptionPane.INFORMATION_MESSAGE);
			gp.zoom=1;*/
			gp.centrer=true;
			JOptionPane.showMessageDialog(null,"Cliquer sur la zone de centrage", "Centrage" ,JOptionPane.INFORMATION_MESSAGE);
		//}
	}
	else if (source == sans ) { // sommets sans labels
	    gp.gr.sans  = true;
	    gp.gr.longs = false;
	    if ( gp.mst1 && gp.lArbre1 != null ) {
		gp.lArbre1.sans  = gp.gr.sans;
		gp.lArbre1.longs = gp.gr.longs;
	    }
	    if ( gp.mst2 && gp.lArbre2 != null ) {
		gp.lArbre2.sans  = gp.gr.sans;
		gp.lArbre2.longs =gp.gr.longs ;
	    }
	    if ( gp.mcl && gp.mg != null ) {
		gp.mg.sans  = gp.gr.sans;
		gp.mg.longs = gp.gr.longs ;
	    }
	    if ( gp.connexe && gp.cnx != null ) {
		gp.cnx.sans  = gp.gr.sans;
		gp.cnx.longs = gp.gr.longs ;	
	    }   
	    if ( gp.morphing != null ) {
		gp.morphing.sans  = gp.gr.sans;
		gp.morphing.longs = gp.gr.longs ;
	    }
	}
	else if (source == court ) { // sommets avec labels courts
	    gp.gr.sans = false;
	    gp.gr.longs = false;
	   if ( gp.mst1 && gp.lArbre1 != null ) {
		gp.lArbre1.sans  = gp.gr.sans;
		gp.lArbre1.longs = gp.gr.longs;
	    }
	    if ( gp.mst2 && gp.lArbre2 != null ) {
		gp.lArbre2.sans  = gp.gr.sans;
		gp.lArbre2.longs = gp.gr.longs ;
	    }
	    if ( gp.mcl && gp.mg != null ) {
		gp.mg.sans  = gp.gr.sans;
		gp.mg.longs = gp.gr.longs ;
	    }
	    if ( gp.connexe && gp.cnx != null ) {
		gp.cnx.sans  = gp.gr.sans;
		gp.cnx.longs = gp.gr.longs ;	
	    }
	    if ( gp.morphing != null ) {
		gp.morphing.sans  = gp.gr.sans;
		gp.morphing.longs = gp.gr.longs ;
	    }
	}
	else if (source == longs ) { // sommets avec labels longs
	    gp.gr.sans = false;
	    gp.gr.longs = true;

	    if ( gp.lArbre1 != null ) {
		gp.lArbre1.sans  = gp.gr.sans;
		gp.lArbre1.longs = gp.gr.longs;
	    }
	    if ( gp.lArbre2 != null ) {
		gp.lArbre2.sans  = gp.gr.sans;
		gp.lArbre2.longs = gp.gr.longs ;
	    }
	    if ( gp.mg != null ) {
		gp.mg.sans  = gp.gr.sans;
		gp.mg.longs = gp.gr.longs ;
	    }
	    if ( gp.connexe && gp.cnx != null ) {
		gp.cnx.sans  = gp.gr.sans;
		gp.cnx.longs = gp.gr.longs ;	
	    }
	    if ( gp.morphing != null ) {
		gp.morphing.sans  = gp.gr.sans;
		gp.morphing.longs = gp.gr.longs ;
	    }
	}
	// type de dessin
	else if (source == Random) { // dessin aleatoire
	    gp.ChangeGraphe = true;
	    gp.random = true;
	    if ( !gp.mcl )
		for (int i = 0 ; i < gp.gr.nombreSommets() ; i++) {
		    Sommet n =  gp.gr.getSommet(i);
		    if (!n.fixe) {
			Point p = gp.gr.nouvellePosition(d.width, d.height);
			n.x = p.x ;
			n.y = p.y;
			gp.ChangeGraphe=true;
		    }
		}
	    else
		for (int i = 0 ; i < gp.mg.nombreSommets() ; i++) {
		    Sommet n =  gp.mg.getSommet(i);
		    if (!n.fixe) {
			Point p = gp.mg.nouvellePosition(d.width, d.height);
			n.x = p.x ;
			n.y = p.y;
			gp.ChangeGraphe=true;
		    }
		}
	}
	else if (source==Requete){
		gp.req= new Requetage(gp.gr.instance, gp.param2, gp.gr, gp);
		this.gp.req.fenetre.setLocation(1000, 100);
		this.gp.req.fenetre.setVisible(true);
    }else if ((source == fdp) | (source == fdpm) |(source==fdp2) |(source==fdp2m)) {  // dessin avec animation
		if (source == fdp) {gp.force_morph=0;gp.new_force=0;}
		if (source == fdp2) {gp.force_morph=0;gp.new_force=1;}
		if (source == fdpm) gp.force_morph=1;
		if (source == fdp2m) {gp.force_morph=2;gp.new_force=1;}
		
	    gp.ChangeGraphe = true;
	    gp.force3 = true;
	    gp.circulaire = false;

	    if ( !gp.connexe && !gp.mst1 && !gp.mst2) {
		gp.Force = new ForceDirect(gp.gr, 10 );
		
		gp.Force.time = (1/gp.control.slider2.getValue()/100)*10;
		gp.Force.Temp(10);
	    } else 
		if ( gp.connexe) { 
		    gp.Force = new ForceDirect(gp.cnx, 10 );
		    
		    gp.Force.time = (1/gp.control.slider2.getValue()/100)*10;
		    gp.Force.Temp(10);
		}
		else 
		    if ( gp.mcl) {

			gp.Force = new ForceDirect(gp.mg, 10 );
			
			gp.Force.time = (1/gp.control.slider2.getValue()/100)*10;
			gp.Force.Temp(10);
		    } 
	    if ( gp.mst1 || gp.mst2) {
		gp.Force = new ForceDirect(gp.mg, 10 );
		
		gp.Force.time = (1/gp.control.slider2.getValue()/100)*10;
		gp.Force.Temp(10);
	    }
	    gp.start();
	}
	else if (source == arret) { // arret de l'animation
		gp.pas=0;
	    gp.force3 = false; 
	    gp.stop();
	    gp.ChangeGraphe = true;
	}
	else if ( source == circulaire ) {
		/*Repere.setSelected(false);
		gp.repmorph=0;*/
	    gp.ChangeGraphe = true;
	    gp.circulaire = true;
	    if ( gp.connexe )
		gp.circulaire(gp.cnx);
	    else
		gp.circulaire(gp.gr);
	    if ( gp.mcl )
		gp.circulaire(gp.mg);
	    if ( gp.morphing != null )
		gp.circulaire(gp.morphing);
	}
	else if ( source == parametres ) { // affichage de la fenetre des
										// parametres
	    gp.control.fenetre.setVisible(true);

	}
	else if ( source == noir ) { // couleur du de l'ecran
	    gp.setBackground(Color.black);
	     gp.gr.SetCouleursAretes(gp.ConversionIntensite(gp.control.slider3.getValue()), 1);
	     gp.couleur_fond=1;
	}
	else if ( source == blanc ) { // couleur de l'ecran
	    gp.setBackground(Color.white);
	    gp.gr.SetCouleursAretes(gp.ConversionIntensite(gp.control.slider3.getValue()), 0);
	    gp.couleur_fond=0;
	}
	else if (source == export){
		try{gp.ecrire_classe(gp.gr);}
		catch(IOException e1){};
	}
	else if (source == stochastique ) { // clustering stochastique
		classif();
			gp.mcl2=true;
	}
	else if (source == origine) { // graphe d'origine
		
		try{
			for (int i = 0; i< gp.gr.nombreSommets(); i++){
				Sommet s = gp.gr.getSommet(i);
				s.setVisible();
			}
			for (int j=0; j < gp.gr.nombreAretes(); j++){
				Arete a = gp.gr.getArete(j);
				if (!a.getE1().nom.contains("Repere") && !a.getE2().nom.contains("Repere") &&!a.getE1().nom.contains("virtuel") && !a.getE2().nom.contains("virtuel"))
				a.setVisible();
			}
			gp.control.slider11.setValue(0);
			gp.init_max=0;
			for (int i = 0; i< gp.gr.nombreSommets(); i++){
				Sommet s = gp.gr.getSommet(i);
				s.setVisible();
			}
			for (int j=0; j < gp.gr.nombreAretes(); j++){
				Arete a = gp.gr.getArete(j);
				if (!a.getE1().nom.contains("Repere") && !a.getE2().nom.contains("Repere") &&!a.getE1().nom.contains("virtuel") && !a.getE2().nom.contains("virtuel"))
				a.setVisible();
			}

		}
		catch(NumberFormatException el){}
	    gp.ChangeGraphe = true;
	    gp.mst1 = false;
	    gp.mst2 = false;
	    gp.mcl = false;


	    gp.control.slider1.setMinimum((int)gp.gr.minArete);
	    gp.control.slider1.setValue((int)gp.gr.minArete);
	    gp.control.slider1.setMaximum((int)gp.gr.maxArete);
	    gp.control.slider1.setMinorTickSpacing(1);
	    
		if (gp.gr.instance>1){
		if (gp.control.slider5.getValue()>0) {
			for (int i = 0; i< gp.gr.nombreSommets(); i++){
				Sommet s = gp.gr.getSommet(i);
				if (s.Metrique[gp.control.slider5.getValue()]==0) s.setInvisible();
			}
		}
	}

	}
	else if (source == init) { 
		gp.circulaire=false;
		gp.placementOriente(gp.gr);
		gp.ChangeGraphe=true;
		gp.centrerGraphe(gp.gr);
		try{
			for (int i = 0; i< gp.gr.nombreSommets(); i++){
				Sommet s = gp.gr.getSommet(i);
				s.setVisible();
			}
			for (int j=0; j < gp.gr.nombreAretes(); j++){
				Arete a = gp.gr.getArete(j);
				if (!a.getE1().nom.contains("Repere") && !a.getE2().nom.contains("Repere"))
				a.setVisible();
			}
			gp.control.slider11.setValue(0);
			gp.init_max=0;


		}
		catch(NumberFormatException el){}
	    gp.ChangeGraphe = true;
	    gp.mst1 = false;
	    gp.mst2 = false;
	    gp.mcl = false;


	    gp.control.slider1.setMinimum((int)gp.gr.minArete);
	    gp.control.slider1.setValue((int)gp.gr.minArete);
	    gp.control.slider1.setMaximum((int)gp.gr.maxArete);
	    gp.control.slider1.setMinorTickSpacing(1);
	}
     else if (source == Seuil){
   		if (gp.gr.instance>1){
   			if (gp.control.slider5.getValue()>=1){
	   		JOptionPane.showMessageDialog(null,"La selection d'un sommet se fait sur le graphe global", "Seuillage par voisinage" ,JOptionPane.INFORMATION_MESSAGE);
	  		gp.control.slider5.setValue(0);
   			}
   			else JOptionPane.showMessageDialog(null,"Veuillez selectionner un sommet", "Seuillage par voisinage" ,JOptionPane.INFORMATION_MESSAGE);
       	}else JOptionPane.showMessageDialog(null,"Veuillez selectionner un sommet", "Seuillage par voisinage" ,JOptionPane.INFORMATION_MESSAGE);
     }else if ( source == metaGraphe ) { // graphe de clusters : meta-graphe
    	 	if (gp.gr.instance >1) Repere.setSelected(false);
    	 	gp.repmorph=0;
    	 	gp.gr.repaint();
    	  	if ( gp.gr.typeMat == 2 || gp.gr.typeMat == 3)  	 classif();
    	  	else{ 
    	  		gp.ChangeGraphe = true;
	     	    if (gp.force3 ) gp.force3 = false;
	     	    gp.mst1 = false;
	     	    gp.mst2 = false;
	     	    if ( gp.gr.typeMat == 1 ) {
	     		gp.mcl = true;
	     		gp.mg.cercle  = gp.gr.cercle;
	     	    }
	     	    gp.control.slider1.setMinimum((int)gp.mg.minArete);
	     	    gp.control.slider1.setValue((int)gp.mg.minArete);
	     	    gp.control.slider1.setMaximum((int)gp.mg.maxArete);
	     	    gp.control.slider1.setMinorTickSpacing(1);
	
	     	    gp.ChangeGraphe = true;
	     	    gp.force3 = false;
	
	     	    gp.stop();
	     	    String chemin =  System.getProperty("cheminResult");
	     	    String cheminin  =  chemin + "/clusters.out";
	     	    String cheminout =  chemin + "/out.clusters";
	
	     	    gp.MCL(cheminin);
	     	}
    	  	String chemin =  System.getProperty("cheminResult");
     	    String cheminout =  chemin + "/out.clusters";
     	    if ( gp.gr.clustering ) {
     		System.out.println("Fin de clustering ");
     		gp.cluster2 = true;
     		gp.mcl = true;
     		gp.lireClustersMCL(cheminout);

     		gp.initColor();
     		gp.mg.sans = gp.gr.sans;
     		gp.mg.longs = false;
     		gp.mg.cercle  = gp.gr.cercle;

     		for (int i = 0; i < gp.nbClusters; i++ ) {
     		    for (int j = 0; j < gp.Clusters[i].size(); j++) {
     			int ind = ((Integer)(gp.Clusters[i].elementAt(j))).intValue();
     			try {
     			    Sommet s = gp.gr.getSommet(ind);
     			    s.couleurClasse = gp.CouleursClusters[i];
     			}catch (ArrayIndexOutOfBoundsException exception) {
     			    ;
     			}
     		    }
     		}
     		gp.genereClusters(gp.gr);
     		if (gp.flag) gp.circulaireClustered(gp.gr);
     		gp.flag = true;
     		gp.mg.clustering=true;

     	    }


	}
	else if ( source == pcercle  ) {
	    if (pcercle.isSelected()) {
	    	pcercle.setSelected(true);
	    	cercle.setSelected(false);
	    	if (gp.gr.instance>1)HF.setSelected(false);
	    	gp.gr.cercle = 0;
			Dessiner(gp.gr.cercle);
	    } else {
	    	//pcercle.setSelected(true);
	    		gp.gr.cercle = 2;
			if ( gp.lArbre1 != null )
				gp.lArbre1.cercle  = gp.gr.cercle;
			if ( gp.lArbre2 != null )
				gp.lArbre2.cercle  = gp.gr.cercle;
			if ( gp.mg != null )
				gp.mg.cercle  = gp.gr.cercle;
			if ( gp.connexe )
				gp.cnx.cercle  = gp.gr.cercle;
			if ( gp.morphing != null )
				gp.morphing.cercle = gp.gr.cercle;
		}
		if (cercle.isSelected()) {   	gp.gr.cercle = 1;
		Dessiner(gp.gr.cercle);}
		if(barre.isSelected()) barre.setSelected(false);
	} else if ( source == cercle ) {
	    if (cercle.isSelected()) {
	    	gp.gr.cercle = 1;
			Dessiner(gp.gr.cercle);
			pcercle.setSelected(false);
			barre.setSelected(false);
			HF.setSelected(false);
	     }else {
	      		gp.gr.cercle = 2;
			if ( gp.lArbre1 != null )
				gp.lArbre1.cercle  = gp.gr.cercle;
			if ( gp.lArbre2 != null )
				gp.lArbre2.cercle  = gp.gr.cercle;
			if ( gp.mg != null )
				gp.mg.cercle  = gp.gr.cercle;
			if ( gp.connexe )
				gp.cnx.cercle  = gp.gr.cercle;
			if ( gp.morphing != null )
				gp.morphing.cercle = gp.gr.cercle;
			
			if (gp.classon == true) gp.gr.cercle = 3;
			else {gp.gr.cercle = 2;}
			gp.param = 2;
			Dessiner(gp.gr.cercle);
		}
		/*
		 * on est dans le cas o� cercle a �t� coch� ou d�coch�. on veut g�rer
		 * alors la coche de classe et avoir les deux � la fois (classe et
		 * cercle). on r�applique l'algorithme qui trace les classes et on
		 * controle qu'aucun autre choix (nuances,histo, barre..) ne soit
		 * possible.
		 */
		if (classe.isSelected()) {

		        gp.gr.clustering=true;
		}
	} else if ( source == histog ) {
	    gp.param=2;
	    gp.gr.cercle = 2;
	    if ( gp.lArbre1 != null )
	    if ( gp.lArbre1 != null )
		gp.lArbre1.cercle  = gp.gr.cercle;
	    if ( gp.lArbre2 != null )
		gp.lArbre2.cercle  = gp.gr.cercle;
	    if ( gp.mg != null )
		gp.mg.cercle  = gp.gr.cercle;
	    if ( gp.connexe )
		gp.cnx.cercle  = gp.gr.cercle;
	    if ( gp.morphing != null )
		gp.morphing.cercle = gp.gr.cercle;
	    cercle.setSelected(false);
	    pcercle.setSelected(false);
	    barre.setSelected(false);

	} else if ( source == classe ) {
		gp.classon = classe.isSelected();
		if (!gp.gr.clustering) {classif(); }
	    /* if ( gp.gr.typeMat == 1 || gp.gr.typeMat == 2 ) */
		gp.gr.cercle = 1;
		if (gp.classon == true){
			Dessiner(gp.gr.cercle);
		}
		else{
			gp.gr.clustering=false;
			gp.gr.cercle = 1;
			Dessiner(gp.gr.cercle);
			if ( gp.lArbre1 != null )  gp.lArbre1.cercle  = gp.gr.cercle;
			if ( gp.lArbre2 != null )  gp.lArbre2.cercle  = gp.gr.cercle;
			if ( gp.mg != null )       gp.mg.cercle  = gp.gr.cercle;
			if ( gp.connexe )          gp.cnx.cercle  = gp.gr.cercle;
			if ( gp.morphing != null ) gp.morphing.cercle = gp.gr.cercle;
			/*if (gp.gr.instance>1){
			gp.morphing.cercle = 2;
			Dessiner(gp.morphing.cercle);
			}*/
		}
		if(pcercle.isSelected())    {pcercle.setSelected(false); pcercle.setSelected(true);}
		else if(barre.isSelected())      {barre.setSelected(false); barre.setSelected(true);}
		if (pcercle.isSelected()) {   	gp.gr.cercle = 0;
			Dessiner(gp.gr.cercle);
			pcercle.setSelected(true);
			barre.setSelected(false);}
		gp.ChangeGraphe=true;
	} else if (source == HV){
		if (HV.isSelected()==false){HV.setSelected(false);}
		else {HV.setSelected(true);pcercle.setSelected(false);barre.setSelected(false);}
		gp.param=2;
		gp.gr.cercle = 2;
		Dessiner(gp.gr.cercle);
		if (cercle.isSelected()) cercle.setSelected(false);
	} else if (source == HF){
		System.out.println("etat de HF : "+HF.isSelected());
		if (HF.isSelected()==false){HF.setSelected(false);}
		else {HF.setSelected(true);}
		gp.param=1;
		gp.gr.cercle = 2;
		Dessiner(gp.gr.cercle);

	}else if (source == barre){
		if (gp.gr.instance>1){if (HF.isSelected()) HF.setSelected(false);}
	    gp.groupon = barre.isSelected();
	    if (gp.groupon == true) {
		gp.param=3;
		gp.gr.cercle = 2;
		if (gp.classon == true) gp.gr.cercle = 3;
		cercle.setSelected(false);
		pcercle.setSelected(false);
		Dessiner(gp.gr.cercle);
	    }else {
		if (gp.classon == true) gp.gr.cercle = 3;
		else {gp.gr.cercle = 2;}
		gp.param = 2;
		Dessiner(gp.gr.cercle);
		}

	}else if (source==Repere){
		if (Repere.isSelected())gp.repmorph=1;
		else gp.repmorph=0;
		gp.gr.repaint();
	}else if (source==aid){
		gp.aide_en_ligne= new Aide();
		this.gp.aide_en_ligne.fenetre.setLocation(1000, 100);
		this.gp.aide_en_ligne.fenetre.setVisible(true);
    }else if (source == masquage){
	    gp.ChangeGraphe = true;
	    gp.masq = masquage.isSelected();
	    gp.gr.masque   = gp.masq;
	    Graphe graphe = new Graphe(false);
	    if ( !gp.mst1 && !gp.mst2 && !gp.mcl ) { gp.gr.masque = gp.masq; graphe = gp.gr ; }
	    if (  gp.mst1 && !gp.mst2 && !gp.mcl ) { gp.lArbre1.masque = gp.masq; graphe = gp.lArbre1;}
	    if ( !gp.mst1 &&  gp.mst2 && !gp.mcl ) { gp.lArbre2.masque = gp.masq; graphe = gp.lArbre2;}
	    if ( !gp.mst1 && !gp.mst2 &&  gp.mcl ) { gp.mg.masque = gp.masq; graphe = gp.mg ;}
	    if ( gp.connexe )                { gp.cnx.masque = gp.masq; graphe = gp.cnx;}
	    if ( gp.morphing != null )       { gp.morphing.masque = gp.masq; graphe = gp.morphing; }

	    for ( int i = 0; i < graphe.nombreSommets(); i++ ) {
		Sommet v = graphe.getSommet(i);
		graphe.getVoisins(v, false, graphe.seuil );

		if ( graphe.degre(graphe.indiceSommet(v)) == 0 &&
		     v.getVisible() && gp.masq &&(!v.nom.contains("Repere")))
		    v.setInvisible();

		if ( graphe.degre(graphe.indiceSommet(v)) == 0 &&
		     !gp.masq )
		    v.setVisible();
	    }
	    if ( gp.circulaire ) gp.circulaire(graphe);
    }
		else { // le cas d'exploration a partir d'un focus.
			 JMenuItem item = (JMenuItem)e.getSource();
		    gp.individuChoisi = item.getText();
		    Sommet selectionne = null;


		    int w = (int)(d.width- 20)/2;
		    int h = (int)(d.height-6)/2;
		    boolean trouve = false;
		    gp.cnx = null;
		    gp.cnx = new Graphe(false);
		    gp.cnx.typeGraphe = gp.gr.typeGraphe;
		    gp.cnx.instance = gp.gr.instance;

		    gp.cnx.stress = gp.gr.stress;
		    gp.cnx.noms   = gp.gr.noms;
		    gp.cnx.cercle = gp.gr.cercle;
		    gp.cnx.longs  = gp.gr.longs;
		    gp.cnx.sans   = gp.gr.sans;
		    gp.cnx.maxMetrique1 = gp.gr.maxMetrique1;
		    gp.cnx.maxMetrique2 = gp.gr.maxMetrique2;
		    gp.cnx.maxArete = gp.gr.maxArete;
		    gp.cnx.typeMat = gp.gr.typeMat;
		    // Identification d'item selectionn� dans le graphe d'origine
		    if ( !gp.mst1 || !gp.mst2 && gp.connexe == true) {
			for ( int i = 0; i < gp.gr.nombreSommets() && !trouve; i++) {
			    Sommet s = gp.gr.getSommet(i);
			    s.setCouleurTxt(Color.white);
			    if ( s.fixe ) {
				s.x += 20;
				s.y += 20;
				s.fixe = false;
			    }
			    s.marked = false;
			    s.niveau = 100;

			    if (( s.nom.compareToIgnoreCase(gp.individuChoisi) == 0 ||
				 s.nomlong.compareToIgnoreCase(gp.individuChoisi) == 0 ) ) {

				selectionne = s;
				selectionne.fixe = true;
				selectionne.x = w;
				selectionne.y = h;
				s.x = w;
				s.y = h;
				trouve = true;
				s.setCouleur(Color.black);
				//s.setCouleurTxt(Color.red);
				s.niveau = 0;
				gp.cnx.ajouterSommet(s);
			    }
			}
			// Recherche des voisins de l'item selectionn�
			gp.gr.getVoisins( selectionne, false, gp.cnx.seuil );
			for ( int i = 0; i < selectionne.voisins.size(); i++ ) {
			    Sommet s = (Sommet)selectionne.voisins.elementAt(i); 
			    gp.cnx.ajouterSommet(s);
			    s.niveau = 1;
			    s.setCouleur(Color.red);
			    s.setCouleurTxt(Color.white);
			    //s.fixe = true;
			    int indice = gp.gr.areteDansGraphe(selectionne, s);  
			    if ( indice != -1 ) {
				Arete a = gp.gr.getArete(indice);
				gp.cnx.ajouterArete(a);
			    }
			}
			for ( int i = 0; i < gp.cnx.nombreSommets(); i++) { 
			    Sommet si = gp.cnx.getSommet(i);
			    for ( int j = 0; j < gp.cnx.nombreSommets(); j++) { 
				Sommet sj = gp.cnx.getSommet(j);
				if ( i != j ) {
				    int indice = gp.gr.areteDansGraphe(si, sj);
				    if ( indice != -1 ) {
					Arete a = gp.gr.getArete(indice);
					gp.cnx.ajouterArete(a);
				    }
				}
			    }
			}
		    }
		    if ( gp.mst1 && !gp.mst2 && gp.connexe == true ) {
			for ( int i = 0; i < gp.lArbre1.nombreSommets(); i++) {
			    Sommet s = gp.lArbre1.getSommet(i);

			    if ( s.fixe ) {
				s.x += 20;
				s.y += 20;
				s.fixe = false;
			    }
			    if ( s.nom.compareTo(gp.individuChoisi) == 0 ) {
				selectionne = s;
				selectionne.fixe = true;
				selectionne.x = w;
				selectionne.y = h;
			    }
			}
		    }
		    if ( !gp.mst1 && gp.mst2 && gp.connexe == true ) {
			for ( int i = 0; i < gp.lArbre2.nombreSommets(); i++) {
			    Sommet s = gp.lArbre2.getSommet(i);
			    if ( s.fixe ) {
				s.x += 20;
				s.y += 20;
				s.fixe = false;
			    }
			    if ( s.nom.compareTo(gp.individuChoisi) == 0 ) {
				selectionne = s;
				selectionne.fixe = true;
				selectionne.x = w;
				selectionne.y = h;
			    }
			}
		    }
		    gp.cnx.initDegre();
		    gp.cnx.maximumMetrique();
		    gp.cnx.maximumArete();
		    
		}
		gp.repaint();
		defaut();
	    }
    public void classif(){
    	
	    gp.ChangeGraphe = true;
	 
	    String chemin =  System.getProperty("cheminResult");
	    String cheminin  =  chemin + "/clusters.out";
	    String cheminout =  chemin + "/out.clusters";

	    gp.MCL(cheminin);
	    if ( gp.gr.clustering ) {
		System.out.println("Fin de clustering ");
		gp.cluster2 = true;
		gp.mcl = true;
		gp.lireClustersMCL(cheminout);
		gp.initColor();
		gp.mg.sans = gp.gr.sans;
		gp.mg.longs = false;
		gp.mg.cercle  = gp.gr.cercle;

		for (int i = 0; i < gp.nbClusters; i++ ) {
		    for (int j = 0; j < gp.Clusters[i].size(); j++) {
				int ind = ((Integer)(gp.Clusters[i].elementAt(j))).intValue();
				try {
				    Sommet s = gp.gr.getSommet(ind);
				    s.couleurClasse = gp.CouleursClusters[i];
				}catch (ArrayIndexOutOfBoundsException exception) {}
		    }
	    }

		//gp.genereClusters(gp.gr);
		if (gp.flag) {
			try{
				gp.circulaireClustered(gp.gr);
			}
			catch(Exception n){
				gp.genereClusters(gp.gr);
				gp.circulaireClustered(gp.gr);
			}
	    gp.ChangeGraphe = true;
	    gp.mst1 = false;
	    gp.mst2 = false;
	    gp.mcl = false;}
	
	    }
    }
	}

