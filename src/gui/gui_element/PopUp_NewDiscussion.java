package gui.gui_element;

import object.User;
import object.Group;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.SwingConstants;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.Dialog.ModalityType;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import javax.swing.Action;

public class PopUp_NewDiscussion extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextArea textArea;
	private JButton okButton;
	
	private JComboBox<String> comboBox;
	private List<Integer> list_id = new ArrayList<>();
	
	
	Timer t = new Timer();
	
	private User user;
	private final Action action = new SwingAction();
	private final Action action_1 = new SwingAction_1();
	
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
		setTitle("New discussion");
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
		contentPanel.add(panel, BorderLayout.NORTH);
			
		JLabel lblPickAGroup = new JLabel("Pick a group");
		lblPickAGroup.setHorizontalAlignment(SwingConstants.LEFT);
		panel.add(lblPickAGroup);
			
			
		comboBox = new JComboBox();
		for (Group g : user.getAllGroup()) {
			list_id.add(g.getiD_group());
			comboBox.addItem(g.getName());
		}
		panel.add(comboBox);
			
		
		
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);
			
		textArea = new JTextArea();
		scrollPane.setViewportView(textArea);
			
		
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
		okButton = new JButton("Send");
		okButton.setAction(action_1);
		okButton.setEnabled(false);
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
			
			
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setAction(action);
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
			
		t.schedule(new SendEnable(), 1,300);
		
	}
	
	private class SendEnable extends TimerTask {
		
		@Override
		public void run() {
			if (!"".equals(textArea.getText()) && !comboBox.getSelectedItem().equals(null)) {
				okButton.setEnabled(true);
			} else {
				okButton.setEnabled(false);
			}
		}
		
	}
	
	
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "Cancel");
			putValue(SHORT_DESCRIPTION, "Abord the creation of a new discussion");
		}
		public void actionPerformed(ActionEvent e) {
			dispose();
		}
	}
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Send");
			putValue(SHORT_DESCRIPTION, "Create a new discussion");
		}
		public void actionPerformed(ActionEvent e) {
			String message = textArea.getText();
			int index = comboBox.getSelectedIndex();
			int id = list_id.get(index);
			
			Group group = user.getGroupFromAllGroup(id);
			
			user.createConversation(message, message, group);
			dispose();
		}
	}
}
