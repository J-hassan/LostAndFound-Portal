package com.lostfound.engine;

import java.time.Period;
import java.util.ArrayList;
import java.util.List;

import com.lostfound.models.Item;
import com.lostfound.storage.DatabaseManager;

public class MatchEngine {

    public static List<Item> findMatches(Item reportedItem) {
        List<Item> allItems = DatabaseManager.getAllItems();
        List<Item> matches = new ArrayList<>();

        String reportedItemLoc = reportedItem.getLocation().toLowerCase();
        String reportedItemDes = reportedItem.getDescription().toLowerCase();
        String reportedItemName = reportedItem.getName().toLowerCase();

        for (Item item : allItems) {
            if (item.getId().equals(reportedItem.getId())) {
                continue;
            }

            if (item.getReportedByEmail().equalsIgnoreCase(reportedItem.getReportedByEmail())) {
                continue;
            }

            if (item.getType().equalsIgnoreCase(reportedItem.getType())) {
                continue;
            }

            String itemName = item.getName().toLowerCase();
            String itemDes = item.getDescription().toLowerCase();
            String itemLoc = item.getLocation().toLowerCase();

            int score = 0;

            if (itemName.contains(reportedItemName) || reportedItemName.contains(itemName)) {
                score += 30;
            }

            if (itemDes.contains(reportedItemDes) || reportedItemDes.contains(itemDes)) {
                score += 20;
            }

            if (itemLoc.contains(reportedItemLoc) || reportedItemLoc.contains(itemLoc)) {
                score += 10;
            }

            if (item.getCategory().equalsIgnoreCase(reportedItem.getCategory())) {
                score += 20;
            }

            Period period = Period.between(item.getDate(), reportedItem.getDate());
            if (Math.abs(period.getDays()) <= 2) {
                score += 20;
            }

            if (score >= 70) {
                matches.add(item);
            }

        }

        return matches;
    }

}
