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

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.text.Text;

/**
 * POJO for storing graveyard data
 */
public class Graveyard {

    private final String name;
    private final Vector3i location;
    private final Vector3d rotation;
    private Text message;
    private int discoverDistance;
    private int range;

    /**
     * Creates a new Graveyard
     *
     * @param name the name of the Graveyard
     * @param location the location of the Graveyard
     * @param rotation the rotation to Spawn the Player
     * @param message the message to send the Player when spawning
     * @param discoverDistance the minimum distance the player must be in order to discover the graveyard
     * @param range the maximum range at which the player will spawn in the graveyard
     */
    public Graveyard(String name, Vector3i location, Vector3d rotation, Text message, int discoverDistance, int range) {
        this.name = name;
        this.location = location;
        this.rotation = rotation;
        this.message = message;
        this.discoverDistance = discoverDistance;
        this.range = range;
    }

    public String getName() {
        return name;
    }

    public Vector3i getLocation() {
        return location;
    }

    public Vector3d getRotation() {
        return rotation;
    }

    public Text getMessage() {
        return message;
    }

    public void setMessage(Text message) {
        this.message = message;
    }

    public int getDiscoverDistance() {
        return discoverDistance;
    }

    public void setDiscoverDistance(int discoverDistance) {
        this.discoverDistance = discoverDistance;
    }

    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public boolean isInRangeDistanceSquared(int distanceSquared) {
        return (range == -1) || (distanceSquared < (range * range));
    }
}
