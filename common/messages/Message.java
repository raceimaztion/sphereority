package common.messages;

public abstract class Message implements MessageConstants
{
	protected int messageType;
	
    /**
     * Create a new message of a particular type.
     * @param messageType
     */
	protected Message(int messageType)
	{
		this.messageType = messageType;
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
	protected abstract byte[] getByteMessage();
    
    /**
     * Get the array of bytes that represent the entire message.
     * @return The byte array that represents this message 
     */
    public byte[] getByteArray()
    {
        byte[] contents = getByteMessage();
        
        if (contents == null)
            return new byte[] { GAME_MAGIC_NUMBER, (byte)messageType }; 
        
        byte[] result = new byte[contents.length + 2];
        
        result[0] = GAME_MAGIC_NUMBER;
        result[1] = (byte)messageType;
        for (int i=0; i < contents.length; i++)
            result[i+2] = contents[i];
        
        return result;
    }
}
