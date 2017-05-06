import java.awt.AWTException;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;  
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;  
import org.opencv.core.Mat;   
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;  
import org.opencv.core.Size;
import org.opencv.imgproc.*;
import org.opencv.core.CvType;
import org.opencv.videoio.VideoCapture;

/*
 * Color tracking system, modified to function in OpenCV 3.2.0 without deprecated methods.
 * Modified to have a more lenient color tracking range for smoother cursor movement.
 *
 * The following code is based on source for the Color Tracking program by Rishi Desai (https://github.com/apache8080/ColorTrackingOpenCV)
 */

/**
 * This panel handles image conversion
 */
class Panel extends JPanel {
	private static final long serialVersionUID = 1L;  
	private BufferedImage image;

	public Panel(){
		super();
	}

	private BufferedImage getImage() {
		return image;  
	}

	public void setImageWithMat(Mat newImage) {
		image = this.matToBufferedImage(newImage);
	}

	/**  
	 * Converts/writes a Mat into a BufferedImage.  
	 *  
	 * @param matrix Mat of type CV_8UC3 or CV_8UC1  
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY  
	 */  
	private BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();  
		int rows = matrix.rows();  
		int elemSize = (int)matrix.elemSize();  
		byte[] data = new byte[cols * rows * elemSize];  
		int type;  
		matrix.get(0, 0, data);  
		switch (matrix.channels()) {  
		case 1:  
			type = BufferedImage.TYPE_BYTE_GRAY;  
			break;  
		case 3:  
			type = BufferedImage.TYPE_3BYTE_BGR;  
			// bgr to rgb conversion
			byte b;  
			for(int i=0; i<data.length; i=i+3) {  
				b = data[i];  
				data[i] = data[i+2];  
				data[i+2] = b;  
			}  
			break;  
		default:  
			return null;  
		}  
		BufferedImage image2 = new BufferedImage(cols, rows, type);  
		image2.getRaster().setDataElements(0, 0, cols, rows, data);  
		return image2;  
	}  
	@Override  
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage temp= getImage();
		if( temp != null)
			g.drawImage(temp,10,10,temp.getWidth(),temp.getHeight(), this);  
	}  
}

/**
 * This class is in charge of analyzing the image for contours and changing the mouse position.
 */
public class ledObjectTrack {

	public static void main(String arg[]) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		try {
			trackColor();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	
	private static void trackColor() throws AWTException {
		Mouse mouse = new Mouse();

		JFrame frame1 = new JFrame("Camera");
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setSize(640,480);  
		frame1.setBounds(0, 0, frame1.getWidth(), frame1.getHeight());  
		Panel panel1 = new Panel();  
		frame1.setContentPane(panel1);  
		frame1.setVisible(true);

		JFrame frame2 = new JFrame("HSV");  
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame2.setSize(640,480);  
		frame2.setBounds(300,100, frame2.getWidth()+300, 100+frame2.getHeight());  
		Panel panel2 = new Panel();  
		frame2.setContentPane(panel2);  
		frame2.setVisible(true);

		/*JFrame frame3 = new JFrame("S,V Distance");  
                      frame3.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
                      frame3.setSize(640,480);  
                      frame3.setBounds(600,200, frame3.getWidth()+600, 200+frame3.getHeight());  
                      Panel panel3 = new Panel();  
                      frame3.setContentPane(panel3);  
                      frame3.setVisible(true);*/

		JFrame frame4 = new JFrame("Threshold");  
		frame4.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame4.setSize(640,480);  
		frame4.setBounds(900,300, frame2.getWidth()+900, 300+frame2.getHeight());  
		Panel panel4 = new Panel();  
		frame4.setContentPane(panel4);      
		frame4.setVisible(true);

		//read video stream
		VideoCapture capture =new VideoCapture(0);
		//capture.set(10, 0);
		capture.set(3, 640);
		capture.set(4, 480);
		capture.set(15, -2);

		Mat webcam_image=new Mat();  
		Mat hsv_image=new Mat();  
		Mat threshold=new Mat();
		Mat threshold2=new Mat();
		capture.read(webcam_image);

		frame1.setSize(webcam_image.width()+40,webcam_image.height()+60);
		frame2.setSize(webcam_image.width()+40,webcam_image.height()+60);  
		//frame3.setSize(webcam_image.width()+40,webcam_image.height()+60);  
		frame4.setSize(webcam_image.width()+40,webcam_image.height()+60);

		Mat array255=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);  
		array255.setTo(new Scalar(255));
		Mat distance=new Mat(webcam_image.height(),webcam_image.width(),CvType.CV_8UC1);
		List<Mat> lhsv = new ArrayList<Mat>(3);      
		Mat circles = new Mat();

		Scalar hsv_min = new Scalar(40, 50, 100);
		Scalar hsv_max = new Scalar(70, 255, 255);
		Scalar hsv_min2 = new Scalar(100,100,100);
		Scalar hsv_max2 = new Scalar(120,255,255);


		if(capture.isOpened()) {
			while(true) {
				capture.read(webcam_image);  
				if( !webcam_image.empty() ) {
					//search color range
					Imgproc.cvtColor(webcam_image, hsv_image, Imgproc.COLOR_BGR2HSV);  
					Core.inRange(hsv_image, hsv_min, hsv_max, threshold);
					Core.inRange(hsv_image, hsv_min2, hsv_max2, threshold2);
					Core.bitwise_or(threshold, threshold2, threshold);
					Imgproc.erode(threshold, threshold, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8,8)));
					Imgproc.dilate(threshold, threshold, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(8, 8)));

					Core.split(hsv_image, lhsv);
					Mat S = lhsv.get(1);  
					Mat V = lhsv.get(2);  
					Core.subtract(array255, S, S);  
					Core.subtract(array255, V, V);  
					S.convertTo(S, CvType.CV_32F);  
					V.convertTo(V, CvType.CV_32F);  
					Core.magnitude(S, V, distance);  
					Core.inRange(distance,new Scalar(0.0), new Scalar(200.0), threshold2);
					Core.bitwise_and(threshold, threshold2, threshold);

					//find circles here
					Imgproc.GaussianBlur(threshold, threshold, new Size(9,9),0,0);
					List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
					Imgproc.HoughCircles(threshold, circles, Imgproc.CV_HOUGH_GRADIENT, 2, threshold.height()/8, 200, 100, 0, 0);
					Imgproc.findContours(threshold, contours, threshold2, Imgproc.RETR_LIST,Imgproc.CHAIN_APPROX_SIMPLE);
					Imgproc.drawContours(webcam_image, contours, -1, new Scalar(255, 0, 0), 2);

					int x = mouse.getX(contours);
					int y = mouse.getY(contours);
					//moves mouse to the specific x,y coordinate calculated in the Mouse class
					mouse.moveMouse(webcam_image.width() - x, y);
					System.out.println(x);
					System.out.println(y);
					Imgproc.circle(webcam_image, new Point(x, y), 4, new Scalar(255,49,0,255), 4);

					distance.convertTo(distance, CvType.CV_8UC1);

					panel1.setImageWithMat(webcam_image);
					panel2.setImageWithMat(hsv_image);
					panel4.setImageWithMat(threshold);
					frame1.repaint();  
					frame2.repaint();
					frame4.repaint();  

				} else {
					System.out.println(" --(!) No captured frame -- Break!");  
					break;  
				}  
			}  
		}
	}
} 
