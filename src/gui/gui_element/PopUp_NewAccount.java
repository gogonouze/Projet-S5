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
import gui.Launcher;
import object.User;
import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Font;

public class PopUp_NewAccount extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField loginText;
	private final Action action = new SwingAction();
	
	private User user;
	private final Action action_1 = new SwingAction_1();
	private JTextField passwordText;
	
	private Color buttonColor = new Color(66, 73, 106);
	

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
		setTitle("New account");
		setResizable(false);
		this.user = user;
		
		setType(Type.POPUP);
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 320, 177);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		contentPanel.add(panel, BorderLayout.SOUTH);
		contentPanel.setBackground(new Color(34,34,40));
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_2 = new JPanel();
		panel.add(panel_2, BorderLayout.CENTER);
		
		JButton createAccountButton = new JButton("New button");
		createAccountButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		createAccountButton.setForeground(new Color(255, 255, 255));
		createAccountButton.setAction(action_1);
		createAccountButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			}
		});
		panel_2.add(createAccountButton);
		
		JButton cancelButton = new JButton("New button");
		cancelButton.setForeground(new Color(255, 255, 255));
		cancelButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		cancelButton.setAction(action);
		panel_2.add(cancelButton);
		
		JPanel panel_3 = new JPanel();
		contentPanel.add(panel_3, BorderLayout.CENTER);
		GridBagLayout gbl_panel_3 = new GridBagLayout();
		gbl_panel_3.columnWidths = new int[]{134, 164, 0};
		gbl_panel_3.rowHeights = new int[]{59, 59, 0};
		gbl_panel_3.columnWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		gbl_panel_3.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		panel_3.setLayout(gbl_panel_3);
		
		JPanel panel_6 = new JPanel();
		FlowLayout flowLayout_3 = (FlowLayout) panel_6.getLayout();
		flowLayout_3.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel_6 = new GridBagConstraints();
		gbc_panel_6.fill = GridBagConstraints.BOTH;
		gbc_panel_6.insets = new Insets(0, 0, 5, 5);
		gbc_panel_6.gridx = 0;
		gbc_panel_6.gridy = 0;
		panel_3.add(panel_6, gbc_panel_6);
		
		JLabel lblPseudo = new JLabel("user name");
		lblPseudo.setForeground(new Color(255, 255, 255));
		lblPseudo.setBackground(new Color(34,34,40));
		lblPseudo.setFont(new Font("Tahoma", Font.BOLD, 12));
		panel_6.add(lblPseudo);
		
		JPanel panel_4 = new JPanel();
		FlowLayout flowLayout_1 = (FlowLayout) panel_4.getLayout();
		flowLayout_1.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel_4 = new GridBagConstraints();
		gbc_panel_4.fill = GridBagConstraints.BOTH;
		gbc_panel_4.insets = new Insets(0, 0, 5, 0);
		gbc_panel_4.gridx = 1;
		gbc_panel_4.gridy = 0;
		panel_3.add(panel_4, gbc_panel_4);
		
		loginText = new JTextField();
		loginText.setBackground(new Color(64,68,75));
		loginText.setForeground(new Color(255,255,255));
		
		panel_4.add(loginText);
		loginText.setColumns(10);
		
		JPanel panel_7 = new JPanel();
		FlowLayout flowLayout_2 = (FlowLayout) panel_7.getLayout();
		flowLayout_2.setAlignment(FlowLayout.RIGHT);
		GridBagConstraints gbc_panel_7 = new GridBagConstraints();
		gbc_panel_7.fill = GridBagConstraints.BOTH;
		gbc_panel_7.insets = new Insets(0, 0, 0, 5);
		gbc_panel_7.gridx = 0;
		gbc_panel_7.gridy = 1;
		panel_3.add(panel_7, gbc_panel_7);
		
		JLabel lblNewLabel = new JLabel("Password");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNewLabel.setBackground(new Color(34,34,40));
		panel_7.add(lblNewLabel);
		
		JPanel panel_5 = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel_5.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		GridBagConstraints gbc_panel_5 = new GridBagConstraints();
		gbc_panel_5.fill = GridBagConstraints.BOTH;
		gbc_panel_5.gridx = 1;
		gbc_panel_5.gridy = 1;
		panel_3.add(panel_5, gbc_panel_5);
		
		passwordText = new JTextField();
		passwordText.setBackground(new Color(64,68,75));
		passwordText.setForeground(new Color(255,255,255));
		
		panel_5.add(passwordText);
		passwordText.setColumns(10);
		
		panel.setBackground(new Color(34,34,40));
		panel_2.setBackground(new Color(34,34,40));
		panel_3.setBackground(new Color(34,34,40));
		panel_4.setBackground(new Color(34,34,40));
		panel_5.setBackground(new Color(34,34,40));
		panel_6.setBackground(new Color(34,34,40));
		panel_7.setBackground(new Color(34,34,40));
		
		createAccountButton.setBackground(buttonColor);
		cancelButton.setBackground(buttonColor);
		
	}
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Cancel");
			putValue(SHORT_DESCRIPTION, "Cancel");
		}
		public void actionPerformed(ActionEvent e) {
			Launcher.launch(user);
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
			dispose();
			
			
		}
	}
}
