/*
    Mod Template
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Mod-Template
    Licence: GNU LGPLv3
*/

package com.mclegoman.mod_template.mixin;

import com.mclegoman.mod_template.client.data.ClientData;
import com.mclegoman.mod_template.common.data.Data;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
	@Inject(method = "render", at = @At("TAIL"))
	public void mod_template$render(GuiGraphics graphics, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		graphics.drawShadowedText(ClientData.CLIENT.textRenderer, Data.MOD_TEMPLATE_VERSION.getName() + " " + Data.MOD_TEMPLATE_VERSION.getFriendlyString(), 2, 2, 0xFFFFFF);
	}
}
