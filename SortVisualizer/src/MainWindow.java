import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

public class MainWindow {
	private JFrame frame;
	private sortPanel selectionSort, insertionSort, mergeSort, quickSort;
	private JPanel buttonPanel;
	private JEditorPane numbersPane;
	private boolean started;
	private JButton setValuesButton, startButton;
	private JLabel instructionsLabel;
	private ArrayList<Integer> numList;
	
	public MainWindow() {
		started = false;	
		numList = new ArrayList<Integer>();
		frame = new JFrame("Sorting Algorithms Visualizer");
		frame.setLayout(new GridLayout(3,2,10,10));
		frame.setLocation(5,5);
		
		selectionSort = new sortPanel("Selection Sort");
		insertionSort = new sortPanel("Insertion Sort");
		mergeSort = new sortPanel("Merge Sort");
		quickSort = new sortPanel("Quick Sort");
		
		numbersPane = new JEditorPane();
		
		ButtonListener buttonListener = new ButtonListener();
		instructionsLabel = new JLabel("<html>Enter numbers that you would like to sort, separated by spaces (positive integers only).</html>");
		setValuesButton = new JButton("Set Values");
		setValuesButton.setToolTipText("Use the entered values for sorting");
		setValuesButton.addActionListener(buttonListener);
		startButton = new JButton(" ");
		startButton.setText("Start");
		startButton.setToolTipText("Start sort");
		startButton.addActionListener(buttonListener);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.PAGE_AXIS));
		buttonPanel.add(instructionsLabel);
		buttonPanel.add(setValuesButton);
		buttonPanel.add(startButton);
		
		frame.setSize(800, 800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(selectionSort);
		frame.add(insertionSort);
		frame.add(mergeSort);
		frame.add(quickSort);
		frame.add(buttonPanel);
		frame.add(numbersPane);
		frame.setVisible(true);
	}
	public void setValues(String nums){
		String[] numArray = nums.split(" ");
		numList.clear();
		for(String s : numArray) {
			if (s.matches("^[0-9]*$")){
				numList.add(Integer.parseInt(s));
			}
		}
		selectionSort.setList(numList);
		insertionSort.setList(numList);
		mergeSort.setList(numList);
		quickSort.setList(numList);
	}
	public boolean isStarted(){
		return started;
	}
	public void start(){
		started = !started;
		if (started){
			startButton.setText("Stop");
			startButton.setToolTipText("Stop sort");
		} else {
			startButton.setText("Start");
			startButton.setToolTipText("Start/Resume sort");
		}
	}

	public static void main(String[] args) throws InterruptedException {
		MainWindow m = new MainWindow();
	}
	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == setValuesButton){
				setValues(numbersPane.getText());
				frame.repaint();
			} else if (event.getSource() == startButton){
				start();
			}
		}
		
	}

}