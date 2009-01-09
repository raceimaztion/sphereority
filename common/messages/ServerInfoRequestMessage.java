package common.messages;

/**
 * This signals that a client would like some information about a particular server.
 * No additional information.
 */
public class ServerInfoRequestMessage extends Message
{
    public ServerInfoRequestMessage()
    {
        super(TYPE_SERVER_INFO_REQUEST);
    }
    
    public byte[] getByteMessage()
    {
        // We have no addition information
        return null;
    }

}
