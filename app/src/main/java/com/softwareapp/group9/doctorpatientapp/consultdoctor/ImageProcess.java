package com.softwareapp.group9.doctorpatientapp.consultdoctor;

public class ImageProcess {

    private static int decodeToRedSum(int h, int w, byte[] yuv420sp) {

        if (yuv420sp == null)
        {
            return 0;
        }

        final int frameSize = h * w;

        int total = 0;
        for (int i = 0, yp = 0; i < h; i++)
        {
            int uvp = frameSize + (i >> 1) * w, m = 0, n = 0;

            for (int j = 0; j < w; j++, yp++)
            {
                int k = (yuv420sp[yp] & 0xff) - 16;

                if (k < 0)
                {
                    k = 0;
                }

                if ((j & 1) == 0)
                {
                    m = (yuv420sp[uvp++] & 0xff) - 128;
                    n = (yuv420sp[uvp++] & 0xff) - 128;
                }


                int y1192 = k * 1192;

                int b = (y1192 + 2066 * m);
                int g = (y1192 - 833 * n - 400 * m);
                int r = (y1192 + 1634 * n);


                if (b > 262143)
                {
                    b = 262143;
                }

                else if (b < 0)
                {
                    b = 0;
                }

                if (g > 262143)
                {
                    g = 262143;
                }

                else if (g < 0 )
                {
                    g = 0;
                }

                if (r > 262143)
                {

                    r = 262143;
                }

                else if (r < 0)
                {
                    r = 0;
                }


                int pixel = 0xff000000 | (0xff & (b >> 10)) | (0xff00 & (g >> 2)) | (0xff0000 & (r << 6));

                int red = (pixel >> 16) & 0xff;
                total += red;
            }
        }
        return total;
    }


    public static int decodeToRedAvg(int h, int w, byte[] yuv420sp) {

        if (yuv420sp == null)
        {
            return 0;
        }

        int total = decodeToRedSum(h, w, yuv420sp);
        final int frameSz = h * w;

        return (total / frameSz);
    }
}
