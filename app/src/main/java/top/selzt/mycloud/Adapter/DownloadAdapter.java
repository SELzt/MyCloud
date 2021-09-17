package top.selzt.mycloud.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import top.selzt.mycloud.R;
import top.selzt.mycloud.TransmissionThread.DownloadThread;

public class DownloadAdapter extends RecyclerView.Adapter<DownloadAdapter.ViewHolder> {
    private Context mContext;
    private int resourceId;
    private List<DownloadThread> mList;
    private RecyclerView recyclerView;

    public DownloadAdapter(Context context,int resourceId,List<DownloadThread> list){

    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(parent);

        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

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
