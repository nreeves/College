package net.runelite.script;

import static net.runelite.api.Opcodes.RUNELITE_EXECUTE;
import net.runelite.cache.script.Instructions;

public class RuneLiteInstructions extends Instructions
{
	@Override
	public void init()
	{
		super.init();
		add(RUNELITE_EXECUTE, "runelite_callback");
	}
}
