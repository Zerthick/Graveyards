/*
 * Copyright (C) 2017  Zerthick
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

import com.flowpowered.math.vector.Vector3i;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Manager Class for Holding All Graveyard Groups
 */
public class GraveyardGroupManager {

    private Map<String, GraveyardGroup> graveyardsGroupMap;

    /**
     * Instantiates the GraveyardGroupManager with the given Graveyard Data
     *
     * @param graveyardsGroupMap the map of all the Graveyards in this Group
     */
    public GraveyardGroupManager(Map<String, GraveyardGroup> graveyardsGroupMap) {
        this.graveyardsGroupMap = graveyardsGroupMap;
    }

    /**
     * Returns the map of all the Graveyard Groups, used to serialize all graveyard data to a file
     *
     * @return the map of all the Graveyard Groups
     */
    public Map<String, GraveyardGroup> getGraveyardGroupMap() {
        return graveyardsGroupMap;
    }

    /**
     * Attempts to get the Graveyard Group with the given name
     *
     * @param groupName the name of the Graveyard Group to search for
     * @return Optional containing the Graveyard Group if found, empty if otherwise
     */
    public Optional<GraveyardGroup> getGraveyardGroup(String groupName) {
        return Optional.ofNullable(graveyardsGroupMap.get(groupName));
    }

    /**
     * Adds a new group with the given name to the GroupManager
     *
     * @param groupName the name of the Graveyard Group
     */
    public void addGraveyardGroup(String groupName, GraveyardGroup group) {
        graveyardsGroupMap.put(groupName, group);
    }

    /**
     * Checks whether or not a Graveyard Group with the given name exists
     *
     * @param groupName the name of the Graveyard Group
     * @return a boolean representing whether or not the Graveyard Group exists
     */
    public boolean exists(String groupName) {
        return graveyardsGroupMap.containsKey(groupName);
    }

    /**
     * Removes a GraveyardGroup with the given name
     *
     * @param groupName teh name of the Graveyard Group
     * @return Optional containing the Graveyard Group removed if found, empty if otherwise
     */
    public Optional<GraveyardGroup> removeGraveyardGroup(String groupName) {
        return Optional.ofNullable(graveyardsGroupMap.remove(groupName));
    }

    /**
     * Gets a list representation of the Graveyard Groups
     *
     * @return
     */
    public List<GraveyardGroup> getGraveyardGroupList() {
        return graveyardsGroupMap.values().stream().collect(Collectors.toList());
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
        return findNearestGraveyardWithFilter(location, worldUUID, e -> true);
    }

    /**
     * Helper method to find the nearest graveyard. Uses brute-force
     * method for finding nearest neighbor.
     *
     * @param location  the query Location
     * @param worldUUID the query World
     * @return the nearest Graveyard
     */
    public Optional<Graveyard> findNearestGraveyardInRange(Vector3i location, UUID worldUUID) {
        return findNearestGraveyardInRangeWithFilter(location, worldUUID, e -> true);
    }

    /**
     * Helper method to find the nearest graveyard. Only considers graveyard groups that pass through the provided
     * filter. Uses brute-force method for finding nearest neighbor.
     *
     * @param location
     * @param worldUUID
     * @param filter
     * @return
     */
    public Optional<Graveyard> findNearestGraveyardWithFilter(Vector3i location, UUID worldUUID, Predicate<Map.Entry<String, GraveyardGroup>> filter) {

        List<Graveyard> candidates = getGraveyardGroupMap().entrySet().stream().filter(filter)
                .map(g -> g.getValue().findNearestGraveyard(location, worldUUID))
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        if (!candidates.isEmpty()) {
            return candidates.stream().min(Comparator.comparingInt(g -> location.distanceSquared(g.getLocation())));
        }

        return Optional.empty();
    }

    /**
     * Helper method to find the nearest graveyard in range. Only considers graveyard groups that pass through the provided
     * filter. Uses brute-force method for finding nearest neighbor.
     *
     * @param location
     * @param worldUUID
     * @param filter
     * @return
     */
    public Optional<Graveyard> findNearestGraveyardInRangeWithFilter(Vector3i location, UUID worldUUID, Predicate<Map.Entry<String, GraveyardGroup>> filter) {

        List<Graveyard> candidates = getGraveyardGroupMap().entrySet().stream().filter(filter)
                .map(g -> g.getValue().findNearestGraveyardInRange(location, worldUUID))
                .filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());

        if (!candidates.isEmpty()) {
            return candidates.stream().min(Comparator.comparingInt(g -> location.distanceSquared(g.getLocation())));
        }

        return Optional.empty();
    }
}
