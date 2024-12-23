package com.tgirltoa;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("tgirltoa")
public interface TgirlToaConfig extends Config
{
	/**
	@ConfigItem(
		keyName = "messages",
		name = "Blood Orb Messages",
		description = "A list of messages that the orbs can say."
	)
	default String bloodMessages()
	{
		return "Hello";
	}
	*/

}
