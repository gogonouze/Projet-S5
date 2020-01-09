package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import javax.swing.BoxLayout;
import javax.swing.JTextField;

import gui.gui_element.PopUp_NewAccount;
import object.User;

import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Component;
import javax.swing.Box;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.GridLayout;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;
import java.awt.event.ActionListener;
import javax.swing.JPasswordField;

public class Launcher {

	private JFrame frame;
	private JTextField loginText;
	private final Action action = new SwingAction();
	private JLabel ErrorLabel;
	
	private Color buttonColor = new Color(66, 73, 106);
	
	private User user;
	private final Action action_1 = new SwingAction_1();
	private JPasswordField passwordText;
	
	/**
	 * Launch the application.
	 */
	public static void launch(User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Launcher window = new Launcher(user);
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Launcher(User user) {
		this.user = user;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.setBounds(100, 100, 298, 184);
		frame.setBackground(new Color(34,34,40));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		frame.getContentPane().setBackground(new Color(34,34,40));
		
		JPanel panel = new JPanel();
		panel.setBackground(new Color(34,34,40));
		frame.getContentPane().add(panel, BorderLayout.SOUTH);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(new Color(34,34,40));
		panel.add(panel_1, BorderLayout.NORTH);
		
		ErrorLabel = new JLabel("");
		panel_1.add(ErrorLabel);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBackground(new Color(34,34,40));
		panel.add(panel_2, BorderLayout.CENTER);
		
		JButton createAccountButton = new JButton("New button");
		createAccountButton.setBackground(buttonColor);
		createAccountButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		createAccountButton.setForeground(new Color(255, 255, 255));
		createAccountButton.setAction(action_1);
		createAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_2.add(createAccountButton);
		
		JButton connectionButton = new JButton("New button");
		connectionButton.setBackground(buttonColor);
		connectionButton.setForeground(new Color(255, 255, 255));
		connectionButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		connectionButton.setAction(action);
		panel_2.add(connectionButton);
		
		JPanel panel_3 = new JPanel();
		panel_3.setBackground(new Color(34,34,40));
		frame.getContentPane().add(panel_3, BorderLayout.CENTER);
		panel_3.setLayout(new BoxLayout(panel_3, BoxLayout.Y_AXIS));
		
		JPanel panel_4 = new JPanel();
		panel_4.setBackground(new Color(34,34,40));
		panel_3.add(panel_4);
		
		JLabel lblPseudo = new JLabel("Login");
		lblPseudo.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblPseudo.setForeground(new Color(255, 255, 255));
		panel_4.add(lblPseudo);
		
		loginText = new JTextField();
		loginText.setBackground(new Color(64,68,75));
		loginText.setForeground(new Color(255,255,255));
		
		panel_4.add(loginText);
		loginText.setColumns(10);
		
		JPanel panel_5 = new JPanel();
		panel_5.setBackground(new Color(34,34,40));
		panel_3.add(panel_5);
		
		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setForeground(new Color(255, 255, 255));
		panel_5.add(lblNewLabel);
		
		passwordText = new JPasswordField();
		passwordText.setColumns(10);
		passwordText.setBackground(new Color(64,68,75));
		passwordText.setForeground(new Color(255,255,255));
		
		
		
		panel_5.add(passwordText);
	

		
		
	}
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Connection");
			putValue(SHORT_DESCRIPTION, "Connection");
		}
		public void actionPerformed(ActionEvent e) {
			String login = loginText.getText();
			String password = passwordText.getText();
			
			if(user.connect(login,password)) {
				Gui.launch(user);
				frame.dispose();
			} else {
				ErrorLabel.setText("Login or Password incorrect");
				ErrorLabel.setForeground(Color.red);
			}
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Create new account");
			putValue(SHORT_DESCRIPTION, "Create a new account");
		}
		public void actionPerformed(ActionEvent e) {
			PopUp_NewAccount.launch(user);
			frame.dispose();
			
		}
	}
}
