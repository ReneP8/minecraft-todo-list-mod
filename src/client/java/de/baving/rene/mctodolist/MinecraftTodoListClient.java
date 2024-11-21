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
        System.out.println("To-Do List Mod initialized!");

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
        // Example rendering a static text
        MinecraftClient client = MinecraftClient.getInstance();
        drawContext.drawTextWithShadow(client.textRenderer, "Tasks:", 10, 10, 0xFFFFFF);

        // Render each task dynamically, but limit to a maximum of 5 tasks
        int yOffset = 25; // Starting y position for tasks
        int maxTasksToShow = 5; // Maximum number of tasks to show
        for (int i = 0; i < Math.min(taskManager.getTasks().size(), maxTasksToShow); i++) {
            String task = "- " + taskManager.getTasks().get(i); // Format: "- task"
            drawContext.drawTextWithShadow(client.textRenderer, task, 10, yOffset, 0xFFFFFF);
            yOffset += 10; // Move to the next line
        }
    }
}