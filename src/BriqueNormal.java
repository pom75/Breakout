import java.awt.Color;


public class BriqueNormal extends Brique {
	private static Color color = Color.gray;
	public BriqueNormal(int x, int y, int l, int h) {
		super(x, y, l, h,color);
	}

	public int getType(){
		return 1;
	}

	public void onRebond(Balle B,Raquette R) {

	}

}
