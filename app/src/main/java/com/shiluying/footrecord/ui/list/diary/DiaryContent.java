package com.shiluying.footrecord.ui.list.diary;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class DiaryContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<DiaryItem> ITEMS = new ArrayList<DiaryItem>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<String, DiaryItem> ITEM_MAP = new HashMap<String, DiaryItem>();

    private static final int COUNT = 25;

    /**
     * A dummy item representing a piece of content.
     */
    public static class DiaryItem {
        public final String id;
        public final String content;
        public final String title;

        public DiaryItem(String id, String title, String content) {
            this.id = id;
            Log.i("TESTID",id);
            this.content = content;
            this.title = title;
        }

        @Override
        public String toString() {
            return content;
        }
    }
}
