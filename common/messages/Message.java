package common.messages;

import java.net.InetSocketAddress;

public abstract class Message implements MessageConstants
{
	/**
	 * The size of all message headers in bytes
	 */
	public static final int MESSAGE_HEADER_SIZE = 2;
	
	protected int messageType;
    protected byte[] messageBytes;
    
    /**
     * The ip address and port that originated this packet
     */
    protected InetSocketAddress source;
	
    /**
     * Create a new message of a particular type.
     * @param messageType
     */
	protected Message(int messageType)
	{
		this.messageType = messageType;
		
        messageBytes = null;
        source = null;
	}
    
    /**
     * Create a message from a preexisting byte array
     * @param message
     */
    protected Message(byte[] message, InetSocketAddress source)
    {
    	if (message == null)
    		throw new NullPointerException("All Message classes MUST be passed a non-null byte array message for decoding!");
    	
        messageType = message[1];
        messageBytes = message;
        this.source = source;
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
     * For messages that have come in from the network
     * @return The InetSocketAddress that originated this message
     */
    public InetSocketAddress getSource()
    {
    	return source;
    }
    
    /**
     * Find out if we sent this message
     * @return True if it's a local message, or false if it came through the network from another machine
     */
    public boolean isMyMessage()
    {
    	// If we don't have a network address on this message, it's definitely ours
    	if (source == null)
    		return true;
    	
    	// Otherwise, if the address is ours, it came from us
    	// TODO: Figure out how to ask a InetAddress if it's the local computer's address
    	return false;
    }
}
