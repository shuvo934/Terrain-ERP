package ttit.com.shuvo.icomerp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import ttit.com.shuvo.icomerp.R;
import ttit.com.shuvo.icomerp.arrayList.LevelThreeLists;

public class LevelThreeAdapter extends RecyclerView.Adapter<LevelThreeAdapter.LevelThreeHolder> {

    private ArrayList<LevelThreeLists> mCategoryItem;
    private final Context myContext;

    int selectedPosition = -1;

    public LevelThreeAdapter(ArrayList<LevelThreeLists> mCategoryItem, Context myContext) {
        this.mCategoryItem = mCategoryItem;
        this.myContext = myContext;
    }
    public class LevelThreeHolder extends RecyclerView.ViewHolder {

        public TextView code;
        public TextView sh_code;
        public TextView name;

        public LevelThreeHolder(@NonNull View itemView) {
            super(itemView);
            code = itemView.findViewById(R.id.code_of_level_three);
            sh_code = itemView.findViewById(R.id.short_code_of_level_three);
            name = itemView.findViewById(R.id.name_of_level_three);

        }

    }

    @NonNull
    @Override
    public LevelThreeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(myContext).inflate(R.layout.level_three_ca_view, parent, false);
        return new LevelThreeHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LevelThreeHolder holder, int position) {

        LevelThreeLists categoryItem = mCategoryItem.get(position);

        holder.code.setText(categoryItem.getCode());
        holder.name.setText(categoryItem.getName());
        holder.sh_code.setText(categoryItem.getSh_code());


        if(selectedPosition == position) {
            holder.itemView.setBackgroundColor(Color.parseColor("#81ecec"));
        }
        else {
            holder.itemView.setBackgroundColor(Color.parseColor("#f5f6fa"));
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedPosition == position) {
                    selectedPosition = -1;
                } else {
                    selectedPosition=position;
                }

                notifyDataSetChanged();

            }
        });

    }

    @Override
    public int getItemCount() {
        return mCategoryItem.size();
    }
}
