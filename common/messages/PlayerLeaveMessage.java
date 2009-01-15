package common.messages;

import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

/**
 * Player has left the game (for whatever reason)
 * Contents:
 *    Player's id (2 bytes)
 */
public class PlayerLeaveMessage extends Message
{
	private char playerId;
	
	/**
	 * Encode a new player leave message
	 * @param playerId The id of the leaving player
	 */
	public PlayerLeaveMessage(char playerId)
	{
		super(TYPE_PLAYER_LEAVE);
		
		this.playerId = playerId;
	}
	
	/**
	 * Decode a player leave message
	 * @param message
	 * @throws Exception If the message given is not a player leave message, an exception is thrown
	 */
	public PlayerLeaveMessage(byte[] message, InetSocketAddress source) throws Exception
	{
		super(message, source);
		
		if (message[1] != TYPE_PLAYER_LEAVE)
			throw new InvalidParameterException(String.format("The byte array passed to the PlayerLeaveMessage class is NOT a player leave message. Message code is 0x%02x.", message[1]));
		
		playerId = (char)(((0xff & message[MESSAGE_HEADER_SIZE]) << 8) | (0xff & message[MESSAGE_HEADER_SIZE + 1]));
	}
	
	protected byte[] getMessageContents()
	{
		return new byte[] { (byte)(0xff & (playerId >> 8)), (byte)(0xff & playerId) };
	}
	
	public char getPlayerId()
	{
		return playerId;
	}
}
