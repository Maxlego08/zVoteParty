package fr.maxlego08.zvoteparty.listener.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import com.vexsoftware.votifier.model.Vote;
import com.vexsoftware.votifier.model.VotifierEvent;

import fr.maxlego08.zvoteparty.ZVotePartyPlugin;
import fr.maxlego08.zvoteparty.api.VotePartyManager;

public class VotifierListener implements Listener {

    private final ZVotePartyPlugin plugin;

    /**
     * Constructor to initialize the VotifierListener with the plugin instance.
     * 
     * @param plugin The instance of ZVotePartyPlugin
     */
    public VotifierListener(ZVotePartyPlugin plugin) {
        super();
        this.plugin = plugin;
    }

    /**
     * Handles Votifier events by processing votes.
     * 
     * @param event The VotifierEvent containing vote information
     */
    @EventHandler
    public void onVote(VotifierEvent event) {
        VotePartyManager manager = this.plugin.getManager();
        
        Vote vote = event.getVote();
        // Process the vote and update the vote party manager
        manager.vote(vote.getUsername(), vote.getServiceName(), true);
    }
}
