package object;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import javax.swing.ScrollPaneConstants;
import javax.swing.Timer;
import javax.swing.border.BevelBorder;
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

public class Gui {
	
	private JTextArea displayArea;
	private JFrame frame;
	private User user;
	
	private JList<String> list;
	private List<Integer> list_id = new ArrayList<>();
	private DefaultListModel<String> model = new DefaultListModel<>();
	
	JTextArea messageArea;
	
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
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
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
				displayContent();
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
		
		JButton btnNewButton_1 = new JButton("New button");
		btnNewButton_1.setAction(Send);
		panel_4.add(btnNewButton_1);
		
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
	}
	
	private void updateDiscussionsList() {
		for(Discussion d : user.getDiscussions()) {
			list_id.add(d.getId());
			model.addElement(d.getName());
		}
	}
	
	private void displayContent() {
		int index = list.getSelectedIndex();
		int idDiscussion = list_id.get(index);
		Discussion discussion = user.getDiscussion(idDiscussion); 
		
		displayArea.setText("");
		
		for (Message m : discussion.getMessages()) {
			String messageContent = m.getMessage();
			System.out.println(messageContent);
			String messageAuthor = "alo?";
			
			displayArea.append("["+messageAuthor+"]"+" :"+"\n");
			
			displayArea.append(messageContent+"\n");
			
			displayArea.append("=-=-=-="+"\n");
		
			
		}
		
		
	}
	
	
	
	private class SwingAction extends AbstractAction {
		public SwingAction() {
			putValue(NAME, "New Discussion");
			putValue(SHORT_DESCRIPTION, "Get a new discussion");
		}
		public void actionPerformed(ActionEvent e) {
			
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
