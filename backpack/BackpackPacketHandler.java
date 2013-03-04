package backpack;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class BackpackPacketHandler implements IPacketHandler {
	/**
	 * Handles incoming packets.
	 * 
	 * @param manager
	 *            The network manager.
	 * @param packet
	 *            The incoming packet.
	 * @param player
	 *            The player who sends the packet.
	 */
	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		if(packet.channel.equals("BackpackRename")) {
			handlePacket(packet, (EntityPlayer) player);
		} else if(packet.channel.equals("OpenBackpack")) {
			EntityPlayer entityPlayer = (EntityPlayer) player;
			
			if(!entityPlayer.worldObj.isRemote) {
				ItemStack backpack = entityPlayer.inventory.armorInventory[2];
				((ItemBackpack)backpack.getItem()).setWeared(true);
				ItemBackpack.tryOpen(backpack, entityPlayer);
			}
		}
	}

	/**
	 * Handles the packet if it was in channel "BackpackRename"
	 * 
	 * @param packet
	 *            The packet which was send.
	 * @param entityPlayer
	 *            The player who sends the packet.
	 */
	private void handlePacket(Packet250CustomPayload packet, EntityPlayer entityPlayer) {
		// converts the byte array to a string and trims it
		String name = new String(packet.data).trim();

		if(entityPlayer.getCurrentEquippedItem() != null) {
			ItemStack is = entityPlayer.getCurrentEquippedItem();
			InventoryBackpack inv = new InventoryBackpack(entityPlayer, is, false);
			// set new name
			inv.setInvName(name);
			// save the new data
			inv.saveInventory();
		}
	}
}
