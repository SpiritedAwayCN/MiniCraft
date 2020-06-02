package minicraft.frontend.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.font.LineMetrics;
import java.awt.geom.Rectangle2D;

import javax.swing.JButton;
import javax.swing.border.Border;

/*
 * 一个自己重写绘制方法、比较漂亮的button类
 * 参考：https://blog.csdn.net/gaowen_han/article/details/8164722
 * 		class test.MyButton
 */
public class FairButton extends JButton {
	protected String state="normal";
	// state = "normal" | "focused" | "pressed"
	Font font;
	
	FairButton(String caption){
		super(caption);
		this.setDoubleBuffered(true);
		font= new Font("Sitka Text",Font.PLAIN,17);
		
		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseEntered(MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("光标移入组件");
				state = "focused";
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.println("光标移出组件");
				state = "normal";
				repaint();
			}

			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.print("鼠标按键被按下，");
				state = "pressed";
				repaint();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				// TODO Auto-generated method stub
				//System.out.print("鼠标按键被释放，");
				state = "normal";
				repaint();
			}

		});
		//this.setBorder(new FairButtonBorder());
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		Rectangle rect=this.getBounds();
		if(state.equals("normal")) {
			g.setColor(HSVtoColor(70,0.05f,1.0f));
		}else if(state.equals("focused")) {
			g.setColor(HSVtoColor(90,0.05f,1.0f));
		}else{//pressed
			g.setColor(HSVtoColor(90,0.05f,0.85f));
		}
		if(!this.isEnabled()) {
			g.setColor(new Color(0.5f,0.5f,0.5f,1.0f));
		}
		//g.fillRect(rect.x, rect.y, rect.width, rect.height);
		g.fillRect(0, 0, rect.width, rect.height);
		//这个graphics.fillRect的xy参数竟然是相对坐标，这时我万万没想到的
		
		//下面进入到麻烦的画文字时间
		Graphics2D g2=(Graphics2D)g;
		String text=this.getText();
		
		g.setFont(font);
		g.setColor(Color.black);
		
		Rectangle2D strRect = font.getStringBounds(text, g2.getFontRenderContext());
		LineMetrics lineMetrics = font.getLineMetrics(text, g2.getFontRenderContext());

		g2.drawString(
				text,
				(float) (rect.width / 2 - strRect.getWidth() / 2),
				(float) (rect.height / 2 + ((lineMetrics.getAscent() + lineMetrics.getDescent()) / 2 - lineMetrics.getDescent()))
				);
	}
	/*
	 * 仍待模块化
	 * 参考：https://blog.csdn.net/zhuxb523/article/details/51017139
	 */
	private static Color HSVtoColor(float h, float s, float v )
	{
		float r,b,g;
		
		int i;
		float f, p, q, t;
		if( s == 0 ) {
			// achromatic (grey)
			r = g = b = v;
			return new Color(r,g,b);
		}
		h /= 60;            // sector 0 to 5
		i = (int) h;
		f = h - i;          // factorial part of h
		p = v * ( 1 - s );
		q = v * ( 1 - s * f );
		t = v * ( 1 - s * ( 1 - f ) );
		switch( i ) {
			case 0:
				r = v;
				g = t;
				b = p;
				break;
        	case 1:
        		r = q;
        		g = v;
        		b = p;
        		break;
        	case 2:
        		r = p;
        		g = v;
        		b = t;
        		break;
        	case 3:
        		r = p;
        		g = q;
        		b = v;
        		break;
        	case 4:
        		r = t;
        		g = p;
        		b = v;
        		break;
        	default:        // case 5:
        		r = v;
        		g = p;
        		b = q;
        		break;
		}
		return new Color(r,g,b);
    }

}

//待实现，目标是像win10按钮一样的效果
/*class FairButtonBorder implements Border{

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Insets getBorderInsets(Component c) {
		// TODO Auto-generated method stub
		return new Insets(0, 0, 0, 0);
	}

	@Override
	public boolean isBorderOpaque() {
		// TODO Auto-generated method stub
		return false;
	}
	
	public FairButtonBorder() {
		
	}
	
}*/

