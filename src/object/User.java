package object;

public abstract class User {
	private String name;
	private Discussion discussions[];
	private Group groups[];

	public User(String name) {
		this.name = name;
	}
	
	public void sendMessage(String message, Discussion discussion) {
		
	}
	
	public String getName() {
		return name;
	}

}
