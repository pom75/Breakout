import java.awt.Color;

// Notez aussi qu'on a plusieurs sortes de briques, il serait donc pertinent que Brique soit une classe abstraite, et d'avoir un sous-classe pour chaque type de brique différente.
abstract class Brique extends ElementARebond {
	private int X;// coin supérieur gauche
	private int Y;// coin supéreur droit
	private int L;// largeur de la brique
	private int H;// hateur de la brique
	private Color color = Color.green;
	
	public Brique(int x,int y ,int l ,int h){
		this.X=x;
		this.Y=y;
		this.L=l;
		this.H=h;
	}
	
	public Brique(int x,int y ,int l ,int h, Color c){
		this.X=x;
		this.Y=y;
		this.L=l;
		this.H=h;
		this.color = c; 
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
	
	public int getType(){
		return 0;
	}
	public abstract void onRebond(Balle B,Raquette R);
}

