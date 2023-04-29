package funskydev.pianocraft.screen;

import funskydev.pianocraft.PCMain;
import funskydev.pianocraft.registry.PCScreenHandlers;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.screen.slot.Slot;
import org.jetbrains.annotations.Nullable;

public class PianoScreenHandler extends ScreenHandler {

    private final Inventory inventory;

    public PianoScreenHandler(int syncId) {
        this(syncId, new SimpleInventory(1));
    }

    public PianoScreenHandler(int syncId, Inventory inventory) {

        super(PCScreenHandlers.PIANO_SCREEN_HANDLER, syncId);
        this.inventory = inventory;

        //this.addSlot(new Slot(inventory, 0, 0, 0));

    }

    @Override
    public boolean onButtonClick(PlayerEntity player, int id) {

        PCMain.LOGGER.info("Button clicked (id: " + id + ")");

        return super.onButtonClick(player, id);
    }

    @Override
    public ItemStack quickMove(PlayerEntity player, int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canUse(PlayerEntity player) {
        return this.inventory.canPlayerUse(player);
    }

    public ItemStack getSoundItemStack() {
        return this.inventory.getStack(0);
    }

}
