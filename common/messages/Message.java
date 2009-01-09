package common.messages;

public abstract class Message implements MessageConstants
{
	protected int messageType;
	
	protected Message(int messageType)
	{
		this.messageType = messageType;
	}
	
	public int getMessageType()
	{
		return messageType;
	}
	
	public abstract byte[] getByteMessage();
}
