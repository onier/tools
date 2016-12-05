
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import javax.swing.AbstractAction;
import javax.swing.Action;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author zhenhai
 */
public class ApkItem extends AbstractAction {

    public File file;
    public State apkState;

    public ApkItem(File file) {
        this.file = file;
        putValue(Action.NAME, "上传文件");
        this.apkState = new State(this);
        apkState.setState(State.Default);
    }

    public void uploadFile() {
        try {
            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost("http://192.168.1.130:8080/imooc/");

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.addBinaryBody(file.getName(), file, ContentType.APPLICATION_OCTET_STREAM, file.getName());
            HttpEntity multipart = builder.build();
            CountingMultipartRequestEntity.ProgressListener pListener = new CountingMultipartRequestEntity.ProgressListener() {
                @Override
                public void progress(float percentage) {
                    apkState.setProcess((int) percentage);
                    firePropertyChange("uploadProcess", -1, percentage);
                }
            };
            httpPost.setEntity(new CountingMultipartRequestEntity.ProgressEntityWrapper(multipart, pListener));
            CloseableHttpResponse response = client.execute(httpPost);
            client.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the file
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file the file to set
     */
    public void setFile(File file) {
        this.file = file;
    }

    /**
     * @return the apkState
     */
    public State getApkState() {
        return apkState;
    }

    /**
     * @param apkState the apkState to set
     */
    public void setApkState(State apkState) {
        this.apkState = apkState;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                uploadFile();
            }
        }).start();
    }

}
