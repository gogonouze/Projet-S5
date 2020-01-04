package test;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Tests {

	public static void main(String[] args) {
		String dateCreation;
		DateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		dateCreation = format.format(date);
		System.out.println(dateCreation);
	}

}
