package fr.maxlego08.zvoteparty.save;

import fr.maxlego08.zvoteparty.zcore.utils.storage.Persist;
import fr.maxlego08.zvoteparty.zcore.utils.storage.Saveable;

public class Storage implements Saveable {

	public static long voteCount = 0;
	
	/**
	 * static Singleton instance.
	 */
	private static volatile Storage instance;



	/**
	 * Private constructor for singleton.
	 */
	private Storage() {
	}

	/**
	 * Return a singleton instance of Config.
	 */
	public static Storage getInstance() {
		// Double lock for thread safety.
		if (instance == null) {
			synchronized (Storage.class) {
				if (instance == null) {
					instance = new Storage();
				}
			}
		}
		return instance;
	}

	public void save(Persist persist) {
		persist.save(getInstance());
	}

	public void load(Persist persist) {
		persist.loadOrSaveDefault(getInstance(), Storage.class);
	}

}
