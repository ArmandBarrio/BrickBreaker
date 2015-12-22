import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;

public class PowerUp extends Object{
	String Type;
	boolean active;
	
	
	public PowerUp(String T, int ax, int ay){
		super( ax, ay, (float)(Math.PI/2), (float)5); 
		Type=T;
		try {
             image= ImageIO.read(new File(Type+".png"));
             } 
         catch(Exception err) 
             {
            System.out.println(Type+".png"+" introuvable !");            
            System.exit(0);    
            }
            h= image.getHeight(null);   
			l= image.getWidth(null); 
			BoxObject = new Rectangle(x,y,l,h);	
			active = true;
		
	}	
	
		
	
}
