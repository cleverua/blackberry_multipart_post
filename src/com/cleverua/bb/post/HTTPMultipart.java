package com.cleverua.bb.post;

import java.io.IOException;
import java.io.InputStream;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import net.rim.device.api.util.ByteVector;

public class HTTPMultipart {
    private static final String KEY_IMG_BYTES = "imgBytes";
    public static final String CRLF = "\r\n";
    public static final String BOUNDARY_MARKER = "AaB03x";
    
    private static final String KEY_DESCRIPTION = "description";
    
    public byte[] toMultipartBody(String description, String filePath) {
        ByteVector buff = new ByteVector();
        
        appendArray(buff, getImageHeader());
        appendArray(buff, getImageBytes(filePath));
        appendArray(buff, CRLF.getBytes());
        
        appendArray(buff, postParameter(KEY_DESCRIPTION, description));
        appendArray(buff, closeBoundary().getBytes());

        return buff.getArray();
    }

    private byte[] getImageHeader() {
        String header = boundary() + CRLF;
        header += "Content-Disposition: form-data; name=\"" + KEY_IMG_BYTES +"\"; filename=\"image.jpg\"" + CRLF;
        header += "Content-Type: image/jpeg" + CRLF + CRLF;
        return header.getBytes();
    }

    private byte[] getImageBytes(String imgPath) {
        try {
            return getFileData(imgPath);
        } catch (IOException e) {
            return null;
        }
    }

    protected static byte[] postParameter(String name, String value) {
        return postParameterAsString(name, value).getBytes();
    }

    protected static String postParameterAsString(String name, String value) {
        String result = boundary() + CRLF;
        result = result + "Content-Disposition: form-data; name=\"" + name + "\"" + CRLF + CRLF;
        result = result + value + CRLF;
        return result;
    }

    protected static String closeBoundary() {
        return boundary() + "--" + CRLF;
    }

    protected static String boundary() {
        return "--" + BOUNDARY_MARKER;
    }

    private void appendArray(ByteVector into, byte[] array) {
        for (int i = 0; i < array.length; i++) {
            into.addElement(array[i]);
        }
    }


    private byte[] getFileData(String filePath) throws IOException {
        InputStream in = null;
        FileConnection fc = null;

        try {
            fc = (FileConnection) Connector.open(filePath);
            in = fc.openInputStream();
            byte[] data = new byte[(int) fc.fileSize()];
            in.read(data);
            return data;
        } finally {
            safelyCloseStream(in);
            safelyCloseStream(fc);
        }
    }

    private void safelyCloseStream(InputStream stream) {
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (Exception e) { /* that's ok */ }
        }
    }

    private void safelyCloseStream(FileConnection stream) {
        if (stream != null) {
            try {
                stream.close();
                stream = null;
            } catch (Exception e) { /* that's ok */ }
        }
    }

}
