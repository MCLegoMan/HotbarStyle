/*
    Mod Template
    Contributor(s): MCLegoMan
    Github: https://github.com/MCLegoMan/Mod-Template
    Licence: GNU LGPLv3
*/

package com.mclegoman.mod_template.common;

import com.mclegoman.mod_template.common.data.Data;
import com.mclegoman.releasetypeutils.common.version.Helper;
import org.quiltmc.loader.api.ModContainer;
import org.quiltmc.qsl.base.api.entrypoint.ModInitializer;

public class ModTemplate implements ModInitializer {
	@Override
	public void onInitialize(ModContainer mod) {
		Data.MOD_TEMPLATE_VERSION.sendToLog(Helper.LogType.INFO, "Initializing Common...");
	}
}
