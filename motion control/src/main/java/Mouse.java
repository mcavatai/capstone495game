import java.awt.AWTException;
import java.awt.Robot;
import org.opencv.core.MatOfPoint;
import java.util.List;
import java.util.ArrayList;
import org.opencv.imgproc.*;


public class Mouse {
	private Robot mouse;
	Mouse() throws AWTException{
		
		mouse = new Robot();
	}
	public int getX(List<MatOfPoint> contours){
		List<Moments> mu = new ArrayList<Moments>(contours.size());
		int x=0;
        for (int i = 0; i < contours.size(); i++) {
            mu.add(i, Imgproc.moments(contours.get(i), false));
            Moments p = mu.get(i);
            x = (int) (p.get_m10() / p.get_m00());
        }
        return x;
	}
	public int getY(List<MatOfPoint> contours){
		List<Moments> mu = new ArrayList<Moments>(contours.size());
		int y=0;
        for (int i = 0; i < contours.size(); i++) {
            mu.add(i, Imgproc.moments(contours.get(i), false));
            Moments p = mu.get(i);
            y = (int) (p.get_m01() / p.get_m00());
        }
        return y;
	}
	public void moveMouse(int x, int y){
            mouse.mouseMove(x, y);
	}
}
