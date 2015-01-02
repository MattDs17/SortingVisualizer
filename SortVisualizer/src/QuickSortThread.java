import java.util.ArrayList;


// Fourth sort thread. Runs a quick sort algorithm with the rightmost element as pivot.
class QuickSortThread extends SortThread {

	/**
	 * 
	 */
	private final MainWindow mainWindow;

	public QuickSortThread(MainWindow mainWindow, SortPanel sp, long msdelay) {
		super(sp, msdelay);
		this.mainWindow = mainWindow;
		sp.setIndex(-1);
	}
	public int partition(ArrayList<Integer> nums, int a, int b) {
		if (this.mainWindow.started) {
			int pivot = nums.get(b);
			sp.setLine(pivot);

			sp.setColorRange(a, b, Colors.ACTIVE);
			sp.setColor(b, Colors.TARGET);
			int greater = a;
			sp.setMessage("Moving elements before/after index " + greater
					+ " if they are < or > " + pivot + ".");
			for (int i = a; i < b && this.mainWindow.started; i++) {
				sp.setIndex(i);
				while (this.mainWindow.paused) {
					try {
						Thread.sleep(10);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				if (nums.get(i) < pivot) {
					sp.setColor(i, Colors.LOWER);
					sp.swap(i, greater);
					greater++;
					sp.setMessage("Moving elements before/after index "
							+ greater + " if they are < or > " + pivot
							+ ".");
				} else {
					sp.setColor(i, Colors.UPPER);
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
		if (a < b + 1 && this.mainWindow.started) {
			int pivot = partition(nums, a, b);
			quickSort(nums, a, pivot - 1);
			quickSort(nums, pivot + 1, b);
		}
	}

	public void run() {
		quickSort(list, 0, list.size() - 1);
		if (this.mainWindow.started) {
			sorted = true;
			sp.setLine(0);
			sp.setMessage("Sorted!");
			sp.setColorRange(0, Colors.SORTED);
			sp.setIndex(-1);
			sp.repaint();
		}

		if (this.mainWindow.checkAllSorted() && this.mainWindow.started) {
			this.mainWindow.start();
		}
	}
}