package fr.maxlego08.zvoteparty.zcore.utils.nms;

import org.bukkit.Bukkit;

public class NMSUtils {

	public static double version = getNMSVersion();

	/**
	 * Get minecraft server version
	 *
	 * @return version
	 */
	public static double getNMSVersion() {
		if (version != 0)
			return version;
		try {
			String var1 = Bukkit.getServer().getClass().getPackage().getName();
			String[] parts = var1.split("\\.");
			if (parts.length > 3) {
				String[] versionParts = parts[3].split("_");
				if (versionParts.length >= 2) {
					String majorVersion = versionParts[0].replace("v", "");
					String minorVersion = versionParts[1];
					return version = Double.parseDouble(majorVersion + "." + minorVersion);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0.0;
	}

	/**
	 * Check if minecraft version has shulker
	 *
	 * @return boolean
	 */
	public static boolean hasShulker() {
		return !isOneHand();
	}

	/**
	 * Check if minecraft version has barrel
	 *
	 * @return boolean
	 */
	public static boolean hasBarrel() {
		final double version = getNMSVersion();
		return !(version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.11
				|| version == 1.12 || version == 1.13);
	}

	/**
	 * check if version is greater than 1.13
	 *
	 * @return boolean
	 */
	public static boolean isNewVersion() {
		return !isOldVersion();
	}

	/**
	 * Check if version has one hand
	 *
	 * @return boolean
	 */
	public static boolean isOneHand() {
		return getNMSVersion() == 1.7 || getNMSVersion() == 1.8;
	}

	/**
	 * Check if version is minecraft 1.7
	 *
	 * @return boolean
	 */
	public static boolean isVeryOldVersion() {
		return getNMSVersion() == 1.7;
	}

	/**
	 * Check if version has itemmeta unbreakable
	 *
	 * @return boolean
	 */
	public static boolean isUnbreakable() {
		return version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10;
	}

	/**
	 * Check if version is old version of minecraft with old material system
	 *
	 * @return boolean
	 */
	public static boolean isOldVersion() {
		return version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.12
				|| version == 1.11;
	}

	/**
	 *
	 * Check if server version is new version
	 *
	 * @return boolean
	 */
	public static boolean isNewNMSVersion() {
		final double version = getNMSVersion();
		return !(version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.11
				|| version == 1.12 || version == 1.13 || version == 1.14 || version == 1.15 || version == 1.16);
	}

	/**
	 * Allows to check if the version has the colors in hex
	 *
	 * @return boolean
	 */
	public static boolean isHexColor() {
		return !(version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.11
				|| version == 1.12 || version == 1.13 || version == 1.14 || version == 1.15);
	}

	/**
	 *
	 * Check if server version is new version
	 *
	 * @return boolean
	 */
	public static boolean isNewNBTVersion() {
		final double version = getNMSVersion();
		return !(version == 1.7 || version == 1.8 || version == 1.9 || version == 1.10 || version == 1.11
				|| version == 1.12 || version == 1.13 || version == 1.14 || version == 1.15 || version == 1.16
				|| version == 1.17);
	}

}
