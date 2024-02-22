package fr.maxlego08.zvoteparty.zcore.enums;

public enum Folder {

	PLAYERS,

	;
	

	public String toFolder(){
		return name().toLowerCase();
	}
	
}
