import java.io.*;
import java.net.*;

class B1aClient3336{

	final int port=10000;

	public void formSession() throws IOException
	{
		//create a socket to connect to server accepts serverip and serverport;
		Socket serverConnection= new Socket("127.0.0.1",port);
		//streams
		BufferedReader input;
		DataInputStream din;
		DataOutputStream dout;
		
		try
		{
			//user input stream
			input=new BufferedReader(new InputStreamReader(System.in));
			//stream to read from socket i.e server
			din =new DataInputStream(serverConnection.getInputStream());
			//stream to write to socket i.e server
			dout =new DataOutputStream(serverConnection.getOutputStream());
	    }
	    catch(Exception e)
	    {
	    	System.out.println("error in creating streams");
	    	return;
	    }
	
		//loop goes on till connection
		while(true)
		{		
			System.out.print("to Server: ");
			//read a string 
			String str=input.readLine();
			System.out.println(str);		
			//write to socket
			dout.writeUTF(str);	
			//read message from socket
			str=din.readUTF();
			System.out.println("from server: "+str);
			if(str.equals("bye"))
			{
				break;
			}
		}
		//close the socket
		serverConnection.close();
		//close streams
		input.close();
		din.close();
		dout.close();
	}
	public static void main(String[] agrs)
	{
		B1aClient3336 obj =new B1aClient3336();
		try{
			obj.formSession();
		}
		catch(IOException e)
		{
			System.out.println("error in i/o");
		}
	}
}