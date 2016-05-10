//import java.awt.Color;
//import java.awt.Dimension;
//import java.awt.Graphics;
//import java.awt.event.ActionListener;
//import java.util.ArrayList;
//
//import javax.swing.JFrame;
//import javax.swing.JPanel;
//
//
//public class transitivite extends JPanel {
//	
//	JFrame fenetre   = new JFrame("Transitivité");
//
//	public int max=0;
//	transitivite(){
//		fenetre.setSize(180, 650);
//		fenetre.setResizable(false);
//		fenetre.setBackground(Color.lightGray);
//	}
//
//
//	public synchronized void histoTransit(Graphics g,ArrayList<String> seuilVoisins){
//		for (int i = 0 ; i < seuilVoisins.size(); i ++){
//			ArrayList<String> maximum = new ArrayList<String>();
//			maximum.add(seuilVoisins.get(seuilVoisins.size()));
//			max=maximum.size();
//			ArrayList<String> listeVoisins = new ArrayList<String>();
//			listeVoisins.add(seuilVoisins.get(i));
//				g.fillRoundRect(i+1, i,5, listeVoisins.size()/max,1,1);
//		}		
//	}
//	public static void main(String[] argv) {
//		transitivite t = new transitivite();
//		ArrayList<String> test= new ArrayList<String>();
//		test.add("aa");
//		test.add("aa bbb");
//		test.add("aa ccc cccc");
//		Graphics g=new Graphics();
//		t.histoTransit(g,test);
//		}
//		
//	}
	
	

