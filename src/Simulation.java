//Κουφάκης Αλέξανδρος-Μιχαήλ, 2175
//Σεϊταρίδης Ανδρέας, 2200

package EnvironmentFinish;

import input.Input;

import javax.swing.JOptionPane;
import javax.vecmath.Color3f;
import javax.vecmath.Vector3d;
import javax.vecmath.Vector3f;

import simbad.gui.Simbad;
import simbad.sim.Box;
import simbad.sim.CherryAgent;
import simbad.sim.EnvironmentDescription;
import simbad.sim.Wall;

public class Simulation {

    public static void main(String[] args) {

    	Input input = new Input(); // item that reads or creates the file
    	char[][] map; // the enviroment map (boxes, goal etc...)
    	
    	// Asking for user's input
    	 String[] choices = { "Random", "File"};
    	    String choice = (String) JOptionPane.showInputDialog(null, "Choose now mr Vrakas", "Chose input method", JOptionPane.QUESTION_MESSAGE, null, choices, // Array of choices
    	        choices[1]); // Initial choice
    	    
    	    if(choice.equals("Random"))
    	    {
    	    	map = input.createRandomFile(); // create random file
    	    }
    	    else
    	    {
    	    	map = input.readFileWithGraphical(); // read from file
    	    }
    	 // end of input
    	
    	double world_size = map.length; // world's size
    	
        EnvironmentDescription env = new EnvironmentDescription();
        env.setWorldSize(map.length); 
        System.out.println(map.length);
        
        MyRobot robot = null;
        
        double goal_x = 0, goal_y = 0, robot_x = 0, robot_y = 0; // robot's and goal's coordinates
        
        
        // adding items to the environment
		for(int i = 0; i < map.length; i++) // for every block in the x axis
		{
			for(int j = 0; j < map[i].length; j++) // for every block in the z axis
			{
				
				if(map[i][j] == 'x') // x represents a box
				{
					double x, y;
					// transpose map array indexes to world's coordinates
					y = j - (world_size / 2); 
					x = i - (world_size / 2);
					// adding the box
					Box b1 = new Box(new Vector3d(x + 0.5, 0, -y - 0.5), new Vector3f(1, 1, 1), env);
					b1.setColor(new Color3f(0.0f, 0.0f, 1.0f));
			        env.add(b1); 
				}
				if(map[i][j] == 'R') // R represents the Robot
				{
					// saving robot's coords for future use
					robot_y = j - (world_size / 2); 
					robot_x = i - (world_size / 2);
				}
				if(map[i][j] == 'G') // G = goal
				{
					goal_y = j - (world_size / 2);
					goal_x = i - (world_size / 2);	
					// adding a cherry to the goal's position in the world
			        CherryAgent ca = new CherryAgent(new Vector3d(goal_x + 0.5, 0, -goal_y - 0.5),"goal",0.3f);
			        env.add(ca);
				}
				if(map[i][j] == 'e') // e = enemy robot. It is considered as an obstacle
				{
					/*
					double x, y;
					y = j - (world_size / 2);
					x = i - (world_size / 2);
					// adding the obstacle
					Box b1 = new Box(new Vector3d(x + 0.5, 0, -y - 0.5), new Vector3f(1, 1, 1), env);
					b1.setColor(new Color3f(0.0f, 1.0f, 1.0f));
			        env.add(b1);
			        */
				}
			}
			System.out.println();
		}

		// adding our robot to the world
		robot =  new MyRobot(new Vector3d(robot_x + 0.5, 0, -robot_y - 0.5), "Mr Rubato", new Vector3d(goal_x + 0.5, 0, -goal_y - 0.5));
		robot.setMap(map);
		env.add(robot);
    
        
		// adding surrounding walls
        Wall w1 = new Wall(new Vector3d(0.0, 0, world_size/2 + 0.15), map.length + 0.6f, 1, env);
        w1.setColor(new Color3f(1.0f, 1.0f, 0.0f));
        env.add(w1);
        w1 = new Wall(new Vector3d(0.0, 0, -(world_size/2 + 0.15)), map.length + 0.6f, 1, env);
        w1.setColor(new Color3f(1.0f, 1.0f, 0.0f));
        env.add(w1);
        w1 = new Wall(new Vector3d(-(world_size/2 + 0.15), 0, 0), map.length, 1, env);
        w1.setColor(new Color3f(1.0f, 1.0f, 0.0f));
        w1.rotate90(1);
        env.add(w1);
        w1 = new Wall(new Vector3d(world_size/2 + 0.15, 0, 0), map.length, 1, env);
        w1.setColor(new Color3f(1.0f, 1.0f, 0.0f));
        w1.rotate90(1);
        env.add(w1);
        // endof adding surrounding walls


        @SuppressWarnings("unused")
		Simbad simulator = new Simbad(env, false);
    }

}
