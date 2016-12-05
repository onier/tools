
import java.beans.PropertyChangeSupport;
import java.io.File;
import java.io.IOException;
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
public class State extends PropertyChangeSupport {

    public final static String Uploaded = "uploaded", Uploading = "uploading", Default = "defalt";
    protected String state;
    protected int process;
    protected ApkItem apkItem;

    public State(ApkItem apkItem) {
        super(State.class.getName());
        this.apkItem = apkItem;
    }

    /**
     * @return the state
     */
    public String getState() {
        return state;
    }

    /**
     * @param state the state to set
     */
    public void setState(String state) {
        this.state = state;
    }

    /**
     * @return the process
     */
    public int getProcess() {
        return process;
    }

    /**
     * @param process the process to set
     */
    public void setProcess(int process) {
        this.process = process;
        if (process == 100) {
            setState(Uploaded);
            apkItem.putValue(Action.NAME, "重新上传");
            apkItem.setEnabled(true);
        } else {
            setState(Uploading);
            apkItem.putValue(Action.NAME, "文件上传");
            apkItem.setEnabled(false);
        }
    }

    /**
     * @return the apkItem
     */
    public ApkItem getApkItem() {
        return apkItem;
    }

    /**
     * @param apkItem the apkItem to set
     */
    public void setApkItem(ApkItem apkItem) {
        this.apkItem = apkItem;
    }
}
