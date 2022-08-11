package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Simple Compressor compresses by counting consecutive 0's and 1's and writing their count
 * Starting from the count of 0.
 *
 */

public class SimpleCompressorOutputStream extends OutputStream {
    private OutputStream out;

    public SimpleCompressorOutputStream(OutputStream os)
    {
        this.out=os;
    }
    @Override
    public void write(int b) throws IOException {
        if(b>255)
        {
            handleByteOverflow(b);
        }else{
            out.write(b);
        }
    }
    @Override
    public void write(byte[] b) throws IOException {

        int a;
        for(int i=0;i<12;i++)
        {
            if(b[i] < 0)
            {
                a = b[i] & 0xFF;
                this.write(a);
            }else {
                this.write(b[i]);
            }
        }
        int count=0;
        int prev=0;
        for(int i=12;i<b.length;i++)
        {
            if(b[i] != prev)
            {
                this.write(count);
                count=0;
            }
            count++;
            prev=b[i];

        }
        //write remainder if exists
        if(count>0)
            this.write(count);
    }

    private void handleByteOverflow(int num) throws IOException {
        while(num>255)
        {
            out.write(255);
            out.write(0);
            num-=255;
        }
        if(num>0)
            out.write(num);

    }
}
