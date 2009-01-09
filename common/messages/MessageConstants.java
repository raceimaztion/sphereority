package common.messages;

/**
 * Mostly message number constants, but also documents message contents
 * @author dvanhumb
 * All messages start with the GAME_MAGIC_NUMBER and the message type (both as single bytes) so we can identify each message as ours and what type they are.
 */
public interface MessageConstants
{
	/**
	 * This lets us identify messages from other software and ignore those not from our software
	 */
	public static final byte GAME_MAGIC_NUMBER = (byte)53;
	
	/**
	 * This signals that a client would like some information about a particular server
	 * Contents: No additional information
	 */
	public static final byte TYPE_SERVER_INFO_REQUEST = (byte)0x10;
	
	/**
	 * This is the server's response to a client's request for server information
	 * Contents:
	 *   Game type (1 byte)
	 *   Number of players (2 bytes)
	 *   Per player:
	 *      Player name (1 string)
	 *      Team (1 byte)
	 *   Map name (1 string)
	 *   Map (1 string)
	 */
	public static final byte TYPE_SERVER_INFO_RESPONSE = (byte)0x11;
	
	/**
	 * A client will send this to a server to ask that it be included in the game
	 * Contents:
	 *   Player name (1 string)
	 *   Server password (1 string, empty if not included)
	 */
	public static final byte TYPE_LOGIN_REQUEST = (byte)0x12;
	
	/**
	 * If a login is sucesssful, this message will fill in the new player with all they need to
	 *   know about the game currently in progress. If a login fails, the connection is closed.
	 * Contents:
	 *    The new player's id (2 bytes)
	 *    Map name (1 string)
	 *    Map (1 string)
	 *    Number of players (2 bytes)
	 *    Per player:
	 *       Player name (1 string)
	 *       Player id (2 bytes)
	 *       Team (1 byte)
	 *    New player's initial position (2 floats or 2 ints)
	 *    Number of seconds the current game has been running for (1 float)
	 */
	public static final byte TYPE_LOGIN_RESPONSE = (byte)0x13;
	
	/**
	 * Indicates that a player has died (possibly the recipient of the message)
	 * Contents:
	 *    The dead player's id (2 bytes)
	 *    The id of the player who killed the above (2 bytes)
	 */
	public static final byte TYPE_PLAYER_DEATH = (byte)0x14;
	
	/**
	 * A player has respawned
	 * Contents:
	 *    The player's id (2 bytes)
	 *    New position (2 floats)
	 *    Time of respawn (1 float)
	 */
	public static final byte TYPE_PLAYER_RESPAWN = (byte)0x15;
	
	/**
	 * A player has joined the game
	 * Contents:
	 *    Player's id (2 bytes)
	 *    Player's name (1 string)
	 *    Player's team (1 byte)
	 */
	public static final byte TYPE_PLAYER_JOIN = (byte)0x16;
	
	/**
	 * Player has left the game (for whatever reason)
	 * Contents:
	 *    Player's id (2 bytes)
	 */
	public static final byte TYPE_PLAYER_LEAVE = (byte)0x17;
	
	/**
	 * Somebody fired a projectile
	 * Contents:
	 *    Launching player's id (2 bytes)
	 *    Point of origin (2 floats)
	 *    Direction of motion (2 floats)
	 *    Time of launch (1 float)
	 *    Projectile type (1 byte)
	 */
	public static final byte TYPE_PROJECTILE_LAUNCH = (byte)0x18;
	
	/**
	 * Somebody's health status has changed
	 * Contents:
	 *    Player id (2 bytes)
	 *    Health value (2 bytes)
	 *    Time of update (1 float) [not sure about this one]
	 */
	public static final byte TYPE_HEALTH_UPDATE = (byte)0x19;
	
	/**
	 * This means a player has moved.
	 * Contents:
	 *    Player id (2 bytes)
	 *    Time of motion (1 float)
	 *    Position (2 floats)
	 *    Velocity (2 floats)
	 */
	public static final byte TYPE_PLAYER_MOTION = (byte)0x1a;
	
	/**
	 * A request that anyone hearing this message send a response.
	 * Contains no additional information
	 */
	public static final byte TYPE_PING_REQUEST = (byte)0x1b;
	
	/**
	 * A response to a ping request
	 * Contains:
	 *    Sender's player id (2 bytes) or 0xffff for the server
	 */
	public static final byte TYPE_PING_RESPONSE = (byte)0x1c;
	
	/**
	 * A text chat message
	 * Contains:
	 *    Sender's player id (2 bytes)
	 *    Message (1 string)
	 */
	public static final byte TYPE_CHAT_MESSAGE = (byte)0x1d;
	
	/**
	 * Indicates that all recipients should switch to the specified multicast channel
	 * Contains:
	 *    The new multicast address to use (1 string)
	 *    The new multicast port to use (2 bytes);
	 */
	public static final byte TYPE_MULTICAST_CHANGE = (byte)0x1e;
    
    /**
     * The character set we'll be using for encoding and decoding our strings
     */
    public static final String STRING_CHARSET = "UTF-8";
}
