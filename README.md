# Graveyards
Graveyards is a simple spawnpoint defining plugin. When players die, they respawn at the nearest graveyard.

**Note:** Graveyards currently **does not work** and is waiting on the DestructEntityEvent.Death event to be implemented.

You can check out the Graveyards Sponge Forum Post [here](https://forums.spongepowered.org/t/wip-graveyards-v0-1-0-pre-defined-spawnpoints-for-players/9575 "Graveyards Sponge Forum Post")!
 
##Commands
```
/gy - Displays version information about the Graveyards Plugin.
/gy create <name> [world] [x, y, z] - Creates a graveyard with the given name at the provided world and location or your current world and location if neither is provided.
/gy destroy <name> [world] - Destroys a graveyard with the given name in the provided world or your current world if none is provided.
/gy list [world] - Lists all graveyards in the provided world or your current world if none is provided.
```

##Permissions
```
graveyards.command
graveyards.command.create
graveyards.command.destroy
graveyards.command.list
```
