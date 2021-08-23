package fr.maxlego08.zvoteparty.zcore.utils.interfaces;

import java.util.Collection;

@FunctionalInterface
public interface CollectionConsumer<T> {

	Collection<String> accept(T t);
	
}
