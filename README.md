# Graveyards
Graveyards is a simple spawnpoint defining plugin. When players die, they respawn at the nearest graveyard.
 
## Commands
`/gy` - Displays version information about the Graveyards Plugin. (Aliases: gy, graveyard)  
`/gy create <name> [group] [world] [x, y, z] [x, y, z]` - Creates a graveyard with the given name in the given group, or the default group if none is provided, at the provided world, location and orientation, or your current world, location, and orientation if none are provided. (Aliases: mk, create, add)  
`/gy destroy <name> [group] [world]` - Destroys a graveyard with the given name in the given group, or the default group if none is provided, in the provided world or your current world if none is provided. (Aliases: rm, destroy, remove)  
`/gy creategroup <name>` - Creates a new Graveyard Group with the provided name. (Aliases: mkgr, creategroup, addgroup)  
`/gy destroygroup <name>` - Destroys a Graveyard Group with the provided name, in additon to **all graveyards in the group!** (Aliases: rmgr, destroygroup, removegroup)  
`/gy list [world]` - Lists all graveyards in the provided world or your current world if none is provided. (Aliases: ls, list)  
`/gy nearest [group] [world] [x, y, z]` - Identifies the nearest graveyard in the given group, or all groups if none is provided, from the provided world and location or your current world and location if neither is provided. (Aliases: fd, nearest, closest)  
`/gy teleport <name> [group] [world]` - Teleports you to the graveyard with the given name in the given group, or the default group if none is provided, at the provided world or your current world if none is provided. (Aliases: tp, teleport)  
`/gy setMessage <name> <group> [world] <message>` - Sets the welcome message of the graveyard with he given name in the given group at the provided world or your current world if none is provided. (Aliases: sm, setMessage) 
`/gy setRange <name> <group> [world] <range>` - Sets the respawn range of the graveyard with he given name in the given group at the provided world or your current world if none is provided (set to -1 for infinte range). (Aliases: sr, setRange) 

## Permissions
`graveyards.commands.info`  
`graveyards.commands.create`  
`graveyards.commands.destroy`  
`graveyards.commands.group.create`  
`graveyards.commands.group.destroy`  
`graveyards.commands.list`  
`graveyards.commands.nearest`  
`graveyards.commands.teleport`  
`graveyards.commands.set.message`  
`graveyards.commands.set.range`  

## Graveyard Groups
As of Graveyards v2.2.0, Graveyards are now contained within Graveyard Groups, there is a default group which always exists and you can create new groups with the `creategroup` command. Graveyards can be assigned a group when first created in the `create` command. All players have acess to the graveyards in the default group. In order for a player to be able to respawn in a graveyard outside the default group, they must have the permision `graveyards.respawn.<group_name>` where `<group_name>` is the name of the group in **all lowercase!**

## Support Me
I will **never** charge money for the use of my plugins, however they do require a significant amount of work to maintain and update. If you'd like to show your support and buy me a cup of tea sometime (I don't drink that horrid coffee stuff :P) you can do so [here](https://www.paypal.me/zerthick)
