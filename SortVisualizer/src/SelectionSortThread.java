
// First sort thread. Runs a selection sort algorithm.
class SelectionSortThread extends SortThread {
	/**
	 * 
	 */
	private final MainWindow mainWindow;

	public SelectionSortThread(MainWindow mainWindow, SortPanel sp, long msdelay) {
		super(sp, msdelay);
		this.mainWindow = mainWindow;
	}

	public void run() {
		int smallestIndex;
		int listSize = sp.getListSize();

		for (int a = 0; a < listSize && this.mainWindow.started; a++) {
			while (this.mainWindow.paused) {
				try {
					Thread.sleep(10);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			smallestIndex = a;
			sp.setLine(sp.get(smallestIndex));
			sp.setMessage("Searching remaining list for smallest element. Smallest found: "
					+ sp.get(smallestIndex));
			for (int i = a; i < listSize && this.mainWindow.started; i++) {
				while (this.mainWindow.paused) {
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
					sp.setMessage("Searching remaining list for smallest element. Smallest found: "
							+ sp.get(smallestIndex) + ".");
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
		if (this.mainWindow.checkAllSorted() && this.mainWindow.started) {
			this.mainWindow.start();
		}
	}
}