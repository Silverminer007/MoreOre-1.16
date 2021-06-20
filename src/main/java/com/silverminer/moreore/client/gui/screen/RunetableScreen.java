package com.silverminer.moreore.client.gui.screen;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.silverminer.moreore.MoreOre;
import com.silverminer.moreore.client.gui.container.RunetableContainer;
import com.silverminer.moreore.util.runes.RuneInventoryRegistry;

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
		this.imageWidth = 204;
		this.titleLabelX = 50;
	}

	protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
		super.renderBg(matrixStack, partialTicks, x, y);
		int i = (this.width - this.imageWidth) / 2;
		int j = (this.height - this.imageHeight) / 2;
		for (int k = RuneInventoryRegistry.getInventorySize(this.getMenu().activeUUID); k < 3; k++) {
			this.blit(matrixStack, i + 176, j + 7 + k * 22, this.imageWidth, 21, 18, 18);
		}
	}
}