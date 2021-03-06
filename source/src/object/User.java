package object;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NavigableSet;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;






public abstract class User {
	private int id = 0;
	private String name;
	OutputStreamWriter output;
	BufferedReader input ;
	NavigableSet<Group> groups = new TreeSet<Group>() ;
	NavigableSet<Discussion> discussions= new TreeSet<Discussion>();
	List<Group> allGroup = new ArrayList<Group>();
	public List<Group> getAllGroup() {
		requestGroup();
		return allGroup;
	}
	public Group getGroupFromAllGroup(int id) {
		for(Group g : allGroup) {
			if(g.getiD_group()==id) {
				return g;
			}
		}
		return null;
	}

	private static final int PORT = 8952;
	Socket socket;
	public User(String name) {
		this.name = name;

	}

	public User(String name, int id) {
		this.name = name;
		this.id = id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public String getNameUser() {
		return name;
	}

		public int atoi(String str) {
			if (str == null || str.length() < 1)
				return 0;

			// trim white spaces
			str = str.trim();

			char flag = '+';

			// check negative or positive
			int i = 0;
			if (str.charAt(0) == '-') {
				flag = '-';
				i++;
			} else if (str.charAt(0) == '+') {
				i++;
			}
			// use double to store result
			double result = 0;

			// calculate value
			while (str.length() > i && str.charAt(i) >= '0' && str.charAt(i) <= '9') {
				result = result * 10 + (str.charAt(i) - '0');
				i++;
			}

			if (flag == '-')
				result = -result;

			// handle max and min
			if (result > Integer.MAX_VALUE)
				return Integer.MAX_VALUE;

			if (result < Integer.MIN_VALUE)
				return Integer.MIN_VALUE;

			return (int) result;
		}

	protected Discussion request_discussion(int discussion) throws IOException {
		output.write("@Rdiscussion@"+id+"@"+discussion+"\n");
		output.flush();
		String reponse="";
		reponse = input.readLine();
		String name="";
		String temp="";
		List <User> u = new ArrayList<User>();
		int nbdot =0;
		for(char car : reponse.toCharArray()) {
			if(car=='@') {
				if(nbdot==0) {
					name=temp;
					temp="";
					nbdot++;
				}
				else {
					if(nbdot==1) {
						discussion=atoi(temp);
						nbdot++;
						temp="";
					}
					else {
						u.add(new Client(temp));
						temp="";
					}
				}
			}
			else {
				temp=temp+String.valueOf(car);
			}
		}
		return new Discussion(name,new TreeSet<Message>(),u,discussion);

	}

	public boolean connect(String userName, String password) {
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			socket.getKeepAlive();
			output = new OutputStreamWriter(socket.getOutputStream());
			input =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String command="";
		command="@Connection@"+userName+"@"+password+"@"+id;
				try {
					output.write(command +"\n");
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		String reponse="";
		try {
			reponse = input.readLine();
			System.out.println(reponse);
			if(!reponse.equals("WrongPassword")) {
				this.id=atoi(reponse);

				System.out.println("Connected");
			}
			else {
				System.out.println("wrongPassword");
			}
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return !reponse.equals("WrongPassword");

	}

	public void create_account(String userName, String password) {
		try {
			socket = new Socket(InetAddress.getLocalHost(),PORT);//"192.168.43.95", PORT);
			socket.getKeepAlive();
			output = new OutputStreamWriter(socket.getOutputStream());
			input =  new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
		String command="";
		command="@createAccount@"+userName+"@"+password;
				try {
					output.write(command +"\n");
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		String reponse="";
		try {
			reponse = input.readLine();
			this.id=atoi(reponse);
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	public void disconnect(){
		try {
			output.write("@disconnect@"+id+"\n");
			output.flush();

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	public void requestGroup() {
		try {
			output.write("@requestGroup@"+id+"\n");
			output.flush();
			String reponse="";
			while(!reponse.equals(".")) {
				reponse = input.readLine();
				String name_group = "";
				int id_group = 0;
				List<User> membres= new LinkedList<User>();
				String temp ="";
				int nbdot =0;
				for(char car : reponse.toCharArray()) {
					if(car=='@') {
						if(nbdot==0) {
							name_group=temp;
							temp="";
							nbdot++;
						}
						else {
							if(nbdot==1) {
								id_group=atoi(temp);
								nbdot++;
								temp="";
							}
							else {
								membres.add(new Client(temp));
								temp = "";
							}


						}
					}
					else {
						temp=temp+String.valueOf(car);
					}
				}
				Group g = new Group(name_group, id_group, membres);
				boolean present=false;
				for(Group alo : allGroup) {
					if(g.equals(alo)) {
						present=true;
					}
				}
				if(!present) {
					allGroup.add(g);
				}
				else {
					allGroup.remove(g);
					allGroup.add(g);
				}
				majGroup();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}



	}
	private void majGroup() {
		for (Group g : groups) {
			g =allGroup.get(allGroup.indexOf(g));
		}

	}
	public void joinGroup(Group groupe) {
		groups.add(groupe);
		try {
			output.write("@joinGroup@"+id+"@"+groupe.getiD_group()+"\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void leaveGroup(Group groupe) {
		groups.remove(groupe);
		try {
			output.write("@leaveGroup@"+id+"@"+groupe.getiD_group()+"\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	public void createGroup(String nameG) {
		List<User> l =new ArrayList<User>();
		l.add((Client) this);
		Group g = new Group(nameG, 0, l );

		String command="";
		command="@createGroup@"+nameG+"@"+id;
				try {
					output.write(command +"\n");
					output.flush();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

		String reponse="";
		try {
			synchronized (reponse) {
			reponse = input.readLine();
		}
			g.setiD_group(atoi(reponse));
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		allGroup.add(g);
		groups.add(g);
	}
	public void sendMessage(String message ,Discussion discussion) {
		Message temp = new Message(message);
		discussion.getMessages().add(temp);
		try {
			output.write("@Message@"+getId()+"@"+discussion.getId()+"@"+temp.getMessage()+"\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String reponse="";
		try {
			reponse = input.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		temp.setIdAuthor(id);

		temp.setId(atoi(reponse));
		discussion.getMessages().add(temp);
	}

	public void createConversation( String message, String name_conv ,Group group) {
		Message tempo = new Message(message);
		int idConv=0;
		try {
			output.write("@NeWMessage@"+name_conv+"@"+group.getiD_group()+"@"+id+"@"+tempo.getMessage()+"\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String reponse="";
		try {
			reponse = input.readLine();
			System.out.println("affiche reponse");
			 idConv=0;
			int idMessage = 0;
			String temp ="";
			for(char car : reponse.toCharArray()) {
				if(car=='@') {
					idConv=atoi(temp);
				}
				else {
					temp=temp+String.valueOf(car);
				}
			}
			idMessage= atoi(temp);

		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		tempo.setIdAuthor(id);
		tempo.setId(idConv);
		Discussion d = new Discussion(message, new Group("", 0, group.getGroup()), tempo);
		d.getGroup().add(this);

		d.setId_Conv(idConv);

		discussions.add(d);
	}
	public void leaveConversation (Discussion conversation) {
		try {
			output.write("@LeaveC@"+id+"@"+conversation.getId()+"\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.discussions.remove(conversation);

	}

	public String toString() {
		return "User [name=" + name + "]";
	}
	public String requestName(int other_id) {
		String reponse="";
		if(other_id!=0) {
		try {
			output.write("@RName@"+id+"@"+other_id+"\n");
			output.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			reponse = input.readLine();
		} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return reponse;

	}

	public NavigableSet<Discussion> getDiscussions() {
		 Refresh ();
		ArrayList <Discussion>discussionToDelete = new ArrayList<Discussion>();
		 for(Discussion d : discussions) {
			 if(d.getGroup().isEmpty()) {
				 discussionToDelete.add(d);
			 }
		 }
		 for(Discussion f : discussionToDelete) {
			 discussions.remove(f);
		 }
		return discussions;
	}

	private void Refresh () {
			String command="@Refresh@"+id;
					try {
						output.write(command +"\n");
						output.flush();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

			String reponse="";
			ArrayList<Message> unread= new ArrayList<Message>();
			ArrayList<Integer> ploup = new ArrayList<Integer>();
			while(!reponse.equals(".")) {
				try {
					reponse = input.readLine();
					reponse=reponse.replaceFirst("@", "");
					//renvoie tout les message non lus renvoyés par getAllUnviewedMessage sous la forme "Envoyeur@Discussion@Date@idmessage@contenu"
					String expediteur = "";
					int id_discussion = 0;
					int id_message = 0;
					String date = "";
					String contenu = "";
					String temp ="";
					int nbdot =0;
					for(char car : reponse.toCharArray()) {
						if(car=='@') {
							if(nbdot==0) {
								expediteur=temp;
								temp="";
								nbdot++;
							}
							else {
								if(nbdot==1) {

									id_discussion=atoi(temp);
									nbdot++;
									temp="";
								}
								else {
									if (nbdot == 2) {
										date=temp;
										nbdot++;
										temp="";
									}
									else {
										id_message = atoi(temp);
										temp = "";
									}
								}
							}
						}
						else {
							temp=temp+String.valueOf(car);
						}
					}
					contenu = temp;
					boolean already_read = false;
					for(Discussion d : discussions ) {
						if(d.getId()==id_discussion) {
							for(Message m : d.getMessages()) {
								if(m.getId()==id_message) {
									already_read= true;
								}
							}
						}
						if(!already_read) {
							d.getMessages().add(new Message(contenu,Status.received,date,atoi(expediteur),id_message));
							already_read=true;
						}
					}
					if(!already_read) {
						unread.add(new Message(contenu,Status.received,date,atoi(expediteur),id_message));
						ploup.add(id_discussion);

					}

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			for(int i=0; i<ploup.size();i++) {
				Discussion m = null;
				try {
					m = request_discussion(ploup.get(i));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				m.getMessages().add(unread.get(i));
				discussions.add(m);
			}

		}



	public Discussion getDiscussion(int i) {
		for (Discussion d : this.discussions) {
			if (d.getId() == i)
				return d;
		}
		return null;
	}
	
	public void debug_addDiscussion(Discussion discussion) {
		discussions.add(discussion);
	}


}
