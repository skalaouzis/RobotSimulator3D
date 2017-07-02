

package EnvironmentFinish;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.PriorityQueue;
import simbad.sim.*;

import javax.vecmath.Vector3d;
import javax.media.j3d.*;
import javax.vecmath.Matrix3d;
import javax.vecmath.Point2i;
import javax.vecmath.Point3d;

import Data.ListData;

public class MyRobot extends Agent {

	Vector3d goal;
	Vector3d current_goal;
	int g; // this is what the robot is supposed to do - 1 = turn around - 2 = move forward
		  // 3 = compute the angle for the next step - 4 = goal reached
	
	char map[][]; // the map of the world
	double world_size; // world's size
	
	// A* stuff
	private ArrayList<ListData> closeList; // the close List for the A*
	private ArrayList<ListData> path;
	// endof A* stuff
	
	private double angle; // how much the robot must rotate to move to the next block
	private double rotational_velocity = Math.PI / 16; // the speed that the robot rotates
	private double steps = 0.0; // how many blocks the robot moved so far
	private Point3d r;

	RangeSensorBelt sonars;
    CameraSensor camera;
    
	// reading the map
	public void setMap(char map[][]) {
		this.map = map;
		world_size = map.length;
	}

	// Constructor
	public MyRobot(Vector3d position, String name, Vector3d goal) {
		super(position, name);
		// Add sensors
        camera = RobotFactory.addCameraSensor(this);
        RobotFactory.addBumperBeltSensor(this);
       // Add sonars and get corresponding object.
       sonars  = RobotFactory.addSonarBeltSensor(this);
		this.goal = goal;
	}

	public void initBehavior() {
		
		computeNextGoalWithAStar(); // compute the path with A*

		findPath(); // finds the path that the robot will follow. Sets the path to the 'path' variable

		Collections.reverse(path); // reversing the path, because it is saved reversed
		g = 3; // set the state -> compute angle
	}

	public void performBehavior() {

		// rotate
		if (g == 1) {
		
				// when the robot reaches the desired angle it should stop rotating
				if ((round(getAngle(), 1)) == (round(angle, 1)) )
				{
					g = 2; // change the state

					rotate(0); // set the rotational velocity to zero
				} else { // if the desired angle hasn't been reached rotate
					rotate(rotational_velocity);
				}
		}
		// move forward
		else if (g == 2) {
			r = new Point3d();// save the robot's coordinates
			this.getCoords(r); 

			// if the robot have moved 1 meter (which is the size of a block)
			if (this.getOdometer() / steps >= 1.0) { // then stop moving
				g = 3; // set the state to compute the next angle
				this.setTranslationalVelocity(0); // set the translational velocity to zero 
			} else { // keep moving
				this.setTranslationalVelocity(0.05);
			}
	        Vector3d lg;
	        lg = getLocalCoords(goal);        
	        double dist = Math.sqrt(lg.x*lg.x+lg.z*lg.z);
	        System.out.println(dist);
	        if ( dist < 0.1)
	        { 
	        	this.setTranslationalVelocity(0);
	        	g = 4;
	        }
		} else if (g == 3) { // compute the next angle - for the next block
			g = 1;
			
			computeNextGoalWithAStar(); // compute the path with A*

			findPath(); // finds the path that the robot will follow. Sets the path to the 'path' variable
			System.out.println("----------------------------------");



			Collections.reverse(path); // reversing the path, because it is saved reversed
			for(ListData d: path)
			{
				System.out.println(d.getX() + ", " + d.getY());
			}
			computeAngle();
		}

	}

	
	
    public Vector3d getLocalCoords(Vector3d p)
    {
        Vector3d a = new Vector3d();
        Point3d r = new Point3d();
        double th = getAngle();        
        double x,y,z;
        getCoords(r);
        x=p.x - r.x;
        z=-p.z+ r.z;   
        a.x = x*Math.cos(th) + z*Math.sin(th);
        a.z = z*Math.cos(th) - x*Math.sin(th);
        a.y = p.y;
        return a;
    }  
	
	
	
	// round a double number with the desired precision
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = new BigDecimal(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	
	
	// computes the angle for the next block
	public void computeAngle()
	{
		
		steps ++;
		// if the goal has been reached - last step then change the state to the final state

 // if there are more steps to move

			
			
			System.out.println("--------------------------------------");

			Point3d r = new Point3d(); // robot's current coordinates
			this.getCoords(r);

			ListData ken_block = path.get(1); // coordinates of the next block

			// transpose indexes to coordinates
			Point3d k = new Point3d(ken_block.getX() - (world_size/2) + 0.5, 0, -(ken_block.getY() - (world_size/2))  - 0.5);
			
			angle = -Math.atan2((k.z - r.z), (k.x - r.x)); // compute the angle that the two points form
			// if the angle is less than zero then transpose it to the positive
			// transpose from [-pi, pi] to  [0, 2pi]
			if(angle < 0)
			{
				angle += 2 * Math.PI;
			}
			System.out.println("The new angle: " + Math.toDegrees(angle));
			
			
			
			// find the best suitable rotational velocity direction (clockwise or counter clockwise)
			// finds the shortest path from the old angle to the new, and sets the respective velocity sign
			// explained furthermore in the Report
			double currentAngle = this.getAngle();
			
			double dif1 = angle - currentAngle;
			double dif2 = (2 * Math.PI) - Math.abs(dif1);
			rotational_velocity = Math.abs(rotational_velocity);
			if(dif1 > 0)
			{
				if(Math.abs(dif1) < Math.abs(dif2))
				{
					rotational_velocity = Math.abs(rotational_velocity);
				}
				else if(Math.abs(dif1) > Math.abs(dif2))
				{
					rotational_velocity = -Math.abs(rotational_velocity);
				}
			}
			else if(dif1 < 0)
			{
				if(Math.abs(dif1) < Math.abs(dif2))
				{
					rotational_velocity = -Math.abs(rotational_velocity);
				}
				else if(Math.abs(dif1) > Math.abs(dif2))
				{
				}
			}


			System.out.println("Robot coords: " + r.toString() + ", " + k.toString());
	

		
	}
	
	
	

	
	// sets the rotational velocity
	public void rotate(double a) {
		this.setRotationalVelocity(a);
	}

	
// returns the robot\c current angle
	public double getAngle() {
		double angle = 0;
		double msin;
		double mcos;
		Transform3D m_Transform3D = new Transform3D();
		this.getRotationTransform(m_Transform3D);
		Matrix3d m1 = new Matrix3d();
		m_Transform3D.get(m1);
		msin = m1.getElement(2, 0);
		mcos = m1.getElement(0, 0);
		if (msin < 0) {
			angle = Math.acos(mcos);
		} else {
			if (mcos < 0) {
				angle = 2 * Math.PI - Math.acos(mcos);
			} else {
				angle = -Math.asin(msin);
			}
		}
		while (angle < 0)
			angle += Math.PI * 2;
		return angle;
	}

	public void setCurrent_goal(Vector3d current_goal) {
		this.current_goal = current_goal;
	}


	// computes the path with A*
	public void computeNextGoalWithAStar() {
		Comparator<ListData> comparator = new fValueComparator(); // a comparator...
		PriorityQueue<ListData> openList = new PriorityQueue<ListData>(
				comparator); // A list that keeps the element with the minimum F (heuristic) value, keeps the blocks that are going to be checked
		closeList = new ArrayList<ListData>(); // the list that keeps the blocks that A* has visited

		r = new Point3d(); // save the robot's coordinates
		this.getCoords(r); 

		// transpose to array's indexes
		r.x = (r.x - 0.5) + (world_size / 2);
		r.z = -(r.z + 0.5) + (world_size / 2);
		
		r.x = Math.round(r.x);
		r.y = Math.round(r.z);

		
		openList.add(new ListData((int) Math.round(r.x), (int) Math.round(r.z), null)); // adding the first entry to the open list - robot's current position
		
		int counter = 1;
		while (!openList.isEmpty()) {
			//System.out.println("Run " + counter + ": ");
			counter++;
			ListData q = openList.remove();
			int x, y;
			x = q.getPoint().x;
			y = q.getPoint().y;

			// the if statements below check in which directions the robot can move
			
			if ((y + 1) != map.length) { // if it is not out of the map
				if (map[x][y + 1] == 'e' || map[x][y + 1] == 'R'
						|| map[x][y + 1] == 'G') { // if the point is free
					if (checkLists(x, y + 1, openList, q, (int)r.x, (int)r.z)) {
						closeList.add(q);
						closeList.add(new ListData(x, y + 1, q));
						break;
					}
				}
			}
			if ((y - 1) != -1) {
				if (map[x][y - 1] == 'e' || map[x][y - 1] == 'R'
						|| map[x][y - 1] == 'G') {
					if (checkLists(x, y - 1, openList, q, (int)r.x, (int)r.z)) {
						closeList.add(q);
						closeList.add(new ListData(x, y - 1, q));
						break;
					}
				}
			}
			if ((x + 1) != map.length) {
				if (map[x + 1][y] == 'e' || map[x + 1][y] == 'R'
						|| map[x + 1][y] == 'G') {
					if (checkLists(x + 1, y, openList, q, (int)r.x, (int)r.z)) {
						closeList.add(q);
						closeList.add(new ListData(x + 1, y, q));
						break;
					}
				}
			}
			if ((x - 1) != -1) {
				if (map[x - 1][y] == 'e' || map[x - 1][y] == 'R'
						|| map[x - 1][y] == 'G') {
					if (checkLists(x - 1, y, openList, q, (int)r.x, (int)r.z)) {
						closeList.add(q);
						closeList.add(new ListData(x - 1, y, q));
						break;
					}
				}
			}
			closeList.add(q);
//			System.out.println("Close List: ");
//			for (ListData d : closeList) {
//				System.out.println(d.getX() + ", " + d.getY());
//			}
//			System.out.println("Open List: ");
//			for (ListData d : openList) {
//				System.out.println(d.getX() + ", " + d.getY());
//			}
		}

	}

	
	// computes the F value of the new block
	// checks if the chosen block is not already in a list or if it is in a list but with a different F value
	public boolean checkLists(int x, int y, PriorityQueue<ListData> openList, ListData parent, int start_x, int start_y) {

		ListData successor;
		Point2i g = new Point2i((int) Math.round((goal.x - 0.5)
				+ (world_size / 2)), (int) Math.round(-(goal.z + 0.5)
				+ (world_size / 2)));
		boolean valid = true;

		successor = new ListData(x, y, parent);
		if (x == g.x && (y) == g.y) {
			System.out
					.println("You have reached the goal. Find a way to break the execution.");
			return true;
		} else {
			double f;
			f = Math.abs(x - g.x) + Math.abs((y) - g.y);
			
			//find the number of parents - distance that the robot did
			ListData it = successor;
			int count = 0;
			while(it.getParent() != null)
			{
				count++;
				it = it.getParent();
			}
			
			f += count;
			successor.setF(f);

			for (ListData d : openList) {
				if ((d.getX() == x) && (d.getY() == (y))) {
					if (d.getF() <= f) {
						valid = false;
					}
				}
			}

			if (valid) {
				for (ListData dt : closeList) {
					if ((dt.getX() == x) && (dt.getY() == (y))) {
						if (dt.getF() <= f) {
							valid = false;
						}
					}
				}
			}
			if (valid) {
				openList.add(successor);
			}

		} // mexri edw i synartisi
		return false;
	}

	
	// finds the path that the robot will eventually follow
	public void findPath() {
		path = new ArrayList<ListData>();
		ListData prev;
		path.add(closeList.get(closeList.size() - 1));

		prev = closeList.get(closeList.size() - 1);

		for (int i = closeList.size() - 2; i >= 0; i--) {
			System.out.println(i);
			ListData dt = closeList.get(i);
			if (dt.compareData(prev)) {
				path.add(dt);
				prev = closeList.get(i);
			}

		}
		//for (int i = path.size() - 1; i >= 0; i--) {
			//System.out.println(path.get(i).getX() + ", " + path.get(i).getY());
		//}
	}

	public class fValueComparator implements Comparator<ListData> {
		public int compare(ListData d0, ListData d1) {
			if (d0.getF() > d1.getF()) {
				return 1;
			} else if (d0.getF() < d1.getF()) {
				return -1;
			} else {
				return 0;
			}
		}
	}

}
