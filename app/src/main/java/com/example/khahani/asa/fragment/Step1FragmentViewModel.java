package com.example.khahani.asa.fragment;

import com.mohamadamin.persianmaterialdatetimepicker.utils.PersianCalendar;

public class Step1FragmentViewModel {
    private String fromDate;
    private String numberNights;

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getNumberNights() {
        return numberNights;
    }

    public void setNumberNights(String numberNights) {
        this.numberNights = numberNights;
    }

    public String getToDate() {

        PersianCalendar calendar = new PersianCalendar();
        calendar.parse(fromDate);

        calendar.addPersianDate(PersianCalendar.DAY_OF_MONTH,
                Integer.parseInt(numberNights));

        return calendar.getPersianYear() + "/" + calendar.getPersianMonth() + "/" + calendar.getPersianDay();
    }
}
