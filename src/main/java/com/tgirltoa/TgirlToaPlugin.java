package com.tgirltoa;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.NpcDespawned;
import net.runelite.api.events.NpcSpawned;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;

import java.util.HashSet;
import java.util.Set;

@Slf4j
@PluginDescriptor(
	name = "OwO Bloods"
)
public class TgirlToaPlugin extends Plugin
{
	private final int MESSAGE_REFRESH_RATE = 8; // TODO: Make configurable
	private final int MESSAGE_DISPLAY_DURATION = 6;

	private final String[] GOBLIN_MESSAGES = { "*nuzzles*", "UwU", "OwO", "Fren??", "Fren!" };

	@Getter(AccessLevel.PACKAGE)
	private final Set<NPC> goblins = new HashSet<>();

	private int ticksPassed = 0;

	@Inject
	private Client client;

	@Inject
	private TgirlToaConfig config;

	@Override
	protected void startUp() throws Exception
	{

	}

	@Override
	protected void shutDown() throws Exception
	{
		goblins.clear();
	}
	/**
	@Subscribe
	public void onGameStateChanged(GameStateChanged gameStateChanged)
	{
		if (gameStateChanged.getGameState() == GameState.LOGGED_IN)
		{
			client.addChatMessage(ChatMessageType.GAMEMESSAGE, "", "Example says " + config.greeting(), null);
		}
	}
	*/
	@Subscribe
	public void onNpcSpawned(NpcSpawned event){
		int i;

		NPC npc = event.getNpc();

		if (3029 <= npc.getId() && npc.getId() <= 3036){
			//Then the NPC is a goblin in lumby. Using for testing, will change id to match blood orbs once things are working
			i = (int) (Math.random() * GOBLIN_MESSAGES.length);
			npc.setOverheadText(GOBLIN_MESSAGES[i]);
			goblins.add(npc);
		}
	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event){
		NPC npc = event.getNpc();
		goblins.remove(npc);
	}

	@Subscribe
	public void onGameTick(GameTick tick){
		int i;

		ticksPassed = (ticksPassed + 1) % (MESSAGE_REFRESH_RATE + 1);
		for ( NPC goblin: goblins  ){
			i = (int) (Math.random() * ((GOBLIN_MESSAGES.length-1)+1));
			if ( ticksPassed == MESSAGE_REFRESH_RATE ) {
				goblin.setOverheadText(GOBLIN_MESSAGES[i]);
			}
			if ( ticksPassed == MESSAGE_DISPLAY_DURATION ){
				goblin.setOverheadText("");
			}
		}
	}



	@Provides
	TgirlToaConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TgirlToaConfig.class);
	}
}
