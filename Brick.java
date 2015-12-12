/*
 * The brick's type defines its behaviour in case of collision.
 * It's state is the number off collisions left before destruction. If 0, the brick is inactive.
 * If -1, the brick will never reach -1, it is Unbreakable.

*/
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class  Brick extends Object {
    public int state;
    public Brick(int ax, int ay, String Type, int s){
        super( ax, ay, 0,0);
        this.state = s;
        if (Type == "Unbreakable"){
			try {
             image= ImageIO.read(new File("Unbreakable.png"));
             } 
         catch(Exception err) 
             {
            System.out.println("Unbreakable.png"+" introuvable !");            
            System.exit(0);    
            }
		}
			
		if (Type == "Normal" && state > 0){
			try {
             image= ImageIO.read(new File("Brick"+state+".jpg"));
             } 
         catch(Exception err) 
             {
            System.out.println("Brick"+state+".jpg"+" introuvable !");            
            System.exit(0);    
            }
		}
		if (state == 0){ 
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

