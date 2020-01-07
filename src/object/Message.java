package object;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message>{
	private static int nbMessage = 0;
	
	String message;
	Status status = Status.wait;
	String dateCreation;
	private int id;
	
	public Message(String message) {
		this.message = message;
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		dateCreation = format.format(date);
		this.id = nbMessage;
		nbMessage++;
	}

	public Message(String message, Status status, String dateCreation, int id) {
		this.message = message;
		this.status = status;
		this.dateCreation = dateCreation;
		this.id = id;
	}

	public int getId() {
		return id;
	}
	
	public String getMessage() {
		return message;
	}
	
	public Status getStatus() {
		return status;
	}
	
	public String getDateCreation() {
		return dateCreation;
	}
	
	public int compareTo(Message m) {
		return dateCreation.compareTo(m.getDateCreation());
	}
	
	@Override
	public String toString() {
		return "Message [message=" + message + ", status=" + status + ", dateCreation=" + dateCreation + ", id=" + id
				+ "]";
	}
}
