package gui.gui_element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import object.User;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.Action;
import java.awt.Font;

public class PopUp_NewGroup extends JDialog {

	private final JPanel contentPanel = new JPanel();
	
	private Color buttonColor = new Color(66, 73, 106);
	
	private User user;
	/**
	 * @wbp.nonvisual location=-37,314
	 */
	private final JPanel panel = new JPanel();
	private JTextField groupNameText;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();
	private JButton okButton;

	Timer t = new Timer();
	
	/**
	 * Launch the application.
	 */
	public static void launch(User user) {
		try {
			PopUp_NewGroup dialog = new PopUp_NewGroup(user);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PopUp_NewGroup(User user) {
		setTitle("New group");
		setResizable(false);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setAlwaysOnTop(true);
		this.user = user;
		setBounds(100, 100, 290, 125);
		setBackground(new Color(34,34,40));
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setLayout(new FlowLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.WEST);
		getContentPane().setBackground(new Color(34,34,40));
		
		contentPanel.setBackground(new Color(34,34,40));
		
		JLabel lblNewLabel = new JLabel("Group name");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		contentPanel.add(lblNewLabel);
		
		groupNameText = new JTextField();
		groupNameText.setBackground(new Color(64,68,75));
		groupNameText.setForeground(new Color(255,255,255));
		
		contentPanel.add(groupNameText);
		groupNameText.setColumns(15);
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(34,34,40));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		
		okButton = new JButton("OK");
		okButton.setBackground(buttonColor);
		okButton.setForeground(new Color(255, 255, 255));
		okButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		okButton.setAction(action);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
		
		
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setBackground(buttonColor);
		cancelButton.setForeground(new Color(255, 255, 255));
		cancelButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		cancelButton.setAction(action_1);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		
		t.schedule(new CreateEnable(), 1,300);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Create group");
			putValue(SHORT_DESCRIPTION, "Some short description");
		}
		public void actionPerformed(ActionEvent e) {
			user.createGroup(groupNameText.getText());
			dispose();
		}
	}
	
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Cancel");
			putValue(SHORT_DESCRIPTION, "Cancel");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	
	private class CreateEnable extends TimerTask {
		
		@Override
		public void run() {
			if (!"".equals(groupNameText.getText())) {
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}
		}
		
	}
}
