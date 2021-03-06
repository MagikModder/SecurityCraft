package org.freeforums.geforce.securitycraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import org.freeforums.geforce.securitycraft.enums.EnumCustomModules;
import org.freeforums.geforce.securitycraft.main.HelpfulMethods;
import org.freeforums.geforce.securitycraft.main.mod_SecurityCraft;
import org.freeforums.geforce.securitycraft.tileentity.TileEntityInventoryScanner;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockInventoryScannerBlock extends Block{
    
	public BlockInventoryScannerBlock(Material par2Material) {
		super(par2Material);
		this.setBlockBounds(0.250F, 0.300F, 0.300F, 0.750F, 0.700F, 0.700F);

	}
	

    


	public AxisAlignedBB getCollisionBoundingBoxFromPool(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
	
	public boolean isOpaqueCube(){
		return false;	
	}
	
	/**
     * If this block doesn't render as an ordinary block it will return False (examples: signs, buttons, stairs, etc)
     */
    public boolean renderAsNormalBlock()
    {
        return false;
    }
    
    /**
     * Lets the block know when one of its neighbor changes. Doesn't know which neighbor changed (coordinates passed are
     * their own) Args: x, y, z, neighbor Block
     */
    public void onNeighborBlockChange(World par1World, int par2, int par3, int par4, Block par5Block) {
    	if(par1World.isRemote){
    		return;
    	}else{
    		if(!HelpfulMethods.hasInventoryScannerFacingBlock(par1World, par2, par3, par4)){
        		par1World.func_147480_a(par2, par3, par4, false);
        	}
    	}
    }
    
    
    /**
     * Triggered whenever an entity collides with this block (enters into the block). Args: world, x, y, z, entity
     */
    public void onEntityCollidedWithBlock(World par1World, int par2, int par3, int par4, Entity par5Entity){
    	if(par1World.isRemote){
    		return;
    	}else{
	        if(par5Entity instanceof EntityPlayer){   	        	
	        	if(par1World.getTileEntity(par2 - 1, par3, par4) != null && par1World.getTileEntity(par2 - 1, par3, par4) instanceof TileEntityInventoryScanner){    
		        	if(HelpfulMethods.checkForModule(par1World, par2 - 1, par3, par4, ((EntityPlayer) par5Entity), EnumCustomModules.WHITELIST)){ return; }
	        		for(int i = 0; i < 10; i++){
	        			for(int j = 0; j < ((EntityPlayer) par5Entity).inventory.mainInventory.length; j++){
	        				if(((TileEntityInventoryScanner)par1World.getTileEntity(par2 - 1, par3, par4)).getStackInSlotCopy(i) != null){       				
	        					if(((EntityPlayer) par5Entity).inventory.mainInventory[j] != null){
	        						checkInventory(((EntityPlayer) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2 - 1, par3, par4)), ((TileEntityInventoryScanner)par1World.getTileEntity(par2 - 1, par3, par4)).getStackInSlotCopy(i));
	        					}       					
	        				}
	        			}
	        		}
	        	}else if(par1World.getTileEntity(par2 + 1, par3, par4) != null && par1World.getTileEntity(par2 + 1, par3, par4) instanceof TileEntityInventoryScanner){
		        	if(HelpfulMethods.checkForModule(par1World, par2 + 1, par3, par4, ((EntityPlayer) par5Entity), EnumCustomModules.WHITELIST)){ return; }
	        		for(int i = 0; i < 10; i++){
	        			for(int j = 0; j < ((EntityPlayer) par5Entity).inventory.mainInventory.length; j++){
	        				if(((TileEntityInventoryScanner)par1World.getTileEntity(par2 + 1, par3, par4)).getStackInSlotCopy(i) != null){       				
	        					if(((EntityPlayer) par5Entity).inventory.mainInventory[j] != null){
	        						checkInventory(((EntityPlayer) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2 + 1, par3, par4)), ((TileEntityInventoryScanner)par1World.getTileEntity(par2 + 1, par3, par4)).getStackInSlotCopy(i));
	        					}       					
	        				}
	        			}
	        		}
	        	}else if(par1World.getTileEntity(par2, par3, par4 - 1) != null && par1World.getTileEntity(par2, par3, par4 - 1) instanceof TileEntityInventoryScanner){
		        	if(HelpfulMethods.checkForModule(par1World, par2, par3, par4 - 1, ((EntityPlayer) par5Entity), EnumCustomModules.WHITELIST)){ return; }
	        		for(int i = 0; i < 10; i++){
	        			for(int j = 0; j < ((EntityPlayer) par5Entity).inventory.mainInventory.length; j++){
	        				if(((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 - 1)).getStackInSlotCopy(i) != null){       				
	        					if(((EntityPlayer) par5Entity).inventory.mainInventory[j] != null){
	        						checkInventory(((EntityPlayer) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 - 1)), ((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 - 1)).getStackInSlotCopy(i));
	        					}       					
	        				}
	        			}
	        		}
	        	}else if(par1World.getTileEntity(par2, par3, par4 + 1) != null && par1World.getTileEntity(par2, par3, par4 + 1) instanceof TileEntityInventoryScanner){
		        	if(HelpfulMethods.checkForModule(par1World, par2, par3, par4 + 1, ((EntityPlayer) par5Entity), EnumCustomModules.WHITELIST)){ return; }
	        		for(int i = 0; i < 10; i++){
	        			for(int j = 0; j < ((EntityPlayer) par5Entity).inventory.mainInventory.length; j++){
	        				if(((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 + 1)).getStackInSlotCopy(i) != null){       				
	        					if(((EntityPlayer) par5Entity).inventory.mainInventory[j] != null){
	        						checkInventory(((EntityPlayer) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 + 1)), ((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 + 1)).getStackInSlotCopy(i));
	        					}       					
	        				}
	        			}
	        		}
	        	}
	        //******************************************
	        }else if(par5Entity instanceof EntityItem){
	        	if(par1World.getTileEntity(par2 - 1, par3, par4) != null && par1World.getTileEntity(par2 - 1, par3, par4) instanceof TileEntityInventoryScanner){
	        		for(int i = 0; i < 10; i++){
	        			if(((TileEntityInventoryScanner)par1World.getTileEntity(par2 - 1, par3, par4)).getStackInSlotCopy(i) != null){       				
	        				if(((EntityItem) par5Entity).getEntityItem() != null){
	        					checkEntity(((EntityItem) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2 - 1, par3, par4)).getStackInSlotCopy(i));
	        				}       					
	        			}
	        		}
	        	}else if(par1World.getTileEntity(par2 + 1, par3, par4) != null && par1World.getTileEntity(par2 + 1, par3, par4) instanceof TileEntityInventoryScanner){
	        		for(int i = 0; i < 10; i++){
	        			if(((TileEntityInventoryScanner)par1World.getTileEntity(par2 + 1, par3, par4)).getStackInSlotCopy(i) != null){       				
	        				if(((EntityItem) par5Entity).getEntityItem() != null){
	        					checkEntity(((EntityItem) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2 + 1, par3, par4)).getStackInSlotCopy(i));
	        				}       					
	        			}
	        		}
	        	}else if(par1World.getTileEntity(par2, par3, par4 - 1) != null && par1World.getTileEntity(par2, par3, par4 - 1) instanceof TileEntityInventoryScanner){
	        		for(int i = 0; i < 10; i++){
	        			if(((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 - 1)).getStackInSlotCopy(i) != null){       				
	        				if(((EntityItem) par5Entity).getEntityItem() != null){
	        					checkEntity(((EntityItem) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 - 1)).getStackInSlotCopy(i));
	        				}       					
	        			}
	        		}
	        	}else if(par1World.getTileEntity(par2, par3, par4 + 1) != null && par1World.getTileEntity(par2, par3, par4 + 1) instanceof TileEntityInventoryScanner){
	        		for(int i = 0; i < 10; i++){
	        			if(((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 + 1)).getStackInSlotCopy(i) != null){       				
	        				if(((EntityItem) par5Entity).getEntityItem() != null){
	        					checkEntity(((EntityItem) par5Entity), ((TileEntityInventoryScanner)par1World.getTileEntity(par2, par3, par4 + 1)).getStackInSlotCopy(i));
	        				}       					
	        			}
	        		}
	        	}
	        }      
    	}
    }
    
    public void checkInventory(EntityPlayer par1EntityPlayer, TileEntityInventoryScanner par2TileEntity, ItemStack par3){
//    	Block block = null;
//		Item item = null;
//		boolean flag = false;
//		
//		if(hasMultipleItemStacks(par3)){
//			if (Item.itemRegistry.containsKey(par3))
//	        {
//				
//	            item = (Item)Item.itemRegistry.getObject(par3);
//	            flag = true;
//	        }
//		}
//		
//		if (Block.blockRegistry.containsKey(par3) && !flag)
//        {
//			
//            block = (Block)Block.blockRegistry.getObject(par3);
//        }
//		
//		if (Item.itemRegistry.containsKey(par3) && !flag)
//        {
//			
//            item = (Item)Item.itemRegistry.getObject(par3);
//        }
		
		if(par2TileEntity.getType().matches("redstone")){
			for(int i = 1; i <= par1EntityPlayer.inventory.mainInventory.length; i++){
				if(par1EntityPlayer.inventory.mainInventory[i - 1] != null){
					//if(par1EntityPlayer.inventory.mainInventory[i - 1].getItem() == par3.getItem() || par1EntityPlayer.inventory.mainInventory[i - 1].getItem() == Item.getItemFromBlock(par3.getItem())){
					if(par1EntityPlayer.inventory.mainInventory[i - 1].getItem() == par3.getItem()){
						if(!par2TileEntity.shouldProvidePower()){
							par2TileEntity.setShouldProvidePower(true);
						}
						
						mod_SecurityCraft.log("Running te update");
						par2TileEntity.setCooldown(60);
						this.checkAndUpdateTEAppropriately(par2TileEntity.getWorldObj(), par2TileEntity.xCoord, par2TileEntity.yCoord, par2TileEntity.zCoord, par2TileEntity);
						HelpfulMethods.updateAndNotify(par2TileEntity.getWorldObj(), par2TileEntity.xCoord, par2TileEntity.yCoord, par2TileEntity.zCoord, par2TileEntity.getWorldObj().getBlock(par2TileEntity.xCoord, par2TileEntity.yCoord, par2TileEntity.zCoord), 1, true);
						mod_SecurityCraft.log("Emitting redstone on the " + FMLCommonHandler.instance().getEffectiveSide() + " side. (te coords: X: " + par2TileEntity.xCoord + " Y: " + + par2TileEntity.yCoord + " Z: " + + par2TileEntity.zCoord + " )");
					}
				}
			}
		}else if(par2TileEntity.getType().matches("check")){
			for(int i = 1; i <= par1EntityPlayer.inventory.mainInventory.length; i++){
				if(par1EntityPlayer.inventory.mainInventory[i - 1] != null){
					//if(par1EntityPlayer.inventory.mainInventory[i - 1].getItem() == par3.getItem() || par1EntityPlayer.inventory.mainInventory[i - 1].getItem() == HelpfulMethods.getItemFromBlock(par3.getItem())){
					if(par1EntityPlayer.inventory.mainInventory[i - 1].getItem() == par3.getItem()){
						par1EntityPlayer.inventory.mainInventory[i - 1] = null;
					}
				}
			}
		}
    }
    
    public void checkEntity(EntityItem par1EntityItem, ItemStack par2){
		if(par1EntityItem.getEntityItem().getItem() == par2.getItem()){
			par1EntityItem.setDead();
		}
		
    }
    
    private void checkAndUpdateTEAppropriately(World par1World, int par2, int par3, int par4, TileEntityInventoryScanner par5TileEntityIS) {
    	mod_SecurityCraft.log("Updating te");
		if(par1World.getBlockMetadata(par2, par3, par4) == 4 && par1World.getBlock(par2 - 2, par3, par4) == mod_SecurityCraft.inventoryScanner && par1World.getBlock(par2 - 1, par3, par4) == mod_SecurityCraft.inventoryScannerField && par1World.getBlockMetadata(par2 - 2, par3, par4) == 5){
			((TileEntityInventoryScanner) par1World.getTileEntity(par2 - 2, par3, par4)).setShouldProvidePower(true);
			((TileEntityInventoryScanner) par1World.getTileEntity(par2 - 2, par3, par4)).setCooldown(60);
			HelpfulMethods.updateAndNotify(par1World, par2 - 2, par3, par4, par1World.getBlock(par2, par3, par4), 1, true);
		}
		else if(par1World.getBlockMetadata(par2, par3, par4) == 5 && par1World.getBlock(par2 + 2, par3, par4) == mod_SecurityCraft.inventoryScanner && par1World.getBlock(par2 + 1, par3, par4) == mod_SecurityCraft.inventoryScannerField && par1World.getBlockMetadata(par2 + 2, par3, par4) == 4){
			((TileEntityInventoryScanner) par1World.getTileEntity(par2 + 2, par3, par4)).setShouldProvidePower(true);
			((TileEntityInventoryScanner) par1World.getTileEntity(par2 + 2, par3, par4)).setCooldown(60);
			HelpfulMethods.updateAndNotify(par1World, par2 + 2, par3, par4, par1World.getBlock(par2, par3, par4), 1, true);

		}
		else if(par1World.getBlockMetadata(par2, par3, par4) == 2 && par1World.getBlock(par2, par3, par4 - 2) == mod_SecurityCraft.inventoryScanner && par1World.getBlock(par2, par3, par4 - 1) == mod_SecurityCraft.inventoryScannerField && par1World.getBlockMetadata(par2, par3, par4 - 2) == 3){
			((TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4 - 2)).setShouldProvidePower(true);
			((TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4 - 2)).setCooldown(60);
			HelpfulMethods.updateAndNotify(par1World, par2, par3, par4 - 2, par1World.getBlock(par2, par3, par4), 1, true);

		}
		else if(par1World.getBlockMetadata(par2, par3, par4) == 3 && par1World.getBlock(par2, par3, par4 + 2) == mod_SecurityCraft.inventoryScanner && par1World.getBlock(par2, par3, par4 + 1) == mod_SecurityCraft.inventoryScannerField && par1World.getBlockMetadata(par2, par3, par4 + 2) == 2){
			((TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4 + 2)).setShouldProvidePower(true);
			((TileEntityInventoryScanner) par1World.getTileEntity(par2, par3, par4 + 2)).setCooldown(60);
			HelpfulMethods.updateAndNotify(par1World, par2, par3, par4 + 2, par1World.getBlock(par2, par3, par4), 1, true);

		}
	}
    
    private boolean hasMultipleItemStacks(String string) {
    	boolean flag1 = false, flag2 = false;
    	
    	if (Block.blockRegistry.containsKey(string))
        {
            flag1 = true;
        }
		
		if (Item.itemRegistry.containsKey(string))
        {
            flag2 = true;
        }
		
		return flag1 && flag2 ? true : false;
	}
    
    
    /**
     * Called when the block is placed in the world.
     */
    public void onBlockPlacedBy(World par1World, int par2, int par3, int par4, EntityLivingBase par5EntityLivingBase, ItemStack par6ItemStack)
    {
        int l = MathHelper.floor_double((double)(par5EntityLivingBase.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;

        if (l == 0)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 2, 2);
            
        }

        if (l == 1)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 5, 2);
           

        }

        if (l == 2)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 3, 2);
           


        }

        if (l == 3)
        {
            par1World.setBlockMetadataWithNotify(par2, par3, par4, 4, 2);
           
            

        }
   
    }

    /**
     * Updates the blocks bounds based on its current state. Args: world, x, y, z
     */
    public void setBlockBoundsBasedOnState(IBlockAccess par1IBlockAccess, int par2, int par3, int par4)
    {
        if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 1)
        {
    		this.setBlockBounds(0.000F, 0.000F, 0.400F, 1.000F, 1.000F, 0.600F);
        }
        else if (par1IBlockAccess.getBlockMetadata(par2, par3, par4) == 2)
        {
    		this.setBlockBounds(0.400F, 0.000F, 0.000F, 0.600F, 1.000F, 1.000F);
        }
    }
    
    @SideOnly(Side.CLIENT)

    /**
     * only called by clickMiddleMouseButton , and passed to inventory.setCurrentItem (along with isCreative)
     */
    public Item getItem(World par1World, int par2, int par3, int par4)
    {
        return null;
    }
    

    @SideOnly(Side.CLIENT)

    /**
     * When this method is called, your block should register all the icons it needs with the given IconRegister. This
     * is the only chance you get to register icons.
     */
    public void registerBlockIcons(IIconRegister par1IconRegister)
    {
        this.blockIcon = par1IconRegister.registerIcon("securitycraft:aniLaser");
        
    }

//	public TileEntity createNewTileEntity(World par1World) {
//		return new TileEntityISF();
//	}

}
