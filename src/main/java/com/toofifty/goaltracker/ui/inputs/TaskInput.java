package com.toofifty.goaltracker.ui.inputs;

import com.toofifty.goaltracker.ui.TextButton;
import lombok.Getter;
import net.runelite.client.ui.ColorScheme;
import net.runelite.client.ui.FontManager;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public abstract class TaskInput extends JPanel
{
    protected final int PREFERRED_INPUT_HEIGHT = 16;

    @Getter
    private Runnable updater;
    @Getter
    private JPanel inputRow;

    TaskInput(String title)
    {
        super(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1;
        constraints.gridwidth = 1;
        constraints.gridy = 0;
        constraints.ipady = 8;

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(FontManager.getRunescapeSmallFont());
        titleLabel.setForeground(ColorScheme.LIGHT_GRAY_COLOR);
        titleLabel.setBorder(new EmptyBorder(2, 8, 0, 8));

        JPanel titleContainer = new JPanel(new BorderLayout());
        titleContainer.add(titleLabel, BorderLayout.WEST);

        add(titleContainer, constraints);
        constraints.gridy++;

        inputRow = new JPanel(new BorderLayout());
        inputRow.setBackground(ColorScheme.DARKER_GRAY_COLOR);

        TextButton addButton = new TextButton("Add");
        addButton.onClick(e -> onSubmit());

        inputRow.add(addButton, BorderLayout.EAST);

        add(inputRow, constraints);
        constraints.gridy++;
    }

    abstract protected void onSubmit();

    public TaskInput onUpdate(Runnable updater)
    {
        this.updater = updater;
        return this;
    }

    abstract protected void reset();
}