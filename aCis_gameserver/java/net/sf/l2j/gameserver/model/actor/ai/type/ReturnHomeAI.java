package net.sf.l2j.gameserver.model.actor.ai.type;

import net.sf.l2j.commons.pool.ThreadPool;

import net.sf.l2j.gameserver.enums.IntentionType;
import net.sf.l2j.gameserver.model.actor.Npc;
import net.sf.l2j.gameserver.model.location.Location;

public class ReturnHomeAI
{
	private static final int WALKING_TIME = 4000;
	private static final int RESTING_TIME = 4000;
	
	private final Npc _npc;
	
	private boolean _isReturningHome = false;
	private final Location _lastLocation;
	
	public ReturnHomeAI(Npc npc)
	{
		_npc = npc;
		_lastLocation = new Location(npc.getPosition());
	}
	
	/**
	 * Returns true if NPC is returning home. Also returns true if it rests.
	 * @return
	 */
	public boolean isReturningHome()
	{
		return _isReturningHome;
	}
	
	/**
	 * Starts returning home. Takes effect only first call.
	 */
	public void startReturningHome()
	{
		// Don't do anything if already returning home.
		if (_isReturningHome)
			return;
		
		_isReturningHome = true;
		startWalk();
	}
	
	/**
	 * Completely stops returning to home process.
	 */
	public void stopReturningHome()
	{
		if (_isReturningHome)
		{
			_isReturningHome = false;
			_npc.getMove().stop();
			_npc.getAI().tryToActive();
		}
	}
	
	/**
	 * Starts walking to the spawn location.
	 */
	public void startWalk()
	{
		// Interrupt returning to home if intention is not active.
		if (_npc.getAI().getCurrentIntention().getType() != IntentionType.ACTIVE)
		{
			_isReturningHome = false;
			return;
		}
		
		// Check if we already returned before walking.
		if (getSqDistTo(_npc.getSpawn().getLoc()) < 10000)
		{
			stopReturningHome();
			return;
		}
		
		_lastLocation.set(_npc.getPosition());
		_npc.forceWalkStance();
		_npc.getAI().tryToMoveTo(_npc.getSpawn().getLoc(), null);
		if (_isReturningHome)
			ThreadPool.schedule(this::doRest, WALKING_TIME);
	}
	
	/**
	 * Stops and waits a little bit.
	 */
	public void doRest()
	{
		// Interrupt returning to home if intention is not active.
		if (_npc.getAI().getCurrentIntention().getType() != IntentionType.MOVE_TO)
		{
			_isReturningHome = false;
			return;
		}
		
		if (isStuck())
		{
			teleportToSpawn();
			return;
		}
		
		_npc.getMove().stop();
		_npc.getAI().tryToActive();
		if (_isReturningHome)
			ThreadPool.schedule(this::startWalk, RESTING_TIME);
	}
	
	/**
	 * If monster don't move at least 200 distance - it stuck.
	 * @return
	 */
	private boolean isStuck()
	{
		return getSqDistTo(_lastLocation) < 5000;
	}
	
	/**
	 * Returns square distance to the given location.
	 * @param loc
	 * @return
	 */
	private int getSqDistTo(Location loc)
	{
		final int dx = _npc.getX() - loc.getX();
		final int dy = _npc.getY() - loc.getY();
		final int dz = _npc.getZ() - loc.getZ();
		return dx * dx + dy * dy + dz * dz;
	}
	
	/**
	 * Teleports monster to the spawn location. Call this method when movement is blocked or can't find path.
	 */
	private void teleportToSpawn()
	{
		_isReturningHome = false;
		_npc.getMove().stop();
		_npc.teleportTo(_npc.getSpawn().getLoc(), 0);
	}
}