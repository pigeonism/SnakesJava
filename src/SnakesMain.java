// wayne warren 2016 
import java.awt.Graphics;
import java.awt.Color;
import java.awt.Event;
import java.util.ArrayList;
import java.util.Random;

public class SnakesMain extends java.applet.Applet implements Runnable{
	// calls the helper classes
	int time = 200;
	Thread runner;
	
	// direction flags
	boolean up, down, left, right;
	boolean enterpressed;
	
	//screen vars
	boolean drawselected = true;
	int width; int height;
	
	
	//squares to show user pattern
	int[] coords = {0, 0};
	ArrayList<Object> squares;
	int trimtime = 0;
	//snacks
	int[] snackCoords = {0,0}; 
	boolean showSnack;
	boolean snackCollision = false;
	int snackLife = 0;
	
	// ##### all Drawing and thread methods ######
	public void init() {
		//screen params
		width = getWidth();
		height = getHeight();
		//System.out.println(height);
		setBackground(Color.white);
		gameStart();
		
	}
	public void gameStart(){
		snackLife = 0;
		trimtime = 0;
		time = 200;
		coords[0] = width/2; coords[1] = height/2;

		// add sqr to center
		squares = new ArrayList<Object>();
		squares.add(coords);
		pickDirection();
		// to shorten snake
		showSnack = true;
		addSnack();
	}
	public void pickDirection(){
		//pick random direction 0 to .3 to set direction flags with
		up = false; down = false; left = false; right = false;
		Random rand = new Random();
		int rnum = rand.nextInt(4);
		//System.out.println("random number: " + rnum );
		if(rnum == 0){up = true;}
		if(rnum == 1){down = true;}
		if(rnum == 2){left = true;}
		if(rnum == 3){right = true;}
	}
	
	public void addSnack(){
		// choose random coords and place snacks there
		Random xrand = new Random();
		Random yrand = new Random();
		int xNum = xrand.nextInt(width/2);
		int yNum = yrand.nextInt(height/2);
		snackCoords[0] =  (xNum / 10)*10;
		snackCoords[1] =   (yNum / 10*10);
		System.out.println("snack coords: " + snackCoords[0] +" "+ snackCoords[1]);
		
		// if snack is in snake, call myself again
		for (int i = 0; i < squares.size(); i++) {
			int[] pair = (int[])squares.get(i);
			if(pair[0] == snackCoords[0] && pair[1] == snackCoords[1]){
				addSnack();
			}
		}
	}
	
	public void collideCheck(){
		//check screen bounds
		int[] firstpair = (int[])squares.get(0);
		if(firstpair[0] < 0 ){gameStart();}
		if(firstpair[0] > width){gameStart();}
		if(firstpair[1] < 0){gameStart();}
		if(firstpair[1] > height){gameStart();}
		// check self collision from element 1, because 0 is what we're checking against
		for (int i = 1; i < squares.size(); i++) {
			int[] pair = (int[])squares.get(i);
			if(pair[0] == firstpair[0] && pair[1] == firstpair[1]){
				gameStart();
			}
			
		}
		// check for snack collision
		if(firstpair[0] == snackCoords[0] && firstpair[1] == snackCoords[1]){
			// trim size
			System.out.println("snack collision");
			squares.remove(1);squares.remove(1);squares.remove(1);squares.remove(1);
			// move snack
			time -= 5;
			snackCollision = true;
			addSnack();
			snackLife = 0;
		}
		
	}
	public void start() {
		if (runner == null) {
			runner = new Thread(this);
			runner.start();
		}
	}
	public void stop() {
		if (runner != null) {
			runner.stop();
			runner = null;
		}
	}
	public void run() {
		
		while (true) {
			//check collisions first
			collideCheck();
			
			//System.out.println("cool");
			if (up == true){
				// get first pair of coords subtract ten from y
				int[] firstpair = (int[])squares.get(0);
				int[] temp = {firstpair[0], firstpair[1]}; //copy of original
				firstpair[1] -= 10;
				squares.set(0, firstpair);
				//add copy
				squares.add(temp);
			}
			if (down == true){
				// get first pair of coords add ten to y
				int[] firstpair = (int[])squares.get(0);
				int[] temp = {firstpair[0], firstpair[1]}; //copy of original
				firstpair[1] += 10;
				squares.set(0, firstpair);
				//add copy
				squares.add(temp);
			}
			if (left == true){
				// get first pair subtract ten from x 
				int[] firstpair = (int[])squares.get(0);
				int[] temp = {firstpair[0], firstpair[1]}; //copy of original
				firstpair[0] -= 10;
				squares.set(0, firstpair);
				//add copy
				squares.add(temp);
				
			}
			if (right == true){
				// get first pair add ten to x 
				int[] firstpair = (int[])squares.get(0);
				int[] temp = {firstpair[0], firstpair[1]}; //copy of original
				firstpair[0] += 10;
				squares.set(0, firstpair);
				//add copy
				squares.add(temp);
			}
			trimtime += 1;
			snackLife += 1;

			//System.out.println("snack life:" + snackLife);
			if(snackLife == 90){
				snackLife = 0;
				addSnack();
			}
			// for shortening snake tail
			if(trimtime == 2){
				trimtime = 0;
				squares.remove(1);
			}
			repaint();
			try { 
				Thread.sleep(time);
				
				}
			
			catch (InterruptedException e) { }
			
		}
		
	}
	
	public boolean keyDown(Event evt, int key) {
		switch (key) {
		case Event.ENTER:		
		//case (10):
			enterpressed = true;
			//System.out.println("Enter pressed");
			gameStart();
			break;
		case Event.UP:
			//System.out.println("Up pressed");
			up = true;
			// make others false
			down = false; left = false; right = false;
			break;
		case Event.DOWN:
			//System.out.println("Down pressed");
			down = true;
			//make others false
			up = false; left = false; right = false;
			break;
		case Event.LEFT:
			//System.out.println("Left pressed");
			left = true;
			//make others false
			up = false; down = false; right = false;
			break;
		case Event.RIGHT:
			//System.out.println("Right pressed");
			right = true;
			//make others false
			up = false; left = false; down = false;
			break;
		default:
			System.out.println("Something else pressed");
		}
		repaint();
		return true;
	}
	
	public void paint(Graphics g) {
		g.setColor(Color.black);
		// array list contains only a pair of ints this time
		if(drawselected == true){
			for (int i = 0; i < squares.size(); i++) {
				int[] pair = (int[])squares.get(i);
				g.fillRect(pair[0] , pair[1] ,10,10);
			}
			g.setColor(Color.red);
			if(showSnack){
				g.fillRect(snackCoords[0], snackCoords[1], 10, 10);
			}
		}
		
	}

}
