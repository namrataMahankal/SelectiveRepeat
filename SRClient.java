// A Java program for a Client
import java.net.*;
import java.io.*;
import java.util.*;

public class SRClient
{
	// initialize socket and input output streams
	private Socket socket		 = null;
	private DataInputStream input = null;
	private DataInputStream in = null;
	private DataOutputStream out	 = null;
	ArrayList<Integer> a = new ArrayList<>();
   Scanner sc = new Scanner(System.in);
	// constructor to put ip address and port
	public SRClient(String address, int port)
	{
		// establish a connection
		try
		{
			socket = new Socket(address, port);
			System.out.println("Connected");

			// takes input from terminal
			input = new DataInputStream(System.in);
			in = new DataInputStream(
					new BufferedInputStream(socket.getInputStream()));

			// sends output to the socket
			out = new DataOutputStream(socket.getOutputStream());
		}
		catch(UnknownHostException u)
		{
			System.out.println(u);
		}
		catch(IOException i)
		{
			System.out.println(i);
		}

		// string to read message from input
		String line = "";
		
		//==============================================
		System.out.println("Enter window Size:");
		int window=sc.nextInt();
		System.out.println("Enter frames:");
		int frames=sc.nextInt();
		try {
			out.writeUTF(String.valueOf(window));
			out.writeUTF(String.valueOf(frames));
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// keep reading until "Over" is input
		while (true)
		{
			try
			{
				line = in.readUTF();
				//System.out.println("Received line:"+line);
				if(line.equals("Stop"))
					{
					  System.out.println("Connection closed*************");
					  break;
					}
				else if(line.equals("Window start"))
				{
					a.clear();
					a.add(0);
					System.out.println("=============================");
					String num=in.readUTF();
					while(!num.equals("Window end"))
					{
						a.add(Integer.parseInt(num));
						//System.out.println("Frame "+num+" received");
						num=in.readUTF();
					}
					Random r = new Random();
					int corr=a.get(r.nextInt(a.size()));
					for(int i=1;i<a.size();i++)
					{
						int sample=a.get(i).intValue();
						if(sample!=corr)
							System.out.println("Frame "+sample+" received");
						else
							System.out.println("Frame "+sample+" got corrupted");
					}
					out.writeUTF(String.valueOf(corr));
					System.out.println("=============================");
					
				}
			}
			catch(IOException i)
			{
				System.out.println(i);
			}
		}
		
	
		
		

		// close the connection
		try
		{
			input.close();
			out.close();
			socket.close();
		}
		catch(IOException i)
		{
			System.out.println(i);
		}
	}

	public static void main(String args[])
	{
		SRClient client = new SRClient("127.0.0.1", 7068);
	}
}
