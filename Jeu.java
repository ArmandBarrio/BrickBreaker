import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.awt.geom.*;
import java.awt.image.*;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

// for Music
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

//Listeners
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

public class Jeu extends JFrame implements ActionListener,KeyListener{
	
	//Add Timer
	public int TempsTimer_ms = 10;
	public Timer Montimer;
	public long Temps;
	public long s;
	public long gameStartTime;
    
    //Add the Object array
    public Brick lesBriques[][]= new Brick[12][3];

	//Game Animation
	public int NbVies = 3;
	public BufferedImage ArrierePlan;
	public Graphics buffer;
	
	//Key booleans
	public boolean toucheDroite;
	public boolean toucheGauche;
	public boolean toucheEspace;	
	public boolean toucheEnter;
	public boolean toucheEchap;
	public boolean toucheHaut;
	public boolean toucheBas;
	
	//Wallpaper image and Rectangle
	public Rectangle Ecran;
	public Image Wallpaper;
	public Image startScreenWallpaper;
	public Image paddle;
    public Image brick;
	
	//Screen Dimension
	public int screenWidth;
	public int screenHeight;
	
	//paddle size
	public int paddleWidth = 100;
	public int paddleHeight = 30;
	
	//Start Screen
	boolean startScreen = false;
    boolean arrowUp = true;
    
    //Objets
    public Brick brique;
    public Brick brique1;
    public Brick brique2;
    public Brick brique3;
    public Object upperWall;
    public Object leftWall;
    public Object rightWall;
    public Object Paddle;
    
    
    public Object Ball;
	
	//font
	Font font;
	
	public static void main(String[] args){
		Jeu Game = new Jeu();
	}
	
	public Jeu(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		screenWidth = (int)(screenSize.getWidth());
		screenHeight = (int)(screenSize.getHeight());

		int screenWidth = (int)(screenSize.getWidth());
		int screenHeight = (int)(screenSize.getHeight());
		
		
        
        // Pour tester les briques, initialisation
        for (int i = 0; i < lesBriques.length; i++){
			for (int j = 0 ; j< lesBriques[0].length; j++){
				double r = Math.random();
				String randomType = "Normal";
				int randomState = (int)(Math.random()*3 +1);
				if (r <0.2)  randomType = "Unbreakable";
				if ( r > 0.7) randomType = "Normal";
				lesBriques[i][j] = new Brick ( 100 + i * 70, 200+j * 34, randomType, randomState );
			}
		}
        // Pour créer les murs
        leftWall = new Object ( "VerticalWall.png" , 10000,10, 0,0);
        rightWall = new Object ( "VerticalWall.png" , 100000+lesBriques.length * 70,10, 0,0);
        upperWall = new Object ( "HorizontalWall.png" , 100000,10, 0,0);
        
        // Create the Paddle
        Paddle = new Object ( "Paddle.png", (int)(screenWidth*0.3),(int)(screenHeight*0.9),10,10);
        
        
				
        /*brique = new Brick ( 100, 100,"Unbreakable",-1);
        brique1 = new Brick ( 200, 200,"Normal",1);
        brique2 = new Brick ( 200, 300,"Normal",2);
        brique3= new Brick ( 200, 400,"Normal",3);
        lesBriques[0]=brique;
        lesBriques[1]=brique1;
        lesBriques[2]=brique2;
        lesBriques[3]=brique3;
        */
        
        Ball = new Object("Ball.png", (int)(screenWidth*0.2),(int)(screenHeight*0.5),(float) (285*Math.PI*2.0/360.0),10);


		
		//Make Window appear		
		this.setTitle("Brick Breaker");
		this.setLayout(null);
		this.setBounds(0,0,screenWidth,screenHeight);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Ecran = new Rectangle(0,0,getSize().width,getSize().height);
		
		//Initialiser rapidement les images sans contrôler leur existence
		Toolkit T=Toolkit.getDefaultToolkit();
		Wallpaper = T.getImage("wallpaper.jpg");
		startScreenWallpaper = T.getImage("StartScreen.jpg");
		paddle = T.getImage("Paddle.png");
		
		//ActionListener
		Montimer = new Timer(TempsTimer_ms,this);	
		Montimer.start();

		//Started by Enter Key Montimer.start();
		Montimer.start();
		
        //Buffer and all
		ArrierePlan = new BufferedImage(Ecran.width,Ecran.height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		
		//play music (doesn't work yet)
		music();
		

        
        
		//KeyListener
		addKeyListener(this);
			
			
		// Font
		 try{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("COMPUTER.TTF"));
			buffer.setFont(font);
        } catch(Exception ex){
            System.out.println("Fonte COMPUTER.TTF non trouvée !");
        } 
        
		
		this.setVisible(true);
		repaint();
	}
		
	public void actionPerformed(ActionEvent e){
			
			s = Temps/(long)(100);
			if (!startScreen){
				this.setTitle("Time : " + String.valueOf(s-gameStartTime) + "   |  Lives "+ String.valueOf(NbVies));
			}
			Temps++;
			
			startScreenAction();
            
            gestionBall();
            gestionPaddle();
            gestionBricks();
			
			if(Temps%50 == 0){
				//System.out.println(Paddle.x + "   " + Paddle.y + "  | " + Ball.x + "   " + Ball.y +  "     " +  richtung + "  |  " + Ball.direction);
			}
			
			
			repaint();
			
	}
	
	public void startScreenAction(){
		if (startScreen && arrowUp && toucheEnter){
			startScreen = false;
			gameStartTime = s;
		}
		if (startScreen && !arrowUp && toucheEnter){
			System.exit(0);
		}
	}
	
	public void music(){
		
		try{
			AudioInputStream audioInputStream =
			AudioSystem.getAudioInputStream(
            this.getClass().getResource("Music.mp3"));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}catch(Exception ex){
			System.out.println("No Music found");
		}
	}
		
	public void gestionPaddle(){
		
		if (toucheDroite==true){
			Paddle.x=Paddle.x+(int)(Paddle.vitesse); 
		}
		if (toucheGauche==true){
			Paddle.x=Paddle.x-(int)(Paddle.vitesse);
		} 
	}
				
	public void gestionBricks(){
        // tests if there are collisions. 
        for (int i = 0; i < lesBriques.length; i++){
			for (int j = 0 ; j < lesBriques[0].length;  j++){
				if (lesBriques[i][j].Collision(Ball) && lesBriques[i][j].state !=0){
					lesBriques[i][j].state= lesBriques[i][j].state-1;
				}
            //System.out.println( lesBriques[i][j].state);
			}
        }
			
			
	}
	
	public void gestionBall(){
        Ball.move(Ecran);
        for (int i = 0; i < lesBriques.length; i++){
			for (int j = 0 ; j < lesBriques[0].length;  j++){
                if( lesBriques[i][j].state != 0){
                    Ball.bounce(lesBriques[i][j]);
                }
            }
        }
        Ball.bounce(upperWall);
        Ball.bounce(leftWall);
        Ball.bounce(rightWall);
        Ball.bounce(Paddle);
                
        
		
	}
		
	public void keyTyped(KeyEvent e) { }
         
    public void keyPressed(KeyEvent e){
		

        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            toucheGauche=true;
            System.out.println("Left pressed");
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            toucheDroite=true;
            System.out.println("Right pressed");
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            toucheHaut=true;
            System.out.println("Up pressed");
            arrowUp = !arrowUp;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            toucheBas=true;
            System.out.println("Down pressed");
            arrowUp = !arrowUp;
		}
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            toucheEspace=true;
            System.out.println("Space pressed");
        }
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            toucheEnter = true;            
            System.out.println("Enter pressed");
        }
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
            toucheEchap=true;
            System.exit(0);
        }
        
       /* if(e.getKeyCode()==KeyEvent.VK_1){
            choice = 0;
        }
        if(e.getKeyCode()==KeyEvent.VK_2){
            choice = 1;
        }
        if(e.getKeyCode()==KeyEvent.VK_3){
            choice = 2;
        }
        if(e.getKeyCode()==KeyEvent.VK_4){
            choice = 3;
        }
        if(e.getKeyCode()==KeyEvent.VK_5){
            choice = 4;
        }
        if(e.getKeyCode()==KeyEvent.VK_6){
            choice = 5;
        }        
        */
    }
     
    public void keyReleased(KeyEvent e){

        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            toucheGauche=false;
        }
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            toucheEnter=false;
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            toucheDroite=false;
        }
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            toucheEspace=false;
        }
        if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
            toucheEchap=false;
        }
        //useful?
    }

	public void paint(Graphics g){
		
		if(startScreen == true){
			buffer.drawImage(startScreenWallpaper,0,0,this);
			//font.size = 200;
			//buffer.setFont(font); 
			buffer.setFont(new Font("Dialog", Font.PLAIN, (int)(screenHeight*0.17)));
			buffer.setColor(Color.white);
			buffer.drawString("New Game?",300,(int)(screenHeight*0.3));
			
			buffer.setFont(new Font("Dialog", Font.PLAIN, (int)(screenHeight*0.17)));
			buffer.setColor(Color.white);
			buffer.drawString("Yes",(int)(screenWidth*0.4),(int)(screenHeight*0.6));
		
			buffer.setFont(new Font("Dialog", Font.PLAIN, (int)(screenHeight*0.17)));
			buffer.setColor(Color.white);
			buffer.drawString("Exit",(int)(screenWidth*0.4),(int)(screenHeight*0.8));
			
			if( arrowUp == true){
				buffer.drawString(">",(int)(screenWidth*0.4 - 100),(int)(screenHeight*0.6));
			}else{
				buffer.drawString(">",(int)(screenWidth*0.4 - 100),(int)(screenHeight*0.8));
			}
			
		}else{
            
			buffer.drawImage(Wallpaper,0,0,this);
			// afficher toutes les briques actives
            for ( int i = 0; i< lesBriques.length; i++){
				for (int j = 0 ; j < lesBriques[0].length;  j++){
					
					if (lesBriques[i][j].state != 0){					
						buffer.drawImage(lesBriques[i][j].image, lesBriques[i][j].x,lesBriques[i][j].y,this);
					}
                    
				}
			}
            buffer.drawImage(Ball.image, Ball.x,Ball.y,this);

            buffer.drawImage(leftWall.image, leftWall.x,leftWall.y,this);
            buffer.drawImage(rightWall.image, rightWall.x,rightWall.y,this);
            buffer.drawImage(upperWall.image, upperWall.x,upperWall.y,this);
            buffer.drawImage(Paddle.image, Paddle.x,Paddle.y,this);

           buffer.drawImage(paddle,Paddle.x,(int)(screenHeight*0.9),paddleWidth,paddleHeight,this);	

		}
			
		g.drawImage(ArrierePlan,0,0,this);
		
		
	}
}
