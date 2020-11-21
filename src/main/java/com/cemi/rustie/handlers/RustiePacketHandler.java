package com.cemi.rustie.handlers;

import com.cemi.rustie.Rustie;
import com.cemi.rustie.Packets.MessageCraftItem;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

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
	
	private static void registerMessage(Class packet, Side side)
	{
        if (side != Side.CLIENT)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.SERVER);

        if (side != Side.SERVER)
            registerMessage(packet, net.minecraftforge.fml.relauncher.Side.CLIENT);
    }

    private static void registerMessage(Class packet, net.minecraftforge.fml.relauncher.Side side) {
        INSTANCE.registerMessage(packet, packet, msgid++, side);
    }
}
