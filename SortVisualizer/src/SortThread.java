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
	public void setDelay(int msdelay){
		this.msdelay = msdelay;
	}
	public boolean isSorted(){
		return sorted;
	}
}
