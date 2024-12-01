package de.baving.rene.mctodolist;

import de.baving.rene.mctodolist.screen.TodoScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class MinecraftTodoListClient implements ClientModInitializer {

    private static TaskManager taskManager;
    private static KeyBinding openTodoScreenKey;

    @Override
    public void onInitializeClient() {
        HudRenderCallback.EVENT.register(this::onHudRender);
        taskManager = TaskManager.getInstance();

        // Register key binding
        openTodoScreenKey = new KeyBinding(
                "key.mctodolist.open_todo_screen",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_Z,
                "category.mctodolist"
        );
        KeyBindingHelper.registerKeyBinding(openTodoScreenKey);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openTodoScreenKey.wasPressed()) {
                client.setScreen(new TodoScreen());
            }
        });
    }

    private void onHudRender(DrawContext drawContext, RenderTickCounter renderTickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        int screenHeight = client.getWindow().getScaledHeight();

        // Calculate the starting position for the task list
        int xPosition = 10; // Fixed x position on the left side
        int yPosition = screenHeight / 2 - 50; // Centered vertically

        // Render the "Tasks:" label
        drawContext.drawTextWithShadow(client.textRenderer, "Tasks:", xPosition, yPosition, 0xFFFFFF);

        // Render each task dynamically, but limit to a maximum of 5 tasks
        int yOffset = yPosition + 15; // Starting y position for tasks
        int maxTasksToShow = 5; // Maximum number of tasks to show
        int maxTextLength = 30; // Maximum length for task text

        for (int i = 0; i < Math.min(taskManager.getTasks().size(), maxTasksToShow); i++) {
            String task = taskManager.getTasks().get(i);
            String displayText = task;

            // Truncate the text if it exceeds the maximum length
            if (task.length() > maxTextLength) {
                displayText = task.substring(0, maxTextLength - 3) + "...";
            }

            drawContext.drawTextWithShadow(client.textRenderer, "- " + displayText, xPosition, yOffset, 0xFFFFFF);
            yOffset += 10; // Move to the next line
        }
    }
}