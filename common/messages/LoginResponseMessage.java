package common.messages;

import java.io.ByteArrayInputStream;
import java.security.InvalidParameterException;

import common.Position;

/**
 * If a login is sucesssful, this message will fill in the new player with all they need to
 *   know about the game currently in progress. If a login fails, the connection is closed.
 * Contents:
 *    The new player's id (2 bytes)
 *    Map name (1 string)
 *    Map (1 string)
 *    Number of players (2 bytes)
 *    Per player:
 *       Player name (1 string)
 *       Player id (2 bytes)
 *       Team (1 byte)
 *    New player's initial position (2 floats or 2 ints)
 *    Number of seconds the current game has been running for (1 float)
 */
public class LoginResponseMessage extends Message
{
	private char playerId;
	private String mapName, map;
	private char numPlayers;
	private String[] playerNames;
	private char[] playerIds;
	private byte[] playerTeams;
	private Position initialPosition;
	private float gameTime;

	public LoginResponseMessage(char playerId, String mapName, String map, char numPlayers, String[] playerNames, char[] playerIds, byte[] playerTeams, Position initialPosition, float gameTime)
	{
		super(TYPE_LOGIN_RESPONSE);
		
		this.playerId = playerId;
		this.mapName = mapName;
		this.map = map;
		this.numPlayers = numPlayers;
		this.playerNames = playerNames;
		this.playerIds = playerIds;
		this.playerTeams = playerTeams;
		this.initialPosition = initialPosition;
	}
	
	public LoginResponseMessage(byte[] message)
	{
		super(message);
		
		if (message[1] != TYPE_LOGIN_RESPONSE)
            throw new InvalidParameterException(String.format("The byte array passed to the LoginResponseMessage class is NOT a login response message. Message code is 0x%02x.", message[1]));
		
		// Ignore the first two bytes, they're the game magic number and message type
		ByteArrayInputStream in = new ByteArrayInputStream(message, 2, message.length-2);
		// Player id
	}
	
	protected byte[] getMessageContents()
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	public float getGameTime()
	{
		return gameTime;
	}

	public Position getInitialPosition()
	{
		return initialPosition;
	}

	public String getMap()
	{
		return map;
	}

	public String getMapName()
	{
		return mapName;
	}

	public char getNumPlayers()
	{
		return numPlayers;
	}

	public char getPlayerId()
	{
		return playerId;
	}

	public char[] getPlayerIds()
	{
		return playerIds;
	}

	public String[] getPlayerNames()
	{
		return playerNames;
	}

	public byte[] getPlayerTeams()
	{
		return playerTeams;
	}
}