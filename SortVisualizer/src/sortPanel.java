import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.LayoutManager;
import java.util.ArrayList;
import javax.swing.JPanel;


public class sortPanel extends JPanel {
	ArrayList<Integer> list;
	int barWidth;
	int hPad;
	int hRatio = 10; //ratio between width of bars and padding
	int vPad = 5;
	float vScale;
	public sortPanel(ArrayList<Integer> list) {
		super();
		this.list = list;
	}
	public int getMaxIndex(){
		int max = 0;
		for (int i = 1; i < list.size();i++){
			if (list.get(i) > list.get(max)){
				max = i;
			}
		}
		return max;
	}
	
	
	public void paintComponent(Graphics g){
		Graphics2D g2d = (Graphics2D)g;
		hPad = this.getWidth()/((hRatio+1)*list.size()+1);
		vScale = (this.getHeight()-2*vPad)/this.list.get(this.getMaxIndex());
		for(int i = 0; i < list.size();i++){
			g2d.fillRect(hPad*((hRatio+1)*i + 1), vPad, hRatio*hPad, Math.round(list.get(i)*vScale));
		}
		
		
	}
}
