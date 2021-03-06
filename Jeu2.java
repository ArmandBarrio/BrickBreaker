﻿import javax.swing.*;
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
  public Brick lesBriques[][]= new Brick[8][14];
  public PowerUp lesPowerUps[] = new PowerUp[0];
  //PowerUp firstPowerUp;



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
	public Image WinImage;

	//Screen Dimension
	public int screenWidth;
	public int screenHeight;
	public boolean onFire = false;

	//paddle size
	public int paddleWidth = 200;
	public int paddleHeight = 30;

	//Start Screen and Game Over
	public boolean intro = true;
	public boolean startScreen = false;
	public boolean play = false;
	public boolean gameOver = false;
	public boolean arrowUp = true;
	public boolean win = false;
	public int nbNormalBricks=0;
	public int nbDestroyedBricks=0;
	public long HighScore=0;



  //Pause the ball before sending it
  public boolean newBall = true;
  public int countdown = -200;

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
				if ( r > 0.2 && r < 0.4) randomType = "PowerUp";
				if ( r >=0.4) {
					randomType = "Normal";
				}
				lesBriques[i][j] = new Brick ( 10 + i * 70, 100+j * 34, randomType, randomState );
			}
		}
		
		//customLevelChristmas(); 
    // Pour créer les murs
    upperWall = new Object ( "HorizontalWall.png" , 10000,10, 0,0);
    leftWall = new Object ( "VerticalWall.png" , 10000,10+upperWall.h, 0,0);
    rightWall = new Object ( "VerticalWall.png" , 10000+lesBriques.length * 70,10+upperWall.h, 0,0);


    // Create the Paddle
    Paddle = new Object ( "Paddle.png", (int)(screenWidth*0.3),(int)(screenHeight*0.9),10,10);

    // test powerups
    //firstPowerUp = new PowerUp("fasterBall", 100, 400);
		//lesPowerUps[0] = firstPowerUp;



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

    // Create the Paddle and Ball
    Paddle = new Object ( "Paddle.png", 400,(int)(screenHeight*0.9),10,10/TempsTimer_ms);
    Ball = new Object("Ball.png", (int)(screenWidth*0.2),(int)(screenHeight*0.5),(float) (Math.random()*60*Math.PI*2.0/360.0 + 30*Math.PI*2.0/360.0),10/TempsTimer_ms);


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
		WinImage= T.getImage("WinImage.jpg");
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
			if (countdown > 100){
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
			gestionPowerUp();
			if (!newBall){
				gestionBall();
			}else{
				countdown++;
			}
			if (countdown > 100){
				newBall = false;
				countdown = 0;
			}

		}

		if(gameOver){
			this.setTitle("BRICKBREAKER || INSA EDITION");
			countdown++;
			if (countdown > 100){
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
			countdown = -160;
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

	public void gestionPowerUp(){
		for (int i=0; i< lesPowerUps.length; i++){
			if (lesPowerUps[i].active){
				lesPowerUps[i].setX(lesPowerUps[i].x);
				lesPowerUps[i].setY(lesPowerUps[i].y);
				lesPowerUps[i].move(Ecran);
				if (lesPowerUps[i].bounceOffPaddle(Paddle.x, Paddle.y, paddleWidth)){
					System.out.println("le PU"+lesPowerUps[i].Type+ "touche le paddle");
					lesPowerUps[i].active = false;
					// each power up has its effect
					if (lesPowerUps[i].Type == "fasterBall"){
						Ball.vitesse = (float)( Ball.vitesse*1.1);
						System.out.println ( " la vitesse de la balle est : " +Ball.vitesse);
					}
					if(lesPowerUps[i].Type == "slowerBall" ){
						Ball.vitesse = (float)(Ball.vitesse*0.9);
						System.out.println ( " la vitesse de la balle est : " +Ball.vitesse);
					}
					if (lesPowerUps[i].Type== "largerPaddle"){
						paddleWidth=(int)(paddleWidth*1.2);
					}
					if (lesPowerUps[i].Type== "smallerPaddle"){
						paddleWidth=(int)(paddleWidth*0.8);
					}
					if (lesPowerUps[i].Type== "fireBall"){
						onFire = true;
						Ball.vitesse = 20;
					}
					
				}
			}
		}
	}
	public void customLevelChristmas(){
		for(int i=0; i <7; i++){
			for (int j =0; j<14;j++){
				lesBriques[i][j]= new Brick ( 400 + i*70, 150+j*34, "Normal", 3);
			}
		} 
		for ( int i = 0;i<7;i++){ 
			lesBriques[i][6]= new Brick ( 400 + i*70, 150+6*34, "PowerUp", 1);
			lesBriques[i][7]= new Brick ( 400 + i*70, 150+7*34, "PowerUp", 1);
		}
		for ( int j = 0; j< 14; j++){
			lesBriques[3][j]= new Brick ( 400 + 3*70, 150+j*34, "PowerUp", 1);
		}
		lesBriques[7][0]= new Brick ( 400 + 3*70, 150-34, "Unbreakable", 1);
		lesBriques[7][1]= new Brick ( 400 + 2*70, 150-2*34, "Unbreakable", 1);
		lesBriques[7][2]= new Brick ( 400 + 4*70, 150-2*34, "Unbreakable", 1);
		lesBriques[7][3]= new Brick ( 400 + 5*70, 150-2*34, "Unbreakable", 1);
		lesBriques[7][4]= new Brick ( 400 + 1*70, 150-2*34, "Unbreakable", 1);
		lesBriques[7][5]= new Brick ( 400 + 0*70, 150-3*34, "Unbreakable", 1);
		lesBriques[7][6]= new Brick ( 400 + 6*70, 150-3*34, "Unbreakable", 1);
		lesBriques[7][7]= new Brick ( 400 + 6*70, 150-4*34, "Unbreakable", 1);
		lesBriques[7][8]= new Brick ( 400 + 0*70, 150-4*34, "Unbreakable", 1);
		lesBriques[7][9]= new Brick ( 400 + 1*70, 150-4*34, "Unbreakable", 1);
		lesBriques[7][12]= new Brick ( 400 + 5*70, 150-4*34, "Unbreakable", 1);		
		lesBriques[7][10]= new Brick ( 400 + 4*70, 150-3*34, "Unbreakable", 1);
		lesBriques[7][11]= new Brick ( 400 + 2*70, 150-3*34, "Unbreakable", 1);
		lesBriques[7][13]= new Brick ( 0, 0, "Normal", 0);
		
		
	}	

	public void gestionBall(){

		Ball.move(Ecran);
		win = true;

		for (int i = 0; i < lesBriques.length; i++){
			for (int j = 0 ; j < lesBriques[0].length;  j++){
				if( lesBriques[i][j].state != 0){
				//si la balle n'est pas en feu !
					if (!onFire){
						if (Ball.bounce(lesBriques[i][j])){
							music("CollisionMusic.wav");
							if(lesBriques[i][j].Type == "PowerUp"){
								String randomPowerUp = randomPowerUp();
								PowerUp newPowerUp = new PowerUp( randomPowerUp, lesBriques[i][j].x,lesBriques[i][j].y);
								System.out.println ( "nouveau "+randomPowerUp +"créé. vitesse :"+ newPowerUp.vitesse +"direction : "+newPowerUp.direction);
								PowerUp lesNewPowerUps[] = new PowerUp[lesPowerUps.length+1];
								for ( int k =0; k<lesPowerUps.length; k++){
									lesNewPowerUps[k]= lesPowerUps[k];
								}
								lesNewPowerUps[lesPowerUps.length] = newPowerUp;
								lesPowerUps=lesNewPowerUps;
								for (int k =0; k<lesPowerUps.length;k++){
									System.out.print (lesPowerUps[k].Type + "position :" +lesNewPowerUps[k].x+";"+lesNewPowerUps[k].y);
									if (lesNewPowerUps[k].active){
										System.out.println( "  is active");
									}else{
										System.out.println("   is not active");
									}
								}
							}
						}
				// si la balle est en feu, elle ne rebondit pas sur les briques mais les detruit toutes...
					}else {
						
						if (lesBriques[i][j].Collision(Ball)){
							lesBriques[i][j].state = 0;
							music("CollisionMusic.wav");
							/*if(lesBriques[i][j].Type == "PowerUp"){
								String randomPowerUp = randomPowerUp();
								PowerUp newPowerUp = new PowerUp( randomPowerUp, lesBriques[i][j].x,lesBriques[i][j].y);
								System.out.println ( "nouveau "+randomPowerUp +"créé. vitesse :"+ newPowerUp.vitesse +"direction : "+newPowerUp.direction);
								PowerUp lesNewPowerUps[] = new PowerUp[lesPowerUps.length+1];
								for ( int k =0; k<lesPowerUps.length; k++){
									lesNewPowerUps[k]= lesPowerUps[k];
								}
								lesNewPowerUps[lesPowerUps.length] = newPowerUp;
								lesPowerUps=lesNewPowerUps;
								for (int k =0; k<lesPowerUps.length;k++){
									System.out.print (lesPowerUps[k].Type + "position :" +lesNewPowerUps[k].x+";"+lesNewPowerUps[k].y);
									if (lesNewPowerUps[k].active){
										System.out.println( "  is active");
									}else{
										System.out.println("   is not active");
									}
								}
							}*/
						} 		
					}				
					if( lesBriques[i][j].state > 0){
						win = false;
					}
				}


				if( lesBriques[i][j].state > 0){
					win = false;
				}
				if (win) play = false;

				}
				if (win) play = false;
					
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
			Ball.vitesse = 10;
			onFire = false;
			if (NbVies == 0) {
				play = false;
				gameOver = true;
				music("GameOverMusic.wav");
			}
		}
	}


	public String randomPowerUp(){
		double random = Math.random();
		if (random<=0.3)return "fasterBall";
		if (random>0.3 &&random<=0.5) return "slowerBall";		
		if (random > 0.5 && random <= 0.7) return "smallerPaddle";
		if (random>0.7 && random <= 0.85)return "largerPaddle";
		if (random > 0.85)return "fireBall";
		return "";
		
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

	public void mouseMoved(MouseEvent e) {
		if ( e.getX() > paddleWidth/2 && e.getX() < screenWidth - paddleWidth/2){
			Paddle.x = e.getX() - paddleWidth/2;
		}
	}
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
			buffer.drawString("Highscore="+Highscore,(int)(screenWidth * 0.05),(int)(screenHeight*0.05));
			// afficher toutes les briques actives
      for ( int i = 0; i< lesBriques.length; i++){
				for (int j = 0 ; j < lesBriques[0].length;  j++){

					if (lesBriques[i][j].state != 0){
						buffer.drawImage(lesBriques[i][j].image, lesBriques[i][j].x,lesBriques[i][j].y,this);
					}
				}
			}
			for (int i = 0 ; i<lesPowerUps.length ; i++){
				if (lesPowerUps[i].active){

					buffer.drawImage(lesPowerUps[i].image, lesPowerUps[i].x,lesPowerUps[i].y,this);
				}
			}
      buffer.drawImage(Ball.image, Ball.x,Ball.y,this);
			buffer.drawImage(leftWall.image, leftWall.x,leftWall.y,this);
      buffer.drawImage(rightWall.image, rightWall.x,rightWall.y,this);
      buffer.drawImage(upperWall.image, upperWall.x,upperWall.y,this);
      buffer.drawImage(Paddle.image, Paddle.x, Paddle.y, paddleWidth, paddleHeight, this);
		}

		if (gameOver){
			HighScore=Temps;
			buffer.drawImage(GameOverImage,0,0,screenWidth, screenHeight, this);
			for ( int i = 0; i< lesBriques.length; i++){
				for (int j = 0 ; j < lesBriques[0].length;  j++){
					if (lesBriques[i][j].Type=="Normal"){
						lesBriques[i][j].state =(int)(Math.random()*3 +1);
					}
				}
			}
		}
		if (win){
			buffer.drawImage(WinImage, 0,0, screenWidth, screenHeight, this);
		}

		g.drawImage(ArrierePlan,0,0,this);
	}
}
