package com.madgique.nomoremapinoffhand.forge.client.config;

import com.madgique.nomoremapinoffhand.config.ModConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class PositionConfigScreen extends Screen {
    
    private final Screen parent;
    private final ModConfig config;
    private boolean isDragging = false;
    private int dragOffsetX = 0;
    private int dragOffsetY = 0;
    private int tempX, tempY; // Temporary position while dragging

    public PositionConfigScreen(Screen parent) {
        super(Component.literal("Configure Minimap Position"));
        this.parent = parent;
        this.config = ModConfig.getInstance();
        this.tempX = config.getMinimapX();
        this.tempY = config.getMinimapY();
    }

    @Override
    protected void init() {
        super.init();
        
        // Done button
        this.addRenderableWidget(Button.builder(Component.literal("Done"), 
            button -> this.onClose())
            .bounds(this.width / 2 - 100, this.height - 40, 200, 20)
            .build());
            
        // Reset to center button
        this.addRenderableWidget(Button.builder(Component.literal("Center"), 
            button -> {
                tempX = (this.width - config.getMinimapSize()) / 2;
                tempY = (this.height - config.getMinimapSize()) / 2;
            })
            .bounds(this.width / 2 - 205, this.height - 40, 100, 20)
            .build());
            
        // Reset to top-left button  
        this.addRenderableWidget(Button.builder(Component.literal("Top-Left"), 
            button -> {
                tempX = 10;
                tempY = 10;
            })
            .bounds(this.width / 2 + 105, this.height - 40, 100, 20)
            .build());
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        // Dark overlay background
        guiGraphics.fill(0, 0, this.width, this.height, 0xAA000000);
        
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        
        // Draw minimap preview
        drawMinimapPreview(guiGraphics, mouseX, mouseY);
        
        // Instructions
        guiGraphics.drawCenteredString(this.font, 
            "§6Click and drag the preview to reposition the minimap", 
            this.width / 2, 20, 0xFFFFFF);
            
        guiGraphics.drawCenteredString(this.font, 
            "§7Position: X=" + tempX + ", Y=" + tempY, 
            this.width / 2, 35, 0xCCCCCC);
    }
    
    private void drawMinimapPreview(GuiGraphics guiGraphics, int mouseX, int mouseY) {
        int size = config.getMinimapSize();
        
        // Highlight if hovering
        boolean hovering = mouseX >= tempX - 4 && mouseX <= tempX + size + 4 &&
                          mouseY >= tempY - 4 && mouseY <= tempY + size + 4;
        
        if (hovering || isDragging) {
            // Bright highlight when hovering/dragging
            guiGraphics.fill(tempX - 6, tempY - 6, tempX + size + 6, tempY + size + 6, 
                isDragging ? 0xFF00AA00 : 0xFF0066AA);
        }
        
        // Draw border preview (if enabled)
        if (config.shouldShowBorder()) {
            // Outer border
            guiGraphics.fill(tempX - 4, tempY - 4, tempX, tempY + size + 4, 0xFF8B4513);
            guiGraphics.fill(tempX + size, tempY - 4, tempX + size + 4, tempY + size + 4, 0xFF8B4513);
            guiGraphics.fill(tempX - 4, tempY - 4, tempX + size + 4, tempY, 0xFF8B4513);
            guiGraphics.fill(tempX - 4, tempY + size, tempX + size + 4, tempY + size + 4, 0xFF8B4513);
            
            // Inner border
            guiGraphics.fill(tempX - 2, tempY - 2, tempX, tempY + size + 2, 0xFFD2B48C);
            guiGraphics.fill(tempX + size, tempY - 2, tempX + size + 2, tempY + size + 2, 0xFFD2B48C);
            guiGraphics.fill(tempX - 2, tempY - 2, tempX + size + 2, tempY, 0xFFD2B48C);
            guiGraphics.fill(tempX - 2, tempY + size, tempX + size + 2, tempY + size + 2, 0xFFD2B48C);
        }
        
        // Draw minimap area with pattern
        guiGraphics.fill(tempX, tempY, tempX + size, tempY + size, 0xFFF5DEB3);
        
        // Draw a simple grid pattern to simulate map content
        for (int i = 0; i < size; i += 16) {
            for (int j = 0; j < size; j += 16) {
                if ((i / 16 + j / 16) % 2 == 0) {
                    guiGraphics.fill(tempX + i, tempY + j, 
                        tempX + Math.min(i + 16, size), 
                        tempY + Math.min(j + 16, size), 0xFFE0C090);
                }
            }
        }
        
        // Draw center cross
        guiGraphics.fill(tempX + size/2 - 1, tempY + size/4, tempX + size/2 + 1, tempY + 3*size/4, 0xFF000000);
        guiGraphics.fill(tempX + size/4, tempY + size/2 - 1, tempX + 3*size/4, tempY + size/2 + 1, 0xFF000000);
        
        // Draw "PREVIEW" text
        guiGraphics.drawCenteredString(this.font, "§1PREVIEW", 
            tempX + size/2, tempY + size/2 + 10, 0x000000);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) { // Left click
            int size = config.getMinimapSize();
            
            // Check if click is within minimap bounds (including border)
            if (mouseX >= tempX - 4 && mouseX <= tempX + size + 4 &&
                mouseY >= tempY - 4 && mouseY <= tempY + size + 4) {
                
                isDragging = true;
                dragOffsetX = (int)(mouseX - tempX);
                dragOffsetY = (int)(mouseY - tempY);
                return true;
            }
        }
        
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (button == 0 && isDragging) {
            isDragging = false;
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isDragging) {
            // Calculate new position
            int newX = (int)(mouseX - dragOffsetX);
            int newY = (int)(mouseY - dragOffsetY);
            
            // Clamp to screen bounds
            int size = config.getMinimapSize();
            tempX = Math.max(4, Math.min(newX, this.width - size - 4));
            tempY = Math.max(4, Math.min(newY, this.height - size - 4));
            
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public void onClose() {
        // Save the new position
        config.setMinimapPosition(tempX, tempY);
        this.minecraft.setScreen(parent);
    }
}