package daryl;

import common.messages.*;

public class MessageTest
{
    public static void main(String[] args)
    {
        LoginRequestMessage msg = new LoginRequestMessage("User", "None");
        byte[] b_msg = msg.getByteArray();
        
        System.out.printf("Login request message is of length %d.\n", b_msg.length);
        System.out.printf("Username: '%s', Password: '%s'\n", msg.getUserName(), msg.getPassword());
        for (int i=0; i < b_msg.length; i++)
            System.out.printf("0x%02x  ", b_msg[i]);
    }
}
