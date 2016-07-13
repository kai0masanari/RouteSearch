

import javax.swing.JFrame;
import javax.swing.JPanel;

public class MazeViewer extends JFrame{

	private Maze maze;

	public MazeViewer(Maze maze){

		this.maze = maze;
		int[][] block = maze.getMazeBlock();

		for(int i = 0, x = 1, y = 10; i < maze.getPath().length(); i++){



			char p = maze.getPath().charAt(i);
			switch(p){
			case 'u':
				if(i != 0) block[y][x] = 5;
				y -= 1;
				break;
			case 'r':
				if(i != 0) block[y][x] = 6;
				x += 1;
				break;
			case 'd':
				if(i != 0) block[y][x] = 7;
				y += 1;
				break;
			case 'l':
				if(i != 0) block[y][x] = 8;
				x -= 1;
				break;
			}
		}

		JPanel mazePanel = new JPanel();
		mazePanel.setBounds(0, 0, 600, 600);

		BlockPanel[][] mazeBlockPanel = new BlockPanel[12][12];

		for(int i = 0; i < 12; i++){
			for(int j = 0; j < 12; j++){
				mazeBlockPanel[i][j] = new BlockPanel(block[i][j]);
				mazeBlockPanel[i][j].setBounds(40 * j + 40, 40 * i + 40, 40, 40);
				mazePanel.add(mazeBlockPanel[i][j]);
				this.getContentPane().add(mazeBlockPanel[i][j]);
			}
		}

		this.getContentPane().add(mazePanel);

		this.setBounds(200, 200, 600, 600);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
