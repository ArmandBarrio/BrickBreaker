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
    boolean touche = false;

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
    // habile Armand le deuxieme constructeur !
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

    boolean bounce (Brick O){
		
		if (touche){ 
		touche = false;
		return false;
		
		}else{
		
			if (x>= (O.x-l/2) && x<= (O.x + O.l -l/2) && y <=(O.y + O.h+(int)(O.h*0.4)) && y >= (O.y + O.h)){
				direction = (float)(2 *(Math.PI) - direction);
				System.out.println("Collision with BOTTOM OF BRICK" );
				O.lowerState();
				System.out.println("H="+O.h+ " L="+O.l);
				touche = true;
				return true;
			}
			if (x>= (O.x-l/2) && x<= (O.x + O.l -l/2) && y >=(O.y - h -(int)(O.h*0.4)) && y <=(O.y -h)){
				direction = (float)(2 *(Math.PI) - direction);
				System.out.println("Collision with TOP OF BRICK" );
				O.lowerState();
				touche = true;
				return true;
			}
			if (x >= (O.x - l - (int)(l*0.4)) && x<= (O.x - l) && y >=(O.y -h/2) && y <=(O.y + O.h -h/2)){
				direction = (float)(Math.PI - direction);
				System.out.println("Collision with LEFT OF BRICK" );
				O.lowerState();
				touche = true;
				return true;
			}
			if (x <= (O.x + O.l +(int)(l*0.4)) && x>= (O.x + O.l) && y >=(O.y - h/2) && y <=(O.y -h/2 + O.h)){
				direction = (float)(Math.PI - direction);
				System.out.println("Collision with RIGHT OF BRICK" );
				O.lowerState();
				touche = true;
				return true;
			}
		}
		return false;
	}
        boolean bounceOffPaddle(int ax, int ay, int length){
			if(y >= ay-h && y <= ay && x >= ax && x<=ax + length){
				//For no interaction between the paddle and the ball
				//direction = (float)(2 *(Math.PI) - direction);

				//more complexe ball orientation through paddle collision
				direction= (float) (270*Math.PI*2.0/360.0 - ((ax +length/2.0)-x)/(length/2.0) * 50.0*Math.PI*2.0/360.0);
				//System.out.println("Collision with PADDLE ");
				return true;
			}
			return false;
		}

		boolean bounceOffWalls(int screenW, int screenH){

			if (y <= h ){
				direction = (float)(2 *(Math.PI) - direction);
				return true;
			}
			if (x< 0){
				direction = (float)(Math.PI - direction);
				return true;
			}
			if (x > screenW - l ){
				direction = (float)(Math.PI - direction);
				return true;
			}
			return false;
		}

    public void setX(int ax){
        trueX = (double)(ax);
		x = ax;
	}

	public void setY( int ay){
		trueY = (double)(ay);
		y = ay;
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
