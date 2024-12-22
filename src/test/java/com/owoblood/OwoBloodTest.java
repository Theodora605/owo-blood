package com.owoblood;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class OwoBloodTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(OwoBloodPlugin.class);
		RuneLite.main(args);
	}
}