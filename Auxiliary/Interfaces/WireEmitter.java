/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2017
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ElectriCraft.Auxiliary.Interfaces;

import net.minecraftforge.common.util.ForgeDirection;

public interface WireEmitter extends WireTerminus {

	public abstract int getGenVoltage();

	public abstract int getGenCurrent();

	public boolean canEmitPowerToSide(ForgeDirection dir);

	public abstract boolean canEmitPower();

}
