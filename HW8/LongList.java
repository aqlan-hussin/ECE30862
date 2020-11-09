
public class LongList implements MyList {
	protected long data;
	protected LongList next;
	
	public LongList() {
		next = null;
		data = 0;
	}
	
	public LongList(LongList n, long l) {
		next = n;
		data = l;
	}
	
	public long getData() {
		return data;
	}
	
	public LongList next() {
		return next;
	}
	
	public void printNode() {
		System.out.print("LongList Node, data is: " + data);
	}
}
