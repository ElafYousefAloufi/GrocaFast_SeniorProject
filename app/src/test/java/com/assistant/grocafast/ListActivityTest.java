package com.assistant.grocafast;

import static org.junit.Assert.*;

import com.grocafast.grocafast.ListActivity;

import org.junit.Test;

public class ListActivityTest {
    @Test
    public void Sorting_items_favorites() {
        int[] items_repeatation = new int[5];
        items_repeatation[0]=0;
        items_repeatation[1]=0;
        items_repeatation[2]=0;
        items_repeatation[3]=5;
        items_repeatation[4]=4;
        String[] items_names = new String[5];
        items_names[0] = "item0";
        items_names[1] = "item1";
        items_names[2] = "item2";
        items_names[3] = "item3";
        items_names[4] = "item4";
        String[] tops = ListActivity.GetTOPThreeItems(items_repeatation,items_names);
        assertEquals(tops[0],items_names[3]);
        assertEquals(tops[1],items_names[4]);
        assertEquals(tops[2],"");
    }
}