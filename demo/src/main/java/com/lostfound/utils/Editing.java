package com.lostfound.utils;

import java.time.LocalDate;

public class Editing {
    private static String name;
    private static String category;
    private static String type;
    private static String location;
    private static String description;
    private static String id;
    private static LocalDate date;

    private static boolean editingMode = false;

    private static Editing instance = null;

    private Editing() {
    }

    public static void setEditingItems(String id, String name, String category, String type, String location,
            String description,
            LocalDate date) {
        Editing.id = id;
        Editing.name = name;
        Editing.category = category;
        Editing.type = type;
        Editing.location = location;
        Editing.description = description;
        Editing.date = date;
    }

    public static Editing getInstance() {
        if (instance == null) {
            instance = new Editing();
        }
        return instance;
    }

    public static void setEditingMode(boolean mode) {
        editingMode = mode;
    }

    public static boolean isEditingMode() {
        return editingMode;
    }

    public static void clear() {
        name = null;
        category = null;
        type = null;
        location = null;
        description = null;
        id = null;
        date = null;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getType() {
        return type;
    }

    public String getLocation() {
        return location;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDate() {
        return date;
    }

}
