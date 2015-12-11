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

public class Jeu extends JFrame implements ActionListener{
	
	//Add Timer
	public int TempsTimer_ms = 100;
	public Timer Montimer;
	public long Temps;
    
    //Add the Object array
    public Brick lesBriques[]= new Brick[2];

	//Game Animation
	public int NbVies = 3;
	public BufferedImage ArrierePlan;
	public Graphics buffer;
	
	//Key booleans
	public boolean ToucheDroite;
	public boolean ToucheGauche;
	public boolean ToucheEspace;	
	
	//Wallpaper image and Rectangle
	public Rectangle Ecran;
	public Image Wallpaper;
	public Image startScreenWallpaper;
	public Image paddle;
    public Image brick;
	
	//Start Screen
	boolean startScreen = false;
    
    //Objets
    public Brick brique;
    public Brick brique1;
    
    public Object Ball;
	
	public static void main(String[] args){
		Jeu Game = new Jeu();
	}
	
	public Jeu(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int screenWidth = (int)(screenSize.getWidth());
		int screenHeight = (int)(screenSize.getHeight());
        brique = new Brick ( 100, 100,"brick.jpg",0);
        brique1 = new Brick ( 200, 200,"brick.jpg",0);
        lesBriques[0]=brique;
        lesBriques[1]=brique1;
        Ball = new Object("Paddle.png", 100,100, 0,0);
        
		
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
        

		
		//paddle = new Objet("navire.png", (int)(Ecran.width/2),(int)(Ecran.height/2),0,0);
		
		//ActionListener
		Montimer = new Timer(TempsTimer_ms,this);	
		Montimer.start();
        
        // tests if there are collisions
        for (int i = 0; i < lesBriques.length; i++){
            if (lesBriques[i].Collision(Ball)){
                lesBriques[i].state= lesBriques[i].state-1;
            }
        }
        System.out.println( brique.state);
		
		
		//Buffer and all
		ArrierePlan = new BufferedImage(Ecran.width,Ecran.height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		
		this.setVisible(true);
		repaint();
	}
		
	public void actionPerformed(ActionEvent e){
			
			long s = Temps/(long)(10);
			this.setTitle("Time : " + String.valueOf(s) + "   |  Lives "+ String.valueOf(NbVies));
			Temps++;
			
	}
		
	public void gestionPaddle(){
			
			
	}
				
	public void gestionBricks(){
			
			
	}
				
	public void paint(Graphics g){
		
		
		if(startScreen == true){
			buffer.drawImage(startScreenWallpaper,0,0,this);
			buffer.setFont(new Font("TimesRoman", Font.PLAIN, 300));
			buffer.setColor(Color.blue);
			buffer.drawString("New Game?",100,500);
			g.drawImage(paddle,0,0,this);
		}else{
            
			buffer.drawImage(Wallpaper,0,0,this);
            for ( int i = 0; i< lesBriques.length; i++){
                buffer.drawImage(lesBriques[i].image, lesBriques[i].x,lesBriques[i].y,this);
            }
            buffer.drawImage(Ball.image, Ball.x,Ball.y,this);
		}
			
		g.drawImage(ArrierePlan,0,0,this);
		
		
	}
}
