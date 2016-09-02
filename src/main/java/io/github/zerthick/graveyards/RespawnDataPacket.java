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

package io.github.zerthick.graveyards;

import com.flowpowered.math.vector.Vector3d;
import com.flowpowered.math.vector.Vector3i;
import org.spongepowered.api.text.Text;

public class RespawnDataPacket {

    private final Text respawnMessage;
    private final Vector3d respawnRotation;
    private final Vector3i respawnLocation;

    public RespawnDataPacket(Text respawnMessage, Vector3d respawnRotation, Vector3i respawnLoaction) {
        this.respawnMessage = respawnMessage;
        this.respawnRotation = respawnRotation;
        this.respawnLocation = respawnLoaction;
    }

    public Text getRespawnMessage() {
        return respawnMessage;
    }

    public Vector3d getRespawnRotation() {
        return respawnRotation;
    }

    public Vector3i getRespawnLocation() {
        return respawnLocation;
    }
}
