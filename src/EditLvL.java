import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;



public class EditLvL extends JComponent {
	//Varibale Graphique
		private JPanel paneM = new JPanel(new GridLayout(7, 0));
		private JFrame frame = new JFrame("Casse Brique (By Stephane Ferreira)");
		private JButton enregistre = new JButton("Enregistré le Niveau");
		private JButton nomLvl = new JButton("Nom Du Niveau");
		private JButton ZDCC = new JButton("Zone De Cantonnement");
		private JButton nbVie = new JButton("Nombre de Vie");
		private JButton tailleRaquette = new JButton("Taille de la raquette");
		private JButton ajouteB = new JButton("Ajouter une Brique");
		private JButton suprimeB = new JButton("Suprimer la derniere Brique");
		private int L = 1200;
		private int H = 1000;
		private Graphics2D g2;
		private Timer t = new Timer(30, new Refresh());
		private int sec =1;
		private String buff;
		
		//Variable a enregistré
		private int nombreVie = 10;
		private ZoneDeCantonnement ZDC=new ZoneDeCantonnement(60,700,900,100);
		private Raquette raquette=new Raquette(100,20);
		private List<ElementDeJeu> paroisEtBriques = new LinkedList<ElementDeJeu>(); 
		private String NomNiveau="";
		
	public EditLvL(){
				//Graphic
				frame.setSize(L,H);
				frame.add(this);

				//Menu Droite
				paneM.setSize(200, 1000);
				paneM.add(nomLvl , new FlowLayout());
				paneM.add(ZDCC , new FlowLayout());
				paneM.add(nbVie,new FlowLayout());
				paneM.add(tailleRaquette,new FlowLayout());
				paneM.add(ajouteB,new FlowLayout());
				paneM.add(suprimeB,new FlowLayout());
				paneM.add(enregistre,new FlowLayout());
				//Ecouteurs
				nomLvl.addMouseListener(new NomLvl());
				enregistre.addMouseListener(new Sauvegarde());
				ZDCC.addMouseListener(new ZDC());
				nbVie.addMouseListener(new NbVie());
				tailleRaquette.addMouseListener(new TailleR());
				ajouteB.addMouseListener(new AjoutB());
				suprimeB.addMouseListener(new SupB());
				
				paroisEtBriques.add(new Paroi(0,0,10,1000));
				paroisEtBriques.add(new Paroi(990,0,10,1000));
				paroisEtBriques.add(new Paroi(0,0,1000,10));
				
				frame.getContentPane().add(paneM, BorderLayout.EAST); 
				frame.setVisible(true);
				frame.repaint();
				t.start();
	}
	
	public boolean NomNiveauPresent(String chemin,String s){ 

		File repertoire = new File(chemin); 
		int i; 
		int j =0; 
		String [] listefichiers;
		listefichiers=repertoire.list();


		for(i=0;i<listefichiers.length;i++){ 
			if(listefichiers[i].endsWith(".rtf") || listefichiers[i].endsWith(".txt")){ 
				if(s.equalsIgnoreCase(listefichiers[i])){
					return true;
					
				}
			}
		}
		return false;
	}
	
	public void paintComponent(Graphics g){
		refreshLvl(g);
		ZDC.affiche((Graphics2D) g,   getWidth(), getHeight());
		raquette.affiche((Graphics2D) g, getWidth(), getHeight());


	}
	private class Refresh implements ActionListener{
		public void actionPerformed(ActionEvent event)
		{	
			frame.repaint();	
		}

	}
	public void refreshLvl(Graphics g){
		for(int i=0;i<paroisEtBriques.size();i++){

			paroisEtBriques.get(i).affiche((Graphics2D) g, getWidth(), getHeight());
		}
	}
	private class NomLvl extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 

				buff = JOptionPane.showInputDialog("Enter le nom du niveau");
				try{	
					
					NomNiveau  = buff; 
					if(NomNiveauPresent("niveaux",NomNiveau+".txt")){
						JOptionPane.showMessageDialog(frame,"Attention , se nom de niveau est déja présent , si vous ne changer pas de nom , le fichier déja présent sera suprimé");
					}
					if(NomNiveauPresent("niveaux",NomNiveau+".rtf")){
						JOptionPane.showMessageDialog(frame,"Attention , se nom de niveau est déja présent , si vous ne changer pas de nom , le fichier déja présent sera suprimé");
					}
					}catch(NumberFormatException e){
					}
			}
	}
	private class Sauvegarde extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			PrintWriter ecri ;
			try
			{
				ecri = new PrintWriter(new FileWriter("niveaux/"+NomNiveau+".txt"));
				String text="";
				text+="NIVEAU: "+NomNiveau+"/\n";
				text+="BALLE: 500 500 5 5 /\n";
				text+="VIES: "+nombreVie+"/\n";
				text+="ZONE: "+ZDC.getX()+" "+ZDC.getY()+" "+ZDC.getL()+" "+ZDC.getH()+"/\n";
				text+="RAQUETTE: "+raquette.getL()+" "+raquette.getH()+"/\n";
				text+="PAROI: 0 0 10 1000/\n";
				text+="PAROI: 990 0 10 1000/\n";
				text+="PAROI: 0 0 1000 10/\n";
				
				
				for(int i =3 ;i < paroisEtBriques.size() ;i++){
					text += "BRIQUE "+((ElementARebond) paroisEtBriques.get(i)).getType()+" "+paroisEtBriques.get(i).getX()+" "+paroisEtBriques.get(i).getY()+"/\n"; 
				}
				
				ecri.print(text);


				ecri.flush();
				ecri.close();
			}//try
			catch (NullPointerException a)
			{
				System.out.println("Erreur : pointeur null");
			}
			catch (IOException a)
			{
				System.out.println("Problème d'IO");
			}
			JOptionPane.showMessageDialog(frame,"Niveau enregistré!");
		}

	}

	private class ZDC extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			int x=0;
			int y=0;
			int l=0;
			int h=0;
			
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la position X de la zone de Cantonement  (Coin supérieur droit)");
				try{
					x  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la position Y de la zone de Cantonement  (Coin supérieur droit)");
				try{
					y  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la largeur de la Zone de Cantonnement");
				try{
					l  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la hauteur de la Zone de Cantonnement");
				try{
					h  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			ZDC = new ZoneDeCantonnement(x,y,l,h);

		}
	}
	private class NbVie extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré le nombre de vie pour le Niveau");
				try{
					nombreVie  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
		}
	}
	private class TailleR extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			int l=0;
			int h=0;
			
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la largeur de la raquette");
				try{
					l  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la hauteur de la raquette");
				try{
					h  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			raquette = new Raquette(l,h);

		}
	}
	
	private class SupB extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			if(paroisEtBriques.size()>3 ){
				paroisEtBriques.remove(paroisEtBriques.size()-1);
			}

		}
	}
	
	private class AjoutB extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			int type=0;
			int X=0;
			int Y=0;
			
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré le type de la brique ( voir resgle du jeux pour les différents types)");
				try{
					type  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la position X de la brique ( un multiple de 100 , de 0 a 900)");
				try{
					X  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			
			while(sec==1){
				buff = JOptionPane.showInputDialog("Entré la position Y de la brique ( un multiple de 30 , de 0 a 450)");
				try{
					Y  = Integer.parseInt(buff); 
					sec=0;
					}catch(NumberFormatException e){
						sec = 1;
					}
			}
			sec=1;
			
			switch(type){
			case 0:
				((LinkedList<ElementDeJeu>) paroisEtBriques).add(new Paroi(X,Y,100,30)) ;
				return;
			case 1:
				paroisEtBriques.add(new BriqueNormal(X,Y,100,30)) ; 
				return;
			case 2 :
				paroisEtBriques.add(new BriqueBRaquette(X,Y,100,30)) ;  
				return;
			case 3 :
				paroisEtBriques.add(new BriqueMRaquette(X,Y,100,30)) ; 
				return;
			case 4 :
				paroisEtBriques.add(new BriqueMBalle(X,Y,100,30)) ;  
				return;
			case 5 :
				paroisEtBriques.add(new BriqueBBalle(X,Y,100,30)) ; 
				return;
			default:
				paroisEtBriques.add(new BriqueNormal(X,Y,100,30)) ; 
				return;
			}
		}
	}
}
