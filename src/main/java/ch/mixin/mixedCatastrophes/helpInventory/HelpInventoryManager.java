package ch.mixin.mixedCatastrophes.helpInventory;

import ch.mixin.mixedCatastrophes.catastropheManager.global.constructs.ConstructType;
import ch.mixin.mixedCatastrophes.catastropheManager.global.weather.WeatherCatastropheType;
import ch.mixin.mixedCatastrophes.catastropheManager.personal.dream.DreamType;
import ch.mixin.mixedCatastrophes.eventChange.aspect.AspectType;
import ch.mixin.mixedCatastrophes.helperClasses.Constants;
import ch.mixin.mixedCatastrophes.helperClasses.Coordinate3D;
import ch.mixin.mixedCatastrophes.main.MixedCatastrophesPlugin;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;

public class HelpInventoryManager {
    public static final ItemStack HelpBookItem;

    static {
        HelpBookItem = new ItemStack(Material.ENCHANTED_BOOK, 1);
        ItemMeta meta = HelpBookItem.getItemMeta();
        meta.setDisplayName(ChatColor.GOLD + "Mixed Catastrophes Dictionary");
        HelpBookItem.setItemMeta(meta);
    }

    public static final String FrontBackSymmetryText = "Also place this around the center in the front and back.";

    private final MixedCatastrophesPlugin plugin;
    private final HashMap<HelpInventoryType, HelpInventory> helpInventoryMap;
    private final HelpInventoryType mainInventoryType;

    public HelpInventoryManager(MixedCatastrophesPlugin plugin) {
        this.plugin = plugin;
        mainInventoryType = HelpInventoryType.Help;
        helpInventoryMap = new HashMap<>();
        initialize();
    }

    public void open(Player player) {
        player.openInventory(helpInventoryMap.get(mainInventoryType).getInventory());
    }

    public void click(InventoryClickEvent event) {
        HelpInventory inventory = getInventory(event.getInventory());

        if (inventory == null)
            return;

        int slot = event.getRawSlot();

        if (slot < inventory.getInventory().getSize()) {
            event.setCancelled(true);
            HashMap<Integer, HelpInventoryType> linkInventoryMap = inventory.getLinkInventoryMap();

            if (!linkInventoryMap.containsKey(slot))
                return;

            HelpInventoryType type = linkInventoryMap.get(slot);
            Player player = (Player) event.getWhoClicked();

            if (type == null) {
                player.closeInventory();
                return;
            }

            HelpInventory openInventory = helpInventoryMap.get(type);

            if (openInventory == null)
                return;

            player.openInventory(openInventory.getInventory());
        }
    }

    public void drag(InventoryDragEvent event) {
        HelpInventory inventory = getInventory(event.getInventory());

        if (inventory == null)
            return;

        for (int slot : event.getRawSlots()) {
            if (slot < inventory.getInventory().getSize()) {
                event.setCancelled(true);
                break;
            }
        }
    }

    public boolean contains(Inventory inventory) {
        return getInventory(inventory) != null;
    }

    private HelpInventory getInventory(Inventory inventory) {
        for (HelpInventory helpInventory : helpInventoryMap.values()) {
            if (helpInventory.getInventory().equals(inventory))
                return helpInventory;
        }

        return null;
    }

    private void initialize() {
        helpInventoryMap.put(HelpInventoryType.Help, makeHelpInventory());
        helpInventoryMap.put(HelpInventoryType.Weathers, makeWeathersInventory());
        helpInventoryMap.put(HelpInventoryType.Aspects, makeAspectsInventory());
        helpInventoryMap.put(HelpInventoryType.Rites, makeRitesInventory());
        helpInventoryMap.put(HelpInventoryType.Rites_Production, makeRitesProductionInventory());
        helpInventoryMap.put(HelpInventoryType.Rites_Sacrifice, makeRitesSacrificeInventory());
        helpInventoryMap.put(HelpInventoryType.Rites_Curse, makeRitesCurseInventory());
        helpInventoryMap.put(HelpInventoryType.Rites_Blessing, makeRitesBlessingInventory());
        helpInventoryMap.put(HelpInventoryType.Rites_Ultimatum, makeRitesUltimatumInventory());
        helpInventoryMap.put(HelpInventoryType.Dreams, makeDreamsInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs, makeConstructsInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_GreenWell, makeConstructsGreenWellInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_Blitzard, makeConstructsBlitzardInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_Lighthouse, makeConstructsLighthouseInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_BlazeReactor, makeConstructsBlazeReactorInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_Scarecrow, makeConstructsScarecrowInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_EnderRail, makeConstructsEnderRailInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_EnderRail_Side, makeConstructsEnderRailSideInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_EnderRail_Up, makeConstructsEnderRailUpInventory());
        helpInventoryMap.put(HelpInventoryType.Constructs_EnderRail_Down, makeConstructsEnderRailDownInventory());
    }

    private HelpInventory makeHelpInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Help");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Click on the various Icons to gain Info about them."
        });

        ConfigurationSection commandsSection = plugin.getConfig().getConfigurationSection("commands");

        if (commandsSection != null) {
            if (commandsSection.getBoolean("dealWithDevil")) {
                createSlot(inventory, Material.PAPER, 1, true, slot(1, 2), "/mx-cs dealWithDevil", new String[]{
                        "Type this Command when Stuck somewhere with no Resources."
                });
            }
        }

        createSlotLink(inventory, Material.CLOCK, 1, slot(2, 1), "Weathers", new String[]{
                "Fire, Ice and Lightning falling from the Sky."
        }, linkInventoryMap, HelpInventoryType.Weathers);
        createSlotLink(inventory, Material.TOTEM_OF_UNDYING, 1, slot(2, 3), "Aspects", new String[]{
                "What Influence do you have on the World...", "And what Influence does it have on you?"
        }, linkInventoryMap, HelpInventoryType.Aspects);
        createSlotLink(inventory, Material.FLINT_AND_STEEL, 1, slot(2, 5), "Rites", new String[]{
                "Trade Secrets, sacrifice Resources.", "Gain Blessings, gain Curses."
        }, linkInventoryMap, HelpInventoryType.Rites);
        createSlotLink(inventory, Material.RED_BED, 1, slot(2, 7), "Dreams", new String[]{
                "In Dreams various Insights can be acquired."
        }, linkInventoryMap, HelpInventoryType.Dreams);
        createSlotLink(inventory, Material.BRICKS, 1, slot(2, 9), "Constructs", new String[]{
                "Altars and Machines can both produce alike."
        }, linkInventoryMap, HelpInventoryType.Constructs);

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeWeathersInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Weathers");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Help);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Special Weathers can happen at random Times, lasting a random Duration."
        });
        createSlot(inventory, Constants.TimeDistortionTheme.getMaterial(), 1, slot(2, 2), "Time Distortion", new String[]{
                "Time shifts forward or backward."
        });
        createSlot(inventory, Constants.WeatherThemes.get(WeatherCatastropheType.RadiantSky).getMaterial(), 1, slot(2, 3), "Radiant Sky", new String[]{
                "Hot Rays pierce the Sky.", "Seek Shelter, or be burned and blinded.", "No Danger at Night."
        });
        createSlot(inventory, Constants.WeatherThemes.get(WeatherCatastropheType.SearingCold).getMaterial(), 1, slot(2, 4), "Searing Cold", new String[]{
                "It is freezing Cold.", "Stay near Fire or other Sources of Heat.", "Less Danger at Day."
        });
        createSlot(inventory, Constants.WeatherThemes.get(WeatherCatastropheType.ThunderStorm).getMaterial(), 1, slot(2, 5), "Thunderstorm", new String[]{
                "Lightning strikes, a lot of it.", "Seek Shelter, or be hit.", "Misfortune attracts Lightning."
        });
        createSlot(inventory, Constants.WeatherThemes.get(WeatherCatastropheType.GravityLoss).getMaterial(), 1, slot(2, 6), "Gravity Loss", new String[]{
                "The Laws of Gravity are temporarily broken.", "Fly up high and land soft."
        });
        createSlot(inventory, Constants.WeatherThemes.get(WeatherCatastropheType.CatsAndDogs).getMaterial(), 1, slot(2, 7), "Cats and Dogs", new String[]{
                "It is raining Cats and Dogs."
        });
        createSlot(inventory, Constants.WeatherThemes.get(WeatherCatastropheType.PersonaShift).getMaterial(), 1, slot(2, 8), "Persona Shift", new String[]{
                "Players may switch positions at any moment."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeAspectsInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Aspects");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Help);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Certain Events or Actions cause certain Aspects.", "Certain Aspects cause certain Things to happen."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Secrets).getMaterial(), 1, slot(2, 1), "Secrets", new String[]{
                "Can be used for certain Rites or other Things.", "Gained by certain Rites.", "Gained by periodic horrific Whispers."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Terror).getMaterial(), 1, slot(2, 2), "Terror", new String[]{
                "Causes various terrible Things to occur.", "Causes more Secrets from horrific Whispers.", "Gained by periodic horrific Whispers."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Nobility).getMaterial(), 1, slot(2, 3), "Nobility", new String[]{
                "Causes various good Things to occur.", "Gained by completing special Achievements.", "Only available with MixedAchievements Plugin."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Nature_Conspiracy).getMaterial(), 1, slot(2, 4), "Nature Conspiracy", new String[]{
                "Causes Killer Rabbits", "Causes your Food to eat you.", "Sometimes gained by breaking Logs.", "Sometimes gained by killing Animals."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Celestial_Favor).getMaterial(), 1, slot(2, 5), "Celestial Favor", new String[]{
                "Saves you from falling into the Void", "Saves your Inventory on Death.", "Gained by conducting a certain Rite."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Death_Seeker).getMaterial(), 1, slot(2, 6), "Death Seeker", new String[]{
                "Causes Stalkers to appear.", "Gained by dying."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Misfortune).getMaterial(), 1, slot(2, 7), "Misfortune", new String[]{
                "Causes you to miss Attacks.", "Causes Lightning to hit you.", "Sometimes gained by breaking Glass.", "Sometimes gained by breaking Stone."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Greyhat_Debt).getMaterial(), 1, slot(2, 8), "Greyhat Debt", new String[]{
                "Causes Dreams to be confiscated.", "Gained by making a certain Deal."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Resolve).getMaterial(), 1, slot(2, 9), "Resolve", new String[]{
                "Can cause critical Hits and reduces Damage taken.", "Chance to gain on Terror Event.", "Slowly declining."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.SkyScorn).getMaterial(), 1, slot(3, 1), "Sky Scorn", new String[]{
                "May cause bad Things when using Elytra Boost.", "Gained when using Elytra Boost.", "Slowly declining."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeRitesInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Rites");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Help);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "The bottom Block determines the Rite Type.", "The top Block determines the Modifier.", "Light a Fire on the top to execute the Rite."
        });
        createSlotLink(inventory, Material.GLASS, 1, slot(2, 3), "Resource Production (Glass)", new String[]{
                "Rite Type (Bottom Block).", "Trade Secrets for Materials."
        }, linkInventoryMap, HelpInventoryType.Rites_Production);
        createSlotLink(inventory, Material.STONE_BRICKS, 1, slot(2, 4), "Resource Sacrifice (Stone Bricks)", new String[]{
                "Rite Type (Bottom Block).", "Sacrifice Materials for Secrets."
        }, linkInventoryMap, HelpInventoryType.Rites_Sacrifice);
        createSlotLink(inventory, Material.QUARTZ_BLOCK, 1, slot(2, 5), "Curse Removal (Quartz)", new String[]{
                "Rite Type (Bottom Block).", "Use Secrets to remove a Curse."
        }, linkInventoryMap, HelpInventoryType.Rites_Curse);
        createSlotLink(inventory, Material.GLOWSTONE, 1, slot(2, 6), "Blessing Invocation (Glowstone)", new String[]{
                "Rite Type (Bottom Block).", "Use Secrets to gain a Blessing."
        }, linkInventoryMap, HelpInventoryType.Rites_Blessing);
        createSlotLink(inventory, Material.HAY_BLOCK, 1, slot(2, 7), "Ultimatum (Hay)", new String[]{
                "Rite Type (Bottom Block).", "Use Secrets to perform an Ultimatum."
        }, linkInventoryMap, HelpInventoryType.Rites_Ultimatum);

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeRitesProductionInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Rite Production");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Rites);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "The bottom Block will be replaced by the top.", "You will loose a certain Amount of Secrets."
        });
        createSlot(inventory, Material.QUARTZ_BLOCK, 1, slot(2, 1), "Quartz", new String[]{
                "Modifier (Top Block).", "Secrets -40."
        });
        createSlot(inventory, Material.COAL_BLOCK, 1, slot(2, 2), "Coal", new String[]{
                "Modifier (Top Block).", "Secrets -80."
        });
        createSlot(inventory, Material.IRON_BLOCK, 1, slot(2, 3), "Iron", new String[]{
                "Modifier (Top Block).", "Secrets -400."
        });
        createSlot(inventory, Material.LAPIS_BLOCK, 1, slot(2, 4), "Lapis Lazuli", new String[]{
                "Modifier (Top Block).", "Secrets -400."
        });
        createSlot(inventory, Material.REDSTONE_BLOCK, 1, slot(2, 5), "Redstone", new String[]{
                "Modifier (Top Block).", "Secrets -400."
        });
        createSlot(inventory, Material.GOLD_BLOCK, 1, slot(2, 6), "Gold", new String[]{
                "Modifier (Top Block).", "Secrets -800."
        });
        createSlot(inventory, Material.EMERALD_BLOCK, 1, slot(2, 7), "Emerald", new String[]{
                "Modifier (Top Block).", "Secrets -3200."
        });
        createSlot(inventory, Material.DIAMOND_BLOCK, 1, slot(2, 8), "Diamond", new String[]{
                "Modifier (Top Block).", "Secrets -6400."
        });
        createSlot(inventory, Material.NETHERITE_BLOCK, 1, slot(2, 9), "Netherite", new String[]{
                "Modifier (Top Block).", "Secrets -6400."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeRitesSacrificeInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Rite Sacrifice");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Rites);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "The Resources will be lost.", "You will gain a certain Amount of Secrets."
        });
        createSlot(inventory, Material.QUARTZ_BLOCK, 1, slot(2, 2), "Quartz", new String[]{
                "Modifier (Top Block).", "Secrets +20."
        });
        createSlot(inventory, Material.IRON_BLOCK, 1, slot(2, 3), "Iron", new String[]{
                "Modifier (Top Block).", "Secrets +200."
        });
        createSlot(inventory, Material.LAPIS_BLOCK, 1, slot(2, 4), "Lapis Lazuli", new String[]{
                "Modifier (Top Block).", "Secrets +200."
        });
        createSlot(inventory, Material.REDSTONE_BLOCK, 1, slot(2, 5), "Redstone", new String[]{
                "Modifier (Top Block).", "Secrets +200."
        });
        createSlot(inventory, Material.GOLD_BLOCK, 1, slot(2, 6), "Gold", new String[]{
                "Modifier (Top Block).", "Secrets +400."
        });
        createSlot(inventory, Material.EMERALD_BLOCK, 1, slot(2, 7), "Emerald", new String[]{
                "Modifier (Top Block).", "Secrets +1600."
        });
        createSlot(inventory, Material.DIAMOND_BLOCK, 1, slot(2, 8), "Diamond", new String[]{
                "Modifier (Top Block).", "Secrets +3200."
        });
        createSlot(inventory, Material.NETHERITE_BLOCK, 1, slot(2, 8), "Netherite", new String[]{
                "Modifier (Top Block).", "Secrets +3200."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeRitesCurseInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Rite Curse");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Rites);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "The Resources and Secrets will be lost.", "You will lose dangerous Aspects."
        });
        createSlot(inventory, Material.SOUL_SAND, 1, slot(2, 3), "Remove Nature Conspiracy (Soul Sand)", new String[]{
                "Modifier (Top Block).", "Secrets -1000.", "Nature Conspiracy - (3 + 35%)."
        });
        createSlot(inventory, Material.BOOKSHELF, 1, slot(2, 5), "Remove Misfortune (Bookshelf)", new String[]{
                "Modifier (Top Block).", "Secrets -1000.", "Misfortune - (3 + 35%)."
        });
        createSlot(inventory, Material.TERRACOTTA, 1, slot(2, 7), "Remove Terror (Terracotta)", new String[]{
                "Modifier (Top Block).", "Secrets -500.", "Terror - (3 + 35%)."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeRitesBlessingInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Rite Blessing");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Rites);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "The Resources and Secrets will be lost.", "You will gain certain Aspects."
        });
        createSlot(inventory, Material.LAPIS_BLOCK, 1, slot(2, 3), "Celestial Favor (Lapis Lazuli)", new String[]{
                "Modifier (Top Block).", "Secrets -750.", "Celestial Favor +1."
        });
        createSlot(inventory, Material.BOOKSHELF, 1, slot(2, 5), "Terror (Bookshelf)", new String[]{
                "Modifier (Top Block).", "Secrets - (50 + Terror).", "Terror +50."
        });
        createSlot(inventory, Material.COAL_BLOCK, 1, slot(2, 7), "Resolve (Coal)", new String[]{
                "Modifier (Top Block).", "Costs Secrets dependent on Resolve Trigger.", "Trigger Resolve."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeRitesUltimatumInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Rite Ultimatum");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Rites);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "The Resources and Secrets will be lost.", "You will perform something extraordinary."
        });
        createSlot(inventory, Material.SOUL_SAND, 1, slot(2, 5), "Initiate Void (Soul Sand)", new String[]{
                "Modifier (Top Block).", "Secrets - (1000 + 100 per Death Seeker).", "Loose all Aspects.", "Loose all Mixed Achievements.", "Loose Inventory.", "Teleport to random Location."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private int slot(int row, int col) {
        return (row - 1) * 9 + (col - 1);
    }

    private HelpInventory makeDreamsInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Dreams");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Help);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Click on a Bed to Dream.", "Blocks next to the Bed can cause certain Dreams.", "After dreaming, wait a certain Time to dream again."
        });
        createSlot(inventory, Constants.AspectThemes.get(AspectType.Greyhat_Debt).getMaterial(), 1, slot(2, 2), "Greyhat", new String[]{
                "Cancels the Dream.", "Chance to occur scales with Greyhat Debt.", "10 Minute Cooldown."
        });
        createSlot(inventory, Constants.DreamThemes.get(DreamType.SereneDreams).getMaterial(), 1, slot(2, 3), "Serene Dreams (Default)", new String[]{
                "Slight Terror Loss.", "Secrets +10", "10 Minute Cooldown."
        });
        createSlot(inventory, Constants.DreamThemes.get(DreamType.SkyDreams).getMaterial(), 1, slot(2, 4), "Sky Dreams (Lapis Lazuli)", new String[]{
                "Moderate Terror Loss.", "Slight Sky Scorn Loss.", "Moderate Secret Loss.", "10 Minute Cooldown."
        });
        createSlot(inventory, Constants.DreamThemes.get(DreamType.BloodstainedDreams).getMaterial(), 1, slot(2, 5), "Bloodstained Dreams (Redstone)", new String[]{
                "Slight Terror Gain.", "Moderate Secret Gain.", "10 Minute Cooldown.", "10 Minutes of damage while near a Lighthouse."
        });
        createSlot(inventory, Constants.DreamThemes.get(DreamType.ClockworkDreams).getMaterial(), 1, slot(2, 6), "Clockwork Dreams (Iron)", new String[]{
                "Secrets -30", "Speed 1, Haste 1, Strength 1, Resistance 1", "10 Minute Cooldown."
        });
        createSlot(inventory, Constants.DreamThemes.get(DreamType.GloryDreams).getMaterial(), 1, slot(2, 7), "Glory Dreams (Gold)", new String[]{
                "Secrets -120", "Speed 2, Haste 2, Strength 2, Resistance 2", "10 Minute Cooldown."
        });
        createSlot(inventory, Constants.DreamThemes.get(DreamType.PerfectDreams).getMaterial(), 1, slot(2, 8), "Perfect Dreams (Diamond)", new String[]{
                "Secrets -480", "Speed 3, Haste 3, Strength 3, Resistance 3", "10 Minute Cooldown."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Constructs");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Help);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Place Blocks in certain Configurations."
        });
        createSlotLink(inventory, Constants.ConstructThemes.get(ConstructType.GreenWell).getMaterial(), 1, slot(2, 2), "Green Well", new String[]{
                "Regularly drops Logs of a chosen Kind.", "Spawns Flowers.", "Converts Dirt To Grass."
        }, linkInventoryMap, HelpInventoryType.Constructs_GreenWell);
        createSlotLink(inventory, Constants.ConstructThemes.get(ConstructType.BlazeReactor).getMaterial(), 1, slot(2, 3), "Blaze Reactor", new String[]{
                "Regularly drops Cobblestone.", "Drains Water from Cauldrons tofunction."
        }, linkInventoryMap, HelpInventoryType.Constructs_BlazeReactor);
        createSlotLink(inventory, Constants.ConstructThemes.get(ConstructType.Blitzard).getMaterial(), 1, slot(2, 4), "Blitzard", new String[]{
                "Attracts Lightning."
        }, linkInventoryMap, HelpInventoryType.Constructs_Blitzard);
        createSlotLink(inventory, Constants.ConstructThemes.get(ConstructType.Lighthouse).getMaterial(), 1, slot(2, 5), "Lighthouse", new String[]{
                "Protects against any Terror Event."
        }, linkInventoryMap, HelpInventoryType.Constructs_Lighthouse);
        createSlotLink(inventory, Constants.ConstructThemes.get(ConstructType.Scarecrow).getMaterial(), 1, slot(2, 6), "Scarecrow", new String[]{
                "Increases Terror and Secrets from Horrific Whispers."
        }, linkInventoryMap, HelpInventoryType.Constructs_Scarecrow);
        createSlotLink(inventory, Constants.ConstructThemes.get(ConstructType.EnderRail).getMaterial(), 1, slot(2, 7), "Ender Rail", new String[]{
                "Teleport between Stations."
        }, linkInventoryMap, HelpInventoryType.Constructs_EnderRail);

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsGreenWellInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Green Well");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration.", "Use Wheat Seeds to Level up.", "Spawns Flowers when possible.", "Spawns Logs on Flowers spawn."
        });
        createSlot(inventory, Material.WHEAT_SEEDS, 1, true, slot(2, 2), "Wheat Seed", new String[]{
                "Click on the Water with Wheat Seeds.", "Costs Wheat Seeds and Secrets."
        });
        for (int row = 1; row <= 3; row++) {
            for (int col = 4; col <= 6; col++) {
                createSlot(inventory, Material.OAK_LOG, 1, slot(row, col), "Log", new String[]{
                        "Can be any Log."
                });
            }
        }
        createSlot(inventory, Material.WATER_BUCKET, 1, slot(2, 5), "Water", new String[]{
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsBlitzardInventory() {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, "Blitzard");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration.", "Use Quartz to Level up.", "Attracts Lightning within Range."
        });
        createSlot(inventory, Material.QUARTZ, 1, true, slot(5, 2), "Quartz", new String[]{
                "Click on the Quartz Block with Quartz.", "Costs Quartz and Secrets."
        });
        createSlot(inventory, Material.QUARTZ_BLOCK, 1, slot(5, 5), "Quartz Block", new String[]{
        });
        createSlot(inventory, Material.IRON_BARS, 1, slot(4, 5), "Iron Bars", new String[]{
        });
        createSlot(inventory, Material.IRON_BARS, 1, slot(3, 5), "Iron Bars", new String[]{
        });
        createSlot(inventory, Material.IRON_BARS, 1, slot(2, 5), "Iron Bars", new String[]{
        });
        createSlot(inventory, Material.IRON_BARS, 1, slot(1, 5), "Iron Bars", new String[]{
        });
        createSlot(inventory, Material.IRON_BARS, 2, slot(2, 4), "Iron Bars", new String[]{
                FrontBackSymmetryText
        });
        createSlot(inventory, Material.IRON_BARS, 2, slot(2, 6), "Iron Bars", new String[]{
                FrontBackSymmetryText
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsLighthouseInventory() {
        Inventory inventory = Bukkit.createInventory(null, 5 * 9, "Lighthouse");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration.", "Use Glowstone Dust to Level up.", "No Terror Events within Range."
        });
        createSlot(inventory, Material.GLOWSTONE_DUST, 1, true, slot(5, 2), "Glowstone Dust", new String[]{
                "Click on the Glowstone Block with Glowstone Dust.", "Costs Glowstone Dust and Secrets."
        });
        createSlot(inventory, Material.GLOWSTONE, 1, slot(5, 5), "Glowstone Block", new String[]{
        });
        createSlot(inventory, Material.STONE_BRICK_WALL, 1, slot(4, 5), "Stone Brick Wall", new String[]{
        });
        createSlot(inventory, Material.STONE_BRICK_WALL, 1, slot(3, 5), "Stone Brick Wall", new String[]{
        });
        createSlot(inventory, Material.STONE_BRICK_WALL, 1, slot(2, 5), "Stone Brick Wall", new String[]{
        });
        createSlot(inventory, Material.STONE_BRICKS, 1, slot(1, 5), "Stone Bricks", new String[]{
        });
        createSlot(inventory, Material.STONE_BRICK_STAIRS, 2, slot(1, 4), "Stone Brick Stairs", new String[]{
                FrontBackSymmetryText
        });
        createSlot(inventory, Material.STONE_BRICK_STAIRS, 2, slot(1, 6), "Stone Brick Stairs", new String[]{
                FrontBackSymmetryText
        });
        createSlot(inventory, Material.LANTERN, 2, slot(2, 4), "Lantern", new String[]{
                FrontBackSymmetryText, "Can be any Lantern."
        });
        createSlot(inventory, Material.LANTERN, 2, slot(2, 6), "Lantern", new String[]{
                FrontBackSymmetryText, "Can be any Lantern."
        });

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsBlazeReactorInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Blaze Reactor");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration.", "Use Magma Cream to Level up.", "Fill Cauldrons with Water regularly.", "Drops Cobblestone."
        });
        createSlot(inventory, Material.PAPER, 1, true, slot(1, 3), "Layer 1", new String[]{});
        createSlot(inventory, Material.PAPER, 1, true, slot(1, 7), "Layer 2", new String[]{});
        createSlot(inventory, Material.MAGMA_CREAM, 1, true, slot(1, 5), "Magma Cream", new String[]{
                "Click on the Lava with Magma Cream.", "Costs Magma Cream and Secrets."
        });

        for (int row = 2; row <= 3; row++) {
            for (int col = 2; col <= 4; col++) {
                createSlot(inventory, Material.CAULDRON, 1, slot(row, col), "Cauldron", new String[]{});
            }
        }

        createSlot(inventory, Material.AIR, 0, slot(3, 3), "", new String[]{});

        for (int row = 4; row <= 6; row++) {
            for (int col = 2; col <= 4; col++) {
                createSlot(inventory, Material.BRICKS, 1, slot(row, col), "Bricks", new String[]{});
            }
        }

        createSlot(inventory, Material.AIR, 0, slot(6, 3), "", new String[]{});


        for (int row = 4; row <= 6; row++) {
            for (int col = 6; col <= 8; col++) {
                createSlot(inventory, Material.BRICKS, 1, slot(row, col), "Bricks", new String[]{});
            }
        }

        createSlot(inventory, Material.MAGMA_BLOCK, 1, slot(4, 6), "Magma Block", new String[]{});
        createSlot(inventory, Material.AIR, 0, slot(4, 7), "", new String[]{});
        createSlot(inventory, Material.MAGMA_BLOCK, 1, slot(4, 8), "Magma Block", new String[]{});
        createSlot(inventory, Material.LAVA_BUCKET, 1, slot(5, 7), "Lava", new String[]{});

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsScarecrowInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Scarecrow");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration.", "Use Pumpkin Pie to activate.", "More Secrets and Terror from Horrific Whispers."
        });
        createSlot(inventory, Material.PUMPKIN_PIE, 1, true, slot(1, 5), "Pumpkin Pie", new String[]{
                "Click on the Pumpkin with Pumpkin Pie.", "Costs Pumpkin Pie and Secrets."
        });
        createSlot(inventory, Material.JACK_O_LANTERN, 1, slot(2, 5), "Jack o' Lantern", new String[]{});
        createSlot(inventory, Material.TORCH, 1, slot(2, 3), "", new String[]{
                "Can be any Torch."
        });
        createSlot(inventory, Material.TORCH, 1, slot(2, 7), "", new String[]{
                "Can be any Torch."
        });

        for (int col = 3; col <= 7; col++) {
            createSlot(inventory, Material.OAK_FENCE, 1, slot(3, col), "Fence", new String[]{
                    "Can be any Fence."
            });
        }

        createSlot(inventory, Material.HAY_BLOCK, 1, slot(3, 5), "Hay Block", new String[]{});

        for (int col = 3; col <= 7; col++) {
            createSlot(inventory, Material.CHAIN, 1, slot(4, col), "Chain", new String[]{});
        }

        createSlot(inventory, Material.OAK_FENCE, 1, slot(4, 5), "Fence", new String[]{
                "Can be any Fence."
        });
        createSlot(inventory, Material.CHAIN, 1, slot(5, 3), "Chain", new String[]{});
        createSlot(inventory, Material.OAK_FENCE, 1, slot(5, 5), "Fence", new String[]{
                "Can be any Fence."
        });
        createSlot(inventory, Material.CHAIN, 1, slot(5, 7), "Chain", new String[]{});

        createSlot(inventory, Material.SOUL_SAND, 1, slot(6, 5), "Soul Sand", new String[]{});

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsEnderRailInventory() {
        Inventory inventory = Bukkit.createInventory(null, 3 * 9, "Ender Rail");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct Stations on the same Axis, pointing at each other.", "Use Ender Eyes to activate.", "Step on the Pressure Plate to teleport."
        });

        createSlot(inventory, Material.ENDER_EYE, 1, true, slot(1, 5), "Ender Eye", new String[]{
                "Click on the Lapis Lazuli Block with Ender Eye.", "Costs Ender Eyes and Secrets."
        });
        createSlotLink(inventory, Material.RAIL, 1, true, slot(2, 3), "Side", new String[]{
                "The Station, that points sideways."
        }, linkInventoryMap, HelpInventoryType.Constructs_EnderRail_Side);
        createSlotLink(inventory, Material.RAIL, 1, true, slot(2, 5), "Up", new String[]{
                "The Station, that points up."
        }, linkInventoryMap, HelpInventoryType.Constructs_EnderRail_Up);
        createSlotLink(inventory, Material.RAIL, 1, true, slot(2, 7), "Down", new String[]{
                "The Station, that points down."
        }, linkInventoryMap, HelpInventoryType.Constructs_EnderRail_Down);

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsEnderRailSideInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Ender Rail Side");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs_EnderRail);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration."
        });

        createSlot(inventory, Material.PAPER, 1, true, slot(1, 3), "Layer 1", new String[]{});
        createSlot(inventory, Material.PAPER, 1, true, slot(1, 7), "Layer 2", new String[]{});

        HashMap<Coordinate3D, Material> materialMap = Constants.EnderRail_Side.getMaterialMap();
        for (Coordinate3D c3d : materialMap.keySet()) {
            Material material = materialMap.get(c3d);
            createSlot(inventory, material, 1, slot(-c3d.getXRound() + 3, c3d.getZRound() + c3d.getYRound() * 4 + 3), material.name(), new String[]{});
        }

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsEnderRailUpInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Ender Rail Up");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs_EnderRail);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration."
        });

        createSlot(inventory, Material.PAPER, 1, true, slot(1, 3), "Layer 1", new String[]{});
        createSlot(inventory, Material.PAPER, 1, true, slot(1, 7), "Layer 2", new String[]{});

        HashMap<Coordinate3D, Material> materialMap = Constants.EnderRail_Up.getMaterialMap();
        for (Coordinate3D c3d : materialMap.keySet()) {
            Material material = materialMap.get(c3d);
            createSlot(inventory, material, 1, slot(-c3d.getXRound() + 4, c3d.getZRound() + c3d.getYRound() * 4 + 3), material.name(), new String[]{});
        }

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private HelpInventory makeConstructsEnderRailDownInventory() {
        Inventory inventory = Bukkit.createInventory(null, 6 * 9, "Ender Rail Down");
        HashMap<Integer, HelpInventoryType> linkInventoryMap = new HashMap<>();

        createSlotLink(inventory, Material.ARROW, 1, slot(1, 9), "Back", new String[]{}
                , linkInventoryMap, HelpInventoryType.Constructs_EnderRail);
        createSlot(inventory, Material.BOOK, 1, slot(1, 1), "Information", new String[]{
                "Construct in the following Configuration."
        });

        createSlot(inventory, Material.PAPER, 1, true, slot(1, 3), "Layer 1", new String[]{});
        createSlot(inventory, Material.PAPER, 1, true, slot(1, 7), "Layer 2", new String[]{});

        HashMap<Coordinate3D, Material> materialMap = Constants.EnderRail_Down.getMaterialMap();
        for (Coordinate3D c3d : materialMap.keySet()) {
            Material material = materialMap.get(c3d);
            createSlot(inventory, material, 1, slot(-c3d.getXRound() + 4, c3d.getZRound() + c3d.getYRound() * 4 + 3), material.name(), new String[]{});
        }

        return new HelpInventory(inventory, linkInventoryMap);
    }

    private void createSlot(Inventory inventory, Material material, int amount, int slot, String name, String[] lores) {
        createSlot(inventory, material, amount, false, slot, name, lores, false);
    }

    private void createSlot(Inventory inventory, Material material, int amount, boolean glowing, int slot, String name, String[] lores) {
        createSlot(inventory, material, amount, glowing, slot, name, lores, false);
    }

    private void createSlot(Inventory inventory, Material material, int amount, int slot, String name, String[] lores, boolean clickable) {
        createSlot(inventory, material, amount, false, slot, name, lores, clickable);
    }

    private void createSlot(Inventory inventory, Material material, int amount, boolean glowing, int slot, String name, String[] lores, boolean clickable) {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();

        if (meta != null) {
            meta.setDisplayName(ChatColor.GOLD + name);

            if (glowing) {
                meta.addEnchant(Enchantment.LOYALTY, 1, true);
                meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            }

            ArrayList<String> Lore = new ArrayList<>();

            if (clickable) {
                Lore.add(ChatColor.LIGHT_PURPLE + "" + ChatColor.ITALIC + "Clickable");
            }

            for (String l : lores) {
                Lore.add(ChatColor.WHITE + l);
            }

            meta.setLore(Lore);
            item.setItemMeta(meta);
        }

        inventory.setItem(slot, item);
    }

    private void createSlotLink(Inventory inventory, Material material, int amount, int slot, String name, String[] lores, HashMap<Integer, HelpInventoryType> linkInventoryMap, HelpInventoryType helpInventoryType) {
        createSlotLink(inventory, material, amount, false, slot, name, lores, linkInventoryMap, helpInventoryType);
    }

    private void createSlotLink(Inventory inventory, Material material, int amount, boolean glowing, int slot, String name, String[] lores, HashMap<Integer, HelpInventoryType> linkInventoryMap, HelpInventoryType helpInventoryType) {
        linkInventoryMap.put(slot, helpInventoryType);
        createSlot(inventory, material, amount, glowing, slot, name, lores, true);
    }
}
