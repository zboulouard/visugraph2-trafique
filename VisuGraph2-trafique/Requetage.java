import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.print.Printable;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.Reader;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MenuListener;
import java.applet.*;
import java.awt.*;
import java.util.Collections;
import java.lang.Object;
import java.util.ArrayList;



class Requetage extends JFrame{
		private JPanel panel; //champs d'instances de l'objet jpanel
	    public JFrame fenetre   = new JFrame("Requêtage");
	    JButton ajouter = new JButton ("Ajout champ");
		JButton notice = new JButton("Notice");
		JButton graphe = new JButton("Graphe");
		JButton quit = new JButton("Fermer");
		JCheckBoxMenuItem et = new JCheckBoxMenuItem("ET");
		JCheckBoxMenuItem et1 = new JCheckBoxMenuItem("ET");
		JCheckBoxMenuItem ou = new JCheckBoxMenuItem("OU");
		JCheckBoxMenuItem ou1 = new JCheckBoxMenuItem("OU");
		Choice choix;
		int nch,nliste,ct;
		List liste,liste2,liste3;	
		String champs_selectionnes="req:";
		String s,sel[]=new String[5];
		String s1="";
		String s2="";
		String s3="";
		String param;
		TextArea t;
		Graphe gr;
		GraphPanel gp;
		int nb_dimensions=3;
		public String requete="";
		
		public class liste extends Applet
		{ Font font = new Font("Helvetica",0,12);
		  Font bold = new Font("Helvetica",1,12);
}
		
		public Requetage(int instance, String param2, Graphe g, GraphPanel gp)
		{
			fenetre.setSize(350,450);//taille
			param = param2;
			gr=g;
			this.gp=gp;
			int j=0;
    		for (int i =0; i<param.length(); i++){
    			char c = param.charAt(i);
    			if((c!='-')&(j==0)) s1+=c;
    			if((c!='-')&(j==1)) s2+=c;
    			if((c!='-')&(j==2)) s3+=c;
    			if ( c=='-') {j+=1; }
      		}

    		if ((s3.equals("")) & (!s1.equals(s2))){nb_dimensions-=1;}
    		if (s1.equals(s2)) {if (s3=="") {nb_dimensions-=2;}else {s2=s3; s3="";nb_dimensions-=1;}}
    		if (s2.equals(s3)) {s3="";nb_dimensions-=1;}
  		
			panel = new JPanel(new GridLayout(3,3));//création d'un panel
			panel.setFont(new Font("Helvetica",Font.BOLD,16));
			panel.add(new Label ("choisissez vos champs"));
			fenetre.getContentPane().add(panel, BorderLayout.NORTH);
			/**************************************** GESTION BOUTONS ****************************************/
			ajouter.addActionListener(new Appli_Requete());
			notice.addActionListener(new Appli_Requete());
			graphe.addActionListener(new Appli_Requete());
			quit.addActionListener(new Appli_Requete());

			/******************************** GESTION BARRE DES BOUTONS **************************************/
			JToolBar bar= new JToolBar("Faites votre choix"); //création d'un JToolBar avec titre
			//ajoute des actions au jtoolbar
			bar.addSeparator();
			bar.add(ajouter);
			bar.addSeparator();
			bar.add(notice);
			bar.addSeparator();
			bar.add(graphe);
			bar.addSeparator(); //met une séparation entre les options
			bar.add(quit);
			//ajoute le jtoolbar au container
			fenetre.getContentPane().add(bar, BorderLayout.SOUTH);
			
			/************************************** GESTION LABEL CHAMP ***************************************/
			//panel = new JPanel();//création d'un panel
			//ajoute un libellé
			JLabel lb = new JLabel("Selectionnez vos champs");
		    //panel.
		    add(lb);//ajoute du libellé au panel

			  
			setBackground(Color.lightGray);
		
			/****************************************** MENU CHAMPS ******************************************/
			ButtonGroup groupMorph = new ButtonGroup();
			groupMorph.add(et);
			groupMorph.add(ou);
			ButtonGroup groupMorph2 = new ButtonGroup();
			groupMorph2.add(et1);
			groupMorph2.add(ou1);
			panel = new JPanel();//création d'un panel
			System.out.println("nb_dimensions : "+nb_dimensions);
			liste = new List(4,true);
			/*-------------------------------------------Dimension 1------------------------------------------*/
			ArrayList<String> l = new ArrayList<String>();
			for (int i = 0; i <g.nombreSommets(); i++){
				Sommet s=g.getSommet(i);
				l.add(s.nomlong);     
			}
			Collections.sort(l);
		
			if (nb_dimensions==1)
				{for (int i = 0; i <l.size(); i++){
					liste.addItem(l.get(i));     
				}
				panel.add(liste);
			}
			/*-------------------------------------------Dimension 2------------------------------------------*/
			if (nb_dimensions == 2){
				liste2 = new List(10,true); //affichage sur 3 lignes, multiple autorisé
				if (g.typeGraphe==2){
					if (s1.equals("dp")|s2.equals("dp")){
						String repertoire = gp.cmd7.concat("/");
					    String fichier  = repertoire.concat("dp.ind");
					    lecture (fichier, liste, g);
					    
					}
					for (int i = 0; i <l.size(); i++){
						liste2.addItem(l.get(i));     
					}			   
				}
				else {
					ArrayList<String> l1 = new ArrayList<String>();			// liste du premier type de sommet ex: auteurs
					ArrayList<String> l2 = new ArrayList<String>();			// liste du second type de sommets ex:journaux
					for (int i = 0; i <g.nombreSommets(); i++){
						Sommet s=g.getSommet(i);
						if (s.type==1)  l1.add(s.nomlong);
						else l2.add(s.nomlong);
					}
					Collections.sort(l1);
					Collections.sort(l2);
					for (int i = 0; i <l1.size(); i++){
						liste.addItem(l1.get(i));     
					}	
					for (int i = 0; i <l2.size(); i++){
						liste2.addItem(l2.get(i));     
					}	
				}
					    
				panel.add(liste);
				panel.add(liste2);
				panel.add(new JLabel(" "));
				panel.add(et);
				panel.add(ou);
				
				panel.add(et1);
				panel.add(ou1);
			}
			/*****************************************DIMENSION 3 A TRAITER *****************************************************************/
		    fenetre.getContentPane().add(panel, BorderLayout.CENTER);//panel ajouté au container
		
		    t = new TextArea(3,40);
		    t.setBackground(Color.yellow);
		    panel.add(t);

		}
		public void lecture (String fichier, List l, Graphe g){
			try {
		        File file = new File (fichier);
			    Reader rd_result = new FileReader (file);
			    String ligne_doc = null;
			    LineNumberReader line = new LineNumberReader(rd_result);
		 	    line = new LineNumberReader(rd_result);
		 	    int n=0;
		 	   while ( (ligne_doc = line.readLine()) != null )
		 	    {
		 		  if (g.typeGraphe==0) {l.addItem(ligne_doc);}
					else l.addItem(ligne_doc);
			    }
		    }
			   
			catch( IOException e2 ) {
			    System.err.println(e2); System.exit(1);

			}
			catch (NullPointerException e3  ) {
			    System.out.println("\nNullPointerException dans isntance 0: " + e3 );
			}
		}
	    private Requetage rq;


		public void paint(Graphics g)
		{  
			ct++;    g.drawString("Ct = "+ct,10,70);
		   g.drawString("Choix "+(nch+1),10,90);
		   g.drawString("Item : "+s,10,110);
		   g.drawString("Sélection : ",120,70);
		   for (int i=0; i<sel.length; i++)
		      g.drawString(sel[i],120,85+15*i);
		   g.drawString("Arg = "+s1,90,110);}
		

		public boolean action(Event evt, Object arg)
		{ s1=arg.toString(); //retour de la chaîne de l'item sélectionné
		  if (evt.target==choix) {
		      nch=choix.getSelectedIndex();//index de l'item sélectionné
		      s=choix.getSelectedItem();
		      if (nch==2) ct=0;} //RAZ du compteur de passage dans paint()
		   else if (evt.target.equals(liste))
		      {sel=liste.getSelectedItems();
			  for (int i=0; i<sel.length; i++){
		  		  t.setText(sel[i]); 
		  		
		  		  }
		      }
		  else return super.action(evt,arg);


		  
		  
		  repaint();
		  return true;}
		
		
		class Appli_Requete implements ActionListener{
			public void actionPerformed(ActionEvent e) {
				
				if (e.getSource()==quit) {fenetre.dispose();}
				if (e.getSource()==ajouter){
			
					String op1="";
					String op2="";
					if (et.isSelected()) op1="et";
					if (ou.isSelected()) op1="ou";
					if (et1.isSelected()) op2="et";
					if (ou1.isSelected()) op2="ou";
					if (!et.isSelected() && !ou.isSelected()) JOptionPane.showMessageDialog(null,"Veuillez choisir un opérateur (et/ou)", "Requête" ,JOptionPane.INFORMATION_MESSAGE);
					else {
						champs_selectionnes+="(";
						for (int i = 0; i < liste.countItems(); i++){
							if (liste.isSelected(i)) {champs_selectionnes+=liste.getItem(i)+"_"+op1+"_";}
						}
						champs_selectionnes=champs_selectionnes.substring(0,champs_selectionnes.length()-4);
						champs_selectionnes+=")";
						champs_selectionnes+="_et_";
						champs_selectionnes+="(";
						for (int i = 0; i < liste2.countItems(); i++){
							if (liste2.isSelected(i)) {champs_selectionnes+=" "+liste2.getItem(i)+"_"+op2+"_";}
						}
						champs_selectionnes=champs_selectionnes.substring(0,champs_selectionnes.length()-4);
						champs_selectionnes+=")";
						/**************************** NETTOYAGE DE LA REQUETE (on enlève les espaces et on met au format _a_b_c_ET_d_e_f_**************************/
			    		
						for (int i =0; i<champs_selectionnes.length(); i++){
			    			char c = champs_selectionnes.charAt(i);
			    			if(c!=' ') {requete+=champs_selectionnes.charAt(i);}
			      		}
						t.setText(requete);
						System.out.println(requete);
					}
								
				}
				if (e.getSource()==graphe){
					JOptionPane.showMessageDialog(null,"Cliquer sur le graphe pour visualiser le résultat", "Requête" ,JOptionPane.INFORMATION_MESSAGE);
					/*-------------------------------------NETTOYAGE GRAPHE -----------------------------------------------------------------*/
					/*On efface tous les sommets pour n'afficher ensuite que les sommets de la requête */
					for (int h=0; h<gr.nombreSommets(); h++){
						Sommet s1 = gr.getSommet(h);
						s1.setInvisible();
						for (int k=0; k<gr.nombreAretes(); k++){
							Arete a =gr.getArete(k); 
							a.setInvisible();
						}
						repaint();
					}
					for (int j = 0; j < gr.nombreSommets(); j++){
						Sommet s= gr.getSommet(j);
						for (int i = 0; i < liste.countItems(); i++){
							if (liste.isSelected(i) && (liste.getItem(i)==s.nomlong)) s.setVisible(); }
						for (int i = 0; i < liste2.countItems(); i++){
							if (liste2.isSelected(i) && (liste2.getItem(i)==s.nomlong)) s.setVisible(); }
					}
					for (int i=0; i< gr.nombreSommets(); i++){
						Sommet s1=gr.getSommet(i);
						gr.getVoisins(s1, false, gr.seuil );
						for ( int j = 0; j < s1.voisins.size(); j++ ) {
							Sommet s2=(Sommet)s1.voisins.get(j);
							if (s1.getVisible() && s2.getVisible()){
							int indice = gr.areteDansGraphe( s1, s2 );
						    if ( indice!=-1 ) {
								Arete a = gr.getArete(indice);
								a.setVisible();}}
						}
					}
					repaint();
					
				}
				if (e.getSource()==notice){
					String synonyme="";
					synonyme=gp.cmd7+"/"+s1+".syn";
					if (nb_dimensions==2)synonyme+="%"+gp.cmd7+"/"+s2+".syn";
					if (nb_dimensions==3)synonyme+="%"+gp.cmd7+"/"+s2+".syn"+"%"+gp.cmd7+"/"+s3+".syn";
					requete='"'+requete+'"';
					try{System.out.println("tableur2 "+gp.cmd1+" "+ gp.cmd2+" "+ gp.cmd3+" "+requete+" "+ gp.cmd5+" "+gp.cmd6+" "+synonyme+" "+gp.cmd8+" "+gp.cmd9);
			    		Runtime.getRuntime().exec("tableur2 "+gp.cmd1+" "+ gp.cmd2+" "+ gp.cmd3+" "+requete+" "+ gp.cmd5+" "+gp.cmd6+" "+synonyme+" "+gp.cmd8+" "+gp.cmd9);}
			    		catch(Exception err){}
				}
				repaint();
			}
	
		}
	}