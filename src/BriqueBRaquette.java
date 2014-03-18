import java.awt.Color;


public class BriqueBRaquette extends Brique {
	private static Color color = Color.red;
	public BriqueBRaquette(int x, int y, int l, int h) {
		super(x, y, l, h,color);
	}
	public int getType(){
		return 2;
	}
	@Override
	public void onRebond(Balle B,Raquette R) {
		R.setL(R.getL()*2);

	}

}
