/*
    HotbarStyle
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/HotbarStyle
    Licence: GNU LGPLv3
*/

package com.mclegoman.hotbarstyle.mixin;

import com.mclegoman.hotbarstyle.client.data.ClientData;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.hud.in_game.InGameHud;
import net.minecraft.client.option.AttackIndicator;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.random.RandomGenerator;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
	@Shadow @Final private RandomGenerator random;

	@Shadow protected abstract void drawHeart(GuiGraphics graphics, InGameHud.HeartType type, int x, int y, boolean blinking, boolean halfHeart, boolean bl);

	@Shadow private long heartJumpEndTick;

	@Shadow private int ticks;

	@Shadow private int lastHealthValue;

	@Shadow private long lastHealthCheckTime;

	@Shadow private int renderHealthValue;

	@Shadow private int scaledWidth;

	@Shadow private int scaledHeight;

	@Shadow @Final private static Identifier field_45323;

	@Shadow @Final private static Identifier field_45322;

	@Shadow @Final private static Identifier field_45321;

	@Shadow protected abstract void renderHealthBar(GuiGraphics graphics, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking);

	@Shadow protected abstract LivingEntity getRiddenEntity();

	@Shadow protected abstract int getHeartCount(LivingEntity entity);

	@Shadow @Final private static Identifier field_45324;

	@Shadow @Final private static Identifier field_45325;

	@Shadow @Final private static Identifier field_45326;

	@Shadow @Final private static Identifier field_45327;

	@Shadow @Final private static Identifier field_45328;

	@Shadow @Final private static Identifier field_45298;

	@Shadow protected abstract int getHeartRows(int heartCount);

	@Shadow @Final private static Identifier field_45299;

	@Shadow @Final private static Identifier field_45300;

	@Shadow @Final private static Identifier field_45314;

	@Shadow @Final private static Identifier field_45315;

	@Shadow public abstract void renderHotbarItem(GuiGraphics graphics, int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

	@Shadow @Final private static Identifier field_45312;

	@Shadow @Final private static Identifier field_45313;

	@Shadow @Final private static Identifier field_45311;

	@Shadow @Final private static Identifier field_45310;

	@Inject(method = "renderHotbar", at = @At("HEAD"), cancellable = true)
	private void hotbarstyle$renderHotbar(float tickDelta, GuiGraphics graphics, CallbackInfo ci) {
		if (ClientData.CLIENT.player != null) {
			PlayerEntity playerEntity = ClientData.CLIENT.player;
			if (playerEntity != null) {
				ItemStack itemStack = playerEntity.getOffHandStack();
				Arm arm = playerEntity.getMainArm().getOpposite();
				int i = this.scaledWidth / 2;
				graphics.getMatrices().push();
				graphics.getMatrices().translate(0.0F, 0.0F, -90.0F);
				graphics.drawGuiTexture(field_45310, i - 91, 0, 182, 22);
				graphics.drawGuiTexture(field_45311, i - 91 - 1 + playerEntity.getInventory().selectedSlot * 20, -1, 24, 23);
				if (!itemStack.isEmpty()) {
					if (arm == Arm.LEFT) {
						graphics.drawGuiTexture(field_45312, i - 91 - 29, -1, 29, 24);
					} else {
						graphics.drawGuiTexture(field_45313, i + 91, -1, 29, 24);
					}
				}

				graphics.getMatrices().pop();
				int l = 1;

				int m;
				int n;
				int o;
				for(m = 0; m < 9; ++m) {
					n = i - 90 + m * 20 + 2;
					o = 3;
					this.renderHotbarItem(graphics, n, o, tickDelta, playerEntity, playerEntity.getInventory().main.get(m), l++);
				}

				if (!itemStack.isEmpty()) {
					m = 3;
					if (arm == Arm.LEFT) {
						this.renderHotbarItem(graphics, i - 91 - 26, m, tickDelta, playerEntity, itemStack, l++);
					} else {
						this.renderHotbarItem(graphics, i + 91 + 10, m, tickDelta, playerEntity, itemStack, l++);
					}
				}

				RenderSystem.enableBlend();
				if (ClientData.CLIENT.options.getAttackIndicator().get() == AttackIndicator.HOTBAR) {
					float f = ClientData.CLIENT.player.getAttackCooldownProgress(0.0F);
					if (f < 1.0F) {
						n = 0;
						o = i + 91 + 6;
						if (arm == Arm.RIGHT) {
							o = i - 91 - 22;
						}

						int p = (int)(f * 19.0F);
						graphics.drawGuiTexture(field_45314, o, n, 18, 18);
						graphics.drawGuiTexture(field_45315, 18, 18, 0, 18 - p, o, n + 18 - p, 18, p);
					}
				}

				RenderSystem.disableBlend();
			}
			ci.cancel();
		}
	}
	@Inject(method = "renderExperienceBar", at = @At("HEAD"), cancellable = true)
	private void hotbarstyle$drawGuiTexture(GuiGraphics graphics, int x, CallbackInfo ci) {
		if (ClientData.CLIENT.player != null) {
			graphics.drawGuiTexture(new Identifier("hud/experience_bar_background"), x, 32 + 3, 182, 5);
			if ((int)(ClientData.CLIENT.player.experienceProgress * 183.0F) > 0) {
				graphics.drawGuiTexture(new Identifier("hud/experience_bar_progress"), 182, 5, 0, 0, x, 32 + 3, (int)(ClientData.CLIENT.player.experienceProgress * 183.0F), 5);
				graphics.drawCenteredShadowedText(ClientData.CLIENT.textRenderer, String.valueOf(ClientData.CLIENT.player.experienceLevel), ClientData.CLIENT.getWindow().getScaledWidth() / 2, 32, 8453920);
			}
			ci.cancel();
		}
	}
	@Inject(method = "renderStatusBars", at = @At("HEAD"), cancellable = true)
	private void renderStatusBars(GuiGraphics graphics, CallbackInfo ci) {
		if (ClientData.CLIENT.player != null) {
			int i = MathHelper.ceil(ClientData.CLIENT.player.getHealth());
			boolean bl = this.heartJumpEndTick > (long)this.ticks && (this.heartJumpEndTick - (long)this.ticks) / 3L % 2L == 1L;
			long l = Util.getMeasuringTimeMs();
			if (i < this.lastHealthValue && ClientData.CLIENT.player.timeUntilRegen > 0) {
				this.lastHealthCheckTime = l;
				this.heartJumpEndTick = this.ticks + 20;
			} else if (i > this.lastHealthValue && ClientData.CLIENT.player.timeUntilRegen > 0) {
				this.lastHealthCheckTime = l;
				this.heartJumpEndTick = this.ticks + 10;
			}

			if (l - this.lastHealthCheckTime > 1000L) {
				this.renderHealthValue = i;
				this.lastHealthCheckTime = l;
			}

			this.lastHealthValue = i;
			int j = this.renderHealthValue;
			this.random.setSeed(this.ticks * 312871L);
			HungerManager hungerManager = ClientData.CLIENT.player.getHungerManager();
			int k = hungerManager.getFoodLevel();
			int m = this.scaledWidth / 2 - 91;
			int n = this.scaledWidth / 2 + 91;
			int o = 39;
			float f = Math.max((float)ClientData.CLIENT.player.getAttributeValue(EntityAttributes.GENERIC_MAX_HEALTH), (float)Math.max(j, i));
			int p = MathHelper.ceil(ClientData.CLIENT.player.getAbsorptionAmount());
			int q = MathHelper.ceil((f + (float)p) / 2.0F / 10.0F);
			int r = Math.max(10 - (q - 2), 3);
			int s = o - (q - 1) * r - 10;
			int t = o - 10;
			int u = ClientData.CLIENT.player.getArmor();
			int v = -1;
			if (ClientData.CLIENT.player.hasStatusEffect(StatusEffects.REGENERATION)) {
				v = this.ticks % MathHelper.ceil(f + 5.0F);
			}

			ClientData.CLIENT.getProfiler().push("armor");

			int x;
			for(int w = 0; w < 10; ++w) {
				if (u > 0) {
					x = m + w * 8;
					if (w * 2 + 1 < u) {
						graphics.drawGuiTexture(field_45323, x, s, 9, 9);
					}

					if (w * 2 + 1 == u) {
						graphics.drawGuiTexture(field_45322, x, s, 9, 9);
					}

					if (w * 2 + 1 > u) {
						graphics.drawGuiTexture(field_45321, x, s, 9, 9);
					}
				}
			}

			ClientData.CLIENT.getProfiler().swap("health");
			this.renderHealthBar(graphics, ClientData.CLIENT.player, m, 25, r, v, f, i, j, p, bl);
			LivingEntity livingEntity = this.getRiddenEntity();
			x = this.getHeartCount(livingEntity);
			int y;
			int z;
			int aa;
			if (x == 0) {
				ClientData.CLIENT.getProfiler().swap("food");

				for(y = 0; y < 10; ++y) {
					z = o;
					Identifier identifier;
					Identifier identifier2;
					Identifier identifier3;
					if (ClientData.CLIENT.player.hasStatusEffect(StatusEffects.HUNGER)) {
						identifier = field_45324;
						identifier2 = field_45325;
						identifier3 = field_45326;
					} else {
						identifier = field_45327;
						identifier2 = field_45328;
						identifier3 = field_45298;
					}

					aa = n - y * 8 - 9;
					graphics.drawGuiTexture(identifier, aa, 25, 9, 9);
					if (y * 2 + 1 < k) {
						graphics.drawGuiTexture(identifier3, aa, 25, 9, 9);
					}

					if (y * 2 + 1 == k) {
						graphics.drawGuiTexture(identifier2, aa, 25, 9, 9);
					}
				}

				t -= 10;
			}

			ClientData.CLIENT.getProfiler().swap("air");
			y = ClientData.CLIENT.player.getMaxAir();
			z = Math.min(ClientData.CLIENT.player.getAir(), y);
			if (ClientData.CLIENT.player.isSubmergedIn(FluidTags.WATER) || z < y) {
				int ac = MathHelper.ceil((double)(z - 2) * 10.0 / (double)y);
				int ad = MathHelper.ceil((double)z * 10.0 / (double)y) - ac;

				for(aa = 0; aa < ac + ad; ++aa) {
					if (aa < ac) {
						graphics.drawGuiTexture(field_45299, n - aa * 8 - 9, 15, 9, 9);
					} else {
						graphics.drawGuiTexture(field_45300, n - aa * 8 - 9, 15, 9, 9);
					}
				}
			}

			ClientData.CLIENT.getProfiler().pop();
			ci.cancel();
		}
	}
}
