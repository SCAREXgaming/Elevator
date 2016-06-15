package fr.scarex.elevator.tileentity;

import java.util.List;
import java.util.UUID;

/**
 * @author SCAREX
 *
 */
public interface IWhitelist
{
    public void setWhitelist(boolean whitelist);

    public boolean isWhitelist();

    public List<UUID> getPlayerList();

    public void addPlayerToList(UUID uuid);

    public void removePlayer(int index);

    public void removePlayer(UUID uuid);
}
