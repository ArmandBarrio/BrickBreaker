import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.swing.text.Position;

public class  Object {
    // Attributs 
    int x,y;                
    int h,l;                
    double trueX, trueY;
    float direction;        
    float vitesse;          
    BufferedImage image;    
    Rectangle BoxObject;
    boolean actif;          
   
    // Constructeur
    public Object(String NomImage, int ax, int ay, float ad, float av)    {
         try {
             image= ImageIO.read(new File(NomImage));
             } 
         catch(Exception err) 
             {
            System.out.println(NomImage+" introuvable !");            
            System.exit(0);    
            }
        
        
        h= image.getHeight(null);   
        l= image.getWidth(null);        
        x=ax;
        trueX = ax;   
        y=ay;
        trueY = ay;
        BoxObject = new Rectangle(x,y,l,h);
        direction=ad;
        vitesse=av; 
        actif=true;
        
    }
    public Object( int ax, int ay, float ad, float av)    {      
        x=ax;
        trueX = ax;   
        y=ay;
        trueY = ay;
        direction=ad;
        vitesse=av; 
        actif=true;
    }
    
    boolean Collision(Object O) {
        return BoxObject.intersects(O.BoxObject); 
    }
    
    void bounce (Object O){			
		// this only works if the dimension of the objects are greater than speed...
		if (this.Collision(O)){
			if (this.x > O.x && this.x < ( O.x = O.l) && this.y > O.y && this.y < ( O.y = O.h)){
				if (this.direction< Math.PI*2 ){
					this.direction  = (float)(2*Math.PI-this.direction);
					
				}
				if ( this.direction>3*Math.PI/2){
					this.direction = (float)(3*Math.PI- this.direction);
				}
			}
				// then for the most difficult configuration, define the last position of the "ball" and define the collision point
				//to clearly know on which side the ball bounces
				
			if( this.x < O.x && this.y > O.y){		//the "ball" arrives on the left side
				this.direction  = (float)(2*Math.PI-this.direction);
			}
			if ( this.x >O.x && this.y < O.y){		// the "ball" arrives on the upper side
				this.direction  =(float)( Math.PI-this.direction);
			}
		}
		if (this.Collision(O)){
			direction = (float)(direction +2 *(Math.PI - direction));
		}
	}
        void bounceOffPaddle(Object O){
			
		}
    
    public void move(Rectangle Ecran) {
        trueX = trueX + vitesse*Math.cos(direction); 
        trueY = trueY + vitesse*Math.sin(direction);
        x = (int)(trueX);
        y = (int)(trueY);
        
        BoxObject.setLocation(x,y);
        
        if (( x + l<0)||( y + h<0)||( x > Ecran.width) ||( y > Ecran.height)) actif=false;
    }
    
}
