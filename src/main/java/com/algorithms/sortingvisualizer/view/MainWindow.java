package com.algorithms.sortingvisualizer.view;

import com.algorithms.sortingvisualizer.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class MainWindow {
	private JFrame frame;
	private JButton startButton, pauseButton, sortedPresetButton,
			reversedPresetButton, randomPresetButton;
	private JLabel instructionsLabel, delayLabel;
	private JSlider delaySlider;
	private JPanel buttonPanel;
	private JEditorPane numbersPane;

	private ArrayList<Integer> selectionList, insertionList, mergeList,
			quickList;

	private SortPanel selectionSortPanel, insertionSortPanel, mergeSortPanel, quickSortPanel;
	private ArrayList<SortPanel> sortPanels = new ArrayList<>();

	private SelectionSortThread selectionSortThread;
	private InsertionSortThread insertionSortThread;
	private MergeSortThread mergeSortThread;
	private QuickSortThread quickSortThread;
	private ArrayList<SortThread> sortThreads = new ArrayList<>();

	private State state = State.STOPPED;
	private int delay;

	public MainWindow() {
		delay = 500;

		frame = new JFrame("Sorting Algorithms Visualizer");
		frame.setLayout(new GridLayout(3, 2, 10, 10));
		frame.setLocation(5, 5);

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

		
		selectionList = new ArrayList<>();
		insertionList = new ArrayList<>();
		mergeList = new ArrayList<>();
		quickList = new ArrayList<>();

		selectionSortPanel = new SortPanel("Selection Sort");
		selectionSortPanel.setList(selectionList);
		sortPanels.add(selectionSortPanel);

		insertionSortPanel = new SortPanel("Insertion Sort");
		insertionSortPanel.setList(insertionList);
		sortPanels.add(insertionSortPanel);

		mergeSortPanel = new SortPanel("Merge Sort");
		mergeSortPanel.setList(mergeList);
		sortPanels.add(mergeSortPanel);

		quickSortPanel = new SortPanel("Quick Sort");
		quickSortPanel.setList(quickList);
		sortPanels.add(quickSortPanel);

		selectionSortThread = new SelectionSortThread(this, selectionSortPanel, delay);
		sortThreads.add(selectionSortThread);
		insertionSortThread = new InsertionSortThread(this, insertionSortPanel,	delay);
		sortThreads.add(insertionSortThread);
		mergeSortThread = new MergeSortThread(this, mergeSortPanel, delay);
		sortThreads.add(mergeSortThread);
		quickSortThread = new QuickSortThread(this, quickSortPanel, delay);
		sortThreads.add(quickSortThread);


		frame.setSize(800, 800);
		frame.setResizable(false);
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.add(selectionSortPanel);
		frame.add(insertionSortPanel);
		frame.add(mergeSortPanel);
		frame.add(quickSortPanel);
		frame.add(buttonPanel);
		frame.add(numbersPane);
	}

	public void startDisplay() {
		frame.setVisible(true);
	}

	/** Sets delay between sort actions */
	private void setDelay(int delay) {
		this.delay = delay;
		delayLabel.setText("Delay = " + delay + " ms");
		for (SortThread sortThread : sortThreads) {
            if (sortThread.isAlive()) {
                sortThread.setDelay(delay);
            }
        }
	}

	/** Fills the JEditorPane with numbers 1 to 35 */
	private void fillSorted() {
		if (checkAllSorted() || isPaused() || isStopped()) {
			String s = "";
			for (int i = 1; i <= 35; i++) {
				s += i + " ";
			}
			numbersPane.setText(s);
		}
	}

	/** Fills the JEditorPane with numbers 35 to 1 */
	private void fillReversed() {
		if (checkAllSorted() || isPaused() || isStopped()) {
			String s = "";
			for (int i = 35; i >= 1; i--) {
				s += i + " ";
			}
			numbersPane.setText(s);
		}
	}

	/** Fills the JEditorPane with numbers randomly from 1 to 35 */
	private void fillRandomized() {
		if (checkAllSorted() || isPaused() || isStopped()) {
			String s = "";
			for (int i = 1; i <= 35; i++) {
				s += Math.round(Math.random() * 35 + 1) + " ";
			}
			numbersPane.setText(s);
		}
	}

	/** Checks if there are actual numbers entered into the JEditorPane */
	public boolean hasNums(String str) {
		String[] numArray = str.split(" ");
		for (String s : numArray) {
			if (s.matches("^[0-9]*$") && s.length() > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Checks if all four lists have given confirmation that they have been
	 * sorted
	 */
	public boolean checkAllSorted() {
		return selectionSortThread.isSorted() && insertionSortThread.isSorted()
				&& mergeSortThread.isSorted() && quickSortThread.isSorted();
	}

	/**
	 * Works as a toggle, initializing a new sort or terminating an ongoing
	 * sort.
	 */
	void start() {
		if (!hasNums(numbersPane.getText()))
			return;
		
		state = (state == State.PAUSED || state == State.STOPPED) ? State.STARTED
				: State.STOPPED;
		if (!isStarted())
			return;
		
		sortThreads.clear();
		pauseButton.setVisible(true);

        ArrayList<Integer> listOfNumbers = getListOfNumbersFromPane(numbersPane.getText());
        for (SortPanel sortPanel : sortPanels) {
            sortPanel.assignNewListToSort(listOfNumbers);
            sortPanel.setNewColors();
        }

		if (listOfNumbers.size() > 0) {
			selectionSortThread = new SelectionSortThread(this,
                    selectionSortPanel, delay);
			sortThreads.add(selectionSortThread);

			insertionSortThread = new InsertionSortThread(this,
                    insertionSortPanel, delay);
			sortThreads.add(insertionSortThread);

			mergeSortThread = new MergeSortThread(this, mergeSortPanel, delay);
			sortThreads.add(mergeSortThread);

			quickSortThread = new QuickSortThread(this, quickSortPanel, delay);
			sortThreads.add(quickSortThread);

			startButton.setText("Stop");
			startButton.setToolTipText("Stop sort.");
			frame.repaint();

            // Start all the Sort Threads viz. Selection, Insertion, Merge and Quick sort threads.
            for (SortThread sortThread : sortThreads) {
                sortThread.start();
            }
		}
	}

    private ArrayList<Integer> getListOfNumbersFromPane(String nums) {
        String[] numArray = nums.split(" ");
        ArrayList<Integer> numbersToSort = new ArrayList<>(numArray.length);

        for (String numberString: numArray) {
            if (numberString.matches("^[0-9]*$") && numberString.length() > 0) {
                numbersToSort.add(Integer.parseInt(numberString));
            }
        }

        return numbersToSort;
    }

    /** Works as a toggle, pausing the threads*/
	private void pause() {
		state = (state == State.STARTED || state == State.STOPPED) ? State.PAUSED
				: State.STARTED;
		if (isPaused()) {
			startButton.setVisible(false);
			pauseButton.setText("Unpause");
			pauseButton.setToolTipText("Unpause the current sort.");
		} else if (isStarted()) {
			startButton.setVisible(true);
			pauseButton.setText("Pause");
			pauseButton.setToolTipText("Pause the current sort.");
		}
	}
	
	public void stop() {
		state = (state == State.STARTED || state == State.PAUSED) ? State.STOPPED
				: State.STARTED;
		if (!isStopped())
			return;
		
		for (SortPanel sortPanel : sortPanels) {
			sortPanel.clearTheListOfNumbersToSort();
		}
		
		for (SortThread sortThread : sortThreads) {
			if (sortThread.isAlive()) {
                sortThread.stopThread();
            }
		}
		pauseButton.setVisible(false);
		
		startButton.setText("Start");
		startButton
				.setToolTipText("Start sort with numbers provided in text box.");
		
		sortThreads.clear();
	}

	/** Interprets button events */
	private class ButtonListener implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent event) {
			if (event.getSource() == startButton) {
				if (startButton.getText().equalsIgnoreCase("Start"))
					start();
				else
					stop();
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

	/** Modifies delay value based on slider location */
	private class SliderListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			JSlider source = (JSlider) e.getSource();
			setDelay(source.getValue());
		}
	}

	public enum State {
		STARTED, PAUSED, STOPPED;
	}
	
	public boolean isStarted() {
		return state == State.STARTED;
	}
	
	public boolean isPaused() {
		return state == State.PAUSED;
	}
	
	public boolean isStopped() {
		return state == State.STOPPED;
	}

}