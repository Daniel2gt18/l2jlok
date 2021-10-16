package net.sf.l2j.gameserver.taskmanager;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.handler.admincommandhandlers.AdminAIO;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;

public class AIOTaskManager implements Runnable
{
   private Map<Player, Long> players = new ConcurrentHashMap<>();
   
   protected AIOTaskManager()
   {
       ThreadPool.scheduleAtFixedRate(this, 10000, 10000);
   }
   
   public void add(Player player)
   {
       players.put(player, System.currentTimeMillis());
   }
   
   public void remove(Creature creature)
   {
       players.remove(creature);
   }
   
   @Override
   public void run()
   {
       if (players.isEmpty())
           return;
       
       for (Entry<Player, Long> entry : players.entrySet())
       {
           final Player player = entry.getKey();
           
           if (player.getMemos().getLong("aioEndTime") < System.currentTimeMillis())
           {
               AdminAIO.removeAIO(player);
               remove(player);
           }
       }
   }
   
   public static final AIOTaskManager getInstance()
   {
       return SingletonHolder.INSTANCE;
   }
   
   private static class SingletonHolder
   {
       protected static final AIOTaskManager INSTANCE = new AIOTaskManager();
   }
}