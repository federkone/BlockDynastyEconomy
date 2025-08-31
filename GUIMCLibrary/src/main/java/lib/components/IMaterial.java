package lib.components;

public interface IMaterial {
    /**
     * Gets the platform-specific material implementation
     * @return The native material object (ItemType for Sponge, Material for Bukkit)
     */
    Object get();
}
