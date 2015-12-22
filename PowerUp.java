import java.awt.Rectangle;
import java.io.File;
import javax.imageio.ImageIO;

public class PowerUp extends Object{
	String Type;
	
	
	public PowerUp(String T, int ax, int ay){
		super( ax, ay, (float)(Math.PI*3/2), (float)10); 
		Type=T;
		try {
             image= ImageIO.read(new File(Type+".png"));
             } 
         catch(Exception err) 
             {
            System.out.println(Type+".png"+" introuvable !");            
            System.exit(0);    
            }
		
	}	
	
		
	
}
