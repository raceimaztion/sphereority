package common.messages;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.security.InvalidParameterException;

/**
 * This is the server's response to a client's request for server information
 * Contents:
 *   Game type (1 byte)
 *   Number of players (2 bytes)
 *   Per player:
 *      Player name (1 string)
 *      Team (1 byte)
 *   Map name (1 string)
 *   Map (1 string)
 */
public class ServerInfoResponseMessage extends Message
{
    private byte type;
    private char numPlayers;
    private String[] playerNames;
    private byte[] playerTeams;
    private String mapName, map;
    
    public ServerInfoResponseMessage(byte type, char numPlayers, String[] playerNames, byte[] playerTeams, String mapName, String map)
    {
        super(TYPE_SERVER_INFO_RESPONSE);
        
        this.type = type;
        this.numPlayers = numPlayers;
        this.playerNames = playerNames;
        this.playerTeams = playerTeams;
        this.mapName = mapName;
        this.map = map;
    }
    
    public ServerInfoResponseMessage(byte[] message)
    {
        super(message);
      
        if (message[1] != TYPE_SERVER_INFO_RESPONSE)
            throw new InvalidParameterException(String.format("The byte array passed to the ServerInfoResponse class is NOT a server info response message. Message code is 0x%02x.", message[1]));
        
        // Skip the header
        ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length - MESSAGE_HEADER_SIZE);
        // Game type
        type = (byte)in.read();
        // Number of players
        numPlayers = (char)((0xFF & in.read())<<8 + 0xFF&in.read());
        // Player names
        playerNames = new String[numPlayers];
        for (int i=0; i < numPlayers; i++)
            playerNames[i] = ByteStreamUtils.readString(in);
        // Player teams
        playerTeams = new byte[numPlayers];
        for (int i=0; i < numPlayers; i++)
            playerTeams[i] = (byte)in.read();
        // Map name
        mapName = ByteStreamUtils.readString(in);
        // Map
        map = ByteStreamUtils.readString(in);
    }
    
    protected byte[] getMessageContents()
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Game type
        out.write(type);
        // Number of players
        out.write(numPlayers >> 8);
        out.write(0xFF & numPlayers);
        // Player names
        for (int i=0; i < numPlayers; i++)
        	ByteStreamUtils.write(out, playerNames[i]);
        // Player teams
        for (int i=0; i < numPlayers; i++)
            out.write(playerTeams[i]);
        // Map name
    	ByteStreamUtils.write(out, mapName);
        // Map
    	ByteStreamUtils.write(out, map);
        
        return out.toByteArray();
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

	public String[] getPlayerNames()
	{
		return playerNames;
	}

	public byte[] getPlayerTeams()
	{
		return playerTeams;
	}

	public byte getType()
	{
		return type;
	}
}
