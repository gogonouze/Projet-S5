package object;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message>{
	String message;
	Status status = Status.wait;
	String dateCreation;
	
	public Message(String message) {
		this.message = message;
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		dateCreation = format.format(date);
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
}
