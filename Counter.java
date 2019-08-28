import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.Timer;

class Counter {

	private static int cnt;
	static JFrame f;

	public static void main(String args[]) {

		f = new JFrame();
		f.setSize(100, 100);
		f.setVisible(true);

		ActionListener actListner = new ActionListener() {

			@Override

			public void actionPerformed(ActionEvent event) {
				cnt++;
				System.out.println(cnt);
			}

		};

		Timer timer = new Timer(500, actListner);

		timer.start();
	}
}