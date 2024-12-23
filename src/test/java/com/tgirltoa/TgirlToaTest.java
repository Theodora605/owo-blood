package com.tgirltoa;

import net.runelite.client.RuneLite;
import net.runelite.client.externalplugins.ExternalPluginManager;

public class TgirlToaTest
{
	public static void main(String[] args) throws Exception
	{
		ExternalPluginManager.loadBuiltin(TgirlToaPlugin.class);
		RuneLite.main(args);
	}
}