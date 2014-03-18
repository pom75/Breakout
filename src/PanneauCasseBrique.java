
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.Timer;


public class PanneauCasseBrique extends JComponent {

	//Variable Jeux
	private int MeilleurScore;
	private int pointsJoueur = 0;
	private String pseudo = null;
	private int vies = 10;
	private Balle balle;
	private Balle balleF;
	private ZoneDeCantonnement ZDC;
	private Raquette raquette;
	private List<ElementDeJeu> paroisEtBriques = new LinkedList<ElementDeJeu>(); 
	private PanneauCasseBrique buff = null;

	//Variable Lecture Niveau
	private BufferedReader reader;
	private int Lbrique = 100;
	private int Hbrique = 30;
	private String ligne;
	private int FristRemove = 0; // Variable Qui définie dans le tableau paroisEtBriques l'index des parois (pour ne pas les remove lors des rebond)

	//Varibale Graphique
	private JPanel paneM = new JPanel(new GridLayout(6, 0));
	private JFrame frame = new JFrame("Casse Brique (By Stephane Ferreira)");
	private JLabel pseudoL = new JLabel("Joueur : "+pseudo);
	private JLabel niveauL = new JLabel("Niveau");
	private JLabel MeilleurScoreL = new JLabel("Meilleur score : "+MeilleurScore);
	private JLabel vieL = new JLabel("Nombre de vies : "+vies);
	private JLabel ptsActuel = new JLabel("Score : "+pointsJoueur);
	private JButton legende = new JButton("Regles et Legende Du jeux");
	private JButton pause = new JButton("Pause");
	private JMenu Jeu;
	private JMenu Niveaux;
	private JMenu SubLvl;
	private JMenuBar menuBar = new JMenuBar(); 
	private JMenuItem Newp = new JMenuItem("Nouvelle Partie");
	private JMenuItem Best = new JMenuItem("Meilleurs Scores");
	private JMenuItem quit = new JMenuItem("Quitter");
	private JMenuItem editLvl = new JMenuItem("Editeur de Niveau");
	private List<JMenuItem> It = new LinkedList<JMenuItem>(); 
	private int L = 1200;
	private int H = 1000;
	private Graphics2D g2;
	private int TF = 46; //23 pixel est la taille du bloque ou est marqué le nom de la fentre SUR MAC + 23 menu Déroulant !!!

	//Variables diverses
	public Timer t = new Timer(30, new Refresh());

	//Variables Partie 
	private List<String> listefichiers  = new LinkedList<String>(); // Nom de tous les niveaux
	private int partiActuel = 0;
	private String nomLvl;
	private Scores TabScores [] = new Scores[10];
	private int rep =0;

	//Constructeur
	public PanneauCasseBrique() throws IOException{

		//Graphic
		frame.setSize(L,H);
		frame.addWindowListener(new Close());
		frame.add(this);

		//Menu Droite
		paneM.setSize(200, 1000);
		paneM.add(MeilleurScoreL, new FlowLayout());
		pseudoL = new JLabel("Joueur : "+pseudo);
		paneM.add(niveauL, new FlowLayout());
		paneM.add(vieL, new FlowLayout());
		paneM.add(ptsActuel, new FlowLayout());
		paneM.add(legende,new FlowLayout());
		paneM.add(pause , new FlowLayout());
		//Ecouteurs
		pause.addMouseListener(new Pause());
		legende.addMouseListener(new Legend());
		MeilleurScoreL.addMouseListener( new BS());

		//Menu Déroulant
		Jeu = new JMenu("Jeu");
		Jeu.add(Newp); 
		Jeu.add(new JSeparator()); 
		Jeu.add(Best); 
		Jeu.add(new JSeparator()); 
		Jeu.add(quit); 
		menuBar.add(Jeu);
		Niveaux = new JMenu("Niveaux");
		SubLvl = new JMenu("Jouer un Niveau");
		this.listeReper("niveaux");//affichage des niveaux dans le sous menu + nouvelle partie vers ce niveau
		Niveaux.add(SubLvl); 
		Niveaux.add(new JSeparator()); 
		Niveaux.add(editLvl); 
		menuBar.add(Niveaux);
		//extra
		JMenu Pub = new JMenu("Pub");
		JMenuItem Site = new JMenuItem("Site");
		Pub.add(Site);
		menuBar.add(Pub);
		//Ecouteurs
		Site.addActionListener(new Pub());
		Newp.addActionListener(new NewLvl());
		Best.addActionListener(new BestScore());
		quit.addActionListener(new Ferme());
		editLvl.addActionListener(new EditLvl());
		lireScore();
		triScore();
		MeilleurScoreL.setText("Meilleurs score : "+TabScores[0].score+" pts par : "+TabScores[0].pseudo);


		//affichage Graphique frame
		frame.setJMenuBar(menuBar);
		frame.getContentPane().add(paneM, BorderLayout.EAST); 
		frame.setVisible(true);
		frame.addMouseListener(new Start());
	}

	//Fonctions Scores
	//lis les scores dans le fichié
	public void lireScore() throws IOException{
		String nomDuFichier = "scores/top.txt";
		try{
			reader = new BufferedReader( new FileReader(nomDuFichier));
		}catch(IOException e){
			System.out.println(e);
		}

		ligne = reader.readLine();
		while (ligne!=null)
		{	
			recupElem(ligne);
			ligne = reader.readLine();
		}

		reader.close();	
	}
	//tri les score par ordre décroissant
	public void triScore(){
		Scores s;
		int pos;

		for(int i =0 ;i < 10;i++){
			s = TabScores[i];
			pos = i;
			for(int j =i;j<10;j++){
				if(s.score<=TabScores[j].score){
					s= TabScores[j];
					pos = j;
				}
			}
			TabScores[pos]=TabScores[i];
			TabScores[i]=s;
		}

	}
	//ecris les scores dans le fichier
	public void ecrirScore() throws IOException{
		PrintWriter ecri ;
		try
		{
			ecri = new PrintWriter(new FileWriter("scores/top.txt"));
			String text="";
			for(int i =0 ;i < 10 ;i++){
				text += "SCORE "+TabScores[i].pseudo+" "+TabScores[i].score+"/\n"; 
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

	}
	//Ajoute le score de la perso
	public void ajouteScore() throws IOException{
		triScore();
		Scores s;
		if(pointsJoueur > TabScores[9].score ){

			String pseudo = JOptionPane.showInputDialog("Félicitation vous avez fait "+pointsJoueur+" pts ! entrer votre pseudo !");
			s = new Scores(pointsJoueur,pseudo);
			TabScores[9]=s;
			triScore(); 
			ecrirScore();
		}else{
			JOptionPane.showMessageDialog(frame,"Partie fini , vous n'est pas dans le Top 10 avec "+pointsJoueur);
		}
		//// =0
	}


	// Lecture des Niveaux
	public void lireNiveau(String niveau) throws IOException{
		String nomDuFichier = "niveaux/"+niveau;
		paroisEtBriques.clear();
		FristRemove=0;
		try{
			reader = new BufferedReader( new FileReader(nomDuFichier));
		}catch(IOException e){
			System.out.println(e);
		}

		ligne = reader.readLine();
		while (ligne!=null)
		{
			recupElem(ligne);
			ligne = reader.readLine();
		}
		niveauL.setText("Niveau "+partiActuel+" : "+nomLvl);
		frame.repaint();
		t.stop();
		reader.close();	
	}

	private void recupElem(String phrase){
		int i = 0;
		int j;
		String typeB="";
		String buff="";
		String X="";
		String Y="";
		String L="";
		String H="";
		String vitesseY="";
		String vitesseX="";
		String Pseudo="";
		String score="";


		if((j=testKey(phrase,"NIVEAU"))!=-1){
			i=1;
		}else if((j=testKey(phrase,"ZONE"))!=-1){
			i=2;
		}else if((j=testKey(phrase,"BALLE"))!=-1){
			i=3;
		}else if((j=testKey(phrase,"VIES"))!=-1){
			i=4;
		}else if((j=testKey(phrase,"RAQUETTE"))!=-1){
			i=5;
		}else if((j=testKey(phrase,"PAROI"))!=-1){
			i=6;
		}else if((j=testKey(phrase,"BRIQUE"))!=-1){
			i=7;
		}else if((j=testKey(phrase,"SCORE"))!=-1){
			i=8;
		}

		switch(i){
		case 0:break;
		case 1:
			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)!=' ' && phrase.charAt(t)!=':'  ){
					nomLvl=(String) phrase.subSequence(t, phrase.length()-1);
					break;

				}
			}
			break;
		case 2:

			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)>=48 && phrase.charAt(t)<=57){

					for(int p= t; p<phrase.length() ;p++){	
						if(phrase.charAt(p)>=48 && phrase.charAt(p)<=57){
							X+=""+phrase.charAt(p);
						}
						else{
							for(int v = p; v<phrase.length() ;v++){	
								if(phrase.charAt(v)>=48 && phrase.charAt(v)<=57){

									for(int k= v; k<phrase.length() ;k++){

										if(phrase.charAt(k)>=48 && phrase.charAt(k)<=57){
											Y+=""+phrase.charAt(k);
										}
										else{
											for(int h = k; h<phrase.length() ;h++){	
												if(phrase.charAt(h)>=48 && phrase.charAt(h)<=57){

													for(int r= h; r<phrase.length() ;r++){
														if(phrase.charAt(r)>=48 && phrase.charAt(r)<=57){
															L+=""+phrase.charAt(r);
														}
														else{
															for(int m = r; m<phrase.length() ;m++){	
																if(phrase.charAt(m)>=48 && phrase.charAt(m)<=57){
																	for(int a= m; a<phrase.length() ;a++){
																		if(phrase.charAt(a)>=48 && phrase.charAt(a)<=57){
																			H+=""+phrase.charAt(a);
																		}
																		else{
																			ZDC = new ZoneDeCantonnement(Integer.parseInt(X),Integer.parseInt(Y),Integer.parseInt(L),Integer.parseInt(H));
																			return;
																		}	
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			break;
		case 3:

			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)>=48 && phrase.charAt(t)<=57){

					for(int p= t; p<phrase.length() ;p++){	
						if(phrase.charAt(p)>=48 && phrase.charAt(p)<=57){
							X+=""+phrase.charAt(p);
						}
						else{
							for(int v = p; v<phrase.length() ;v++){	
								if(phrase.charAt(v)>=48 && phrase.charAt(v)<=57){

									for(int k= v; k<phrase.length() ;k++){

										if(phrase.charAt(k)>=48 && phrase.charAt(k)<=57){
											Y+=""+phrase.charAt(k);
										}
										else{
											for(int h = k; h<phrase.length() ;h++){	
												if(phrase.charAt(h)>=48 && phrase.charAt(h)<=57){

													for(int r= h; r<phrase.length() ;r++){
														if(phrase.charAt(r)>=48 && phrase.charAt(r)<=57){
															vitesseX+=""+phrase.charAt(r);
														}
														else{
															for(int m = r; m<phrase.length() ;m++){	
																if(phrase.charAt(m)>=48 && phrase.charAt(m)<=57){
																	for(int a= m; a<phrase.length() ;a++){
																		if(phrase.charAt(a)>=48 && phrase.charAt(a)<=57){
																			vitesseY+=""+phrase.charAt(a);
																		}
																		else{
																			balle = new Balle(Integer.parseInt(Y),Integer.parseInt(X),Integer.parseInt(vitesseX));
																			balleF= new Balle(balle);
																			return;
																		}

																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			break;
		case 4:
			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)>=48 && phrase.charAt(t)<=57){
					for(int p= t; p<phrase.length() ;p++){	
						if(phrase.charAt(p)>=48 && phrase.charAt(p)<=57){
							buff+=""+phrase.charAt(p);
						}
						else{
							vies = Integer.parseInt(buff);
							return;
						}
					}
				}
			}

			break;
		case 5:
			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)>=48 && phrase.charAt(t)<=57){

					for(int p= t; p<phrase.length() ;p++){	
						if(phrase.charAt(p)>=48 && phrase.charAt(p)<=57){
							L+=""+phrase.charAt(p);
						}
						else{
							for(int v = p; v<phrase.length() ;v++){	
								if(phrase.charAt(v)>=48 && phrase.charAt(v)<=57){

									for(int k= v; k<phrase.length() ;k++){

										if(phrase.charAt(k)>=48 && phrase.charAt(k)<=57){
											H+=""+phrase.charAt(k);
										}
										else{
											raquette = new Raquette(Integer.parseInt(L),Integer.parseInt(H));
											return;
										}
									}
								}
							}
						}
					}
				}
			}

			break;
		case 6:

			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)>=48 && phrase.charAt(t)<=57){

					for(int p= t; p<phrase.length() ;p++){	
						if(phrase.charAt(p)>=48 && phrase.charAt(p)<=57){
							X+=""+phrase.charAt(p);
						}
						else{
							for(int v = p; v<phrase.length() ;v++){	
								if(phrase.charAt(v)>=48 && phrase.charAt(v)<=57){

									for(int k= v; k<phrase.length() ;k++){

										if(phrase.charAt(k)>=48 && phrase.charAt(k)<=57){
											Y+=""+phrase.charAt(k);
										}
										else{
											for(int h = k; h<phrase.length() ;h++){	
												if(phrase.charAt(h)>=48 && phrase.charAt(h)<=57){

													for(int r= h; r<phrase.length() ;r++){
														if(phrase.charAt(r)>=48 && phrase.charAt(r)<=57){
															L+=""+phrase.charAt(r);
														}
														else{
															for(int m = r; m<phrase.length() ;m++){	
																if(phrase.charAt(m)>=48 && phrase.charAt(m)<=57){
																	for(int a= m; a<phrase.length() ;a++){
																		if(phrase.charAt(a)>=48 && phrase.charAt(a)<=57){
																			H+=""+phrase.charAt(a);
																		}
																		else{
																			((LinkedList<ElementDeJeu>) paroisEtBriques).addFirst(new Paroi(Integer.parseInt(X),Integer.parseInt(Y),Integer.parseInt(L),Integer.parseInt(H))) ;
																			FristRemove++;
																			return;
																		}

																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}

			break;
		case 7:
			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)>=48 && phrase.charAt(t)<=57){

					for(int k= t; k<phrase.length() ;k++){

						if(phrase.charAt(k)>=48 && phrase.charAt(k)<=57){
							typeB+=""+phrase.charAt(k);
						}
						else{
							for(int h = k; h<phrase.length() ;h++){	
								if(phrase.charAt(h)>=48 && phrase.charAt(h)<=57){

									for(int r= h; r<phrase.length() ;r++){
										if(phrase.charAt(r)>=48 && phrase.charAt(r)<=57){
											X+=""+phrase.charAt(r);
										}
										else{
											for(int m = r; m<phrase.length() ;m++){	
												if(phrase.charAt(m)>=48 && phrase.charAt(m)<=57){
													for(int a= m; a<phrase.length() ;a++){
														if(phrase.charAt(a)>=48 && phrase.charAt(a)<=57){
															Y+=""+phrase.charAt(a);
														}
														else{
															switch(Integer.parseInt(typeB)){
															case 0:
																((LinkedList<ElementDeJeu>) paroisEtBriques).addFirst(new Paroi(Integer.parseInt(X),Integer.parseInt(Y),Lbrique,Hbrique)) ;
																FristRemove++;
																return;
															case 1:
																paroisEtBriques.add(new BriqueNormal(Integer.parseInt(X),Integer.parseInt(Y),Lbrique,Hbrique)) ; 
																return;
															case 2 :
																paroisEtBriques.add(new BriqueBRaquette(Integer.parseInt(X),Integer.parseInt(Y),Lbrique,Hbrique)) ; 
																return;
															case 3 :
																paroisEtBriques.add(new BriqueMRaquette(Integer.parseInt(X),Integer.parseInt(Y),Lbrique,Hbrique)) ; 
																return;
															case 4 :
																paroisEtBriques.add(new BriqueMBalle(Integer.parseInt(X),Integer.parseInt(Y),Lbrique,Hbrique)) ; 
																return;
															case 5 :
																paroisEtBriques.add(new BriqueBBalle(Integer.parseInt(X),Integer.parseInt(Y),Lbrique,Hbrique)) ; 
																return;
															default:
																paroisEtBriques.add(new BriqueNormal(Integer.parseInt(X),Integer.parseInt(Y),Lbrique,Hbrique)) ; 
																return;
															}


														}

													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}




			break;
		case 8 :
			for(int t = j; t<phrase.length() ;t++){	
				if(phrase.charAt(t)!=' ' ){
					for(int p= t; p<phrase.length() ;p++){	
						if(phrase.charAt(p)!=' '){
							Pseudo+=""+phrase.charAt(p);
						}
						else{
							for(int v = p; v<phrase.length() ;v++){	
								if(phrase.charAt(v)>=48 && phrase.charAt(v)<=57){
									for(int k= v; k<phrase.length() ;k++){

										if(phrase.charAt(k)>=48 && phrase.charAt(k)<=57){
											score+=""+phrase.charAt(k);
										}
										else{
											TabScores[rep] = new Scores(Integer.parseInt(score),Pseudo);
											rep++;
											return;

										}
									}
								}
							}
						}
					}
				}
			}
			break;
		}
		i=0;
	}

	public static int testKey(String phrase,String mot){

		String ligne = phrase;
		String mots = mot;
		int val= -1;

		for(int i = 0; i<ligne.length() ; i++){
			if(ligne.charAt(i)==mots.charAt(0)){
				for(int j = 1; j<=mots.length();j++){
					i++;
					if(j==mots.length()){
						return i+1;
					}else if(ligne.charAt(i)!=mots.charAt(j)){
						break;
					}
				}
			}
		}


		return val;
	}





	// Lecteur Répertoir Menu Niveaux
	public void listeReper(String chemin){ 

		File repertoire = new File(chemin); 
		int i; 
		int j =0; 
		AllezA allezA = new AllezA();
		String [] listefichier;
		listefichier=repertoire.list();


		for(i=0;i<listefichier.length;i++){ 
			if(listefichier[i].endsWith(".rtf") || listefichier[i].endsWith(".txt")){ 
				this.listefichiers.add(listefichier[i]);
				It.add(j, new JMenuItem(listefichier[i])); 
				It.get(j).addActionListener(allezA);
				SubLvl.add(It.get(j));
				j++;
			}
		}
	}


	//Affichage graphique
	public void paintComponent(Graphics g){
		refreshLvl(g);
		ZDC.affiche((Graphics2D) g,  getWidth(), getHeight());
		DeplaceRaquette();
		DeplaceBalle(g);
		raquette.rebondHV(balle);
		raquette.affiche((Graphics2D) g, getWidth(), getHeight());

		//Rebond sur briques/parois  + remove Brique
		for(int i =0;i<paroisEtBriques.size();i++){
			if(((ElementARebond) paroisEtBriques.get(i)).rebondHV(balle) && i>=FristRemove){
				((Brique) paroisEtBriques.get(i)).onRebond(balle,raquette);
				paroisEtBriques.remove(i);
				pointsJoueur+=10;
				ptsActuel.setText("Score : "+pointsJoueur);

			}
		}

	}

	public void refreshLvl(Graphics g){
		for(int i=0;i<paroisEtBriques.size();i++){

			paroisEtBriques.get(i).affiche((Graphics2D) g, getWidth(), getHeight());
		}
	}

	public void DeplaceRaquette(){
		Point p = java.awt.MouseInfo.getPointerInfo().getLocation();
		int Xr = (p.x-frame.getX());
		int Yr = (p.y-frame.getY()-TF);


		if( Yr>((ZDC.getY()*getHeight())/1000) && Yr<( (ZDC.getY()*getHeight())/1000+(ZDC.getH()*getHeight())/1000 )){
			raquette.setY((((p.y-frame.getY()-TF)*1000)/getHeight())-raquette.getH()/2);
		}
		if( Xr>((ZDC.getX()*(getWidth()))/1000) && Xr<((ZDC.getX()*(getWidth()))/1000+(ZDC.getL()*(getWidth()))/1000) ){
			raquette.setX((((p.x-frame.getX())*1000)/(getWidth()))-raquette.getL()/2);			
		}
	}

	public void DeplaceBalle(Graphics g){

		balle.affiche((Graphics2D) g, getWidth(), getHeight(),t.isRunning()); // elpeche la déplacement de la balle lors du Resize 

		//Gestion vie perdu
		if(balle.getY()>1000 || balle.getX()>1000 || balle.getX()<0){
			vies-=1;
			vieL.setText("Nombre de vies : "+vies);
			balle = new Balle(balleF);
			frame.repaint();
			t.stop();
		}
	}


	//Refresh de l'image + Gestion NIveau
	private class Refresh implements ActionListener{
		public void actionPerformed(ActionEvent event)
		{	
			frame.repaint();	
			//Niveau fini

			if(vies <= 0 && FristRemove != 1 || partiActuel+1 == listefichiers.size() && (paroisEtBriques.size() <= FristRemove && FristRemove != 0)){
				try {
					ajouteScore();
					t.stop();
					balle = null;
					FristRemove =1;

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			if(paroisEtBriques.size() <= FristRemove && FristRemove != 0){
				partiActuel++;
				try {
					System.out.println(paroisEtBriques.size());
					System.out.println(FristRemove);
					paroisEtBriques.clear();
					lireNiveau(listefichiers.get(partiActuel));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}



		}

	}

	// Fermeture de la fentre
	private class Close extends WindowAdapter { 
		public void windowClosing(WindowEvent event) {
			System.exit(0); 
		}
	}

	// Mousses Listener
	private class Pause extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			if(t.isRunning()){
				t.stop();
			}else{
				t.start();
			}
		}
	}
	private class Legend extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			t.stop();
			JOptionPane.showMessageDialog(frame,"Légendes : \n Le but du jeu est de casser toutes les briques cassaable du niveaux .\n Pour commencé une parti allez dans le menu et faite nouvelle partie  et cliquer sur l'écran pour lancer la balle .\n Chaque brique a des bonus ou des malus qui sont déclanché lorsque celle-ci se casse \n les briques grises sont nomales \n les briques rouges agrandisseent la raquette \n les briques la rapetisse \n les briques jaunes accelere la vitesse de la balle \n les brique rose diminue la vitesse de la balle \n Les Brique noires sont incassable \n Bon jeu ! ");

		}
	}
	private class BS extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			t.stop();
			String text="Top 10 Scores : \n";
			for(int i =0;i<10;i++){
				text+=(i+1)+" "+TabScores[i].pseudo+" "+TabScores[i].score+"\n";
			}
			JOptionPane.showMessageDialog(frame,""+text);
		}

	}

	class Start extends MouseAdapter{

		public void mouseClicked(MouseEvent event){ 
			if(! t.isRunning()){
				t.start();
			}
		}
	}
	class AllezA implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				//On recupere le numéro du LVl
				for(int i =0;i<It.size();i++){
					if(((JMenuItem)event.getSource()).getText() == listefichiers.get(i)){
						partiActuel = i;
						pointsJoueur = 0;
						vies = 10;
						vieL.setText("Nombre de vies : "+vies);
						ptsActuel.setText("Score : "+pointsJoueur);
					}
				}
				lireNiveau(((JMenuItem)event.getSource()).getText());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	private class Pub implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				Desktop d = Desktop.getDesktop();
				d.browse(new URI("http://SooZig.com"));

			} catch ( URISyntaxException e ) {
				e.printStackTrace();

			} catch ( IOException e ) {
				e.printStackTrace();
			}
		}
	}
	private class Ferme implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			System.exit(0);
		}
	}
	private class NewLvl implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			try {
				paroisEtBriques.clear();
				partiActuel = 0;
				lireNiveau(listefichiers.get(0));
				t.stop();
				pointsJoueur = 0;
				vies = 10;
				vieL.setText("Nombre de vies : "+vies);
				ptsActuel.setText("Score : "+pointsJoueur);
				frame.repaint();
			} catch (IOException e) {

				e.printStackTrace();
			}
		}
	}
	private class BestScore implements ActionListener {
		public void actionPerformed(ActionEvent event) {
			String text="Top 10 Scores : \n";
			for(int i =0;i<10;i++){
				text+=(i+1)+" "+TabScores[i].pseudo+" "+TabScores[i].score+"\n";
			}
			JOptionPane.showMessageDialog(frame,""+text);
		}
	}
	private class EditLvl implements ActionListener {


		public void actionPerformed(ActionEvent event) {
			EditLvL lvl = new EditLvL();
		}
	}


}

