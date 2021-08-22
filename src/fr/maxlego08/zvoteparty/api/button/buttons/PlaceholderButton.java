package fr.maxlego08.zvoteparty.api.button.buttons;

import fr.maxlego08.zvoteparty.api.enums.PlaceholderAction;

public interface PlaceholderButton extends PermissibleButton {

	/**
	 * 
	 * @return placeholder
	 */
	public String getPlaceHolder();

	/**
	 * 
	 * @return action
	 */
	public PlaceholderAction getAction();

	/**
	 * 
	 * @return boolean
	 */
	public boolean hasPlaceHolder();

	/**
	 * 
	 * @return value
	 */
	public String getValue();

}
