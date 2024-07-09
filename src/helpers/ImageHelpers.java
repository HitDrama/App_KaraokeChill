/**
 * 
 * Lớp chứa những phương thức giúp điều chỉnh kích thước hình ảnh cũng như chuyển các đối tượng mảng byte sang hình ảnh
 */
package helpers;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import java.awt.Graphics2D;

public class ImageHelpers {
	/**
	 * Hàm điều chỉnh kích thước
	 * 
	 * @param originalImage ảnh gốc
	 * @param targetWidth   chiều rộng muốn điều chỉnh
	 * @param targetHeight  chiều dài muốn điều chỉnh
	 * @return resultImage hình ảnh sau khi điều chỉnh kích thước
	 */
	public static Image resize(Image originalImage, int targetWidth, int targetHeight) {
		Image resultImage = originalImage.getScaledInstance(targetWidth, targetHeight, Image.SCALE_SMOOTH);
		return resultImage;
	}
	
	/**
	 * chuyển hình ảnh thành mảng các byte
	 * @param img
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static byte[] toByteArray(Image img, String type) throws IOException {
		BufferedImage bImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_RGB);
		Graphics2D g = bImage.createGraphics();

		g.drawImage(img, 0, 0, null);
		g.dispose();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ImageIO.write(bImage, type, baos);
		byte[] imageInByte = baos.toByteArray();

		return imageInByte;
	}
	
	/**
	 * chuyển mảng các byte về hình ảnh
	 * @param data
	 * @param type
	 * @return
	 * @throws IOException
	 */
	public static Image createImageFromByteArray(byte[] data, String type) throws IOException {
		ByteArrayInputStream bis = new ByteArrayInputStream(data);
		BufferedImage bImage2 = ImageIO.read(bis);
		
		Image img = bImage2.getScaledInstance(bImage2.getWidth(), bImage2.getHeight(), Image.SCALE_SMOOTH);
		return img;
	}
}
