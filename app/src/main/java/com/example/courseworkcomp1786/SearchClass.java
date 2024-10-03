package com.example.courseworkcomp1786;

import java.util.ArrayList;
import java.util.List;

public class SearchClass {
    public static List<AddClass> searchClasses(List<AddClass> classList, String query) {
        List<AddClass> filteredList = new ArrayList<>();
        String lowercaseQuery = query.toLowerCase().trim();

        for (AddClass addClass : classList) {
            if (addClass.getTeacher().toLowerCase().contains(lowercaseQuery) ||
                addClass.getDate().toLowerCase().contains(lowercaseQuery)) {
                filteredList.add(addClass);
            }
        }

        return filteredList;
    }
}