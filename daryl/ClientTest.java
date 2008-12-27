package daryl;

import java.awt.*;
import javax.swing.*;

import common.*;
import client.*;
import client.gui.*;

public class ClientTest implements ActionCallback
{
	public static ClientViewArea cva;
	
	public static void main(String[] args)
	{
		JFrame window = new JFrame("Sphereority client test");
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		InputListener listener = new InputListener();
		
		cva = new ClientViewArea(null);
		LocalPlayer player = new HumanPlayer(listener);
		player.setPosition(0.5f, 0.5f);
		cva.setLocalPlayer(player);
		cva.setMap(new Map());
		window.getContentPane().add(cva, BorderLayout.CENTER);
		window.addKeyListener(cva);
		
		ClientTest ct = new ClientTest();
		
		StringLabel sl = new StringLabel(10, -5, 250, 15, "Press INSERT to toggle antialiasing", 0, 0.5f, Color.orange.darker());
		sl.setFontSize(9);
		cva.addWidget(sl);
		SimpleButton sb = new SimpleButton(-10, -10, 50, 15, "Quit", Color.red);
		sb.addCallback(ct);
		sb.setFontSize(9);
		cva.addWidget(sb);
		sb = new SimpleButton(-10, -35, 50, 15, "Team", Color.green);
		sb.addCallback(ct);
		sb.setFontSize(9);
		cva.addWidget(sb);
		
		listener.attachListeners(window);
		
		window.pack();
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}

	public void actionCallback(InteractiveWidget source, int buttons)
	{
		String label = source.getLabel();
		if (label.equals("Quit"))
		{
			//System.out.println("Quit button pressed, exiting...");
			System.exit(0);
		}
		else if (label.equals("Team"))
		{
			LocalPlayer player = cva.getLocalPlayer();
			if (player.getTeam() == Constants.TEAM_A)
				player.setTeam(Constants.TEAM_B);
			else
				player.setTeam(Constants.TEAM_A);
		}
	}
}
