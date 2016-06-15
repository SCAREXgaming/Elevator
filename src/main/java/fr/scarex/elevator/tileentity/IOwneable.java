package fr.scarex.elevator.tileentity;

import java.util.UUID;

/**
 * @author SCAREX
 *
 */
public interface IOwneable
{
    public boolean isOwner(UUID uuid);
}
