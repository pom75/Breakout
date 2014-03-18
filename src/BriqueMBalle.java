import java.awt.Color;


public class BriqueMBalle extends Brique {
	private static Color color = Color.pink;
	public BriqueMBalle(int x, int y, int l, int h) {
		super(x, y, l, h,color);
		// TODO Auto-generated constructor stub
	}

	public int getType(){
		return 4;
	}
	
	@Override
	public void onRebond(Balle B,Raquette R) {
		B.setVitesse(B.getVitesse()/2);

	}

}
