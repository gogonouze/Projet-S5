package object;

import java.util.ArrayList;
import java.util.List;

public class Group implements Comparable<Group>{
	private String name;
	private int iD_group;
	public List<User> group =new ArrayList<User>();

	public Group(String name, int iD_group, List<User> group) {
		super();
		this.name = name;
		this.iD_group = iD_group;
		this.group = group;
	}

	public int getiD_group() {
		return iD_group;
	}

	public List<User> getGroup() {
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

	@Override
	public String toString() {
		return "Group [iD_group=" + iD_group + ", group=" + group + "]";
	}
	
}
