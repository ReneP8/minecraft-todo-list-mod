// TodoScreen.java
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
    private ScrollableListWidget activeTaskList;

    public TodoScreen() {
        super(Text.literal("To-Do List"));
    }

    // TodoScreen.java
    @Override
    protected void init() {
        super.init();

        taskManager = TaskManager.getInstance();

        // Initialize the combined task list
        int listWidth = this.width - 20; // Adjust the width to be less than the screen width
        int listX = 10; // Position the list horizontally
        int listHeight = this.height - 100; // Adjust the height to leave space for input field and button
        this.activeTaskList = new ScrollableListWidget(MinecraftClient.getInstance(), listWidth, listHeight, 40);
        this.activeTaskList.setX(listX); // Set the left position of the list
        this.addDrawableChild(this.activeTaskList);

        // Populate the combined task list with active and done tasks
        this.populateTaskList();

        // Input Field
        this.inputField = new TextFieldWidget(this.textRenderer, this.width / 2 - 75, this.height - 50, 150, 20, Text.literal("Enter To-Do"));
        this.inputField.setMaxLength(100); // Limit input length
        this.inputField.setEditable(true);
        this.addDrawableChild(this.inputField);

        // Save Button
        this.addDrawableChild(ButtonWidget.builder(Text.literal("Save"), button -> {
            this.savedToDo = this.inputField.getText();
            if (!this.savedToDo.isEmpty()) {
                taskManager.addTask(this.savedToDo);
                this.populateTaskList();
                this.savedToDo = ""; // Clear the savedToDo after adding
            }
            this.inputField.setText(""); // Clear the input field
        }).dimensions(this.width / 2 + 80, this.height - 50, 50, 20).build());
    }

    private void populateTaskList() {
        this.activeTaskList.clearAllEntries();
        for (int i = 0; i < taskManager.getTasks().size(); i++) {
            String task = taskManager.getTasks().get(i);
            int index = i;
            this.activeTaskList.addItem(task, false, () -> {
                if (taskManager.getTasks().size() > index) {
                    taskManager.removeTask(index);
                }
                if (this.activeTaskList.children().size() > index) {
                    this.activeTaskList.removeTodoEntry(this.activeTaskList.children().get(index));
                }
            }, () -> {
                if (taskManager.getTasks().size() > index) {
                    taskManager.markTaskAsDone(index);
                }
                if (this.activeTaskList.children().size() > index) {
                    this.activeTaskList.removeTodoEntry(this.activeTaskList.children().get(index));
                }
                this.populateTaskList();
            });
        }
        for (int i = 0; i < taskManager.getDoneTasks().size(); i++) {
            String task = taskManager.getDoneTasks().get(i);
            int index = i;
            this.activeTaskList.addItem(task, true, () -> {
                if (taskManager.getDoneTasks().size() > index) {
                    taskManager.removeDoneTask(index);
                }
                if (this.activeTaskList.children().size() > index){
                    this.activeTaskList.removeTodoEntry(this.activeTaskList.children().get(index));
                }
            }, () -> {
                if (taskManager.getDoneTasks().size() > index) {
                    taskManager.markTaskAsNotDone(index);
                }
                if (this.activeTaskList.children().size() > index){
                    this.activeTaskList.removeTodoEntry(this.activeTaskList.children().get(index));
                }
                this.populateTaskList();
            });
        }
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