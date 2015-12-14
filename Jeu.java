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
	int screenWidth;
	int screenHeight;
	
	//Start Screen
	boolean startScreen = true;
    
    //Objets
    public Brick brique;
	
	//font
	Font font;
	
	//KeyListener
	//this.addKeyListener(new Jeu_this_keyAdapter(this));
	
	public static void main(String[] args){
		Jeu Game = new Jeu();
	}
	
	private class Jeu_this_keyAdapter extends KeyAdapter {
        private Jeu adaptee;

        Jeu_this_keyAdapter(Jeu adaptee) {
            this.adaptee = adaptee;
        }

        public void keyPressed(KeyEvent e) {
            adaptee.this_keyPressed(e);
        }

        public void keyReleased(KeyEvent e) {
            adaptee.this_keyReleased(e);
        }
    }
	
	public void this_keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_SPACE) ToucheEspace=true;
                        else
                if (code == KeyEvent.VK_LEFT) ToucheGauche=true;
                                else
                        if (code == KeyEvent.VK_RIGHT) ToucheDroit=true;
                                        else
                                if (code == KeyEvent.VK_ENTER)
                                        if (Montimer.isRunning()) timer.stop();
                                                      else     Montimer.start();
                                                else
                                            if (code == KeyEvent.VK_ESCAPE) System.exit(0);
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
        brick = T.getImage("Brick.jpg");
        

		
		//paddle = new Objet("navire.png", (int)(Ecran.width/2),(int)(Ecran.height/2),0,0);
		
		//ActionListener
		Montimer = new Timer(TempsTimer_ms,this);	
		//Started by Enter Key Montimer.start();
		
		
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
            buffer.drawImage(brique.image, brique.x,brique.y,this);
		}
			
		g.drawImage(ArrierePlan,0,0,this);
		
		
	}
}
