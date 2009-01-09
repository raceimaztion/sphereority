package client.net;

import java.io.IOException;

import common.*;
import common.messages.*;

/**
 * This interface is a generic network communication framework
 * @author dvanhumb
 */
public interface NetConnection extends Constants
{
	/**
	 * Add a listener that wants to know about recieved network messages 
	 * @param listener The listener to register
	 */
	public void addNetMessageListener(NetMessageListener listener);
	
	/**
	 * Remove a network listener
	 * @param listener The listener to unregister
	 */
	public void removeNetMessageListener(NetMessageListener listener);
	
	/**
	 * Send a message through this connection
	 * @param m The message to send
	 */
	public void sendMessage(Message m) throws IOException;
}
