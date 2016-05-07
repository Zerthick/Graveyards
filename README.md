# Graveyards
Graveyards is a simple spawnpoint defining plugin. When players die, they respawn at the nearest graveyard.

You can check out the Graveyards Sponge Forum Post [here](https://forums.spongepowered.org/t/wip-graveyards-v0-1-0-pre-defined-spawnpoints-for-players/9575 "Graveyards Sponge Forum Post")!
 
##Commands
```
/gy - Displays version information about the Graveyards Plugin. (Aliases: gy, graveyard)
/gy create <name> [world] [x, y, z] [x, y, z] - Creates a graveyard with the given name at the provided world, location and orientaiton, or your current world, location, and orientation if none are provided. (Aliases: mk, create, add)
/gy destroy <name> [world] - Destroys a graveyard with the given name in the provided world or your current world if none is provided. (Aliases: rm, destroy, remove)
/gy list [world] - Lists all graveyards in the provided world or your current world if none is provided. (Aliases: ls, list)
/gy nearest [world] [x, y, z] - Identifies the nearest graveyard from the provided world and location or your current world and location if neither is provided. (Aliases: fd, nearest, closest)
/gy teleport <name> [world] - Teleports you to the graveyard with the given name at the provided world or your current world if none is provided. (Aliases: tp, teleport)
/gy setMessage <name> [world] <message> - Sets the welcome message of the graveyard with he given name at the provided world or your current world if none is provided. (Aliases: sm, setMessage)
```

##Permissions
```
graveyards.command.help
graveyards.command.create
graveyards.command.destroy
graveyards.command.list
graveyards.command.nearest
graveyards.command.teleport
graveyards.command.set.message
```
