
// Second sort thread. Runs an insertion sort algorithm.
class InsertionSortThread extends SortThread {
	/**
	 * 
	 */
	private final MainWindow mainWindow;
	public InsertionSortThread(MainWindow mainWindow, SortPanel sp, long msdelay) {
		super(sp, msdelay);
		this.mainWindow = mainWindow;
	}
	public void run() {
		int listSize = sp.getListSize();
		for (int i = 0; i < listSize && this.mainWindow.started; i++) {
			sp.setIndex(i);
			sp.setColorRange(0, i, Colors.ACTIVE);
			sp.setColor(i, Colors.TARGET);
			int val = sp.get(i);
			sp.setLine(val);
			sp.setMessage("Finding location where previous numbers < "
					+ val + " < following numbers.");
			for (int j = i - 1; j >= 0 && val < sp.get(j); j--) {
				while (this.mainWindow.paused) {
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
			if (i == listSize - 1 && this.mainWindow.started) {
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

		if (this.mainWindow.checkAllSorted() && this.mainWindow.started) {
			this.mainWindow.start();
		}
	}
}