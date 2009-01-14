package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.InvalidParameterException;

/**
 * A client will send this to a server to ask that it be included in the game
 * Contents:
 *   Player name (1 string)
 *   Server password (1 string, empty if not included)
 */
public class LoginRequestMessage extends Message
{
    private String userName, password;
    
    /**
     * Encode a new login request message
     * @param userName The name the new player would like to use
     * @param password The optional password to use on the server
     */
    public LoginRequestMessage(String userName, String password)
    {
        super(TYPE_LOGIN_REQUEST);
        
        this.userName = userName;
        if (password == null)
            this.password = "";
        else
            this.password = password;
    }
    
    /**
     * Decode a message into a login request message
     * @param message The byte array that encoded the message
     * @throws Exception If the byte array is not actually a login request message, then an exception is thrown
     */
    public LoginRequestMessage(byte[] message) throws Exception
    {
        super(message);
        
        if (message[1] != TYPE_LOGIN_REQUEST)
            throw new InvalidParameterException(String.format("The byte array passed to the LoginRequestMessage class is NOT a login request message. Message code is 0x%02x.", message[1]));
        
        // Skip the header
        ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
        userName = ByteStreamUtils.readString(in);
        password = ByteStreamUtils.readString(in);
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    protected byte[] getMessageContents()
    {
    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	
    	ByteStreamUtils.write(out, userName);
    	ByteStreamUtils.write(out, password);
    	
        return out.toByteArray();
    }
}
