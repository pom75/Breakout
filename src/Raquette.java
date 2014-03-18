import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;


public class Raquette extends ElementARebond {
	private int X;// position X coin superior goche
	private int Y;// position Y de la raquete coin supérieur goche
	private int H;// hauteur de la raquette
	private int L;//Largeur de la raquette
	private Color color = Color.black;//couleur de la raquette
	private Graphics2D g2;
	public Raquette (int X,int Y, int L, int H,Color color){
		this.setX(X);
		this.setY(Y);
		this.setL(L);
		this.setH(H);
		this.setColor(color);
	}
	public Raquette(int X,int Y, int L, int H){
		this(X,Y,H,L,Color.black);
		
	}
	public Raquette(int L, int H){
		this(500,700,H,L);
		
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
