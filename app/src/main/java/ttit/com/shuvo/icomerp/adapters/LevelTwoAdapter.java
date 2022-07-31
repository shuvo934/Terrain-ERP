package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.LevelWiseLists;
import ttit.com.shuvo.icomerp.fragments.ChartAccounts;

public class LevelTwoAdapter extends RecyclerView.Adapter<LevelTwoAdapter.LevelTwoHolder> {

    private ArrayList<LevelWiseLists> mCategoryItem;
    private final ClickedItem2 myClickedItem2;
    private final Context myContext;


    public LevelTwoAdapter(ArrayList<LevelWiseLists> categoryItems, Context context, ClickedItem2 cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem2 = cli;
        this.myContext = context;
    }

    public class LevelTwoHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView code;
        public TextView name;

        ClickedItem2 mClickedItem2;

        public LevelTwoHolder(@NonNull View itemView, ClickedItem2 ci) {
            super(itemView);
            code = itemView.findViewById(R.id.code_of_level_two);
            name = itemView.findViewById(R.id.name_of_level_two);

            this.mClickedItem2 = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem2.onCategoryClicked2(getAdapterPosition());
            //Log.i("Name", mCategoryItem.get(getAdapterPosition()).getName());
        }
    }

    public interface ClickedItem2 {
        void onCategoryClicked2(int CategoryPosition2);
    }

    @NonNull
    @Override
    public LevelTwoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.level_two_ca_view, parent, false);
        return new LevelTwoHolder(view, myClickedItem2);
    }
    @Override
    public void onBindViewHolder(@NonNull LevelTwoHolder holder, int position) {

        LevelWiseLists categoryItem = mCategoryItem.get(position);

        holder.code.setText(categoryItem.getCode());
        holder.name.setText(categoryItem.getName());


        if(ChartAccounts.selectedPosition2 == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }


//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (selectedPosition == position) {
//                    selectedPosition = -1;
//                } else {
//                    selectedPosition=position;
//                }
//
//                notifyDataSetChanged();
//
//            }
//        });


    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }

}
