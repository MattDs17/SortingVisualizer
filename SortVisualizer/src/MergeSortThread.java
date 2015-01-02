import java.util.ArrayList;


/** Runs a merge sort algorithm. */
public class MergeSortThread extends SortThread {

	private final MainWindow mainWindow;

	public MergeSortThread(MainWindow mainWindow, SortPanel sp, long msdelay) {
		super(sp, msdelay);
		this.mainWindow = mainWindow;
		sp.setIndex(-1);
	}
	
	/** Division and joining */
	public void mergeSort(ArrayList<Integer> nums, int a, int b) {
		if (this.mainWindow.started) {
			while (this.mainWindow.paused) {
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
				sp.setMessage("Dividing list from index " + a + " to "
						+ (b - 1) + " in half.");
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

	/** Merging */
	public void merge(ArrayList<Integer> nums, int a, int mid, int b) {
		if (this.mainWindow.started) {
			int[] lower = new int[mid - a];
			int[] upper = new int[b - mid];
			sp.setColorRange(0, Colors.INACTIVE);
			sp.setColorRange(a, mid, Colors.LOWER);
			sp.setColorRange(mid, b, Colors.UPPER);
			sp.repaint();
			sp.setMessage("Merging values from index " + a + " to "
					+ (b - 1) + " in order.");
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
				while (this.mainWindow.paused) {
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
				while (this.mainWindow.paused) {
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
				while (this.mainWindow.paused) {
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
		if (this.mainWindow.started) {
			sorted = true;
			sp.setLine(0);
			sp.setMessage("Sorted!");
			sp.setColorRange(0, Colors.SORTED);
			sp.repaint();
		}

		if (this.mainWindow.checkAllSorted() && this.mainWindow.started) {
			this.mainWindow.start();
		}
	}

}