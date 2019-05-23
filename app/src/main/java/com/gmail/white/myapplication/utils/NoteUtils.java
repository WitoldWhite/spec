package com.gmail.white.myapplication.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NoteUtils {
    public static String dateFromLong(long time) {
        DateFormat format = new SimpleDateFormat("dd MMM yyyy 'at' hh:mm aaa", Locale.ENGLISH);
        return format.format(new Date(time));
    }
}
