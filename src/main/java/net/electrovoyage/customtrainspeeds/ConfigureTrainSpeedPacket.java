package net.electrovoyage.customtrainspeeds;

import com.simibubi.create.AllPackets;

import com.simibubi.create.Create;
import com.simibubi.create.content.trains.entity.Train;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ServerboundPacketPayload;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;

import java.util.UUID;

public record ConfigureTrainSpeedPacket(int maxSpeed, int maxTurnSpeed, int acceleration, UUID trainID) implements ServerboundPacketPayload {
	public static final StreamCodec<ByteBuf, ConfigureTrainSpeedPacket> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, ConfigureTrainSpeedPacket::maxSpeed,
		ByteBufCodecs.VAR_INT, ConfigureTrainSpeedPacket::maxTurnSpeed,
		ByteBufCodecs.VAR_INT, ConfigureTrainSpeedPacket::acceleration,
		UUIDUtil.STREAM_CODEC, ConfigureTrainSpeedPacket::trainID,
		ConfigureTrainSpeedPacket::new
	);

	@Override
	public PacketTypeProvider getTypeProvider() { return AllPackets.TRAIN_SPEED_EDIT; }

	@Override
	public void handle(ServerPlayer player) {
		// what to do when packet reaches server

		Train train = Create.RAILWAYS.sided(player.level()).trains.get(trainID);
		/*if (train == null) {
			System.out.printf("train speed config packet with invalid train UUID %s\n", trainID);
			return;
		}*/

		train.MaxSpeed = this.maxSpeed;
		train.MaxTurnSpeed = this.maxTurnSpeed;
		train.Acceleration = this.acceleration;
	}
}
