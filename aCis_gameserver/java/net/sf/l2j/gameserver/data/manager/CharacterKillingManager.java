package net.sf.l2j.gameserver.data.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.commons.logging.CLogger;
import net.sf.l2j.commons.pool.ConnectionPool;
import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.model.CharSelectSlot;
import net.sf.l2j.gameserver.model.actor.PcPolymorph;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;

/**
 * @author paytaly
 */
public final class CharacterKillingManager
{
   private static final CLogger LOGGER = new CLogger(CharacterKillingManager.class.getName());
   
   private int _cycle = 0;
   private long _cycleStart = 0L;
   private int _winnerPvPKills;
   private int _winnerPvPKillsCount;
   private int _winnerPKKills;
   private int _winnerPKKillsCount;
   
   private volatile CharSelectSlot _winnerPvPKillsInfo;
   private volatile CharSelectSlot _winnerPKKillsInfo;
   
   private ScheduledFuture<?> _scheduledKillingCycleTask;
   
   private List<PcPolymorph> pvpMorphListeners = new CopyOnWriteArrayList<>();
   private List<PcPolymorph> pkMorphListeners = new CopyOnWriteArrayList<>();
   
   protected CharacterKillingManager() {}
   
   public synchronized void init()
   {
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement st = con.prepareStatement("SELECT cycle, cycle_start, winner_pvpkills, winner_pvpkills_count, winner_pkkills, winner_pkkills_count FROM character_kills_info ORDER BY cycle_start DESC LIMIT 1");
           ResultSet rs = st.executeQuery())
       {
           if (rs.next())
           {
               _cycle = rs.getInt("cycle");
               _cycleStart = rs.getLong("cycle_start");
               _winnerPvPKills = rs.getInt("winner_pvpkills");
               _winnerPvPKillsCount = rs.getInt("winner_pvpkills_count");
               _winnerPKKills = rs.getInt("winner_pkkills");
               _winnerPKKillsCount = rs.getInt("winner_pkkills_count");
           }
       }
       catch (Exception e)
       {
           LOGGER.warn("Could not load characters killing cycle: " + e.getMessage(), e);
       }
       
       broadcastMorphUpdate();
       
       if (_scheduledKillingCycleTask != null)
           _scheduledKillingCycleTask.cancel(true);
       
       String hour = Config.CKM_RATE_HOUR_REFRESH.split(":")[0];
       String minutes = Config.CKM_RATE_HOUR_REFRESH.split(":")[1];
       try
       {
           int h = Integer.valueOf(hour);
           int m = Integer.valueOf(minutes);
           Calendar currentTime = Calendar.getInstance();
           Calendar startTime = Calendar.getInstance();
           startTime.setLenient(true);
           startTime.set(Calendar.HOUR_OF_DAY, h);
           startTime.set(Calendar.MINUTE, m);
           startTime.set(Calendar.SECOND, 00);
           if (startTime.getTimeInMillis() < currentTime.getTimeInMillis())
               startTime.add(Calendar.DAY_OF_MONTH, 1);
            long cycleTime = startTime.getTimeInMillis() - currentTime.getTimeInMillis();
            _scheduledKillingCycleTask = ThreadPool.schedule(new CharacterKillingCycleTask(), cycleTime);
           
            LOGGER.info("Loaded " + startTime.getTime() + " next update for " + getClass().getSimpleName() + ".");
       }
       catch (Exception e)
       {
           LOGGER.warn("Could not load characters killing period: " + e.getMessage(), e);
       }
       
       LOGGER.info("Loaded " + _cycle + " " + getClass().getSimpleName() + " cycle(s) - Next cycle in: " + _scheduledKillingCycleTask.getDelay(TimeUnit.SECONDS) + "s.");
   }
   
   public synchronized void newKillingCycle()
   {
       _cycleStart = System.currentTimeMillis();
       computateCyclePvPWinner();
       computateCyclePKWinner();
       refreshKillingSnapshot();
       
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement st = con.prepareStatement("INSERT INTO character_kills_info (cycle_start, winner_pvpkills, winner_pvpkills_count, winner_pkkills, winner_pkkills_count) VALUES (?, ?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS))
       {
           st.setLong(1, _cycleStart);
           st.setInt(2, _winnerPvPKills);
           st.setInt(3, _winnerPvPKillsCount);
           st.setInt(4, _winnerPKKills);
           st.setInt(5, _winnerPKKillsCount);
           st.execute();
           
           try (ResultSet rs = st.getGeneratedKeys())
           {
               if (rs.next())
                   _cycle = rs.getInt(1);
           }
       }
       catch (Exception e)
       {
           LOGGER.warn("Could not create characters killing cycle: " + e.getMessage(), e);
       }
       
       broadcastMorphUpdate();
       
       if (_scheduledKillingCycleTask != null)
           _scheduledKillingCycleTask.cancel(true);

       _scheduledKillingCycleTask = ThreadPool.schedule(new CharacterKillingCycleTask(), 86400000);
   }
   
   private void computateCyclePvPWinner()
   {
       _winnerPvPKills = 0;
       _winnerPvPKillsCount = 0;
       _winnerPvPKillsInfo = null;
       
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement st = con.prepareStatement("SELECT c.obj_Id, (c.pvpkills - COALESCE(ck.pvpkills, 0)) pvpkills FROM characters c LEFT JOIN character_kills_snapshot ck ON ck.charId = c.obj_Id WHERE accesslevel = 0 ORDER BY pvpkills DESC LIMIT 1");
           ResultSet rs = st.executeQuery();)
       {
           if (rs.next())
           {
               int kills = rs.getInt(2);
               if (kills > 0)
               {
                   _winnerPvPKills = rs.getInt(1);
                   _winnerPvPKillsCount = kills;
               }
           }
       }
       catch (Exception e)
       {
           LOGGER.warn("Could not computate characters killing cycle winners: " + e.getMessage(), e);
       }
   }
   
   private void computateCyclePKWinner()
   {
       _winnerPKKills = 0;
       _winnerPKKillsCount = 0;
       _winnerPKKillsInfo = null;
       
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement st = con.prepareStatement("SELECT c.obj_Id, (c.pkkills - COALESCE(ck.pkkills, 0)) pkkills FROM characters c LEFT JOIN character_kills_snapshot ck ON ck.charId = c.obj_Id WHERE accesslevel = 0 ORDER BY pkkills DESC LIMIT 1");
           ResultSet rs = st.executeQuery();)
       {
           if (rs.next())
           {
               int kills = rs.getInt(2);
               if (kills > 0)
               {
                   _winnerPKKills = rs.getInt(1);
                   _winnerPKKillsCount = kills;
               }
           }
       }
       catch (Exception e)
       {
           LOGGER.warn("Could not computate characters killing cycle winners: " + e.getMessage(), e);
       }
   }
   
   private static void refreshKillingSnapshot()
   {
       try (Connection con = ConnectionPool.getConnection();
           PreparedStatement stTruncate = con.prepareStatement("TRUNCATE TABLE character_kills_snapshot");
           PreparedStatement stRefresh = con.prepareStatement("INSERT INTO character_kills_snapshot (charId, pvpkills, pkkills) SELECT obj_Id, pvpkills, pkkills FROM characters WHERE (pvpkills > 0 OR pkkills > 0) AND accesslevel = 0"))
       {
           stTruncate.executeUpdate();
           stRefresh.executeUpdate();
       }
       catch (Exception e)
       {
           LOGGER.warn("Could not refresh characters killing snapshot: " + e.getMessage(), e);
       }
   }
   
   public void broadcastMorphUpdate()
   {
       final CharSelectSlot winnerPvPKillsInfo = getWinnerPvPKillsInfo();
       for (PcPolymorph npc : pvpMorphListeners)
           broadcastPvPMorphUpdate(npc, winnerPvPKillsInfo);
       
       final CharSelectSlot winnerPKKillsInfo = getWinnerPKKillsInfo();
       for (PcPolymorph npc : pkMorphListeners)
           broadcastPKMorphUpdate(npc, winnerPKKillsInfo);
   }
   
   private void broadcastPvPMorphUpdate(PcPolymorph npc, CharSelectSlot winnerPvPKillsInfo)
   {
      if (winnerPvPKillsInfo == null)
       {
           npc.setPolymorphInfo(null);
           npc.decayMe();
           npc.spawnMe();
           return;
       }
       
       npc.setVisibleTitle(Config.CKM_PVP_NPC_TITLE.replaceAll("%kills%", String.valueOf(_winnerPvPKillsCount)));
       npc.setTitleColor(Config.CKM_PVP_NPC_TITLE_COLOR);
       npc.setNameColor(Config.CKM_PVP_NPC_NAME_COLOR);
       npc.setPolymorphInfo(winnerPvPKillsInfo);
       npc.broadcastPacket(new SocialAction(npc, 16));
       npc.decayMe();
       npc.spawnMe();
   }
   
   private void broadcastPKMorphUpdate(PcPolymorph npc, CharSelectSlot winnerPKKillsInfo)
   {
       if (winnerPKKillsInfo == null)
       {
           npc.setPolymorphInfo(null);
           npc.decayMe();
           npc.spawnMe();         
           return;
       }
       
       npc.setVisibleTitle(Config.CKM_PK_NPC_TITLE.replaceAll("%kills%", String.valueOf(_winnerPKKillsCount)));
       npc.setTitleColor(Config.CKM_PK_NPC_TITLE_COLOR);
       npc.setNameColor(Config.CKM_PK_NPC_NAME_COLOR);
       npc.setPolymorphInfo(winnerPKKillsInfo);
       npc.broadcastPacket(new SocialAction(npc, 16));
       npc.decayMe();
       npc.spawnMe();
   }
   
   public boolean addPvPMorphListener(PcPolymorph npc)
   {
       if (npc == null)
           return false;

       broadcastPvPMorphUpdate(npc, getWinnerPvPKillsInfo());
       return pvpMorphListeners.add(npc);
   }
   
   public boolean removePvPMorphListener(PcPolymorph npc)
   {
       return pvpMorphListeners.remove(npc);
   }
   
   public boolean addPKMorphListener(PcPolymorph npc)
   {
       if (npc == null)
           return false;

       broadcastPKMorphUpdate(npc, getWinnerPKKillsInfo());
       return pkMorphListeners.add(npc);
   }
   
   public boolean removePKMorphListener(PcPolymorph npc)
   {
       return pkMorphListeners.remove(npc);
   }
   
   private CharSelectSlot getWinnerPvPKillsInfo()
   {
       if (_winnerPvPKills != 0 && _winnerPvPKillsInfo == null)
       {
           synchronized (this)
           {
               if (_winnerPvPKillsInfo == null)
                   _winnerPvPKillsInfo = PcPolymorph.loadCharInfo(_winnerPvPKills);
           }
       }
       return _winnerPvPKillsInfo;
   }
   
   private CharSelectSlot getWinnerPKKillsInfo()
   {
       if (_winnerPKKills != 0 && _winnerPKKillsInfo == null)
       {
           synchronized (this)
           {
               if (_winnerPKKillsInfo == null)
                   _winnerPKKillsInfo = PcPolymorph.loadCharInfo(_winnerPKKills);
           }
       }
       return _winnerPKKillsInfo;
   }
   
   protected static class CharacterKillingCycleTask implements Runnable
   {
       @Override
       public void run()
       {
           CharacterKillingManager.getInstance().newKillingCycle();
       }
   }
   
   public static CharacterKillingManager getInstance()
   {
       return SingletonHolder.INSTANCE;
   }
   
   private static class SingletonHolder
   {
       protected static final CharacterKillingManager INSTANCE = new CharacterKillingManager();
   }
}
