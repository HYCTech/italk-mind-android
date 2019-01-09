package com.lw.italk.utils;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public abstract class Packet {
    protected static final String DEFAULT_LANGUAGE = Locale.getDefault().getLanguage().toLowerCase();
    private static String DEFAULT_XML_NS = null;
    public static final String ID_NOT_AVAILABLE = "ID_NOT_AVAILABLE";
    public static final DateFormat XEP_0082_UTC_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    private static String prefix;
    private static long id;
    private String xmlns;
    private String packetID;
    private String to;
    private String from;
    private final List<PacketExtension> packetExtensions;
    private final Map<String, Object> properties;
    private XMPPError error;

    static {
        XEP_0082_UTC_FORMAT.setTimeZone(TimeZone.getTimeZone("UTC"));
        prefix = StringUtils.randomString(5) + "-";
        id = 0L;
    }

    public static synchronized String nextID() {
        return prefix + Long.toString((long) (id++));
    }

    public static void setDefaultXmlns(String var0) {
        DEFAULT_XML_NS = var0;
    }

    public Packet() {
        this.xmlns = DEFAULT_XML_NS;
        this.packetID = null;
        this.to = null;
        this.from = null;
        this.packetExtensions = new CopyOnWriteArrayList();
        this.properties = new HashMap();
        this.error = null;
    }

    public Packet(Packet var1) {
        this.xmlns = DEFAULT_XML_NS;
        this.packetID = null;
        this.to = null;
        this.from = null;
        this.packetExtensions = new CopyOnWriteArrayList();
        this.properties = new HashMap();
        this.error = null;
        this.packetID = var1.getPacketID();
        this.to = var1.getTo();
        this.from = var1.getFrom();
        this.xmlns = var1.xmlns;
        this.error = var1.error;
        Iterator var3 = var1.getExtensions().iterator();

        while (var3.hasNext()) {
            PacketExtension var2 = (PacketExtension) var3.next();
            this.addExtension(var2);
        }

    }

    public String getPacketID() {
        if ("ID_NOT_AVAILABLE".equals(this.packetID)) {
            return null;
        } else {
            if (this.packetID == null) {
                this.packetID = nextID();
            }

            return this.packetID;
        }
    }

    public void setPacketID(String var1) {
        this.packetID = var1;
    }

    public String getTo() {
        return this.to;
    }

    public void setTo(String var1) {
        this.to = var1;
    }

    public String getFrom() {
        return this.from;
    }

    public void setFrom(String var1) {
        this.from = var1;
    }

    public XMPPError getError() {
        return this.error;
    }

    public void setError(XMPPError var1) {
        this.error = var1;
    }

    public synchronized Collection<PacketExtension> getExtensions() {
        return this.packetExtensions == null ? Collections.emptyList() : Collections.unmodifiableList(new ArrayList(this.packetExtensions));
    }

    public PacketExtension getExtension(String var1) {
        return this.getExtension((String) null, var1);
    }

    public PacketExtension getExtension(String var1, String var2) {
        if (var2 == null) {
            return null;
        } else {
            Iterator var4 = this.packetExtensions.iterator();

            PacketExtension var3;
            do {
                do {
                    if (!var4.hasNext()) {
                        return null;
                    }

                    var3 = (PacketExtension) var4.next();
                } while (var1 != null && !var1.equals(var3.getElementName()));
            } while (!var2.equals(var3.getNamespace()));

            return var3;
        }
    }

    public void addExtension(PacketExtension var1) {
        if (var1 != null) {
            this.packetExtensions.add(var1);
        }
    }

    public void addExtensions(Collection<PacketExtension> var1) {
        if (var1 != null) {
            this.packetExtensions.addAll(var1);
        }
    }

    public void removeExtension(PacketExtension var1) {
        this.packetExtensions.remove(var1);
    }

    public synchronized Object getProperty(String var1) {
        return this.properties == null ? null : this.properties.get(var1);
    }

    public synchronized void setProperty(String var1, Object var2) {
        if (!(var2 instanceof Serializable)) {
            throw new IllegalArgumentException("Value must be serialiazble");
        } else {
            this.properties.put(var1, var2);
        }
    }

    public synchronized void deleteProperty(String var1) {
        if (this.properties != null) {
            this.properties.remove(var1);
        }
    }

    public synchronized Collection<String> getPropertyNames() {
        return this.properties == null ? Collections.emptySet() : Collections.unmodifiableSet(new HashSet(this.properties.keySet()));
    }

    public abstract String toXML();

    protected synchronized String getExtensionsXML() {
        StringBuilder var1 = new StringBuilder();
        Iterator var3 = this.getExtensions().iterator();

        while (var3.hasNext()) {
            PacketExtension var2 = (PacketExtension) var3.next();
            var1.append(var2.toXML());
        }

        if (this.properties != null && !this.properties.isEmpty()) {
            var1.append("<properties xmlns=\"http://www.jivesoftware.com/xmlns/xmpp/properties\">");

            for (var3 = this.getPropertyNames().iterator(); var3.hasNext(); var1.append("</property>")) {
                String var22 = (String) var3.next();
                Object var4 = this.getProperty(var22);
                var1.append("<property>");
                var1.append("<name>").append(StringUtils.escapeForXML(var22)).append("</name>");
                var1.append("<value type=\"");
                if (var4 instanceof Integer) {
                    var1.append("integer\">").append(var4).append("</value>");
                } else if (var4 instanceof Long) {
                    var1.append("long\">").append(var4).append("</value>");
                } else if (var4 instanceof Float) {
                    var1.append("float\">").append(var4).append("</value>");
                } else if (var4 instanceof Double) {
                    var1.append("double\">").append(var4).append("</value>");
                } else if (var4 instanceof Boolean) {
                    var1.append("boolean\">").append(var4).append("</value>");
                } else if (var4 instanceof String) {
                    var1.append("string\">");
                    var1.append(StringUtils.escapeForXML((String) var4));
                    var1.append("</value>");
                } else {
                    ByteArrayOutputStream var5 = null;
                    ObjectOutputStream var6 = null;

                    try {
                        var5 = new ByteArrayOutputStream();
                        var6 = new ObjectOutputStream(var5);
                        var6.writeObject(var4);
                        var1.append("java-object\">");
                        String var7 = StringUtils.encodeBase64(var5.toByteArray());
                        var1.append(var7).append("</value>");
                    } catch (Exception var20) {
                        var20.printStackTrace();
                    } finally {
                        if (var6 != null) {
                            try {
                                var6.close();
                            } catch (Exception var19) {
                                ;
                            }
                        }

                        if (var5 != null) {
                            try {
                                var5.close();
                            } catch (Exception var18) {
                                ;
                            }
                        }

                    }
                }
            }

            var1.append("</properties>");
        }

        return var1.toString();
    }

    public String getXmlns() {
        return this.xmlns;
    }

    public static String getDefaultLanguage() {
        return DEFAULT_LANGUAGE;
    }

    public boolean equals(Object var1) {
        if (this == var1) {
            return true;
        } else if (var1 != null && this.getClass() == var1.getClass()) {
            Packet var2;
            label87:
            {
                var2 = (Packet) var1;
                if (this.error != null) {
                    if (this.error.equals(var2.error)) {
                        break label87;
                    }
                } else if (var2.error == null) {
                    break label87;
                }

                return false;
            }

            label80:
            {
                if (this.from != null) {
                    if (this.from.equals(var2.from)) {
                        break label80;
                    }
                } else if (var2.from == null) {
                    break label80;
                }

                return false;
            }

            if (!this.packetExtensions.equals(var2.packetExtensions)) {
                return false;
            } else {
                label72:
                {
                    if (this.packetID != null) {
                        if (this.packetID.equals(var2.packetID)) {
                            break label72;
                        }
                    } else if (var2.packetID == null) {
                        break label72;
                    }

                    return false;
                }

                label65:
                {
                    if (this.properties != null) {
                        if (this.properties.equals(var2.properties)) {
                            break label65;
                        }
                    } else if (var2.properties == null) {
                        break label65;
                    }

                    return false;
                }

                if (this.to != null) {
                    if (!this.to.equals(var2.to)) {
                        return false;
                    }
                } else if (var2.to != null) {
                    return false;
                }

                boolean var10000;
                label106:
                {
                    if (this.xmlns != null) {
                        if (!this.xmlns.equals(var2.xmlns)) {
                            break label106;
                        }
                    } else if (var2.xmlns != null) {
                        break label106;
                    }

                    var10000 = true;
                    return var10000;
                }

                var10000 = false;
                return var10000;
            }
        } else {
            return false;
        }
    }

    public int hashCode() {
        int var1 = this.xmlns != null ? this.xmlns.hashCode() : 0;
        var1 = 31 * var1 + (this.packetID != null ? this.packetID.hashCode() : 0);
        var1 = 31 * var1 + (this.to != null ? this.to.hashCode() : 0);
        var1 = 31 * var1 + (this.from != null ? this.from.hashCode() : 0);
        var1 = 31 * var1 + this.packetExtensions.hashCode();
        var1 = 31 * var1 + this.properties.hashCode();
        var1 = 31 * var1 + (this.error != null ? this.error.hashCode() : 0);
        return var1;
    }
}
