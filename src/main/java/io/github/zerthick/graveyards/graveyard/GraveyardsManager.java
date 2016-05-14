/*
 * Copyright (C) 2016  Zerthick
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
import io.github.zerthick.graveyards.utils.config.ConfigValues;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Manager Class for Holding All Graveyards
 */
public class GraveyardsManager {

    // Map of all graveyards, Graveyards are first keyed by World UUID then Name.
    private Map<UUID, Map<String, Graveyard>> graveyardMap;

    /**
     * Instantiates the GraveyardManager with the given Graveyard Data
     *
     * @param graveyardMap the map of all the Graveyards
     */
    public GraveyardsManager(Map<UUID, Map<String, Graveyard>> graveyardMap) {
        this.graveyardMap = graveyardMap;
    }

    /**
     * Returns the map of all the Graveyards, used to serialize all graveyard data to a file
     *
     * @return the map of all the Graveyards
     */
    public Map<UUID, Map<String, Graveyard>> getGraveyardMap() {
        return graveyardMap;
    }

    /**
     * Attempts to get the Graveyard with the given name in the given worldUUID from the map
     *
     * @param name the name of the Graveyard to search for
     * @param worldUUID the UUID of the World to search in
     * @return Optional containing the Graveyard if found, empty if otherwise
     */
    public Optional<Graveyard> getGraveyard(String name, UUID worldUUID) {

        return Optional.ofNullable(graveyardMap.getOrDefault(worldUUID, new HashMap<>()).get(name.toLowerCase()));
    }

    /**
     * Adds a new Graveyard with the given attributes to the Graveyard Map
     *
     * @param name the name of the Graveyard
     * @param location the location of the Graveyard
     * @param rotation the rotation of the Graveyard
     * @param worldUUID the UUID of the Graveyard World
     */
    public void addGraveyard(String name, Vector3i location, Vector3d rotation, UUID worldUUID) {
        Graveyard graveyardToAdd = new Graveyard(name, location, rotation, ConfigValues.getInstance().getDefaultGraveyardMessage(name), 0);
        Map<String, Graveyard> graveyardWorldMap = graveyardMap.getOrDefault(worldUUID, new HashMap<>());
        graveyardWorldMap.put(name.toLowerCase(), graveyardToAdd);
        graveyardMap.put(worldUUID, graveyardWorldMap);
    }

    /**
     * Checks whether or not a Graveyard with the given name exists in the given World UUID
     *
     * @param name the name of the Graveyard
     * @param worldUUID the UUID of the Graveyard World
     * @return a boolean representing whether or not the Graveyard exists
     */
    public boolean exists(String name, UUID worldUUID){
        return getGraveyard(name, worldUUID).isPresent();
    }

    /**
     * Removes a Graveyard with the given name and World UUID from the Graveyard Map, effectively deleting it
     *
     * @param name the name of the Graveyard
     * @param worldUUID the UUID of the Graveyard World
     * @return Optional containing the Graveyard removed if found, empty if otherwise
     */
    public Optional<Graveyard> removeGraveyard(String name, UUID worldUUID) {

        return Optional.ofNullable(graveyardMap.getOrDefault(worldUUID, new HashMap<>()).remove(name.toLowerCase()));
    }

    /**
     * Gets a list representation of the Graveyards in the given World UUID
     *
     * @param worldUUID
     * @return
     */
    public List<Graveyard> getGraveyardList(UUID worldUUID) {

        return graveyardMap.getOrDefault(worldUUID, new HashMap<>()).values().stream().collect(Collectors.toList());
    }

    /**
     * Helper method to find the nearest graveyard. Uses brute-force
     * method for finding nearest neighbor.
     *
     * @param location  the query Location
     * @param worldUUID the query World
     * @return the nearest Graveyard
     */
    public Optional<Graveyard> findNearestGraveyard(Vector3i location, UUID worldUUID) {

        return getGraveyardList(worldUUID).parallelStream()
                .min((g1, g2) -> Integer.compare(location.distanceSquared(g1.getLocation()), location.distanceSquared(g2.getLocation())));
    }
}
