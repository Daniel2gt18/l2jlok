package net.sf.l2j.gameserver.model.actor.instance;

import net.sf.l2j.Config;
import net.sf.l2j.gameserver.data.manager.CharacterKillingManager;
import net.sf.l2j.gameserver.model.actor.PcPolymorph;
import net.sf.l2j.gameserver.model.actor.template.NpcTemplate;

/**
 * @author paytaly
 */
public class TopPKMonumentInstance extends PcPolymorph
{
   public TopPKMonumentInstance(int objectId, NpcTemplate template)
   {
       super(objectId, template);
       
       if (Config.CKM_ENABLED)
           CharacterKillingManager.getInstance().addPKMorphListener(this);
   }
}