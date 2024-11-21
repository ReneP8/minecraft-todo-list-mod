// ScrollableListWidget.java
package de.baving.rene.mctodolist.widgets;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.EntryListWidget;
import net.minecraft.text.Text;

public class ScrollableListWidget extends EntryListWidget<ScrollableListWidget.TodoEntry> {

    public ScrollableListWidget(MinecraftClient client, int width, int height, int y) {
        super(client, width, height, y, 22); // Adjust itemHeight based on text height
    }

    // Add a method to populate the list
    public void addItem(String todoText, Runnable onDelete) {
        this.addEntry(new TodoEntry(todoText, onDelete));
    }

    // Add a public method to remove an entry
    public void removeTodoEntry(TodoEntry entry) {
        this.removeEntry(entry);
    }

    @Override
    protected void appendClickableNarrations(NarrationMessageBuilder builder) {

    }

    // Define the entry class
    public static class TodoEntry extends Entry<TodoEntry> {
        private final String text;
        private final ButtonWidget deleteButton;

        public TodoEntry(String text, Runnable onDelete) {
            this.text = text;
            this.deleteButton = ButtonWidget.builder(Text.literal("Delete"), button -> onDelete.run())
                    .dimensions(0, 0, 50, 20)
                    .build();
        }

        @Override
        public void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float delta) {
            // Render the to-do text
            context.drawTextWithShadow(MinecraftClient.getInstance().textRenderer, text, x + 5, y + 2, 0xFFFFFF); // Adjust y position to center text

            // Render the delete button
            this.deleteButton.setX(x + entryWidth - 55); // Position the button to the right of the text
            this.deleteButton.setY(y);
            this.deleteButton.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            // Handle mouse click for the delete button
            return this.deleteButton.mouseClicked(mouseX, mouseY, button) || super.mouseClicked(mouseX, mouseY, button);
        }
    }
}