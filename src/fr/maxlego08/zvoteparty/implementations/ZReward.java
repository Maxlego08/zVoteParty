package fr.maxlego08.zvoteparty.implementations;

import java.util.List;

import fr.maxlego08.zvoteparty.api.Reward;

public class ZReward implements Reward {

	private final double percent;
	private final List<String> commands;

	/**
	 * @param percent
	 * @param commands
	 */
	public ZReward(double percent, List<String> commands) {
		super();
		this.percent = percent;
		this.commands = commands;
	}

	@Override
	public double getPercent() {
		return this.percent;
	}

	@Override
	public List<String> getCommands() {
		return this.commands;
	}

}
