package org.freeforums.geforce.securitycraft.commands;

import java.util.ArrayList;
import java.util.List;

import org.freeforums.geforce.securitycraft.items.ItemModule;
import org.freeforums.geforce.securitycraft.main.HelpfulMethods;
import org.freeforums.geforce.securitycraft.main.mod_SecurityCraft;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;

public class CommandModule extends CommandBase implements ICommand {

	private List nicknames;

	private final String usage = "/module add <playerName> OR /module remove <playerName> OR /module copy OR /module paste";

	public CommandModule() {
		this.nicknames = new ArrayList();
		this.nicknames.add("module");
	}
	
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

	public String getName() {
		return "module";
	}
	
	public List getAliases() {
		return this.nicknames;
	}

	public String getCommandUsage(ICommandSender p_71518_1_) {
		return usage;
	}
	
	public boolean canCommandSenderUse(ICommandSender p_71519_1_) {
		return true;
	}

	public void execute(ICommandSender par1ICommandSender, String[] par2String) throws CommandException{
		if(par2String.length == 1){
			if(par2String[0].matches("copy")){
				EntityPlayer player = HelpfulMethods.getPlayerFromName(par1ICommandSender.getName());

				if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemModule && ((ItemModule) player.getCurrentEquippedItem().getItem()).canBeModified()){		
					mod_SecurityCraft.instance.setSavedModule(player.getCurrentEquippedItem().getTagCompound());
					HelpfulMethods.sendMessageToPlayer(player, "Module data saved.", EnumChatFormatting.GREEN);
				}else{
					HelpfulMethods.sendMessageToPlayer(player, "You must be holding the module you wish to save data from.", EnumChatFormatting.RED);
				}
				
				return;
			}else if(par2String[0].matches("paste")){
				EntityPlayer player = HelpfulMethods.getPlayerFromName(par1ICommandSender.getName());

				if(mod_SecurityCraft.instance.getSavedModule() == null){
					HelpfulMethods.sendMessageToPlayer(player, "There is no module data saved.", EnumChatFormatting.RED);
					return;
				}
				
				if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemModule && ((ItemModule) player.getCurrentEquippedItem().getItem()).canBeModified()){		
					player.getCurrentEquippedItem().setTagCompound(mod_SecurityCraft.instance.getSavedModule());
					mod_SecurityCraft.instance.setSavedModule(null);
					HelpfulMethods.sendMessageToPlayer(player, "Saved data to module.", EnumChatFormatting.GREEN);
				}
				
				return;
			}
		}else if(par2String.length == 2){
			if(par2String[0].matches("add")){
				EntityPlayer player = HelpfulMethods.getPlayerFromName(par1ICommandSender.getName());
				
				if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemModule && ((ItemModule) player.getCurrentEquippedItem().getItem()).canBeModified()){			
					if(player.getCurrentEquippedItem().getTagCompound() == null){
						player.getCurrentEquippedItem().setTagCompound(new NBTTagCompound());				
					}
					
					for(int i = 1; i <= 10; i++){
						if(player.getCurrentEquippedItem().getTagCompound().hasKey("Player" + i) && player.getCurrentEquippedItem().getTagCompound().getString("Player" + i).matches(par2String[1])){
							HelpfulMethods.sendMessageToPlayer(player, "The module you are holding already contains the player " + par2String[1] + ".", EnumChatFormatting.RED);
							return;
						}
					}
                    
					player.getCurrentEquippedItem().getTagCompound().setString("Player" + getNextSlot(player.getCurrentEquippedItem().getTagCompound()), par2String[1]);
					HelpfulMethods.sendMessageToPlayer(player, "Added " + par2String[1] + " to the held module.", EnumChatFormatting.GREEN);
					return;
				}else{
					HelpfulMethods.sendMessageToPlayer(player, "You must be holding the module you wish to modify!", EnumChatFormatting.RED);
					return;
				}
			}else if(par2String[0].matches("remove")){
				EntityPlayer player = HelpfulMethods.getPlayerFromName(par1ICommandSender.getName());
				
				if(player.getCurrentEquippedItem() != null && player.getCurrentEquippedItem().getItem() instanceof ItemModule && ((ItemModule) player.getCurrentEquippedItem().getItem()).canBeModified()){			
					if(player.getCurrentEquippedItem().getTagCompound() == null){
						player.getCurrentEquippedItem().setTagCompound(new NBTTagCompound());				
					}
					
					for(int i = 1; i <= 10; i++){
						if(player.getCurrentEquippedItem().getTagCompound().hasKey("Player" + i) && player.getCurrentEquippedItem().getTagCompound().getString("Player" + i).matches(par2String[1])){
							player.getCurrentEquippedItem().getTagCompound().removeTag("Player" + i);
						}
					}
					
					HelpfulMethods.sendMessageToPlayer(player, "Removed " + par2String[1] + " from the held module.", EnumChatFormatting.GREEN);
					return;
				}else{
					HelpfulMethods.sendMessageToPlayer(player, "You must be holding the module you wish to modify!", EnumChatFormatting.RED);
					return;
				}
			}
		}
		
		throw new WrongUsageException(usage);
	}

	private int getNextSlot(NBTTagCompound stackTagCompound) {
		for(int i = 1; i <= 10; i++){
			if(stackTagCompound.getString("Player" + i) != null && !stackTagCompound.getString("Player" + i).isEmpty()){
				continue;
			}else{
				return i;
			}
		}
		
		return 0;
	}

	public int compareTo(Object par1Obj) {
        return this.compareTo((ICommand)par1Obj);
	}


}
