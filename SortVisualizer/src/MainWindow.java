import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class MainWindow {
	private JFrame frame;
	private SortPanel selectionSort, insertionSort, mergeSort, quickSort;
	private ArrayList<Integer> selectionList, insertionList, mergeList,
			quickList;
	private JPanel buttonPanel;
	private JEditorPane numbersPane;
	private boolean started, sorted;
	private JButton startButton;
	private JLabel instructionsLabel, delayLabel;
	private JSlider delaySlider;
	private SelectionSortThread selectionSortThread;
	private InsertionSortThread insertionSortThread;
	private int delay;

	public MainWindow() {
		delay = 500;
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
		
		selectionSortThread = new SelectionSortThread(selectionSort, delay);
		insertionSortThread = new InsertionSortThread(insertionSort, delay);

		numbersPane = new JEditorPane();
		GenericListener buttonListener = new GenericListener();
		instructionsLabel = new JLabel(
				"<html> Enter numbers that you would like to sort, separated by spaces (positive integers only). </html>");

		startButton = new JButton(" ");
		startButton.setText("Start");
		startButton
				.setToolTipText("Start sort with numbers provided in text box.");
		startButton.addActionListener(buttonListener);
		
		delaySlider = new JSlider(0,500);
		delaySlider.setMajorTickSpacing(100);
		delaySlider.setMinorTickSpacing(50);
		delaySlider.setPaintTicks(true);
		delaySlider.setPaintLabels(true);
		delaySlider.addChangeListener(new SliderListener());
		delayLabel = new JLabel("Delay = " + delaySlider.getValue() + " ms");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setBorder(new EmptyBorder(0,10,10,10));
		buttonPanel.add(instructionsLabel);
		buttonPanel.add(startButton);
		buttonPanel.add(delayLabel);
		buttonPanel.add(delaySlider);

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
	private void setDelay(int delay) {
		this.delay = delay;
		delayLabel.setText("Delay = " + delay + " ms");
		if (selectionSortThread.isAlive()){
			selectionSortThread.setDelay(delay);
		}
		if (insertionSortThread.isAlive()){
			insertionSortThread.setDelay(delay);
		}
		
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

	public boolean checkAllSorted() {
		sorted = (selectionSortThread.isSorted() && insertionSortThread.isSorted());
		return sorted;
	}

	private void start() {
		started = !started;

		if (started) {
			setValues(numbersPane.getText());
			if (selectionList.size() > 0) {
				selectionSortThread = new SelectionSortThread(selectionSort, delay);
				selectionSortThread.start();
				insertionSortThread = new InsertionSortThread(insertionSort, delay);
				insertionSortThread.start();
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

	class GenericListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == startButton) {
				start();
			}
		}
	}
	class SliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider)e.getSource();
			setDelay(source.getValue());
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
				for (int i = a; i < listSize && started; i++) {
					sp.setColor(smallestIndex, Colors.TARGET);
					sp.setIndex(i);

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

				if (a == listSize - 1) {
					sorted = true;
				} else {
					sp.setColorRange(a + 1, Colors.ACTIVE);
				}
				sp.repaint();

			}
			sp.repaint();
			if (checkAllSorted()) {
				MainWindow.this.start();
			}
		}
	}

	class InsertionSortThread extends SortThread {

		public InsertionSortThread(SortPanel sp, long msdelay) {
			super(sp, msdelay);
		}

		public void run() {
			int listSize = sp.getListSize();
			for (int i = 0; i < listSize && started; i++) {
				sp.setIndex(i);
				sp.setColorRange(0, i, Colors.ACTIVE);
				sp.setColor(i, Colors.TARGET);
				int val = sp.get(i);
				for (int j = i - 1; j >= 0 && val < sp.get(j); j--) {
					sp.swap(j, j + 1);
					sp.repaint();
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				sp.repaint();
				try {
					Thread.sleep(msdelay);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
			for (int i = 0; i < listSize && started; i++) {
				sp.setColor(i, Colors.SORTED);
				sp.repaint();
				if (i + 1 == listSize){
					sorted = true;
				}
				try {
					Thread.sleep(msdelay);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}
			if (checkAllSorted()) {
				MainWindow.this.start();
			}
		}

	}
}