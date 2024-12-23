package com.tgirltoa;

import com.google.inject.Provides;
import javax.inject.Inject;

import lombok.Getter;
import lombok.AccessLevel;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.NPC;
import net.runelite.api.NpcID;
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
	name = "T-Girl ToA"
)
public class TgirlToaPlugin extends Plugin
{
	private final int MESSAGE_REFRESH_RATE = 8;
	private final int MESSAGE_DISPLAY_DURATION = 6;

	private final int ORB_MESSAGES_RATIO = 3; // Assign messages to 1 / value orbs
	private final int SWARM_MESSAGES_RATIO = 3;

	private final String[] BLOOD_MESSAGES = { "*nuzzles*", "UwU", "OwO", "fren??", "fren!" };
	private final String[] ORB_MESSAGES = { "stickies :3", "yummies", "~~~~~", "nom" };
	private final String[] BOULDER_MESSAGES = {  "~~tumbly~rumblies~~", "don't hit me senpai y.y" };
	private final String[] SWARM_MESSAGES = { "step on me UwU", "OwO", "pewpew" };

	@Getter(AccessLevel.PACKAGE)
	private final Set<NPC> zebakBloodClouds = new HashSet<>();

	@Getter(AccessLevel.PACKAGE)
	private final Set<NPC> akkhaOrbs = new HashSet<>();

	@Getter(AccessLevel.PACKAGE)
	private final Set<NPC> kephriSwarms = new HashSet<>();

	@Getter(AccessLevel.PACKAGE)
	private final Set<NPC> babaBoulders = new HashSet<>();

	private int ticksPassed = 0;

	private int orbCounter = 0;
	private int swarmCounter = 0;

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
		akkhaOrbs.clear();
		babaBoulders.clear();
		zebakBloodClouds.clear();
		kephriSwarms.clear();
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
	private boolean isZebakBloodCloud(NPC npc){
		return npc.getId() == NpcID.BLOOD_CLOUD || npc.getId() == NpcID.BLOOD_CLOUD_11743;
	}

	private boolean isAkkhaOrb(NPC npc){
		return npc.getId() == NpcID.UNSTABLE_ORB;
	}

	private boolean isBabaBoulder(NPC npc){
		return npc.getId() == NpcID.BOULDER_11782 || npc.getId() == NpcID.BOULDER_11783;
	}

	private boolean isKephriSwarm(NPC npc){
		return npc.getId() == NpcID.SCARAB_SWARM_11723;
	}

	private void assignRandomMessageOrReset(NPC npc, String[] messages, int tick){
		if ( tick == MESSAGE_REFRESH_RATE ) {
			int i = (int) (Math.random() * messages.length);
			npc.setOverheadText(messages[i]);
		}
		if( tick == MESSAGE_DISPLAY_DURATION ){
			npc.setOverheadText("");
		}
	}

	@Subscribe
	public void onNpcSpawned(NpcSpawned event){

		NPC npc = event.getNpc();

		if ( isBabaBoulder(npc) ){
			assignRandomMessageOrReset(npc, BOULDER_MESSAGES, MESSAGE_REFRESH_RATE);
			babaBoulders.add(npc);
		}

		if( isZebakBloodCloud(npc) ){
			assignRandomMessageOrReset(npc, BLOOD_MESSAGES, MESSAGE_REFRESH_RATE);
			zebakBloodClouds.add(npc);
		}

		if ( isAkkhaOrb(npc) ){
			if ( orbCounter == 0 ) {
				assignRandomMessageOrReset(npc, ORB_MESSAGES, MESSAGE_REFRESH_RATE);
				akkhaOrbs.add(npc);
			}
			orbCounter = (orbCounter + 1) % ORB_MESSAGES_RATIO;
		}

		if ( isKephriSwarm(npc) ){
			if ( swarmCounter == 0 ) {
				assignRandomMessageOrReset(npc, SWARM_MESSAGES, MESSAGE_REFRESH_RATE);
				kephriSwarms.add(npc);
			}
			swarmCounter = (swarmCounter + 1) % SWARM_MESSAGES_RATIO;
		}

	}

	@Subscribe
	public void onNpcDespawned(NpcDespawned event){
		NPC npc = event.getNpc();

		babaBoulders.remove(npc);
		akkhaOrbs.remove(npc);
		zebakBloodClouds.remove(npc);
		kephriSwarms.remove(npc);
	}

	@Subscribe
	public void onGameTick(GameTick tick){
		ticksPassed = (ticksPassed + 1) % (MESSAGE_REFRESH_RATE + 1);

		for ( NPC orb: akkhaOrbs ){
			assignRandomMessageOrReset(orb, ORB_MESSAGES, ticksPassed);
		}

		for( NPC boulder: babaBoulders ){
			assignRandomMessageOrReset(boulder, BOULDER_MESSAGES, ticksPassed);
		}

		for ( NPC swarm: kephriSwarms ){
			assignRandomMessageOrReset(swarm, SWARM_MESSAGES, ticksPassed);
		}

		for ( NPC blood: zebakBloodClouds ){
			assignRandomMessageOrReset(blood, BLOOD_MESSAGES, ticksPassed);
		}
	}



	@Provides
	TgirlToaConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(TgirlToaConfig.class);
	}
}
