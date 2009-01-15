package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

/**
 * Somebody's health status has changed
 * Contents:
 *    Player id (2 bytes)
 *    Health value (2 bytes)
 */
public class HealthUpdateMessage extends Message
{
	private char playerId;
	private char health;
	
	/**
	 * Encode a new health update message
	 * @param playerId The player who's health has changed
	 * @param health The new value for their health
	 */
	public HealthUpdateMessage(char playerId, char health)
	{
		super(TYPE_HEALTH_UPDATE);
		
		this.playerId = playerId;
		this.health = health;
	}
	
	/**
	 * Decode a health update message
	 * @param message
	 * @throws Exception If the given message is not a health update message, an exception is thrown
	 */
	public HealthUpdateMessage(byte[] message, InetSocketAddress source) throws Exception
	{
		super(message, source);
		
        if (message[1] != TYPE_PLAYER_DEATH)
            throw new InvalidParameterException(String.format("The byte array passed to the HealthUpdateMessage class is NOT a health update message. Message code is 0x%02x.", message[1]));
        
        // Skip the header
        ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
        // Player id
        playerId = ByteStreamUtils.readChar(in);
        // Health
        health = ByteStreamUtils.readChar(in);
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Player id
		ByteStreamUtils.write(out, playerId);
		// Health
		ByteStreamUtils.write(out, health);
		
		return out.toByteArray();
	}

	public char getHealth()
	{
		return health;
	}

	public char getPlayerId()
	{
		return playerId;
	}
}
