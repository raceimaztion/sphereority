package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;

/**
 * A text chat message
 * Contains:
 *    Sender's player id (2 bytes)
 *    Message (1 string)
 */
public class ChatMessage extends Message
{
	private char playerId;
	private String message;
	
	/**
	 * Encode a new chat message
	 * @param playerId The id of the sending player
	 * @param message The message sent
	 */
	public ChatMessage(char playerId, String message)
	{
		super(TYPE_CHAT_MESSAGE);
		
		this.playerId = playerId;
		this.message = message;
	}
	
	public ChatMessage(byte[] message) throws Exception
	{
		super(message);
		
		if (message[1] != TYPE_CHAT_MESSAGE)
			throw new InvalidParameterException(String.format("The byte array passed to the ChatMessage class is NOT a chat message. Message code is 0x%02x.", message[1]));
		
		// Skip the header
		ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
		// Player id
		playerId = ByteStreamUtils.readChar(in);
		// Message
		this.message = ByteStreamUtils.readString(in);
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Player id
		ByteStreamUtils.write(out, playerId);
		// Message
		ByteStreamUtils.write(out, message);
		
		return out.toByteArray();
	}

	public String getMessage()
	{
		return message;
	}

	public char getPlayerId()
	{
		return playerId;
	}
}
