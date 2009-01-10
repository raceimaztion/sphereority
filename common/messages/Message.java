package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;


public abstract class Message implements MessageConstants
{
	protected int messageType;
    protected byte[] messageBytes;
	
    /**
     * Create a new message of a particular type.
     * @param messageType
     */
	protected Message(int messageType)
	{
		this.messageType = messageType;
        messageBytes = null;
	}
    
    /**
     * Create a message from a preexisting byte array
     * @param message
     */
    protected Message(byte[] message)
    {
        messageType = message[1];
        messageBytes = message;
    }
	
    /**
     * Get this message's type (one of MessageConstants.TYPE_*)
     * @return This message's type
     */
	public int getMessageType()
	{
		return messageType;
	}
	
    /**
     * Get the additional contents of this message (note that this does not include the header!)
     * Please avoid using this and instead use getByteArray()
     * @return The contents of this message as a byte array
     */
	protected abstract byte[] getMessageContents();
    
    /**
     * Get the array of bytes that represent the entire message.
     * @return The byte array that represents this message 
     */
    public byte[] getMessageBytes()
    {
        if (messageBytes != null)
            return messageBytes;
        
        byte[] contents = getMessageContents();
        
        if (contents == null)
            return new byte[] { GAME_MAGIC_NUMBER, (byte)messageType };
        
        messageBytes = new byte[contents.length + 2];
        
        messageBytes[0] = GAME_MAGIC_NUMBER;
        messageBytes[1] = (byte)messageType;
        for (int i=0; i < contents.length; i++)
            messageBytes[i+2] = contents[i];
        
        return messageBytes;
    }
    
    /**
     * Convert a string to an array of bytes
     * @param s The string to convert
     * @return The resulting array of bytes
     */
    public static byte[] convertStringToByteArray(String s)
    {
        try
        {
            return s.getBytes(STRING_CHARSET);
        }
        catch (UnsupportedEncodingException er)
        {
            System.err.println("Fatal error: The character encoding 'UTF-8' is not supported on this platform!");
            return s.getBytes();
        }
    }
    
    /**
     * Writes a zero-terminated string to a ByteArrayOutputStream
     * @param s The string to write
     * @param out The stream to write to
     */
    public static void addStringToByteOutputStream(String s, ByteArrayOutputStream out)
    {
        byte[] temp = convertStringToByteArray(s);
        out.write(temp, 0, temp.length);
        out.write(0);
    }
    
    /**
     * Convert an array of bytes to a string
     * @param array The array of bytes to convert
     * @return The resulting string
     */
    public static String convertByteArrayToString(byte[] array)
    {
        try
        {
            return new String(array, STRING_CHARSET);
        }
        catch (UnsupportedEncodingException er)
        {
            System.err.println("Fatal error: The character encoding 'UTF-8' is not supported on this platform!");
            return new String(array);
        }
    }
    
    /**
     * Convert part of an array of bytes to a string
     * @param array The array of bytes to convert
     * @param offset The offset into the array
     * @param length The length of the string (should not end with 0x00)
     * @return The resulting string
     */
    public static String convertByteArrayToString(byte[] array, int offset, int length)
    {
        try
        {
            return new String(array, offset, length, STRING_CHARSET);
        }
        catch (UnsupportedEncodingException er)
        {
            System.err.println("Fatal error: The character encoding 'UTF-8' is not supported on this platform!");
            return new String(array, offset, length);
        }
    }
    
    /**
     * Reads a zero-terminated string from the stream
     * @param in
     * @return
     */
    public static String readStringFromByteArrayInputStream(ByteArrayInputStream in)
    {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        
        b = in.read();
        while (b != -1 && b != 0)
        {
            out.write(b);
            
            b = in.read();
        }
        
        return convertByteArrayToString(out.toByteArray());
    }
}
