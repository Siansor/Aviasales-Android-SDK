package ru.aviasales.template.ui.dialog;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;

import ru.aviasales.core.AviasalesSDK;
import ru.aviasales.template.R;


public class ResultsSortingDialog extends BaseDialogFragment {

	public final static String TAG = "fragment.ResultsSortingDialog";

	public static final int SORTING_BY_PRICE = 0;
	public static final int SORTING_BY_DEPARTURE = 1;
	public static final int SORTING_BY_ARRIVAL = 2;
	public static final int SORTING_BY_DEPARTURE_ON_RETURN = 3;
	public static final int SORTING_BY_ARRIVAL_ON_RETURN = 4;
	public static final int SORTING_BY_DURATION = 5;
	public static final int SORTING_BY_RATING = 6;

	private static int currentSorting;

	private OnSortingChangedListener onSortingChangedListener;

	public static ResultsSortingDialog newInstance(int sortingType, OnSortingChangedListener onSortingChangedListener) {
		ResultsSortingDialog dialog = new ResultsSortingDialog();
		dialog.setOnSortingChangedListener(onSortingChangedListener);
		currentSorting = sortingType;
		return dialog;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NO_TITLE, R.style.CustomDialog);
	}

	@Override
	public String getFragmentTag() {
		return TAG;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		ViewGroup layout = (ViewGroup) inflater.inflate(R.layout.results_sorting_dialog, container);

		setUpSortingItem(layout, R.id.tv_sort_by_price, SORTING_BY_PRICE);
		setUpSortingItem(layout, R.id.tv_sort_by_departure, SORTING_BY_DEPARTURE);
		setUpSortingItem(layout, R.id.tv_sort_by_arrival, SORTING_BY_ARRIVAL);
		setUpSortingItem(layout, R.id.tv_sort_by_departure_on_way_back, SORTING_BY_DEPARTURE_ON_RETURN);
		setUpSortingItem(layout, R.id.tv_sort_by_arrival_on_way_back, SORTING_BY_ARRIVAL_ON_RETURN);
		setUpSortingItem(layout, R.id.tv_sort_by_duration, SORTING_BY_DURATION);
		setUpSortingItem(layout, R.id.tv_sort_by_rating, SORTING_BY_RATING);

		if (ticketsWithoutReturn()) {
			layout.findViewById(R.id.tv_sort_by_departure_on_way_back).setVisibility(View.GONE);
			layout.findViewById(R.id.tv_sort_by_arrival_on_way_back).setVisibility(View.GONE);
		}

		return layout;
	}

	private boolean ticketsWithoutReturn() {
		return AviasalesSDK.getInstance().getSearchParamsOfLastSearch().getReturnDate() == null;
	}

	private void setUpSortingItem(ViewGroup layout, int itemId, final int sortingType) {
		CheckedTextView view = (CheckedTextView) layout.findViewById(itemId);

		view.getBackground().setColorFilter(getResources().getColor(R.color.colorAviasalesMain), PorterDuff.Mode.SRC_ATOP);

		if (sortingType == currentSorting) {
			view.setChecked(true);
		}
		view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				onSortingChangedListener.onSortingChanged(sortingType);
			}
		});
	}

	public void setOnSortingChangedListener(OnSortingChangedListener onSortingChangedListener) {
		this.onSortingChangedListener = onSortingChangedListener;
	}

	public interface OnSortingChangedListener {
		void onSortingChanged(int sortingType);
		void onCancel();
	}

	@Override
	public void onStart() {
		super.onStart();

		if (getDialog() == null) {
			return;
		}

		int dialogWidth = getResources().getDimensionPixelSize(R.dimen.sorting_dialog_width);
		int dialogHeight;

		if (ticketsWithoutReturn()) {
			dialogHeight = getResources().getDimensionPixelSize(R.dimen.sorting_dialog_height_without_return);
		} else {
			dialogHeight = getResources().getDimensionPixelSize(R.dimen.sorting_dialog_height);
		}

		getDialog().getWindow().setLayout(dialogWidth, dialogHeight);
	}

	@Override
	public void onCancel(DialogInterface dialog) {
		onSortingChangedListener.onCancel();
		super.onCancel(dialog);
	}

}


