package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;

/**
 * Indicates that a player has died (possibly the recipient of the message)
 * Contents:
 *    The dead player's id (2 bytes)
 *    The id of the player who killed the above (2 bytes)
 */
public class PlayerDeathMessage extends Message
{
	private char deadPlayerId, killingPlayerId;

	/**
	 * Encode a new player death message
	 * @param deadPlayerId The id of the now-dead player
	 * @param killingPlayerId The id of the player who killed the player
	 */
	public PlayerDeathMessage(char deadPlayerId, char killingPlayerId)
	{
		super(TYPE_PLAYER_DEATH);
		
		this.deadPlayerId = deadPlayerId;
		this.killingPlayerId = killingPlayerId;
	}
	
    /**
     * Decode a message into a player death message
     * @param message The byte array that encoded the message
     * @throws Exception If the byte array is not actually a player death message, then an exception is thrown
     */
	public PlayerDeathMessage(byte[] message) throws Exception
	{
		super(message);
		
        if (message[1] != TYPE_PLAYER_DEATH)
            throw new InvalidParameterException(String.format("The byte array passed to the PlayerDeathMessage class is NOT a player death message. Message code is 0x%02x.", message[1]));
        
        // Ignore the first two bytes, they're the magic number and the message type
        ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
        // Dead player's id
        deadPlayerId = ByteStreamUtils.readChar(in);
        // Killing player's id
        killingPlayerId = ByteStreamUtils.readChar(in);
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Dead player's id
		ByteStreamUtils.write(out, deadPlayerId);
		// Killing player's id
		ByteStreamUtils.write(out, killingPlayerId);
		
		return out.toByteArray();
	}
	
	public char getDeadPlayerId()
	{
		return deadPlayerId;
	}

	public char getKillingPlayerId()
	{
		return killingPlayerId;
	}
}
