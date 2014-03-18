import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.RoundRectangle2D;


public class ZoneDeCantonnement extends ElementDeJeu {
	private int X;// coin supérieur gauche
	private int Y;// coin supéreur droit
	private int L;// largeur 
	private int H;// hateur 
	
	public ZoneDeCantonnement(int x,int y ,int l ,int h){
		this.X=x;
		this.Y=y;
		this.L=l;
		this.H=h;
	}


	@Override
	void affiche(Graphics2D g, int largeur_reelle, int hauteur_reelle) {
		Graphics2D g2 = (Graphics2D) g;
		final float dash1[] = {10.0f};
	    final BasicStroke dashed =
	        new BasicStroke(1.0f,
	                        BasicStroke.CAP_BUTT,
	                        BasicStroke.JOIN_MITER,
	                        10.0f, dash1, 0.0f);
	g2.setStroke(dashed);
	g2.draw(new RoundRectangle2D.Double((getX()*largeur_reelle)/1000,(getY()*hauteur_reelle)/1000,(getL()*largeur_reelle)/1000,(getH()*hauteur_reelle)/1000,
	                                   0, 0));
		
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
