import java.awt.Color;
import java.awt.Graphics2D;


public abstract class ElementDeJeu {
	protected int X;
	protected int Y;
	protected int H;
	protected int L;
	protected Color color = null;
	
	
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
	
	abstract void affiche(Graphics2D g, int largeur_reelle, int hauteur_reelle);
	


}
