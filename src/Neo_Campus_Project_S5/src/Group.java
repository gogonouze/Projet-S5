
import java.util.TreeSet;

public class Group implements Comparable<Group>{
	private int iD_group;
	TreeSet<User> group =new TreeSet<User>();

	public int getiD_group() {
		return iD_group;
	}

	public TreeSet<User> getGroup() {
		return group;
	}

	@Override
	public int compareTo(Group o2) {
		// TODO Auto-generated method stub
		return this.iD_group-o2.iD_group;
	}
	
	public boolean equals (Object obj) {
		if(obj!=null && obj instanceof Group) {
			Group other=(Group) obj;
			return this.iD_group==other.iD_group;
			
		}
		return false;
	}
}
