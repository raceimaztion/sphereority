package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

/**
 * Indicates that all recipients should switch to the specified multicast channel
 * Contains:
 *    The new multicast address to use (1 string)
 *    The new multicast port to use (2 bytes);
 */
public class MulticastChangeMessage extends Message
{
	private String multicastAddress;
	private int multicastPort;
	
	/**
	 * Encode a new MulticastChangeMessage
	 * @param address The new multicast address to use
	 * @param port The new multicast port to use
	 */
	public MulticastChangeMessage(String address, int port)
	{
		super(TYPE_MULTICAST_CHANGE);
		
		multicastAddress = address;
		multicastPort = port;
	}
	
	public MulticastChangeMessage(byte[] message, InetSocketAddress source) throws Exception
	{
		super(message, source);
		
		if (message[1] != TYPE_MULTICAST_CHANGE)
            throw new InvalidParameterException(String.format("The byte array passed to the MulticastChangeMessage class is NOT a multicast change message. Message code is 0x%02x.", message[1]));
        
        // Ignore the first two bytes, they're the magic number and the message type
        ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
        // Addres
        multicastAddress = ByteStreamUtils.readString(in);
        // Port
        multicastPort = ByteStreamUtils.readChar(in);
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Address
		ByteStreamUtils.write(out, multicastAddress);
		// Port
		ByteStreamUtils.write(out, (char)multicastPort);
		
		return out.toByteArray();
	}

	public String getMulticastAddress()
	{
		return multicastAddress;
	}

	public int getMulticastPort()
	{
		return multicastPort;
	}
}
