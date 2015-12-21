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


public class Jeu extends JFrame implements ActionListener,KeyListener,MouseMotionListener{
		
	//Add Timer
	public int TempsTimer_ms = 1;
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
	public Image IntroImage;
	public Image Wallpaper;
	public Image GameOverImage;
	public Image startScreenWallpaper;
	public Image paddle;
    public Image brick;
	
	//Screen Dimension
	public int screenWidth;
	public int screenHeight;
	
	//paddle size
	public int paddleWidth = 200;
	public int paddleHeight = 30;
	
	//Start Screen and Game Over
	public boolean intro = true;
	public boolean startScreen = false;
	public boolean play = false;
    public boolean gameOver = false;	
	public boolean arrowUp = true;
 
    
    //Pause the ball before sending it
    public boolean newBall = true;
    public int countdown = -600;
    
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
	
	//Font management
	Font font;
	Font font2;
	
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
				if (r < 0.2)  randomType = "Unbreakable";
				if ( r > 0.7) randomType = "Normal";
				lesBriques[i][j] = new Brick ( 100 + i * 70, 200+j * 34, randomType, randomState );
			}
		}
        // Pour créer les murs
        leftWall = new Object ( "VerticalWall.png" , 10000,10, 0,0);
        rightWall = new Object ( "VerticalWall.png" , 100000+lesBriques.length * 70,10, 0,0);
        upperWall = new Object ( "HorizontalWall.png" , 100000,10, 0,0);
        
<<<<<<< HEAD
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

=======
        // Create the Paddle and Ball
        Paddle = new Object ( "Paddle.png", 400,(int)(screenHeight*0.9),10,10/TempsTimer_ms);        
        Ball = new Object("Ball.png", (int)(screenWidth*0.2),(int)(screenHeight*0.5),(float) (Math.random()*60*Math.PI*2.0/360.0 + 30*Math.PI*2.0/360.0),6/TempsTimer_ms);
>>>>>>> 90aaeffbcc0f3c1e857c503d70b0a87381bfde84

		
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
		GameOverImage = T.getImage("GameOver.jpg");
		IntroImage = T.getImage("IntroImage.gif");
		
		//ActionListener
		Montimer = new Timer(TempsTimer_ms,this);	
		Montimer.start();
		
        //Buffer and all
		ArrierePlan = new BufferedImage(Ecran.width,Ecran.height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();

		//KeyListener and MouseMotionListener
		addKeyListener(this);
		addMouseMotionListener(this);
		
		//Start Intro Music
		music("IntroMusic.wav");
			
		// Font
		 try{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("COMPUTER.TTF"));
			buffer.setFont(font);
        } catch(Exception ex){
            System.out.println("Fonte COMPUTER.TTF non trouvée !");
        }     
        
        font2 = font.deriveFont(1,200);
		buffer.setFont(font2); 
		buffer.setColor(Color.white);
		
		this.setVisible(true);
		repaint();
	}
		
	public void actionPerformed(ActionEvent e){
			
		s = Temps/(long)(100);
		Temps++;
			
		if(intro){
			this.setTitle("BRICKBREAKER || INSA EDITION");
			countdown++;
			if (countdown > 300){
				intro = false;
				startScreen = true;
				countdown = 0;
			}
		}
			
		if (startScreen){ 
			startScreenAction();
			this.setTitle("BRICKBREAKER || INSA EDITION");
		}
			
		if (play){
			this.setTitle("Time : " + String.valueOf(s-gameStartTime) + "   |  Lives "+ String.valueOf(NbVies));
			gestionPaddle();
			if (!newBall){
				gestionBall();
			}else{
				countdown++;
			}	
			if (countdown > 200){
				newBall = false;
				countdown = 0;
			}
		}
		
		if(gameOver){
			this.setTitle("BRICKBREAKER || INSA EDITION");
			countdown++;
			if (countdown > 300){
				gameOver = false;
				startScreen = true;
				startScreenAction();
				countdown = 0;
			}
		}
	
		repaint();
		
	}
	
	public void startScreenAction(){
				
		if (arrowUp && toucheEnter){
			startScreen = false;
			play = true;
			music("PlayMusic.wav");
			NbVies = 3;
			gameStartTime = s;
			countdown = -360;
		}else if (!arrowUp && toucheEnter){
			System.exit(0);
		}
	}
	
	public void music(String s){
		
		try{
			AudioInputStream audioInputStream =
			AudioSystem.getAudioInputStream(
            this.getClass().getResource(s));
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		}catch(Exception ex){
			System.out.println("No Music found");
		}
	}
		
	public void gestionPaddle(){
		
		if (toucheDroite==true && Paddle.x < screenWidth-paddleWidth){
			Paddle.x=Paddle.x+(int)(Paddle.vitesse); 
		}
		if (toucheGauche==true && Paddle.x > 0){
			Paddle.x=Paddle.x-(int)(Paddle.vitesse);
		} 
	}
	
	public void gestionBall(){
		
		Ball.move(Ecran);
        
        for (int i = 0; i < lesBriques.length; i++){
			for (int j = 0 ; j < lesBriques[0].length;  j++){
                if( lesBriques[i][j].state != 0){
                    if (Ball.bounce(lesBriques[i][j])){
						music("CollisionMusic.wav");
					}
                }
            }
        }
        
        if (Ball.bounceOffPaddle(Paddle.x, Paddle.y, paddleWidth)){
			music("PaddleBounceMusic.wav");
		}
        
        if (Ball.bounceOffWalls(screenWidth, screenHeight)){
			music("WallBouncenMusic.wav");
		}
        
        if (Ball.y > screenHeight + 100){
			NbVies--;
			Ball.setX((int)(screenWidth*0.2));
			Ball.setY((int)(screenHeight*0.5));
			newBall = true;
			Ball.direction = (float) (Math.random()*60*Math.PI*2.0/360.0 + 30*Math.PI*2.0/360.0);
			if (NbVies == 0) {
				play = false;
				gameOver = true;
				music("GameOverMusic.wav");
			}
		}
		
	}
		
	public void keyTyped(KeyEvent e) { }
         
    public void keyPressed(KeyEvent e){
		

        if(e.getKeyCode()==KeyEvent.VK_LEFT){
            toucheGauche=true;
            //System.out.println("Left pressed");
        }
        if(e.getKeyCode()==KeyEvent.VK_RIGHT){
            toucheDroite=true;
            //System.out.println("Right pressed");
        }
        if(e.getKeyCode()==KeyEvent.VK_UP){
            toucheHaut=true;
            //System.out.println("Up pressed");
            arrowUp = !arrowUp;
        }
        if(e.getKeyCode()==KeyEvent.VK_DOWN){
            toucheBas=true;
            //System.out.println("Down pressed");
            arrowUp = !arrowUp;
		}
        if(e.getKeyCode()==KeyEvent.VK_SPACE){
            toucheEspace=true;
            //System.out.println("Space pressed");
        }
        if(e.getKeyCode()==KeyEvent.VK_ENTER){
            toucheEnter = true;            
            //System.out.println("Enter pressed");
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

		@Override
	public void mouseMoved(MouseEvent e) {
		if ( e.getX() > paddleWidth/2 && e.getX() < screenWidth - paddleWidth/2){
			Paddle.x = e.getX() - paddleWidth/2;
		}
	}
		@Override
	public void mouseDragged(MouseEvent e) {
      throw new UnsupportedOperationException("Not supported yet.");
	}
	
	public void paint(Graphics g){
		
		if (intro){
			buffer.drawImage(IntroImage,0,0,screenWidth,screenHeight,this);
		}
		
		if(startScreen){
			buffer.drawImage(startScreenWallpaper,0,0,this);
			font2 = font.deriveFont(1,(int)(screenHeight*0.25));
			buffer.setFont(font2); 
			buffer.setColor(Color.white);
 
			buffer.drawString("New Game?",(int)(screenWidth * 0.1),(int)(screenHeight*0.3));		
			buffer.drawString("Yes",(int)(screenWidth*0.4),(int)(screenHeight*0.6));;
			buffer.drawString("Exit",(int)(screenWidth*0.4),(int)(screenHeight*0.8));
			
			if( arrowUp == true){
				buffer.drawString(">",(int)(screenWidth*0.4 - 100),(int)(screenHeight*0.6));
			}else{
				buffer.drawString(">",(int)(screenWidth*0.4 - 100),(int)(screenHeight*0.8));
			}
		}
			
		if (play){
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
            buffer.drawImage(Paddle.image, Paddle.x, Paddle.y, paddleWidth, paddleHeight, this);
		} 
		
		if (gameOver){
			buffer.drawImage(GameOverImage,0,0,screenWidth, screenHeight, this);
		}
			
		g.drawImage(ArrierePlan,0,0,this);		
	}
}
