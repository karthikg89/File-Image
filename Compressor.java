import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;

public class Compressor {

	static class Pixel {
		int r, g, b, data;

		public Pixel (int r, int g, int b) {
			data = (r << 16) | (g << 8) | b;
		}

		void setPixel (int x, int y, BufferedImage img) {
			img.setRGB(x, y, data);
		}
	}

	static int countChars (String s) throws IOException {
		int count = 0;

		FileReader input = null;
		try {
			input = new FileReader(s);
			
			while (input.read() != -1) {
				count++;
			}
		} finally {
			if (input != null)
				input.close();
		}
		return count;
	}


	public static void main (String[] args) throws IOException {
		FileReader input = null;

		BufferedImage img = null;

		int length;

		try {
			input = new FileReader(args[0]);
			File output = new File(args[1]);

			length = countChars(args[0]);
			int n = ((int) Math.sqrt(length/3)) + 1;
			img = new BufferedImage(n, n, BufferedImage.TYPE_INT_RGB);

			int c1 = 0, c2 = 0, c3 = 0, endx = 0, endy = 0;
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++) {
					if ((c1 = input.read()) != -1 && (c2 = input.read()) != -1 
						&& (c3 = input.read()) != -1) {
						(new Pixel(c1,c2,c3)).setPixel(x, y, img);
					} else {
						endx = x;
						endy = y;
						break;
					}
				}
				if (endx != 0) break;
			}

			if (c2 == -1)
				(new Pixel(c1,0,0)).setPixel(endx, endy, img);
			else if (c3 == -1)
				(new Pixel(c1,c2,0)).setPixel(endx, endy, img); 



			ImageIO.write(img, "PNG", output);
			
		} finally {
			if (input != null)
				input.close();
		}


	}



}