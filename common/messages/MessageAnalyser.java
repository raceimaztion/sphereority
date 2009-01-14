package common.messages;

public class MessageAnalyser implements MessageConstants
{
    /**
     * Create an instance of a Message subclass for the specified message 
     * @param message The byte array that represents the message
     * @return The message wrapped in a Message subclass
     */
    public static Message getMessageFromArray(byte[] message)
    {
        if (!isValidMessage(message))
            return null;

        switch (getMessageType(message))
        {
            case TYPE_CHAT_MESSAGE:
            	try
            	{
            		return new ChatMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}

            case TYPE_HEALTH_UPDATE:
            	try
            	{
            		return new HealthUpdateMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}

            case TYPE_LOGIN_REQUEST:
                try
                {
                    return new LoginRequestMessage(message);
                }
                catch (Exception er)
                {
                    return null;
                }

            case TYPE_LOGIN_RESPONSE:
            	try
            	{
            		return new LoginResponseMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_MULTICAST_CHANGE:
            	try
            	{
            		return new MulticastChangeMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
                
            case TYPE_PING_REQUEST:
            	try
            	{
            		return new PingRequestMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_PING_RESPONSE:
            	try
            	{
            		return new PingResponseMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_PLAYER_DEATH:
            	try
            	{
            		return new PlayerDeathMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_PLAYER_JOIN:
            	try
            	{
            		return new PlayerJoinMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_PLAYER_LEAVE:
            	try
            	{
            		return new PlayerLeaveMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_PLAYER_MOTION:
            	try
            	{
            		return new PlayerMotionMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_PLAYER_RESPAWN:
            	try
            	{
            		return new PlayerRespawnMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_PROJECTILE_LAUNCH:
            	try
            	{
            		return new ProjectileLaunchMessage(message);
            	}
            	catch (Exception er)
            	{
            		return null;
            	}
            
            case TYPE_SERVER_INFO_REQUEST:
                try
                {
                    return new ServerInfoRequestMessage(message);
                }
                catch (Exception er)
                {
                    return null;
                }
            
            case TYPE_SERVER_INFO_RESPONSE:
                try
                {
                    return new ServerInfoResponseMessage(message);
                }
                catch (Exception er)
                {
                    return null;
                }

            default:
                System.out.printf("Unknown message type 0x%02x.\n", getMessageType(message));
                return null;
        }
    }

    /**
     * Find out what type this message is
     * 
     * @param message
     *            The array of bytes that represent a message
     * @return The message type if message is a valid message or -1 if it's not
     *         a valid message
     */
    public static int getMessageType(byte[] message)
    {
        if (message == null || message[0] != GAME_MAGIC_NUMBER)
            return -1;

        return message[1];
    }

    /**
     * Find out if a message is a valid message (note that it doesn't check to
     * see if the message type is supported)
     * 
     * @param message
     *            The byte array that represent a message
     * @return True if message is valid, false otherwise, including a null
     *         message
     */
    public static boolean isValidMessage(byte[] message)
    {
        if (message == null)
            return false;

        return message[0] == GAME_MAGIC_NUMBER;
    }
}
