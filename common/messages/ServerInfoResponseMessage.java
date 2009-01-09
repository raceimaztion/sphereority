package common.messages;

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
    public ServerInfoResponseMessage(byte type, char numPlayers, String[] playerName, byte[] playerTeam, String mapName, String map)
    {
        super(TYPE_SERVER_INFO_RESPONSE);
    }
    
    protected byte[] getByteMessage()
    {
        // TODO Auto-generated method stub
        return null;
    }

}
