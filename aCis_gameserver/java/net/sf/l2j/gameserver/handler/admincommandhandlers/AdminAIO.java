package net.sf.l2j.gameserver.handler.admincommandhandlers;

import java.util.StringTokenizer;
import java.util.concurrent.TimeUnit;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.SkillTable;
import net.sf.l2j.gameserver.data.xml.PlayerLevelData;
import net.sf.l2j.gameserver.enums.SayType;
import net.sf.l2j.gameserver.enums.skills.Stats;
import net.sf.l2j.gameserver.handler.IAdminCommandHandler;
import net.sf.l2j.gameserver.model.World;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.holder.IntIntHolder;
import net.sf.l2j.gameserver.network.SystemMessageId;
import net.sf.l2j.gameserver.network.serverpackets.CreatureSay;
import net.sf.l2j.gameserver.network.serverpackets.InventoryUpdate;
import net.sf.l2j.gameserver.network.serverpackets.SocialAction;
import net.sf.l2j.gameserver.network.serverpackets.StopMove;
import net.sf.l2j.gameserver.skills.L2Skill;
import net.sf.l2j.gameserver.taskmanager.AIOTaskManager;

public class AdminAIO implements IAdminCommandHandler
{
   private static final String[] ADMIN_COMMANDS =
   {
       "admin_setaio",
       "admin_removeaio"
   };
   
   @Override
   public void useAdminCommand(String command, Player player)
   {
       if (player == null)
           return;
       
       StringTokenizer st = new StringTokenizer(command, " ");
       
       switch (st.nextToken())
       {
           case "admin_setaio":
               try
               {
                   final int paramCount = st.countTokens();
                   
                   if (paramCount == 1)
                   {
                       final int time = Integer.valueOf(st.nextToken());
                       
                       if (player.getTarget() instanceof Player)
                           doAIO((Player) player.getTarget(), time);
                       else
                           player.sendPacket(SystemMessageId.INVALID_TARGET);
                   }
                   else if (paramCount == 2)
                   {
                       final String name = st.nextToken();
                       final int time = Integer.valueOf(st.nextToken());
                       
                       final Player playerName = World.getInstance().getPlayer(name);
                       
                       if (playerName != null)
                           doAIO(playerName, time);
                   }
               }
               catch (Exception e)
               {
                   player.sendMessage("Usage: //setaio <target_day's> | <player_name> <day's>");
               }
               break;
           
           case "admin_removeaio":
               try
               {
                   final int paramCount = st.countTokens();
                   
                   if (paramCount == 0)
                   {
                       if (player.getTarget() instanceof Player)
                           removeAIO((Player) player.getTarget());
                       else
                           player.sendPacket(SystemMessageId.INVALID_TARGET);
                   }
                   else if (paramCount == 1)
                   {
                       final String name = st.nextToken();
                       final Player playerName = World.getInstance().getPlayer(name);
                       
                       if (playerName != null)
                           removeAIO(playerName);
                   }
                   
               }
               catch (Exception e)
               {
                   player.sendMessage("Usage: //removeaio <target> | <player_name>");
               }
               break;
       }
       return;
   }
   
   public static void doAIO(Player player, int time)
   {
       if (!Config.ALLOW_AIO_BUFFER)
       {
           player.sendMessage("System AIO-Buffer is disable.");
           return;
       }
       
       AIOTaskManager.getInstance().add(player);
       
       long remainingTime = player.getMemos().getLong("aioEndTime", 0);
       
       if (remainingTime > 0)
       {
           player.getMemos().set("aioEndTime", remainingTime + TimeUnit.DAYS.toMillis(time));
           player.sendPacket(new CreatureSay(SayType.HERO_VOICE, "System", "Dear player, your AIO has been extended by " + time + " day(s)."));
       }
       else
       {
           player.getMemos().set("aioEndTime", System.currentTimeMillis() + TimeUnit.DAYS.toMillis(time));
           player.sendPacket(new CreatureSay(SayType.HERO_VOICE, "System", "Dear player, you are now an AIO, congratulations."));
           
           if (Config.ALLOW_AIO_BUFFER_CUSTOM_NAME)
               player.setName("AIO-" + player.getName());
           
           if (Config.ALLOW_AIO_BUFFER_CUSTOM_TITLE)
               player.setTitle(Config.AIO_BUFFER_CUSTOM_TITLE);
           
           player.getStatus().addExp(PlayerLevelData.getInstance().getPlayerLevel(80).getRequiredExpToLevelUp());
           player.setAIO(true);
           
           for (IntIntHolder skills : Config.AIO_BUFFER_SKILL_LIST)
           {
               if (Config.AIO_BUFFER_SKILL_LIST.isEmpty())
                   continue;
               
               L2Skill skill = SkillTable.getInstance().getInfo(skills.getId(), skills.getValue());
               
               player.addSkill(skill, true);
           }
           
           if (Config.ALLOW_AIO_BUFFER_ITEMS)
           {
               for (IntIntHolder items : Config.AIO_BUFFER_ITEMS_LIST)
               {
                   if (Config.AIO_BUFFER_ITEMS_LIST.isEmpty())
                       continue;
                   
                   player.getInventory().addItem("", items.getId(), items.getValue(), player, player);
                   
                   if (player.getInventory().getItemByItemId(items.getId()).isEquipable())
                       player.getInventory().equipItem(player.getInventory().getItemByItemId(items.getId()));
               }
           }
           updateCharacter(player);
           player.sendMessage(player.getName() + " is now an AIO for " + time + " day(s).");
       }
   }
   
   public static void removeAIO(Player player)
   {
       if (!player.isAIO())
       {
           player.sendMessage(player.getName() + "is not an AIO.");
           return;
       }
       
       AIOTaskManager.getInstance().remove(player);
       player.getMemos().set("aioEndTime", 0);
       
       player.startAbnormalEffect(0x0800);
       player.setIsParalyzed(true);
       player.broadcastPacket(new StopMove(player));
       
       for (L2Skill skill : player.getSkills().values())
           player.removeSkill(skill.getId(), true);
       
       if (Config.ALLOW_AIO_BUFFER_ITEMS)
       {
           for (IntIntHolder items : Config.AIO_BUFFER_ITEMS_LIST)
           {
               if (Config.AIO_BUFFER_ITEMS_LIST.isEmpty())
                   continue;
               
               final int getItemCount = player.getInventory().getItemByItemId(items.getId()).getCount();
               
               if (getItemCount < items.getValue())
                   player.getInventory().destroyItemByItemId("", items.getId(), getItemCount, player, player);
               else
                   player.getInventory().destroyItemByItemId("", items.getId(), items.getValue(), player, player);
           }
       }
       
       player.setAIO(false);
       player.removeExpAndSp(player.getStatus().getExp() - 1, 0);
       
       if (Config.ALLOW_AIO_BUFFER_CUSTOM_NAME)
           player.setName(player.getName().replace("AIO-", ""));
       
       if (Config.ALLOW_AIO_BUFFER_CUSTOM_TITLE && player.getTitle().contains(Config.AIO_BUFFER_CUSTOM_TITLE))
           player.setTitle("");
       
       player.sendPacket(new CreatureSay(SayType.PARTYROOM_COMMANDER, "System", "Your AIO period is over."));
       player.sendMessage(player.getName() + "'s AIO status has been removed.");
       player.broadcastPacket(new SocialAction(player, 13));
       
       updateCharacter(player);
       
       player.stopAbnormalEffect(0x0800);
       player.setIsParalyzed(false);
   }
   
   public static void updateCharacter(Player player)
   {
       player.sendPacket(new InventoryUpdate());
       player.broadcastUserInfo();
       player.broadcastCharInfo();
       player.sendSkillList();
       player.store();
   }
   
   public static double onStats(Stats stats, Creature creature, double value)
   {
       if (creature == null)
       {
           return value;
       }
       if (!(creature instanceof Player))
           return value;
       
       if (!((Player) creature).isAIO())
           return value;
       
       if (Config.ALLOW_AIO_BUFFER_STATS && Config.AIO_BUFFER_STATS.containsKey(stats))
       {
           double aux = value *= Config.AIO_BUFFER_STATS.get(stats);
           return aux += value - aux;
       }
       
       return value;
   }
   
   @Override
   public String[] getAdminCommandList()
   {
       return ADMIN_COMMANDS;
   }
   
}