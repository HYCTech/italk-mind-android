package com.lw.italk.greendao.model;

import java.util.Comparator;

public class PinyinComparator implements Comparator {

    @Override
    public int compare(Object arg0, Object arg1) {
        // 按照名字排序
        Contact user0 = (Contact) arg0;
        Contact user1 = (Contact) arg1;
        char catalog0 = 'a';
        char catalog1 = 'a';

        if (user0 != null && user0.getRemark() != null
                && user0.getRemark().length() > 1)
            catalog0 = PingYinUtil.converterToFirstSpell(user0.getRemark()).toUpperCase()
                    .charAt(0);

        if (user1 != null && user1.getRemark() != null
                && user1.getRemark().length() > 1)
            catalog1 = PingYinUtil.converterToFirstSpell(user1.getRemark()).toUpperCase()
                    .charAt(0);
        return catalog0 - catalog1;

    }

}
