package gui_element;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Discussion extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public Discussion() {
		
		textField = new JTextField();
		add(textField);
		textField.setColumns(10);

	}

}
