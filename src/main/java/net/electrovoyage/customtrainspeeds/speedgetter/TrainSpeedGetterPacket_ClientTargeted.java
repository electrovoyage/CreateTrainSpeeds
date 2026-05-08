package net.electrovoyage.customtrainspeeds.speedgetter;

import com.simibubi.create.AllPackets;

import com.simibubi.create.content.trains.station.StationScreen;

import io.netty.buffer.ByteBuf;
import net.createmod.catnip.net.base.ClientboundPacketPayload;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

public record TrainSpeedGetterPacket_ClientTargeted(int MaxSpeed, int MaxTurnSpeed, int Acceleration) implements ClientboundPacketPayload  {
	public static final StreamCodec<ByteBuf, TrainSpeedGetterPacket_ClientTargeted> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.VAR_INT, TrainSpeedGetterPacket_ClientTargeted::MaxSpeed,
		ByteBufCodecs.VAR_INT, TrainSpeedGetterPacket_ClientTargeted::MaxTurnSpeed,
		ByteBufCodecs.VAR_INT, TrainSpeedGetterPacket_ClientTargeted::Acceleration,
		TrainSpeedGetterPacket_ClientTargeted::new
	);

	@Override
	public PacketTypeProvider getTypeProvider() { return AllPackets.TRAIN_GETSPEED_TOCLIENT; }

	@Override
	@OnlyIn(Dist.CLIENT)
	public void handle(LocalPlayer player) {
		StationScreen screen = StationScreen.getInstance();
		if (screen != null) {
			screen.setSpeedValues(MaxSpeed, MaxTurnSpeed, Acceleration);
		}
	}
}
