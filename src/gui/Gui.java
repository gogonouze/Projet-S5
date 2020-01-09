package gui;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.BevelBorder;

import gui.gui_element.PopUp_JoinGroup;
import gui.gui_element.PopUp_NewDiscussion;
import gui.gui_element.PopUp_NewGroup;
import object.Discussion;
import object.Message;
import object.User;

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
import javax.swing.BoxLayout;

public class Gui {
	

	private JFrame frame;
	private JTextArea displayArea;
	private JTextArea messageArea;
	private User user;
	private JButton sendButton;

	private Color buttonColor = new Color(66, 73, 106);
	
	Timer t = new Timer();
	
	private JList<String> list;
	private List<Integer> list_id = new ArrayList<>();
	private DefaultListModel<String> model = new DefaultListModel<>();
	
	private final Action newDiscussion = new SwingAction();
	private final Action Send = new SwingAction_1();
	private final Action action = new SwingAction_2();
	private final Action action_1 = new SwingAction_3();
	private final Action action_2 = new SwingAction_4();
	private final Action action_3 = new SwingAction_5();
	
	
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
		frame.setTitle(user.requestName(user.getId()));
		frame.setMinimumSize(new Dimension(500,300));
		frame.setSize(new Dimension(750, 450));
		frame.setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                leave();
            }
        });
		
		frame.setBackground(new Color(34,34,40));
		
		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.WEST);
		panel.setLayout(new BorderLayout(0, 0));
		panel.setBackground(new Color(34, 34, 40));
		
		JPanel panel_1 = new JPanel();
		panel.add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new GridLayout(0, 2, 0, 0));
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton.setAction(newDiscussion);
		panel_1.add(btnNewButton);
		
		JButton refreshButton = new JButton("New button");
		refreshButton.setForeground(new Color(255, 255, 255));
		refreshButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		refreshButton.setAction(action);
		refreshButton.setBackground(buttonColor);
		panel_1.add(refreshButton);
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setForeground(new Color(255, 255, 255));
		btnNewButton_1.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_1.setAction(action_1);
		panel_1.add(btnNewButton_1);
		
		JButton btnNewButton_2 = new JButton("New button");
		btnNewButton_2.setForeground(new Color(255, 255, 255));
		btnNewButton_2.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_2.setAction(action_2);
		panel_1.add(btnNewButton_2);
		
		list = new JList();		
		list.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				displayContent();
			}
		});
		list.setBackground(new Color(41,41,50));
		list.setForeground(new Color(255,255,255));
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
		sendButton.setForeground(new Color(255, 255, 255));
		sendButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		sendButton.setAction(Send);
		sendButton.setBackground(buttonColor);
		panel_4.add(sendButton);
		
		JButton btnNewButton_3 = new JButton("New button");
		btnNewButton_3.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton_3.setForeground(new Color(255, 255, 255));
		btnNewButton_3.setAction(action_3);
		panel_4.add(btnNewButton_3);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
		panel_3.add(scrollPane_1, BorderLayout.CENTER);
		
		messageArea = new JTextArea();
		messageArea.setLineWrap(true);
		messageArea.setWrapStyleWord(true);
		messageArea.setRows(3);
		messageArea.setBackground(new Color(64,68,75));
		messageArea.setForeground(new Color(255,255,255));
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
		displayArea.setBackground(new Color(54,57,63));
		displayArea.setForeground(new Color(255,255,255));
		scrollPane.setViewportView(displayArea);
		
		btnNewButton.setBackground(buttonColor);
		btnNewButton_1.setBackground(buttonColor);
		btnNewButton_2.setBackground(buttonColor);
		btnNewButton_3.setBackground(buttonColor);
		
		panel_1.setBackground(new Color(34,34,40));
		panel_2.setBackground(new Color(34,34,40));
		panel_3.setBackground(new Color(34,34,40));
		panel_4.setBackground(new Color(34,34,40));
		
		scrollPane.setBackground(new Color(34,34,40));
		scrollPane_1.setBackground(new Color(34,34,40));
		
		
		updateDiscussionsList();
		list.setModel(model);
		//t.schedule(new Display(), 1,300);
		t.schedule(new SendEnable(), 1, 300);
	}
	
	private void updateDiscussionsList() {
		list_id.clear();
		model.clear();
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
		user.disconnect();
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
			int id = m.getIdAuthor();
			String messageAuthor = user.requestName(id);
			
			displayArea.append("["+messageAuthor+"]"+" :"+"\n");
			
			displayArea.append(messageContent+"\n");
			
			displayArea.append("=-=-=-="+"\n");
		}		
	}
	
	private void displayBlank() {
		displayArea.setText("");
	}
	
	/*private class Display extends TimerTask {
		
		@Override
		public void run() {
			if (!list.isSelectionEmpty()) {
				displayContent();
			} else {
				displayBlank();
			}
			
		}
		
	}*/
	
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
	private class SwingAction_2 extends AbstractAction {
		public SwingAction_2() {
			putValue(NAME, "refresh");
			putValue(SHORT_DESCRIPTION, "refresh discussions list");
		}
		public void actionPerformed(ActionEvent e) {
			updateDiscussionsList();
		}
	}
	private class SwingAction_3 extends AbstractAction {
		public SwingAction_3() {
			putValue(NAME, "Create new group");
			putValue(SHORT_DESCRIPTION, "Create new group");
		}
		public void actionPerformed(ActionEvent e) {
			PopUp_NewGroup.launch(user);
		}
	}
	private class SwingAction_4 extends AbstractAction {
		public SwingAction_4() {
			putValue(NAME, "Join group");
			putValue(SHORT_DESCRIPTION, "Join group");
		}
		public void actionPerformed(ActionEvent e) {
			PopUp_JoinGroup.launch(user);
		}
	}
	private class SwingAction_5 extends AbstractAction {
		public SwingAction_5() {
			putValue(NAME, "refresh");
			putValue(SHORT_DESCRIPTION, "refresh message");
		}
		public void actionPerformed(ActionEvent e) {
			
			displayContent();
		}
	}
}
