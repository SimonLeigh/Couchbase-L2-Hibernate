package net.thumbtack.hibernate.test.entities;

/**
 * StatusEnum
 */
public enum StatusEnum {
    NEW("new"),
    PLACED("placed"),
    INPROCESS("inprocess"),
    SHIPPED("shipped"),
    CANCELLED("cancelled");

    private String asString;

    private StatusEnum(String asString) {
        this.asString = asString;
    }

    public String getLowCase() {
        return asString;
    }
}
