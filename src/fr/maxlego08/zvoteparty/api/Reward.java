package fr.maxlego08.zvoteparty.api;

import java.util.List;

public interface Reward {

	/**
	 * Returns the percentage chance of getting the reward
	 * 
	 * @return percent
	 */
	double getPercent();
	
	/**
	 * Returns the list of commands that will be executed
	 * 
	 * @return commands
	 */
	List<String> getCommands();
	
}
