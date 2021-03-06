       package net.sf.l2j.gameserver.handler.itemhandlers;
       
       import net.sf.l2j.gameserver.enums.actors.ClassId;
       import net.sf.l2j.gameserver.handler.IItemHandler;
       import net.sf.l2j.gameserver.model.actor.Playable;
       import net.sf.l2j.gameserver.model.actor.Player;
       import net.sf.l2j.gameserver.model.item.instance.ItemInstance;
       import net.sf.l2j.gameserver.network.serverpackets.ItemList;
       import net.sf.l2j.gameserver.network.serverpackets.MagicSkillUse;
       
       
       public class GradeDBox implements IItemHandler
       {
           @Override
           public void useItem(Playable playable, ItemInstance item, boolean forceUse)
           {
               if (!(playable instanceof Player))
                   return;
       
               Player activeChar = (Player)playable;
       
               if (activeChar.isInOlympiadMode())
               {
                   activeChar.sendMessage("This item cannot be used on Olympiad Games.");
                   return;
               }
       
               if (activeChar.destroyItem("Consume", item.getObjectId(), 1, null, false))
               {
                   //Heavy
                   if (activeChar.getClassId() == ClassId.WARRIOR)
                   {
                       //Armor
                       ItemInstance item1 = activeChar.getInventory().addItem("Armor", 352, 1, activeChar, null);
                       ItemInstance item2 = activeChar.getInventory().addItem("Armor", 2378, 1, activeChar, null);
                       ItemInstance item3 = activeChar.getInventory().addItem("Armor", 2411, 1, activeChar, null);
                       ItemInstance item4 = activeChar.getInventory().addItem("Armor", 2425, 1, activeChar, null);
                       ItemInstance item5 = activeChar.getInventory().addItem("Armor", 2449, 1, activeChar, null);
                       
                      //Weapon
                       ItemInstance item6 = activeChar.getInventory().addItem("Weapon", 2525, 1, activeChar, null);
                       
                      //Jewels
                       ItemInstance item7 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item8 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item9 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item10 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item11 = activeChar.getInventory().addItem("Necklace", 913, 1, activeChar, null);
                       
                       //Equipp
                       activeChar.getInventory().equipItemAndRecord(item1);
                       activeChar.getInventory().equipItemAndRecord(item2);
                       activeChar.getInventory().equipItemAndRecord(item3);
                       activeChar.getInventory().equipItemAndRecord(item4);
                       activeChar.getInventory().equipItemAndRecord(item5);
                       activeChar.getInventory().equipItemAndRecord(item6);
                       activeChar.getInventory().equipItemAndRecord(item7);
                       activeChar.getInventory().equipItemAndRecord(item8);
                       activeChar.getInventory().equipItemAndRecord(item9);
                       activeChar.getInventory().equipItemAndRecord(item10);
                       activeChar.getInventory().equipItemAndRecord(item11);
                       
                       //Secund Weapon
                       activeChar.getInventory().addItem("Weapon", 297, 1, activeChar, null);
                       
                       //Misc
                       activeChar.getInventory().addItem("Soul Shot Grade D", 1463, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Mana Potion", 728, 100, activeChar, null);
                       activeChar.getInventory().addItem("Greater Healing Potion", 1539, 20, activeChar, null);
                       activeChar.getInventory().addItem("Scroll of Scape", 736, 5, activeChar, null);
                       
                       activeChar.getInventory().updateDatabase();
                       activeChar.sendPacket(new ItemList(activeChar, true));
       
                       playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
                       MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
                       activeChar.broadcastPacket(MSU);
                   }
                   //Heavy
                   if (activeChar.getClassId() == ClassId.KNIGHT || activeChar.getClassId() == ClassId.ELVEN_KNIGHT || activeChar.getClassId() == ClassId.PALUS_KNIGHT || activeChar.getClassId() == ClassId.SCAVENGER || activeChar.getClassId() == ClassId.ARTISAN)
                   {
                       //Armor
                       ItemInstance item1 = activeChar.getInventory().addItem("Armor", 352, 1, activeChar, null);
                       ItemInstance item2 = activeChar.getInventory().addItem("Armor", 2378, 1, activeChar, null);
                       ItemInstance item3 = activeChar.getInventory().addItem("Armor", 2411, 1, activeChar, null);
                       ItemInstance item4 = activeChar.getInventory().addItem("Armor", 2425, 1, activeChar, null);
                       ItemInstance item5 = activeChar.getInventory().addItem("Armor", 2449, 1, activeChar, null);
                       
                       //Weapon
                       ItemInstance item6 = activeChar.getInventory().addItem("Weapon", 2499, 1, activeChar, null);
                       
                       //Jewels
                       ItemInstance item7 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item8 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item9 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item10 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item11 = activeChar.getInventory().addItem("Necklace", 913, 1, activeChar, null);
                       
                       //Shield
                       ItemInstance item12 = activeChar.getInventory().addItem("Shield", 2493, 1, activeChar, null);
                       
                       //Equipp
                       activeChar.getInventory().equipItemAndRecord(item1);
                       activeChar.getInventory().equipItemAndRecord(item2);
                       activeChar.getInventory().equipItemAndRecord(item3);
                       activeChar.getInventory().equipItemAndRecord(item4);
                       activeChar.getInventory().equipItemAndRecord(item5);
                       activeChar.getInventory().equipItemAndRecord(item6);
                       activeChar.getInventory().equipItemAndRecord(item7);
                       activeChar.getInventory().equipItemAndRecord(item8);
                       activeChar.getInventory().equipItemAndRecord(item9);
                       activeChar.getInventory().equipItemAndRecord(item10);
                       activeChar.getInventory().equipItemAndRecord(item11);
                       activeChar.getInventory().equipItemAndRecord(item12);
                       
                       //Secund Weapon
                       activeChar.getInventory().addItem("Weapon", 159, 1, activeChar, null);
                       
                       //Misc
                       activeChar.getInventory().addItem("Soul Shot Grade D", 1463, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Mana Potion", 728, 100, activeChar, null);
                       activeChar.getInventory().addItem("Greater Healing Potion", 1539, 20, activeChar, null);
                       activeChar.getInventory().addItem("Scroll of Scape", 736, 5, activeChar, null);
                       
                       activeChar.getInventory().updateDatabase();
                       activeChar.sendPacket(new ItemList(activeChar, true));
       
                       playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
                       MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
                       activeChar.broadcastPacket(MSU);
                   }
                   //Light
                   if (activeChar.getClassId() == ClassId.ROGUE || activeChar.getClassId() == ClassId.ELVEN_SCOUT || activeChar.getClassId() == ClassId.ASSASSIN)
                   {
                       //Armor
                       ItemInstance item1 = activeChar.getInventory().addItem("Armor", 395, 1, activeChar, null);
                       ItemInstance item2 = activeChar.getInventory().addItem("Armor", 417, 1, activeChar, null);
                       ItemInstance item3 = activeChar.getInventory().addItem("Armor", 2424, 1, activeChar, null);
                       ItemInstance item4 = activeChar.getInventory().addItem("Armor", 2448, 1, activeChar, null);
                       ItemInstance item5 = activeChar.getInventory().addItem("Armor", 2412, 1, activeChar, null);
                       
                       //Weapon
                       ItemInstance item6 = activeChar.getInventory().addItem("Weapon", 225, 1, activeChar, null);
                       
                       //Jewels
                       ItemInstance item7 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item8 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item9 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item10 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item11 = activeChar.getInventory().addItem("Necklace", 913, 1, activeChar, null);
                       
                       //Equip
                       activeChar.getInventory().equipItemAndRecord(item1);
                       activeChar.getInventory().equipItemAndRecord(item2);
                       activeChar.getInventory().equipItemAndRecord(item3);
                       activeChar.getInventory().equipItemAndRecord(item4);
                       activeChar.getInventory().equipItemAndRecord(item5);
                       activeChar.getInventory().equipItemAndRecord(item6);
                       activeChar.getInventory().equipItemAndRecord(item7);
                       activeChar.getInventory().equipItemAndRecord(item8);
                       activeChar.getInventory().equipItemAndRecord(item9);
                       activeChar.getInventory().equipItemAndRecord(item10);
                       activeChar.getInventory().equipItemAndRecord(item11);
                       
                       //Secund Weapon
                       activeChar.getInventory().addItem("Weapon", 280, 1, activeChar, null);
                       
                       //Misc
                       activeChar.getInventory().addItem("Soul Shot Grade D", 1463, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Arrow D", 1341, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Mana Potion", 728, 100, activeChar, null);
                       activeChar.getInventory().addItem("Greater Healing Potion", 1539, 20, activeChar, null);
                       activeChar.getInventory().addItem("Scroll of Scape", 736, 5, activeChar, null);
                       
                       activeChar.getInventory().updateDatabase();
                       activeChar.sendPacket(new ItemList(activeChar, true));
       
                       playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
                       MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
                       activeChar.broadcastPacket(MSU);
                   }
                   //Robe
                   if (activeChar.getClassId() == ClassId.HUMAN_MYSTIC || activeChar.getClassId() == ClassId.CLERIC || activeChar.getClassId() == ClassId.ELVEN_WIZARD || activeChar.getClassId() == ClassId.SHILLIEN_ORACLE || activeChar.getClassId() == ClassId.DARK_WIZARD || activeChar.getClassId() == ClassId.ELVEN_ORACLE || activeChar.getClassId() == ClassId.ORC_SHAMAN)
                   {
                     //Armor
                       ItemInstance item1 = activeChar.getInventory().addItem("Armor", 437, 1, activeChar, null);
                     ItemInstance item2 = activeChar.getInventory().addItem("Armor", 470, 1, activeChar, null);
                       ItemInstance item3 = activeChar.getInventory().addItem("Armor", 2450, 1, activeChar, null);
                      ItemInstance item4 = activeChar.getInventory().addItem("Armor", 2426, 1, activeChar, null);
                       ItemInstance item5 = activeChar.getInventory().addItem("Armor", 2412, 1, activeChar, null);
                       
                       //Weapon
                       ItemInstance item6 = activeChar.getInventory().addItem("Weapon", 189, 1, activeChar, null);
                       
                       //Jewels
                       ItemInstance item7 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item8 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item9 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item10 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item11 = activeChar.getInventory().addItem("Necklace", 913, 1, activeChar, null);
                       
                       //Shield
                       ItemInstance item12 = activeChar.getInventory().addItem("Shield", 629, 1, activeChar, null);
                       
                       //Equipp
                       activeChar.getInventory().equipItemAndRecord(item1);
                       activeChar.getInventory().equipItemAndRecord(item2);
                       activeChar.getInventory().equipItemAndRecord(item3);
                       activeChar.getInventory().equipItemAndRecord(item4);
                       activeChar.getInventory().equipItemAndRecord(item5);
                       activeChar.getInventory().equipItemAndRecord(item6);
                       activeChar.getInventory().equipItemAndRecord(item7);
                       activeChar.getInventory().equipItemAndRecord(item8);
                       activeChar.getInventory().equipItemAndRecord(item9);
                       activeChar.getInventory().equipItemAndRecord(item10);
                       activeChar.getInventory().equipItemAndRecord(item11);
                       activeChar.getInventory().equipItemAndRecord(item12);
       
                       //Misc
                       activeChar.getInventory().addItem("Spirit Shot Grade D", 3948, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Mana Potion", 728, 100, activeChar, null);
                       activeChar.getInventory().addItem("Greater Healing Potion", 1539, 20, activeChar, null);
                       activeChar.getInventory().addItem("Scroll of Scape", 736, 5, activeChar, null);
                       
                       activeChar.getInventory().updateDatabase();
                       activeChar.sendPacket(new ItemList(activeChar, true));
       
                       playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
                       MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
                       activeChar.broadcastPacket(MSU);
                   }
                   //Heavy
                   if (activeChar.getClassId() == ClassId.ORC_RAIDER)
                   {
                       //Armor
                       ItemInstance item1 = activeChar.getInventory().addItem("Armor", 352, 1, activeChar, null);
                       ItemInstance item2 = activeChar.getInventory().addItem("Armor", 2378, 1, activeChar, null);
                       ItemInstance item3 = activeChar.getInventory().addItem("Armor", 2411, 1, activeChar, null);
                       ItemInstance item4 = activeChar.getInventory().addItem("Armor", 2425, 1, activeChar, null);
                       ItemInstance item5 = activeChar.getInventory().addItem("Armor", 2449, 1, activeChar, null);
                       
                       //Weapon
                       ItemInstance item6 = activeChar.getInventory().addItem("Weapon", 70, 1, activeChar, null);
                       
                       //Jewels
                       ItemInstance item7 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item8 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item9 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item10 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item11 = activeChar.getInventory().addItem("Necklace", 913, 1, activeChar, null);
                       
                       //Equipp
                       activeChar.getInventory().equipItemAndRecord(item1);
                       activeChar.getInventory().equipItemAndRecord(item2);
                       activeChar.getInventory().equipItemAndRecord(item3);
                       activeChar.getInventory().equipItemAndRecord(item4);
                       activeChar.getInventory().equipItemAndRecord(item5);
                       activeChar.getInventory().equipItemAndRecord(item6);
                       activeChar.getInventory().equipItemAndRecord(item7);
                       activeChar.getInventory().equipItemAndRecord(item8);
                       activeChar.getInventory().equipItemAndRecord(item9);
                       activeChar.getInventory().equipItemAndRecord(item10);
                       activeChar.getInventory().equipItemAndRecord(item11);
                       
                       //Secund Weapon
                       activeChar.getInventory().addItem("Weapon", 297, 1, activeChar, null);
                       
                       //Misc
                       activeChar.getInventory().addItem("Soul Shot Grade D", 1463, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Mana Potion", 728, 100, activeChar, null);
                       activeChar.getInventory().addItem("Greater Healing Potion", 1539, 20, activeChar, null);
                       activeChar.getInventory().addItem("Scroll of Scape", 736, 5, activeChar, null);
                       
                       activeChar.getInventory().updateDatabase();
                       activeChar.sendPacket(new ItemList(activeChar, true));
       
                       playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
                       MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
                       activeChar.broadcastPacket(MSU);
                   }
                   //Light
                   if (activeChar.getClassId() == ClassId.MONK)
                   {
                       //Armor
                       ItemInstance item1 = activeChar.getInventory().addItem("Armor", 395, 1, activeChar, null);
                       ItemInstance item2 = activeChar.getInventory().addItem("Armor", 417, 1, activeChar, null);
                       ItemInstance item3 = activeChar.getInventory().addItem("Armor", 2424, 1, activeChar, null);
                       ItemInstance item4 = activeChar.getInventory().addItem("Armor", 2448, 1, activeChar, null);
                       ItemInstance item5 = activeChar.getInventory().addItem("Armor", 2412, 1, activeChar, null);
                       
                       //Weapon
                       ItemInstance item6 = activeChar.getInventory().addItem("Weapon", 262, 1, activeChar, null);
                       
                       //Jewels
                       ItemInstance item7 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item8 = activeChar.getInventory().addItem("Ring", 881, 1, activeChar, null);
                       ItemInstance item9 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item10 = activeChar.getInventory().addItem("Earring", 850, 1, activeChar, null);
                       ItemInstance item11 = activeChar.getInventory().addItem("Necklace", 913, 1, activeChar, null);
                       
                       //Equipp
                       activeChar.getInventory().equipItemAndRecord(item1);
                       activeChar.getInventory().equipItemAndRecord(item2);
                       activeChar.getInventory().equipItemAndRecord(item3);
                       activeChar.getInventory().equipItemAndRecord(item4);
                       activeChar.getInventory().equipItemAndRecord(item5);
                       activeChar.getInventory().equipItemAndRecord(item6);
                       activeChar.getInventory().equipItemAndRecord(item7);
                       activeChar.getInventory().equipItemAndRecord(item8);
                       activeChar.getInventory().equipItemAndRecord(item9);
                       activeChar.getInventory().equipItemAndRecord(item10);
                       activeChar.getInventory().equipItemAndRecord(item11);
                       
                       //Misc
                       activeChar.getInventory().addItem("Soul Shot Grade D", 1463, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Arrow D", 1341, 2000, activeChar, null);
                       activeChar.getInventory().addItem("Mana Potion", 728, 100, activeChar, null);
                       activeChar.getInventory().addItem("Greater Healing Potion", 1539, 20, activeChar, null);
                       activeChar.getInventory().addItem("Scroll of Scape", 736, 5, activeChar, null);
                       
                       activeChar.getInventory().updateDatabase();
                       activeChar.sendPacket(new ItemList(activeChar, true));
       
                       playable.destroyItem("Consume", item.getObjectId(), 1, null, false);
                       MagicSkillUse MSU = new MagicSkillUse(activeChar, activeChar, 2024, 1, 1, 0);
                       activeChar.broadcastPacket(MSU);
                   }
               }
           }
       }