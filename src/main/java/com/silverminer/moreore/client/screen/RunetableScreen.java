package com.silverminer.moreore.client.screen;

import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.container.RunetableContainer;

import net.minecraft.client.gui.screen.inventory.AbstractRepairScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RunetableScreen extends AbstractRepairScreen<RunetableContainer> {
	private static final ResourceLocation GUI = new ResourceLocation(MoreOre.MODID, "textures/gui/rune_table_gui.png");

	public RunetableScreen(RunetableContainer container, PlayerInventory playerInventory, ITextComponent title) {
		super(container, playerInventory, title, GUI);
	}
}