package paintapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.event.*;

public class PaintApp extends JFrame{
    public Color strokeColor = Color.BLACK,fillColor = Color.BLACK;
    public int strokeSize = 5;
    public int currentAction = 4;
    public Box btnBox;
    public JNumberSpinner spinner;
    public JButton rectBtn,ellipseBtn,lineBtn,fillBtn,strokeBtn,brushBtn,clearBtn;
    Painter painter = new Painter();
    public static void main(String[] args) {
        PaintApp app = new PaintApp();
    }
    public PaintApp(){
        this.setTitle("Painter");
        this.setSize(500,500);
        this.setMinimumSize(new Dimension(500,500));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        /* setting layout */
        btnBox = Box.createHorizontalBox();
        brushBtn  = makeButton("./ico/brush.png",1);
        lineBtn   = makeButton("./ico/line.png", 2);
        ellipseBtn = makeButton("./ico/ellipse.png", 3);
        rectBtn   = makeButton("./ico/rect.png", 4);
        
        strokeBtn = makeColorButton("./ico/stroke.png", 5, true);
        fillBtn   = makeColorButton("./ico/fill.png", 6, false);
        clearBtn  = makeClearButton("C");
        spinner = new JNumberSpinner(strokeSize);
        
        spinner.addChangeListener((ChangeEvent e)->{
            this.strokeSize = (int) spinner.getValue();
        });
        
        this.btnBox.add(clearBtn);
        this.btnBox.add(brushBtn);
        this.btnBox.add(lineBtn);
        this.btnBox.add(ellipseBtn);
        this.btnBox.add(rectBtn);
        this.btnBox.add(strokeBtn);
        this.btnBox.add(fillBtn);
        this.btnBox.add(spinner);
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(painter,BorderLayout.CENTER);
        this.getContentPane().add(this.btnBox,BorderLayout.SOUTH);
        this.setVisible(true);
    }
    private JButton makeClearButton(String title){
        JButton btn = new JButton(title);
        btn.setMargin(new Insets(5,5,5,5));
        btn.setMaximumSize(new Dimension(40,40));
        btn.setBackground(Color.white);
        btn.addActionListener((e)->{
            painter.clearArea();
        });
        return btn;
    }
    private JButton makeButton(String iconPath,int action){
        JButton btn = new JButton();
        Icon icon = new ImageIcon(iconPath);
        btn.setIcon(icon);
        btn.setMargin(new Insets(5,5,5,5));
        btn.setMaximumSize(new Dimension(40,40));
        btn.setBackground(Color.white);
        
        btn.addActionListener((e)->{
            this.currentAction = action;
        });
        
        return btn;
    }
    private JButton makeColorButton(String iconPath,int action,boolean stroke){
        JButton btn = new JButton();
        Icon icon = new ImageIcon(iconPath);
        btn.setIcon(icon);
        btn.setMargin(new Insets(5,5,5,5));
        btn.setMaximumSize(new Dimension(40,40));
        btn.setBackground(Color.white);
        if(stroke) btn.setForeground(strokeColor);
        else btn.setBackground(fillColor);
        btn.addActionListener((e)->{
            if(stroke){
                strokeColor = JColorChooser.showDialog(null, "Pick a stroke color!", Color.black);
                btn.setForeground(fillColor);
            }else{
                fillColor = JColorChooser.showDialog(null,"Pic a fill color!",Color.black);
                btn.setBackground(fillColor);
            }
        });
        return btn;
    }
    
    public class Painter extends JComponent{
        public ArrayList<Integer> strokeSizes = new ArrayList<>();
        public ArrayList<Shape> shapes = new ArrayList<>();
        public ArrayList<Color> shapeFill = new ArrayList<>();
        public ArrayList<Color> shapeStroke= new ArrayList<>();
        public Point drawStart,drawEnd;
        public Painter(){
            this.setBackground(Color.white);
            //Mouse Listener
            this.addMouseListener(new MouseAdapter() {

                @Override
                public void mousePressed(MouseEvent e) {
                    drawStart = new Point(e.getX(),e.getY());
                    drawEnd = drawStart;
                    repaint();
                }
                @Override
                public void mouseReleased(MouseEvent e) {
                    if(currentAction != 1){
                        Shape aShape = drawAccToAction(currentAction,drawStart.x,drawStart.y,e.getX(),e.getY());
                        shapes.add(aShape);
                        strokeSizes.add(strokeSize);
                        shapeFill.add(fillColor);
                        shapeStroke.add(strokeColor);
                        drawStart = null;drawEnd=null;
                        repaint();
                    }
                }
            });
            //END MouseListener
            
            //Motion Listener
            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e){
                    if(currentAction == 1){
                        int x = e.getX();
                        int y = e.getY();
                        strokeColor = fillColor;
                        Shape eShape = null;
                        eShape = drawBrush(x,y,strokeSize,strokeSize);
                        shapes.add(eShape);
                        shapeFill.add(fillColor);
                        shapeStroke.add(strokeColor);
                        strokeSizes.add(strokeSize);
                    }
                    drawEnd = new Point(e.getX(),e.getY());
                    repaint();
                }
            }); 
            // End MotionListener
        }
        @Override
        public void paint(Graphics g) {
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setStroke(new BasicStroke(strokeSize));
            Iterator<Color> strokeCounters = shapeStroke.iterator();
            Iterator<Color> fillCounter = shapeFill.iterator();
            Iterator<Integer> strokeSizeCounter = strokeSizes.iterator();
            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1f));
            for(Shape s : shapes){
                g2.setStroke(new BasicStroke(strokeSizeCounter.next()));
                g2.setPaint(strokeCounters.next());
                g2.draw(s);
                g2.setPaint(fillCounter.next());
                g2.fill(s);
            }
            if(drawStart != null && drawEnd != null){
                if(currentAction != 1){
                    g2.setStroke(new BasicStroke(strokeSize));
                    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.40f));
                    g2.setPaint(Color.gray);
                    Shape eShape = drawAccToAction(currentAction,drawStart.x,drawStart.y,drawEnd.x,drawEnd.y);
                    g2.fill(eShape);
                    if(currentAction == 2) g2.draw(eShape);
                }
            }
        }
        private Shape drawRect(int x1,int y1,int x2,int y2){
            int x = Math.min(x1, x2);
            int y = Math.min(y1,y2);
            int width = Math.abs(x1-x2);
            int height = Math.abs(y1-y2);
            return new Rectangle2D.Float(x,y,width,height);
        }
        private Shape drawEllipse(int x1,int y1,int x2,int y2){
            int x = Math.min(x1, x2);
            int y = Math.min(y1,y2);
            int width = Math.abs(x1-x2);
            int height = Math.abs(y1-y2);
            return new Ellipse2D.Float(x,y,width,height);
        }
        private Shape drawLine(int x1,int y1,int x2, int y2){
            return new Line2D.Float(x1, y1, x2, y2);
        }

        private Ellipse2D.Float drawBrush(int x1, int y1, int width, int height){
            return new Ellipse2D.Float(x1, y1, width, height);
        }

        // brush line ellipse rect
        private Shape drawAccToAction(int action,int x1,int y1,int x2,int y2){
            Shape result = null;
            switch(action){
                case 4:
                    result = drawRect(x1, y1, x2, y2);
                    break;
                case 3:
                    result = drawEllipse(x1,y1,x2,y2);
                    break;
                case 2:
                    result = drawLine(x1,y1,x2,y2);
                    break;
            }
            return result;
        }
        public void clearArea(){
            painter.shapes.clear();
            painter.shapeFill.clear();
            painter.shapeStroke.clear();
            painter.strokeSizes.clear();
            painter.repaint();
        }
    }
}
