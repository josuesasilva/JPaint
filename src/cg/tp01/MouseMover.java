package cg.tp01;

import java.awt.AWTException;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MouseMover {
    
    private Robot robot;
    
    public MouseMover() {
        try {
            robot = new Robot();
        } catch (AWTException ex) {
            Logger.getLogger(MouseMover.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public int getCurrentMouseXCoordinate() {
        return Double.valueOf(MouseInfo.getPointerInfo().getLocation().getX()).intValue();
    }
    
    public int getCurrentMouseYCoordinate() {
        return Double.valueOf(MouseInfo.getPointerInfo().getLocation().getY()).intValue();
    }
    
    public void moveMouseToRight() {
        robot.mouseMove(getCurrentMouseXCoordinate()+10, getCurrentMouseYCoordinate());
    }
    
    public void moveMouseToLeft() {
        robot.mouseMove(getCurrentMouseXCoordinate()-10, getCurrentMouseYCoordinate());
    }
    
    public void moveMouseToTop() {
        robot.mouseMove(getCurrentMouseXCoordinate(), getCurrentMouseYCoordinate()-10);
    }
    
    public void moveMouseToBottom() {
        robot.mouseMove(getCurrentMouseXCoordinate(), getCurrentMouseYCoordinate()+10);
    }
}
