package com.lw.italk.utils;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public interface PacketExtension {
    String getElementName();

    String getNamespace();

    String toXML();
}
