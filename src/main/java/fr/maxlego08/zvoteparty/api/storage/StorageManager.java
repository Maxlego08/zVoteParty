package fr.maxlego08.zvoteparty.api.storage;

import fr.maxlego08.zvoteparty.zcore.utils.storage.Saveable;

public interface StorageManager extends Saveable {

	Storage getStorage();
	
	IStorage getIStorage();
	
}
