package com.lw.italk.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Spannable;
import android.text.Spannable.Factory;
import android.text.style.ImageSpan;

import com.lw.italk.R;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//表情
public class SmileUtils {
    public static final String f_static_00 = "[龇牙]";
    public static final String f_static_01 = "[调皮]";
    public static final String f_static_02 = "[流汗]";
    public static final String f_static_03 = "[偷笑]";
    public static final String f_static_04 = "[再见]";
    public static final String f_static_05 = "[敲打]";
    public static final String f_static_06 = "[擦汗]";
    public static final String f_static_07 = "[猪头]";
    public static final String f_static_08 = "[玫瑰]";
    public static final String f_static_09 = "[流泪]";
    public static final String f_static_010 = "[大哭]";
    public static final String f_static_011 = "[嘘]";
    public static final String f_static_012 = "[酷]";
    public static final String f_static_013 = "[抓狂]";
    public static final String f_static_014 = "[委屈]";
    public static final String f_static_015 = "[便便]";
    public static final String f_static_016 = "[炸弹]";
    public static final String f_static_017 = "[菜刀]";
    public static final String f_static_018 = "[可爱]";
    public static final String f_static_019 = "[色]";
    public static final String f_static_020 = "[删除]";

    public static final String f_static_021 = "[害羞]";
    public static final String f_static_022 = "[得意]";
    public static final String f_static_023 = "[吐]";
    public static final String f_static_024 = "[微笑]";
    public static final String f_static_025 = "[怒]";
    public static final String f_static_026 = "[尴尬]";
    public static final String f_static_027 = "[惊恐]";
    public static final String f_static_028 = "[冷汗]";
    public static final String f_static_029 = "[爱心]";
    public static final String f_static_030 = "[示爱]";
    public static final String f_static_031 = "[白眼]";
    public static final String f_static_032 = "[傲慢]";
    public static final String f_static_033 = "[难过]";
    public static final String f_static_034 = "[惊讶]";
    public static final String f_static_035 = "[疑问]";
    public static final String f_static_036 = "[困]";
    public static final String f_static_037 = "[么么哒]";
    public static final String f_static_038 = "[憨笑]";
    public static final String f_static_039 = "[爱情]";
    public static final String f_static_040 = "[衰]";
    public static final String f_static_041 = "[删除]";

    public static final String f_static_042 = "[撇嘴]";
    public static final String f_static_043 = "[阴险]";
    public static final String f_static_044 = "[奋斗]";
    public static final String f_static_045 = "[发呆]";
    public static final String f_static_046 = "[右哼哼]";
    public static final String f_static_047 = "[抱抱]";
    public static final String f_static_048 = "[坏笑]";
    public static final String f_static_049 = "[飞吻]";
    public static final String f_static_050 = "[鄙视]";
    public static final String f_static_051 = "[晕]";
    public static final String f_static_052 = "[大兵]";
    public static final String f_static_053 = "[可怜]";
    public static final String f_static_054 = "[强]";
    public static final String f_static_055 = "[弱]";
    public static final String f_static_056 = "[握手]";
    public static final String f_static_057 = "[胜利]";
    public static final String f_static_058 = "[抱拳]";
    public static final String f_static_059 = "[凋谢]";
    public static final String f_static_060 = "[米饭]";
    public static final String f_static_061 = "[蛋糕]";
    public static final String f_static_062 = "[删除]";

    public static final String f_static_063 = "[西瓜]";
    public static final String f_static_064 = "[啤酒]";
    public static final String f_static_065 = "[瓢虫]";
    public static final String f_static_066 = "[勾引]";
    public static final String f_static_067 = "[OK]";
    public static final String f_static_068 = "[爱你]";
    public static final String f_static_069 = "[咖啡]";
    public static final String f_static_070 = "[月亮]";
    public static final String f_static_071 = "[刀]";
    public static final String f_static_072 = "[发抖]";
    public static final String f_static_073 = "[差劲]";
    public static final String f_static_074 = "[拳头]";
    public static final String f_static_075 = "[心碎了]";
    public static final String f_static_076 = "[太阳]";
    public static final String f_static_077 = "[礼物]";
    public static final String f_static_078 = "[皮球]";
    public static final String f_static_079 = "[骷髅]";
    public static final String f_static_080 = "[挥手]";
    public static final String f_static_081 = "[闪电]";
    public static final String f_static_082 = "[饥饿]";
    public static final String f_static_083 = "[删除]";

    public static final String f_static_084 = "[睡觉]";
    public static final String f_static_085 = "[咒骂]";
    public static final String f_static_086 = "[抓狂]";
    public static final String f_static_087 = "[抠鼻]";
    public static final String f_static_088 = "[鼓掌]";
    public static final String f_static_089 = "[糗大了]";
    public static final String f_static_090 = "[左哼哼]";
    public static final String f_static_091 = "[打哈欠]";
    public static final String f_static_092 = "[快哭了]";
    public static final String f_static_093 = "[吓]";
    public static final String f_static_094 = "[篮球]";
    public static final String f_static_095 = "[乒乓]";
    public static final String f_static_096 = "[NO]";
    public static final String f_static_097 = "[跳跳]";
    public static final String f_static_098 = "[怄火]";
    public static final String f_static_099 = "[转圈]";
    public static final String f_static_0100 = "[磕头]";
    public static final String f_static_0101 = "[回头]";
    public static final String f_static_0102 = "[跳绳]";
    public static final String f_static_0103 = "[激动]";
    public static final String f_static_0104 = "[删除]";

    public static final String f_static_0105 = "[街舞]";
    public static final String f_static_0106 = "[献吻]";
    public static final String f_static_0107 = "[左太极]";
    public static final String f_static_0108 = "[右太极]";
    public static final String f_static_0109 = "[闭嘴]";
    public static final String f_static_0110 = "[猫咪]";
    public static final String f_static_0111 = "[红双喜]";
    public static final String f_static_0112 = "[鞭炮]";
    public static final String f_static_0113 = "[红灯笼]";
    public static final String f_static_0114 = "[麻将]";
    public static final String f_static_0115 = "[麦克风]";
    public static final String f_static_0116 = "[礼品袋]";
    public static final String f_static_0117 = "[信封]";
    public static final String f_static_0118 = "[象棋]";
    public static final String f_static_0119 = "[彩带]";
    public static final String f_static_0120 = "[蜡烛]";
    public static final String f_static_0121 = "[爆筋]";
    public static final String f_static_0122 = "[棒棒糖]";
    public static final String f_static_0123 = "[奶瓶]";
    public static final String f_static_0124 = "[面条]";
    public static final String f_static_0125 = "[删除]";

    public static final String f_static_0126 = "[香蕉]";
    public static final String f_static_0127 = "[飞机]";
    public static final String f_static_0128 = "[左车头]";
    public static final String f_static_0129 = "[车厢]";
    public static final String f_static_0130 = "[右车头]";
    public static final String f_static_0131 = "[多云]";
    public static final String f_static_0132 = "[下雨]";
    public static final String f_static_0133 = "[钞票]";
    public static final String f_static_0134 = "[熊猫]";
    public static final String f_static_0135 = "[灯泡]";
    public static final String f_static_0136 = "[风车]";
    public static final String f_static_0137 = "[闹钟]";
    public static final String f_static_0138 = "[彩球]";
    public static final String f_static_0139 = "[钻戒]";
    public static final String f_static_0140 = "[沙发]";
    public static final String f_static_0141 = "[纸巾]";
    public static final String f_static_0142 = "[手枪]";
    public static final String f_static_0143 = "[青蛙]";
    public static final String f_static_0144 = "[雨伞]";
    public static final String f_static_0145 = "[药]";
    public static final String f_static_0146 = "[删除]";


    private static final Factory spannableFactory = Spannable.Factory
            .getInstance();

    private static final Map<Pattern, Integer> emoticons = new HashMap<Pattern, Integer>();

    static {
        addPattern(emoticons, f_static_00, R.drawable.f_static_00);
        addPattern(emoticons, f_static_01, R.drawable.f_static_01);
        addPattern(emoticons, f_static_02, R.drawable.f_static_02);
        addPattern(emoticons, f_static_03, R.drawable.f_static_03);
        addPattern(emoticons, f_static_04, R.drawable.f_static_04);
        addPattern(emoticons, f_static_05, R.drawable.f_static_05);
        addPattern(emoticons, f_static_06, R.drawable.f_static_06);
        addPattern(emoticons, f_static_07, R.drawable.f_static_07);
        addPattern(emoticons, f_static_08, R.drawable.f_static_08);
        addPattern(emoticons, f_static_09, R.drawable.f_static_09);
        addPattern(emoticons, f_static_010, R.drawable.f_static_010);
        addPattern(emoticons, f_static_011, R.drawable.f_static_011);
        addPattern(emoticons, f_static_012, R.drawable.f_static_012);
        addPattern(emoticons, f_static_013, R.drawable.f_static_013);
        addPattern(emoticons, f_static_014, R.drawable.f_static_014);
        addPattern(emoticons, f_static_015, R.drawable.f_static_015);
        addPattern(emoticons, f_static_016, R.drawable.f_static_016);
        addPattern(emoticons, f_static_017, R.drawable.f_static_017);
        addPattern(emoticons, f_static_018, R.drawable.f_static_018);
        addPattern(emoticons, f_static_019, R.drawable.f_static_019);
        addPattern(emoticons, f_static_020, R.drawable.f_static_020);
        addPattern(emoticons, f_static_021, R.drawable.f_static_021);
        addPattern(emoticons, f_static_022, R.drawable.f_static_022);
        addPattern(emoticons, f_static_023, R.drawable.f_static_023);
        addPattern(emoticons, f_static_024, R.drawable.f_static_024);
        addPattern(emoticons, f_static_025, R.drawable.f_static_025);
        addPattern(emoticons, f_static_026, R.drawable.f_static_026);
        addPattern(emoticons, f_static_027, R.drawable.f_static_027);
        addPattern(emoticons, f_static_028, R.drawable.f_static_028);
        addPattern(emoticons, f_static_029, R.drawable.f_static_029);
        addPattern(emoticons, f_static_030, R.drawable.f_static_030);
        addPattern(emoticons, f_static_031, R.drawable.f_static_031);
        addPattern(emoticons, f_static_032, R.drawable.f_static_032);
        addPattern(emoticons, f_static_033, R.drawable.f_static_033);
        addPattern(emoticons, f_static_034, R.drawable.f_static_034);
        addPattern(emoticons, f_static_035, R.drawable.f_static_035);
        addPattern(emoticons, f_static_036, R.drawable.f_static_036);
        addPattern(emoticons, f_static_037, R.drawable.f_static_037);
        addPattern(emoticons, f_static_038, R.drawable.f_static_038);
        addPattern(emoticons, f_static_039, R.drawable.f_static_039);
        addPattern(emoticons, f_static_040, R.drawable.f_static_040);
        addPattern(emoticons, f_static_041, R.drawable.f_static_041);
        addPattern(emoticons, f_static_042, R.drawable.f_static_042);
        addPattern(emoticons, f_static_043, R.drawable.f_static_043);
        addPattern(emoticons, f_static_044, R.drawable.f_static_044);
        addPattern(emoticons, f_static_045, R.drawable.f_static_045);
        addPattern(emoticons, f_static_046, R.drawable.f_static_046);
        addPattern(emoticons, f_static_047, R.drawable.f_static_047);
        addPattern(emoticons, f_static_048, R.drawable.f_static_048);
        addPattern(emoticons, f_static_049, R.drawable.f_static_049);
        addPattern(emoticons, f_static_050, R.drawable.f_static_050);
        addPattern(emoticons, f_static_051, R.drawable.f_static_051);
        addPattern(emoticons, f_static_052, R.drawable.f_static_052);
        addPattern(emoticons, f_static_053, R.drawable.f_static_053);
        addPattern(emoticons, f_static_054, R.drawable.f_static_054);
        addPattern(emoticons, f_static_055, R.drawable.f_static_055);
        addPattern(emoticons, f_static_056, R.drawable.f_static_056);
        addPattern(emoticons, f_static_057, R.drawable.f_static_057);
        addPattern(emoticons, f_static_058, R.drawable.f_static_058);
        addPattern(emoticons, f_static_059, R.drawable.f_static_059);
        addPattern(emoticons, f_static_060, R.drawable.f_static_060);
        addPattern(emoticons, f_static_061, R.drawable.f_static_061);
        addPattern(emoticons, f_static_062, R.drawable.f_static_062);
        addPattern(emoticons, f_static_063, R.drawable.f_static_063);
        addPattern(emoticons, f_static_064, R.drawable.f_static_064);
        addPattern(emoticons, f_static_065, R.drawable.f_static_065);
        addPattern(emoticons, f_static_066, R.drawable.f_static_066);
        addPattern(emoticons, f_static_067, R.drawable.f_static_067);
        addPattern(emoticons, f_static_068, R.drawable.f_static_068);
        addPattern(emoticons, f_static_069, R.drawable.f_static_069);
        addPattern(emoticons, f_static_070, R.drawable.f_static_070);
        addPattern(emoticons, f_static_071, R.drawable.f_static_071);
        addPattern(emoticons, f_static_072, R.drawable.f_static_072);
        addPattern(emoticons, f_static_073, R.drawable.f_static_073);
        addPattern(emoticons, f_static_074, R.drawable.f_static_074);
        addPattern(emoticons, f_static_075, R.drawable.f_static_075);
        addPattern(emoticons, f_static_076, R.drawable.f_static_076);
        addPattern(emoticons, f_static_077, R.drawable.f_static_077);
        addPattern(emoticons, f_static_078, R.drawable.f_static_078);
        addPattern(emoticons, f_static_079, R.drawable.f_static_079);
        addPattern(emoticons, f_static_080, R.drawable.f_static_080);
        addPattern(emoticons, f_static_081, R.drawable.f_static_081);
        addPattern(emoticons, f_static_082, R.drawable.f_static_082);
        addPattern(emoticons, f_static_083, R.drawable.f_static_083);
        addPattern(emoticons, f_static_084, R.drawable.f_static_084);
        addPattern(emoticons, f_static_085, R.drawable.f_static_085);
        addPattern(emoticons, f_static_086, R.drawable.f_static_086);
        addPattern(emoticons, f_static_087, R.drawable.f_static_087);
        addPattern(emoticons, f_static_088, R.drawable.f_static_088);
        addPattern(emoticons, f_static_089, R.drawable.f_static_089);
        addPattern(emoticons, f_static_090, R.drawable.f_static_090);
        addPattern(emoticons, f_static_091, R.drawable.f_static_091);
        addPattern(emoticons, f_static_092, R.drawable.f_static_092);
        addPattern(emoticons, f_static_093, R.drawable.f_static_093);
        addPattern(emoticons, f_static_094, R.drawable.f_static_094);
        addPattern(emoticons, f_static_095, R.drawable.f_static_095);
        addPattern(emoticons, f_static_096, R.drawable.f_static_096);
        addPattern(emoticons, f_static_097, R.drawable.f_static_097);
        addPattern(emoticons, f_static_098, R.drawable.f_static_098);
        addPattern(emoticons, f_static_099, R.drawable.f_static_099);
        addPattern(emoticons, f_static_0100, R.drawable.f_static_0100);
        addPattern(emoticons, f_static_0101, R.drawable.f_static_0101);
        addPattern(emoticons, f_static_0102, R.drawable.f_static_0102);
        addPattern(emoticons, f_static_0103, R.drawable.f_static_0103);
        addPattern(emoticons, f_static_0104, R.drawable.f_static_0104);
        addPattern(emoticons, f_static_0105, R.drawable.f_static_0105);
        addPattern(emoticons, f_static_0106, R.drawable.f_static_0106);
        addPattern(emoticons, f_static_0107, R.drawable.f_static_0107);
        addPattern(emoticons, f_static_0108, R.drawable.f_static_0108);
        addPattern(emoticons, f_static_0109, R.drawable.f_static_0109);
        addPattern(emoticons, f_static_0110, R.drawable.f_static_0110);
        addPattern(emoticons, f_static_0111, R.drawable.f_static_0111);
        addPattern(emoticons, f_static_0112, R.drawable.f_static_0112);
        addPattern(emoticons, f_static_0113, R.drawable.f_static_0113);
        addPattern(emoticons, f_static_0114, R.drawable.f_static_0114);
        addPattern(emoticons, f_static_0115, R.drawable.f_static_0115);
        addPattern(emoticons, f_static_0116, R.drawable.f_static_0116);
        addPattern(emoticons, f_static_0117, R.drawable.f_static_0117);
        addPattern(emoticons, f_static_0118, R.drawable.f_static_0118);
        addPattern(emoticons, f_static_0119, R.drawable.f_static_0119);
        addPattern(emoticons, f_static_0120, R.drawable.f_static_0120);
        addPattern(emoticons, f_static_0121, R.drawable.f_static_0121);
        addPattern(emoticons, f_static_0122, R.drawable.f_static_0122);
        addPattern(emoticons, f_static_0123, R.drawable.f_static_0123);
        addPattern(emoticons, f_static_0124, R.drawable.f_static_0124);
        addPattern(emoticons, f_static_0125, R.drawable.f_static_0125);
        addPattern(emoticons, f_static_0126, R.drawable.f_static_0126);
        addPattern(emoticons, f_static_0127, R.drawable.f_static_0127);
        addPattern(emoticons, f_static_0128, R.drawable.f_static_0128);
        addPattern(emoticons, f_static_0129, R.drawable.f_static_0129);
        addPattern(emoticons, f_static_0130, R.drawable.f_static_0130);
        addPattern(emoticons, f_static_0131, R.drawable.f_static_0131);
        addPattern(emoticons, f_static_0132, R.drawable.f_static_0132);
        addPattern(emoticons, f_static_0133, R.drawable.f_static_0133);
        addPattern(emoticons, f_static_0134, R.drawable.f_static_0134);
        addPattern(emoticons, f_static_0135, R.drawable.f_static_0135);
        addPattern(emoticons, f_static_0136, R.drawable.f_static_0136);
        addPattern(emoticons, f_static_0137, R.drawable.f_static_0137);
        addPattern(emoticons, f_static_0138, R.drawable.f_static_0138);
        addPattern(emoticons, f_static_0139, R.drawable.f_static_0139);
        addPattern(emoticons, f_static_0140, R.drawable.f_static_0140);
        addPattern(emoticons, f_static_0141, R.drawable.f_static_0141);
        addPattern(emoticons, f_static_0142, R.drawable.f_static_0142);
        addPattern(emoticons, f_static_0143, R.drawable.f_static_0143);
        addPattern(emoticons, f_static_0144, R.drawable.f_static_0144);
        addPattern(emoticons, f_static_0145, R.drawable.f_static_0145);
        addPattern(emoticons, f_static_0146, R.drawable.f_static_0146);
    }

    private static void addPattern(Map<Pattern, Integer> map, String smile,
                                   int resource) {
        map.put(Pattern.compile(Pattern.quote(smile)), resource);
    }

    /**
     * replace existing spannable with smiles
     *
     * @param context
     * @param spannable
     * @return
     */
    public static boolean addSmiles(Context context, Spannable spannable) {
        boolean hasChanges = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(spannable);
            while (matcher.find()) {
                boolean set = true;
                for (ImageSpan span : spannable.getSpans(matcher.start(),
                        matcher.end(), ImageSpan.class))
                    if (spannable.getSpanStart(span) >= matcher.start()
                            && spannable.getSpanEnd(span) <= matcher.end())
                        spannable.removeSpan(span);
                    else {
                        set = false;
                        break;
                    }
                if (set) {
                    hasChanges = true;
                    Drawable drawable = context.getResources().getDrawable(
                            entry.getValue());
                    drawable.setBounds(0, 0, 70, 70);// 这里设置图片的大小
                    ImageSpan imageSpan = new ImageSpan(drawable,
                            ImageSpan.ALIGN_BOTTOM);
                    spannable.setSpan(imageSpan, matcher.start(),
                            matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
            }
        }
        return hasChanges;
    }

    public static Spannable getSmiledText(Context context, CharSequence text) {
        if (text == null) {
            text = "";
        }
        Spannable spannable = spannableFactory.newSpannable(text);
        addSmiles(context, spannable);
        return spannable;
    }

    public static boolean containsKey(String key) {
        boolean b = false;
        for (Entry<Pattern, Integer> entry : emoticons.entrySet()) {
            Matcher matcher = entry.getKey().matcher(key);
            if (matcher.find()) {
                b = true;
                break;
            }
        }

        return b;
    }

}
