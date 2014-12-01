import java.util.ArrayList;

import javax.swing.*;
public class MainWindow {
	JFrame frame;
	sortPanel selectionSort;
	public MainWindow() {
		frame = new JFrame("Sorting Algorithms Visualizer");
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(5);
		list.add(83);
		list.add(15);
		list.add(23);
		list.add(100);
		list.add(53);
		list.add(29);
		selectionSort = new sortPanel(list);
		frame.setSize(500, 500);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(selectionSort);
		
		frame.setVisible(true);
	}
	public static void main(String[] args){
		MainWindow m = new MainWindow();
	}
}
