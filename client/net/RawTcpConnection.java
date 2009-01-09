package client.net;

import java.io.*;
import java.net.*;
import java.util.Vector;

import common.Constants;
import common.messages.*;

/**
 * This is a raw TCP connection with minimal layers on top for 
 * @author dvanhumb
 *
 */
public class RawTcpConnection implements NetConnection, Constants, Runnable
{
	private boolean running = false;
	private Vector<NetMessageListener> listeners;
	
	private Socket socket;
	private OutputStream out;
	
	public RawTcpConnection(String hostname, int port) throws IOException, UnknownHostException
	{
		socket = new Socket(hostname, port);
		out = socket.getOutputStream();
		
		listeners = new Vector<NetMessageListener>();
	}
	
	public void addNetMessageListener(NetMessageListener listener)
	{
		listeners.add(listener);
	}

	public void removeNetMessageListener(NetMessageListener listener)
	{
		listeners.remove(listener);
	}

	public void sendMessage(Message m) throws IOException
	{
		out.write(m.getByteMessage());
	}
	
	public void start()
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void run()
	{
		while (running)
		{
			// TODO: write the recieve code here
			Thread.yield();
		}
	}
}
