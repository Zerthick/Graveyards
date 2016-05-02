/*
 * Copyright (C) 2015  Zerthick
 *
 * This file is part of Graveyards.
 *
 * Graveyards is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * Graveyards is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Graveyards.  If not, see <http://www.gnu.org/licenses/>.
 */

package io.github.zerthick.graveyards.graveyard;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.text.Text;

import java.util.*;
import java.util.stream.Collectors;

public class GraveyardManager {

    private Map<UUID, Map<String, Graveyard>> graveyardMap;

    public GraveyardManager(Map<UUID, Map<String, Graveyard>> graveyardMap) {
        this.graveyardMap = graveyardMap;
    }

    public Map<UUID, Map<String, Graveyard>> getGraveyardMap() {
        return graveyardMap;
    }

    public Optional<Graveyard> getGraveyard(String name, UUID worldUUID) {
        Graveyard graveyardToReturn = graveyardMap.getOrDefault(worldUUID, new HashMap<>()).get(name);

        return Optional.ofNullable(graveyardToReturn);
    }

    public void addGraveyard(String name, Vector3i location, Vector3d rotation, UUID worldUUID) {

        Graveyard graveyardToAdd = new Graveyard(name, location, rotation, Text.of(""), 0);
        Map<String, Graveyard> graveyardWorldMap = graveyardMap.getOrDefault(worldUUID, new HashMap<>());
        graveyardWorldMap.put(name, graveyardToAdd);
        graveyardMap.put(worldUUID, graveyardWorldMap);
    }

    public boolean exists(String name, UUID worldUUID){
        return getGraveyard(name, worldUUID).isPresent();
    }

    public Optional<Graveyard> removeGraveyard(String name, UUID worldUUID) {
        if (graveyardMap.containsKey(worldUUID)) {
            return Optional.ofNullable(graveyardMap.get(worldUUID).remove(name));
        }
        return Optional.empty();
    }

    public List<Graveyard> getGraveyardList(UUID worldUUID) {
        return graveyardMap.getOrDefault(worldUUID, new HashMap<>()).values().stream().collect(Collectors.toList());
    }

    /**
     * Helper method to find the nearest graveyard. Uses brute-force
     * method for finding nearest neighbor.
     *
     * @param location  the query location
     * @param worldUUID the query world
     * @return the nearest graveyard
     */
    public Optional<Graveyard> findNearestGraveyard(Vector3i location, UUID worldUUID) {
        Collection<Graveyard> graveyardCol = graveyardMap.getOrDefault(worldUUID, new HashMap<>()).values();

        Graveyard nearestGraveyard = null;

        if (!graveyardCol.isEmpty()) {
            Iterator<Graveyard> it = graveyardCol.iterator();
            nearestGraveyard = it.next();
            Graveyard currentGraveyard;
            while (it.hasNext()) {
                currentGraveyard = it.next();
                if (location.distanceSquared(currentGraveyard.getLocation()) < location
                        .distanceSquared(nearestGraveyard.getLocation())) {
                    nearestGraveyard = currentGraveyard;
                }
            }
        }
        return Optional.ofNullable(nearestGraveyard);
    }
}
