package net.sirplop.aetherworks.api.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.sirplop.aetherworks.api.power.IHeatCapability;

public class AetherworksCapabilities {

    public static final Capability<IHeatCapability> HEAT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
}
