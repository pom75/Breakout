import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class ElementARebond  extends ElementDeJeu{

	private Graphics2D g2;

	void affiche(Graphics2D g, int largeur_reelle, int hauteur_reelle) {
		g2 = (Graphics2D) g;
		g2.setColor(getColor());
		Rectangle square = new Rectangle((getX()*largeur_reelle)/1000,(getY()*hauteur_reelle)/1000,(getL()*largeur_reelle)/1000,(getH()*hauteur_reelle)/1000);
		g2.fill(square);
		g2.setColor(Color.black);
		Rectangle squar = new Rectangle((getX()*largeur_reelle)/1000,(getY()*hauteur_reelle)/1000,(getL()*largeur_reelle)/1000,(getH()*hauteur_reelle)/1000);
		g2.draw(squar);

	}

	//Intersection Entre un cercle de centre Cx CY et de rayon R et un segment de CoordonŽ Ax Ay ....
	public boolean interCS(float Cx,float Cy,float R,float Ax, float Ay,float Bx,float By){
		float alpha = ( Bx - Ax )* ( Bx - Ax ) + ( By - Ay ) * ( By - Ay );
		float beta = 2 * ( ( Bx - Ax ) *  ( Ax - Cx ) +( By - Ay ) *  ( Ay - Cy ));
		float gama = Ax * Ax + Ay * Ay + Cx * Cx + Cy * Cy - 2 * (Ax * Cx + Ay * Cy) - R * R;

		if( (beta *beta - 4 * alpha * gama)>=0){
			float buff = ((Cx - Ax)*(Bx-Ax)+(Cy-Ay)*(By-Ay))/(( Bx - Ax )* ( Bx - Ax ) + ( By - Ay ) * ( By - Ay ));	
			return ( 0.0 <= buff && buff <= 1.0);

		}else{
			return false;
		}	
	}

	

	//Calcul du rebond Horizontal et Vertical
	//Test si il y a une collision accutelemnt entre la balle et l'element 
	//Empeche l'accumulation de rebond avec la meme parois 
	public boolean rebondHV(Balle b){
		int centreBX = b.getX()+ b.getL()/2;
		int centreBY = b.getY()+ b.getH()/2;


		if( interCS(centreBX,centreBY,b.getRayon(),this.getX(),this.getY(),this.getX()+this.getL(),this.getY()) ){
			if(b.isCollH()){

			}else{
				b.setAngle(360-b.getAngle());
				b.setCollH(true);
				b.setCollD(false);
				b.setCollG(false);
				b.setCollB(false);
				return true;
			}
		}
		if(interCS(centreBX,centreBY,b.getRayon(),this.getX(),this.getY()+this.getH(),this.getX()+this.getL(),this.getY()+this.getH()) ){
			if(b.isCollB()){

			}else{
				b.setAngle(360-b.getAngle());
				b.setCollB(true);
				b.setCollD(false);
				b.setCollG(false);
				b.setCollH(false);
				
				return true;
			}
		}
		if (interCS(centreBX,centreBY,b.getRayon(),this.getX(),this.getY(),this.getX(),this.getY()+this.getH()) ){
			if(b.isCollG()){

			}else{
				b.setAngle(180-b.getAngle());
				b.setCollG(true);
				b.setCollD(false);
				b.setCollB(false);
				b.setCollH(false);
				
				return true;
			}
		}
		if(interCS(centreBX,centreBY,b.getRayon(),this.getX()+this.getL(),this.getY(),this.getX()+this.getL(),this.getY()+this.getH()) ){
			if(b.isCollD()){

			}else{
				b.setAngle(180-b.getAngle());
				b.setCollD(true);
				b.setCollG(false);
				b.setCollB(false);
				b.setCollH(false);
				
				return true;
			}
		}
		return false;
	}

	public int getType() {
		
		return 0;
	}
	
	



}
