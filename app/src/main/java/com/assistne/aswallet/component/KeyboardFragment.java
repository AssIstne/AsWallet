package com.assistne.aswallet.component;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.assistne.aswallet.R;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * A keyboard fragments.
 * Use {@link ItemClickListener} and {@link #setCallback(ItemClickListener)} to handle click-events.
 * Created by assistne on 15/12/27.
 */
public class KeyboardFragment extends Fragment implements
        View.OnClickListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{

    /**
     * Flags identify keys on the keyboard
     * */
    public interface Flag {
//        flags for numbers 0-9
        int NUM_ZERO = 0;
        int NUM_ONE = 1;
        int NUM_TWO = 2;
        int NUM_THREE = 3;
        int NUM_FOUR = 4;
        int NUM_FIVE = 5;
        int NUM_SIX = 6;
        int NUM_SEVEN = 7;
        int NUM_EIGHT = 8;
        int NUM_NINE = 9;

        int OPR_DOT = 10;
        int OPR_DEL = 11;
    }

    @Bind(R.id.keyboard_grid_numbers) GridView mGVNumbers;

    private ItemClickListener mCallback;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_keyboard, container, false);
        ButterKnife.bind(this, root);

        mGVNumbers.setAdapter(new NumberAdapter());
        mGVNumbers.setOnItemClickListener(this);
        mGVNumbers.setOnItemLongClickListener(this);
        return root;
    }

    @Override
    public void onClick(View v) {
        if (mCallback != null) {
            mCallback.onKeyboardClick((int) v.getTag());
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mCallback != null) {
            mCallback.onKeyboardClick((int) view.getTag());
        }
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        return mCallback != null && mCallback.onKeyboardLongClick((int) view.getTag());
    }

    public interface ItemClickListener {
        void onKeyboardClick(int flag);
        boolean onKeyboardLongClick(int flag);
    }

    public void setCallback(ItemClickListener callback) {
        mCallback = callback;
    }

    class NumberAdapter extends BaseAdapter {

        private int rowHeight;
        @Override
        public int getCount() {
            return 12;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            if (convertView != null) {
                view = convertView;
            } else {
                view = LayoutInflater.from(getActivity())
                        .inflate(R.layout.grid_item_keyboard, parent, false);
            }
            // 根据父控件的高度动态计算每行的高度
            TextView button = (TextView) view.findViewById(R.id.button);
            if (rowHeight == 0) {
                rowHeight = parent.getHeight() / 4;
            }
            button.setMinHeight(rowHeight);
//            set tag to identify the key
            switch (position) {
                case 9:
                    button.setText(".");
                    view.setTag(Flag.OPR_DOT);
                    break;
                case 11:
                    button.setText("Del");
                    view.setTag(Flag.OPR_DEL);
                    break;
                case 0:
                case 1:
                case 2:
                    button.setText(String.valueOf(position + 7));
                    view.setTag(position + 7);
                    break;
                case 3:
                case 4:
                case 5:
                    button.setText(String.valueOf(position + 1));
                    view.setTag(position + 1);
                    break;
                case 6:
                case 7:
                case 8:
                    button.setText(String.valueOf(position - 5));
                    view.setTag(position - 5);
                    break;
                default:
                    button.setText(String.valueOf(0));
                    view.setTag(Flag.NUM_ZERO);
                    break;
            }
            return view;
        }
    }
}
