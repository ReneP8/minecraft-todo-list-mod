// ScrollableListWidget.java
package de.baving.rene.mctodolist.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;

public class ScrollableListWidget extends EntryListWidget<ScrollableListWidget.TodoEntry> {

    public ScrollableListWidget(MinecraftClient client, int width, int height, int y) {
        super(client, width, height, y, 22); // Adjust itemHeight based on text height
    }

    // Add a method to populate the list
    public void addItem(String todoText, boolean isDone, Runnable onDelete, Runnable onCheck, Runnable onMoveUp, Runnable onMoveDown) {
        this.addEntry(new TodoEntry(todoText, isDone, onDelete, onCheck, onMoveUp, onMoveDown));
    }

    // Add a public method to remove an entry
    public void removeTodoEntry(TodoEntry entry) {
        this.removeEntry(entry);
    }

    // Add a public method to clear entries
    public void clearAllEntries() {
        this.clearEntries();
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {}

    public static class TodoEntry extends Entry<TodoEntry> {
        private final String text;
        private final ButtonWidget deleteButton;
        private final ButtonWidget moveUpButton;
        private final ButtonWidget moveDownButton;
        private final CheckboxWidget checkBox;
        private boolean isDone;

        public TodoEntry(String text, boolean isDone, Runnable onDelete, Runnable onCheck, Runnable onMoveUp, Runnable onMoveDown) {
            this.text = text;
            this.isDone = isDone;
            this.deleteButton = ButtonWidget.builder(Text.literal("x"), button -> onDelete.run())
                    .dimensions(0, 0, 20, 20)
                    .build();
            this.moveUpButton = ButtonWidget.builder(Text.literal("↑"), button -> onMoveUp.run())
                    .dimensions(0, 0, 20, 20)
                    .build();
            this.moveDownButton = ButtonWidget.builder(Text.literal("↓"), button -> onMoveDown.run())
                    .dimensions(0, 0, 20, 20)
                    .build();
            this.checkBox = CheckboxWidget.builder(Text.literal(""), MinecraftClient.getInstance().textRenderer)
                    .pos(0, 0)
                    .callback((checkbox, checked) -> {
                        onCheck.run();
                        this.isDone = checked;
                    })
                    .checked(isDone)
                    .build();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
            // Calculate available width for the text
            int availableWidth = entryWidth - 120; // Adjust based on the width of the buttons and padding

            // Truncate the text if it exceeds the available width
            String displayText = isDone ? text + " (done)" : text;
            if (MinecraftClient.getInstance().textRenderer.getWidth(displayText) > availableWidth) {
                displayText = MinecraftClient.getInstance().textRenderer.trimToWidth(displayText, availableWidth - MinecraftClient.getInstance().textRenderer.getWidth("...")) + "...";
            }

            // Render the to-do text with strikethrough if done
            int color = isDone ? 0xFFAAAAAA : 0xFFFFFF; // Gray color if done
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, displayText, x + 25, y + 2, color);

            // Render the delete button
            this.deleteButton.setX(x + entryWidth - 25); // Position the button to the right of the text
            this.deleteButton.setY(y);
            this.deleteButton.render(context, mouseX, mouseY, delta);

            // Render the move up button
            this.moveUpButton.setX(x + entryWidth - 50);
            this.moveUpButton.setY(y);
            this.moveUpButton.render(context, mouseX, mouseY, delta);

            // Render the move down button
            this.moveDownButton.setX(x + entryWidth - 75);
            this.moveDownButton.setY(y);
            this.moveDownButton.render(context, mouseX, mouseY, delta);

            // Render the checkbox
            this.checkBox.setX(x + 5);
            this.checkBox.setY(y);
            this.checkBox.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Handle mouse click for the delete button, move up button, move down button, and checkbox
            return this.deleteButton.mouseClicked(mouseX, mouseY, button) ||
                    this.moveUpButton.mouseClicked(mouseX, mouseY, button) ||
                    this.moveDownButton.mouseClicked(mouseX, mouseY, button) ||
                    this.checkBox.mouseClicked(mouseX, mouseY, button) ||
                    super.mouseClicked(mouseX, mouseY, button);
        }
    }
}