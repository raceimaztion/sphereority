package common.messages;

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
        
        byte[] result = new byte[contents.length + 2];
        
        result[0] = GAME_MAGIC_NUMBER;
        result[1] = (byte)messageType;
        for (int i=0; i < contents.length; i++)
            result[i+2] = contents[i];
        
        messageBytes = result;
        
        return result;
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
    
    public static int findNextZero(byte[] array, int offset)
    {
        for (int i=offset+1; i < array.length; i++)
            if (array[i] == 0)
                return i;
        
        return -1;
    }
}
