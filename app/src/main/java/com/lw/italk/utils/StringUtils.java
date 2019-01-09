package com.lw.italk.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2018/8/17 0017.
 */

public class StringUtils {
    public static final DateFormat XEP_0082_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static final DateFormat dateFormatter;
    private static final Pattern datePattern;
    private static final DateFormat timeFormatter;
    private static final Pattern timePattern;
    private static final DateFormat timeNoZoneFormatter;
    private static final Pattern timeNoZonePattern;
    private static final DateFormat timeNoMillisFormatter;
    private static final Pattern timeNoMillisPattern;
    private static final DateFormat timeNoMillisNoZoneFormatter;
    private static final Pattern timeNoMillisNoZonePattern;
    private static final DateFormat dateTimeFormatter;
    private static final Pattern dateTimePattern;
    private static final DateFormat dateTimeNoMillisFormatter;
    private static final Pattern dateTimeNoMillisPattern;
    private static final DateFormat xep0091Formatter;
    private static final DateFormat xep0091Date6DigitFormatter;
    private static final DateFormat xep0091Date7Digit1MonthFormatter;
    private static final DateFormat xep0091Date7Digit2MonthFormatter;
    private static final Pattern xep0091Pattern;
    private static final List<PatternCouplings> couplings;
    private static final char[] QUOTE_ENCODE;
    private static final char[] APOS_ENCODE;
    private static final char[] AMP_ENCODE;
    private static final char[] LT_ENCODE;
    private static final char[] GT_ENCODE;
    private static MessageDigest digest;
    private static Random randGen;
    private static char[] numbersAndLetters;

    static {
        dateFormatter = DateFormatType.XEP_0082_DATE_PROFILE.createFormatter();
        datePattern = Pattern.compile("^\\d+-\\d+-\\d+$");
        timeFormatter = DateFormatType.XEP_0082_TIME_MILLIS_ZONE_PROFILE.createFormatter();
        timePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))$");
        timeNoZoneFormatter = DateFormatType.XEP_0082_TIME_MILLIS_PROFILE.createFormatter();
        timeNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+.\\d+$");
        timeNoMillisFormatter = DateFormatType.XEP_0082_TIME_ZONE_PROFILE.createFormatter();
        timeNoMillisPattern = Pattern.compile("^(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))$");
        timeNoMillisNoZoneFormatter = DateFormatType.XEP_0082_TIME_PROFILE.createFormatter();
        timeNoMillisNoZonePattern = Pattern.compile("^(\\d+:){2}\\d+$");
        dateTimeFormatter = DateFormatType.XEP_0082_DATETIME_MILLIS_PROFILE.createFormatter();
        dateTimePattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+.\\d+(Z|([+-](\\d+:\\d+)))?$");
        dateTimeNoMillisFormatter = DateFormatType.XEP_0082_DATETIME_PROFILE.createFormatter();
        dateTimeNoMillisPattern = Pattern.compile("^\\d+(-\\d+){2}+T(\\d+:){2}\\d+(Z|([+-](\\d+:\\d+)))?$");
        xep0091Formatter = new SimpleDateFormat("yyyyMMdd'T'HH:mm:ss");
        xep0091Date6DigitFormatter = new SimpleDateFormat("yyyyMd'T'HH:mm:ss");
        xep0091Date7Digit1MonthFormatter = new SimpleDateFormat("yyyyMdd'T'HH:mm:ss");
        xep0091Date7Digit2MonthFormatter = new SimpleDateFormat("yyyyMMd'T'HH:mm:ss");
        xep0091Pattern = Pattern.compile("^\\d+T\\d+:\\d+:\\d+$");
        couplings = new ArrayList();
        TimeZone var0 = TimeZone.getTimeZone("UTC");
        XEP_0082_UTC_FORMAT.setTimeZone(var0);
        dateFormatter.setTimeZone(var0);
        timeFormatter.setTimeZone(var0);
        timeNoZoneFormatter.setTimeZone(var0);
        timeNoMillisFormatter.setTimeZone(var0);
        timeNoMillisNoZoneFormatter.setTimeZone(var0);
        dateTimeFormatter.setTimeZone(var0);
        dateTimeNoMillisFormatter.setTimeZone(var0);
        xep0091Formatter.setTimeZone(var0);
        xep0091Date6DigitFormatter.setTimeZone(var0);
        xep0091Date7Digit1MonthFormatter.setTimeZone(var0);
        xep0091Date7Digit1MonthFormatter.setLenient(false);
        xep0091Date7Digit2MonthFormatter.setTimeZone(var0);
        xep0091Date7Digit2MonthFormatter.setLenient(false);
        couplings.add(new StringUtils.PatternCouplings(datePattern, dateFormatter));
        couplings.add(new StringUtils.PatternCouplings(dateTimePattern, dateTimeFormatter, true));
        couplings.add(new StringUtils.PatternCouplings(dateTimeNoMillisPattern, dateTimeNoMillisFormatter, true));
        couplings.add(new StringUtils.PatternCouplings(timePattern, timeFormatter, true));
        couplings.add(new StringUtils.PatternCouplings(timeNoZonePattern, timeNoZoneFormatter));
        couplings.add(new StringUtils.PatternCouplings(timeNoMillisPattern, timeNoMillisFormatter, true));
        couplings.add(new StringUtils.PatternCouplings(timeNoMillisNoZonePattern, timeNoMillisNoZoneFormatter));
        QUOTE_ENCODE = "&quot;".toCharArray();
        APOS_ENCODE = "&apos;".toCharArray();
        AMP_ENCODE = "&amp;".toCharArray();
        LT_ENCODE = "&lt;".toCharArray();
        GT_ENCODE = "&gt;".toCharArray();
        digest = null;
        randGen = new Random();
        numbersAndLetters = "0123456789abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();
    }

    public static Date parseXEP0082Date(String var0) throws ParseException {
        return parseDate(var0);
    }

    public static Date parseDate(String var0) throws ParseException {
        Matcher var1 = xep0091Pattern.matcher(var0);
        if(var1.matches()) {
            int var2 = var0.split("T")[0].length();
            if(var2 >= 8) {
                DateFormat var10 = xep0091Formatter;
                synchronized(xep0091Formatter) {
                    return xep0091Formatter.parse(var0);
                }
            }

            Date var3 = handleDateWithMissingLeadingZeros(var0, var2);
            if(var3 != null) {
                return var3;
            }
        } else {
            Iterator var11 = couplings.iterator();

            while(var11.hasNext()) {
                StringUtils.PatternCouplings var8 = (StringUtils.PatternCouplings)var11.next();
                var1 = var8.pattern.matcher(var0);
                if(var1.matches()) {
                    if(var8.needToConvertTimeZone) {
                        var0 = var8.convertTime(var0);
                    }

                    DateFormat var4 = var8.formatter;
                    synchronized(var8.formatter) {
                        return var8.formatter.parse(var0);
                    }
                }
            }
        }

        DateFormat var9 = dateTimeNoMillisFormatter;
        synchronized(dateTimeNoMillisFormatter) {
            return dateTimeNoMillisFormatter.parse(var0);
        }
    }

    private static Date handleDateWithMissingLeadingZeros(String var0, int var1) throws ParseException {
        if(var1 == 6) {
            DateFormat var7 = xep0091Date6DigitFormatter;
            synchronized(xep0091Date6DigitFormatter) {
                return xep0091Date6DigitFormatter.parse(var0);
            }
        } else {
            Calendar var2 = Calendar.getInstance();
            Calendar var3 = parseXEP91Date(var0, xep0091Date7Digit1MonthFormatter);
            Calendar var4 = parseXEP91Date(var0, xep0091Date7Digit2MonthFormatter);
            List var5 = filterDatesBefore(var2, new Calendar[]{var3, var4});
            return !var5.isEmpty()?determineNearestDate(var2, var5).getTime():null;
        }
    }

    private static Calendar parseXEP91Date(String var0, DateFormat var1) {
        try {
            synchronized(var1) {
                var1.parse(var0);
                return var1.getCalendar();
            }
        } catch (ParseException var4) {
            return null;
        }
    }

    private static List<Calendar> filterDatesBefore(Calendar var0, Calendar... var1) {
        ArrayList var2 = new ArrayList();
        Calendar[] var6 = var1;
        int var5 = var1.length;

        for(int var4 = 0; var4 < var5; ++var4) {
            Calendar var3 = var6[var4];
            if(var3 != null && var3.before(var0)) {
                var2.add(var3);
            }
        }

        return var2;
    }

    private static Calendar determineNearestDate(final Calendar var0, List<Calendar> var1) {
        Collections.sort(var1, new Comparator<Calendar>() {
            public int compare(Calendar var1, Calendar var2) {
                Long var3 = new Long(var0.getTimeInMillis() - var1.getTimeInMillis());
                Long var4 = new Long(var0.getTimeInMillis() - var2.getTimeInMillis());
                return var3.compareTo(var4);
            }
        });
        return (Calendar)var1.get(0);
    }

    public static String formatXEP0082Date(Date var0) {
        DateFormat var1 = dateTimeFormatter;
        synchronized(dateTimeFormatter) {
            return dateTimeFormatter.format(var0);
        }
    }

    public static String formatDate(Date var0, DateFormatType var1) {
        return null;
    }

    public static String parseName(String var0) {
        if(var0 == null) {
            return null;
        } else {
            int var1 = var0.lastIndexOf("@");
            return var1 <= 0?"":var0.substring(0, var1);
        }
    }

    public static String parseServer(String var0) {
        if(var0 == null) {
            return null;
        } else {
            int var1 = var0.lastIndexOf("@");
            if(var1 + 1 > var0.length()) {
                return "";
            } else {
                int var2 = var0.indexOf("/");
                return var2 > 0 && var2 > var1?var0.substring(var1 + 1, var2):var0.substring(var1 + 1);
            }
        }
    }

    public static String parseResource(String var0) {
        if(var0 == null) {
            return null;
        } else {
            int var1 = var0.indexOf("/");
            return var1 + 1 <= var0.length() && var1 >= 0?var0.substring(var1 + 1):"";
        }
    }

    public static String parseBareAddress(String var0) {
        if(var0 == null) {
            return null;
        } else {
            int var1 = var0.indexOf("/");
            return var1 < 0?var0:(var1 == 0?"":var0.substring(0, var1));
        }
    }

    public static boolean isFullJID(String var0) {
        return parseName(var0).length() > 0 && parseServer(var0).length() > 0 && parseResource(var0).length() > 0;
    }

    public static String escapeNode(String var0) {
        if(var0 == null) {
            return null;
        } else {
            StringBuilder var1 = new StringBuilder(var0.length() + 8);
            int var2 = 0;

            for(int var3 = var0.length(); var2 < var3; ++var2) {
                char var4 = var0.charAt(var2);
                switch(var4) {
                    case '"':
                        var1.append("\\22");
                        break;
                    case '&':
                        var1.append("\\26");
                        break;
                    case '\'':
                        var1.append("\\27");
                        break;
                    case '/':
                        var1.append("\\2f");
                        break;
                    case ':':
                        var1.append("\\3a");
                        break;
                    case '<':
                        var1.append("\\3c");
                        break;
                    case '>':
                        var1.append("\\3e");
                        break;
                    case '@':
                        var1.append("\\40");
                        break;
                    case '\\':
                        var1.append("\\5c");
                        break;
                    default:
                        if(Character.isWhitespace(var4)) {
                            var1.append("\\20");
                        } else {
                            var1.append(var4);
                        }
                }
            }

            return var1.toString();
        }
    }

    public static String unescapeNode(String var0) {
        if(var0 == null) {
            return null;
        } else {
            char[] var1 = var0.toCharArray();
            StringBuilder var2 = new StringBuilder(var1.length);
            int var3 = 0;

            for(int var4 = var1.length; var3 < var4; ++var3) {
                char var5 = var0.charAt(var3);
                if(var5 == 92 && var3 + 2 < var4) {
                    char var6 = var1[var3 + 1];
                    char var7 = var1[var3 + 2];
                    if(var6 == 50) {
                        switch(var7) {
                            case '0':
                                var2.append(' ');
                                var3 += 2;
                                continue;
                            case '2':
                                var2.append('"');
                                var3 += 2;
                                continue;
                            case '6':
                                var2.append('&');
                                var3 += 2;
                                continue;
                            case '7':
                                var2.append('\'');
                                var3 += 2;
                                continue;
                            case 'f':
                                var2.append('/');
                                var3 += 2;
                                continue;
                        }
                    } else if(var6 == 51) {
                        switch(var7) {
                            case 'a':
                                var2.append(':');
                                var3 += 2;
                                continue;
                            case 'b':
                            case 'd':
                            default:
                                break;
                            case 'c':
                                var2.append('<');
                                var3 += 2;
                                continue;
                            case 'e':
                                var2.append('>');
                                var3 += 2;
                                continue;
                        }
                    } else if(var6 == 52) {
                        if(var7 == 48) {
                            var2.append("@");
                            var3 += 2;
                            continue;
                        }
                    } else if(var6 == 53 && var7 == 99) {
                        var2.append("\\");
                        var3 += 2;
                        continue;
                    }
                }

                var2.append(var5);
            }

            return var2.toString();
        }
    }

    public static String escapeForXML(String var0) {
        if(var0 == null) {
            return null;
        } else {
            int var2 = 0;
            int var3 = 0;
            char[] var4 = var0.toCharArray();
            int var5 = var4.length;

            StringBuilder var6;
            for(var6 = new StringBuilder((int)((double)var5 * 1.3D)); var2 < var5; ++var2) {
                char var1 = var4[var2];
                if(var1 <= 62) {
                    if(var1 == 60) {
                        if(var2 > var3) {
                            var6.append(var4, var3, var2 - var3);
                        }

                        var3 = var2 + 1;
                        var6.append(LT_ENCODE);
                    } else if(var1 == 62) {
                        if(var2 > var3) {
                            var6.append(var4, var3, var2 - var3);
                        }

                        var3 = var2 + 1;
                        var6.append(GT_ENCODE);
                    } else if(var1 == 38) {
                        if(var2 > var3) {
                            var6.append(var4, var3, var2 - var3);
                        }

                        if(var5 <= var2 + 5 || var4[var2 + 1] != 35 || !Character.isDigit(var4[var2 + 2]) || !Character.isDigit(var4[var2 + 3]) || !Character.isDigit(var4[var2 + 4]) || var4[var2 + 5] != 59) {
                            var3 = var2 + 1;
                            var6.append(AMP_ENCODE);
                        }
                    } else if(var1 == 34) {
                        if(var2 > var3) {
                            var6.append(var4, var3, var2 - var3);
                        }

                        var3 = var2 + 1;
                        var6.append(QUOTE_ENCODE);
                    } else if(var1 == 39) {
                        if(var2 > var3) {
                            var6.append(var4, var3, var2 - var3);
                        }

                        var3 = var2 + 1;
                        var6.append(APOS_ENCODE);
                    }
                }
            }

            if(var3 == 0) {
                return var0;
            } else {
                if(var2 > var3) {
                    var6.append(var4, var3, var2 - var3);
                }

                return var6.toString();
            }
        }
    }

    public static synchronized String hash(String var0) {
        if(digest == null) {
            try {
                digest = MessageDigest.getInstance("SHA-1");
            } catch (NoSuchAlgorithmException var3) {
                System.err.println("Failed to load the SHA-1 MessageDigest. Jive will be unable to function normally.");
            }
        }

        try {
            digest.update(var0.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException var2) {
            System.err.println(var2);
        }

        return encodeHex(digest.digest());
    }

    public static String encodeHex(byte[] var0) {
        StringBuilder var1 = new StringBuilder(var0.length * 2);
        byte[] var5 = var0;
        int var4 = var0.length;

        for(int var3 = 0; var3 < var4; ++var3) {
            byte var2 = var5[var3];
            if((var2 & 255) < 16) {
                var1.append("0");
            }

            var1.append(Integer.toString(var2 & 255, 16));
        }

        return var1.toString();
    }

    public static String encodeBase64(String var0) {
        byte[] var1 = null;

        try {
            var1 = var0.getBytes("ISO-8859-1");
        } catch (UnsupportedEncodingException var3) {
            var3.printStackTrace();
        }

        return encodeBase64(var1);
    }

    public static String encodeBase64(byte[] var0) {
        return encodeBase64(var0, false);
    }

    public static String encodeBase64(byte[] var0, boolean var1) {
        return encodeBase64(var0, 0, var0.length, var1);
    }

    public static String encodeBase64(byte[] var0, int var1, int var2, boolean var3) {
        return Base64.encodeBytes(var0, var1, var2, var3?0:8);
    }

//    public static byte[] decodeBase64(String var0) {
//        byte[] var1;
//        try {
//            var1 = var0.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException var3) {
//            var1 = var0.getBytes();
//        }
//
//        var1 = Base64.decode(var1, 0, var1.length, 0);
//        return var1;
//    }

    public static String randomString(int var0) {
        if(var0 < 1) {
            return null;
        } else {
            char[] var1 = new char[var0];

            for(int var2 = 0; var2 < var1.length; ++var2) {
                var1[var2] = numbersAndLetters[randGen.nextInt(71)];
            }

            return new String(var1);
        }
    }

    private StringUtils() {
    }

    private static class PatternCouplings {
        Pattern pattern;
        DateFormat formatter;
        boolean needToConvertTimeZone = false;

        public PatternCouplings(Pattern var1, DateFormat var2) {
            this.pattern = var1;
            this.formatter = var2;
        }

        public PatternCouplings(Pattern var1, DateFormat var2, boolean var3) {
            this.pattern = var1;
            this.formatter = var2;
            this.needToConvertTimeZone = var3;
        }

        public String convertTime(String var1) {
            return var1.charAt(var1.length() - 1) == 90?var1.replace("Z", "+0000"):var1.replaceAll("([\\+\\-]\\d\\d):(\\d\\d)", "$1$2");
        }
    }
}
