import java.io.FileWriter;
import java.io.IOException;
import java.io.File;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;


public class Decompressor {

	static class Pixel {
		int r, g, b, data;

		public Pixel (int data) {
			r = (data & 0xFF0000) >> 16;
			g = (data & 0x00FF00) >> 8;
			b = data & 0x0000FF;
		}

		void setPixel (int x, int y, BufferedImage img) {
			img.setRGB(y, x, data);
		}
	}

	public static void main (String[] args) throws IOException {

		BufferedImage img = null;
		FileWriter output = null;

		try {
			output = new FileWriter(args[1]);
			img = ImageIO.read(new File(args[0]));

			for (int x = 0; x < img.getWidth(); x++) {
				for (int y = 0; y < img.getHeight(); y++) {
					Pixel p = new Pixel (img.getRGB(x,y));
					
					if (p.r != 0) 
						output.write(p.r);
					if (p.g != 0) 
						output.write(p.g);
					if (p.b != 0) 
						output.write(p.b);
				}
			}			
		} finally {
			if (output != null)
				output.close();
		}

	}


}