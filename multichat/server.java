import java.io.*;
import java.net.*;

class B1aServer3336{

	final int portNumber=10000;
	public void formSession() throws IOException
	{
		//opens a port
		ServerSocket port = new ServerSocket(portNumber);
		System.out.println("Waiting for clients");
		//form a connection between server and client at port when it recieves a client request
		Socket soc = port.accept(); 
		//streams	
		BufferedReader input;
		DataInputStream din;
		DataOutputStream dout;
		try
		{
			//user input stream
			input=new BufferedReader(new InputStreamReader(System.in));
			//stream to read from socket
			din =new DataInputStream(soc.getInputStream());
			//stream to write to socket
			dout =new DataOutputStream(soc.getOutputStream());
		}
		catch(Exception e)
		{
			System.out.println("error in stream formation");
			return;
		}
		//loop goes on till connection
		while(true)
		{	
			//read message from socket 
			String str;
			str=din.readUTF();
			System.out.println("from client: "+str);
			if(str.equals("bye"))
			{
				dout.writeUTF("bye");
				break;
			}
			System.out.print("to client: ");
			//read a string 
			str=input.readLine();
			System.out.println(str);
			//write to socket
			dout.writeUTF(str);
			
		}
		//close the socket
		soc.close();
		//close streams
		input.close();
		din.close();
		dout.close();
	}

	public static void main(String[] agrs)
	{
		B1aServer3336 obj=new B1aServer3336();
		try
		{
			obj.formSession();
		}
		catch(IOException e)
		{
			System.out.println("error in i/o");
		}
	}
}