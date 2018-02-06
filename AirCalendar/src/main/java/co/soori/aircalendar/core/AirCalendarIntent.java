/***********************************************************************************
 * The MIT License (MIT)
 * <p/>
 * Copyright (c) 2017 LeeYongBeom
 * https://github.com/yongbeam
 * <p/>
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * <p/>
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * <p/>
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 ***********************************************************************************/
package co.soori.aircalendar.core;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Parcelable;

import co.soori.aircalendar.AirCalendarDatePickerActivity;

import java.util.ArrayList;

public class AirCalendarIntent  extends Intent implements Parcelable {

    private AirCalendarIntent() {
    }

    private AirCalendarIntent(Intent o) {
        super(o);
    }

    private AirCalendarIntent(String action) {
        super(action);
    }

    private AirCalendarIntent(String action, Uri uri) {
        super(action, uri);
    }

    private AirCalendarIntent(Context packageContext, Class<?> cls) {
        super(packageContext, cls);
    }

    public AirCalendarIntent(Context packageContext) {
        super(packageContext, AirCalendarDatePickerActivity.class);
    }

    public void isSelect(boolean isSelect) {
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_IS_SELECT, isSelect);
    }

    public void isBooking(boolean isBooking) {
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_IS_BOOIKNG, isBooking);
    }

    public void isMonthLabels(boolean isLabel) {
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_IS_MONTH_LABEL, isLabel);
    }

    public void isSingleSelect(boolean isSingle) {
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_IS_SINGLE_SELECT, isSingle);
    }

    public void setBookingDateArray(ArrayList<String> arrays) {
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_BOOKING_DATES, arrays);
    }

    public void setStartDate(int year , int month , int day){
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_SELECT_DATE_SY, year);
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_SELECT_DATE_SM, month);
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_SELECT_DATE_SD, day);

    }

    public void setEndDate(int year , int month , int day){
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_SELECT_DATE_EY, year);
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_SELECT_DATE_EM, month);
        this.putExtra(AirCalendarDatePickerActivity.EXTRA_SELECT_DATE_ED, day);
    }
}