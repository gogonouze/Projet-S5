package object;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import gui_element.PopUp_NewDiscussion;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.NavigableSet;
import java.util.TimerTask;
import java.util.Timer;

import javax.swing.Action;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.ListSelectionModel;
import javax.swing.JTextPane;
import javax.swing.ListModel;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Dialog.ModalExclusionType;
import java.awt.Window.Type;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JCheckBox;

public class Gui {
	

	private JFrame frame;
	private JTextArea displayArea;
	private JTextArea messageArea;
	private User user;
	private JButton sendButton;

	Timer t = new Timer();
	
	private JList<String> list;
	private List<Integer> list_id = new ArrayList<>();
	private DefaultListModel<String> model = new DefaultListModel<>();
	
	private final Action newDiscussion = new SwingAction();
	private final Action Send = new SwingAction_1();
	
	
	/**
	 * Launch the application.
	 */
	public static void launch(User user) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Gui window = new Gui(user);
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
	public Gui(User user) {
		this.user = user;
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame();
		frame.setMinimumSize(new Dimension(500,300));
		frame.setSize(new Dimension(750, 450));
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                leave();
            }
        });
		
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setAction(newDiscussion);
		panel_1.add(btnNewButton);
		
		list = new JList();		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				
			}
		});
		list.setBackground(Color.WHITE);
		list.setForeground(Color.BLACK);
		list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		panel.add(list, BorderLayout.CENTER);
		
		JPanel panel_2 = new JPanel();
		frame.getContentPane().add(panel_2, BorderLayout.CENTER);
		panel_2.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_3 = new JPanel();
		panel_2.add(panel_3, BorderLayout.SOUTH);
		panel_3.setLayout(new BorderLayout(0, 0));
		
		JPanel panel_4 = new JPanel();
		panel_3.add(panel_4, BorderLayout.EAST);
		panel_4.setLayout(new GridLayout(0, 1, 0, 0));
		
		sendButton = new JButton("New button");
		sendButton.setAction(Send);
		panel_4.add(sendButton);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_3.add(scrollPane_1, BorderLayout.CENTER);
		
		messageArea = new JTextArea();
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setRows(3);
		scrollPane_1.setViewportView(messageArea);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		panel_2.add(scrollPane, BorderLayout.CENTER);
		
		displayArea = new JTextArea();
		displayArea.setFont(new Font("Monospaced", Font.BOLD, 13));
		displayArea.setEditable(false);
		displayArea.setLineWrap(true);
		displayArea.setWrapStyleWord(true);
		scrollPane.setViewportView(displayArea);
		
		updateDiscussionsList();
		list.setModel(model);
		t.schedule(new Display(), 1,300);
		t.schedule(new SendEnable(), 1, 300);
	}
	
	private void updateDiscussionsList() {
		for(Discussion d : user.getDiscussions()) {
			list_id.add(d.getId());
			model.addElement(d.getName());
		}
	}
	
	
	private User getUser(List<User> group, int id) {
		for (User u : group) {
			if (u.getId() == id) {
				return u;
			}
		}
		return null;
	}
	
	private void leave() {
		System.exit(0);
	}
	
	private void displayContent() {
		int index = list.getSelectedIndex();
		int idDiscussion = list_id.get(index);
		Discussion discussion = user.getDiscussion(idDiscussion); 
		List<User> group = discussion.getGroup();
		
		displayArea.setText("");
		
		for (Message m : discussion.getMessages()) {
			String messageContent = m.getMessage();
			int id = m.getId();
			User messageAuthor = getUser(group, id);
			
			displayArea.append("["+messageAuthor.getNameUser()+"]"+" :"+"\n");
			
			displayArea.append(messageContent+"\n");
			
			displayArea.append("=-=-=-="+"\n");
		}		
	}
	
	private void displayBlank() {
		displayArea.setText("");
	}
	
	private class Display extends TimerTask {
		
		@Override
		public void run() {
			if (!list.isSelectionEmpty()) {
				displayContent();
			} else {
				displayBlank();
			}
		}
		
	}
	
	private class SendEnable extends TimerTask {
		
		@Override
		public void run() {
			if (!"".equals(messageArea.getText())) {
				sendButton.setEnabled(true);
			} else {
				sendButton.setEnabled(false);
			}
		}
		
	}
	
	
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "New Discussion");
			putValue(SHORT_DESCRIPTION, "Get a new discussion");
		}
		public void actionPerformed(ActionEvent e) {
			PopUp_NewDiscussion.launch(user);
		}
	}
	
	
	private class SwingAction_1 extends AbstractAction {
		public SwingAction_1() {
			putValue(NAME, "Send");
			putValue(SHORT_DESCRIPTION, "Send the message");
		}
		public void actionPerformed(ActionEvent e) {
			int index = list.getSelectedIndex();
			int idDiscussion = list_id.get(index);
			Discussion discussion = user.getDiscussion(idDiscussion); 
			user.sendMessage(messageArea.getText(), discussion);
			messageArea.setText("");
		}
	}
}
