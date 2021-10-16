package net.sf.l2j.gameserver.handler.itemhandlers;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.enums.ZoneId;
import net.sf.l2j.gameserver.handler.IItemHandler;
import net.sf.l2j.gameserver.handler.admincommandhandlers.AdminAIO;
import net.sf.l2j.gameserver.model.actor.Playable;
import net.sf.l2j.gameserver.model.actor.Player;
import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
import net.sf.l2j.gameserver.model.olympiad.OlympiadManager;
import net.sf.l2j.gameserver.network.SystemMessageId;

public class AIOCoin implements IItemHandler
{
   @Override
   public void useItem(Playable playable, ItemInstance item, boolean forceUse)
   {
       if (!(playable instanceof Player))
           return;
       
       final Player player = (Player) playable;
       
       if (player.isInsideZone(ZoneId.TOWN) && !player.isInsideZone(ZoneId.PEACE))
       {
           player.sendMessage("To make use of this item you must be within the town.");
           return;
       }
       
       if (!Config.ALLOW_AIO_BUFFER_COIN)
       {
           player.sendMessage("AIO Coin is not allowed.");
           return;
       }
       
       if (Config.AIO_BUFFER_COIN_DAYS < 1)
       {
           player.sendMessage("The time must be one day or more.");
           return;
       }
       
       if (player.isInOlympiadMode() || OlympiadManager.getInstance().isRegisteredInComp(player))
       {
           player.sendPacket(SystemMessageId.YOU_ARE_NOT_AUTHORIZED_TO_DO_THAT);
           return;
       }
       
       if (player.isAIO())
       {
           player.sendMessage("Player " + player.getName() + " is already an AIO.");
           return;
       }
       
       player.destroyItem("Consume", item.getObjectId(), 1, null, true);
       AdminAIO.doAIO(player, Config.AIO_BUFFER_COIN_DAYS);
   }
}