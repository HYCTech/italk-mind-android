package com.lw.italk.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/8/16 0016.
 */

public class XMPPError {
    private int code;
    private XMPPError.Type type;
    private String condition;
    private String message;
    private List<PacketExtension> applicationExtensions = null;

    public XMPPError(XMPPError.Condition var1) {
        this.init(var1);
        this.message = null;
    }

    public XMPPError(XMPPError.Condition var1, String var2) {
        this.init(var1);
        this.message = var2;
    }

    public XMPPError(int var1) {
        this.code = var1;
        this.message = null;
    }

    public XMPPError(int var1, String var2) {
        this.code = var1;
        this.message = var2;
    }

    public XMPPError(int var1, XMPPError.Type var2, String var3, String var4, List<PacketExtension> var5) {
        this.code = var1;
        this.type = var2;
        this.condition = var3;
        this.message = var4;
        this.applicationExtensions = var5;
    }

    private void init(XMPPError.Condition var1) {
        XMPPError.ErrorSpecification var2 = XMPPError.ErrorSpecification.specFor(var1);
        this.condition = var1.value;
        if (var2 != null) {
            this.type = var2.getType();
            this.code = var2.getCode();
        }

    }

    public String getCondition() {
        return this.condition;
    }

    public XMPPError.Type getType() {
        return this.type;
    }

    public int getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String toXML() {
        StringBuilder var1 = new StringBuilder();
        var1.append("<error code=\"").append(this.code).append("\"");
        if (this.type != null) {
            var1.append(" type=\"");
            var1.append(this.type.name());
            var1.append("\"");
        }

        var1.append(">");
        if (this.condition != null) {
            var1.append("<").append(this.condition);
            var1.append(" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\"/>");
        }

        if (this.message != null) {
            var1.append("<text xml:lang=\"en\" xmlns=\"urn:ietf:params:xml:ns:xmpp-stanzas\">");
            var1.append(this.message);
            var1.append("</text>");
        }

        Iterator var3 = this.getExtensions().iterator();

        while (var3.hasNext()) {
            PacketExtension var2 = (PacketExtension) var3.next();
            var1.append(var2.toXML());
        }

        var1.append("</error>");
        return var1.toString();
    }

    public String toString() {
        StringBuilder var1 = new StringBuilder();
        if (this.condition != null) {
            var1.append(this.condition);
        }

        var1.append("(").append(this.code).append(")");
        if (this.message != null) {
            var1.append(" ").append(this.message);
        }

        return var1.toString();
    }

    public synchronized List<PacketExtension> getExtensions() {
        return this.applicationExtensions;
    }

    public synchronized PacketExtension getExtension(String var1, String var2) {
        if (this.applicationExtensions != null && var1 != null && var2 != null) {
            Iterator var4 = this.applicationExtensions.iterator();

            PacketExtension var3;
            do {
                if (!var4.hasNext()) {
                    return null;
                }

                var3 = (PacketExtension) var4.next();
            } while (!var1.equals(var3.getElementName()) || !var2.equals(var3.getNamespace()));

            return var3;
        } else {
            return null;
        }
    }

    public synchronized void addExtension(PacketExtension var1) {
        if (this.applicationExtensions == null) {
            this.applicationExtensions = new ArrayList();
        }

        this.applicationExtensions.add(var1);
    }

    public synchronized void setExtension(List<PacketExtension> var1) {
        this.applicationExtensions = var1;
    }

    public static class Condition {
        public static final XMPPError.Condition interna_server_error = new XMPPError.Condition("internal-server-error");
        public static final XMPPError.Condition forbidden = new XMPPError.Condition("forbidden");
        public static final XMPPError.Condition bad_request = new XMPPError.Condition("bad-request");
        public static final XMPPError.Condition conflict = new XMPPError.Condition("conflict");
        public static final XMPPError.Condition feature_not_implemented = new XMPPError.Condition("feature-not-implemented");
        public static final XMPPError.Condition gone = new XMPPError.Condition("gone");
        public static final XMPPError.Condition item_not_found = new XMPPError.Condition("item-not-found");
        public static final XMPPError.Condition jid_malformed = new XMPPError.Condition("jid-malformed");
        public static final XMPPError.Condition no_acceptable = new XMPPError.Condition("not-acceptable");
        public static final XMPPError.Condition not_allowed = new XMPPError.Condition("not-allowed");
        public static final XMPPError.Condition not_authorized = new XMPPError.Condition("not-authorized");
        public static final XMPPError.Condition payment_required = new XMPPError.Condition("payment-required");
        public static final XMPPError.Condition recipient_unavailable = new XMPPError.Condition("recipient-unavailable");
        public static final XMPPError.Condition redirect = new XMPPError.Condition("redirect");
        public static final XMPPError.Condition registration_required = new XMPPError.Condition("registration-required");
        public static final XMPPError.Condition remote_server_error = new XMPPError.Condition("remote-server-error");
        public static final XMPPError.Condition remote_server_not_found = new XMPPError.Condition("remote-server-not-found");
        public static final XMPPError.Condition remote_server_timeout = new XMPPError.Condition("remote-server-timeout");
        public static final XMPPError.Condition resource_constraint = new XMPPError.Condition("resource-constraint");
        public static final XMPPError.Condition service_unavailable = new XMPPError.Condition("service-unavailable");
        public static final XMPPError.Condition subscription_required = new XMPPError.Condition("subscription-required");
        public static final XMPPError.Condition undefined_condition = new XMPPError.Condition("undefined-condition");
        public static final XMPPError.Condition unexpected_request = new XMPPError.Condition("unexpected-request");
        public static final XMPPError.Condition request_timeout = new XMPPError.Condition("request-timeout");
        private String value;

        public Condition(String var1) {
            this.value = var1;
        }

        public String toString() {
            return this.value;
        }
    }

    private static class ErrorSpecification {
        private int code;
        private XMPPError.Type type;
        private XMPPError.Condition condition;
        private static Map<Condition, ErrorSpecification> instances = errorSpecifications();

        private ErrorSpecification(XMPPError.Condition var1, XMPPError.Type var2, int var3) {
            this.code = var3;
            this.type = var2;
            this.condition = var1;
        }

        private static Map<XMPPError.Condition, XMPPError.ErrorSpecification> errorSpecifications() {
            HashMap var0 = new HashMap(22);
            var0.put(XMPPError.Condition.interna_server_error, new XMPPError.ErrorSpecification(XMPPError.Condition.interna_server_error, XMPPError.Type.WAIT, 500));
            var0.put(XMPPError.Condition.forbidden, new XMPPError.ErrorSpecification(XMPPError.Condition.forbidden, XMPPError.Type.AUTH, 403));
            var0.put(XMPPError.Condition.bad_request, new XMPPError.ErrorSpecification(XMPPError.Condition.bad_request, XMPPError.Type.MODIFY, 400));
            var0.put(XMPPError.Condition.item_not_found, new XMPPError.ErrorSpecification(XMPPError.Condition.item_not_found, XMPPError.Type.CANCEL, 404));
            var0.put(XMPPError.Condition.conflict, new XMPPError.ErrorSpecification(XMPPError.Condition.conflict, XMPPError.Type.CANCEL, 409));
            var0.put(XMPPError.Condition.feature_not_implemented, new XMPPError.ErrorSpecification(XMPPError.Condition.feature_not_implemented, XMPPError.Type.CANCEL, 501));
            var0.put(XMPPError.Condition.gone, new XMPPError.ErrorSpecification(XMPPError.Condition.gone, XMPPError.Type.MODIFY, 302));
            var0.put(XMPPError.Condition.jid_malformed, new XMPPError.ErrorSpecification(XMPPError.Condition.jid_malformed, XMPPError.Type.MODIFY, 400));
            var0.put(XMPPError.Condition.no_acceptable, new XMPPError.ErrorSpecification(XMPPError.Condition.no_acceptable, XMPPError.Type.MODIFY, 406));
            var0.put(XMPPError.Condition.not_allowed, new XMPPError.ErrorSpecification(XMPPError.Condition.not_allowed, XMPPError.Type.CANCEL, 405));
            var0.put(XMPPError.Condition.not_authorized, new XMPPError.ErrorSpecification(XMPPError.Condition.not_authorized, XMPPError.Type.AUTH, 401));
            var0.put(XMPPError.Condition.payment_required, new XMPPError.ErrorSpecification(XMPPError.Condition.payment_required, XMPPError.Type.AUTH, 402));
            var0.put(XMPPError.Condition.recipient_unavailable, new XMPPError.ErrorSpecification(XMPPError.Condition.recipient_unavailable, XMPPError.Type.WAIT, 404));
            var0.put(XMPPError.Condition.redirect, new XMPPError.ErrorSpecification(XMPPError.Condition.redirect, XMPPError.Type.MODIFY, 302));
            var0.put(XMPPError.Condition.registration_required, new XMPPError.ErrorSpecification(XMPPError.Condition.registration_required, XMPPError.Type.AUTH, 407));
            var0.put(XMPPError.Condition.remote_server_not_found, new XMPPError.ErrorSpecification(XMPPError.Condition.remote_server_not_found, XMPPError.Type.CANCEL, 404));
            var0.put(XMPPError.Condition.remote_server_timeout, new XMPPError.ErrorSpecification(XMPPError.Condition.remote_server_timeout, XMPPError.Type.WAIT, 504));
            var0.put(XMPPError.Condition.remote_server_error, new XMPPError.ErrorSpecification(XMPPError.Condition.remote_server_error, XMPPError.Type.CANCEL, 502));
            var0.put(XMPPError.Condition.resource_constraint, new XMPPError.ErrorSpecification(XMPPError.Condition.resource_constraint, XMPPError.Type.WAIT, 500));
            var0.put(XMPPError.Condition.service_unavailable, new XMPPError.ErrorSpecification(XMPPError.Condition.service_unavailable, XMPPError.Type.CANCEL, 503));
            var0.put(XMPPError.Condition.subscription_required, new XMPPError.ErrorSpecification(XMPPError.Condition.subscription_required, XMPPError.Type.AUTH, 407));
            var0.put(XMPPError.Condition.undefined_condition, new XMPPError.ErrorSpecification(XMPPError.Condition.undefined_condition, XMPPError.Type.WAIT, 500));
            var0.put(XMPPError.Condition.unexpected_request, new XMPPError.ErrorSpecification(XMPPError.Condition.unexpected_request, XMPPError.Type.WAIT, 400));
            var0.put(XMPPError.Condition.request_timeout, new XMPPError.ErrorSpecification(XMPPError.Condition.request_timeout, XMPPError.Type.CANCEL, 408));
            return var0;
        }

        protected static XMPPError.ErrorSpecification specFor(XMPPError.Condition var0) {
            return (XMPPError.ErrorSpecification) instances.get(var0);
        }

        protected XMPPError.Condition getCondition() {
            return this.condition;
        }

        protected XMPPError.Type getType() {
            return this.type;
        }

        protected int getCode() {
            return this.code;
        }
    }

    public static enum Type {
        WAIT,
        CANCEL,
        MODIFY,
        AUTH,
        CONTINUE;

        private Type() {
        }
    }
}
