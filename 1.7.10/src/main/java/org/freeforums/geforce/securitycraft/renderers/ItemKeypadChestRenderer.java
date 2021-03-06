package org.freeforums.geforce.securitycraft.renderers;

import net.minecraft.client.model.ModelChest;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;

import org.freeforums.geforce.securitycraft.tileentity.TileEntityKeypadChest;

public class ItemKeypadChestRenderer implements IItemRenderer {

	private ModelChest chestModel;
	
	public ItemKeypadChestRenderer(){
		
		chestModel = new ModelChest();
		
	}
	
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		TileEntityRendererDispatcher.instance.renderTileEntityAt(new TileEntityKeypadChest(), 0.0D, 0.0D, 0.0D, 0.0F);

	}

}
