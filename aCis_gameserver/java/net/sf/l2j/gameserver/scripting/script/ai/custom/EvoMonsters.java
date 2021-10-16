package net.sf.l2j.gameserver.scripting.script.ai.custom;

import java.util.HashMap;
import java.util.Map;

import net.sf.l2j.gameserver.enums.ScriptEventType;
import net.sf.l2j.gameserver.model.actor.Creature;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.scripting.script.ai.AttackableAIScript;

public class EvoMonsters extends AttackableAIScript
{
   private static final Map<Integer, Integer[]> EVOSPAWNS = new HashMap<>();
   
   static
   {
       EVOSPAWNS.put(50020, new Integer[]
       {
           50021,
           100,
           100,
           0
       }); // Evo Monster 1st stage
       EVOSPAWNS.put(50021, new Integer[]
       {
           50022,
           100,
           100,
           1
       }); // Evo Monster 2nd stage
       EVOSPAWNS.put(50022, new Integer[]
       {
           50023,
           100,
           100,
           2
       }); // Evo Monster 3nd stage
       EVOSPAWNS.put(50023, new Integer[]
       {
           50024,
           100,
           100,
           3
       }); // Evo Monster 4nd stage
       EVOSPAWNS.put(50024, new Integer[]
       {
           50025,
           100,
           100,
           4
       }); // Evo Monster 5nd stage
   }
   
   public EvoMonsters()
   {
       super("ai/custom");
   }
   
   @Override
   protected void registerNpcs()
   {
       addEventIds(EVOSPAWNS.keySet(), ScriptEventType.ON_KILL);
   }
   
   @Override
   public String onKill(Npc npc, Creature attacker)
   {
       if (npc.isVisible() && npc.isDead())
       {
           final Integer[] tmp = EVOSPAWNS.get(npc.getNpcId());
           if (tmp != null)
           {               
               final Npc newNpc = addSpawn(tmp[0], npc, false, 0, true);
               newNpc.forceAttack(attacker, 200);
               npc.deleteMe();
           }
       }
       return super.onKill(npc, attacker);
   }
}