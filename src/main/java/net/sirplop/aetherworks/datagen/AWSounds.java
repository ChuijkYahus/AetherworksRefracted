package net.sirplop.aetherworks.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinition;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;
import net.sirplop.aetherworks.AWRegistry;
import net.sirplop.aetherworks.Aetherworks;

public class AWSounds extends SoundDefinitionsProvider {
    public AWSounds(PackOutput output, ExistingFileHelper helper) {
        super(output, Aetherworks.MODID, helper);
    }

    //this is just here so the class loads, nothing else needs to happen here
    public static void init() {}

    //Sounds
    public static final RegistryObject<SoundEvent> FORGE_GROAN = registerSoundEvent("block.forge_groan");

    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return AWRegistry.SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(Aetherworks.MODID, name)));
    }
    @Override
    public void registerSounds() {
        withSubtitle(FORGE_GROAN, definition().with(
                sound(resource("forge_groan"))));
    }


    public void withSubtitle(RegistryObject<SoundEvent> soundEvent, SoundDefinition definition) {
        add(soundEvent, definition.subtitle("subtitles." + Aetherworks.MODID + "." + soundEvent.getId().getPath()));
    }
    public ResourceLocation resource(String path) {
        return new ResourceLocation(Aetherworks.MODID, path);
    }
}
