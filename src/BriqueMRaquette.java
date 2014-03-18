import java.awt.Color;


public class BriqueMRaquette extends Brique {
	private static Color color = Color.blue;
	public BriqueMRaquette(int x, int y, int l, int h) {
		super(x, y, l, h,color);
		// TODO Auto-generated constructor stub
	}
	
	public int getType(){
		return 3;
	}

	@Override
	public void onRebond(Balle B,Raquette R) {
		R.setL(R.getL()/2);

	}

}
