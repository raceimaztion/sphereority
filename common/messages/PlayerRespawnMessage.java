package common.messages;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.InetSocketAddress;
import java.security.InvalidParameterException;

import common.Position;

/**
 * A player has respawned
 * Contents:
 *    The player's id (2 bytes)
 *    New position (2 floats)
 */
public class PlayerRespawnMessage extends Message
{
	private char playerId;
	private Position newPosition;
	
	/**
	 * Encode a new player respawn message
	 * @param playerId The id of the player who just respawned
	 * @param newPosition The position that they start in
	 */
	public PlayerRespawnMessage(char playerId, Position newPosition)
	{
		super(TYPE_PLAYER_RESPAWN);
		
		this.playerId = playerId;
		this.newPosition = newPosition;
	}
	
    /**
     * Decode a message into a player respawn message
     * @param message The byte array that encoded the message
     * @throws Exception If the byte array is not actually a player respawn message, then an exception is thrown
     */
	public PlayerRespawnMessage(byte[] message, InetSocketAddress source) throws Exception
	{
		super(message, source);
		
        if (message[1] != TYPE_PLAYER_RESPAWN)
            throw new InvalidParameterException(String.format("The byte array passed to the PlayerRespawnMessage class is NOT a player respawn message. Message code is 0x%02x.", message[1]));
        
        // Skip the header
        ByteArrayInputStream in = new ByteArrayInputStream(message, MESSAGE_HEADER_SIZE, message.length-MESSAGE_HEADER_SIZE);
        // Player id
        playerId = ByteStreamUtils.readChar(in);
        // New position
        newPosition = new Position(ByteStreamUtils.readFloat(in),
        		ByteStreamUtils.readFloat(in));
	}
	
	protected byte[] getMessageContents()
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		// Player id
		ByteStreamUtils.write(out, playerId);
		// New position
		ByteStreamUtils.write(out, newPosition.getX());
		ByteStreamUtils.write(out, newPosition.getY());
		
		return out.toByteArray();
	}

	public Position getNewPosition()
	{
		return newPosition;
	}

	public char getPlayerId()
	{
		return playerId;
	}
}
