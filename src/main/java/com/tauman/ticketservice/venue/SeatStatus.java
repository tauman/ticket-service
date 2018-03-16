package com.tauman.ticketservice.venue;

/**
 *
 * @author Steven Reich
 */
public class SeatStatus {

    public static final int NOT_HELD = -1;
    public static final String UNRESERVED = "UNRESERVED";

    private int seatHoldId;
    private String confirmationCode;

    public SeatStatus(int seatHoldId, String confirmationCode) {
        this.seatHoldId = seatHoldId;
        this.confirmationCode = confirmationCode;
    }

    public SeatStatus(int seatHoldId) {
        this(seatHoldId, UNRESERVED);
    }

    public SeatStatus(String confirmationCode) {
        this(NOT_HELD, confirmationCode);
    }

    public SeatStatus() {
        this(NOT_HELD, UNRESERVED);
    }

    public int getSeatHoldId() {
        return seatHoldId;
    }

    public void setSeatHoldId(int seatHoldId) {
        this.seatHoldId = seatHoldId;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    @Override
    public String toString() {
        return "SeatStatus{" + "seatHoldId=" + seatHoldId + ", confirmationCode=" + confirmationCode + '}';
    }
}
