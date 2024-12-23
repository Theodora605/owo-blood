package com.owoblood;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("owoblood")
public interface OwoBloodConfig extends Config
{

	@ConfigItem(
		keyName = "messages",
		name = "Blood Orb Messages",
		description = "A list of messages that the orbs can say."
	)
	default String bloodMessages()
	{
		return "Hello";
	}

}
