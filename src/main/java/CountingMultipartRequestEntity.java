/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import org.apache.http.HttpEntity;
import org.apache.http.entity.HttpEntityWrapper;

public class CountingMultipartRequestEntity {

    public static class CountingOutputStream extends FilterOutputStream {

        private ProgressListener listener;
        private long transferred;
        private long totalBytes;
        private int progress;

        public CountingOutputStream(
                OutputStream out, ProgressListener listener, long totalBytes) {
            super(out);
            this.listener = listener;
            transferred = 0;
            this.totalBytes = totalBytes;
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out.write(b, off, len);
            transferred += len;
            int n = (int) getCurrentProgress();
            if (n > progress) {
                progress = n;
                listener.progress(progress);
            }
        }

        @Override
        public void write(int b) throws IOException {
            out.write(b);
            transferred++;
            int n = (int) getCurrentProgress();
            if (n > progress) {
                progress = n;
                listener.progress(progress);
            }
//            listener.progress(getCurrentProgress());
        }

        private float getCurrentProgress() {
            return ((float) transferred / totalBytes) * 100;
        }
    }

    public static class ProgressEntityWrapper extends HttpEntityWrapper {

        private ProgressListener listener;

        public ProgressEntityWrapper(HttpEntity entity,
                ProgressListener listener) {
            super(entity);
            this.listener = listener;
        }

        @Override
        public void writeTo(OutputStream outstream) throws IOException {
            super.writeTo(new CountingOutputStream(outstream, listener, getContentLength()));
        }
    }

    public static interface ProgressListener {

        void progress(float percentage);
    }

    public static void main(String[] args) throws IOException {
//        HttpClient client = new DefaultHttpClient();
//        HttpPost post = new HttpPost("http://localhost:8081/web/UploadServlet");
//MultipartEntityBuilder.create().
//        MultipartEntity entity = new MultipartEntity();
//        entity.addPart("file", new FileBody(file));
//        post.setEntity(entity);
//
//        HttpResponse response = client.execute(post);

    }
}
