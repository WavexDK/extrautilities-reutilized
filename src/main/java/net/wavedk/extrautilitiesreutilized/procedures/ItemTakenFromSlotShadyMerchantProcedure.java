package net.wavedk.extrautilitiesreutilized.procedures;

import net.wavedk.extrautilitiesreutilized.init.EuruModMenus;
import net.wavedk.extrautilitiesreutilized.init.EuruModItems;
import net.wavedk.extrautilitiesreutilized.EuruMod;

import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.Level;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.util.RandomSource;
import net.minecraft.util.Mth;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.network.chat.Component;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.component.DataComponents;

import java.util.Comparator;

public class ItemTakenFromSlotShadyMerchantProcedure {
	public static void execute(LevelAccessor world, double x, double y, double z, Entity entity, double slot) {
		if (entity == null)
			return;
		ItemStack rI = ItemStack.EMPTY;
		boolean isShady = false;
		Entity lEntity = null;
		if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu0 ? _menu0.getSlots().get((int) slot).getItem() : ItemStack.EMPTY).getItem() == Items.NETHER_STAR) {
			rI = new ItemStack(EuruModItems.SHADY_ITEM.get()).copy();
			{
				final String _tagName = "itemNumber";
				final double _tagValue = 0;
				CustomData.update(DataComponents.CUSTOM_DATA, rI, tag -> tag.putDouble(_tagName, _tagValue));
			}
			rI.set(DataComponents.CUSTOM_NAME, Component.literal("\u00A7fNether Star (FAKE)"));
			rI.enchant(world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.AQUA_AFFINITY), 1);
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack5 = rI.copy();
				_setstack5.setCount(1);
				_menu.getSlots().get((int) slot).set(_setstack5);
				ItemStack _setstack8 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu7 ? _menu7.getSlots().get(0).getItem() : ItemStack.EMPTY).copy();
				_setstack8.setCount(getAmountInGUISlot(entity, 0) - 1);
				_menu.getSlots().get(0).set(_setstack8);
				_player.containerMenu.broadcastChanges();
			}
		} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu9 ? _menu9.getSlots().get((int) slot).getItem() : ItemStack.EMPTY).getItem() == Items.DIAMOND) {
			rI = new ItemStack(EuruModItems.SHADY_ITEM.get()).copy();
			{
				final String _tagName = "itemNumber";
				final double _tagValue = 1;
				CustomData.update(DataComponents.CUSTOM_DATA, rI, tag -> tag.putDouble(_tagName, _tagValue));
			}
			rI.set(DataComponents.CUSTOM_NAME, Component.literal("\u00A7fDiamond (FAKE)"));
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack13 = rI.copy();
				_setstack13.setCount(1);
				_menu.getSlots().get((int) slot).set(_setstack13);
				ItemStack _setstack16 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu15 ? _menu15.getSlots().get(0).getItem() : ItemStack.EMPTY).copy();
				_setstack16.setCount(getAmountInGUISlot(entity, 0) - 2);
				_menu.getSlots().get(0).set(_setstack16);
				_player.containerMenu.broadcastChanges();
			}
		} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu17 ? _menu17.getSlots().get((int) slot).getItem() : ItemStack.EMPTY).getItem() == Items.NETHERITE_INGOT) {
			rI = new ItemStack(EuruModItems.SHADY_ITEM.get()).copy();
			{
				final String _tagName = "itemNumber";
				final double _tagValue = 2;
				CustomData.update(DataComponents.CUSTOM_DATA, rI, tag -> tag.putDouble(_tagName, _tagValue));
			}
			rI.set(DataComponents.CUSTOM_NAME, Component.literal("\u00A7fNetherite Ingot (FAKE)"));
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack21 = rI.copy();
				_setstack21.setCount(1);
				_menu.getSlots().get((int) slot).set(_setstack21);
				ItemStack _setstack24 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu23 ? _menu23.getSlots().get(0).getItem() : ItemStack.EMPTY).copy();
				_setstack24.setCount(getAmountInGUISlot(entity, 0) - 3);
				_menu.getSlots().get(0).set(_setstack24);
				_player.containerMenu.broadcastChanges();
			}
		} else if ((entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu25 ? _menu25.getSlots().get((int) slot).getItem() : ItemStack.EMPTY).getItem() == Items.NETHERITE_PICKAXE) {
			rI = new ItemStack(EuruModItems.SHADY_ITEM.get()).copy();
			{
				final String _tagName = "itemNumber";
				final double _tagValue = 3;
				CustomData.update(DataComponents.CUSTOM_DATA, rI, tag -> tag.putDouble(_tagName, _tagValue));
			}
			rI.set(DataComponents.CUSTOM_NAME, Component.literal("\u00A7fNetherite Pickaxe (FAKE)"));
			if (entity instanceof Player _player && _player.containerMenu instanceof EuruModMenus.MenuAccessor _menu) {
				ItemStack _setstack29 = rI.copy();
				_setstack29.setCount(1);
				_menu.getSlots().get((int) slot).set(_setstack29);
				ItemStack _setstack32 = (entity instanceof Player _plrSlotItem && _plrSlotItem.containerMenu instanceof EuruModMenus.MenuAccessor _menu31 ? _menu31.getSlots().get(0).getItem() : ItemStack.EMPTY).copy();
				_setstack32.setCount(getAmountInGUISlot(entity, 0) - 4);
				_menu.getSlots().get(0).set(_setstack32);
				_player.containerMenu.broadcastChanges();
			}
		}
		{
			final Vec3 _center = new Vec3(x, y, z);
			for (Entity entityiterator : world.getEntitiesOfClass(Entity.class, new AABB(_center, _center).inflate(2 / 2d), e -> true).stream().sorted(Comparator.comparingDouble(_entcnd -> _entcnd.distanceToSqr(_center))).toList()) {
				if (entityiterator instanceof Villager) {
					lEntity = entityiterator;
					isShady = lEntity instanceof net.minecraft.world.entity.npc.Villager villager && villager.getVillagerData().getProfession() == net.wavedk.extrautilitiesreutilized.init.EuruModVillagerProfessions.SHADY_MERCHANT.get();
					if (isShady) {
						if (entityiterator instanceof LivingEntity _livingEntity34 && _livingEntity34.getAttributes().hasAttribute(Attributes.MOVEMENT_SPEED))
							_livingEntity34.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(1);
						if (world instanceof ServerLevel projectileLevel) {
							Projectile _entityToSpawn = createPotionProjectile(projectileLevel, PotionContents.createItemStack(Items.SPLASH_POTION, Potions.INVISIBILITY), null, new Vec3(0, 0, 0));
							_entityToSpawn.setPos(x, y, z);
							_entityToSpawn.shoot(0, (-0.5), 0, 3, 0);
							projectileLevel.addFreshEntity(_entityToSpawn);
						}
						if (entityiterator instanceof LivingEntity _entity && !_entity.level().isClientSide())
							_entity.addEffect(new MobEffectInstance(MobEffects.INVISIBILITY, 400, 1));
						if (entityiterator instanceof Mob _entity)
							_entity.getNavigation().moveTo((x + Mth.nextInt(RandomSource.create(), 1, 10)), y, (x + Mth.nextInt(RandomSource.create(), 1, 10)), 3);
						EuruMod.queueServerWork(10, () -> {
							if (entity instanceof Player _player)
								_player.closeContainer();
						});
					}
				}
			}
		}
	}

	private static int getAmountInGUISlot(Entity entity, int sltid) {
		if (entity instanceof Player player && player.containerMenu instanceof EuruModMenus.MenuAccessor menuAccessor) {
			ItemStack stack = menuAccessor.getSlots().get(sltid).getItem();
			if (stack != null)
				return stack.getCount();
		}
		return 0;
	}

	private static Projectile createPotionProjectile(Level level, ItemStack contents, Entity shooter, Vec3 acceleration) {
		ThrownPotion entityToSpawn = new ThrownPotion(EntityType.POTION, level);
		entityToSpawn.setItem(contents);
		return initProjectileProperties(entityToSpawn, shooter, acceleration);
	}

	private static Projectile initProjectileProperties(Projectile entityToSpawn, Entity shooter, Vec3 acceleration) {
		entityToSpawn.setOwner(shooter);
		if (!Vec3.ZERO.equals(acceleration)) {
			entityToSpawn.setDeltaMovement(acceleration);
			entityToSpawn.hasImpulse = true;
		}
		return entityToSpawn;
	}
}