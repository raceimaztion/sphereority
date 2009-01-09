package common.messages;

import java.io.UnsupportedEncodingException;
import java.security.InvalidParameterException;

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
        super(TYPE_LOGIN_REQUEST);
        
        if (message[1] != (byte)messageType)
            throw new InvalidParameterException(String.format("The byte array passed to the LoginRequestMessage class is not a login request message. Message number is 0x%02x.", message[1]));
        
        // Ignore the first two bytes, they're the magic number and the message type
        try
        {
            int first, second;
            for (first=2; first < message.length && message[first] != 0; first++);
            for (second=first+1; second < message.length && message[second] != 0; second++);
            userName = new String(message, 2, first-2, STRING_CHARSET);
            password = new String(message, first+1, second-first-1, STRING_CHARSET);
        }
        catch (UnsupportedEncodingException er)
        {
            System.err.printf("Fatal error: Character set 'UTF-8' is not supported!");
        }
    }
    
    public String getUserName()
    {
        return userName;
    }
    
    public String getPassword()
    {
        return password;
    }
    
    protected byte[] getByteMessage()
    {
        try
        {
            byte[] uname = userName.getBytes(STRING_CHARSET),
                   pword = password.getBytes(STRING_CHARSET);
            byte[] result = new byte[uname.length + pword.length + 2];
            
            for (int i=0; i < uname.length; i++)
                result[i] = uname[i];
            result[uname.length] = 0;
            
            for (int i=0,j=uname.length+1; i < pword.length; i++,j++)
                result[j] = pword[i];
            result[result.length-1] = 0;
            
            return result;
        }
        catch (UnsupportedEncodingException er)
        {
            System.err.println("Fatal error: Character set 'UTF-8' is not supported!");
            return null;
        }
    }
}
