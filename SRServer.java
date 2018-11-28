// A Java program for a Server
import java.net.*;
import java.io.*;

public class SRServer
{
	//initialize socket and input stream
	private Socket		 socket = null;
	private ServerSocket server = null;
	private DataInputStream in	 = null;
	private DataOutputStream out = null;

	// constructor with port
	public SRServer(int port)
	{
		// starts server and waits for a connection
		try
		{
			server = new ServerSocket(port);
			System.out.println("Server started");

			System.out.println("Waiting for a client ...");

			socket = server.accept();
			System.out.println("Client accepted");

			// takes input from the client socket
			in = new DataInputStream(
				new BufferedInputStream(socket.getInputStream()));
			out = new DataOutputStream(socket.getOutputStream());
			String line = "";
             
			int lastFrameSent=0;
			int window=Integer.parseInt(in.readUTF());
			System.out.println("Value of window:"+window);
			int frames=Integer.parseInt(in.readUTF());
			System.out.println("Value of frames:"+frames);
			boolean loop=true;
			int sent=1;
			// reads message from client until "Over" is sent
			int corr=0;
			while(loop)
			  {
				out.writeUTF("Window start");
				  for(int i=0;i<window;i++)
				  {
					  if(corr!=0)
						  {
						    System.out.println("Frame "+corr+" sent");
						    out.writeUTF(String.valueOf(corr));
			                if(corr==frames)
			                	break;
			                if(lastFrameSent==1)
			                	break;
						  }
					  else
					  {
						  System.out.println("Frame "+ sent+" sent");
						  
						  out.writeUTF(String.valueOf(sent));
						  if(sent==frames)
							  {
							    lastFrameSent=1;
							    break;
							  }
						  sent++;
			
					  }
					  corr=0;
					 
					  
				  }
				  
				  out.writeUTF("Window end");
				  try
				  {
				  Thread.sleep(5000);
				  }
				  catch(Exception e){}
				  System.out.println("Enter the frame that got corrupted:");
				  corr=Integer.parseInt(in.readUTF());
				  System.out.println(corr);
				  
				 
				  if(corr==0 && sent==frames)
					  loop=false;
				 
			  }
			out.writeUTF("Stop");
			System.out.println("Closing connection");

			// close connection
			socket.close();
			in.close();
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		SRServer server = new SRServer(7068);
	}
}
