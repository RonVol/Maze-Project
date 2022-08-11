package IO;

import java.io.IOException;
import java.io.InputStream;

public class MyDecompressorInputStream extends InputStream {
    private InputStream in;

    public MyDecompressorInputStream(InputStream is)
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

        for(int i=12;i<b.length;i++)
        {
            int temp = this.read();
            String binaryStr = Integer.toBinaryString(temp);
            String num;
            //take care of padding, especially on last iteration
            if(b.length - i < 8) {
                num = padLeftZeros(binaryStr, b.length - i);
            }else{
                num = padLeftZeros(binaryStr, 8);
            }

            for(int j=0;j<num.length();j++)
            {
                byte res = (byte) Integer.parseInt(String.valueOf(num.charAt(j)));
                b[i+j] = res;
            }
            i+=num.length()-1;
        }
        return b.length;
    }


    private String padLeftZeros(String inputString, int length) {
        if (inputString.length() >= length) {
            return inputString;
        }
        StringBuilder sb = new StringBuilder();
        while (sb.length() < length - inputString.length()) {
            sb.append('0');
        }
        sb.append(inputString);

        return sb.toString();
    }


}
