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
        super(TYPE_SERVER_INFO_RESPONSE);
      
        if (message[1] != TYPE_SERVER_INFO_RESPONSE)
            throw new InvalidParameterException(String.format("The byte array passed to the ServerInfoResponse class is NOT a server info response message. Message code is 0x%02x.", message[1]));
        
        ByteArrayInputStream in = new ByteArrayInputStream(message);
        // Game type
        type = (byte)in.read();
        // Number of players
        numPlayers = (char)((0xFF & in.read())<<8 + 0xFF&in.read());
        // Player names
        playerNames = new String[numPlayers];
        for (int i=0; i < numPlayers; i++)
        {
            // TODO: figure out how to read a string out of a "BAIS"
        }
    }
    
    protected byte[] getMessageContents()
    {
        // To avoid the write(byte[]) method that throws IOExceptions
        byte[] temp;
        
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        // Game type
        out.write(type);
        // Number of players
        out.write(numPlayers >> 8);
        out.write(0xFF & numPlayers);
        // Player names
        for (int i=0; i < numPlayers; i++)
        {
            temp = Message.convertStringToByteArray(playerNames[i]);
            out.write(temp, 0, temp.length);
            out.write(0);
        }
        // Player teams
        for (int i=0; i < numPlayers; i++)
            out.write(playerTeams[i]);
        // Map name
        temp = Message.convertStringToByteArray(mapName);
        out.write(temp, 0, temp.length);
        // Map
        temp = Message.convertStringToByteArray(map);
        out.write(temp, 0, temp.length);
        
        return out.toByteArray();
    }

}
