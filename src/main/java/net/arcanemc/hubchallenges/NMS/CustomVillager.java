package net.arcanemc.hubchallenges.NMS;

import java.lang.reflect.Field;
import java.util.List;

import net.minecraft.server.v1_8_R3.EntityHuman;
import net.minecraft.server.v1_8_R3.EntityVillager;
import net.minecraft.server.v1_8_R3.PathfinderGoalLookAtPlayer;
import net.minecraft.server.v1_8_R3.PathfinderGoalSelector;
import net.minecraft.server.v1_8_R3.World;

public class CustomVillager extends EntityVillager {

	Field bk_;
	boolean bk;
	
	public CustomVillager(World world) {
		super(world);
		
        List goalB = (List)CustomEntity.getPrivateField("b", PathfinderGoalSelector.class, goalSelector); goalB.clear();
        List goalC = (List)CustomEntity.getPrivateField("c", PathfinderGoalSelector.class, goalSelector); goalC.clear();
        List targetB = (List)CustomEntity.getPrivateField("b", PathfinderGoalSelector.class, targetSelector); targetB.clear();
        List targetC = (List)CustomEntity.getPrivateField("c", PathfinderGoalSelector.class, targetSelector); targetC.clear();
        
        this.goalSelector.a(0, new PathfinderGoalLookAtPlayer(this, EntityHuman.class, 1.0F));
	}
	
	@Override
	public void g(double x, double y, double z) {
		return;
	}
	
	@Override
	public void makeSound(String s, float f, float f1) {
		return;
	}
}
