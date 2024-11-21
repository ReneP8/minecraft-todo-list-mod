package de.baving.rene.mctodolist.screen;

import de.baving.rene.mctodolist.TaskManager;
import de.baving.rene.mctodolist.widgets.ScrollableListWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;

public class TodoScreen extends Screen {
    private TextFieldWidget inputField;
    private String savedToDo = "";

    private static TaskManager taskManager;
    private ScrollableListWidget scrollableList;

    public TodoScreen() {
        super(Text.literal("To-Do List"));
    }

    @Override
    protected void init() {
        super.init();

        taskManager = TaskManager.getInstance();

        // Initialize the scrollable list with adjusted width and position
        int listWidth = this.width; // Adjust the width to be less than the screen width
        int listX = 0; // Center the list horizontally
        this.scrollableList = new ScrollableListWidget(MinecraftClient.getInstance(), listWidth, this.height / 2, 40);
        this.scrollableList.setX(listX); // Set the left position of the list
        this.addDrawableChild(this.scrollableList);

        // Populate the scrollable list with tasks
        for (int i = 0; i < taskManager.getTasks().size(); i++) {
            String task = taskManager.getTasks().get(i);
            int index = i;
            this.scrollableList.addItem(task, () -> {
                if (taskManager.getTasks().size() > index) {
                    taskManager.removeTask(index);
                }
                if (this.scrollableList.children().size() > index){
                    this.scrollableList.removeTodoEntry(this.scrollableList.children().get(index));
                }

            });
        }

        // Input Field
        this.inputField = new TextFieldWidget(this.textRenderer, this.width / 2 - 100, this.height - 40, 150, 20, Text.literal("Enter To-Do"));
        this.inputField.setMaxLength(100); // Limit input length
        this.inputField.setEditable(true);
        this.addDrawableChild(this.inputField);

        // Save Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            this.savedToDo = this.inputField.getText();
            if (!this.savedToDo.isEmpty()) {
                taskManager.addTask(this.savedToDo);
                int index = taskManager.getTasks().size() - 1;
                this.scrollableList.addItem(this.savedToDo, () -> {
                    if (taskManager.getTasks().size() > index) {
                        taskManager.removeTask(index);
                    }
                    if (this.scrollableList.children().size() > index){
                        this.scrollableList.removeTodoEntry(this.scrollableList.children().get(index));
                    }
                });
                this.savedToDo = ""; // Clear the savedToDo after adding
            }
            this.inputField.setText(""); // Clear the input field
        }).dimensions(this.width / 2 + 60, this.height - 40, 50, 20).build());
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        // Render the background
        this.renderBackground(context, mouseX, mouseY, delta);

        // Render input field and widgets
        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
        // Use DrawContext to render the default screen background with more transparency
        context.fill(0, 0, this.width, this.height, 0x80101010); // Dark gray background with 50% transparency
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.inputField.charTyped(chr, modifiers) || super.charTyped(chr, modifiers);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.inputField.keyPressed(keyCode, scanCode, modifiers) || super.keyPressed(keyCode, scanCode, modifiers);
    }
}