package top.selzt.mycloud.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DecimalFormat;
import java.util.List;

import top.selzt.mycloud.R;
import top.selzt.mycloud.TransmissionActivity;
import top.selzt.mycloud.TransmissionThread.DownloadThread;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private Context mContext;
    private int resourceId;
    private List<DownloadThread> mList;
    DecimalFormat df;
    public DownloadAdapter(Context context,int resourceId,List<DownloadThread> list){
        mContext = context;
        this.resourceId = resourceId;
        mList = list;
        df = new DecimalFormat("0.00");
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resourceId,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DownloadThread thread = mList.get(position);
        holder.tvFilename.setText(thread.getFilename());
        holder.tvUploadTotal.setText((long)thread.getDownloadSize()+"/"+(long)thread.getFileSize());
        holder.tvPercent.setText(df.format(thread.getDownloadSize()/thread.getFileSize()*100)+"%");
        holder.progressBar.setMax(100);
        holder.progressBar.setProgress((int)(thread.getDownloadSize()/thread.getFileSize()*100));
    }

    @Override
    public int getItemCount() {
        return mList.size();
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
