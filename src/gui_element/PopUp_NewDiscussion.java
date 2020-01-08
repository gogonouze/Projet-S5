package gui_element;

import object.User;
import object.Group;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Dialog.ModalityType;

public class PopUp_NewDiscussion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JComboBox comboBox;
	private JTextArea textArea;
	JButton okButton;
	
	Timer t = new Timer();
	
	private User user;
	private List<Group> allGroup;

	/**
	 * Launch the application.
	 */
	public static void launch(User user) {
		try {
			PopUp_NewDiscussion dialog = new PopUp_NewDiscussion(user);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/**
	 * Create the dialog.
	 */
	public PopUp_NewDiscussion(User user) {
		this.user = user;
		this.allGroup = user.();
		
		
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
		contentPanel.add(panel, BorderLayout.NORTH);
			
		JLabel lblPickAGroup = new JLabel("Pick a group");
		lblPickAGroup.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblPickAGroup);
			
			
		comboBox = new JComboBox();
		panel.add(comboBox);
			
		
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);
			
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
			
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		okButton = new JButton("Send");
		okButton.setEnabled(false);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
			
			
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
			
		t.schedule(new SendEnable(), 1,300);
		
	}
	
	private class SendEnable extends TimerTask {
		
		@Override
		public void run() {
			if (!"".equals(textArea.getText())) {
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}
		}
		
	}
	
	
	
}
