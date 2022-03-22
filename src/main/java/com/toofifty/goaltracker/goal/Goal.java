package com.toofifty.goaltracker.goal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.toofifty.goaltracker.ReorderableList;
import com.toofifty.goaltracker.goal.factory.TaskFactory;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Goal implements ReorderableList<Task> {
    @Setter
    @Getter
    private String description = "New goal";

    @Setter
    @Getter
    private int displayOrder = -1;

    @Getter
    private List<Task> tasks = new ArrayList<>();

    public static Goal create(JsonObject json) {
        Goal goal = new Goal();
        goal.setDescription(json.get("description").getAsString());
        goal.setDisplayOrder(json.get("display_order").getAsInt());

        json.get("items").getAsJsonArray().forEach(item -> {
            JsonObject obj = item.getAsJsonObject();
            TaskFactory factory = Task.TaskType.fromString(obj.get("type").getAsString()).getFactory();
            goal.add(factory.create(obj));
        });

        return goal;
    }

    public List<Task> getComplete() {
        return filterBy(Task::isComplete);
    }

    public List<Task> getIncomplete() {
        return filterBy(task -> !task.isComplete());
    }

    public Boolean isComplete() {
        return this.getComplete().size() == this.getTasks().size();
    }

    public Boolean isInProgress() {
        return this.getComplete().size() > 0;
    }

    public JsonObject serialize() {
        JsonObject json = new JsonObject();
        json.addProperty("description", description);
        json.addProperty("display_order", displayOrder);
        JsonArray items = new JsonArray();
        tasks.forEach(task -> items.add(task.serialize()));
        json.add("items", items);
        return json;
    }

    private List<Task> filterBy(Predicate<Task> predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.<Task>toList());
    }

    @Override
    public List<Task> getAll() {
        return tasks;
    }

    @Override
    public void add(Task task) {
        tasks.add(task);
    }

    @Override
    public void remove(Task task) {
        tasks.remove(task);
    }

    @Override
    public void move(Task task, int offset) {
        int index = tasks.indexOf(task);
        tasks.remove(task);
        tasks.add(index + offset, task);
    }

    @Override
    public void moveToTop(Task task) {
        tasks.remove(task);
        tasks.add(0, task);
    }

    @Override
    public void moveToBottom(Task task) {
        tasks.remove(task);
        tasks.add(task);
    }

    @Override
    public Boolean isFirst(Task task) {
        return tasks.get(0) == task;
    }

    @Override
    public Boolean isLast(Task task) {
        return tasks.get(tasks.size() - 1) == task;
    }
}