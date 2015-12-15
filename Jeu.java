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

public class Jeu extends JFrame implements ActionListener{
	
	//Add Timer
	public int TempsTimer_ms = 100;
	public Timer Montimer;
	public long Temps;
    
    //Add the Object array
    public Brick lesBriques[][]= new Brick[12][3];

	//Game Animation
	public int NbVies = 3;
	public BufferedImage ArrierePlan;
	public Graphics buffer;
	
	//Key booleans
	public boolean ToucheDroite;
	public boolean ToucheGauche;
	public boolean ToucheEspace;	
	public boolean ToucheEnter;
	
	//Wallpaper image and Rectangle
	public Rectangle Ecran;
	public Image Wallpaper;
	public Image startScreenWallpaper;
	public Image paddle;
    public Image brick;
	
	//Screen Dimension
	public int screenWidth;
	public int screenHeight;
	
	//Start Screen
	boolean startScreen = false;
    
    //Objets
    public Brick brique;
    public Brick brique1;
    public Brick brique2;
    public Brick brique3;
    
    
    public Object Ball;
	
	//font
	Font font;
	
	//KeyListener
	//this.addKeyListener(new Jeu_this_keyAdapter(this));
	
	public static void main(String[] args){
		Jeu Game = new Jeu();
	}
	
	
	public Jeu(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

		screenWidth = (int)(screenSize.getWidth());
		screenHeight = (int)(screenSize.getHeight());

		int screenWidth = (int)(screenSize.getWidth());
		int screenHeight = (int)(screenSize.getHeight());
        //brique = new Brick ( 100, 100,"brick.jpg",0);
		
		// temporary fix
		brique = new Brick ( 100, 100,"Paddle.png",0);
		
        
        // Pour tester les briques
        for (int i = 0; i < lesBriques.length; i++){
			for (int j = 0 ; j< lesBriques[0].length; j++){
				double r = Math.random();
				String randomType = "Normal";
				int randomState = (int)(Math.random()*3 +1);
				if (r <0.2)  randomType = "Unbreakable";
				if ( r > 0.7) randomType = "Normal";
				lesBriques[i][j] = new Brick ( 10 + i * 70, 50 + j * 34, randomType, randomState );
			}
		}
				
        /*brique = new Brick ( 100, 100,"Unbreakable",-1);
        brique1 = new Brick ( 200, 200,"Normal",1);
        brique2 = new Brick ( 200, 300,"Normal",2);
        brique3= new Brick ( 200, 400,"Normal",3);
        lesBriques[0]=brique;
        lesBriques[1]=brique1;
        lesBriques[2]=brique2;
        lesBriques[3]=brique3;
        */
        
        Ball = new Object("Ball.png", 400,400, 0,0);


		
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

		//Started by Enter Key Montimer.start();
		Montimer.start();
		
		Ball.move(Ecran);
		
        //Call the method Ball.bounce(object) for the walls and the paddle
        
        // tests if there are collisions. CAREFUL : this should be among the last instructions since it will change the direction
        for (int i = 0; i < lesBriques.length; i++){
			for (int j = 0 ; j < lesBriques[0].length;  j++){
				Ball.bounce(lesBriques[i][j]);		// if the ball collides with one of them, it changes direction
				if (lesBriques[i][j].Collision(Ball) && lesBriques[i][j].state !=0){
					lesBriques[i][j].state= lesBriques[i][j].state-1;
				}
            System.out.println( lesBriques[i][j].state);
			}
        }
		
		
		//Buffer and all
		ArrierePlan = new BufferedImage(Ecran.width,Ecran.height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		
		//play music (doesn't work yet)
		music();
			
			
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
			
			long s = Temps/(long)(10);
			this.setTitle("Time : " + String.valueOf(s) + "   |  Lives "+ String.valueOf(NbVies));
			Temps++;
			
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
			
			
	}
				
	public void gestionBricks(){
			
			
	}
	
	public void gestionBall(){
		
		
	}
				
	public void paint(Graphics g){
		
		
		if(startScreen == true){
			buffer.drawImage(startScreenWallpaper,0,0,this);
			//font.size = 200;
			buffer.setFont(font); 
			buffer.setColor(Color.white);
			buffer.drawString("New Game?",100,(int)(screenHeight*0.3));
			
			buffer.setFont(new Font("Dialog", Font.PLAIN, (int)(screenHeight*0.17)));
			buffer.setColor(Color.white);
			buffer.drawString("Yes",(int)(screenWidth*0.4),(int)(screenHeight*0.6));
			
			buffer.setFont(new Font("Dialog", Font.PLAIN, (int)(screenHeight*0.17)));
			buffer.setColor(Color.white);
			buffer.drawString("Exit",(int)(screenWidth*0.4),(int)(screenHeight*0.8));
			
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
		}
			
		g.drawImage(ArrierePlan,0,0,this);
		
		
	}
}
