import java.util.Date;

public class Message implements Comparable<Message>{
	private String message;
	private Status statut;
	private Date dateCration;
	public Message(String message) {
		this.message=message;
		statut = Status.WAIT;
		dateCration = new Date();
		// TODO Auto-generated constructor stub
	}
	public String getMessage() {
		return message;
	}
	public Status getStatut() {
		return statut;
	}
	public Date getDateCration() {
		return dateCration;
	}
	@Override
	public int compareTo(Message o) {
		// TODO Auto-generated method stub
		return this.dateCration.compareTo(o.getDateCration());
	}
	public boolean equals (Object obj) {
		if(obj!=null && obj instanceof Message) {
			Message other=(Message) obj;
			return this.dateCration==other.getDateCration() && this.message==other.getMessage();
			
		}
		return false;
	}

}
