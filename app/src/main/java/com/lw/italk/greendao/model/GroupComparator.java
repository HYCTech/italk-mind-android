package com.lw.italk.greendao.model;

import com.lw.italk.gson.group.GroupItem;

import java.util.Comparator;

public class GroupComparator implements Comparator {

	@Override
	public int compare(Object arg0, Object arg1) {
		// 按照名字排序
		GroupItem user0 = (GroupItem) arg0;
		GroupItem user1 = (GroupItem) arg1;
		String catalog0 = "";
		String catalog1 = "";

		if (user0 != null && user0.getName() != null
				&& user0.getName().length() > 1)
			catalog0 = PingYinUtil.converterToFirstSpell(user0.getName())
					.substring(0, 1);

		if (user1 != null && user1.getName() != null
				&& user1.getName().length() > 1)
			catalog1 = PingYinUtil.converterToFirstSpell(user1.getName())
					.substring(0, 1);
		int flag = catalog0.compareTo(catalog1);
		return flag;

	}

}
