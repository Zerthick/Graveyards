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
    private Map<UUID, Set<Graveyard>> graveyardMap;

    public GraveyardManager() {
        graveyardMap = new HashMap<>();
    }

    public GraveyardManager(Map<UUID, Set<Graveyard>> graveyardMap){
        this.graveyardMap = graveyardMap;
    }

    public Map<UUID, Set<Graveyard>> getGraveyardMap(){
        return graveyardMap;
    }

    public boolean addGraveyard(String name, Vector3i location, UUID worldUUID) {
        Graveyard newGraveyard = new Graveyard(name, location);
        Set<Graveyard> graveyardSet = graveyardMap.getOrDefault(worldUUID,
                new HashSet<>());

        if(!graveyardSet.add(newGraveyard)){return false;}
        graveyardMap.put(worldUUID, graveyardSet);

        return true;
    }

    public boolean removeGraveyard(String name, UUID worldUUID) {
        Set<Graveyard> graveyardSet = graveyardMap.getOrDefault(worldUUID,
                new HashSet<>());

        for(Graveyard graveyard : graveyardSet){
            if(graveyard.getName().equalsIgnoreCase(name)){
                graveyardSet.remove(graveyard);

                if (!graveyardSet.isEmpty()) {
                    graveyardMap.put(worldUUID, graveyardSet);
                } else {
                    graveyardMap.remove(worldUUID);
                }
                return true;
            }
        }
        return false;
    }

    public List<Graveyard> getGraveyardList(UUID worldUUID){
        Set<Graveyard> graveyardSet = graveyardMap.get(worldUUID);
        List<Graveyard> graveyardList = new ArrayList<>();
        if(graveyardSet != null) {
            graveyardList.addAll(graveyardSet);
        }
        return graveyardList;
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
        Set<Graveyard> graveyardSet = graveyardMap.get(worldUUID);
        Graveyard nearestGraveyard = null;
        if (!graveyardSet.isEmpty()) {
            Graveyard currentGraveyard;
            Iterator<Graveyard> it = graveyardSet.iterator();
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
