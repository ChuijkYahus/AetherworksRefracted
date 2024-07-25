package net.sirplop.aetherworks.capabilities;

import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.sirplop.aetherworks.api.capabilities.IAetheriometerCap;
import net.sirplop.aetherworks.api.capabilities.IHeatCapability;

public class AWCapabilities {

    public static final Capability<IHeatCapability> HEAT_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
    public static final Capability<IAetheriometerCap> AETHERIOMETER_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {});
}
