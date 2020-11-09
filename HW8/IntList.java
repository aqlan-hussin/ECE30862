
public class IntList implements MyList {
	
	protected int data;
	protected IntList next;
	
	public IntList() {
		next = null;
		data = 0;
	}
	
	public IntList(IntList n, int d) {
		next = n;
		data = d;
	}
	
	public int getData() {
		return data;
	}
	
	public IntList next() {
		return next;
	}
	
	public void printNode() {
		System.out.print("IntList Node, data is: " + data);
	}
}
