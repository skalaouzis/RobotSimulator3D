

package Input;
import java.awt.Color;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.Scanner;

import javax.swing.BorderFactory;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class Input {

	
	private char[][] map;
	
	public char[][] readFileWithGraphical()
	{
		JFileChooser chooser = new JFileChooser();
		String path = "";
		// to choose with file chooser
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		int option = chooser.showOpenDialog(chooser); // parentComponent must a component like JFrame, JDialog...
		if (option == JFileChooser.APPROVE_OPTION) 
		{
		   File selectedFile = chooser.getSelectedFile();
		   path = selectedFile.getAbsolutePath();
		   System.out.println(path);
		}else {
			System.exit(1);
		}
		
		
		
	
		FileInputStream fstream = null;
		try {
			fstream = new FileInputStream(path);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
		
		String line;
		
		int[] dimensions = new int[2];
		//String[][] map;
		try
		{
			
			line = br.readLine();
			String[] temp = line.split(" ");
			
			for(int i = 0; i < 2; i++)
			{
				dimensions[i] = Integer.parseInt(temp[i]);
			}
			//map = new String[dimensions[0]][dimensions[1]];
			map = new char[dimensions[0]][dimensions[1]];
			
			for(int i = 0; i < dimensions[0]; i++)
			{
				line = br.readLine();
				
				//temp = line.split("");
				for(int j = 0; j < dimensions[1]; j++)
				{
					//map[i][j] = temp[j + 1];
					map[i][j] = line.charAt(j);
				}
			}
			
			for(int i = 0; i < dimensions[0]; i++)
			{
				for(int j = 0; j < dimensions[1]; j++)
				{
					System.out.print(map[i][j]);
				}
				System.out.println();
			}
		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return map;
		
	}
	
	
	
	private int input;
    private JSpinner inputField;
	
	private JPanel getPanel()
    {
        JPanel basePanel = new JPanel();
        //basePanel.setLayout(new BorderLayout(5, 5));
        basePanel.setOpaque(true);
        basePanel.setBackground(Color.BLUE.darker());

        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(3, 2, 5, 5));
        centerPanel.setBorder(
            BorderFactory.createEmptyBorder(5, 5, 5, 5));
        centerPanel.setOpaque(true);
        centerPanel.setBackground(Color.WHITE);

        JLabel mLabel1 = new JLabel("Enter world size : ");


        inputField = new JSpinner();

        inputField.setModel(new SpinnerNumberModel(10, 0, 100, 1));
        

        centerPanel.add(mLabel1);
        centerPanel.add(inputField);


        basePanel.add(centerPanel);

        return basePanel;
    }
	
	
	
	public char[][] createRandomFile()
	{

		try {
			
			int selection = JOptionPane.showConfirmDialog(
	                null, getPanel(), "Input Form : "
	                                , JOptionPane.OK_CANCEL_OPTION
	                                , JOptionPane.PLAIN_MESSAGE);

	        if (selection == JOptionPane.OK_OPTION) 
	        {

	               input = (Integer)inputField.getValue();
	               System.out.println(input);           

	            
	        }
	        else
	        {
	            System.exit(1);
	        }

			
			PrintWriter writer = new PrintWriter(System.getProperty("user.dir")+"/ranbot_input.txt", "UTF-8");
			
			Scanner scan = new Scanner(System.in);
			int dimensions = input;


			
			map = new char[dimensions][dimensions];
			
			Random rand = new Random();
			String charSet = "eXee";
			for(int i = 0; i < dimensions; i++)
			{
				for(int j = 0; j < dimensions; j++)
				{
					int randomChar = rand.nextInt(charSet.length());
					map[i][j] = charSet.charAt(randomChar);
				}
			}
			
			charSet = "DGR";
			for(int i = 0; i < 3; i++)
			{
				int x = rand.nextInt(dimensions);
				int y = rand.nextInt(dimensions);
				
				System.out.println(x + ", " + y);
				map[x][y] = charSet.charAt(i);
			}

			
			
			scan.close();
			writer.close();
			
			for(int i = 0; i < dimensions; i++)
			{
				for(int j = 0; j < dimensions; j++)
				{
					System.out.print(map[i][j]);
				}
				System.out.println();
			}
			
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
			return map;
		
	}
	
	
	
}
