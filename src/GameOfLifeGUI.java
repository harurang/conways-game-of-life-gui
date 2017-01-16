/**
 * Completed: 03/24/2015
**/

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.nio.file.*;
import java.lang.*;

/**
* This class is responsible for the 'View' of the program.
**/
public class GameOfLifeGUI extends JPanel implements Runnable
{
	private Grid grid;
	//allows users to draw on grid with mouse
	private MouseMotionListener mouse; 
	//allows users to control game with keyboard
	private KeyListener keyboard;
	//contains contents of entire GUI
	JFrame frame;
	
	//outputs data to binary file
	private ObjectOutputStream output;
	//inputs data from binary file
	private ObjectInputStream input;

	/**
	@requres instance variables are declared
	@ensures instance variables are instantiated and JPanel is added to JFrame
	**/
	public GameOfLifeGUI()
	{
		//instantiate grid 100 x 100
		this.grid = new Grid(100,100);
		//add mouse listener to JPanel
		this.addMouseMotionListener(mouse = new MouseMotionListener());

		frame = new JFrame("Game of Life");
		//set size of frame
		frame.setSize(1000,1000);
		//ensure frame is visible
		frame.setVisible(true);
		//I chose BorderLayout as my JFrame layout because this way my JPanel will cover bottom, top, left, right and center
		frame.setLayout(new BorderLayout());
		//have frame display in the center of the computer
		frame.setLocationRelativeTo(null);
		//have program end when frame closes
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//add keyListener to JFrame
		frame.addKeyListener(keyboard = new KeyListener());
		//add JPanel to the center
		frame.add(this);

		//initiate Grid object with live cells specified at specific indices
		grid.gliderSetup();
	}

	/**
	@ensures serialized file is created 
	**/
	public void openNewFile()
	{
		try
		{
			//output will be used to write into "GameState.ser" the bytes that represent an object
			output = new ObjectOutputStream(new FileOutputStream("GameState.ser"));
		}
		catch(IOException e)
		{
			System.err.println("Error opening file.");
		}
	}

	/**
	@ensures existing serialized file is opened
	**/
	public void openExisitingFile()
	{
		try
		{
			//input will be used to read the bytes from "GameState.ser" 
			input = new ObjectInputStream(Files.newInputStream(Paths.get("GameState.ser")));
		}
		catch(IOException e)
		{
			System.err.println("File not found.");
		}
	}

	/**
	@ensures data is written to serialized file
	**/
	public void writeFile()
	{
		try
		{
			//write grid state to binary file
			output.writeObject(grid);
		}
		catch(IOException ioException)
		{
			System.err.println("Error saving Grid state.");
		}
	}

	/**
	@requires file is open
	@ensures data is written into grid
	**/
	public void readFile()
	{
		try
		{
			//read in serialized data to grid
			grid = (Grid) input.readObject();
			//repaint JPanel
			repaint();
		}
		catch(IOException ioException)
		{
			System.err.println("Error reading from file, terminating.");
		}
		catch(ClassNotFoundException classNotFound)
		{
			System.out.println("Class not found.");
		}
	}

	/**
	@requires pre exising file
	@ensures serialized file is closed after reading in data
	**/
	public void closeInput()
	{
		try
		{
			//close input object
			input.close();
		}
		catch(IOException e)
		{
			System.err.println("Error closing.");
		}
	}

	/**
	@requires pre exising file
	@ensures serialized file is closed after writing data
	**/
	public void closeOutput()
	{
		try
		{
			//close output object
			output.close();
		}
		catch(IOException e)
		{
			System.err.println("Error closing.");
		}
	}

	/**
	@requires Graphics object which is provided by system and used to draw shapes
	@ensures paintComponent is automatically called every time the View needs to be displayed

	paintComponent is is part of JPanel. Since I extended JPanel, I can override it.
	**/
	@Override
	public void paintComponent(Graphics g)
	{
		//ensures JPanel is displayed correctly
		super.paintComponent(g);
		
		//loop through Grid object
		for(int i = 0; i < 100; i++) 
			for(int j = 0; j < 100; j++)
			{ 
				/*
				Create pixles, pixels must be smaller than cell to ensure size to ensure gridlines are present
				 */
				int x = i * 17;
				int y = j * 17;

				//based on boolean value of Cell
				if(grid.cellIsAlive(i, j))
				{
					//set color
					g.setColor(Color.PINK);
					//fill specified position and size with set color
					g.fillRect(x,y,13,13);
					//draw rectangle at specified postion with specified size
					g.drawRect(x,y,13,13);
				}
				else
				{
					//set different color 
					g.setColor(Color.GRAY);
					//fill specified position and size with set color
					g.fillRect(x,y,13,13);
					//draw rectangle last after all specifications are made
					g.drawRect(x,y,13,13);
				}
			}
	}

	/**
	@requires value of keyPressed to be true
	@ensures the model and the view continuously update
	**/
	public void run()
	{
		while(true) {
			//update Grid
			grid.update();
			//repaint JPanel
			repaint();
			//pause every 30 millis
			try
			{
				Thread.sleep(30);
			}
			catch(InterruptedException e)
			{
				System.out.println(e);
			}
		}
	}

	/**
	@requires a MouseEvent
	@ensures selected Cells change color
	**/
	private class MouseMotionListener extends MouseMotionAdapter
	{
		//if a MouseEvent is generated
		public void mouseDragged(MouseEvent e)
		{
			//get coordinates of Cell
			int x = e.getX()/17;
			int y = e.getY()/17;
			//if that cell is alive
			if(grid.cellIsAlive(x,y))
			{
				//change state of Cell
				//getCell is a method I added to Grid to query Cell state
				grid.getCell(x,y).setAlive(false);
				//repaint JPanel
				repaint();
			}
			else
			{
				//repaint the true Cells
				grid.getCell(x,y).setAlive(true);
				repaint();
			}
		}
	}

	/**
	@requires KeyEvent
	@ensures appropriate action is executed based on key pressed
	**/
	private class KeyListener extends KeyAdapter
	{
		//if KeyEvent is generated
		public void keyTyped(KeyEvent e)
		{
			//get the value of the key
			char key = e.getKeyChar();

			//if key is 'b'for begin
			if(key == 'b')
			{
				/**
				* Because run() contains an infinite while loop, a new thread is created.
				* If it was a single loop, GameOfLIfeGUI.this.run() or new Thread(GameOfLifeGUI.this).run() would suffice.
				**/
				new Thread(GameOfLifeGUI.this).start();
			}
			//if key is 's' for save
			else if(key == 's')
			{	
				//create new serialized file
				openNewFile();
				//write serialized file
				writeFile();
				//close serialized file
				closeOutput();
			}
			//if key is 'l' for load
			else if(key == 'l')
			{
				//open exisiting serialized file
				openExisitingFile();
				//read in serialized file
				readFile();
				//close serialized file
				closeInput();
			}
		}
	}
}












