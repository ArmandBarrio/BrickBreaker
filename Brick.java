/*
 * The brick's type defines its behaviour in case of collision.
 * It's state is the number off collisions left before destruction. If 0, the brick is inactive.
 * If -1, the brick will never reach -1, it is Unbreakable.

*/
import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;

public class  Brick extends Object {

    public int state;
    public String Type;

    void lowerState(){

		state--;

		if (state > 0){
			try {
				image= ImageIO.read(new File("Brick"+state+".png"));
			}catch(Exception err){
				System.out.println("Brick"+state+".png"+" introuvable !");
			}
		}
	}

    public Brick(int ax, int ay, String T, int s){

        super( ax, ay, 0,0);
        this.state = s;
        Type=T;


        if (Type == "Unbreakable"){
			try {
             image= ImageIO.read(new File("Unbreakable.png"));
             }
         catch(Exception err)
             {
            System.out.println("Unbreakable.png"+" introuvable !");
            System.exit(0);
            }
            this.state = -1;
		}
		if (Type == "PowerUp"){
				try {
             image= ImageIO.read(new File("BrickPowerUp.png"));
             }
         catch(Exception err)
             {
            System.out.println("BrickPowerUp.png"+" introuvable !");
            System.exit(0);
            }
            this.state = 1;
		}
		if (Type == "Normal" && state > 0){
			try {
             image= ImageIO.read(new File("Brick"+state+".png"));
             }
         catch(Exception err)
             {
            System.out.println("Brick"+state+".png"+" introuvable !");
            System.exit(0);
            }
		}

		/* Guillaume, I didn't quite get the point of this, it seems to work fine without it
		 * It works now because of the second constructor without an image for objects  G.
		 * It is actually quite practical. I'm using it to initialize invisible bricks to fill-up my array without consequences
		 */
		  if(state == 0){
			// this is a random image that is not displayed
			try {
             image= ImageIO.read(new File("Paddle.png"));
             }
         catch(Exception err)
             {
            System.out.println("Paddle.jpg"+" introuvable !");
            System.exit(0);
            }
		}

		h= image.getHeight(null);
        l= image.getWidth(null);
        BoxObject = new Rectangle(x,y,l,h);
    }
}
