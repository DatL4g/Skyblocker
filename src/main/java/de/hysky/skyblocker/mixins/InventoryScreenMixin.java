package de.hysky.skyblocker.mixins;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;

import de.hysky.skyblocker.config.SkyblockerConfigManager;
import de.hysky.skyblocker.skyblock.itemlist.recipebook.SkyblockRecipeBookWidget;
import de.hysky.skyblocker.utils.Utils;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.ingame.StatusEffectsDisplay;
import net.minecraft.client.gui.screen.recipebook.RecipeBookWidget;

@Mixin(InventoryScreen.class)
public abstract class InventoryScreenMixin {

    @ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/RecipeBookScreen;<init>(Lnet/minecraft/screen/AbstractRecipeScreenHandler;Lnet/minecraft/client/gui/screen/recipebook/RecipeBookWidget;Lnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/text/Text;)V"))
    private static RecipeBookWidget<?> skyblocker$replaceRecipeBook(RecipeBookWidget<?> original) {
        return SkyblockerConfigManager.get().general.itemList.enableItemList && Utils.isOnSkyblock() ? new SkyblockRecipeBookWidget() : original;
    }

    @ModifyArg(method = "getRecipeBookButtonPos", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/ScreenPos;<init>(II)V"), index = 0)
    private int skyblocker$moveButton(int x) {
        return Utils.isOnSkyblock() && SkyblockerConfigManager.get().uiAndVisuals.showEquipmentInInventory ? x + 21 : x;
    }

	@WrapWithCondition(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/ingame/StatusEffectsDisplay;drawStatusEffects(Lnet/minecraft/client/gui/DrawContext;IIF)V"))
	private boolean skyblocker$dontDrawStatusEffects(StatusEffectsDisplay statusEffectsDisplay, DrawContext context, int mouseX, int mouseY, float tickDelta) {
		return !(Utils.isOnSkyblock() && SkyblockerConfigManager.get().uiAndVisuals.hideStatusEffectOverlay);
	}
}
