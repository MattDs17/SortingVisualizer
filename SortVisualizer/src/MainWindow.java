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
	private boolean started, paused, sorted;
	private JButton startButton, pauseButton, sortedPresetButton,
			reversedPresetButton, randomPresetButton;
	private JLabel instructionsLabel, delayLabel;
	private JSlider delaySlider;
	private SelectionSortThread selectionSortThread;
	private InsertionSortThread insertionSortThread;
	private MergeSortThread mergeSortThread;
	private QuickSortThread quickSortThread;
	private int delay;

	public MainWindow() {
		delay = 500;
		started = false;
		paused = false;

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
		mergeSortThread = new MergeSortThread(mergeSort, delay);
		quickSortThread = new QuickSortThread(quickSort, delay);

		numbersPane = new JEditorPane();
		ButtonListener buttonListener = new ButtonListener();
		instructionsLabel = new JLabel(
				"<html> Enter numbers that you would like to sort, separated by spaces (positive integers only). </html>");

		startButton = new JButton(" ");
		startButton.setText("Start");
		startButton
				.setToolTipText("Start sort with numbers provided in text box.");
		startButton.addActionListener(buttonListener);
		pauseButton = new JButton("Pause");
		pauseButton.setToolTipText("Pause the current sort.");
		pauseButton.setVisible(false);
		pauseButton.addActionListener(buttonListener);
		sortedPresetButton = new JButton("Use sorted list");
		sortedPresetButton.addActionListener(buttonListener);
		reversedPresetButton = new JButton("Use reversed list");
		reversedPresetButton.addActionListener(buttonListener);
		randomPresetButton = new JButton("Use randomized list");
		randomPresetButton.addActionListener(buttonListener);

		delaySlider = new JSlider(0, 500);
		delaySlider.setMajorTickSpacing(100);
		delaySlider.setMinorTickSpacing(50);
		delaySlider.setPaintTicks(true);
		delaySlider.setPaintLabels(true);
		delaySlider.addChangeListener(new SliderListener());
		delayLabel = new JLabel("Delay = " + delaySlider.getValue() + " ms");
		buttonPanel = new JPanel();
		buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.PAGE_AXIS));
		buttonPanel.setBorder(new EmptyBorder(0, 10, 10, 10));
		buttonPanel.add(instructionsLabel);
		buttonPanel.add(startButton);
		buttonPanel.add(pauseButton);
		buttonPanel.add(delayLabel);
		buttonPanel.add(delaySlider);
		buttonPanel.add(sortedPresetButton);
		buttonPanel.add(reversedPresetButton);
		buttonPanel.add(randomPresetButton);

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
		if (selectionSortThread.isAlive()) {
			selectionSortThread.setDelay(delay);
		}
		if (insertionSortThread.isAlive()) {
			insertionSortThread.setDelay(delay);
		}
		if (mergeSortThread.isAlive()) {
			mergeSortThread.setDelay(delay);
		}
		if (quickSortThread.isAlive()) {
			quickSortThread.setDelay(delay);
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

	private void fillSorted() {
		if (checkAllSorted() || !started) {
			String s = "";
			for (int i = 1; i <= 35; i++) {
				s += i + " ";
			}
			numbersPane.setText(s);
			setValues(s);
		}
	}

	private void fillReversed() {
		if (checkAllSorted() || !started) {
			String s = "";
			for (int i = 35; i >= 1; i--) {
				s += i + " ";
			}
			numbersPane.setText(s);
			setValues(s);
		}
	}

	private void fillRandomized() {
		if (checkAllSorted() || !started) {
			String s = "";
			for (int i = 1; i <= 35; i++) {
				s += Math.round(Math.random() * 35) + " ";
			}
			numbersPane.setText(s);
			setValues(s);
		}
	}

	public boolean hasNums(String str) {
		String[] numArray = str.split(" ");
		for (String s : numArray) {
			if (s.matches("^[0-9]*$") && s.length() > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean isStarted() {
		return started;
	}

	public boolean checkAllSorted() {
		sorted = (selectionSortThread.isSorted()
				&& insertionSortThread.isSorted() && mergeSortThread.isSorted() && quickSortThread
				.isSorted());
		return sorted;
	}

	private void start() {
		if (hasNums(numbersPane.getText())) {
			started = !started;
			if (started) {
				if (paused) {
					pause();
				}
				pauseButton.setVisible(true);
				setValues(numbersPane.getText());
				if (selectionList.size() > 0) {
					selectionSortThread = new SelectionSortThread(
							selectionSort, delay);
					selectionSortThread.start();
					insertionSortThread = new InsertionSortThread(
							insertionSort, delay);
					insertionSortThread.start();
					mergeSortThread = new MergeSortThread(mergeSort, delay);
					mergeSortThread.start();
					quickSortThread = new QuickSortThread(quickSort, delay);
					quickSortThread.start();
					frame.repaint();
					startButton.setText("Stop");
					startButton.setToolTipText("Stop sort.");
				}
			} else {
				if (paused) {
					pause();
				}
				pauseButton.setVisible(false);
				startButton.setText("Start");
				startButton
						.setToolTipText("Start sort with numbers provided in text box.");
			}
		}
	}

	private void pause() {
		paused = !paused;

		if (paused) {
			startButton.setVisible(false);
			pauseButton.setText("Unpause");
			pauseButton.setToolTipText("Unpause the current sort.");
		} else {
			startButton.setVisible(true);
			pauseButton.setText("Pause");
			pauseButton.setToolTipText("Pause the current sort.");
		}
	}

	public static void main(String[] args) {
		MainWindow mw = new MainWindow();
	}

	class ButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == startButton) {
				start();
			} else if (event.getSource() == pauseButton) {
				pause();
			} else if (event.getSource() == sortedPresetButton) {
				fillSorted();
			} else if (event.getSource() == reversedPresetButton) {
				fillReversed();
			} else if (event.getSource() == randomPresetButton) {
				fillRandomized();
			}
		}
	}

	class SliderListener implements ChangeListener {

		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
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
				while (paused) {
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				smallestIndex = a;
				sp.setLine(sp.get(smallestIndex));
				sp.setMessage("Searching remaining list for smallest element. Smallest found: " + sp.get(smallestIndex));
				for (int i = a; i < listSize && started; i++) {
					while (paused) {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					sp.setColor(smallestIndex, Colors.TARGET);
					sp.setIndex(i);

					if (sp.get(i) < sp.get(smallestIndex)) {
						sp.setColor(i, Colors.TARGET);
						sp.setColor(smallestIndex, Colors.ACTIVE);
						smallestIndex = i;
						sp.setLine(sp.get(smallestIndex));
						sp.setMessage("Searching remaining list for smallest element. Smallest found: " + sp.get(smallestIndex) + ".");
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
					sp.setLine(0);
					sp.setMessage("Sorted!");
				} else {
					sp.setColorRange(a + 1, Colors.ACTIVE);
				}
				sp.repaint();

			}
			sp.repaint();
			if (checkAllSorted() && started) {
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
				sp.setLine(val);
				sp.setMessage("Finding location where previous numbers < " + val + " < following numbers.");
				for (int j = i - 1; j >= 0 && val < sp.get(j); j--) {
					while (paused) {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					sp.swap(j, j + 1);
					sp.repaint();					
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}

				}
				sp.repaint();
				if (i == listSize - 1 && started) {
					sorted = true;
					sp.setColorRange(0, Colors.SORTED);
					sp.setLine(0);
					sp.setMessage("Sorted!");
					sp.repaint();
				}
				try {
					Thread.sleep(msdelay);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}

			if (checkAllSorted() && started) {
				MainWindow.this.start();
			}
		}

	}

	class MergeSortThread extends SortThread {

		public MergeSortThread(SortPanel sp, long msdelay) {
			super(sp, msdelay);
			sp.setIndex(-1);
		}

		public void mergeSort(ArrayList<Integer> nums, int a, int b) {
			if (started) {
				while (paused) {
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				sp.setColorRange(0, Colors.INACTIVE);

				if (b > a + 1) {
					sp.setColorRange(a, (a + b) / 2, Colors.LOWER);
					sp.setColorRange((a + b) / 2, b, Colors.UPPER);
					sp.repaint();
					sp.setMessage("Dividing list from index " + a + " to " + (b-1) + " in half.");
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
					mergeSort(nums, a, (a + b) / 2);
					mergeSort(nums, (a + b) / 2, b);
					merge(nums, a, (a + b) / 2, b);
				}
			}
		}

		public void merge(ArrayList<Integer> nums, int a, int mid, int b) {
			if (started) {
				int[] lower = new int[mid - a];
				int[] upper = new int[b - mid];
				sp.setColorRange(0, Colors.INACTIVE);
				sp.setColorRange(a, mid, Colors.LOWER);
				sp.setColorRange(mid, b, Colors.UPPER);
				sp.repaint();
				sp.setMessage("Merging values from index " + a + " to " + (b-1) + " in order.");
				try {
					Thread.sleep(msdelay);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				int index = a;
				int i;
				int j;
				for (i = 0; index < mid; i++, index++) {
					lower[i] = nums.get(index);
				}
				for (j = 0; index < b; j++, index++) {
					upper[j] = nums.get(index);
				}
				i = 0;
				j = 0;
				index = a;
				while (i < lower.length && j < upper.length) {
					while (paused) {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (lower[i] < upper[j]) {
						nums.set(index, lower[i]);
						sp.setLine(lower[i]);
						i++;
						sp.setColor(index, Colors.LOWER);
					} else {
						nums.set(index, upper[j]);
						sp.setLine(upper[j]);
						j++;
						sp.setColor(index, Colors.UPPER);
					}
					index++;
					sp.repaint();
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				while (i < lower.length) {
					while (paused) {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					nums.set(index, lower[i]);
					sp.setLine(lower[i]);
					sp.setColor(index, Colors.LOWER);					
					i++;
					index++;
					sp.repaint();
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				while (j < upper.length) {
					while (paused) {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					nums.set(index, upper[j]);
					sp.setLine(upper[j]);
					sp.setColor(index, Colors.UPPER);
					j++;
					index++;
					sp.repaint();
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
			}
		}

		public void run() {
			mergeSort(list, 0, list.size());
			if (started) {
				sorted = true;
				sp.setLine(0);
				sp.setMessage("Sorted!");
				sp.setColorRange(0, Colors.SORTED);
				sp.repaint();
			}

			if (checkAllSorted() && started) {
				MainWindow.this.start();
			}
		}

	}

	class QuickSortThread extends SortThread {

		public QuickSortThread(SortPanel sp, long msdelay) {
			super(sp, msdelay);
			sp.setIndex(-1);
		}

		public int partition(ArrayList<Integer> nums, int a, int b) {
			if (started) {
				int pivot = nums.get(b);
				sp.setLine(pivot);
				
				sp.setColorRange(a,b,Colors.ACTIVE);
				sp.setColor(b, Colors.TARGET);
				int greater = a;
				sp.setMessage("Moving elements before/after index " + greater + " if they are < or > " + pivot + ".");
				for (int i = a; i < b && started; i++) {
					sp.setIndex(i);
					while (paused) {
						try {
							Thread.sleep(10);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					if (nums.get(i) < pivot) {
						sp.setColor(i,Colors.LOWER);
						sp.swap(i, greater);
						greater++;
						sp.setMessage("Moving elements before/after index " + greater + " if they are < or > " + pivot + ".");
					} else {
						sp.setColor(i,Colors.UPPER);
					}
					sp.repaint();
					try {
						Thread.sleep(msdelay);
					} catch (Exception ex) {
						ex.printStackTrace();
					}
				}
				sp.swap(greater, b);
				sp.repaint();
				try {
					Thread.sleep(msdelay);
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			
			return greater;
			}
			return -1;
		}

		public void quickSort(ArrayList<Integer> nums, int a, int b) {
			sp.setColorRange(0, Colors.INACTIVE);
			sp.repaint();
			if (a < b + 1 && started) {
				int pivot = partition(nums, a, b);
				quickSort(nums, a, pivot - 1);
				quickSort(nums, pivot + 1, b);
			}
		}

		public void run() {
			quickSort(list, 0, list.size() - 1);
			if (started) {
				sorted = true;
				sp.setLine(0);
				sp.setMessage("Sorted!");
				sp.setColorRange(0, Colors.SORTED);
				sp.setIndex(-1);
				sp.repaint();
			}

			if (checkAllSorted() && started) {
				MainWindow.this.start();
			}
		}
	}
}