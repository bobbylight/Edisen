package org.fife.edisen.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.*;

/**
 * Renders CHR ROM.
 */
public class ChrRomViewer extends JComponent {

    private Edisen edisen;
    private File chrFile;
    private BufferedImage image;

    public ChrRomViewer(Edisen edisen, File chrFile) {
        this.edisen = edisen;
        this.chrFile = chrFile;
    }

    private void initializeImage() {

        int w = 512;//getWidth();
        int h = 256;//getHeight();

        image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        int[] pixels = ((DataBufferInt)image.getRaster().getDataBuffer()).getData();

        if (chrFile != null) {

            try (BufferedInputStream bin = new BufferedInputStream(new FileInputStream(chrFile))) {
                loadIntoPixelArray(pixels, image.getWidth(), bin);
            } catch (IOException ioe) {
                edisen.displayException(ioe);
            }
        }
    }

    private void loadIntoPixelArray(int[] pixels, int w, InputStream in) throws IOException {

        int xOffs = 0;
        int yOffs = 0;
        byte[] b = new byte[16];
        int count;
        int total = 0;
        while ((count = in.read(b)) == b.length) {

            total += count;

            for (int y = 0; y < 8; y++) {

                int[] newPixels = new int[8];
                for (int x = 0; x < 8; x++) {
                    newPixels[7 - x] = ((b[y] >> x) & 1) +
                            ((b[y + 8] >> x) & 1) * 2;
                }

                for (int i = 0; i < newPixels.length; i++) {
                    int color;
                    switch (newPixels[i]) {
                        case 0:
                            color = 0xffffffff;
                            break;
                        case 1:
                            color = 0xffff0000;
                            break;
                        case 2:
                            color = 0xff00ff00;
                            break;
                        case 3:
                            color = 0xff0000ff;
                            break;
                        default:
                            throw new RuntimeException("Unexpected color value: " + newPixels[i]);
                    }
                    pixels[yOffs * w + xOffs + i] = color;
                }

                yOffs++;
            }

            xOffs += 8;
            if (xOffs == w) {
                xOffs = 0;
            }
            else {
                yOffs -= 8;
            }

        }
        assert count == -1;

        System.out.println("Read " + total + " bytes");
    }

    @Override
    protected void paintComponent(Graphics g) {

        if (image == null) {
            initializeImage();
        }

        g.drawImage(image, 0, 0, null);
    }
}
