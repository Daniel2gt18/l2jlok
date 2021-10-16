package net.sf.l2j.gameserver.network.serverpackets;

import net.sf.l2j.gameserver.data.sql.ClanTable;
import net.sf.l2j.gameserver.data.xml.PlayerData;
import net.sf.l2j.gameserver.enums.Paperdoll;
import net.sf.l2j.gameserver.model.CharSelectSlot;
import net.sf.l2j.gameserver.model.actor.PcPolymorph;
import net.sf.l2j.gameserver.model.actor.template.PlayerTemplate;
import net.sf.l2j.gameserver.model.pledge.Clan;

/**
 * @author paytaly
 */
public final class NpcInfoPolymorph extends L2GameServerPacket
{
   private final PcPolymorph _activeChar;
   private final CharSelectSlot _morph;
   private final PlayerTemplate _template;
   private final Clan _clan;
   private final int _x, _y, _z, _heading;
   private final int _mAtkSpd, _pAtkSpd;
   private final int _runSpd, _walkSpd;
   private final float _moveMultiplier;
   
   public NpcInfoPolymorph(PcPolymorph cha)
   {
       _activeChar = cha;
       _morph = cha.getPolymorphInfo();
       _template = PlayerData.getInstance().getTemplate(_morph.getBaseClassId());
       _clan = ClanTable.getInstance().getClan(_morph.getClanId());
       
       _x = _activeChar.getX();
       _y = _activeChar.getY();
       _z = _activeChar.getZ();
       _heading = _activeChar.getHeading();
       
       _mAtkSpd = _activeChar.getStatus().getMAtkSpd();
       _pAtkSpd = _activeChar.getStatus().getPAtkSpd();
       
       _moveMultiplier = _activeChar.getStatus().getMovementSpeedMultiplier();
       _runSpd = (int) (_activeChar.getStatus().getBaseRunSpeed() / _moveMultiplier);
       _walkSpd = (int) (_activeChar.getStatus().getBaseWalkSpeed() / _moveMultiplier);
   }
   
   @Override
   protected final void writeImpl()
   {
       writeC(0x03);
       writeD(_x);
       writeD(_y);
       writeD(_z);
       writeD(_heading);
       writeD(_activeChar.getObjectId());
       writeS(_morph.getName());
       writeD(_morph.getRace());
       writeD(_morph.getSex());
       
       writeD(_morph.getBaseClassId());
       
       writeD(_morph.getPaperdollItemId(Paperdoll.HAIRALL));
       writeD(_morph.getPaperdollItemId(Paperdoll.HEAD));
       writeD(_morph.getPaperdollItemId(Paperdoll.RHAND));
       writeD(_morph.getPaperdollItemId(Paperdoll.LHAND));
       writeD(_morph.getPaperdollItemId(Paperdoll.GLOVES));
       writeD(_morph.getPaperdollItemId(Paperdoll.CHEST));
       writeD(_morph.getPaperdollItemId(Paperdoll.LEGS));
       writeD(_morph.getPaperdollItemId(Paperdoll.FEET));
       writeD(_morph.getPaperdollItemId(Paperdoll.CLOAK));
       writeD(_morph.getPaperdollItemId(Paperdoll.RHAND));
       writeD(_morph.getPaperdollItemId(Paperdoll.HAIR));
       writeD(_morph.getPaperdollItemId(Paperdoll.FACE));
       
       // c6 new h's
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeD(_morph.getAugmentationId());
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeD(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       writeH(0x00);
       
       writeD(0);
       writeD(0);
       
       writeD(_mAtkSpd);
       writeD(_pAtkSpd);
       
       writeD(0);
       writeD(0);
       
       writeD(_runSpd);
       writeD(_walkSpd);
       writeD(_runSpd); // swim run speed
       writeD(_walkSpd); // swim walk speed
       writeD(_runSpd); // fl run speed
       writeD(_walkSpd); // fl walk speed
       writeD(_runSpd); // fly run speed
       writeD(_walkSpd); // fly walk speed
       writeF(_activeChar.getStatus().getMovementSpeedMultiplier());
       writeF(_activeChar.getStatus().getAttackSpeedMultiplier());
       
       writeF(_template.getCollisionRadius());
       writeF(_template.getCollisionHeight());
       
       writeD(_morph.getHairStyle());
       writeD(_morph.getHairColor());
       writeD(_morph.getFace());
       
       writeS(_activeChar.getVisibleTitle());
       
       if (_clan != null)
       {
           writeD(_clan.getClanId());
           writeD(_clan.getCrestId());
           writeD(_clan.getAllyId());
           writeD(_clan.getAllyCrestId());
       }
       else
       {
           writeD(0);
           writeD(0);
           writeD(0);
           writeD(0);
       }
       
       writeD(0);
       
       writeC(1); // standing = 1 sitting = 0
       writeC(_activeChar.isRunning() ? 1 : 0); // running = 1 walking = 0
       writeC(_activeChar.isInCombat() ? 1 : 0);
       writeC(_activeChar.isAlikeDead() ? 1 : 0);
       
       writeC(0); // invisible = 1 visible =0
           
       writeC(0); // 1 on strider 2 on wyvern 0 no mount
       writeC(0); // 1 - sellshop
       
       writeH(0);
       
       writeC(0);
       
       writeD(_activeChar.getAbnormalEffect());

       writeC(0);
       writeH(0); // Blue value for name (0 = white, 255 = pure blue)
       writeD(_morph.getClassId());
       
       writeD(0);
       writeD(0);
       writeC((_morph.getEnchantEffect() > 127) ? 127 : _morph.getEnchantEffect());
       
       writeC(0x00); // team circle around feet 1= Blue, 2 = red
           
       writeD(_clan != null ? _clan.getCrestLargeId() : 0);
       writeC(0); // Symbol on char menu ctrl+I
       writeC(0); // Hero Aura
       
       writeC(0); // 0x01: Fishing Mode (Cant be undone by setting back to 0)
       writeD(0);
       writeD(0);
       writeD(0);
       
       writeD(_activeChar.getNameColor());
       
       writeD(0x00); // isRunning() as in UserInfo?
      
       writeD(0);
       writeD(0);
       
       writeD(_activeChar.getTitleColor());
       
       writeD(0x00);
   }
}