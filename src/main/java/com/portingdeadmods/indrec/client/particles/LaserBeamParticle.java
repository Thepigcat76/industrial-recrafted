package com.portingdeadmods.indrec.client.particles;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.portingdeadmods.indrec.IRConfig;
import com.portingdeadmods.indrec.content.items.electric.MiningLaserItem;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LaserBeamParticle extends TextureSheetParticle {
    private static final double SEGMENT_SPACING = 0.15;

    private static final float FIRST_PERSON_MUZZLE_X = 0.55F;
    private static final float FIRST_PERSON_MUZZLE_Y = -0.3f;

    private static final double FIRST_PERSON_MUZZLE_SCALE = 12;

    private static final double THIRD_PERSON_SHOULDER_SIDE = 0.3125;
    private static final double THIRD_PERSON_ARM_LENGTH = 0.85;

    private static final ParticleRenderType ADDITIVE = new ParticleRenderType() {
        @Override
        public BufferBuilder begin(@NotNull Tesselator tesselator, @NotNull TextureManager textureManager) {
            RenderSystem.depthMask(false);
            RenderSystem.setShaderTexture(0, TextureAtlas.LOCATION_PARTICLES);
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
            return tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.PARTICLE);
        }

        @Override
        public String toString() {
            return "INDREC_LASER_BEAM_ADDITIVE";
        }
    };

    private final Player owner;

    protected LaserBeamParticle(ClientLevel level, double x, double y, double z, Player owner, SpriteSet sprites) {
        super(level, x, y, z);
        this.owner = owner;
        this.pickSprite(sprites);
        this.lifetime = 0;
        this.hasPhysics = false;
        this.gravity = 0;
        this.quadSize = 0.09F + this.random.nextFloat() * 0.03F;
        this.alpha = 0.75F;
    }

    @Override
    public void render(@NotNull VertexConsumer buffer, @NotNull Camera camera, float partialTick) {
        Vec3 start = getBeamStart(camera, partialTick);
        Vec3 end = MiningLaserItem.traceBeam(this.owner, partialTick).getLocation();
        Vec3 beam = end.subtract(start);
        Vec3 step = beam.normalize().scale(SEGMENT_SPACING);
        int segments = (int) (beam.length() / SEGMENT_SPACING);
        for (int i = 0; i <= segments; i++) {
            this.xo = this.x = start.x + step.x * i;
            this.yo = this.y = start.y + step.y * i;
            this.zo = this.z = start.z + step.z * i;
            super.render(buffer, camera, partialTick);
        }
    }

    private Vec3 getBeamStart(Camera camera, float partialTick) {
        int side = this.owner.getMainArm() == HumanoidArm.RIGHT ? 1 : -1;
        if (camera.getEntity() == this.owner && !camera.isDetached()) {
            // A point on the screen, as the hand is drawn at the same spot on the screen no matter where the camera is
            Vec3 muzzle = camera.getNearPlane().getPointOnPlane(side * FIRST_PERSON_MUZZLE_X, FIRST_PERSON_MUZZLE_Y);
            return camera.getPosition().add(muzzle.scale(FIRST_PERSON_MUZZLE_SCALE));
        }

        float bodyYaw = Mth.lerp(partialTick, this.owner.yBodyRotO, this.owner.yBodyRot) * Mth.DEG_TO_RAD;
        Vec3 right = new Vec3(-Mth.cos(bodyYaw), 0, -Mth.sin(bodyYaw));
        return this.owner.getEyePosition(partialTick)
                .add(0.075, -0, 0)
                .add(right.scale(side * THIRD_PERSON_SHOULDER_SIDE))
                .add(this.owner.getViewVector(partialTick).scale(THIRD_PERSON_ARM_LENGTH));
    }

    // The default box only covers the particle's own position, which would cull the beam with it
    @Override
    public @NotNull AABB getRenderBoundingBox(float partialTicks) {
        return this.owner.getBoundingBox().inflate(IRConfig.miningLaserRange + 2);
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    public @NotNull ParticleRenderType getRenderType() {
        return ADDITIVE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        // xSpeed holds the owner's entity id, particles spawned locally can't carry an entity otherwise
        @Override
        public @Nullable Particle createParticle(@NotNull SimpleParticleType type, @NotNull ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return level.getEntity((int) xSpeed) instanceof Player owner ? new LaserBeamParticle(level, x, y, z, owner, this.sprites) : null;
        }
    }
}
