/*
 * DocumentViewer.java  1.0
 * 
 * Copyright (c) 1999 Emmanuel PUYBARET - eTeks.
 * All Rights Reserved.
 *
 */

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLFrameHyperlinkEvent;

// Classe de fenetre Swing permettant de visualiser un
// document (HTML ou texte)
public class DocumentViewer extends JFrame  implements HyperlinkListener, ActionListener
{
    // Composant Swing permettant de visualiser un document
    
    JEditorPane viewer       = new JEditorPane ();
    
    // Champ de saisie de l'URL a visualiser
    JTextField  urlTextField = new JTextField ();
    
    public DocumentViewer (String titre) 
    {     
	setTitle(titre);
	// Construction de l'Interface Graphique
	// Panel en haut avec un label et le champ de saisie
	JPanel inputPanel = new JPanel (new BorderLayout ());
	JLabel label = new JLabel ("   Sources : ");    
	inputPanel.add (label, BorderLayout.WEST);
	inputPanel.add (urlTextField, BorderLayout.CENTER);
	// Zone scrollee au centre avec le document    
	JScrollPane scrollPane = new JScrollPane (viewer);
	// Ajout des composants a la fenetre
	getContentPane ().add (inputPanel, BorderLayout.NORTH);
	getContentPane ().add (scrollPane, BorderLayout.CENTER);
	
	// Mode non editable pour recevoir les clics sur les 
	// liens hypertexte
	viewer.setEditable (false);
	// Ajout du listener de clic sur lien
	viewer.addHyperlinkListener (this);
	// Ajout du listener de modification de la saisie
	urlTextField.addActionListener (this);
    }
    
    // Methode appelee apres un clic sur un lien hyper texte
    public void hyperlinkUpdate (HyperlinkEvent event) 
    {
	if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
	    // Modification du champ de saisie
	    urlTextField.setText (event.getURL ().toString ());
	    if (event instanceof HTMLFrameHyperlinkEvent) {
		// Evenement special en cas d'utilisation de Frame HTML
		HTMLDocument doc = (HTMLDocument)viewer.getDocument ();
		doc.processHTMLFrameHyperlinkEvent ((HTMLFrameHyperlinkEvent)event);
	    }
	    else
		// Chargement de la page
		loadPage (urlTextField.getText ());
	}
    }
    
    // Methode appelee apres une modification de la saisie
    public void actionPerformed (ActionEvent event) {
	loadPage (urlTextField.getText ());
    }
    
    public void loadPage (String urlText) {
	try {
	    // Modification du document visualise
	    viewer.setPage (new URL (urlText));
	} 
	catch (Exception ex) {
	    System.err.println ("Accès impossible à : " + urlText);
	    ex.printStackTrace();
	}
    }

    public void TextToHtml(String fileText, String fileHtml) {
	
    }

    // Methode main () d'exemple de mise en oeuvre.
    // 
    public static void main (String [] args) {
	DocumentViewer viewerFrame = new DocumentViewer ("Retour aux notices");
	viewerFrame.setSize (600, 800);
	viewerFrame.loadPage(args[0]);
	viewerFrame.urlTextField.setText (args[0]);
	viewerFrame.addWindowListener( new WindowAdapter() {
	    public void windowClosing(WindowEvent evt) {
		System.exit(0);
	    }
	});
    }
}
