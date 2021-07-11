package com.cemi.rustie.handlers;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.packets.MessageCraftItem;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;

public class RustiePacketHandler {
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Rustie.MODID);
	private static int msgid = 0;
	
	private static enum Side
    {
        CLIENT, SERVER, BOTH;
    }
	
	public static void init() {
		registerMessage(MessageCraftItem.class, Side.SERVER);
	}
	
	@SuppressWarnings("rawtypes")
	private static void registerMessage(Class packet, Side side)
	{
        if (side != Side.CLIENT)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.SERVER);

        if (side != Side.SERVER)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.CLIENT);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
	private static void registerMessage(Class packet, net.minecraftforge.fml.relauncher.Side side) {
        INSTANCE.registerMessage(packet, packet, msgid++, side);
    }
}
