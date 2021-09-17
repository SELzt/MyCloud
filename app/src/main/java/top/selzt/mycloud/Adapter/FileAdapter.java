package top.selzt.mycloud.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;


import top.selzt.mycloud.R;
import top.selzt.mycloud.SendData.DeleteFile;
import top.selzt.mycloud.TransmissionThread.DownloadThread;
import top.selzt.mycloud.Util.Alert;
import top.selzt.mycloud.pojo.FileDetail;
public class FileAdapter extends RecyclerView.Adapter<FileAdapter.ViewHolder> implements View.OnClickListener {
    private List<FileDetail> mFiles;
    private Context mContext;
    private int resourceId;
    private OnItemClickListener onItemClickListener;
    private RecyclerView recyclerView;
    public FileAdapter(Context context, int resourceId, List<FileDetail> files) {
        this.mContext = context;
        this.mFiles = files;
        this.resourceId = resourceId;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(resourceId,parent,false);
        view.setOnClickListener(this);
        ViewHolder holder = new ViewHolder(view);
        moreActionInit(holder);
        return holder;
    }
    public void moreActionInit(ViewHolder holder){
        holder.ivMoreAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = (int)view.getTag();
                FileDetail file = mFiles.get(position);
                //已经准确获取到点击坐标
                PopupMenu popupMenu = new PopupMenu(mContext,holder.ivMoreAction);
                popupMenu.inflate(R.menu.menu_item_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.download:
                                //下载
                                if(file.getFileType().equals("1"))
                                    return true;
                                String filename = file.getFileName();
                                new DownloadThread(filename).start();
                                return true;
                            case R.id.delete:
                                //删除
                                new DeleteFile().go(mContext,FileAdapter.this,"/"+file.getFileName(),mFiles,position);
                                return true;
                            case R.id.rename:
                                //重命名
                                String oldName = file.getFileName();
                                Alert.getInstance().Rename(mContext,oldName);
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                popupMenu.show();
            }
        });
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FileDetail f = mFiles.get(position);
        holder.tvFilename.setText(f.getFileName());
        holder.tvModifyTime.setText(f.getModifyTime());
        holder.tvFileSize.setText(f.getFileSize());
        holder.ivMoreAction.setTag(position);//避免处理点击事件时坐标出现错误
        if(f.getFileType().equals("1")){
            //文件夹
            Glide.with(mContext).load(R.drawable.ic_baseline_folder_24).into(holder.ivFileImg);
        }
        else {
            //文件
            Glide.with(mContext).load(R.drawable.ic_outline_insert_drive_file_24).into(holder.ivFileImg);
        }
    }

    @Override
    public int getItemCount() {
        return mFiles.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.recyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(@NonNull @NotNull RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.recyclerView = null;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if(onItemClickListener != null){
            this.onItemClickListener.OnItemClick(recyclerView,view,position,mFiles.get(position));
        }
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivFileImg;
        ImageView ivMoreAction;
        TextView tvFilename;
        TextView tvModifyTime;
        TextView tvFileSize;
        public ViewHolder(View itemView) {
            super(itemView);
            ivFileImg = itemView.findViewById(R.id.ivFileImg);
            ivMoreAction = itemView.findViewById(R.id.ivMoreAction);
            tvFilename = itemView.findViewById(R.id.tvFilename);
            tvModifyTime = itemView.findViewById(R.id.tvModifyTime);
            tvFileSize = itemView.findViewById(R.id.tvFileSize);
        }

    }
    public interface OnItemClickListener{
        void OnItemClick(RecyclerView parent, View view, int position, FileDetail file);
    }
}
