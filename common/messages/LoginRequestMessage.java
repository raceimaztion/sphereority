package common.messages;

import java.io.UnsupportedEncodingException;

public class LoginRequestMessage extends Message
{
    private String userName, password;
    
    public LoginRequestMessage(String userName, String password)
    {
        super(TYPE_LOGIN_REQUEST);
        
        this.userName = userName;
        if (password == null)
            this.password = "";
        else
            this.password = password;
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
