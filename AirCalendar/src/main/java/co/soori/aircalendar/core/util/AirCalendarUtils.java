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
package co.soori.aircalendar.core.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AirCalendarUtils {

    /**
     * @return
     */
    public static boolean isWeekend(String strDate) {
        int weekNumber = getWeekNumberInt(strDate, "yyyy-MM-dd");
        if (weekNumber == 6 || weekNumber == 7) {
            return true;
        }
        return false;
    }

    /**
     * @param strDate
     * @param inFormat
     * @return String
     */
    public static int getWeekNumberInt(String strDate, String inFormat) {
        int week = 0;
        Calendar calendar = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat(inFormat);
        try {
            calendar.setTime(df.parse(strDate));
        } catch (Exception e) {
            return 0;
        }
        int intTemp = calendar.get(Calendar.DAY_OF_WEEK) - 1;
        switch (intTemp) {
            case 0:
                week = 7;
                break;
            case 1:
                week = 1;
                break;
            case 2:
                week = 2;
                break;
            case 3:
                week = 3;
                break;
            case 4:
                week = 4;
                break;
            case 5:
                week = 5;
                break;
            case 6:
                week = 6;
                break;
        }
        return week;
    }

    public static boolean checkStartDateToEndDate(String startDate , String endDate){
        SimpleDateFormat format = new SimpleDateFormat( "yyyy-MM-dd" );
        Date day1 = null;
        Date day2 = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            day1 = format.parse( startDate );
            day2 = format.parse( endDate );
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int compare = day1.compareTo( day2 );
        if ( compare > 0 )
        {
            return true;
        }
        else if ( compare < 0 )
        {
            return false;
        }
        else
        {
            return false;
        }
    }

    public static int getDiffDay(String startDate , String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date sDate;
        Date eDate;
        try {
            sDate = sdf.parse(startDate);
            eDate = sdf.parse(endDate);
            return (int)((eDate.getTime() - sDate.getTime()) / 1000 / 60 / 60 / 24);
        }catch(Exception e) {
            return 0;
        }
    }
    /**
     * @param str yyyy-MM-dd 형식
     * @param i 증가시킬 날
     * @return yyyy-MM-dd 증가된 날짜
     * @throws Exception
     */
    public static String getAfterDate(String str , int i) throws Exception {
        DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        java.util.Date sDate =(java.util.Date) formatter.parse(str);
        java.util.Calendar cal = java.util.Calendar.getInstance();
        cal.setTime(sDate);
        cal.add(Calendar.DATE, i );
        String year = Integer.toString(cal.get(Calendar.YEAR)) ;
        String month = cal.get(Calendar.MONTH) < 9 ? "0" + Integer.toString(cal.get(Calendar.MONTH) +1) : Integer.toString(cal.get(Calendar.MONTH) +1) ;;
        String date = cal.get(Calendar.DAY_OF_MONTH) < 10 ? "0" + Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) : Integer.toString(cal.get(Calendar.DAY_OF_MONTH)) ;
        return year + "-" + month + "-" + date;
    }

    /**
     * @param org_date
     * @return
     */
    public static String convertMinusDate(String org_date){
        String resultDate = org_date;
        try{
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd") ;
            Date date = dateFormat.parse(org_date) ;

            // 포맷변경 ( 년월일 시분초)
            SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");

            // Java 시간 더하기
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);

            // 하루 전
            cal.add(Calendar.DATE, -1);
            resultDate = sdformat.format(cal.getTime());
            System.out.println("1일 전 : " + resultDate);
        }catch (Exception e){
            resultDate = org_date;
        }
        return resultDate;
    }

    /**
     * @param date
     * @param dateType
     * @return
     * @throws Exception
     */
    public static String getDateDay(String date, String dateType) throws Exception {
        String day = "" ;
        SimpleDateFormat dateFormat = new SimpleDateFormat(dateType) ;
        Date nDate = dateFormat.parse(date) ;
        Calendar cal = Calendar.getInstance() ;
        cal.setTime(nDate);
        int dayNum = cal.get(Calendar.DAY_OF_WEEK) ;
        switch(dayNum){
            case 1:
                day = "일";
                break ;
            case 2:
                day = "월";
                break ;
            case 3:
                day = "화";
                break ;
            case 4:
                day = "수";
                break ;
            case 5:
                day = "목";
                break ;
            case 6:
                day = "금";
                break ;
            case 7:
                day = "토";
                break ;
        }
        return day ;
    }

}
