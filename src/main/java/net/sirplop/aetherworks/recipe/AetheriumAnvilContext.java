package net.sirplop.aetherworks.recipe;

import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class AetheriumAnvilContext extends RecipeWrapper {

    public int temperature;

    public AetheriumAnvilContext(IItemHandlerModifiable inv, int temperature) {
        super(inv);
        this.temperature = temperature;
    }
}
