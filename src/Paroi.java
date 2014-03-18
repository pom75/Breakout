import java.awt.Color;


public class Paroi extends ElementARebond {
	private int X;// coin supérieur gauche
	private int Y;// coin supéreur droit
	private int L;// largeur de la parois
	private int H;// hateur de la parois
	private Color color = Color.black;
	
	public Paroi(int x,int y ,int l ,int h){
		this.X=x;
		this.Y=y;
		this.L=l;
		this.H=h;
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
}
