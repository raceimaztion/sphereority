package common.messages;

import java.io.ByteArrayInputStream;
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
    
    
    public LoginRequestMessage(byte[] message) throws Exception
    {
        super(message);
        
        if (message[1] != (byte)messageType)
            throw new InvalidParameterException(String.format("The byte array passed to the LoginRequestMessage class is NOT a login request message. Message code is 0x%02x.", message[1]));
        
        // Ignore the first two bytes, they're the magic number and the message type
        ByteArrayInputStream in = new ByteArrayInputStream(message, 2, message.length-2);
        userName = readStringFromByteArrayInputStream(in);
        password = readStringFromByteArrayInputStream(in);
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
        byte[] uname = Message.convertStringToByteArray(userName),
               pword = Message.convertStringToByteArray(password);
        byte[] result = new byte[uname.length + pword.length + 2];
        
        for (int i=0; i < uname.length; i++)
            result[i] = uname[i];
        result[uname.length] = 0;
        
        for (int i=0,j=uname.length+1; i < pword.length; i++,j++)
            result[j] = pword[i];
        result[result.length-1] = 0;
        
        return result;
    }
}
