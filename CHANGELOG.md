Changelog - Aetherworks Refracted
=============================
1.20.1-1.0.5
-----------------------------
_Additions_

- Added Aetherium Glass, which is wither-proof and blast-proof. Also comes in a borderless form!
- Added a second page to the Aetherium Crown codex entry describing how to imbue vessel gems.
- Added a config option to specify dimensions that are always considered "moonlit", aka triggers Moonsnare containers and Aetheric tools autorepair/fill.

_Changes_

- Increased default aetheric strength of Moonsnare containers and Aetheric tools from 2 to 3.
- Increased span of time that is considered moonlit from 15000-21000 to 12786-23216 (lunar zeniths).
- The End is now considered moonlit at all times.
- Retextured all tool parts, the aetherium crown item, and aetherium gem.
- Retextured Aetherium-Infused Suevite, Aetherium Focusing Matrix, Block of Aetherium Shards, and the Block of Aetherium.

_Fixes_

- Moonsnare Jars and Aetherium Tools not properly checking the time when they can repair.
- Forge Dial was not tagged as mineable correctly, making it impossible to pick up.
- Moonlight Harvesters shared a frame counter, making their animations speed up the more existed.
- Mostly fixed chaining Moonlight Harvesters together causing runes to render over one another.
- Typo in Shovel of the Timeless Cascades' codex entry.

1.20.1-1.0.4
-----------------------------
_Additions_

_Changes_

- Made many blocks sounds like other Embers blocks.
- Reordered the creative menu.
- Retextured Aetherium Lens.
- Remodeled and retextured the forge core, heater, and cooler.
- The Crossbow of the Shattered Reflection now, when hitting the same mob more than once in an attack, only spawns 1 projectile if using the diffraction barrel.

_Fixes_

- Problem with network serialization for Tool Forge recipes.
- The Crossbow of the Shattered Reflection could infinitely chain between 2 entities while trying to hit a third, resulting in a crash.

1.20.1-1.0.3
-----------------------------
_Additions_

- Added the Moonsnare Jar, Cartridge, and Bulb, which have less capacity than their mantle counterparts, but slowly generate Ember in moonlight.
- Animated the Aetherium Crown model texture.

_Changes_

- Changed name of "Heat Dial" to "Forge Dial" to better reflect its purpose (and to not be confused with augment Heat).
- Resprited Aetherium Plate.
- Changed codex category requirement for Reflections of the Sun from Wildfire Core to Dawnstone.
- Moved Tuning Cylinder codex entry to the weapon augments section.

_Fixes_

- Moonlight Harvester's rune overlay can stop rendering at certain angles while still visible.
- Aetherium Forge overlay rendering wrong when GeckoLib is installed.
- The aetherium shovels, axes, and crossbows now properly have an ember resonance value.
- The codex entry tab was slightly misaligned.
- Corrected some capitalization in the codex to better align with the rest of the codex's formatting.

1.20.1-1.0.2
-----------------------------
_Additions_

- Added a startup message.

_Changes_

- Metal Former's processing particles now change from Ember colored to Aetherium colored as the craft progresses.
- Metal Former recipes can now have different crafting times.
- Changed out block harvest functions for non-recursive ones.
- Remade the Moonlight Harvester rune textures.
- Modified Moonlight Amplifier and Refraction Prism textures.
- Improved Aetherium Forge overlay animation.

_Fixes_

- Fixed crash when putting certain items in the metal former.
- Fixed crash when using the Iceberg mod.
- Fixed missing picked-block aetherium forge texture.
- Fixed typo in codex entry for Axe of the Sonorous Archives.
- Fixed Aetherium-Infused Suevite having the wrong sound type.

1.20.1-1.01b
-----------------------------
Removed debug messages that shouldn't have been there.

1.20.1-1.0.1
-----------------------------
_Additions_

- This changelog. Fascinating.

_Changes_

- Updated to Embers 1.3.9
- Made the Aetheriometer use the new gauge tags added in Embers 1.3.9
- Changed gradle versioning for Embers dependency.

_Fixes_

- Fixed meteorite worldgen not detecting water 100% of the time. Now it's, like, 99.9% of the time.
- Fixed typo in mods.toml

1.20.1-1.0.0
-----------------------------
__Initial Release__


Version
-----------------------------
_Additions_

_Changes_

_Fixes_