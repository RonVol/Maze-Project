package IO;

import java.io.IOException;
import java.io.InputStream;

public class SimpleDecompressorInputStream extends InputStream {
    private InputStream in;

    public SimpleDecompressorInputStream(InputStream is)
    {
        this.in = is;
    }

    @Override
    public int read() throws IOException {
        return in.read();
    }
    @Override
    public int read(byte[] b) throws IOException {
        //loop to insert starting format(dimensions,goal,start)
        for(int i=0;i<12;i++)
        {
            b[i] = (byte) this.read();
        }

        boolean zero = true; // start with zero's,alternate with 1's
        for(int i=12;i<b.length;i++)
        {
            int a = this.read() & 0xFF;
            if(a==0)
            {
                i--;
                zero = !zero;
                continue;
            }
            for(int j=0;j<a;j++)
            {
                if((i+j) < b.length) {
                    if (zero) {
                        b[i + j] = 0;
                    } else {
                        b[i + j] = 1;
                    }
                }
            }
            if(a>0) {
                i += a - 1;
            }
            zero = !zero;
        }
        return b.length;
    }

}
