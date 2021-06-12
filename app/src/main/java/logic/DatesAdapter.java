    package logic;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.hedgehogSoft.carcleantime.R;

import java.util.ArrayList;

public class DatesAdapter  extends RecyclerView.Adapter<DatesAdapter.DateViewHolder> {
    ArrayList<String> dates;
    Context context;

    public DatesAdapter(ArrayList<String> dates, Context context) {
        this.dates = dates;
        this.context = context;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @NonNull
    @Override
    public DateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.date_item, parent, false);
        return new DateViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DateViewHolder holder, int position) {
        holder.date.setText("\t\t\t\t" + dates.get(position));
    }

    @Override
    public int getItemCount() {
        return dates.size();
    }

    public class DateViewHolder extends RecyclerView.ViewHolder {
        TextView date;

        @RequiresApi(api = Build.VERSION_CODES.N)
        public DateViewHolder(@NonNull View itemView) {
            super(itemView);
            date = itemView.findViewById(R.id.dateItemED);
        }
    }
}
