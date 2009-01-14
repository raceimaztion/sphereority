package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;

/**
 * A player has joined the game
 * Contents:
 *    Player's id (2 bytes)
 *    Player's name (1 string)
 *    Player's team (1 byte)
 */
public class PlayerJoinMessage extends Message
{
	private char playerId;
	private String playerName;
	private byte playerTeam;
	
	/**
	 * Encode a new player join message
	 * @param playerId The new player's id
	 * @param playerName The new player's name
	 * @param playerTeam The new player's team
	 */
	public PlayerJoinMessage(char playerId, String playerName, byte playerTeam)
	{
		super(TYPE_PLAYER_JOIN);
		
		this.playerId = playerId;
		this.playerName = playerName;
		this.playerTeam = playerTeam;
	}
	
	/**
	 * Decode a player join message
	 * @param message The message to decode
	 * @throws Exception If the byte array is not actually a player death message, then an exception is thrown
	 */
	public PlayerJoinMessage(byte[] message) throws Exception
	{
		super(message);
		
		if (message[1] != TYPE_PLAYER_JOIN)
			throw new InvalidParameterException(String.format("The byte array passed to the PlayerJoinMessage class is NOT a player join message. Message code is 0x%02x.", message[1]));
		
		ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
		// Player id
		playerId = ByteStreamUtils.readChar(in);
		// Player name
		playerName = ByteStreamUtils.readString(in);
		// Player team
		playerTeam = ByteStreamUtils.readByte(in);
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Player id
		ByteStreamUtils.write(out, playerId);
		// Player name
		ByteStreamUtils.write(out, playerName);
		// Player team
		ByteStreamUtils.write(out, playerTeam);
		
		return out.toByteArray();
	}
	
	public char getPlayerId()
	{
		return playerId;
	}
	
	public String getPlayerName()
	{
		return playerName;
	}
	
	public byte getPlayerTeam()
	{
		return playerTeam;
	}
}
