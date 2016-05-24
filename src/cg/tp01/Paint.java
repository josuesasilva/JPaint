package cg.tp01;

import java.awt.Color;
import java.awt.Point;
import java.util.Stack;

/**
 *
 * @author 793604
 */
public class Paint {
    
    private final MyJPanel board;
    
    public Paint(final MyJPanel board) {
        this.board = board;
    }
    
    private void setPixel(int x, int y, Color color) {
        board.setPixel(x, y, color);
    }
    
    private void dda(int x1, int y1, int x2, int y2, Color color) {        
        int dx = x2 - x1, dy = y2 - y1;
        float x = x1, y = y1;
        float steps = (Math.abs(dx) > Math.abs(dy)) ? Math.abs(dx) : Math.abs(dy);
        float xIncr = dx/steps;
        float yIncr = dy/steps;
        setPixel(x1, y1, color);
        
        for (int i = 0; i < steps; i++) {
            x = x + xIncr;
            y = y + yIncr;
            setPixel(Math.round(x), Math.round(y), color);
        }
    }
    
    private void bresenham(int x1, int y1, int x2, int y2, Color color) {
        int incrX, incrY;
        
        int dx = x2 - x1;
        int dy = y2 - y1;
        
        if (dx >= 0) {
            incrX = 1;
        } else {
            incrX = -1;
            dx = -dx;
        }
        
        if (dy >= 0) {
            incrY = 1;
        } else {
            incrY = -1;
            dy = -dy;
        }
        
        int x = x1; 
        int y = y1;
        
        setPixel (x, y, color);

        if (dy < dx) {
            int p = 2 * dy - dx;
            int const1 = 2 * dy;
            int const2 = 2 * (dy - dx);
            
            for (int i = 0; i < dx; i++) {
                x += incrX;
                if (p < 0) {
                    p += const1;
                } else {
                    y += incrY;
                    p += const2;
                }
                setPixel (x, y, color);
            }
            
        } else { 
            int p = 2 * dx - dy;
            int const1 = 2 * dx;
            int const2 = 2 * (dx - dy);
            
            for (int i = 0; i < dy; i++) {
                y += incrY;
                if (p < 0) {
                    p += const1;
                } else {
                    x += incrX;
                    p += const2;
                }
                setPixel (x, y, color);
            }
            
        }
     
    }
    
    private void circunferencia(int xc, int yc, int r, Color color) {
        int x = 0, y = r, p = 3 - 2*r;
        plotCircle(x, y, xc, yc, color);
        
        while(x < y) {
            if (p < 0) {
                p = p + 4*x + 6;
            } else {
                p = p + 4*(x - y) + 10;
                y = y - 1;
            }
            
            x = x + 1;
            plotCircle(x, y, xc, yc, color);
        }
    }
    
    private void plotCircle(int x, int y, int xc, int yc, Color color) {
        setPixel(xc+x, yc+y, color);
        setPixel(xc-x, yc+y, color);
        setPixel(xc+x, yc-y, color);
        setPixel(xc-x, yc-y, color);
        setPixel(xc+y, yc+x, color);
        setPixel(xc-y, yc+x, color);
        setPixel(xc+y, yc-x, color);
        setPixel(xc-y, yc-x, color);
    }
    
    private void boundaryFill(int x, int y, Color color) {
        Stack<Point> points = new Stack<>();
        points.add(new Point(x+1, y));
        
        Color innerColor = getPixelColor(x, y);

        while(!points.isEmpty()) {
            Point currentPoint = points.pop();
            int ix = currentPoint.x;
            int iy = currentPoint.y;
           
            Color currentColor = getPixelColor(ix, iy);
            
            if(currentColor.equals(innerColor)) {
                setPixel(ix, iy, color);
                points.push(new Point(ix+1, iy));
                points.push(new Point(ix-1, iy));
                points.push(new Point(ix, iy+1));
                points.push(new Point(ix, iy-1));
            }
        }
    }

    private Color getPixelColor(int x, int y) {
        return board.getPixelColor(x, y);
    }
    
    public void drawLine(int x1, int y1, int x2, int y2) {
        dda(x1, y1, x2, y2, MyJPanel.getCurrentSelectedColor());
    }
    
    public void drawBresenham(int x1, int y1, int x2, int y2) {
        bresenham(x1, y1, x2, y2, MyJPanel.getCurrentSelectedColor());
    }
    
    public void drawCircle(int xc, int yc, int r) {
        circunferencia(xc, yc, r, MyJPanel.getCurrentSelectedColor());
    }
    
    public void drawFill(int x, int y) {
        boundaryFill(x, y, MyJPanel.getCurrentSelectedColor());
    }
    
    public void drawLine(int x1, int y1, int x2, int y2, Color color) {
        dda(x1, y1, x2, y2, color);
    }
    
    public void drawBresenham(int x1, int y1, int x2, int y2, Color color) {
        bresenham(x1, y1, x2, y2, color);
    }
    
    public void drawCircle(int xc, int yc, int r, Color color) {
        circunferencia(xc, yc, r, color);
    }
    
    public void drawFill(int x, int y, Color color) {
        boundaryFill(x, y, color);
    }
}
