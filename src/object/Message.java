package object;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Message implements Comparable<Message>{
	String message;
	Status status = Status.wait;
	String dateCreation;
	String proprio;
	public Message(String message, Status status, String dateCreation, String proprio, int id) {
		super();
		this.message = message;
		this.status = status;
		this.dateCreation = dateCreation;
		this.proprio = proprio;
		this.id = id;
	}

	private int id;
	private int nbVu = 0;
	
	public Message(String message) {
		this.message = message;
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		dateCreation = format.format(date);
	}
	
	public Message(String message, Status status, String dateCreation, int id, int nbVu) {
		this.message = message;
		this.status = status;
		this.dateCreation = dateCreation;
		this.id = id;
		this.nbVu = nbVu;
	}

	public boolean received(int lenghtGroup) {
		nbVu++;
		System.out.println("nbVu = " + nbVu);
		if (nbVu == lenghtGroup) {
			return true;
		}
		return false;
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
		int cmpT = dateCreation.compareTo(m.getDateCreation());
		if (cmpT == 0) {
			return getId()-m.getId();
		}
		return cmpT;
	}
	
	public int getNbVu() {
		return nbVu;
	}

	@Override
	public String toString() {
		return "Message [message=" + message + ", status=" + status + ", dateCreation=" + dateCreation + ", id=" + id
				+ "]";
	}
}
