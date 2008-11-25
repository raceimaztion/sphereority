package	common;

import common.messages.PlayerMotionMessage;

import java.util.ConcurrentModificationException;
import java.util.Vector;
import java.util.concurrent.*;

/**
 * This class describes a player who is playing the game via the network
 * @author smaboshe
 *
 */
public class RemotePlayer extends Player
{
	/**
	 * The most messages that we want to use at any one time
	 */
	public static final int MAX_SAVED_MESSAGES = 20;
	/**
	 * The oldest message we want to consider, in seconds
	 */
	public static final float OLDEST_SAVED_MESSAGE = 2;
	
	protected float currentTime;
	protected Vector<PlayerMotionMessage> messageList;
	
	private Semaphore lock = new Semaphore(1);
	
	/**
	 * Creates a RemotePlayer with the specified id and name
	 * @param playerID	The id of this player
	 * @param name		The text name of this player
	 */
	public RemotePlayer(byte playerID, String name)
	{
		super(playerID, name);
		
		messageList = new Vector<PlayerMotionMessage>();
		currentTime = -1;
	}
	
	public void addMotionPacket(PlayerMotionMessage msg)
	{
		if ((currentTime - msg.getTime()) > OLDEST_SAVED_MESSAGE)
			return;
		
		try
		{
			lock.acquire();
			messageList.add(msg);
			lock.release();
		}
		catch (InterruptedException er)
		{
			
		}
	}
	
	public boolean animate(float dTime, float currentTime)
	{
		this.currentTime = currentTime;
		
		float x, y, weight, totalWeight;
		x = y = totalWeight = 0;
		
		do
		{
			try
			{
				//lock.acquire();
				for (PlayerMotionMessage m : messageList)
					if ((currentTime - m.getTime()) > OLDEST_SAVED_MESSAGE)
						messageList.remove(m);
				
				for (PlayerMotionMessage m : messageList)
				{
					weight = OLDEST_SAVED_MESSAGE - (currentTime - m.getTime());
					weight = weight*weight;
					x += weight * m.getVelocity().getX() + m.getPosition().getX();
					y += weight * m.getVelocity().getY() + m.getPosition().getY();
					totalWeight += weight;
				}
				//lock.release();
			}
//			catch (InterruptedException er)
//			{
//				continue;
//			}
			catch (ConcurrentModificationException er)
			{
				//continue;
				return false;
			}
		} while (false);
		
		if (totalWeight < 0.0001f)
		{
			// TODO: Add hook for missing packets here
			return false;
		}
		else
			System.out.printf("Player %d has %d motion packets\n", playerID, messageList.size());
		
		x /= totalWeight;
		y /= totalWeight;
		
		setPosition(x, y);
		
		return true;
	}
}
