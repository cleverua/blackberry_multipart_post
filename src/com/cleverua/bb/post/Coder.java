package com.cleverua.bb.post;

import java.util.Vector;

import net.rim.device.api.util.ByteVector;

public class Coder {
    public static final String CRLF = "\r\n";
    public static final String BOUNDARY_MARKER = "AaB03x";

    public byte[] getMultipartBody(Vector requestParams) {
        ByteVector buff = new ByteVector();
        
        for (int i = 0; i < requestParams.size(); i++) {
            RequestParam param = (RequestParam) requestParams.elementAt(i);
            if (param instanceof BinaryRequestParam)
            {
                appendArray(buff, getBinaryHeader((BinaryRequestParam)param));
                appendArray(buff, ((BinaryRequestParam)param).getData());
                appendArray(buff, CRLF.getBytes());
            } else {
                appendArray(buff, postParameter(param.getName(), param.getValue()));
            }
        }
        appendArray(buff, closeBoundary().getBytes());
        return buff.getArray();
    }

    protected static String closeBoundary() {
        return boundary() + "--" + CRLF;
    }

    protected static String boundary() {
        return "--" + BOUNDARY_MARKER;
    }
    
    private static byte[] postParameter(String name, String value) {
        return postParameterAsString(name, value).getBytes();
    }

    private static String postParameterAsString(String name, String value) {
        String result = boundary() + CRLF;
        result = result + "Content-Disposition: form-data; name=\"" + name + "\"" + CRLF + CRLF;
        result = result + value + CRLF;
        return result;
    }
    
    private void appendArray(ByteVector into, byte[] array) {
        for (int i = 0; i < array.length; i++) {
            into.addElement(array[i]);
        }
    }
    
    private byte[] getBinaryHeader(BinaryRequestParam param) {
        String header = boundary() + CRLF;
        header += "Content-Disposition: form-data; name=\"" + param.getName()
                + "\"; filename=\"" + param.getFileName() + "\"" + CRLF;
        header += "Content-Type: "+ param.getContentType() + CRLF + CRLF;
        return header.getBytes();
    }
}
