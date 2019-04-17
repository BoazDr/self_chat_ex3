package com.example.self_chat_ex3;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context mContext;
    private ItemClickListener mClickListener;
    private PopupWindow mPopupWindow;

    // data is passed into the constructor
    MyAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        this.mContext = context;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycle_view_one_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        String str = mData.get(position);
        holder.myTextView.setText(str);

        holder.myTextView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int index_to_remove = holder.getAdapterPosition();
                showPopup(view, index_to_remove);
                return true;
            }
        });
    }


    // total number of rows in the list
    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void showPopup(View view, final int index_to_remove) {
        final View popupView = LayoutInflater.from(mContext).inflate(R.layout.popup_window, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button yes_btn = (Button) popupView.findViewById(R.id.yes_popup_button);
        final String item = mData.get(index_to_remove);
        final SharedPreferences my_shared_prefs  = PreferenceManager.getDefaultSharedPreferences(mContext);
        yes_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mData.remove(index_to_remove);
                notifyItemRemoved(index_to_remove);
                notifyItemRangeChanged(index_to_remove, mData.size());
                SharedPreferences.Editor editor = my_shared_prefs.edit();
                editor.remove(item);
                editor.apply();
                popupWindow.dismiss();
            }
        });
        Button no_btn = (Button) popupView.findViewById(R.id.no_popup_button);
        no_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        popupWindow.showAsDropDown(popupView, 0, 0);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;

        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.text_view_row);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}






