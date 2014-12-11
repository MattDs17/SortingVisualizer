import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;

public class MainWindow {
	private JFrame frame;
	private SortPanel selectionSort, insertionSort, mergeSort, quickSort;
	private ArrayList<Integer> selectionList, insertionList, mergeList,
			quickList;
	private JPanel buttonPanel;
	private JEditorPane numbersPane;
	private boolean started;
	private JButton startButton;
	private JLabel instructionsLabel;
	private SelectionSortThread selectionSortThread;

	public MainWindow() {
		started = false;

		frame = new JFrame("Sorting Algorithms Visualizer");
		frame.setLayout(new GridLayout(3, 2, 10, 10));
		frame.setLocation(5, 5);

		selectionList = new ArrayList<Integer>();
		insertionList = new ArrayList<Integer>();
		mergeList = new ArrayList<Integer>();
		quickList = new ArrayList<Integer>();

		selectionSort = new SortPanel("Selection Sort");
		selectionSort.setList(selectionList);
		insertionSort = new SortPanel("Insertion Sort");
		insertionSort.setList(insertionList);
		mergeSort = new SortPanel("Merge Sort");
		mergeSort.setList(mergeList);
		quickSort = new SortPanel("Quick Sort");
		quickSort.setList(quickList);

		numbersPane = new JEditorPane();
		ButtonListener buttonListener = new ButtonListener();
		instructionsLabel = new JLabel(
				"<html> Enter numbers that you would like to sort, separated by spaces (positive integers only). </html>");

		startButton = new JButton(" ");
		startButton.setText("Start");
		startButton
				.setToolTipText("Start sort with numbers provided in text box.");
		startButton.addActionListener(buttonListener);
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.add(instructionsLabel);
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

	private void setValues(String nums) {
		String[] numArray = nums.split(" ");
		selectionList.clear();
		insertionList.clear();
		mergeList.clear();
		quickList.clear();
		for (String s : numArray) {
			if (s.matches("^[0-9]*$") && s.length() > 0) {
				int num = Integer.parseInt(s);
				selectionList.add(num);
				insertionList.add(num);
				mergeList.add(num);
				quickList.add(num);
			}
			selectionSort.setColorRange(0, selectionList.size(), Colors.ACTIVE);
			insertionSort.setColorRange(0, insertionList.size(), Colors.ACTIVE);
			mergeSort.setColorRange(0, mergeList.size(), Colors.ACTIVE);
			quickSort.setColorRange(0, quickList.size(), Colors.ACTIVE);
		}
	}

	public boolean isStarted() {
		return started;
	}

	private void start() {
		started = !started;

		if (started) {
			setValues(numbersPane.getText());
			if (selectionList.size() > 0) {
				selectionSortThread = new SelectionSortThread(selectionSort, 500);
				selectionSortThread.start();
				frame.repaint();
				startButton.setText("Stop");
				startButton.setToolTipText("Stop sort.");
			}
		} else {
			startButton.setText("Start");
			startButton
					.setToolTipText("Start sort with numbers provided in text box.");
		}
	}

	public static void main(String[] args) {
		MainWindow mw = new MainWindow();
	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == startButton) {
				start();
			}
		}
	}

	class SelectionSortThread extends SortThread {
		public SelectionSortThread(SortPanel sp, long msdelay) {
			super(sp, msdelay);
		}

		public void run() {
			int smallestIndex;
			int listSize = sp.getListSize();
			for (int a = 0; a < listSize && started; a++) {
				smallestIndex = a;
				for (int i = a; i < listSize; i++) {
					sp.setColor(smallestIndex, Colors.TARGET);
					sp.setIndex(i);
					//sp.repaint();
					
					if (sp.get(i) < sp.get(smallestIndex)) {
						sp.setColor(i, Colors.TARGET);
						sp.setColor(smallestIndex, Colors.ACTIVE);
						smallestIndex = i;
					}
					sp.repaint();
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				sp.swap(a, smallestIndex);
				sp.setColor(a, Colors.SORTED);
				
				if (a == listSize-1){
					sorted = true;
				} else{
					sp.setColorRange(a+1, Colors.ACTIVE);
				}
				sp.repaint();
				
			}
			System.out.println("exiting!" + started);
			sp.repaint();
			if (sorted) {
				MainWindow.this.start();
			}
		}
	}

}