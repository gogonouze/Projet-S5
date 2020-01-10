package object;
import java.util.ArrayList;


public class Port_Pool {
	public ArrayList<Integer> pool = new ArrayList<Integer>();
	public Port_Pool() {
		for(int i=0; i<10 ; i++) {
			pool.add(8953+i);
		}
	}
	public int selectPort() {
		int retval =(int) (Math.random()*pool.size());
		int temp = pool.get(retval);
		pool.remove(retval);
		return temp;
	}
	
}
