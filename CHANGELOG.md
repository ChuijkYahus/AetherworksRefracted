Changelog - Aetherworks Refracted
=============================
1.20.1-1.1.2
-----------------------------
_Additions_

_Changes_

_Fixes_

- Forge Heaters and Coolers weren't properly consuming fluids, meaning they didn't require them to operate.

1.20.1-1.1.1
-----------------------------
_Additions_

- Added a melting recipe for the Aetherium Shard Block to align with other raw ore melting recipes.

_Changes_

- Boosted diamond and echo shard drop rate from deepslate geodes.
- Increased Moongaze effect strength from 1 to 2 for aetheric crossbows.
- Fixed up multishot functionality on aetheric crossbows, and enabled the enchantment to be applied to them.
- Aetherium tools now apply Moongaze 2 when hitting mobs. The Hoe of the Hyalopterous Shepherd does not apply Moongaze to animals.

_Fixes_

- The Aetherium Forge now sends block updates from the top forge block, making dials update their comparator outputs correctly.
- The Tuning Cylinder now checks biome tags correctly - it now correctly drops the appropriate geodes.

1.20.1-1.1.0
-----------------------------
_Additions_

- Added the Hoe of the Villatic Harvest, an aetherium hoe that excels at farming crops.
- Added the Hoe of the Hyalopterous Shepherd, an aetherium hoe that excels at animal handling.
- Added the Agrarian Liners augment, which prevents you from trampling farmland when applied to boots.
- Added Suevite Small Bricks block set.
- Added Suevite Small Tile block set.
- Added the "Same Block" config list, which allows certain aetherium tools to treat the specified blocks like they are others, I.E. treating Azalea Leaves as if they were Flowering Azalea Leaves, and vice versa, for vein-mining with the Axe of the Sonorous Archive.

_Changes_

- Aetherium items no longer change modes via sneak+use. They now change modes via keybind (default: V).
- Added a keybind reminder to the tooltips of aetherium items with different modes.
- Added overlay messages when changing modes of aetherium items.
- All recipes now use item tags as input where applicable.
- Added tags to the fluids.
- The forge heater and cooler now use tags to determine what blocks and fluids are allowed beneath them.
- All molten metals are now allowed under the forge heater.
- Mixing recipes now use the minimum quantity of fluid, like normal Embers recipes.
- Tweaked Suevite Tile texture to better tile.

_Fixes_

- Suevite now properly drops cobblestone when not silk touched, and itself when silk touched.
- Aetherium tools now have the proper "perform actions", so they are counted as the appropriate tool type by various mods.
- Water now properly flows out of waterlogged blocks.

1.20.1-1.0.7
-----------------------------
_Additions_

- Replaced the Aetherium Forge with a proper Embers-style multiblock machine version of it. Break your old Aetherium Forge Core to receive the new forge (and enjoy the free dawnstone blocks I guess).
- Added Suevite Big Tile block set.

_Changes_

- Combined the Forge Cooler, Heater, and Vent codex entries.
- Added a page to Aetherium Purification detailing suevite renewability.
- Assorted codex entry tweaks.
- Remodeled and retextured Forge Heat Vent, and changed its crafting recipe.
- Adjusted how several blocks appear in the player's hand. No more floating things!
- Added Suevite Bricks to the #minecraft:stone_bricks tag.

_Fixes_

- Moonsnare Bulb making the reequip sound every time its data changes.
- Suevite dropping suevite cobblestone when mined with silk touch.

1.20.1-1.0.6
-----------------------------
_Additions_

- Added the Volant Calcifier augment, which makes your ember projectiles force flying enemies to the ground.
- Added Seething Aetherium, a horrible fluid that kills everything it touches, and is, in fact, several OSHA violations.
- Added Tinker Lens info to the Aetherium Anvil and Tool Forge.
- Moved some entries around the codex and added new entries.

_Changes_

- The Tuning Cylinder augment now displays its level.
- Retextured the Tuning Cylinder.
- Changed the Tinker Lens info for the Metal Former.
- The Metal Former codex now specifies that you can use a bucket on the metal former to add and remove fluids.
- Tweaked the Tool Forge and Ecclesia Pearls codex entries.
- Changed the flow rate and distance of Alchemic Precursor.

_Fixes_

- The Tool Forge was flipped when facing north or south.
- More codex typos.
- Liquids did not have their renderers set correctly.
- Aetherium Forge was not serializing the structure's validity correctly.
- Aetherium Forge losing a little bit of heat whenever it was reloaded.
- Remixed forge_groan.ogg from stereo to mono so the sound will fade with distance.
- Fixed dying to Moongaze saying the player killed themself.

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