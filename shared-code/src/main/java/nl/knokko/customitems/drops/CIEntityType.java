package nl.knokko.customitems.drops;

import static nl.knokko.customitems.MCVersions.*;

import nl.knokko.customitems.NameHelper;

public enum CIEntityType {

	ELDER_GUARDIAN(VERSION1_12, VERSION1_19),
	WITHER_SKELETON(VERSION1_12, VERSION1_19),
	STRAY(VERSION1_12, VERSION1_19),
	HUSK(VERSION1_12, VERSION1_19),
	ZOMBIE_VILLAGER(VERSION1_12, VERSION1_19),
	SKELETON_HORSE(VERSION1_12, VERSION1_19),
	ZOMBIE_HORSE(VERSION1_12, VERSION1_19),
	ARMOR_STAND(VERSION1_12, VERSION1_19),
	DONKEY(VERSION1_12, VERSION1_19),
	MULE(VERSION1_12, VERSION1_19),
	EVOKER(VERSION1_12, VERSION1_19),
	VEX(VERSION1_12, VERSION1_19),
	VINDICATOR(VERSION1_12, VERSION1_19),
	ILLUSIONER(VERSION1_12, VERSION1_19),
	CREEPER(VERSION1_12, VERSION1_19),
	SKELETON(VERSION1_12, VERSION1_19),
	SPIDER(VERSION1_12, VERSION1_19),
	GIANT(VERSION1_12, VERSION1_19),
	ZOMBIE(VERSION1_12, VERSION1_19),
	SLIME(VERSION1_12, VERSION1_19),
	GHAST(VERSION1_12, VERSION1_19),
	PIG_ZOMBIE(VERSION1_12, VERSION1_15),
	ENDERMAN(VERSION1_12, VERSION1_19),
	CAVE_SPIDER(VERSION1_12, VERSION1_19),
	SILVERFISH(VERSION1_12, VERSION1_19),
	BLAZE(VERSION1_12, VERSION1_19),
	MAGMA_CUBE(VERSION1_12, VERSION1_19),
	ENDER_DRAGON(VERSION1_12, VERSION1_19),
	WITHER(VERSION1_12, VERSION1_19),
	BAT(VERSION1_12, VERSION1_19),
	WITCH(VERSION1_12, VERSION1_19),
	ENDERMITE(VERSION1_12, VERSION1_19),
	GUARDIAN(VERSION1_12, VERSION1_19),
	SHULKER(VERSION1_12, VERSION1_19),
	PIG(VERSION1_12, VERSION1_19),
	SHEEP(VERSION1_12, VERSION1_19),
	COW(VERSION1_12, VERSION1_19),
	CHICKEN(VERSION1_12, VERSION1_19),
	SQUID(VERSION1_12, VERSION1_19),
	WOLF(VERSION1_12, VERSION1_19),
	MUSHROOM_COW(VERSION1_12, VERSION1_19),
	SNOWMAN(VERSION1_12, VERSION1_19),
	OCELOT(VERSION1_12, VERSION1_19),
	IRON_GOLEM(VERSION1_12, VERSION1_19),
	HORSE(VERSION1_12, VERSION1_19),
	RABBIT(VERSION1_12, VERSION1_19),
	POLAR_BEAR(VERSION1_12, VERSION1_19),
	LLAMA(VERSION1_12, VERSION1_19),
	PARROT(VERSION1_12, VERSION1_19),
	VILLAGER(VERSION1_12, VERSION1_19),
	PLAYER(VERSION1_12, VERSION1_19),
	NPC(VERSION1_12, VERSION1_19), // NPC is a kinda special entity type...
	TURTLE(VERSION1_13, VERSION1_19),
	PHANTOM(VERSION1_13, VERSION1_19),
	COD(VERSION1_13, VERSION1_19),
	SALMON(VERSION1_13, VERSION1_19),
	PUFFERFISH(VERSION1_13, VERSION1_19),
	TROPICAL_FISH(VERSION1_13, VERSION1_19),
	DROWNED(VERSION1_13, VERSION1_19),
	DOLPHIN(VERSION1_13, VERSION1_19),
	CAT(VERSION1_14, VERSION1_19),
	PANDA(VERSION1_14, VERSION1_19),
	PILLAGER(VERSION1_14, VERSION1_19),
	RAVAGER(VERSION1_14, VERSION1_19),
	TRADER_LLAMA(VERSION1_14, VERSION1_19),
	WANDERING_TRADER(VERSION1_14, VERSION1_19),
	FOX(VERSION1_14, VERSION1_19),
	BEE(VERSION1_15, VERSION1_19),
	ZOMBIFIED_PIGLIN(VERSION1_16, VERSION1_19),
	HOGLIN(VERSION1_16, VERSION1_19),
	PIGLIN(VERSION1_16, VERSION1_19),
	STRIDER(VERSION1_16, VERSION1_19),
	ZOGLIN(VERSION1_16, VERSION1_19),
	PIGLIN_BRUTE(VERSION1_17, VERSION1_19),
	AXOLOTL(VERSION1_17, VERSION1_19),
	GLOW_ITEM_FRAME(VERSION1_17, VERSION1_19),
	GLOW_SQUID(VERSION1_17, VERSION1_19),
	GOAT(VERSION1_17, VERSION1_19),
	MARKER(VERSION1_17, VERSION1_19),
	ALLAY(VERSION1_19, VERSION1_19),
	CHEST_BOAT(VERSION1_19, VERSION1_19),
	FROG(VERSION1_19, VERSION1_19),
	TADPOLE(VERSION1_19, VERSION1_19),
	WARDEN(VERSION1_19, VERSION1_19);
	
	private static final CIEntityType[] ALL_TYPES = values();
	
	public static final int AMOUNT = ALL_TYPES.length;
	
	public static CIEntityType getByOrdinal(int ordinal) {
		return ALL_TYPES[ordinal];
	}
	
	/**
	 * Do NOT use this on players! The NPC case needs to be handled differently!
	 */
	public static CIEntityType fromBukkitEntityType(Enum<?> entityType) {
		try {
			return valueOf(entityType.name());
		} catch (IllegalArgumentException ex) {
			return null;
		}
	}
	
	public final int firstVersion, lastVersion;
	
	CIEntityType(int firstMcVersion, int lastMcVersion) {
		this.firstVersion = firstMcVersion;
		this.lastVersion = lastMcVersion;
	}
	
	@Override
	public String toString() {
		return NameHelper.getNiceEnumName(name(), firstVersion, lastVersion);
	}
}
