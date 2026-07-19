/*
 * MCreator note: This file will be REGENERATED on each build.
 * Just Enough Recipes keeps its optional JEI bridge in a separate class so it
 * does not replace the workspace's main mod template or conflict with Curios.
 */
package net.wavedk.extrautilitiesreutilized.procedures;

@net.neoforged.fml.common.EventBusSubscriber(modid = "euru")
public final class EuruModJerIntegration {
	private EuruModJerIntegration() {
	}

	@net.neoforged.bus.api.SubscribeEvent
	public static void registerNetworking(net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent event) {
		event.registrar("euru").playBidirectional(JerOpenJeiMessage.TYPE, JerOpenJeiMessage.STREAM_CODEC, JerOpenJeiMessage::handle);
	}

	public static void jerOpenJei(net.minecraft.world.entity.Entity target, String itemId, int mode) {
		if (target == null || itemId == null || itemId.length() > 256 || mode < 0 || mode > 1)
			return;
		if (target instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
			net.neoforged.neoforge.network.PacketDistributor.sendToPlayer(serverPlayer, new JerOpenJeiMessage(mode, itemId));
		} else if (target.level().isClientSide()) {
			openClient(target, itemId, mode);
		}
	}

	private static void openClient(net.minecraft.world.entity.Entity target, String itemId, int mode) {
		if (!net.neoforged.fml.ModList.get().isLoaded("jei"))
			return;
		try {
			Class<?> minecraftClass = Class.forName("net.minecraft.client.Minecraft");
			Object minecraft = minecraftClass.getMethod("getInstance").invoke(null);
			Object localPlayer = minecraftClass.getField("player").get(minecraft);
			if (localPlayer == null || target != null && target != localPlayer)
				return;
			Object runtime = Class.forName("mezz.jei.common.Internal").getMethod("getJeiRuntime").invoke(null);
			if (runtime == null)
				return;
			Object itemObject = net.minecraft.core.registries.BuiltInRegistries.ITEM.get(net.minecraft.resources.ResourceLocation.parse(itemId));
			net.minecraft.world.item.ItemStack stack = ((net.minecraft.world.item.Item) itemObject).getDefaultInstance();
			if (stack.isEmpty())
				return;
			Object recipesGui = invoke(runtime, "getRecipesGui");
			if (mode == 0) {
				Class<?> roleClass = Class.forName("mezz.jei.api.recipe.RecipeIngredientRole");
				@SuppressWarnings({"unchecked", "rawtypes"})
				Object inputRole = Enum.valueOf((Class<? extends Enum>) roleClass, "INPUT");
				Object itemStackType = Class.forName("mezz.jei.api.constants.VanillaTypes").getField("ITEM_STACK").get(null);
				Object helpers = invoke(runtime, "getJeiHelpers");
				Object focusFactory = invoke(helpers, "getFocusFactory");
				Object focus = invoke(focusFactory, "createFocus", inputRole, itemStackType, stack);
				invoke(recipesGui, "show", focus);
			} else {
				Object recipeManager = invoke(runtime, "getRecipeManager");
				Object categoriesLookup = invoke(recipeManager, "createRecipeCategoryLookup");
				java.util.LinkedHashSet<Object> matchingTypes = new java.util.LinkedHashSet<>();
				try (java.util.stream.Stream<?> categories = (java.util.stream.Stream<?>) invoke(categoriesLookup, "get")) {
					for (Object category : categories.toList()) {
						Object recipeType = invoke(category, "getRecipeType");
						Object stationLookup;
						try {
							stationLookup = invoke(recipeManager, "createCraftingStationLookup", recipeType);
						} catch (NoSuchMethodException ignored) {
							stationLookup = invoke(recipeManager, "createRecipeCatalystLookup", recipeType);
						}
						try (java.util.stream.Stream<?> stations = (java.util.stream.Stream<?>) invoke(stationLookup, "getItemStack")) {
							if (stations.anyMatch(candidate -> candidate instanceof net.minecraft.world.item.ItemStack candidateStack && candidateStack.getItem() == stack.getItem()))
								matchingTypes.add(recipeType);
						}
					}
				}
				if (!matchingTypes.isEmpty())
					invoke(recipesGui, "showTypes", new java.util.ArrayList<>(matchingTypes));
			}
		} catch (ReflectiveOperationException | LinkageError | RuntimeException ignored) {
		}
	}

	private static Object invoke(Object target, String name, Object... arguments) throws ReflectiveOperationException {
		if (target == null)
			throw new NullPointerException(name);
		for (java.lang.reflect.Method method : target.getClass().getMethods()) {
			if (!method.getName().equals(name) || method.getParameterCount() != arguments.length)
				continue;
			Class<?>[] parameterTypes = method.getParameterTypes();
			boolean matches = true;
			for (int i = 0; i < parameterTypes.length; i++) {
				if (arguments[i] != null && !parameterTypes[i].isInstance(arguments[i])) {
					matches = false;
					break;
				}
			}
			if (matches) {
				method.trySetAccessible();
				return method.invoke(target, arguments);
			}
		}
		throw new NoSuchMethodException(name);
	}

	public record JerOpenJeiMessage(int mode, String itemId) implements net.minecraft.network.protocol.common.custom.CustomPacketPayload {
		public static final net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<JerOpenJeiMessage> TYPE = new net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<>(
				net.minecraft.resources.ResourceLocation.fromNamespaceAndPath("euru", "jer_open_jei"));
		public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, JerOpenJeiMessage> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(JerOpenJeiMessage::write, JerOpenJeiMessage::read);

		private static void write(net.minecraft.network.FriendlyByteBuf buffer, JerOpenJeiMessage message) {
			buffer.writeVarInt(message.mode);
			buffer.writeUtf(message.itemId, 256);
		}

		private static JerOpenJeiMessage read(net.minecraft.network.FriendlyByteBuf buffer) {
			return new JerOpenJeiMessage(buffer.readVarInt(), buffer.readUtf(256));
		}

		@Override
		public net.minecraft.network.protocol.common.custom.CustomPacketPayload.Type<JerOpenJeiMessage> type() {
			return TYPE;
		}

		private static void handle(JerOpenJeiMessage message, net.neoforged.neoforge.network.handling.IPayloadContext context) {
			if (context.flow() != net.minecraft.network.protocol.PacketFlow.CLIENTBOUND || message.mode < 0 || message.mode > 1 || message.itemId.length() > 256)
				return;
			context.enqueueWork(() -> openClient(null, message.itemId, message.mode));
		}
	}
}