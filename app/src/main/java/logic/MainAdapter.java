package logic;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hedgehogSoft.carcleantime.R;

import java.util.ArrayList;

public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
    ArrayList<MainModel> mainModels;
    Context context;
    int[] colors;
    int colorNum = -1;

    public MainAdapter(ArrayList<MainModel> mainModels, Context context) {
        this.mainModels = mainModels;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_item, parent, false);
        colors = new int[]{Color.parseColor("#B7D6A4"),
                Color.parseColor("#DCAE85")};
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.date.setText(mainModels.get(position).getDate());
        holder.dayTv.setText(mainModels.get(position).getDayTv());
        holder.nightTv.setText(mainModels.get(position).getNightTv());
        holder.eveningTv.setText(mainModels.get(position).getEveningTv());
        holder.morningTv.setText(mainModels.get(position).getMorningTv());
        holder.dayTempTv.setText(mainModels.get(position).getDayTempTv());
        holder.nightTempTv.setText(mainModels.get(position).getNightTempTv());
        holder.eveningTempTv.setText(mainModels.get(position).getEveningTempTv());
        holder.morningTempTv.setText(mainModels.get(position).getMorningTempTv());
        holder.dayPOP.setText(mainModels.get(position).getDayPOP());
        holder.nightPOP.setText(mainModels.get(position).getNightPOP());
        holder.eveningPOP.setText(mainModels.get(position).getEveningPOP());
        holder.morningPOP.setText(mainModels.get(position).getMorningPOP());
        holder.dayDescTv.setText(mainModels.get(position).getDayDescTv());
        holder.nightDescTv.setText(mainModels.get(position).getNightDescTv());
        holder.eveningDescTv.setText(mainModels.get(position).getEveningDescTv());
        holder.morningDescTv.setText(mainModels.get(position).getMorningDescTv());
        holder.dayConDescTv.setText(mainModels.get(position).getDayConDescTv());
        holder.nightConDescTv.setText(mainModels.get(position).getNightConDescTv());
        holder.eveningConDescTv.setText(mainModels.get(position).getEveningConDescTv());
        holder.morningConDescTv.setText(mainModels.get(position).getMorningConDescTv());
        holder.dayIV.setImageResource(mainModels.get(position).getDayIV());
        holder.nightIV.setImageResource(mainModels.get(position).getNightIV());
        holder.eveningIV.setImageResource(mainModels.get(position).getEveningIV());
        holder.morningIV.setImageResource(mainModels.get(position).getMorningIV());
    }

    @Override
    public int getItemCount() {
        return mainModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayTv, nightTv, eveningTv, morningTv,
                dayPOP, nightPOP, eveningPOP, morningPOP,
                dayTempTv, nightTempTv, eveningTempTv, morningTempTv,
                dayDescTv, nightDescTv, eveningDescTv, morningDescTv,
                dayConDescTv, nightConDescTv, eveningConDescTv, morningConDescTv, date;
        ImageView dayIV, nightIV, eveningIV, morningIV;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setBackgroundColor(colors[colorNum < 1 ? ++colorNum : zeroing()]);
            date = itemView.findViewById(R.id.dateTV);
            dayTv = itemView.findViewById(R.id.dayTV);
            nightTv = itemView.findViewById(R.id.nightTV);
            eveningTv = itemView.findViewById(R.id.eveningTV);
            morningTv = itemView.findViewById(R.id.morningTV);
            dayTempTv = itemView.findViewById(R.id.dayTempTV);
            nightTempTv = itemView.findViewById(R.id.nightTempTV);
            eveningTempTv = itemView.findViewById(R.id.eveningTempTV);
            morningTempTv = itemView.findViewById(R.id.morningTempTV);
            dayPOP = itemView.findViewById(R.id.dayPOPTV);
            nightPOP = itemView.findViewById(R.id.nightPOPTV);
            eveningPOP = itemView.findViewById(R.id.eveningPOPTV);
            morningPOP = itemView.findViewById(R.id.morningPOPTV);
            dayDescTv = itemView.findViewById(R.id.dayDescriptionTV);
            nightDescTv = itemView.findViewById(R.id.nightDescriptionTV);
            eveningDescTv = itemView.findViewById(R.id.eveningDescriptionTV);
            morningDescTv = itemView.findViewById(R.id.morningDescriptionTV);
            dayIV = itemView.findViewById(R.id.dayIV);
            nightIV = itemView.findViewById(R.id.nightIV);
            eveningIV = itemView.findViewById(R.id.eveningIV);
            morningIV = itemView.findViewById(R.id.morningIV);
            dayConDescTv = itemView.findViewById(R.id.dayConditionDescriptionTV);
            nightConDescTv = itemView.findViewById(R.id.nightConditionDescriptionTV);
            eveningConDescTv = itemView.findViewById(R.id.eveningConditionDescriptionTV);
            morningConDescTv = itemView.findViewById(R.id.morningConditionDescriptionTV);
        }
    }
    public int zeroing(){
        colorNum = 0;
        return 0;
    }
}
