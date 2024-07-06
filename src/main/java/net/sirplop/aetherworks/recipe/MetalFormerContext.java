package net.sirplop.aetherworks.recipe;

import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class MetalFormerContext extends RecipeWrapper {

    public IFluidHandler fluids;
    public int temperature;

    public MetalFormerContext(IItemHandlerModifiable inv, IFluidHandler fluids, int temperature) {
        super(inv);
        this.fluids = fluids;
        this.temperature = temperature;
    }
}
