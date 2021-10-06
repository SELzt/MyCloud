package top.selzt.mycloud.Util;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.ForwardingSink;
import okio.Okio;
import okio.Sink;

public class UploadRequestBody extends RequestBody {
    private RequestBody mRequestBody;
    private ProgressListener mProgressListener;
    private long mContentLength;
    public UploadRequestBody(RequestBody requestBody,ProgressListener progressListener) {
        mRequestBody = requestBody;
        mProgressListener = progressListener;
    }

    @Override
    //获取文件总长度
    public long contentLength() {
        try {
            if(mContentLength == 0)
                mContentLength = mRequestBody.contentLength();
            return mContentLength;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Nullable
    @Override
    public MediaType contentType() {

        return mRequestBody.contentType();
    }

    @Override
    public void writeTo(@NotNull BufferedSink bufferedSink) throws IOException {
        ByteSink byteSink = new ByteSink(bufferedSink);
        BufferedSink mBufferedSink = Okio.buffer(byteSink);
        mRequestBody.writeTo(mBufferedSink);
        mBufferedSink.flush();
    }
    private final class ByteSink extends ForwardingSink{
        private long uploadSize = 0;
        public ByteSink(@NotNull Sink delegate) {
            super(delegate);
        }

        @Override
        public void write(@NotNull Buffer source, long byteCount) throws IOException {
            super.write(source, byteCount);
            uploadSize +=byteCount;
            mProgressListener.getProgress(uploadSize,contentLength());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    public interface ProgressListener{
        void getProgress(long uploadSize,long fileSize);
    }

}
