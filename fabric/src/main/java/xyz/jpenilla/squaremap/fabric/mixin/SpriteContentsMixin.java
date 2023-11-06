package xyz.jpenilla.squaremap.fabric.mixin;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import xyz.jpenilla.squaremap.fabric.FabricFluidColorExporter;

@Mixin(TextureAtlasSprite.class)
abstract class SpriteContentsMixin implements FabricFluidColorExporter.SpriteContentsExtension {
    @Shadow @Final protected NativeImage[] mainImage;

    @Override
    public int getPixelRGBA(final int x, final int y) {
        // always gets from frame 0 of animated texture
        return this.mainImage[0].getPixelRGBA(x, y);
    }
}
