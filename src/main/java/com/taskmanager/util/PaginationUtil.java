package com.taskmanager.util;

import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public class PaginationUtil {

    public static List<Sort.Order> setSort(String[] sorts){
        List<Sort.Order> orders = new ArrayList<>();

        if (sorts[0].contains(",")) {
            for (String sortOrder : sorts) {
                String[] _sort = sortOrder.split(",");
                orders.add(new Sort.Order(getSortDirection(_sort[1]), _sort[0]));
            }
        } else {
            orders.add(new Sort.Order(getSortDirection(sorts[1]), sorts[0]));
        }
        return orders;
    }

    private static Sort.Direction getSortDirection(String s) {
        return s.contains("desc")?Sort.Direction.DESC:Sort.Direction.ASC;
    }
}
