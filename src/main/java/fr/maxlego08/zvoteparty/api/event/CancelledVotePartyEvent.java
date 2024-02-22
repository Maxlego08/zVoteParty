package fr.maxlego08.zvoteparty.api.event;

import org.bukkit.event.Cancellable;

public class CancelledVotePartyEvent extends VotePartyEvent implements Cancellable {

	private boolean cancelled;

	/**
	 * @return the cancelled
	 */
	public boolean isCancelled() {
		return cancelled;
	}

	/**
	 * @param cancelled
	 *            the cancelled to set
	 */
	public void setCancelled(boolean cancelled) {
		this.cancelled = cancelled;
	}

}
