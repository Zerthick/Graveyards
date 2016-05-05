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


public class Graveyard {

    private final String name;
    private final Vector3i location;
    private final Vector3d rotation;
    private Text message;
    private int DiscoverDistance;

    public Graveyard(String name, Vector3i location, Vector3d rotation, Text message, int discoverDistance) {
        this.name = name;
        this.location = location;
        this.rotation = rotation;
        this.message = message;
        DiscoverDistance = discoverDistance;
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
        return DiscoverDistance;
    }

    public void setDiscoverDistance(int discoverDistance) {
        DiscoverDistance = discoverDistance;
    }
}
