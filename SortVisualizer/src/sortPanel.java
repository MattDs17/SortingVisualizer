import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.JPanel;

public class sortPanel extends JPanel {
	private ArrayList<Integer> list;
	private int hPad;
	private int hRatio = 10; // ratio between width of bars and padding
	private int vPad = 5;
	private float vScale;
	private String name;

	public sortPanel(String name) {
		super();
		list = new ArrayList<Integer>();
		this.name = name;
	}

	public int getMaxIndex() {
		int max = 0;
		for (int i = 1; i < list.size(); i++) {
			if (list.get(i) > list.get(max)) {
				max = i;
			}
		}
		return max;
	}

	public int getListSize() {
		return list.size();
	}

	public void addNum(int num) {
		list.add(num);
	}
	public void setList(ArrayList<Integer> list){
		this.list = list;
	}

	public void swap(int i1, int i2) {
		int temp = list.get(i1);
		list.set(i1, list.get(i2));
		list.set(i2, temp);
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.clearRect(0, 0, this.getWidth(), this.getHeight());
		g2d.drawString(name, 5, this.getHeight() - 5);
		if (this.getListSize() > 0) {
			hPad = this.getWidth() / ((hRatio + 1) * list.size() + 1);
			vScale = (this.getHeight() - 2 * vPad - g2d.getFont().getSize())
					/ (float)list.get(this.getMaxIndex());
			g2d.drawRect(0, 0, this.getWidth(), this.getHeight());
			for (int i = 0; i < list.size(); i++) {
				g2d.fillRect(hPad * ((hRatio + 1) * i + 1), vPad,
						hRatio * hPad, Math.round(list.get(i) * vScale));
			}
		}

	}
}
