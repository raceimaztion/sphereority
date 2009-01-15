package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

/**
 * A request that anyone hearing this message send a response.
 * Contains:
 *    Sender's player id (2 bytes) or 0xffff for the server
 *    Sender's current game time (1 float)
 */
public class PingRequestMessage extends Message
{
	private char playerId;
	private float currentTime;
	
	/**
	 * Encode a new ping request message
	 * @param playerId The id of the player requesting the response
	 * @param currentTime The requestor's current game time
	 */
	public PingRequestMessage(char playerId, float currentTime)
	{
		super(TYPE_PING_REQUEST);
		
		this.playerId = playerId;
		this.currentTime = currentTime;
	}
	
	/**
	 * Decode a ping request message
	 * @param message
	 * @throws Exception If there is a problem decoding the message, an exception is thrown
	 */
	public PingRequestMessage(byte[] message, InetSocketAddress source) throws Exception
	{
		super(message, source);
		
		if (message[1] != TYPE_PING_REQUEST)
			throw new InvalidParameterException(String.format("The byte array passed to the PingRequestMessage class is NOT a ping request message. Message code is 0x%02x.", message[1]));
		
		// Skip the header
		ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length - MESSAGE_HEADER_SIZE);
		// Player id
		playerId = ByteStreamUtils.readChar(in);
		// Current time
		currentTime = ByteStreamUtils.readFloat(in);
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Player id
		ByteStreamUtils.write(out, playerId);
		// Current time
		ByteStreamUtils.write(out, currentTime);
		
		return out.toByteArray();
	}

	public float getCurrentTime()
	{
		return currentTime;
	}

	public char getPlayerId()
	{
		return playerId;
	}
}
