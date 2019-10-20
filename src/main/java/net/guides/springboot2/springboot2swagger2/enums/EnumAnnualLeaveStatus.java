package net.guides.springboot2.springboot2swagger2.enums;

public enum EnumAnnualLeaveStatus {
    COMMIT("COMMIT"),
    WAITING("WAITING"),
    REJECT("REJECT");

    private final String status;
    EnumAnnualLeaveStatus(String status)
    {
        this.status=status;
    }

    @Override
    public String toString() {
        return status;
    }

}
