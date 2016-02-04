package com.algorithms.sortingvisualizer;

import com.algorithms.sortingvisualizer.view.SortPanel;

import javax.swing.*;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public abstract class SortThread extends Thread {

	protected long msdelay;
	protected ArrayList<Integer> list;
	protected SortPanel sp;
	protected boolean sorted;

	public SortThread(SortPanel sp, long msdelay) {
		this.sp = sp;
		this.list = sp.getList();
		this.msdelay = msdelay;
		sp.setIndex(0);
		sorted = false;
	}

	/** Used by MainWindow to update delay when changed */
	public void setDelay(int msdelay) {
		this.msdelay = msdelay;
	}

	/** Returns whether or not the list has finished being sorted */
	public boolean isSorted() {
		return sorted;
	}
	
	public void stopThread() {
		currentThread().interrupt();
	}
	
	protected void sleepThread(long msdelay) {
		try {
			Thread.sleep(msdelay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	protected void repaint() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
				@Override
				public void run() {
					sp.repaint();
				}
			});
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
