package com.cleverua.bb.example;

import java.io.IOException;
import java.io.InputStream;
import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.EditField;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.container.MainScreen;

import com.cleverua.bb.post.BinaryRequestParam;
import com.cleverua.bb.post.Coder;
import com.cleverua.bb.post.RequestParam;
import com.cleverua.bb.post.Sender;

public class MultipartPostScreen extends MainScreen {
    private static final String DESCRIPTION_LABEL = "Description:";
    private static final String SCREEN_TITLE = "Multipart post demo";
    private static final String POST_LABEL = "POST";
    
    private EditField description;
    private BitmapField image;
    private ButtonField postButton;
    
    
    public MultipartPostScreen() {
        super();
        initUI();
    }
    
    protected boolean onSavePrompt() {
        return true;
    }
    
    private void initUI() {
        setPadding(5, 5, 5, 5);
        setTitle(SCREEN_TITLE);
        LabelField descriptionLabel = new LabelField(DESCRIPTION_LABEL, FIELD_LEFT);
        add(descriptionLabel);
        description = new EditField(USE_ALL_WIDTH);
        add(description);
        
        image = new BitmapField(Bitmap.getBitmapResource("example.png"));
        add(image);
        postButton = new ButtonField(POST_LABEL, FIELD_HCENTER);
        postButton.setChangeListener(new FieldChangeListener() {
            public void fieldChanged(Field arg0, int arg1) {
                InputStream is = null;
                try {
                    RequestParam descriptionParam = new RequestParam("description", description.getText());
                    is = getClass().getResourceAsStream("/example.png");
                    byte[] buffer = new byte[is.available()];
                    is.read(buffer);
                    BinaryRequestParam imageParam = new BinaryRequestParam("image", "example.png", "image/png", buffer);
                    
                    Vector params = new Vector();
                    params.addElement(descriptionParam);
                    params.addElement(imageParam);
                    
                    Sender.send(new Coder().getMultipartBody(params), "http://192.168.1.5:3000/post;deviceside=true");
                    
                    
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try { if(is != null) { is.close();} } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                
                //MultipartPostApplication.instance().showNotImplementedAlert();
                
            }
        });
        setStatus(postButton);
    }
}
