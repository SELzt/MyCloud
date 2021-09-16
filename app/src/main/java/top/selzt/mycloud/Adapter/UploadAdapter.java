package top.selzt.mycloud.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import top.selzt.mycloud.R;
import top.selzt.mycloud.TransmissionThread.UploadThread;
import top.selzt.mycloud.Util.ThreadMap;

public class UploadAdapter extends RecyclerView.Adapter<UploadAdapter.ViewHolder> {
    private Context mContext;
    private int resourceId;
    private List<UploadThread> mList;
    private RecyclerView recyclerView;
    public UploadAdapter(Context context,int resourceId,List<UploadThread> list) {
        mContext = context;
        this.resourceId = resourceId;
        mList = list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resourceId,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UploadThread thread = mList.get(position);
        long uploadSize = thread.getUploadSize();
        long fileSize = thread.getFileSize();
        double percent =((double)uploadSize/(double)fileSize)*100;
        DecimalFormat df = new DecimalFormat("0.00");
        holder.tvPercent.setText( df.format(percent)+ "%");
        holder.tvUploadTotal.setText(uploadSize+"/"+fileSize);
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress((int)percent);
        holder.tvFilename.setText(thread.getFilename());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView( RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ProgressBar progressBar;//进度条
        TextView tvFilename;//文件名
        TextView tvUploadTotal;//上传量
        TextView tvPercent;
        public ViewHolder(View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.transmissionProgress);
            tvFilename = itemView.findViewById(R.id.popup_item_filename);
            tvUploadTotal = itemView.findViewById(R.id.popup_item_uploadTotal);
            tvPercent = itemView.findViewById(R.id.popup_item_percent);
        }
    }
}
