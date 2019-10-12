package com.yequan.common.filter;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @Auther: yq
 * @Date: 2019/7/8 14:50
 * @Description:
 */
public class ResponseWrapper extends HttpServletResponseWrapper {

    private ByteArrayOutputStream bos;
    private ServletOutputStream sos;

    public ResponseWrapper(HttpServletResponse response) {
        super(response);
        bos = new ByteArrayOutputStream();
        sos = new WrapperOutputStream(bos);
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return sos;
    }

    @Override
    public void flushBuffer() throws IOException {
        if (sos != null) {
            sos.flush();
        }
    }

    public byte[] getContent() throws IOException {
        flushBuffer();
        return bos.toByteArray();
    }


    class WrapperOutputStream extends ServletOutputStream {

        private ByteArrayOutputStream bos;

        public WrapperOutputStream(ByteArrayOutputStream bos) {
            this.bos = bos;
        }

        public boolean isReady() {
            return false;
        }

        public void setWriteListener(WriteListener writeListener) {

        }

        public void write(int b) throws IOException {
            this.bos.write(b);
        }
    }

}
