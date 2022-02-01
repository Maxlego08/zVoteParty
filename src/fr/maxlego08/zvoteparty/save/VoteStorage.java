package fr.maxlego08.zvoteparty.save;

import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Saveable;

public class VoteStorage implements Saveable {

	public static long voteCount = 0;
	
	/**
	 * static Singleton instance.
	 */
	private static volatile VoteStorage instance;



	/**
	 * Private constructor for singleton.
	 */
	private VoteStorage() {
	}

	/**
	 * Return a singleton instance of Config.
	 */
	public static VoteStorage getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (VoteStorage.class) {
				if (instance == null) {
					instance = new VoteStorage();
				}
			}
		}
		return instance;
	}

	public void save(Persist persist) {
		persist.save(getInstance(), "storage");
	}

	public void load(Persist persist) {
		persist.loadOrSaveDefault(getInstance(), VoteStorage.class, "storage");
	}

}
