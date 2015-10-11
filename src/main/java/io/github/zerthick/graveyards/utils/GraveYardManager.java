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

package io.github.zerthick.graveyards.utils;

import com.flowpowered.math.vector.Vector3i;

import java.util.*;

public class GraveyardManager {
    private Map<UUID, List<Graveyard>> graveyardMap;

    public GraveyardManager() {
        graveyardMap = new HashMap<>();
    }

    public void addGraveyard(String name, Vector3i location, UUID worldUUID) {
        Graveyard newGraveyard = new Graveyard(name, location);
        List<Graveyard> graveyardList = graveyardMap.getOrDefault(worldUUID,
                new ArrayList<>());

        graveyardList.add(newGraveyard);
        graveyardMap.put(worldUUID, graveyardList);

        // TODO save graveyard data to file.
    }

    public void removeGraveyard(String name, UUID worldUUID) {
        Graveyard graveyardToRemove = new Graveyard(name, new Vector3i());
        List<Graveyard> graveyardList = graveyardMap.getOrDefault(worldUUID,
                new ArrayList<>());

        graveyardList.remove(graveyardToRemove);

        if (!graveyardList.isEmpty()) {
            graveyardMap.put(worldUUID, graveyardList);
        } else {
            graveyardMap.remove(worldUUID);
        }

        // TODO save graveyard data to file
    }

    /**
     * Helper method to find the nearest graveyard. Uses brute-force
     * method for finding nearest neighbor.
     *
     * @param location  the query location
     * @param worldUUID the query world
     * @return the nearest graveyard
     */
    public Graveyard findNearestGraveyard(Vector3i location, UUID worldUUID) {
        List<Graveyard> graveyardList = graveyardMap.get(worldUUID);
        Graveyard nearestGraveyard = null;
        if (!graveyardList.isEmpty()) {
            Graveyard currentGraveyard;
            Iterator<Graveyard> it = graveyardList.iterator();
            nearestGraveyard = it.next();
            while (it.hasNext()) {
                currentGraveyard = it.next();
                if (location.distanceSquared(currentGraveyard.getLocation()) < location
                        .distanceSquared(nearestGraveyard.getLocation())) {
                    nearestGraveyard = currentGraveyard;
                }
            }
        }
        return nearestGraveyard;
    }
}
