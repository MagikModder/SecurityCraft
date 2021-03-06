package org.freeforums.geforce.securitycraft.handlers;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;

import org.freeforums.geforce.securitycraft.blocks.BlockLaserBlock;
import org.freeforums.geforce.securitycraft.blocks.BlockOwnable;
import org.freeforums.geforce.securitycraft.entity.EntitySecurityCamera;
import org.freeforums.geforce.securitycraft.interfaces.IOwnable;
import org.freeforums.geforce.securitycraft.items.ItemModule;
import org.freeforums.geforce.securitycraft.main.HelpfulMethods;
import org.freeforums.geforce.securitycraft.main.mod_SecurityCraft;
import org.freeforums.geforce.securitycraft.network.packets.PacketCheckRetinalScanner;
import org.freeforums.geforce.securitycraft.tileentity.CustomizableSCTE;
import org.freeforums.geforce.securitycraft.tileentity.TileEntityOwnable;
import org.freeforums.geforce.securitycraft.tileentity.TileEntityPortableRadar;

import cpw.mods.fml.client.event.ConfigChangedEvent.OnConfigChangedEvent;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.RenderTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SuppressWarnings({"unused", "rawtypes"})
public class ForgeEventHandler {
	
	private int counter = 0;
	private int cooldownCounter = 0;
	
	@SubscribeEvent
	public void onPlayerLoggedIn(PlayerLoggedInEvent event){
		mod_SecurityCraft.instance.createIrcBot(event.player.getCommandSenderName());
		ChatComponentTranslation chatcomponenttranslation = new ChatComponentTranslation("Thanks for using SecurityCraft " + mod_SecurityCraft.getVersion() + "! Tip: " + getRandomTip(), new Object[0]);
    	
		if(mod_SecurityCraft.configHandler.sayThanksMessage){
			event.player.addChatComponentMessage(chatcomponenttranslation);	
		}
	}
	
	private String getRandomTip(){
    	Random random = new Random();
    	int randomInt = random.nextInt(3);
    	
    	if(randomInt == 0){
    		return "Join breakinbad.net, the official SecurityCraft server!";
    	}else if(randomInt == 1){
    		return "/sc recipe will display the recipe for SecurityCraft blocks/items.";
    	}else if(randomInt == 2){
    		return "/sc help will display help info for SecurityCraft blocks/items.";
    	}else{
    		return "";
    	}
    }
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerTick(PlayerTickEvent event){
		counter++;
		if(cooldownCounter > 0){
			cooldownCounter--;
		}
		
		if(counter >= 20){
			mod_SecurityCraft.network.sendToServer(new PacketCheckRetinalScanner(event.player.getCommandSenderName()));
			counter = 0;
		}
		
		if((event.player.ridingEntity == null || !(event.player.ridingEntity instanceof EntitySecurityCamera)) && mod_SecurityCraft.instance.hasUsePosition(event.player.getCommandSenderName())){
			HelpfulMethods.setPlayerPosition(event.player, mod_SecurityCraft.instance.getUsePosition(event.player.getCommandSenderName())[0], mod_SecurityCraft.instance.getUsePosition(event.player.getCommandSenderName())[1], mod_SecurityCraft.instance.getUsePosition(event.player.getCommandSenderName())[2]);
			mod_SecurityCraft.instance.removeUsePosition(event.player.getCommandSenderName());
		}
	}

	@SubscribeEvent
	public void onBucketUsed(FillBucketEvent event){
		ItemStack result = fillBucket(event.world, event.target);
		if(result == null){ return; }
		event.result = result;
		event.setResult(Result.ALLOW);
	}
	
	@SubscribeEvent
	public void onPlayerLoggedOut(PlayerLoggedOutEvent event){
		if(mod_SecurityCraft.configHandler.disconnectOnWorldClose && mod_SecurityCraft.instance.getIrcBot(event.player.getCommandSenderName()) != null){
			mod_SecurityCraft.instance.getIrcBot(event.player.getCommandSenderName()).disconnect();
			mod_SecurityCraft.instance.removeIrcBot(event.player.getCommandSenderName());
		}
			
	}
	
	@SubscribeEvent 
	public void onPlayerInteracted(PlayerInteractEvent event){
		if(!event.entityPlayer.worldObj.isRemote){	
			if(event.action == Action.RIGHT_CLICK_BLOCK &&  event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) != null && isCustomizableBlock(event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z), event.world.getTileEntity(event.x, event.y, event.z)) && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == mod_SecurityCraft.universalBlockModifier){
				event.setCanceled(true);
				
				if(((CustomizableSCTE) event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getOwnerUUID() != null && !((CustomizableSCTE) event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getOwnerUUID().matches(event.entityPlayer.getGameProfile().getId().toString())){
					HelpfulMethods.sendMessageToPlayer(event.entityPlayer, "I'm sorry, you can not customize this block. This block is owned by " + ((TileEntityOwnable) event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getOwnerName() + ".", EnumChatFormatting.RED);
					return;
				}
				
				event.entityPlayer.openGui(mod_SecurityCraft.instance, 100, event.entityPlayer.worldObj, event.x, event.y, event.z);	
				return;
			}
			
			if(event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) == mod_SecurityCraft.portableRadar && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == Items.name_tag && event.entityPlayer.getCurrentEquippedItem().hasDisplayName()){
				event.setCanceled(true);
				
				event.entityPlayer.getCurrentEquippedItem().stackSize--;
				
				((TileEntityPortableRadar) event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).setCustomName(event.entityPlayer.getCurrentEquippedItem().getDisplayName());
				return;
			}
			
			if(event.action == Action.RIGHT_CLICK_BLOCK && event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z) != null && isOwnableBlock(event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z), event.world.getTileEntity(event.x, event.y, event.z)) && event.entityPlayer.getCurrentEquippedItem() != null && event.entityPlayer.getCurrentEquippedItem().getItem() == mod_SecurityCraft.universalBlockRemover){
				event.setCanceled(true);
				
				if(((IOwnable) event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getOwnerUUID() != null && !((IOwnable) event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getOwnerUUID().matches(event.entityPlayer.getGameProfile().getId().toString())){
					HelpfulMethods.sendMessageToPlayer(event.entityPlayer, "I'm sorry, you can not remove this block. This block is owned by " + ((IOwnable) event.entityPlayer.worldObj.getTileEntity(event.x, event.y, event.z)).getOwnerName() + ".", EnumChatFormatting.RED);
					return;
				}

				if(event.entityPlayer.worldObj.getBlock(event.x, event.y, event.z) == mod_SecurityCraft.LaserBlock){
					event.entityPlayer.worldObj.func_147480_a(event.x, event.y, event.z, true);
					BlockLaserBlock.destroyAdjecentLasers(event.world, event.x, event.y, event.z);
					event.entityPlayer.getCurrentEquippedItem().damageItem(1, event.entityPlayer);
				}else{
					event.entityPlayer.worldObj.func_147480_a(event.x, event.y, event.z, true);
					event.entityPlayer.worldObj.removeTileEntity(event.x, event.y, event.z);
					event.entityPlayer.getCurrentEquippedItem().damageItem(1, event.entityPlayer);
				}
			}
		}
	}
	
	@SubscribeEvent
    public void onConfigChanged(OnConfigChangedEvent event) {
        if(event.modID.equals("securitycraft")){
        	mod_SecurityCraft.configFile.save();
        }
    }
	
	@SubscribeEvent
	public void onBlockBroken(BreakEvent event){
		if(!event.world.isRemote){
			if(event.world.getTileEntity(event.x, event.y, event.z) != null && event.world.getTileEntity(event.x, event.y, event.z) instanceof CustomizableSCTE){
				for(int i = 0; i < ((CustomizableSCTE) event.world.getTileEntity(event.x, event.y, event.z)).getNumberOfCustomizableOptions(); i++){
					if(((CustomizableSCTE) event.world.getTileEntity(event.x, event.y, event.z)).itemStacks[i] != null){
						ItemStack stack = ((CustomizableSCTE) event.world.getTileEntity(event.x, event.y, event.z)).itemStacks[i];
						EntityItem item = new EntityItem(event.world, (double) event.x, (double) event.y, (double) event.z, stack);
						event.world.spawnEntityInWorld(item);
						
						((CustomizableSCTE) event.world.getTileEntity(event.x, event.y, event.z)).onModuleRemoved(stack, ((ItemModule) stack.getItem()).getModule());
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event){
		if(event.entity instanceof EntityPlayer){
			event.entity.registerExtendedProperties("SecurityCraftHandler", new PlayerExtendedPropertyHandler());
		}
	}

	private ItemStack fillBucket(World world, MovingObjectPosition position){
		Block block = world.getBlock(position.blockX, position.blockY, position.blockZ);
		
		if(block == mod_SecurityCraft.bogusWater){
			world.setBlockToAir(position.blockX, position.blockY, position.blockZ);
			return new ItemStack(mod_SecurityCraft.fWaterBucket, 1);
		}else if(block == mod_SecurityCraft.bogusLava){
			world.setBlockToAir(position.blockX, position.blockY, position.blockZ);
			return new ItemStack(mod_SecurityCraft.fLavaBucket, 1);
		}else{
			return null;
		}
	}
	
	private int[] getBlockInFront(World par1World, EntityPlayer par2EntityPlayer, double reach){
    	int[] blockInfo = {0, 0, 0, 0, -1, 0};
    	
    	MovingObjectPosition movingobjectposition = getMovingObjectPositionFromPlayer(par1World, par2EntityPlayer, true, reach);
    	
    	if(movingobjectposition != null){
    		if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK){
    			blockInfo[1] = movingobjectposition.blockX;
    			blockInfo[2] = movingobjectposition.blockY;
    			blockInfo[3] = movingobjectposition.blockZ;
    			blockInfo[4] = movingobjectposition.sideHit;
    			blockInfo[5] = par1World.getBlockMetadata(blockInfo[1], blockInfo[2], blockInfo[3]);
    			blockInfo[0] = Block.getIdFromBlock(par1World.getBlock(blockInfo[1], blockInfo[2], blockInfo[3]));

    		}
    	}
    	
    	return blockInfo;
    }
    
    private MovingObjectPosition getMovingObjectPositionFromPlayer(World par1World, EntityPlayer par2EntityPlayer, boolean flag, double reach){
    	float f = 1.0F;
    	float playerPitch = par2EntityPlayer.prevRotationPitch + (par2EntityPlayer.rotationPitch - par2EntityPlayer.prevRotationPitch) * f;
    	float playerYaw = par2EntityPlayer.prevRotationYaw + (par2EntityPlayer.rotationYaw - par2EntityPlayer.prevRotationYaw) * f;
    	double playerPosX = par2EntityPlayer.prevPosX + (par2EntityPlayer.posX - par2EntityPlayer.prevPosX) * f;
    	double playerPosY = (par2EntityPlayer.prevPosY + (par2EntityPlayer.posY - par2EntityPlayer.prevPosY) * f + 1.6200000000000001D) - par2EntityPlayer.yOffset;
    	double playerPosZ = par2EntityPlayer.prevPosZ + (par2EntityPlayer.posZ - par2EntityPlayer.prevPosZ) * f;
    	Vec3 vecPlayer = Vec3.createVectorHelper(playerPosX, playerPosY, playerPosZ);
    	float cosYaw = MathHelper.cos(-playerYaw * 0.01745329F - 3.141593F);
    	float sinYaw = MathHelper.sin(-playerYaw * 0.01745329F - 3.141593F);
    	float cosPitch = -MathHelper.cos(-playerPitch * 0.01745329F);
    	float sinPitch = -MathHelper.sin(-playerPitch * 0.01745329F);
    	float pointX = sinYaw * cosPitch;
    	float pointY = sinPitch;
    	float pointZ = cosYaw * cosPitch;
    	Vec3 vecPoint = vecPlayer.addVector(pointX * reach, pointY * reach, pointZ * reach);
    	MovingObjectPosition movingobjectposition = par1World.rayTraceBlocks(vecPlayer, vecPoint, flag);
    	return movingobjectposition;

    }
	
	private boolean isOwnableBlock(Block block, TileEntity tileEntity){
    	if(tileEntity instanceof TileEntityOwnable || tileEntity instanceof IOwnable || block instanceof BlockOwnable){
    		return true;
    	}else{
    		return false;
    	}
    }
	
	private boolean isCustomizableBlock(Block block, TileEntity tileEntity){
    	if(tileEntity instanceof CustomizableSCTE){
    		return true;
    	}else{
    		return false;
    	}
    }
    
    public EntityPlayer getPlayerFromName(String par1){
    	List players = MinecraftServer.getServer().getConfigurationManager().playerEntityList;
    	Iterator iterator = players.iterator();
    	
    	while(iterator.hasNext()){
    		EntityPlayer tempPlayer = (EntityPlayer) iterator.next();
    		if(tempPlayer.getCommandSenderName().matches(par1)){
    			return tempPlayer;
    		}
    	}
    	
    	return null;
    }
    
    public void setCooldown(int par1){
    	this.cooldownCounter = par1;
    }
    
    public int getCooldown(){
    	return this.cooldownCounter;
    }

}
