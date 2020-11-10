import java.io.*;
import java.net.*;
import java.lang.Thread;



class B2bClient3336{

    private final static int port = 10000;
    private  String username;
    private DatagramSocket cltSocket;
    private InetAddress ip;
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
                    // dout.writeUTF(str);
                    byte[] buff=str.getBytes();
                    // System.out.println("in send client "+str);
                    DatagramPacket p = new DatagramPacket(buff, buff.length,ip, 10000); 
                    cltSocket.send(p);
                    if(str.equals("leave")){
                        {
                            // dout.close();
                            // din.close();
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
                // if(din.available()>0)
                // { 
                    // System.out.println(din.available());
                    byte[] buff=new byte[2048];
                    DatagramPacket p = new DatagramPacket(buff, buff.length); 
                    cltSocket.receive(p);
                    // StringBuilder sb=new StringBuilder();
                    // for(int i=0;i<buff.length;i++)
                    // {
                    //     sb.append((char)(buff[i]));
                    // }
                    // System.out.println(sb.toString());
                    String str=new String(p.getData());
                    str=str.replaceFirst("\u0000+$", "");
                    // System.out.println("hello in client recv"+str);
                    String token[] = str.split(":");
                     if(token.length==2){
                         System.out.println(token[0]);
                         System.out.println("  -->"+token[1]);
                     }
                     else{
                         System.out.println(token[0]);
                     } 
                // }       
            }
        }

    }


    public void newClient() throws IOException
    {
        System.out.println("[+] Client Started...");
        //establish connection to server on port 10000
        cltSocket = new DatagramSocket(); 
        ip = InetAddress.getLocalHost();         
        System.out.println("[+] Connection established...");
        userInput = new BufferedReader(new InputStreamReader(System.in));  
        System.out.print("Enter username: ");
        username = userInput.readLine();
        // System.out.println(username);
        byte[] buff=username.getBytes();
        DatagramPacket p=new DatagramPacket(buff, buff.length,ip, 10000); 
        cltSocket.send(p);
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
        B2bClient3336 obj=new B2bClient3336();
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
