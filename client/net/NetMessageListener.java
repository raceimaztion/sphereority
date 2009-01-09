package client.net;

import common.messages.*;

public interface NetMessageListener
{
	public void handleNetMessage(Message m);
}
