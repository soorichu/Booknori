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
import android.content.res.TypedArray;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import co.soori.aircalendar.R;

import java.util.ArrayList;

public class DayPickerView extends RecyclerView {
    protected Context mContext;
	protected SimpleMonthAdapter mAdapter;
	private DatePickerController mController;
    protected int mCurrentScrollState = 0;
	protected long mPreviousScrollPosition;
	protected int mPreviousScrollState = 0;
    private TypedArray typedArray;
    private OnScrollListener onScrollListener;
    protected boolean isBooking = false;
    protected boolean isMonthDayLabels = false;
    protected boolean isSingleSelect = false;
    protected ArrayList<String> mBookingDates;
    private selectDateModel mSelectDateModel = null;

    public DayPickerView(Context context)
    {
        this(context, null);
    }

    public DayPickerView(Context context, AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public DayPickerView(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        if (!isInEditMode())
        {
            typedArray = context.obtainStyledAttributes(attrs, R.styleable.DayPickerView);
            setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            init(context);
        }
    }

    public void setController(DatePickerController mController)
    {
        this.mController = mController;
        setUpAdapter();
        setAdapter(mAdapter);
    }

    public void setShowBooking(boolean isbooking){
        this.isBooking = isbooking;
    }

    public void setBookingDateArray(ArrayList<String> dates){this.mBookingDates = dates;}

    public void setSelected(selectDateModel date){
        this.mSelectDateModel = date;
    }

    public void setIsMonthDayLabel(boolean isLabel) { this.isMonthDayLabels = isLabel; }

    public void setIsSingleSelect(boolean isSingle) { this.isSingleSelect = isSingle; }

    public void setMonthDayLabels(boolean monthDayLabels){ this.isMonthDayLabels = monthDayLabels; }

	public void init(Context paramContext) {
        setLayoutManager(new LinearLayoutManager(paramContext));
		mContext = paramContext;
		setUpListView();

        onScrollListener = new OnScrollListener()
        {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy)
            {
                super.onScrolled(recyclerView, dx, dy);
                final SimpleMonthView child = (SimpleMonthView) recyclerView.getChildAt(0);
                if (child == null) {
                    return;
                }

                mPreviousScrollPosition = dy;
                mPreviousScrollState = mCurrentScrollState;
            }
        };
	}


	protected void setUpAdapter() {
		if (mAdapter == null) {
			mAdapter = new SimpleMonthAdapter(getContext(), mController, typedArray , isBooking , isMonthDayLabels , isSingleSelect , mBookingDates , mSelectDateModel);
        }
		mAdapter.notifyDataSetChanged();
	}


	protected void setUpListView() {
		setVerticalScrollBarEnabled(false);
		setOnScrollListener(onScrollListener);
		setFadingEdgeLength(0);
	}

    public SimpleMonthAdapter.SelectedDays<SimpleMonthAdapter.CalendarDay> getSelectedDays() { return mAdapter.getSelectedDays();}

    public ArrayList<String> getBookingDates(){ return this.mBookingDates;  };

    protected DatePickerController getController()
    {
        return mController;
    }

    protected TypedArray getTypedArray()
    {
        return typedArray;
    }
}