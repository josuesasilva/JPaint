package cg.tp01;

import java.awt.Color;
import java.awt.Point;
import java.awt.event.ItemEvent;
import java.util.Stack;
import sun.awt.Mutex;

/**
 *
 * @author Josué
 */
public class MainWindow extends javax.swing.JFrame {

    private boolean dda, bresenham, circBres, pointFlag, bFill;
    /**
     * Creates new form MainWindow
     */
    public MainWindow() {
        initComponents();
       
        pointFlag = true;
        dda = false;
        bresenham = false;
        circBres = false;
        bFill = false;
        
        radiusInput.setVisible(false);
        radiusLabel.setVisible(false);
    }
    
    private void dda(int x1, int y1, int x2, int y2) {        
        int dx = x2 - x1, dy = y2 - y1;
        float x = x1, y = y1;
        float steps = (Math.abs(dx) > Math.abs(dy)) ? Math.abs(dx) : Math.abs(dy);
        float xIncr = dx/steps;
        float yIncr = dy/steps;
        setPixel(x1, y1);
        
        for (int i = 0; i < steps; i++) {
            x = x + xIncr;
            y = y + yIncr;
            setPixel(Math.round(x), Math.round(y));
        }
    }
    
    private void bresenham(int x1, int y1, int x2, int y2) {
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
        
        setPixel (x,y);

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
                setPixel(x, y);
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
                setPixel(x, y);
            }
            
        }
     
    }
    
    private void circunferencia(int xc, int yc, int r) {
        int x = 0, y = r, p = 3 - 2*r;
        plotCircle(x, y, xc, yc);
        
        while(x < y) {
            if (p < 0) {
                p = p + 4*x + 6;
            } else {
                p = p + 4*(x - y) + 10;
                y = y - 1;
            }
            
            x = x + 1;
            plotCircle(x, y, xc, yc);
        }
    }
    
    private void plotCircle(int x, int y, int xc, int yc) {
        setPixel(xc+x, yc+y);
        setPixel(xc-x, yc+y);
        setPixel(xc+x, yc-y);
        setPixel(xc-x, yc-y);
        setPixel(xc+y, yc+x);
        setPixel(xc-y, yc+x);
        setPixel(xc+y, yc-x);
        setPixel(xc-y, yc-x);
    }
    
    private int distance(int x1, int y1, int x2, int y2) {
        return (int) Math.ceil(Math.sqrt(Math.pow(y2-y1, 2) + Math.pow(x2-x1, 2)));
    }
    
    private void boundaryFill(int x, int y) {
        Stack<Point> points = new Stack<>();
        points.add(new Point(x+1, y));
        
        Color innerColor = getPixelColor(x, y);

        while(!points.isEmpty()) {
            Point currentPoint = points.pop();
            int ix = currentPoint.x;
            int iy = currentPoint.y;
           
            Color currentColor = getPixelColor(ix, iy);
            
            if(currentColor.equals(innerColor)) {
                setPixel(ix, iy);
                points.push(new Point(ix+1, iy));
                points.push(new Point(ix-1, iy));
                points.push(new Point(ix, iy+1));
                points.push(new Point(ix, iy-1));
            }
        }
    }
    
    private void setPoint(int x, int y) {
        if (pointFlag) {
            pointFlag = false;
            p1X.setText(String.valueOf(x));
            p1Y.setText(String.valueOf(y));
            
            if (bFill) {
                boundaryFill(getP1X(), getP1Y());
            }
        } else {
            pointFlag = true;
            p2X.setText(String.valueOf(x));
            p2Y.setText(String.valueOf(y));
            
            if (dda) {
                dda(getP1X(), getP1Y(), getP2X(), getP2Y());
            } else if (bresenham) {
                bresenham(getP1X(), getP1Y(), getP2X(), getP2Y());
            } else if (circBres) {
                circunferencia(getP1X(), getP1Y(), getRadius());
            } else if (bFill) {
                boundaryFill(getP2X(), getP2Y());
            }
        }
    }
    
    private int getRadius() {
        int d = distance(getP1X(), getP1Y(), getP2X(), getP2Y()); 
        radiusInput.setText(String.valueOf(d));
        return d;
    }
 
    private void setPixel(int x, int y) {
        myJPanel1.setPixel(x, y);
    }
    
    private void setPixel(int x, int y, Color color) {
        myJPanel1.setPixel(x, y, color);
    }
    
    private Color getPixelColor(int x, int y) {
        return myJPanel1.getPixelColor(x, y);
    }
    
    private Color getSelectedColor() {
        return MyJPanel.getColor();
    }
    
    private int getP1X() {
        return Integer.parseInt(p1X.getText());
    }
    private int getP1Y() {
        return Integer.parseInt(p1Y.getText());
    }
    
    private int getP2X() {
        return Integer.parseInt(p2X.getText());
    }
    private int getP2Y() {
        return Integer.parseInt(p2Y.getText());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        myJPanel1 = new cg.tp01.MyJPanel();
        radioButtonDDA = new javax.swing.JRadioButton();
        radioButtonBres = new javax.swing.JRadioButton();
        radioButtonCirc = new javax.swing.JRadioButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        p1Y = new javax.swing.JTextField();
        p2Y = new javax.swing.JTextField();
        p2X = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        p1X = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        radiusLabel = new javax.swing.JLabel();
        radiusInput = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        radioButtonFill = new javax.swing.JRadioButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("JPaint");
        setPreferredSize(new java.awt.Dimension(760, 500));
        setResizable(false);

        jLabel1.setText("X:");

        jTextField1.setEditable(false);

        jTextField2.setEditable(false);

        jLabel2.setText("Y:");

        myJPanel1.setBackground(new java.awt.Color(255, 255, 255));
        myJPanel1.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                myJPanel1MouseDragged(evt);
            }
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                myJPanel1MouseMoved(evt);
            }
        });
        myJPanel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                myJPanel1MouseClicked(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                myJPanel1MousePressed(evt);
            }
        });

        javax.swing.GroupLayout myJPanel1Layout = new javax.swing.GroupLayout(myJPanel1);
        myJPanel1.setLayout(myJPanel1Layout);
        myJPanel1Layout.setHorizontalGroup(
            myJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 514, Short.MAX_VALUE)
        );
        myJPanel1Layout.setVerticalGroup(
            myJPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 429, Short.MAX_VALUE)
        );

        buttonGroup1.add(radioButtonDDA);
        radioButtonDDA.setText("Algoritmo DDA");
        radioButtonDDA.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonDDAItemStateChanged(evt);
            }
        });

        buttonGroup1.add(radioButtonBres);
        radioButtonBres.setText("Algoritmo de Bresenham");
        radioButtonBres.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonBresItemStateChanged(evt);
            }
        });

        buttonGroup1.add(radioButtonCirc);
        radioButtonCirc.setText("Circunferência Bresenham");
        radioButtonCirc.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonCircItemStateChanged(evt);
            }
        });

        jLabel3.setText("Ponto 1:");

        jLabel4.setText("X:");

        jLabel5.setText("Y:");

        p1Y.setEditable(false);

        p2Y.setEditable(false);

        p2X.setEditable(false);

        jLabel6.setText("Y:");

        jLabel7.setText("X:");

        jLabel8.setText("Ponto 2:");

        p1X.setEditable(false);

        jButton1.setText("Apagar tudo");
        jButton1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton1MouseClicked(evt);
            }
        });

        jButton2.setText("Desfazer");
        jButton2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton2MouseClicked(evt);
            }
        });

        radiusLabel.setText("Raio:");

        radiusInput.setEditable(false);
        radiusInput.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                radiusInputFocusLost(evt);
            }
        });

        jButton3.setText("Selecionar cor");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioButtonFill);
        radioButtonFill.setText("Preenchimento");
        radioButtonFill.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                radioButtonFillItemStateChanged(evt);
            }
        });

        jMenu1.setText("Arquivo");

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_F4, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem2.setText("Sair");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem2);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Ajuda");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.ALT_MASK));
        jMenuItem1.setText("Documentação");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 154, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(524, 524, 524)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel7)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(p2X, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel6)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(p2Y, javax.swing.GroupLayout.PREFERRED_SIZE, 72, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(radiusLabel)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(radiusInput, javax.swing.GroupLayout.PREFERRED_SIZE, 58, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel4)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(p1X, javax.swing.GroupLayout.PREFERRED_SIZE, 67, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addGap(8, 8, 8)
                                .addComponent(p1Y, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(myJPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioButtonDDA, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioButtonBres, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioButtonCirc, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioButtonFill, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(myJPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel1)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(radioButtonDDA)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioButtonBres)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioButtonCirc)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(radioButtonFill)
                        .addGap(7, 7, 7)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(radiusLabel)
                            .addComponent(radiusInput, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(p1Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(p1X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(p2X, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7)
                            .addComponent(jLabel6)
                            .addComponent(p2Y, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2)))
                .addGap(5, 5, 5))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void myJPanel1MouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJPanel1MouseMoved
        jTextField1.setText(String.valueOf(evt.getX()));
        jTextField2.setText(String.valueOf(evt.getY()));
    }//GEN-LAST:event_myJPanel1MouseMoved

    private void myJPanel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJPanel1MouseClicked
        setPoint(evt.getX(), evt.getY());
    }//GEN-LAST:event_myJPanel1MouseClicked

    private void radioButtonDDAItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonDDAItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            dda = true;
            System.out.println("DDA radio button selected");
        } else {
            dda = false;
            System.out.println("DDA radio button deselected");
        }
    }//GEN-LAST:event_radioButtonDDAItemStateChanged

    private void radioButtonBresItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonBresItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            bresenham = true;
            System.out.println("Bresenham radio button selected");
        } else {
            bresenham = false;
            System.out.println("Bresenham radio button deselected");
        }
    }//GEN-LAST:event_radioButtonBresItemStateChanged

    private void radioButtonCircItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonCircItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            circBres = true;
            radiusInput.setVisible(true);
            radiusLabel.setVisible(true);
            
            System.out.println("Circunferência Bresenham radio button selected");
        } else {
            circBres = false;
            radiusInput.setVisible(false);
            radiusLabel.setVisible(false);
            System.out.println("Circunferência Bresenham radio button deselected");
        }
    }//GEN-LAST:event_radioButtonCircItemStateChanged

    private void myJPanel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJPanel1MousePressed
        
    }//GEN-LAST:event_myJPanel1MousePressed

    private void jButton1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton1MouseClicked
        myJPanel1.clear();
    }//GEN-LAST:event_jButton1MouseClicked

    private void jButton2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton2MouseClicked
        myJPanel1.undo();
    }//GEN-LAST:event_jButton2MouseClicked

    private void myJPanel1MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_myJPanel1MouseDragged
        myJPanel1.setPixel(evt.getX(), evt.getY());
        setPoint(evt.getX(), evt.getY());
    }//GEN-LAST:event_myJPanel1MouseDragged

    private void radiusInputFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_radiusInputFocusLost
        
        try {
            int radius = Integer.parseInt(radiusInput.getText());
        } catch (Exception e) {
            
        }
    }//GEN-LAST:event_radiusInputFocusLost

    private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem2ActionPerformed
        this.dispose();
    }//GEN-LAST:event_jMenuItem2ActionPerformed

    private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenuItem1ActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            new Documentation().setVisible(true);
        });
    }//GEN-LAST:event_jMenuItem1ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        java.awt.EventQueue.invokeLater(() -> {
            new ColorSelector().setVisible(true);
        });
    }//GEN-LAST:event_jButton3ActionPerformed

    private void radioButtonFillItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_radioButtonFillItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            bFill = true;
            System.out.println("Fill radio button selected");
        } else {
            bFill = false;
            System.out.println("Fill radio button deselected");
        }
    }//GEN-LAST:event_radioButtonFillItemStateChanged

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> {
            new MainWindow().setVisible(true);
        });
    }
    
    private static void exit() {
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private cg.tp01.MyJPanel myJPanel1;
    private javax.swing.JTextField p1X;
    private javax.swing.JTextField p1Y;
    private javax.swing.JTextField p2X;
    private javax.swing.JTextField p2Y;
    private javax.swing.JRadioButton radioButtonBres;
    private javax.swing.JRadioButton radioButtonCirc;
    private javax.swing.JRadioButton radioButtonDDA;
    private javax.swing.JRadioButton radioButtonFill;
    private javax.swing.JTextField radiusInput;
    private javax.swing.JLabel radiusLabel;
    // End of variables declaration//GEN-END:variables
}
