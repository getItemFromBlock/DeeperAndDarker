package com.kyanite.deeperdarker.mixin;

import com.kyanite.deeperdarker.content.DDItems;
import com.kyanite.deeperdarker.content.DDSounds;
import com.kyanite.deeperdarker.content.items.SculkTransmitterItem;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@SuppressWarnings("unused")
@Mixin(value = { AbstractFurnaceMenu.class, BeaconMenu.class, BrewingStandMenu.class, CartographyTableMenu.class, ChestMenu.class, CraftingMenu.class, DispenserMenu.class, EnchantmentMenu.class, GrindstoneMenu.class, HopperMenu.class, ItemCombinerMenu.class, LoomMenu.class, ShulkerBoxMenu.class, StonecutterMenu.class })
public class ContainerMenuMixin {
    @Inject(method = "stillValid", at = @At("HEAD"), cancellable = true)
    public void stillValid(Player player, CallbackInfoReturnable<Boolean> cir) {
        ItemStack transmitter = ItemStack.EMPTY;
        if(player.getMainHandItem().is(DDItems.SCULK_TRANSMITTER.get()) && SculkTransmitterItem.isLinked(player.getMainHandItem(), player.level())) {
            transmitter = player.getMainHandItem();
        } else {
            for(ItemStack stack : player.getInventory().items) {
                if(stack.is(DDItems.SCULK_TRANSMITTER.get()) && SculkTransmitterItem.isLinked(stack,  player.level())) {
                    transmitter = stack;
                    break;
                }
            }
        }

        if(!transmitter.isEmpty()) {
            CompoundTag tag = transmitter.getTag();
            String block = tag.getString("block");
            String dim = tag.getString("dimension");
            int[] pos = tag.getIntArray("blockPos");
            BlockPos linkedPos = new BlockPos(pos[0], pos[1], pos[2]);
            Level lvl = player.level().getServer().getLevel(ResourceKey.create(Registries.DIMENSION, new ResourceLocation(dim)));

            if(lvl.getBlockState(linkedPos).getBlock().getDescriptionId().equals(block)) {
                cir.setReturnValue(true);
                cir.cancel();
            } else {
                SculkTransmitterItem.actionBarMessage(player, "not_found", DDSounds.TRANSMITTER_ERROR);
                SculkTransmitterItem.formConnection(player.level(), transmitter, null);
            }
        }
    }
}
