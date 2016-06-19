package fr.scarex.elevator;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLInterModComms;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;
import fr.scarex.elevator.block.ElevatorBlocks;
import fr.scarex.elevator.block.ElevatorControllerBlock;
import fr.scarex.elevator.network.PacketControllerTeleportation;
import fr.scarex.elevator.network.PacketElevatorBlockAutoOpen;
import fr.scarex.elevator.network.PacketElevatorBlockAutoOpenRequest;
import fr.scarex.elevator.network.PacketElevatorBlockHotKey;
import fr.scarex.elevator.network.PacketElevatorBlockHotKeyRequest;
import fr.scarex.elevator.network.PacketElevatorBlockHotKeysPressed;
import fr.scarex.elevator.network.PacketElevatorBlockNameRequest;
import fr.scarex.elevator.network.PacketElevatorBlockNameToPlayer;
import fr.scarex.elevator.network.PacketElevatorBlockNameToServer;
import fr.scarex.elevator.network.PacketPlayerList;
import fr.scarex.elevator.network.PacketPlayerListAddPlayer;
import fr.scarex.elevator.network.PacketPlayerListRemovePlayer;
import fr.scarex.elevator.network.PacketPlayerListRequest;
import fr.scarex.elevator.network.PacketPlayerListWhitelist;
import fr.scarex.elevator.network.PacketPlayerListWhitelistRequest;
import fr.scarex.elevator.network.PacketTeleportationList;
import fr.scarex.elevator.network.PacketTeleportationListRequest;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;

/**
 * @author SCAREX
 *
 */
@Mod(modid = Elevator.MODID, name = Elevator.NAME, version = Elevator.VERSION, dependencies = Elevator.DEPENDENCIES)
public class Elevator
{
    public static final String MODID = "elevator";
    public static final String NAME = "Elevator Mod";
    public static final String VERSION = "@MOD_VERSION@";
    public static final boolean DEBUG = "@DEBUG@" == "@" + "DEBUG@";
    public static final String DEPENDENCIES = Elevator.DEBUG ? "" : "required-after:CoFHCore@[" + "@MC_VERSION@" + "R" + "@COFHCORE_VERSION_MAJOR@" + ",);required-after:ThermalExpansion@[" + "@MC_VERSION@" + "R" + "@TE_VERSION_MAJOR@" + ",);";

    @SidedProxy(serverSide = "fr.scarex.elevator.CommonProxy", clientSide = "fr.scarex.elevator.client.ClientProxy")
    public static CommonProxy PROXY;
    @Mod.Instance(Elevator.MODID)
    public static Elevator INSTANCE;

    public static Logger LOG;

    public static SimpleNetworkWrapper NETWORK;

    public static final CreativeTabs CREATIVE_TAB = new CreativeTabs(Elevator.MODID) {
        @Override
        public Item getTabIconItem() {
            return Item.getItemFromBlock(ElevatorBlocks.blockMap.get(ElevatorControllerBlock.class));
        }
    };

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOG = event.getModLog();

        ElevatorBlocks.preInit();

        FMLInterModComms.sendMessage("Waila", "register", "fr.scarex.elevator.waila.ElevatorWailaCompat.load");

        NETWORK = NetworkRegistry.INSTANCE.newSimpleChannel(Elevator.MODID);
        int discri = 0;
        NETWORK.registerMessage(PacketControllerTeleportation.Handler.class, PacketControllerTeleportation.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketTeleportationListRequest.Handler.class, PacketTeleportationListRequest.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketTeleportationList.Handler.class, PacketTeleportationList.class, discri++, Side.CLIENT);
        NETWORK.registerMessage(PacketElevatorBlockNameToServer.Handler.class, PacketElevatorBlockNameToServer.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketElevatorBlockNameRequest.Handler.class, PacketElevatorBlockNameRequest.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketElevatorBlockNameToPlayer.Handler.class, PacketElevatorBlockNameToPlayer.class, discri++, Side.CLIENT);
        NETWORK.registerMessage(PacketElevatorBlockAutoOpenRequest.Handler.class, PacketElevatorBlockAutoOpenRequest.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketElevatorBlockAutoOpen.ServerHandler.class, PacketElevatorBlockAutoOpen.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketElevatorBlockAutoOpen.ClientHandler.class, PacketElevatorBlockAutoOpen.class, discri++, Side.CLIENT);
        NETWORK.registerMessage(PacketPlayerListAddPlayer.Handler.class, PacketPlayerListAddPlayer.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketPlayerListRequest.Handler.class, PacketPlayerListRequest.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketPlayerList.Handler.class, PacketPlayerList.class, discri++, Side.CLIENT);
        NETWORK.registerMessage(PacketPlayerListRemovePlayer.Handler.class, PacketPlayerListRemovePlayer.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketPlayerListWhitelistRequest.Handler.class, PacketPlayerListWhitelistRequest.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketPlayerListWhitelist.ClientHandler.class, PacketPlayerListWhitelist.class, discri++, Side.CLIENT);
        NETWORK.registerMessage(PacketPlayerListWhitelist.ServerHandler.class, PacketPlayerListWhitelist.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketElevatorBlockHotKeyRequest.Handler.class, PacketElevatorBlockHotKeyRequest.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketElevatorBlockHotKey.ClientHandler.class, PacketElevatorBlockHotKey.class, discri++, Side.CLIENT);
        NETWORK.registerMessage(PacketElevatorBlockHotKey.ServerHandler.class, PacketElevatorBlockHotKey.class, discri++, Side.SERVER);
        NETWORK.registerMessage(PacketElevatorBlockHotKeysPressed.Handler.class, PacketElevatorBlockHotKeysPressed.class, discri++, Side.SERVER);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        ElevatorBlocks.init();

        PROXY.registerRender();
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        ElevatorBlocks.postInit();

        NetworkRegistry.INSTANCE.registerGuiHandler(this, Elevator.PROXY);

        PROXY.registerHandlers();
    }

    public static String getTranslation(String unlocalizedName, Object ... params) {
        return StatCollector.translateToLocalFormatted(Elevator.MODID.toLowerCase() + "." + unlocalizedName, params);
    }
}
