package nl.knokko.customitems.plugin;

import nl.knokko.customitems.sound.SoundValues;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;

import java.util.Locale;
import java.util.Objects;

public class SoundPlayer {

    private static String determineSoundName(SoundValues sound) {
        if (sound.getVanillaSound() != null) return sound.getVanillaSound().name().toLowerCase(Locale.ROOT);
        else return "kci_" + sound.getCustomSound().getName();
    }

    private static SoundCategory determineSoundCategory(SoundValues sound) {
        if (sound.getCustomSound() != null) return SoundCategory.valueOf(sound.getCustomSound().getSoundCategory().name());

        // Vanilla sounds will respect the sound category by default, so I can use just the playSound methods
        // without a SoundCategory parameter
        else return null;
    }

    public static void playSound(Location location, SoundValues sound) {
        Objects.requireNonNull(location.getWorld());
        SoundCategory category = determineSoundCategory(sound);
        if (category == null) location.getWorld().playSound(location, determineSoundName(sound), sound.getVolume(), sound.getPitch());
        else location.getWorld().playSound(location, determineSoundName(sound), category, sound.getVolume(), sound.getPitch());
    }

    public static void playSound(Player player, SoundValues sound) {
        SoundCategory category = determineSoundCategory(sound);
        if (category == null) player.playSound(player.getLocation(), determineSoundName(sound), sound.getVolume(), sound.getPitch());
        else player.playSound(player.getLocation(), determineSoundName(sound), category, sound.getVolume(), sound.getPitch());
    }

    private static final int JUKEBOX_RANGE = 16;

    public static void stopSound(Location location, SoundValues sound, boolean forceRecordCategory) {
        SoundCategory category = determineSoundCategory(sound);

        for (Player player : Objects.requireNonNull(location.getWorld()).getPlayers()) {
            if (location.distance(player.getLocation()) <= JUKEBOX_RANGE * sound.getVolume()) {
                if (forceRecordCategory) player.stopSound(Sound.valueOf(sound.getVanillaSound().name()), SoundCategory.RECORDS);
                else if (category != null) player.stopSound(determineSoundName(sound), category);
                else player.stopSound(determineSoundName(sound));
            }
        }
    }
}
