package com.github.alexthe666.wikizoomer.client;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.pipeline.TextureTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.IntBuffer;

public class ScreenshotHelper {

    public static void exportScreenshot(String imageName, RenderInScreenshotFunction inScreenshot) {
        RenderTarget prev = Minecraft.getInstance().getMainRenderTarget();
        int w = Minecraft.getInstance().getWindow().getWidth();
        int h = Minecraft.getInstance().getWindow().getHeight();
        RenderTarget target = new TextureTarget(w, h, true, Minecraft.ON_OSX);
        target.setClearColor(0.0F, 0.0F, 0.0F, 0.0F);
        RenderSystem.clear(GL11.GL_COLOR_BUFFER_BIT, Minecraft.ON_OSX);

        target.clear(true);
        target.bindWrite(true);

        inScreenshot.call();

        IntBuffer pixels = BufferUtils.createIntBuffer(w * h);
        target.bindRead();
        GL11.glGetTexImage(GL11.GL_TEXTURE_2D, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixels);
        int[] vals = new int[w * h];
        pixels.get(vals);
        ScreenshotHelper.processPixelValues(vals, w, h);
        BufferedImage bufferedimage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        bufferedimage.setRGB(0, 0, w, h, vals, 0, w);
        File file1 = new File(Minecraft.getInstance().gameDirectory, "screenshots/wikizoomer");
        if(!file1.exists()){
            if(!file1.mkdir()){
                return;
            }
        }
        int sameCount = 0;
        for (File file : file1.listFiles()) {
            String name = file.getName();
            if(name.contains(imageName)){
                sameCount++;
            }
        }
        File f = new File(file1, imageName + (sameCount == 0 ? ".png" : "(" + sameCount + ").png"));
        try {
            f.createNewFile();
            ImageIO.write(bufferedimage, "png", f);

        } catch (Exception e) {

        }
        try {
            Thread.sleep(10L);
        } catch (InterruptedException interruptedexception) {
        }
        target.setClearColor(1.0F, 1.0F, 1.0F, 1.0F);
        target.destroyBuffers();
        RenderSystem.bindTexture(Minecraft.getInstance().getMainRenderTarget().getColorTextureId());
        Minecraft.getInstance().gameRenderer.setPanoramicMode(false);
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);
        Minecraft.getInstance().getMainRenderTarget().bindWrite(true);
        Minecraft.getInstance().gameRenderer.renderLevel(1.0F, 0L, new PoseStack());
        Minecraft.getInstance().levelRenderer.graphicsChanged();
   }

    private static void processPixelValues(int[] p_147953_0_, int p_147953_1_, int p_147953_2_) {
        int[] aint = new int[p_147953_1_];
        int i = p_147953_2_ / 2;

        for (int j = 0; j < i; ++j) {
            System.arraycopy(p_147953_0_, j * p_147953_1_, aint, 0, p_147953_1_);
            System.arraycopy(p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_0_, j * p_147953_1_, p_147953_1_);
            System.arraycopy(aint, 0, p_147953_0_, (p_147953_2_ - 1 - j) * p_147953_1_, p_147953_1_);
        }
    }

    @Deprecated
    @OnlyIn(Dist.CLIENT)
    public interface RenderInScreenshotFunction {
        void call();
    }
}


