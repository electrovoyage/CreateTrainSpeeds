package net.electrovoyage.customtrainspeeds.speedgetter;

import com.simibubi.create.AllPackets;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.createmod.catnip.platform.CatnipServices;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public record TrainSpeedGetterPacket_ServerTargeted(UUID trainID) implements ServerboundPacketPayload {
	public static final StreamCodec<ByteBuf, TrainSpeedGetterPacket_ServerTargeted> STREAM_CODEC = StreamCodec.composite(
		UUIDUtil.STREAM_CODEC, TrainSpeedGetterPacket_ServerTargeted::trainID,
		TrainSpeedGetterPacket_ServerTargeted::new
	);

	@Override
	public PacketTypeProvider getTypeProvider() { return AllPackets.TRAIN_GETSPEED_TOSERVER; }

	@Override
	public void handle(ServerPlayer player) {
		Train train = Create.RAILWAYS.sided(player.level()).trains.get(trainID);

		int MaxSpeed = (int)train.MaxSpeed;
		int MaxTurnSpeed = (int)train.MaxTurnSpeed;
		int Acceleration = (int)train.Acceleration;

		TrainSpeedGetterPacket_ClientTargeted packet = new TrainSpeedGetterPacket_ClientTargeted(MaxSpeed, MaxTurnSpeed, Acceleration);
		CatnipServices.NETWORK.sendToClient(player, packet);
	}
}
