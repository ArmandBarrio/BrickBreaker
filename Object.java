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
    
    void bounce (Brick O){				
		
		/*Armand : To try to fix this problem, I created the nextX and nextY functions
		 * Did not work that well
		
		int ax = this.nextX();
		int ay = this.nextY();
		
		if (ax>= O.x && ax<= (O.x + O.l -l) && ay <=(O.y + O.h) && ay >(O.y + O.h/2)){ 
			direction = (float)(2 *(Math.PI) - direction);
			System.out.println("Collision with BOTTOM OF BRICK" );
		}
		if (ax>= O.x && ax<= (O.x + O.l -l) && ay >=(O.y - h) && ay <(O.y -h + O.h/2)){ 
			direction = (float)(2 *(Math.PI) - direction);
			System.out.println("Collision with TOP OF BRICK" );
		}
		if (ax>= O.x && ax<= (O.x + O.l/2) && ay >=(O.y -h) && ay <(O.y -h + O.h)){ 
			direction = (float)(Math.PI - direction);
			System.out.println("Collision with LEFT OF BRICK" );
		}
		if (ax <= (O.x + O.l) && ax>= (O.x + O.l/2) && ay >=(O.y - h) && ay <(O.y -h + O.h/2)){ 
			direction = (float)(Math.PI - direction);
			System.out.println("Collision with RIGHT OF BRICK" );
		}
		*/
		
		if (x>= O.x && x<= (O.x + O.l -l) && y <=(O.y + O.h) && y >(O.y + O.h/5)){ 
			direction = (float)(2 *(Math.PI) - direction);
			System.out.println("Collision with BOTTOM OF BRICK" );
			O.lowerState();
		}
		if (x>= O.x && x<= (O.x + O.l -l) && y >=(O.y - h) && y <(O.y -h + O.h/5)){ 
			direction = (float)(2 *(Math.PI) - direction);
			System.out.println("Collision with TOP OF BRICK" );
			O.lowerState();
		}
		if (x >= O.x && x<= (O.x + O.l/5 - l) && y >=(O.y -h) && y <(O.y -h + O.h)){ 
			direction = (float)(Math.PI - direction);
			System.out.println("Collision with LEFT OF BRICK" );
			O.lowerState();
		}
		if (x <= (O.x + O.l) && x>= (O.x + O.l/5) && y >=(O.y - h) && y <(O.y -h + O.h)){ 
			direction = (float)(Math.PI - direction);
			System.out.println("Collision with RIGHT OF BRICK" );
			O.lowerState();
		}
	}
        void bounceOffPaddle(int ax, int ay, int length){
			if(y >= ay-h && x >= ax && x<=ax + length){
				//For no interaction between the paddle and the ball
				//direction = (float)(2 *(Math.PI) - direction);	
				
				//more complexe ball orientation through paddle collision								
				direction= (float) (270*Math.PI*2.0/360.0 - ((ax +length/2.0)-x)/(length/2.0) * 50.0*Math.PI*2.0/360.0);
				//System.out.println("Collision with PADDLE ");
			}

		}       
		
		void bounceOffWalls(int screenW, int screenH){
						
			if (y <= h ){ 
				direction = (float)(2 *(Math.PI) - direction);
				//System.out.println("Collision with TOP wall" );
			}
			if (x< 0){ 
				direction = (float)(Math.PI - direction);
				//System.out.println("Collision with LEFT wall" );
			}
			if (x > screenW - l ){ 
				direction = (float)(Math.PI - direction);
				//System.out.println("Collision with RIGHT wall" );
			}
			//This will have to dissapear
			if (y >=  screenH - l ){
				direction = (float)(2 *(Math.PI) - direction);
				//System.out.println("Collision with BOTTOM wall" );
			}
		}
		
	/* NextX and nextY were both created in order to deal with the problem
	 * of the ball going through the object too quickly for the program
	 * to respond
	 */
	 	
    public int nextX(){
        int ax = (int)(trueX + vitesse*Math.cos(direction));
        return ax;
	}		
	
	public int nextY(){
		int ay = (int)(trueY + vitesse*Math.sin(direction));
		return ay;
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
