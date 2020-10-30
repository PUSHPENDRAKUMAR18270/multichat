import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.*;

class User {
	private DatagramSocket soc;
	private SocketAddress src;
	private String username;
	private InetAddress ip;
	private int portnumber;
	public User(DatagramSocket soc,String username,SocketAddress src,InetAddress ip,
							int portnumber) throws IOException
	{
		this.soc=soc;
		this.ip=ip;
		this.portnumber=portnumber;
		this.src=src;
		// username=din.readUTF();
		// byte[] buff= new byte[100];
		// DatagramPacket p=new DatagramPacket(buff,buff.length);
		// this.soc.receive(p);
		this.username=username;
		//get status of user
		onlineStatus();
		//activate thread;
		// this.start();
	}

	public void onlineStatus() throws IOException
	{		
		if(B2bServer3336.mp.containsKey(this.portnumber)==false)
		{
			String str=this.username+" is Online";
			broadcast(str);
			B2bServer3336.mp.put(this.portnumber,this);
			// System.out.println("in online "+str);
			// byte[] buff;
			// buff=str.getBytes();
			// DatagramPacket p=new DatagramPacket(buff,buff.length,this.ip,this.portnumber);
			// this.soc.send(p);
		}
		
	}

	public void broadcast(String msg) throws IOException
	{
		byte[] buff=new byte[2048];
		buff=msg.getBytes();
		// System.out.println("mesbroad "+msg);
		for(int  pn:B2bServer3336.mp.keySet())
		{
			if(this.portnumber==pn){
				continue;
			}
			// DataOutputStream td=new DataOutputStream(soc.getOutputStream());
			// td.writeUTF(username+":"+msg);
			User user=B2bServer3336.mp.get(pn);
			DatagramPacket p=new DatagramPacket(buff,buff.length,user.ip,user.portnumber);
			this.soc.send(p);
		}
	}

	public void chat(String msg) throws IOException
	{
		//read from user	
		// while(true)
		// {
			//read a message from client
			DatagramPacket p;
			if(msg.length()==0)
			{
				return;
			}
			if(msg!=null&&B2bServer3336.mp.containsKey(this.portnumber))
			{
				System.out.println("inchat "+msg);

				if(B2bServer3336.mp.size()>1)
				{
					if(msg.equals("leave"))
					{
						// msg= this.username+" left the chat";
						System.out.println("inleave "+msg);
						B2bServer3336.mp.remove(this.portnumber);
						broadcast(msg);						
					}
					else {
						// System.out.println("inelse "+msg);
						msg=this.username+":"+msg;
						// System.out.println("msginchat "+msg);
						broadcast(msg);
					}
				}
				else if(B2bServer3336.mp.size()==1)
				{
					if(msg.equals("leave"))
					{
						B2bServer3336.mp.remove(this.portnumber);
						this.soc.close();
					}
					else{
						// dout.writeUTF("there is no one online");
						String str="there is no one online";
						byte[] curr=str.getBytes();
						p=new DatagramPacket(curr,curr.length,this.ip,this.portnumber);
						this.soc.send(p);
					}
				}
			
			}
			if(B2bServer3336.mp.size()==0)
			{
				System.out.println("there is no one in chat!!");
				// return;
			}
		
		// }
	}
}



class B2bServer3336{

	final static int portnumber=10000;
	public static Map<Integer,User>mp = new HashMap<>();
	public static void main(String[] args) throws IOException
	{
		
		System.out.println("waiting for clients");
		DatagramSocket soc;
		soc = new DatagramSocket(10000);
		while(true)
		{
			//accept a connection
			
			byte[] buff=new byte[2048];
			DatagramPacket p = new DatagramPacket(buff,buff.length);
			soc.receive(p);
			//if already user is connected
			if(mp.containsKey(p.getPort()))
			{
				String msg=null;
				// byte[] buff=new byte[2048];
				// DatagramPacket p = new DatagramPacket(buff,buff.length);
				// this.soc.receive(p);

				msg=new String(p.getData());
				msg=msg.replaceFirst("\u0000+$", "");
				// System.out.println("inmp "+msg);
				// System.out.println("hello"+msg);
				mp.get(p.getPort()).chat(msg);
				continue;
			}
			System.out.println("new user detected");
			String u=new String(p.getData());
			String username=u.replaceFirst("\u0000+$", "");
			// System.out.println(username.length);
			//create a thread for newuser contacting server
			try{
				User newUser=new User(soc,username,p.getSocketAddress(),p.getAddress(),p.getPort());
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}
