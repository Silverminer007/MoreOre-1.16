package com.silverminer.moreore.common.commands;

import com.mojang.brigadier.CommandDispatcher;

import net.minecraft.command.CommandSource;

public class ModCommands{
	public ModCommands(CommandDispatcher<CommandSource> dispatcher) {
		TlpCommand.register(dispatcher);
	}

}
