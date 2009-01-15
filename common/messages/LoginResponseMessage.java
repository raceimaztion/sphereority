package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

import common.Position;

/**
 * If a login is sucesssful, this message will fill in the new player with all they need to
 *   know about the game currently in progress. If a login fails, the connection is closed.
 * Contents:
 *    The new player's id (2 bytes)
 *    The new player's team (1 byte)
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
	private byte playerTeam;
	private String mapName, map;
	private char numPlayers;
	private String[] playerNames;
	private char[] playerIds;
	private byte[] playerTeams;
	private Position initialPosition;
	private float gameTime;

	public LoginResponseMessage(char playerId, byte playerTeam, String mapName, String map, char numPlayers, String[] playerNames, char[] playerIds, byte[] playerTeams, Position initialPosition, float gameTime)
	{
		super(TYPE_LOGIN_RESPONSE);
		
		this.playerId = playerId;
		this.playerTeam = playerTeam;
		this.mapName = mapName;
		this.map = map;
		this.numPlayers = numPlayers;
		this.playerNames = playerNames;
		this.playerIds = playerIds;
		this.playerTeams = playerTeams;
		this.initialPosition = initialPosition;
	}
	
	public LoginResponseMessage(byte[] message, InetSocketAddress source)
	{
		super(message, source);
		
		if (message[1] != TYPE_LOGIN_RESPONSE)
            throw new InvalidParameterException(String.format("The byte array passed to the LoginResponseMessage class is NOT a login response message. Message code is 0x%02x.", message[1]));
		
		// Skip the header
		ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
		// Player id
		playerId = ByteStreamUtils.readChar(in);
		// Player team
		playerTeam = ByteStreamUtils.readByte(in);
		// Map name
		mapName = ByteStreamUtils.readString(in);
		// Map
		map = ByteStreamUtils.readString(in);
		// Number of players
		numPlayers = ByteStreamUtils.readChar(in);
		// All player names
		playerNames = new String[numPlayers];
		for (int i=0; i < numPlayers; i++)
			playerNames[i] = ByteStreamUtils.readString(in);
		// All player ids
		playerIds = new char[numPlayers];
		for (int i=0; i < numPlayers; i++)
			playerIds[i] = ByteStreamUtils.readChar(in);
		// All player teams
		playerTeams = new byte[numPlayers];
		for (int i=0; i < numPlayers; i++)
			playerTeams[i] = ByteStreamUtils.readByte(in);
		// Initial position
		initialPosition = new Position(ByteStreamUtils.readFloat(in),
				ByteStreamUtils.readFloat(in));
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Player id
		ByteStreamUtils.write(out, playerId);
		// Player team
		ByteStreamUtils.write(out, playerTeam);
		// Map name
		ByteStreamUtils.write(out, mapName);
		// Map
		ByteStreamUtils.write(out, map);
		// Number of players
		ByteStreamUtils.write(out, numPlayers);
		// All player names
		for (int i=0; i < numPlayers; i++)
			ByteStreamUtils.write(out, playerNames[i]);
		// All player ids
		for (int i=0; i < numPlayers; i++)
			ByteStreamUtils.write(out, playerIds[i]);
		// All player teams
		for (int i=0; i < numPlayers; i++)
			ByteStreamUtils.write(out, playerTeams[i]);
		// Initial position
		ByteStreamUtils.write(out, initialPosition.getX());
		ByteStreamUtils.write(out, initialPosition.getY());
		
		return out.toByteArray();
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
	
	public byte getPlayerTeam()
	{
		return playerTeam;
	}
	
	public byte[] getPlayerTeams()
	{
		return playerTeams;
	}
}
