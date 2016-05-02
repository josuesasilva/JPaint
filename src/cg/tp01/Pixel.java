package cg.tp01;

import java.awt.Color;
import java.awt.Graphics;

/**
 *
 * @author Josué
 */
public class Pixel {

        int x, y;
        Color color;

        public Pixel(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }

        public void draw(Graphics g) {
            g.setColor(color);
            g.drawOval(x, y, 1, 1);
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }
}
