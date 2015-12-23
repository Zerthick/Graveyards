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

package io.github.zerthick.graveyards.cmd.cmdExecutors;

import io.github.zerthick.graveyards.GraveyardsMain;
import io.github.zerthick.graveyards.utils.GraveyardManager;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.plugin.PluginContainer;

public abstract class AbstractCmdExecutor implements CommandExecutor {

    protected PluginContainer container;
    protected GraveyardsMain plugin;
    protected GraveyardManager manager;

    public AbstractCmdExecutor(PluginContainer pluginContainer) {
        super();
        container = pluginContainer;
        plugin = (container.getInstance().get() instanceof GraveyardsMain ? (GraveyardsMain) container.getInstance().get() : null);
        manager = plugin.getGraveyardManager();
    }

}
