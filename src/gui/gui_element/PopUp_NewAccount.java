package gui.gui_element;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import gui.Gui;
import object.User;

public class PopUp_NewAccount extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField loginText;
	private final Action action = new SwingAction();
	private JLabel ErrorLabel;
	
	private User user;
	private final Action action_1 = new SwingAction_1();
	private JTextField passwordText;

	/**
	 * Launch the application.
	 */
	public static void launch(User user) {
		try {
			PopUp_NewAccount dialog = new PopUp_NewAccount(user);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PopUp_NewAccount(User user) {
		this.user = user;
		
		setAlwaysOnTop(true);
		setType(Type.POPUP);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.NORTH);
		
		ErrorLabel = new JLabel("");
		panel_1.add(ErrorLabel);
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		
		JButton createAccountButton = new JButton("New button");
		createAccountButton.setAction(action_1);
		createAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_2.add(createAccountButton);
		
		JButton cancelButton = new JButton("New button");
		cancelButton.setAction(action);
		panel_2.add(cancelButton);
		
		JPanel panel_3 = new JPanel();
		contentPanel.add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4);
		
		JLabel lblPseudo = new JLabel("user name");
		panel_4.add(lblPseudo);
		
		loginText = new JTextField();
		panel_4.add(loginText);
		loginText.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panel_3.add(panel_5);
		
		JLabel lblNewLabel = new JLabel("Password");
		panel_5.add(lblNewLabel);
		
		passwordText = new JTextField();
		panel_5.add(passwordText);
		passwordText.setColumns(10);
	}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Cancel");
			putValue(SHORT_DESCRIPTION, "Cancel");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Create new account");
			putValue(SHORT_DESCRIPTION, "Create a new account");
		}
		public void actionPerformed(ActionEvent e) {
			String userName = loginText.getText();
			String password = passwordText.getText();
			
			user.create_account(userName, password);
			
			Gui.launch(user);
			
		}
	}
}
