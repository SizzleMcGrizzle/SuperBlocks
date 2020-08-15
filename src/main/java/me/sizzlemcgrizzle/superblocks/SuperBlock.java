package me.sizzlemcgrizzle.superblocks;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

//String serializedLocation = (int) location.getX() + "&-&" + (int) location.getY() + "&-&" + (int) location.getZ() + "&-&" + location.getWorld().getName();
public abstract class SuperBlock implements ConfigurationSerializable {
    
    private List<Location> structure;
    private UUID owner;
    
    public SuperBlock(List<Location> structure, UUID owner) {
        this.structure = structure;
        this.owner = owner;
    }
    
    public SuperBlock(Map<String, Object> map) {
        this.structure = (List<Location>) map.get("structure");
        this.owner = UUID.fromString((String) map.get("owner"));
    }
    
    @Override
    public @Nonnull
    Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<>();
        
        map.put("structure", structure);
        map.put("owner", owner.toString());
        
        return map;
    }
    
    public UUID getOwner() {
        return owner;
    }
    
    public List<Location> getStructure() {
        return structure;
    }
    
    public abstract void doFunction(Player player, Location location);
    
    
}
