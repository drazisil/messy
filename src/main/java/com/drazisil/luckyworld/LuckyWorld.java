package com.drazisil.luckyworld;

import com.drazisil.luckyworld.event.*;
import com.drazisil.luckyworld.world.WorldHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;

import static com.drazisil.luckyworld.event.LWEventHandler.LuckyEventRarity.*;
import static org.bukkit.Bukkit.getPluginManager;

public final class LuckyWorld extends JavaPlugin {

    public static final Logger logger = LogManager.getLogger();
    private static LuckyWorld instance;
    public static WorldHandler worldHandler;
    static final String name = "LuckyWorld";
    private static int maxNumber;
    private static int magicNumber;

    @Override
    public void onEnable() {

        instance = this;

        saveDefaultConfig();

        maxNumber = getConfig().getInt("max-number", 10);
        magicNumber = getConfig().getInt("magic-number", 3);


        // Create world
        worldHandler = new WorldHandler();


        // Register events
        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventMultiBlock(), "multiblock"));
        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventUndeadHorse(), "undead_horse"));
        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventSheep(), "sheep"));
        LWEventHandler.registerEvent(COMMON, new LuckyEventEntry(
                new LuckyEventParrotDance(), "parrot_dance"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventJumpBoost(), "jump"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventMooshoom(), "mooshoom"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventChickens(), "chickens"));
        LWEventHandler.registerEvent(UNCOMMON, new LuckyEventEntry(
                new LuckyEventOuch(), "loony"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventSlowFallGhast(), "ghast"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventBang(), "tnt"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventSealantern(), "sea_lantern"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventDisco(), "disco"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventEndTimes(), "end_times"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventZombieLord(), "miniboss"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventLavaFloor(), "lava_pit"));
        LWEventHandler.registerEvent(RARE, new LuckyEventEntry(
                new LuckyEventNewWorld(), "new_world"));



        getServer().getPluginManager().registerEvents(new LWListener(), this);

        // Register command manager
        try {
            this.getCommand("lucky").setExecutor(new LWCommands());
            this.getCommand("lucky").setTabCompleter(new LWTabComplete());

        } catch (NullPointerException x) {
            logger.error("Error registering commands, quitting.");
            getPluginManager().disablePlugin(instance);
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static int getMaxNumber() {
        return maxNumber;
    }

    public static int getMagicNumber() {
        return magicNumber;
    }


    public static LuckyWorld getInstance() {
        return instance;
    }

    public Location cleanLocation(Location inLocation) {
        Location outLocation = inLocation.clone();
        outLocation.setX(Math.floor(inLocation.getX()));
        outLocation.setY(Math.floor(inLocation.getY()));
        outLocation.setZ(Math.floor(inLocation.getZ()));
        return outLocation;
    }

    public String locationToString(Location loc) {
        return Math.floor(loc.getX()) +
                " " + Math.floor(loc.getY()) +
                " " + Math.floor(loc.getZ());
    }

}
