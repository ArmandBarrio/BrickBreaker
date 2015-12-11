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


public class Jeu extends JFrame implements ActionListener{
	
	//Add Timer
	public int TempsTimer_ms = 100;
	public Timer Montimer;
	public long Temps;

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
	
	//Screen Dimension
	int screenWidth;
	int screenHeight;
	
	//Start Screen
	boolean startScreen = true;
	
	//font
	Font font;
	
	public static void main(String[] args){
		Jeu Game = new Jeu();
	}
	
	public Jeu(){
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenWidth = (int)(screenSize.getWidth());
		screenHeight = (int)(screenSize.getHeight());
		
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
		
		
		//Buffer and all
		ArrierePlan = new BufferedImage(Ecran.width,Ecran.height,BufferedImage.TYPE_INT_RGB);
		buffer = ArrierePlan.getGraphics();
		
		//play music (doesn't work yet)
		music();
			
			
		// Font
		 try{
			font = Font.createFont(Font.TRUETYPE_FONT, new File("COMPUTER.TTF"));
			buffer.setFont(font.deriveFont(40.0f));
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
			buffer.setFont(font); // StyleConstants.setFontSize(fontSize, 25)
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
		}
			
		g.drawImage(ArrierePlan,0,0,this);
		
		
	}
}
