package me.jdon.simply.snap;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Area;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
public class Snapper {
	
	JFrame frame;
	
	public Snapper(){
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
                }
                Image icon = new ImageIcon(Snapper.class.getResource("/me/jdon/simply/snap/IconHR.png")).getImage();
                frame = new JFrame("SimplySnap");
                frame.setIconImage(icon);
                frame.setUndecorated(true);
                frame.setBackground(new Color(1, 1, 1, 1));
                frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                frame.setLayout(new BorderLayout());
                frame.add(new CapturePane());
                Rectangle bounds = getVirtualBounds();
                frame.setLocation(bounds.getLocation());
                frame.setSize(bounds.getSize());
                frame.setAlwaysOnTop(true);
                frame.setVisible(true);
                Cursor cursor = Cursor.getDefaultCursor();
                //change cursor appearance to HAND_CURSOR when the mouse pointed on images
                cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR); 
                frame.setCursor(cursor);
                frame.setOpacity(0.3f);
               // frame.setIconImage(icon);
            }
        });
	}
	
    public  Rectangle getVirtualBounds() {
        Rectangle bounds = new Rectangle(0, 0, 0, 0);

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice lstGDs[] = ge.getScreenDevices();
        for (GraphicsDevice gd : lstGDs) {
            bounds.add(gd.getDefaultConfiguration().getBounds());
        }
        return bounds;
    }
    public class CapturePane extends JPanel {
    	Options o = new Options(false);
    	private static final long serialVersionUID = 1L;
    	private Rectangle selectionBounds;
        private Point clickPoint;
        
        

        public CapturePane() {
            setOpaque(false);

            MouseAdapter mouseHandler = new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (SwingUtilities.isRightMouseButton(e)) {
                       frame.dispose();
                       MenuInterface menu = new MenuInterface();
                       menu.openMenu();
                    }
                }

                @Override
                public void mousePressed(MouseEvent e) {
                    if(SwingUtilities.isRightMouseButton(e)){
                    	frame.dispose();
                    	
                    }
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        clickPoint = e.getPoint();
                        selectionBounds = null;
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (SwingUtilities.isLeftMouseButton(e)) {
                        clickPoint = null;
                        frame.dispose();
                        if(selectionBounds != null){
                        SimplySnap.saveImage(selectionBounds);
                        }
                    }
                   
                }
                @Override
                public void mouseDragged(MouseEvent e) {
                    Point dragPoint = e.getPoint();
                    int x = Math.min(clickPoint.x, dragPoint.x);
                    int y = Math.min(clickPoint.y, dragPoint.y);
                    int width = Math.max(clickPoint.x - dragPoint.x, dragPoint.x - clickPoint.x);
                    int height = Math.max(clickPoint.y - dragPoint.y, dragPoint.y - clickPoint.y);
                    selectionBounds = new Rectangle(x, y, width, height);
                    repaint();
                }
            };
            
           
            KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventDispatcher() {

                @Override
                public boolean dispatchKeyEvent(KeyEvent ke) {
                    
                        switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) {
                                frame.dispose();
                            }
                            break;

                        case KeyEvent.KEY_RELEASED:
                            if (ke.getKeyCode() == KeyEvent.VK_TAB) {
                            	frame.dispose();
                            	MenuInterface menu = new MenuInterface();
                            	menu.openMenu();
                            }
                            break;
                        }
                        return false;
                    }
                
            });
            
            addMouseListener(mouseHandler);
            addMouseMotionListener(mouseHandler);
        }
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g.create();
            g2d.setColor(new Color(1, 1, 1, 1));
            
            Area fill = new Area(new Rectangle(new Point(0, 0), getSize()));
            if (selectionBounds != null) {
               //fill.subtract(new Area(selectionBounds));
            }
            g2d.fill(fill);
           
            if (selectionBounds != null) {
                g2d.setColor(Color.decode(o.getColour()));
                g2d.fill(selectionBounds);
                g2d.setColor(Color.blue);
                g2d.draw(selectionBounds);
            }
            g2d.dispose();
        }
    }
}
