import java.awt.Color;
import java.awt.Graphics2D;


public class Balle extends ElementDeJeu {
	private int rayon; // rayon de la balle
	private int Y;// position de la balle Y
	private int X;// position de la balle X
	private int H = 15;// hauteur de la balle
	private int L = 15;//Largeur de la balle
	private int angle; // angle de la balle
	private int vitesse;// vitesse de la balle
	private Color color = Color.red; //couleur de la balle
	private boolean collH = false;// Si la balle est en collision coté Haut
	private boolean collB = false;// Si la balle est en collision coté Bas
	private boolean collD = false;// Si la balle est en collision coté Haut
	private boolean collG = false;// Si la balle est en collision coté Haut


	public Balle (int rayon,int positionY,int positionX,int angle,int vitesse,Color color){
		this.rayon = rayon;
		this.Y = positionY;
		this.X = positionX;
		this.angle = angle;
		this.setVitesse(vitesse);
		this.setColor(color);
	}
	public Balle (int positionY,int positionX,int vitesse){
		this(7,positionY,positionX,65,vitesse,Color.red);
	}
	public Balle (Balle b){
		this(b.rayon,b.Y,b.X,b.angle,b.vitesse,b.color);
	}
	public int getRayon(){
		return rayon;
	}
	
	public int getX() {
		return X;
	}
	public void setX(int x) {
		X = x;
	}
	public int getY() {
		return Y;
	}
	public void setY(int y) {
		Y = y;
	}
	public int getH() {
		return H;
	}
	public void setH(int h) {
		H = h;
	}
	public int getL() {
		return L;
	}
	public void setL(int l) {
		L = l;
	}
	public Color getColor() {
		return color;
	}
	public void setColor(Color color) {
		this.color = color;
	}

	public int getAngle(){
		return angle;
	}

	public void setRayon(int rayon){
		this.rayon = rayon;
	}

	
	public void setAngle(int A){
		this.angle = A;
	}

	public int getVitesse() {
		return vitesse;
	}

	public void setVitesse(int vitesse) {
		this.vitesse = vitesse;
	}


	void affiche(Graphics2D g, int largeur_reelle, int hauteur_reelle,boolean t) {
		Color c = this.getColor();
		g.setColor(c);
		g.fillOval((getX()*largeur_reelle)/1000,(getY()*hauteur_reelle)/1000,(getL()*largeur_reelle)/1000,(getH()*hauteur_reelle)/1000);
		g.setColor(Color.black);
		g.drawOval((getX()*largeur_reelle)/1000,(getY()*hauteur_reelle)/1000,(getL()*largeur_reelle)/1000,(getH()*hauteur_reelle)/1000);
		if(t){
			int temp = (int) ((Math.PI*angle)/180);
			X = (int) (X + vitesse * Math.cos(temp));
			Y = (int) (Y + vitesse * Math.sin(temp));
		}
		
	}
	public boolean isCollH() {
		return collH;
	}
	public void setCollH(boolean collH) {
		this.collH = collH;
	}
	public boolean isCollB() {
		return collB;
	}
	public void setCollB(boolean collB) {
		this.collB = collB;
	}
	public boolean isCollD() {
		return collD;
	}
	public void setCollD(boolean collD) {
		this.collD = collD;
	}
	public boolean isCollG() {
		return collG;
	}
	public void setCollG(boolean collG) {
		this.collG = collG;
	}
	@Override
	void affiche(Graphics2D g, int largeur_reelle, int hauteur_reelle) {
		// TODO Auto-generated method stub	
	}



}

