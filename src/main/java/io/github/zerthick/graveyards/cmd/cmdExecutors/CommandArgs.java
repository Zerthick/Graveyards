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

package io.github.zerthick.graveyards.cmd.cmdExecutors;

import org.spongepowered.api.text.LiteralText;
import org.spongepowered.api.text.Text;

public class CommandArgs {
    public static final LiteralText NAME = Text.of("Name");
    public static final LiteralText WORLD = Text.of("World");
    public static final LiteralText LOCATION = Text.of("Location");
    public static final LiteralText ROTATION = Text.of("Rotation");
    public static final LiteralText DISTANCE = Text.of("Distance");
    public static final LiteralText MESSAGE = Text.of("Message");
    public static final LiteralText GROUP = Text.of("Group");
}
