package IO;

import java.io.IOException;
import java.io.OutputStream;

/**
 * MyCompressor Class compresses by converting maze data(0's and 1's) from binary to 8-bit integer
 */

public class MyCompressorOutputStream extends OutputStream {
    private OutputStream out;

    public MyCompressorOutputStream(OutputStream os)
    {
        this.out=os;
    }
    @Override
    public void write(int b) throws IOException {
        out.write(b);
    }
    @Override
    public void write(byte[] b) throws IOException {
        //take care of first 11 cells of the format
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

        int count = 0;
        StringBuilder binStr = new StringBuilder();
        for(int i=12;i<b.length;i++)
        {
            binStr.append(b[i]);
            count++;
            if(count==8)
            {
                int num = Integer.parseInt(String.valueOf(binStr),2);
                this.write(num);
                count=0;
                binStr = new StringBuilder();
            }
        }
        //take care of last one incase it's less than 8
        if(!binStr.isEmpty()){
            int num = Integer.parseInt(String.valueOf(binStr),2);
            this.write(num);
        }

    }






}
