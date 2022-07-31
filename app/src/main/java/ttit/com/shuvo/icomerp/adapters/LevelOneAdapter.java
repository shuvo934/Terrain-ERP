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

public class LevelOneAdapter extends RecyclerView.Adapter<LevelOneAdapter.LevelOneHolder> {

    private ArrayList<LevelWiseLists> mCategoryItem;
    private final ClickedItem myClickedItem;
    private final Context myContext;



    public LevelOneAdapter(ArrayList<LevelWiseLists> categoryItems, Context context, ClickedItem cli) {
        this.mCategoryItem = categoryItems;
        this.myClickedItem = cli;
        this.myContext = context;
    }

    public class LevelOneHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView code;
        public TextView name;

        ClickedItem mClickedItem;

        public LevelOneHolder(@NonNull View itemView, ClickedItem ci) {
            super(itemView);
            code = itemView.findViewById(R.id.code_of_level_one);
            name = itemView.findViewById(R.id.name_of_level_one);

            this.mClickedItem = ci;

            itemView.setOnClickListener(this::onClick);

        }

        @Override
        public void onClick(View v) {
            mClickedItem.onCategoryClicked(getAdapterPosition());
            //Log.i("Name", mCategoryItem.get(getAdapterPosition()).getName());
        }
    }

    public interface ClickedItem {
        void onCategoryClicked(int CategoryPosition);
    }

    @NonNull
    @Override
    public LevelOneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.level_one_ca_view, parent, false);
        return new LevelOneHolder(view, myClickedItem);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelOneHolder holder, int position) {

        LevelWiseLists categoryItem = mCategoryItem.get(position);

        holder.code.setText(categoryItem.getCode());
        holder.name.setText(categoryItem.getName());


        if(ChartAccounts.selectedPosition == position) {
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
