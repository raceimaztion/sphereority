package client;

import client.audio.*;
import client.gui.*;
import common.*;
import common.messages.*;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.awt.Window;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.Vector;
import javax.swing.Timer;

/**
 * This class describes the game loop for this game
 */

public class GameEngine implements Constants, ActionListener, ActionCallback
{
    // SINGLETONS
    public static Logger logger = Logger.getLogger(CLIENT_LOGGER_NAME);
    public static GameEngine gameEngine;

    public boolean gameOver;
    public Map gameMap;
    public ClientViewArea gameViewArea;
    public LocalPlayer localPlayer;
    public InputListener localInputListener;

    // Actor lists and sub-lists
    public Vector<Actor> actorList; // This contains all actors in the game
    public Vector<Stone> stoneList; // This only contains stones
    public Vector<Player> playerList; // This only contains players
    public Vector<Projectile> bulletList; // This only contains bullets
    public Vector<Actor> miscList; // Anything else not in the above lists

    public long lastTime;
    public float currentTime;
    public Timer timer;
    public Timer frameRate;
    
    public Vector<MapChangeListener> mapListeners;

    // Sound stuff
    public GameSoundSystem soundSystem;
    public SoundEffect soundBump, soundDeath, soundFire;

    // CONSTRUCTORS
    public GameEngine(Map m, char playerID, String name, boolean bot)
    {
        preSetup(m);

        localPlayer = bot ? new ComputerPlayer(playerID, name, this)
                          : new HumanPlayer(localInputListener, playerID, name);

        postSetup(true);
    }

    public GameEngine(Map m)
    {
        preSetup(m);

        localPlayer = new HumanPlayer(localInputListener);

        postSetup(false);
    } // end GameEngine() constructor

    private void preSetup(Map m)
    {
        gameEngine = this;
        gameOver = false;
        gameMap = m;
        mapListeners = new Vector<MapChangeListener>();

        actorList = new Vector<Actor>();
        stoneList = new Vector<Stone>();
        playerList = new Vector<Player>();
        bulletList = new Vector<Projectile>();
        miscList = new Vector<Actor>();

        gameViewArea = new ClientViewArea(this);
        addButton(-5, -5, 45, 15, "Quit", Color.red);

        localInputListener = new InputListener();
        localInputListener.attachListeners(gameViewArea);

        triggerMapListeners();

        // Sound engine stuff:
        soundSystem = new GameSoundSystem();
        soundBump = soundSystem.loadSoundEffect(SOUND_BUMP);
        soundDeath = soundSystem.loadSoundEffect(SOUND_DEATH);
        soundFire = soundSystem.loadSoundEffect(SOUND_FIRE);
    }

    private void postSetup(boolean fixed)
    {
        // if (fixed)
        // gameMap.placePlayer(localPlayer, null);
        // else
        gameMap.placePlayer(localPlayer);


        MouseTracker mouseTracker = localPlayer instanceof ComputerPlayer ?
                                    new RandomMouseTracker(localInputListener, gameViewArea) :
                                    new MouseTracker(localInputListener, gameViewArea);
        localPlayer.setAimingTarget(mouseTracker);

        DoubleTracker doubleTracker = new DoubleTracker(mouseTracker, localPlayer);

        TrackingObject playerTracker = new TrackingObject(doubleTracker);
        
        gameViewArea.viewTracker = playerTracker;
        gameViewArea.setLocalPlayer(localPlayer);

        addActor(localPlayer);
        addActor(mouseTracker);
        addActor(doubleTracker);
        addActor(playerTracker);
    }

    // GETTERS
    public LocalPlayer getLocalPlayer()
    {
        return this.localPlayer;
    }

    public Map getGameMap()
    {
        return this.gameMap;
    }

    public boolean getGameOver()
    {
        return this.gameOver;
    }

    public ClientViewArea getGameViewArea()
    {
        return this.gameViewArea;
    }

    public InputListener getInputListener()
    {
        return this.localInputListener;
    }

    // SETTERS
    public void setLocalPlayer(LocalPlayer p)
    {
        this.localPlayer = p;
    }

    public void setGameMap(Map m)
    {
        this.gameMap = m;
        triggerMapListeners();
    }

    // OPERATIONS
    public void addActor(Actor a)
    {
        synchronized(actorList) {
            actorList.add(a);
            if (a instanceof Stone)
                synchronized(stoneList) {
                    stoneList.add((Stone) a);
                }
            else if (a instanceof Projectile)
                synchronized(bulletList) {
                    bulletList.add((Projectile) a);
                }
            else if (a instanceof Player)
                synchronized(playerList) {
                    playerList.add((Player) a);
                }
            else
                synchronized(miscList) {
                    miscList.add(a);
                }
        }
    }

    public void removeActor(Actor a)
    {
        if(a == null) return;
        
        synchronized(actorList) {
            actorList.remove(a);
            if (a instanceof Projectile)
                synchronized(bulletList) {
                    bulletList.remove((Projectile) a);
                }
            else if (a instanceof Player)
                synchronized(playerList) {
                    playerList.remove((Player) a);
                }
            else
                synchronized(miscList) {
                    miscList.remove(a);
                }
        }
    }

    public boolean isGameOver()
    {
        return this.gameOver;
    }

    public void gameOver()
    {
        gameOver = true;
        if (timer != null)
            timer.stop();

        // This code finds the Window that contains the gameViewArea and tells
        // it to disapear
        Component c = gameViewArea.getParent();
        while (!(c instanceof Window) && c != null)
            c = c.getParent();

        if (c != null)
            ((Window) c).setVisible(false);
    }

    public void gameStep()
    {
        checkCollisions();
        updateWorld();
        Thread.yield();
    }

    public void play()
    {
        initialize();

        timer = new Timer(TIMER_TICK, this);
        timer.start();
        timer.setCoalesce(true);

        // while (!isGameOver()) {
        // gameStep();
        //      
        // try { Thread.sleep(10); }
        // catch (InterruptedException er) { }
        // }
    }

    protected void triggerMapListeners()
    {
        for (MapChangeListener mcl : mapListeners)
            mcl.mapChanged(gameMap);
    }

    public void addMapListener(MapChangeListener listener)
    {
        mapListeners.add(listener);
    }

    public void initialize()
    {
        // Copy the map as a bunch of Stones
        for (int x = 0; x < gameMap.getWidth(); x++)
            for (int y = 0; y < gameMap.getHeight(); y++)
            {
                if (gameMap.isWall(x, y))
                {
                    stoneList.add(new Stone(x, y));
                    actorList.add(stoneList.get(stoneList.size() - 1));
                }
            }

        lastTime = System.currentTimeMillis();
        currentTime = 0;
    } // end initialize()

    public void checkCollisions()
    {
        // Environment, Player and projectile collision code goes here
        Rectangle2D bounds1, bounds2;
        Actor actor1, actor2;

        // Check players against stones
        Player p;
        for (int i = 0; i < playerList.size(); i++)
        {
            p = playerList.get(i);
            if (!p.isAlive())
            {
                removeActor(p);
                continue;
            }

            float px = p.getX(), py = p.getY();
            int ix = (int) px, iy = (int) py;
            if (px < 0 || py < 0)
                continue;
            if (px >= gameMap.getWidth() || py >= gameMap.getHeight())
                continue;

            px = px - ix;
            py = py - iy;
            if (px < 0.25 && gameMap.isWall(ix - 1, iy))
                p.collideLeft();
            else if (px > 0.75 && gameMap.isWall(ix + 1, iy))
                p.collideRight();
            if (py < 0.25 && gameMap.isWall(ix, iy - 1))
                p.collideUp();
            else if (py > 0.72 && gameMap.isWall(ix, iy + 1))
                p.collideDown();
        }
        // for (int i=0; i < stoneList.size(); i ++)
        // {
        // actor1 = stoneList.get(i);
        // bounds1 = actor1.getBounds();
        //      
        // for (int j=0; j < playerList.size(); j ++)
        // {
        // actor2 = playerList.get(j);
        // bounds2 = actor2.getBounds();
        //        
        // if (bounds1.intersects(bounds2))
        // {
        // actor1.collision(actor2);
        // actor2.collision(actor1);
        // }
        // }
        // } // end check players against stones

        // Check bullets against players and stones
        for (int i = 0; i < bulletList.size(); i++)
        {
            actor1 = bulletList.get(i);
            if (!actor1.isAlive())
            {
                removeActor(actor1);
                continue;
            }

            bounds1 = actor1.getBounds();

            for (int j = 0; j < playerList.size(); j++)
            {
                actor2 = playerList.get(j);
                bounds2 = actor2.getBounds();

                /*
                 * /<<<<<<< HEAD:client/GameEngine.java if (fixed) {
                 * for(byte i = 0; i < 6; i++) { if(i !=
                 * localPlayer.getPlayerID()) { processPlayerJoin( new
                 * PlayerJoinMessage(i,new
                 * java.net.InetSocketAddress(MCAST_ADDRESS,MCAST_PORT),"User" +
                 * i)); } } } //=======
                 */
                if (bounds1.intersects(bounds2))
                {
                    actor1.collision(actor2);
                    actor2.collision(actor1);
                }
            }

            // for (int j=0; j < stoneList.size(); j ++)
            // {
            // actor2 = stoneList.get(j);
            // bounds2 = actor2.getBounds();
            //        
            // if (bounds1.intersects(bounds2))
            // {
            // actor1.collision(actor2);
            // actor2.collision(actor1);
            // }
            // }
            // Simpler check:
            if (gameMap.isWall((int) actor1.getX(), (int) actor1.getY()))
            {
                actor1.collision(null);
            }
        } // end check bullets against players and stones

        // //Vector actorList = this.gameViewArea.actorList;
        // Rectangle2D playerBounds = this.localPlayer.getBounds();
        // for (int i = 0; i < actorList.size(); i = i + 1) {
        // Actor actor1 = actorList.get(i);
        // if (actor1 instanceof TrackingObject) continue;
        //      
        // Rectangle2D bound1 = actor1.getBounds();
        // if (bound1.intersects(playerBounds)) {
        // this.localPlayer.collision(actor1);
        // actor1.collision(this.localPlayer);
        // }
        // for (int j = i + 1; j < actorList.size(); j = j + 1) {
        // Actor actor2 = actorList.get(j);
        // if (actor2 instanceof TrackingObject) continue;
        //        
        // Rectangle2D bound2 = actor2.getBounds();
        // if (bound1.intersects(bound2)) {
        // actor1.collision(actor2);
        // actor2.collision(actor1);
        // }
        // }
        // }
    } // end checkCollisions()

    public void updateWorld()
    {
        long thisTime = System.currentTimeMillis();
        float dTime = 0.001f * (thisTime - lastTime);
        boolean repaint = false;

        // for (Actor a : actorList)
        // {
        // if (a.animate(dTime))
        // repaint = true;
        // }

        synchronized(playerList) {
            for (Actor a : playerList)
            {
                if (a.animate(dTime, currentTime))
                    repaint = true;
            }
        }
        
        synchronized(bulletList) {
            for (Actor a : bulletList)
            {
                if (a.animate(dTime, currentTime))
                    repaint = true;
            }
        }

        synchronized(miscList) {
            for (Actor a : miscList)
            {
                if (a.animate(dTime, currentTime))
                    repaint = true;
            }
        }

        lastTime = thisTime;
        currentTime += dTime;
        if (repaint)
        {
            /*
             * This may not actually be desireable. If you bump or fire then
             * stop moving, it won't repaint
             */
        }
        gameViewArea.repaint();

    } // end updateWorld()

    public void actionPerformed(ActionEvent e)
    {
        gameStep();
    }

    protected void addButton(int x, int y, int width, int height, String label)
    {
        addButton(x, y, width, height, label, Color.green);
    }

    protected void addButton(int x, int y, int width, int height, String label, Color c)
    {
        SimpleButton b = new SimpleButton(x, y, width, height, label, c);
        b.addCallback(this);
        gameViewArea.addWidget(b);
    }

    public void actionCallback(InteractiveWidget source, int buttons)
    {
        if (source.getLabel().equalsIgnoreCase("Quit"))
        {
            gameOver();
        }
    }

    /* ****************************************** *
     * These method are for playing sound effects *
     * ****************************************** */
    /**
     * Plays a bump sound effect at the specified volume. If the sound is
     * already playing at a lower volume, it is stopped and restarted at the
     * louder volume, otherwise nothing happens.
     * 
     * @param volume
     *            The volume at which to play.
     */
    public void playBump(float volume)
    {
    	logger.info(String.format("Playing bump sound at a volume of %.2f.", volume));
        playSound(volume, soundBump);
    }

    /**
     * Plays a player death sound effect at the specified volume
     * 
     * @param volume
     *            The volume at which to play
     */
    public void playDeath(float volume)
    {
    	logger.info(String.format("Playing death sound at a volume of %.2f.", volume));
        playSound(volume, soundDeath);
    }

    /**
     * Plays a gun fire sound effect at the specified volume
     * 
     * @param volume
     *            The volume at which to play
     */
    public void playFire(float volume)
    {
    	logger.info(String.format("Playing fire sound at a volume of %.2f.", volume));
        playSound(volume, soundFire);
    }

    /**
     * A slightly more generic sound playing method so we don't have to
     * duplicate code all over the place. This actually handles playing or not
     * playing the sound.
     * 
     * @param volume
     * @param sound
     */
    private void playSound(float volume, SoundEffect sound)
    {
        // If we failed to find any audio lines, all sounds will be null
        if (sound == null)
            return;

        if (sound.isPlaying())
        {
            if (sound.getVolume() <= volume)
                sound.stop();
            else
                return;
        }

        sound.setVolume(volume);
        sound.play();
    }

    public void registerActionListeners(Component c)
    {
        localInputListener.attachListeners(c);
        c.addKeyListener(gameViewArea);
    }

    public void registerActionListeners(Window w)
    {
        localInputListener.attachListeners(w);
    }

    public void unregisterActionListeners(Component c)
    {
        localInputListener.detachListeners(c);
        c.removeKeyListener(gameViewArea);
    }

    public void unregisterActionListenerst(Window w)
    {
        localInputListener.detachListeners(w);
    }

    /* ===================================== 
     * Methods for processing messages
     * =====================================
     */
    public synchronized void processPlayerMotion(PlayerMotionMessage message)
    {
        // Get the index of the player
        int playerIndex = getPlayerIndex(message.getPlayerId());

        // Do not process a player if they have not been added
        if(playerIndex == -1) {
            processPlayerJoin(new PlayerJoinMessage(message.getPlayerId(), RESOLVING_NAME, (byte)-1));
        }
        
        if (playerIndex < 0)
            return;
            
        try
        {
            // Update the co-ordinates of the player
            Player player = playerList.get(playerIndex);
            if (player instanceof RemotePlayer)
                ((RemotePlayer) player).addMotionPacket(message);
        } catch (Exception ex)
        {
            ex.printStackTrace();
        }
    }

    public synchronized void processPlayerJoin(PlayerJoinMessage message)
    {
        // Get the index of the player
        int playerIndex = getPlayerIndex(message.getPlayerId());
        Player player;

        // Creating a new player
        if(playerIndex == -1) {
            player = new RemotePlayer(message.getPlayerId(),message.getPlayerName());
            logger.log(Level.FINE,"Player Added: " + player.getPlayerID());
            addActor(player);
        }
        // Updating information about the player
        else
        {
            player = playerList.get(playerIndex);
            logger.log(Level.FINE,"Player Info Updated: " + player.getPlayerID());
            player.setPlayerName(message.getPlayerName());
        }
    }

    public void processProjectile(ProjectileLaunchMessage message)
    {
        // Get the index of the player
        int playerIndex = getPlayerIndex(message.getOwnerId());

        // Do not process this message if the player has not joined the game
        if (playerIndex == -1)
            return;

        Player player = playerList.get(playerIndex);
        
        if (player instanceof RemotePlayer) {
            // Add the projectile to the list
            addActor(new Projectile(message.getOrigin(),
                                    message.getDirection(),
                                    player.getCurrentTime(),
                                    player.getCurrentTime(),
                                    player.getPlayerID(),
                                    player.getTeam()));
            // Signal that the remote player has fired
            player.fire();
            logger.log(Level.FINE,"Player " + player.getPlayerID() + " fired!");
        }
    }    

    /**
     * Retrieve a player given their ID.
     */
    public int getPlayerIndex(int playerId)
    {
        int index = -1;
        for (int i = 0; i < playerList.size(); i++)
        {
            if (playerList.get(i).getPlayerID() == playerId)
            {
                index = i;
                break;
            }
        }
        return index;
    }
    
    public Player getPlayer(byte playerId) {
        synchronized(playerList) {
            for(Player player : playerList) {
                if(player.getPlayerID() == playerId)
                    return player;
            }
        }
        return null;
    }

} // end class GameEngine
