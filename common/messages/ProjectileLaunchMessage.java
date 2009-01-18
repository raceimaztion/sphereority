package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

import common.Position;

/**
 * Somebody fired a projectile
 * Contents:
 *    Launching player's id (2 bytes)
 *    Point of origin (2 floats)
 *    Direction of motion (2 floats)
 *    Time of launch (1 float)
 *    Projectile type (1 byte)
 */
public class ProjectileLaunchMessage extends Message implements MessagePlayerId
{
	private char ownerId;
	private Position origin;
	private Position direction;
	private float launchTime;
	private byte type;
	
	/**
	 * Encode a new projectile launch message
	 * @param ownerId The id of the player who launched this projectile
	 * @param origin The point it was fired from
	 * @param direction The direction it was fired in
	 * @param launchTime The time it was fired
	 * @param type This projectile's type
	 */
	public ProjectileLaunchMessage(char ownerId, Position origin, Position direction, float launchTime, byte type)
	{
		super(TYPE_PROJECTILE_LAUNCH);
		
		this.ownerId = ownerId;
		this.origin = new Position(origin);
		this.direction = new Position(direction);
		this.launchTime = launchTime;
		this.type = type;
	}
	
	/**
	 * Decode a projectile launch message
	 * @param message The message to decode
	 * @throws Exception If the byte array provided is not a projectile launch message, an exception will be thrown
	 */
	public ProjectileLaunchMessage(byte[] message, InetSocketAddress source) throws Exception
	{
		super(message, source);
		
        if (message[1] != TYPE_PROJECTILE_LAUNCH)
            throw new InvalidParameterException(String.format("The byte array passed to the ProjectileLaunchMessage class is NOT a projectile launch message. Message code is 0x%02x.", message[1]));
        
        // Skip the header
        ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
        // Owner id
        ownerId = ByteStreamUtils.readChar(in);
        // Origin
        origin = new Position(ByteStreamUtils.readFloat(in),
        		ByteStreamUtils.readFloat(in));
        // Direction
        direction = new Position(ByteStreamUtils.readFloat(in),
        		ByteStreamUtils.readFloat(in));
        // Launch time
        launchTime = ByteStreamUtils.readFloat(in);
        // Type
        type = ByteStreamUtils.readByte(in);
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Owner id
		ByteStreamUtils.write(out, ownerId);
		// Origin
		ByteStreamUtils.write(out, origin.getX());
		ByteStreamUtils.write(out, origin.getY());
		// Direction
		ByteStreamUtils.write(out, direction.getX());
		ByteStreamUtils.write(out, direction.getY());
		// Launch time
		ByteStreamUtils.write(out, launchTime);
		// Type
		ByteStreamUtils.write(out, type);
		
		return out.toByteArray();
	}

	public Position getDirection()
	{
		return direction;
	}

	public float getLaunchTime()
	{
		return launchTime;
	}

	public Position getOrigin()
	{
		return origin;
	}

	public char getOwnerId()
	{
		return ownerId;
	}
    
    public char getPlayerId()
    {
        return ownerId;
    }

	public byte getType()
	{
		return type;
	}
}
