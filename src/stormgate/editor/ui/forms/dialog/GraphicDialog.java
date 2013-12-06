/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * GraphicDialog.java
 *
 * Created on Mar 22, 2011, 4:37:04 PM
 */
package stormgate.editor.ui.forms.dialog;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import stormgate.common.Common;
import stormgate.common.ScreenInformation;
import stormgate.editor.ui.forms.fileDialog.AWTFileDialog;
import stormgate.image.ImageManager;
import stormgate.image.LibraryResource;

/**
 *
 * @author David
 */
public class GraphicDialog extends javax.swing.JDialog {

    private File targetFile;
    private ImageManager manager;
    private LibraryResource resource;

    /** Creates new form GraphicDialog */
    public GraphicDialog(File targetFile, LibraryResource resource, ImageManager manager) {
        super(new Frame(), true);

        //setLocationRelativeTo(null);

        this.targetFile = targetFile;
        this.resource = resource;
        this.manager = manager;

        initComponents();

        graphicDialogRender1.setResource(resource);
        graphicDialogRender1.setGraphicDialog(this);
        graphicDialogRender1.setType(1);

        jCheckBox1.setSelected(resource.getDepth());

        updateValues();
        jTextField7.setText(targetFile.getPath());

        addGlobalKey();

        Point mousePoint = MouseInfo.getPointerInfo().getLocation();
        Dimension d = getSize();
        int setX = mousePoint.x - d.width / 2;
        int setY = mousePoint.y - d.height / 2;

        Rectangle bounds = ScreenInformation.getScreenBounds(mousePoint);

        if (bounds.x > setX) {
            setX = bounds.x;
        }
        if (bounds.y > setY) {
            setY = bounds.y;
        }
        if (bounds.x + bounds.width < setX + d.width) {
            setX = bounds.x + bounds.width - d.width;
        }
        if (bounds.y + bounds.height < setY + d.height) {
            setY = bounds.y + bounds.height - d.height;
        }

        setLocation(setX, setY);
    }

    private void addGlobalKey() {
        addGlobalKey(this);
    }

    private void addGlobalKey(Container current) {
        Component[] children = current.getComponents();
        for (int i = 0; i < children.length; i++) {
            Component child = children[i];

            KeyListener[] listeners = child.getKeyListeners();

            for (int j = 0; j < listeners.length; j++) {
                KeyListener keyListener = listeners[j];
                child.removeKeyListener(keyListener);
            }

            child.addKeyListener(new java.awt.event.KeyAdapter() {

                @Override
                public void keyPressed(java.awt.event.KeyEvent evt) {
                    formKeyPressed(evt);
                }

                @Override
                public void keyReleased(java.awt.event.KeyEvent evt) {
                }
            });

            if (child instanceof Container) {
                addGlobalKey((Container) child);
            }
        }
    }

    private void formKeyPressed(KeyEvent evt) {
        if (evt.getKeyCode() == 10) {
            save();
            dispose();
        } else if (evt.getKeyCode() == 27) {
            dispose();
        }
    }

    public void updateGraphic() {
        int originX = Common.parseInt(jTextField1.getText());
        int originY = Common.parseInt(jTextField2.getText());

        int pAX = Common.parseInt(jTextField3.getText());
        int pAY = Common.parseInt(jTextField4.getText());
        int pBX = Common.parseInt(jTextField5.getText());
        int pBY = Common.parseInt(jTextField6.getText());

        boolean checkValue = jCheckBox1.isSelected();

        graphicDialogRender1.setOrigin(originX, originY);
        graphicDialogRender1.setDepth(checkValue);

        graphicDialogRender1.setDepthPointA(pAX, pAY);
        graphicDialogRender1.setDepthPointB(pBX, pBY);
        repaint();
    }

    private void updateValues() {
        if (resource != null) {
            jTextField1.setText(resource.getOriginX() + "");
            jTextField2.setText(resource.getOriginY() + "");
            jTextField3.setText(resource.getDepthAX() + "");
            jTextField4.setText(resource.getDepthAY() + "");
            jTextField5.setText(resource.getDepthBX() + "");
            jTextField6.setText(resource.getDepthBY() + "");
            //jCheckBox1.setSelected(resource.getDepth());
            updateDepth();
        }
        updateGraphic();
    }

    public void setOrigin(int x, int y) {
        jTextField1.setText(x + "");
        jTextField2.setText(y + "");
    }

    public void setDepthPointA(int x, int y) {
        jTextField3.setText(x + "");
        jTextField4.setText(y + "");
    }

    public void setDepthPointB(int x, int y) {
        jTextField5.setText(x + "");
        jTextField6.setText(y + "");
    }

    private void save() {
        int originX = Common.parseInt(jTextField1.getText());
        int originY = Common.parseInt(jTextField2.getText());

        int pAX = Common.parseInt(jTextField3.getText());
        int pAY = Common.parseInt(jTextField4.getText());
        int pBX = Common.parseInt(jTextField5.getText());
        int pBY = Common.parseInt(jTextField6.getText());

        boolean checkValue = jCheckBox1.isSelected();
        if (resource != null) {
            resource.setOrigin(originX, originY);
            resource.setDepth(checkValue);

            resource.setDepthPointA(pAX, pAY);
            resource.setDepthPointB(pBX, pBY);

            resource.saveIni();

            manager.updateResource(targetFile.getAbsolutePath());
        }
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jTextField3 = new javax.swing.JTextField();
        jTextField4 = new javax.swing.JTextField();
        jTextField5 = new javax.swing.JTextField();
        jTextField6 = new javax.swing.JTextField();
        jCheckBox1 = new javax.swing.JCheckBox();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        graphicDialogRender1 = new stormgate.editor.ui.forms.dialog.components.GraphicDialogRender();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel7 = new javax.swing.JLabel();
        jTextField7 = new javax.swing.JTextField();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Define Graphic");
        setAlwaysOnTop(true);
        setMinimumSize(new java.awt.Dimension(320, 300));

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Save");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jTextField1.setText("0");
        jTextField1.setNextFocusableComponent(jTextField2);
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGraphic(evt);
            }
        });

        jTextField2.setText("0");
        jTextField2.setNextFocusableComponent(jTextField3);
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGraphic(evt);
            }
        });

        jTextField3.setText("0");
        jTextField3.setEnabled(false);
        jTextField3.setMinimumSize(new java.awt.Dimension(59, 20));
        jTextField3.setNextFocusableComponent(jTextField4);
        jTextField3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGraphic(evt);
            }
        });

        jTextField4.setText("0");
        jTextField4.setEnabled(false);
        jTextField4.setNextFocusableComponent(jTextField5);
        jTextField4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGraphic(evt);
            }
        });

        jTextField5.setText("0");
        jTextField5.setEnabled(false);
        jTextField5.setNextFocusableComponent(jTextField6);
        jTextField5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGraphic(evt);
            }
        });

        jTextField6.setText("0");
        jTextField6.setEnabled(false);
        jTextField6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                updateGraphic(evt);
            }
        });

        jCheckBox1.setText("Depth");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        jLabel1.setText("X 1");

        jLabel2.setText("Y 1");

        jLabel3.setText("X 2");

        jLabel4.setText("Y 2");

        jLabel5.setText("X");

        jLabel6.setText("Y");

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setSelected(true);
        jToggleButton1.setText("Origin");
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setText("Point 1");
        jToggleButton2.setEnabled(false);
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setText("Point 2");
        jToggleButton3.setEnabled(false);
        jToggleButton3.setFocusPainted(false);
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        graphicDialogRender1.setResource(null);

        javax.swing.GroupLayout graphicDialogRender1Layout = new javax.swing.GroupLayout(graphicDialogRender1);
        graphicDialogRender1.setLayout(graphicDialogRender1Layout);
        graphicDialogRender1Layout.setHorizontalGroup(
            graphicDialogRender1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 507, Short.MAX_VALUE)
        );
        graphicDialogRender1Layout.setVerticalGroup(
            graphicDialogRender1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 314, Short.MAX_VALUE)
        );

        jScrollPane1.setViewportView(graphicDialogRender1);

        jComboBox2.setEditable(true);
        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Auto", "10%", "50%", "100%", "200%", "400%", "800%" }));
        jComboBox2.setToolTipText("Zoom");
        jComboBox2.setMaximumSize(new java.awt.Dimension(60, 18));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2zoomComboBoxActionPerformed(evt);
            }
        });

        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/magnifier.png"))); // NOI18N
        jLabel7.setToolTipText("Zoom");
        jLabel7.setMaximumSize(new java.awt.Dimension(20, 16));
        jLabel7.setMinimumSize(new java.awt.Dimension(20, 16));

        jTextField7.setEditable(false);
        jTextField7.setToolTipText("File Path");

        jButton3.setText("Set Reference Image");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Clear Reference Image");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 332, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jTextField7, javax.swing.GroupLayout.DEFAULT_SIZE, 230, Short.MAX_VALUE)
                        .addGap(18, 18, 18)
                        .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 20, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGap(19, 19, 19)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addComponent(jButton2)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel5)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel6)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addComponent(jToggleButton1))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel2)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(jCheckBox1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                            .addComponent(jTextField4, 0, 50, Short.MAX_VALUE)
                                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addComponent(jLabel3)
                                            .addComponent(jLabel4))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jToggleButton2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jButton3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton4)))))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jTextField7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jTextField5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(jLabel3))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(jTextField6, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(jCheckBox1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jToggleButton3)
                            .addComponent(jToggleButton2))
                        .addGap(10, 10, 10)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jButton1)
                            .addComponent(jButton2)))
                    .addComponent(jToggleButton1))
                .addGap(10, 10, 10)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        //Save
        save();
        dispose();
    }//GEN-LAST:event_jButton2ActionPerformed
    private void updateDepth() {
        boolean checkValue = jCheckBox1.isSelected();

        jTextField3.setEnabled(checkValue);
        jTextField4.setEnabled(checkValue);
        jTextField5.setEnabled(checkValue);
        jTextField6.setEnabled(checkValue);
        jToggleButton2.setEnabled(checkValue);
        jToggleButton3.setEnabled(checkValue);

        if (!checkValue) {
            boolean selected = jToggleButton1.isSelected();
            if (selected) {
                graphicDialogRender1.setType(1);
            } else {
                graphicDialogRender1.setType(0);
                buttonGroup1.clearSelection();
            }
        }
    }
    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
        updateDepth();
        updateGraphic();
    }//GEN-LAST:event_jCheckBox1ActionPerformed

    private void updateGraphic(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_updateGraphic
        updateGraphic();
    }//GEN-LAST:event_updateGraphic

    private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton1ActionPerformed
        boolean selected = jToggleButton1.isSelected();
        if (selected) {
            graphicDialogRender1.setType(1);
        } else {
            graphicDialogRender1.setType(0);
        }
    }//GEN-LAST:event_jToggleButton1ActionPerformed

    private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton2ActionPerformed
        boolean selected = jToggleButton2.isSelected();
        if (selected) {
            graphicDialogRender1.setType(2);
        } else {
            graphicDialogRender1.setType(0);
        }
    }//GEN-LAST:event_jToggleButton2ActionPerformed

    private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jToggleButton3ActionPerformed
        boolean selected = jToggleButton3.isSelected();
        if (selected) {
            graphicDialogRender1.setType(3);
        } else {
            graphicDialogRender1.setType(0);
        }
    }//GEN-LAST:event_jToggleButton3ActionPerformed

    private void jComboBox2zoomComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBox2zoomComboBoxActionPerformed
        String zoomString = (String) jComboBox2.getSelectedItem();
        float zoomAmount = 0;
        try {
            zoomString = zoomString.replaceAll("%", "");
            zoomAmount = Integer.parseInt(zoomString);
        } catch (Exception e1) {
            zoomAmount = -1;
        }
        float val = zoomAmount / 100;
        graphicDialogRender1.setZoom(val);
        repaint();
}//GEN-LAST:event_jComboBox2zoomComboBoxActionPerformed
    private void setReference(String referenceImage) {
        //Delete old reference file

        File oldReference = resource.getReferenceFile();
        if (oldReference != null && oldReference.exists()) {
            oldReference.delete();
        }
        String newFileName = null;
        if (referenceImage != null) {
            File newReference = new File(referenceImage);
            String filename = newReference.getName();
            int dotIndex = filename.lastIndexOf('.');
            String extension = "";
            if (dotIndex >= 0) {
                extension = filename.substring(dotIndex).toLowerCase();
            }
            newFileName = targetFile.getName() + extension + ".ref";
            File targetDestination = new File(resource.getPath() + newFileName);
            //Name of the file should be targetFile.extension.ref
            Common.copyFile(newReference, targetDestination);
        }
        resource.setReference(newFileName);
        resource.saveIni();
        resource = manager.getResource(targetFile.getAbsolutePath());
        manager.updateResource(targetFile.getAbsolutePath());
        graphicDialogRender1.setResource(resource);
        repaint();
    }
private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    //SELECT REFERENCE IMAGE
    AWTFileDialog fd = new AWTFileDialog();
    String fileSelected = fd.openFile("Select Reference Image", "", "*.png");
    if (fileSelected != null) {
        setReference(fileSelected);
    }
}//GEN-LAST:event_jButton3ActionPerformed

private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
    setReference(null);
}//GEN-LAST:event_jButton4ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private stormgate.editor.ui.forms.dialog.components.GraphicDialogRender graphicDialogRender1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JTextField jTextField3;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField5;
    private javax.swing.JTextField jTextField6;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    // End of variables declaration//GEN-END:variables
}
