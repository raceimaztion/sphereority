package common.messages;

import java.security.InvalidParameterException;

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
    
    public ServerInfoRequestMessage(byte[] message) throws Exception
    {
        super(message);
        
        if (message[1] != TYPE_SERVER_INFO_REQUEST)
            throw new InvalidParameterException(String.format("The byte array passed to the ServerInfoRequestMessage class is NOT a server info request message. Message code is 0x%02x.", message[1]));
        
        // Nothing else to do
    }
    
    public byte[] getMessageContents()
    {
        // We have no addition information
        return null;
    }

}
