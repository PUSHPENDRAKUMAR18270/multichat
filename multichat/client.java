import java.io.*;
import java.net.*;
import java.lang.Thread;



public class B1bClient3336{

    private final static int port = 10000;
    private  String username;
    private Socket cltSocket;
    private DataOutputStream dout;
    private DataInputStream din;
    private BufferedReader userInput;
    Thread t1,t2;
    //inner class
    class sendThread extends Thread{

        @Override
        public void run() {
            try{
                send();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
        }

        //send function
        void send() throws IOException{
            
            while(true){
                // System.out.println("say what you want to write");
                String str = userInput.readLine();
                if(str.length()>0)
                {
                    dout.writeUTF(str);
                    if(str.equals("leave")){
                        {
                            dout.close();
                            din.close();
                            cltSocket.close();
                            System.exit(0);
                            return;
                        }
                    } 
                }          
            }
        }

    }

    //inner class
    class recvThread extends Thread{
        @Override
        public void run() {
            try{
                recv();
            }
            catch(Exception e){
                e.printStackTrace();
            }
            
            
        }


        void recv() throws IOException{
            while(true){
                //returns an estimate no of bytes to be read
                if(din.available()>0)
                { 
                    // System.out.println(din.available());
                    String str = din.readUTF();
                    String token[] = str.split(":");
                     if(token.length==2){
                         System.out.println(token[0]);
                         System.out.println("  -->"+token[1]);
                     }
                     else{
                         System.out.println(token[0]);
                     } 
                }       
            }
        }

    }


    public void newClient() throws IOException
    {
        System.out.println("[+] Client Started...");
        //establish connection to server on port 10000
        cltSocket = new Socket("localhost",port);          
        System.out.println("[+] Connection established...");
                
        dout = new DataOutputStream(cltSocket.getOutputStream());
        din =  new DataInputStream(cltSocket.getInputStream());
        userInput = new BufferedReader(new InputStreamReader(System.in));  
        System.out.print("Enter username: ");
        username = userInput.readLine();
        System.out.println(username);
        dout.writeUTF(username);
        t1 = this.new sendThread();
        t2= this.new recvThread();
        t1.start();  
        t2.start();
        // cltSocket.close();
        // din.close();
        // dout.close();
        // cltSocket.close();
    }

    public static void main(String []args) throws IOException,InterruptedException
    {
        B1bClient3336 obj=new B1bClient3336();
        try{
            obj.newClient();
        }
        catch(Exception e)
        {
            e.printStackTrace();
            return;
        }
    }

}
