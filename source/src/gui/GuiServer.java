package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Color;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

public class GuiServer {

	private JFrame frmServerControlPanel;
	private final Action action = new SwingAction();

	/**
	 * Launch the application.
	 */
	public static void launch() {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiServer window = new GuiServer();
					window.frmServerControlPanel.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frmServerControlPanel = new JFrame();
		frmServerControlPanel.setTitle("Server control panel");
		frmServerControlPanel.setBounds(100, 100, 450, 300);
		frmServerControlPanel.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setAction(action);
		btnNewButton.setForeground(Color.RED);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 15));
		frmServerControlPanel.getContentPane().add(btnNewButton, BorderLayout.CENTER);
	}

	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Shutdown");
			putValue(SHORT_DESCRIPTION, "Exit the server");
		}
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
