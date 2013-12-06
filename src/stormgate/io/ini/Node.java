package stormgate.io.ini;

public class Node {
	public String id;
	public String data;
	
	public Node() {
		id = "";
		data = "";
	}
	
	public String toString() {
		// TODO Auto-generated method stub
		return "ININode; " + id + " = " + data;
	}
}
