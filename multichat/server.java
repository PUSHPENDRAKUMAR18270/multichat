import java.net.*;
import java.io.*;
import java.lang.Thread;
import java.util.*;

class User extends Thread{
	private Socket currsoc;
	private String username;
	private DataInputStream din;
	private DataOutputStream dout;

	public User(Socket soc) throws IOException
	{
		this.currsoc=soc;
		din=new DataInputStream(soc.getInputStream());
		dout =new DataOutputStream(soc.getOutputStream());
		username=din.readUTF();
		//get status of user
		onlineStatus();
		//activate thread;
		this.start();
	}

	public void run() 
	{
		try{
			chat();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return;
		}
		finally{
			try{
				din.close();
				dout.close();
				currsoc.close();
				return;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	public void onlineStatus() throws IOException
	{		
		if(B1bServer3336.mp.containsKey(currsoc)==false)
		{
			B1bServer3336.mp.put(currsoc,username);
			dout.writeUTF(B1bServer3336.mp.get(currsoc)+" is Online");
		}
		else{
			dout.writeUTF(B1bServer3336.mp.get(currsoc)+" is already Online");
		}
	}

	public void broadcast(String msg) throws IOException
	{
		for(Socket soc:B1bServer3336.mp.keySet())
		{
			if(currsoc==soc){
				continue;
			}
			DataOutputStream td=new DataOutputStream(soc.getOutputStream());
			td.writeUTF(username+":"+msg);
		}
	}

	public void chat() throws IOException
	{
		//read from user	
		while(true)
		{
			//read a message from client
			String msg=null;
			if(din.available()>0)
				msg=din.readUTF();
			if(msg!=null&&B1bServer3336.mp.containsKey(currsoc))
			{
				if(B1bServer3336.mp.size()>1)
				{
					if(msg.equals("leave"))
					{
						msg=B1bServer3336.mp.get(currsoc)+" left the chat";
						B1bServer3336.mp.remove(currsoc);
						broadcast(msg);						
						return;
					}
					else {
						broadcast(msg);
					}
				}
				else if(B1bServer3336.mp.size()==1)
				{
					if(msg.equals("leave"))
					{
						B1bServer3336.mp.remove(currsoc);
						currsoc.close();
					}
					else
						dout.writeUTF("there is no one online");
				}
			}
			if(B1bServer3336.mp.size()==0)
			{
				System.out.println("there is no one in chat!!");
				return;
			}
		}
		
	}
}



class B1bServer3336{
	final static int portnumber=10000;
	public static Map<Socket,String>mp = new HashMap<>();
	public static void main(String[] args) throws IOException
	{
		ServerSocket port;
		//open a port
		port = new ServerSocket(portnumber,3);
		System.out.println("waiting for clients");
		while(true)
		{
			//accept a connection
			Socket soc=port.accept();
			System.out.println("new user detected");
			//create a thread for newuser contacting server
			try{
				User newUser=new User(soc);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			
		}
	}
}
