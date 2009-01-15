package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

import common.Position;

/**
 * This means a player has moved.
 * Contents:
 *    Player id (2 bytes)
 *    Time of motion (1 float)
 *    Position (2 floats)
 *    Velocity (2 floats)
 *    Aim (2 floats)
 */
public class PlayerMotionMessage extends Message
{
	private char playerId;
	private float time;
	private Position position, velocity, aim;
	
	/**
	 * Encode a new player motion message
	 * @param playerId
	 * @param time
	 * @param position
	 * @param velocity
	 */
	public PlayerMotionMessage(char playerId, float time, Position position, Position velocity, Position aim)
	{
		super(TYPE_PLAYER_MOTION);
		
		this.playerId = playerId;
		this.time = time;
		this.position = new Position(position);
		this.velocity = new Position(velocity);
		this.aim = new Position(aim);
	}
	
	/**
	 * Decode a player motion message
	 * @param message
	 * @throws Exception If the given byte array is not a player motion message, an exception will be thrown
	 */
	public PlayerMotionMessage(byte[] message, InetSocketAddress source) throws Exception
	{
		super(message, source);
		
		if (message[1] != TYPE_PLAYER_MOTION)
			throw new InvalidParameterException(String.format("The byte array passed to the PlayerMotionMessage class is NOT a player motion message. Message code is 0x%02x.", message[1]));
		
		// Skip the header
		ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length - MESSAGE_HEADER_SIZE);
		// Player id
		playerId = ByteStreamUtils.readChar(in);
		// Time
		time = ByteStreamUtils.readFloat(in);
		// Position
		position = new Position(ByteStreamUtils.readFloat(in),
				ByteStreamUtils.readFloat(in));
		// Velocity
		velocity = new Position(ByteStreamUtils.readFloat(in),
				ByteStreamUtils.readFloat(in));
		// Aim
		aim = new Position(ByteStreamUtils.readFloat(in),
				ByteStreamUtils.readFloat(in));
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Player id
		ByteStreamUtils.write(out, playerId);
		// Time
		ByteStreamUtils.write(out, time);
		// Position
		ByteStreamUtils.write(out, position.getX());
		ByteStreamUtils.write(out, position.getY());
		// Velocity
		ByteStreamUtils.write(out, velocity.getX());
		ByteStreamUtils.write(out, velocity.getY());
		// Aim
		ByteStreamUtils.write(out, aim.getX());
		ByteStreamUtils.write(out, aim.getY());
		
		return out.toByteArray();
	}
	
	public Position getAim()
	{
		return aim;
	}

	public char getPlayerId()
	{
		return playerId;
	}

	public Position getPosition()
	{
		return position;
	}

	public float getTime()
	{
		return time;
	}

	public Position getVelocity()
	{
		return velocity;
	}
}
