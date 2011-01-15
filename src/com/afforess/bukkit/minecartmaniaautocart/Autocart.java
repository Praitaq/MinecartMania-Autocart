package com.afforess.bukkit.minecartmaniaautocart;

import java.util.Calendar;

import org.bukkit.Entity;
import org.bukkit.Vector;

import com.afforess.bukkit.minecartmaniacore.DirectionUtils;
import com.afforess.bukkit.minecartmaniacore.MinecartManiaMinecart;

public class Autocart {

	public static final int trackMetadata[][][] = { { { 0, 0, -1 }, { 0, 0, 1 } }, { { -1, 0, 0 }, { 1, 0, 0 } }, { { -1, -1, 0 }, { 1, 0, 0 } }, { { -1, 0, 0 }, { 1, -1, 0 } }, { { 0, 0, -1 }, { 0, -1, 1 } }, { { 0, -1, -1 }, { 0, 0, 1 } }, { { 0, 0, 1 }, { 1, 0, 0 } }, { { 0, 0, 1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { -1, 0, 0 } }, { { 0, 0, -1 }, { 1, 0, 0 } } };

	public static Vector getMotionFromDirection(DirectionUtils.CompassDirection direction) {
		Vector vector = new Vector(0D, 0D, 0D);
		if (direction.equals(DirectionUtils.CompassDirection.NORTH)) {
			vector.setX(-0.4D);
			//System.out.println(DirectionUtils.CompassDirection.NORTH);
		}
		else if (direction.equals(DirectionUtils.CompassDirection.EAST)) {
			vector.setZ(-0.4D);
			//System.out.println(DirectionUtils.CompassDirection.EAST);
		}
		else if (direction.equals(DirectionUtils.CompassDirection.SOUTH)) {
			vector.setX(0.4D);
			//System.out.println(DirectionUtils.CompassDirection.SOUTH);
		}
		else if (direction.equals(DirectionUtils.CompassDirection.WEST)) {
			vector.setZ(0.4D);
			//System.out.println(DirectionUtils.CompassDirection.WEST);
		}
		return vector;
	}
	
	public static boolean doCooldown(MinecartManiaMinecart minecart) {
		Object data = minecart.getDataValue("PrevPassenger");
		if (data != null) {
			Entity prevPassenger = (Entity)data;
			//Passenger disembarked
			if (minecart.minecart.getPassenger() == null) {
				minecart.setDataValue("Cooldown", new Long(Calendar.getInstance().getTimeInMillis() + 3000));
				minecart.setDataValue("PrevPassenger", null);
			}
			//Changed passenger
			else if (!minecart.minecart.getPassenger().equals(prevPassenger)) {
				minecart.setDataValue("Cooldown", null );
				minecart.setDataValue("PrevPassenger", minecart.minecart.getPassenger());
			}
		}
		else if (data == null) {
			//New Passenger
			if (minecart.minecart.getPassenger() != null) {
				minecart.setDataValue("Cooldown", null);
				minecart.setDataValue("PrevPassenger", minecart.minecart.getPassenger());
			}
		}
		
		data = minecart.getDataValue("Cooldown");
		if (data != null) {
			long cooldown = ((Long)data).longValue();
			if (Calendar.getInstance().getTimeInMillis() > cooldown) {
				minecart.setDataValue("Cooldown", null);
			}
			else {
				minecart.stopCart();
				return true;
			}
		}
		return false;
	}
}
