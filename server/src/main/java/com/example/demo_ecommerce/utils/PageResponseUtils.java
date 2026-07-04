package com.example.demo_ecommerce.utils;

public final class PageResponseUtils {
    PageResponseUtils() {}
    public static int normalizePage(int page) {
        return Math.max(1, page);
    }
    public static int normalizeSize(int size){
        return Math.clamp(size, 0, 10);
    }
}
