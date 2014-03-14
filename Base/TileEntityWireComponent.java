/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2014
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.ElectriCraft.Base;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.common.ForgeDirection;

public abstract class TileEntityWireComponent extends WiringTile {

	private ForgeDirection facing;

	@Override
	public final boolean canNetworkOnSide(ForgeDirection dir) {
		return dir == this.getFacing() || dir == this.getFacing().getOpposite();
	}

	public final ForgeDirection getFacing() {
		return facing != null ? facing : ForgeDirection.EAST;
	}

	public final void setFacing(ForgeDirection dir) {
		facing = dir;
	}

	public final AxisAlignedBB getAABB() {
		int miny = 0;
		float maxy = this.getHeight();
		float w = this.getWidth()/2F;
		ForgeDirection dir = this.getFacing();
		float maxx = dir.offsetX != 0 ? 1 : 0.5F+w;
		float minx = dir.offsetX != 0 ? 0 : 0.5F-w;
		float maxz = dir.offsetZ != 0 ? 1 : 0.5F+w;
		float minz = dir.offsetZ != 0 ? 0 : 0.5F-w;
		return AxisAlignedBB.getAABBPool().getAABB(xCoord+minx, yCoord+miny, zCoord+minz, xCoord+maxx, yCoord+maxy, zCoord+maxz);
	}

	@Override
	protected void writeSyncTag(NBTTagCompound NBT)
	{
		super.writeSyncTag(NBT);

		NBT.setInteger("face", this.getFacing().ordinal());
	}

	@Override
	protected void readSyncTag(NBTTagCompound NBT)
	{
		super.readSyncTag(NBT);

		this.setFacing(dirs[NBT.getInteger("face")]);
	}

	public abstract float getHeight();
	public abstract float getWidth();

}
