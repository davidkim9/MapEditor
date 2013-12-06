/*
 * ZoneToolPanel.java
 *
 * Created on Apr 28, 2011, 11:29:16 AM
 */
package stormgate.editor.tool.panel;

import it.cnr.imaa.essi.lablib.gui.checkboxtree.TristateCheckBox;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import stormgate.action.zone.ZoneAddAmbientAction;
import stormgate.action.zone.ZoneAddFootstepAction;
import stormgate.action.zone.ZoneAddSpawnAction;
import stormgate.action.zone.ZoneEnableCollisionAction;
import stormgate.action.zone.ZoneEnableLOSAction;
import stormgate.action.zone.ZoneEnableSkillBankAction;
import stormgate.action.zone.ZoneEnableTownAction;
import stormgate.action.zone.ZoneRemoveAmbientAction;
import stormgate.action.zone.ZoneRemoveFootstepAction;
import stormgate.action.zone.ZoneRemoveSpawnAction;
import stormgate.action.zone.ZoneSetMusicAction;
import stormgate.action.zone.ZoneSetNameAction;
import stormgate.action.zone.ZoneSetWalkoverAction;
import stormgate.common.Common;
import stormgate.data.MapZone;
import stormgate.data.SpawnData;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.ZoneTool;
import stormgate.editor.tool.data.ZoneToolPolygonMode;
import stormgate.editor.ui.forms.browser.LibraryViewer;
import stormgate.editor.ui.forms.fileDialog.AWTFileDialog;

/**
 *
 * @author David
 */
public class ZoneToolPanel extends ToolPanel {

    private ZoneTool tool;
    private EditorData data;
    private DefaultListModel ambientModel;
    private DefaultListModel footstepModel;
    private DefaultListModel spawnModel;
    private String zoneName = null;

    /** Creates new form ZoneToolPanel */
    public ZoneToolPanel() {
        initComponents();

        registerCheckBox(jCheckBox1, new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });
        registerCheckBox(jCheckBox2, new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox2ActionPerformed(evt);
            }
        });
        registerCheckBox(jCheckBox3, new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox3ActionPerformed(evt);
            }
        });
        registerCheckBox(jCheckBox4, new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });

        disablePanel();
    }

    private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {
        TristateCheckBox triBox = (TristateCheckBox) jCheckBox1;
        if (triBox.isEnabled()) {
            if (triBox.getState() == TristateCheckBox.NOT_SELECTED) {
                triBox.setState(TristateCheckBox.SELECTED);
                ZoneEnableCollisionAction e = new ZoneEnableCollisionAction(data.getMap(), getSelected(), true);
                data.performAction(e);
            } else {
                triBox.setState(TristateCheckBox.NOT_SELECTED);
                ZoneEnableCollisionAction e = new ZoneEnableCollisionAction(data.getMap(), getSelected(), false);
                data.performAction(e);
            }
        }
    }

    private void jCheckBox2ActionPerformed(java.awt.event.ActionEvent evt) {
        TristateCheckBox triBox = (TristateCheckBox) jCheckBox2;
        if (triBox.isEnabled()) {
            if (triBox.getState() == TristateCheckBox.NOT_SELECTED) {
                triBox.setState(TristateCheckBox.SELECTED);
                ZoneEnableLOSAction e = new ZoneEnableLOSAction(data.getMap(), getSelected(), true);
                data.performAction(e);
            } else {
                triBox.setState(TristateCheckBox.NOT_SELECTED);
                ZoneEnableLOSAction e = new ZoneEnableLOSAction(data.getMap(), getSelected(), false);
                data.performAction(e);
            }
        }
    }

    private void jCheckBox3ActionPerformed(java.awt.event.ActionEvent evt) {
        TristateCheckBox triBox = (TristateCheckBox) jCheckBox3;
        if (triBox.isEnabled()) {
            if (triBox.getState() == TristateCheckBox.NOT_SELECTED) {
                triBox.setState(TristateCheckBox.SELECTED);
                ZoneEnableSkillBankAction e = new ZoneEnableSkillBankAction(data.getMap(), getSelected(), true);
                data.performAction(e);
            } else {
                triBox.setState(TristateCheckBox.NOT_SELECTED);
                ZoneEnableSkillBankAction e = new ZoneEnableSkillBankAction(data.getMap(), getSelected(), false);
                data.performAction(e);
            }
        }
    }

    private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {
        TristateCheckBox triBox = (TristateCheckBox) jCheckBox4;
        if (triBox.isEnabled()) {
            if (triBox.getState() == TristateCheckBox.NOT_SELECTED) {
                triBox.setState(TristateCheckBox.SELECTED);
                ZoneEnableTownAction e = new ZoneEnableTownAction(data.getMap(), getSelected(), true);
                data.performAction(e);
            } else {
                triBox.setState(TristateCheckBox.NOT_SELECTED);
                ZoneEnableTownAction e = new ZoneEnableTownAction(data.getMap(), getSelected(), false);
                data.performAction(e);
            }
        }
    }

    public void setTool(ZoneTool tool) {
        this.tool = tool;
    }

    public void setData(EditorData data) {
        this.data = data;
    }

    public void setPolygonMode() {
        jToggleButton2.setSelected(true);
    }

    public void setSelectMode() {
        jToggleButton1.setSelected(true);
    }

    public void setEditMode() {
        jToggleButton4.setSelected(true);
    }

    public void setNewMode() {
        jToggleButton3.setSelected(true);
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
        jToggleButton1 = new javax.swing.JToggleButton();
        jToggleButton2 = new javax.swing.JToggleButton();
        jToggleButton3 = new javax.swing.JToggleButton();
        jToggleButton4 = new javax.swing.JToggleButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        jCheckBox2 = new it.cnr.imaa.essi.lablib.gui.checkboxtree.TristateCheckBox();
        jCheckBox3 = new it.cnr.imaa.essi.lablib.gui.checkboxtree.TristateCheckBox();
        jCheckBox4 = new it.cnr.imaa.essi.lablib.gui.checkboxtree.TristateCheckBox();
        jLabel3 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        jList1.setFixedCellWidth(100);
        jLabel5 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jSeparator2 = new javax.swing.JSeparator();
        jSeparator3 = new javax.swing.JSeparator();
        jScrollPane2 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList();
        jList2.setFixedCellWidth(100);
        jLabel6 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jSeparator4 = new javax.swing.JSeparator();
        jButton7 = new javax.swing.JButton();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList3 = new javax.swing.JList();
        jList3.setFixedCellWidth(100);
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JSeparator();
        jCheckBox1 = new it.cnr.imaa.essi.lablib.gui.checkboxtree.TristateCheckBox();
        jButton9 = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();

        setPreferredSize(new java.awt.Dimension(157, 662));

        buttonGroup1.add(jToggleButton1);
        jToggleButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zoneEdit.png"))); // NOI18N
        jToggleButton1.setText("Select Vertices");
        jToggleButton1.setFocusPainted(false);
        jToggleButton1.setFocusable(false);
        jToggleButton1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton1.setIconTextGap(10);
        jToggleButton1.setMaximumSize(new java.awt.Dimension(25, 25));
        jToggleButton1.setMinimumSize(new java.awt.Dimension(25, 25));
        jToggleButton1.setPreferredSize(new java.awt.Dimension(25, 25));
        jToggleButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton1ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton2);
        jToggleButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zoneMode.png"))); // NOI18N
        jToggleButton2.setSelected(true);
        jToggleButton2.setText("Edit Polygons");
        jToggleButton2.setFocusPainted(false);
        jToggleButton2.setFocusable(false);
        jToggleButton2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton2.setIconTextGap(10);
        jToggleButton2.setMaximumSize(new java.awt.Dimension(25, 25));
        jToggleButton2.setMinimumSize(new java.awt.Dimension(25, 25));
        jToggleButton2.setPreferredSize(new java.awt.Dimension(25, 25));
        jToggleButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton2ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton3);
        jToggleButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zoneNew.png"))); // NOI18N
        jToggleButton3.setText("New Zone");
        jToggleButton3.setFocusPainted(false);
        jToggleButton3.setFocusable(false);
        jToggleButton3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton3.setIconTextGap(10);
        jToggleButton3.setMaximumSize(new java.awt.Dimension(25, 25));
        jToggleButton3.setMinimumSize(new java.awt.Dimension(25, 25));
        jToggleButton3.setPreferredSize(new java.awt.Dimension(25, 25));
        jToggleButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton3ActionPerformed(evt);
            }
        });

        buttonGroup1.add(jToggleButton4);
        jToggleButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zone.png"))); // NOI18N
        jToggleButton4.setText("Edit Vertices");
        jToggleButton4.setFocusPainted(false);
        jToggleButton4.setFocusable(false);
        jToggleButton4.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        jToggleButton4.setIconTextGap(10);
        jToggleButton4.setMaximumSize(new java.awt.Dimension(25, 25));
        jToggleButton4.setMinimumSize(new java.awt.Dimension(25, 25));
        jToggleButton4.setPreferredSize(new java.awt.Dimension(25, 25));
        jToggleButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jToggleButton4ActionPerformed(evt);
            }
        });

        jLabel2.setText("Zone Name");

        jCheckBox2.setText("Line Of Sight");

        jCheckBox3.setText("Skill Bank Access");

        jCheckBox4.setText("Town Area");

        jLabel3.setText("Music:");

        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel4.setText("Not Selected");

        jScrollPane1.setViewportView(jList1);

        jLabel5.setText("Ambient Sounds");

        jButton2.setText("Add");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setText("Remove");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jScrollPane2.setViewportView(jList2);

        jLabel6.setText("Footstep Sounds");

        jButton4.setText("Add");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("Remove");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton7.setText("Edit");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jLabel7.setText("Spawn Data");

        jScrollPane3.setViewportView(jList3);

        jButton6.setText("Remove");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton8.setText("Add");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Collision Boundary");

        jButton9.setText("Not Set");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel8.setText("Walkover:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jCheckBox1)
                    .addComponent(jToggleButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jToggleButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jToggleButton4, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jToggleButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 76, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton7))
                    .addComponent(jCheckBox2)
                    .addComponent(jCheckBox3)
                    .addComponent(jCheckBox4)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jSeparator4, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel6)
                        .addGap(14, 14, 14))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton5))
                    .addComponent(jSeparator5, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addComponent(jLabel5)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addComponent(jButton1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jSeparator3, javax.swing.GroupLayout.DEFAULT_SIZE, 137, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 84, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToggleButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToggleButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton9, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addGap(3, 3, 3)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(2, 2, 2)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton4, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 18, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 51, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jToggleButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton2ActionPerformed
	{//GEN-HEADEREND:event_jToggleButton2ActionPerformed
            tool.setPolygonMode();
	}//GEN-LAST:event_jToggleButton2ActionPerformed

	private void jToggleButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton1ActionPerformed
	{//GEN-HEADEREND:event_jToggleButton1ActionPerformed
            tool.setSelectMode();
	}//GEN-LAST:event_jToggleButton1ActionPerformed

	private void jToggleButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton3ActionPerformed
	{//GEN-HEADEREND:event_jToggleButton3ActionPerformed
            tool.setNewMode();
	}//GEN-LAST:event_jToggleButton3ActionPerformed

	private void jToggleButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jToggleButton4ActionPerformed
	{//GEN-HEADEREND:event_jToggleButton4ActionPerformed
            tool.setEditMode();
	}//GEN-LAST:event_jToggleButton4ActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
            //SELECT MUSIC
            AWTFileDialog fd = new AWTFileDialog();
            String fileSelected = fd.openFile("Select Music", LibraryViewer.mainDirectory, "*.mp3");

            if (fileSelected != null) {
                if (LibraryViewer.fileInLibrary(new File(fileSelected))) {
                    ZoneSetMusicAction s = new ZoneSetMusicAction(data.getMap(), getSelected(), new File(fileSelected));
                    data.performAction(s);

                    updatePanel();
                } else {
                    JOptionPane.showMessageDialog(new Frame(), "The selected file must be a file in the library");
                }
            }
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
            //ADD AMBIENT
            AWTFileDialog fd = new AWTFileDialog();
            String fileSelected = fd.openFile("Select Ambient", LibraryViewer.mainDirectory, "*.mp3");

            if (fileSelected != null) {
                if (LibraryViewer.fileInLibrary(new File(fileSelected))) {
                    ZoneAddAmbientAction s = new ZoneAddAmbientAction(data.getMap(), getSelected(), new File(fileSelected));
                    data.performAction(s);

                    updatePanel();
                } else {
                    JOptionPane.showMessageDialog(new Frame(), "The selected file must be a file in the library");
                }
            }
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
	{//GEN-HEADEREND:event_jButton4ActionPerformed
            //ADD FOOTSTEP
            AWTFileDialog fd = new AWTFileDialog();
            String fileSelected = fd.openFile("Select Footstep", LibraryViewer.mainDirectory, "*.mp3");

            if (fileSelected != null) {
                if (LibraryViewer.fileInLibrary(new File(fileSelected))) {
                    ZoneAddFootstepAction s = new ZoneAddFootstepAction(data.getMap(), getSelected(), new File(fileSelected));
                    data.performAction(s);

                    updatePanel();
                } else {
                    JOptionPane.showMessageDialog(new Frame(), "The selected file must be a file in the library");
                }
            }
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jButton8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton8ActionPerformed
	{//GEN-HEADEREND:event_jButton8ActionPerformed
            //ADD SPAWN DATA
            String[] unitType = {"Mob", "NPC"};
            JComboBox spawnType = new JComboBox(unitType);
            JTextField spawnID = new JTextField();
            JTextField spawnCount = new JTextField();
            Object[] msg = {"Spawn Type:", spawnType, "Spawn ID:", spawnID, "Spawn Count:", spawnCount};

            JOptionPane op = new JOptionPane(
                    msg,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.OK_CANCEL_OPTION,
                    null,
                    null);

            JDialog dialog = op.createDialog(this, "Add Spawn Data");
            dialog.setVisible(true);

            int result = JOptionPane.OK_OPTION;

            try {
                result = ((Integer) op.getValue()).intValue();
            } catch (Exception e) {
                return;
            }

            if (result == JOptionPane.OK_OPTION) {

                if (!spawnID.getText().isEmpty() && !spawnCount.getText().isEmpty()) {
                    SpawnData spawn = new SpawnData();
                    spawn.type = spawnType.getSelectedIndex() == 0;
                    spawn.id = Common.parseInt(spawnID.getText());
                    spawn.count = Common.parseInt(spawnCount.getText());

                    ZoneAddSpawnAction s = new ZoneAddSpawnAction(data.getMap(), getSelected(), spawn);
                    data.performAction(s);

                    updatePanel();
                }
            }
	}//GEN-LAST:event_jButton8ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
	{//GEN-HEADEREND:event_jButton3ActionPerformed
            //REMOVE AMBIENT
            //File[] selected = (File[]) jList1.getSelectedValues();
            Object[] objects = jList1.getSelectedValues();
            File[] selected = Arrays.copyOf(objects, objects.length, File[].class);

            if (selected != null) {
                ZoneRemoveAmbientAction r = new ZoneRemoveAmbientAction(data.getMap(), getSelected(), selected);
                data.performAction(r);
            }
	}//GEN-LAST:event_jButton3ActionPerformed

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
	{//GEN-HEADEREND:event_jButton5ActionPerformed
            //REMOVE FOOTSTEP
            Object[] objects = jList2.getSelectedValues();
            File[] selected = Arrays.copyOf(objects, objects.length, File[].class);

            if (selected != null) {
                ZoneRemoveFootstepAction r = new ZoneRemoveFootstepAction(data.getMap(), getSelected(), selected);
                data.performAction(r);
            }
	}//GEN-LAST:event_jButton5ActionPerformed

	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
	{//GEN-HEADEREND:event_jButton6ActionPerformed
            //REMOVE SPAWN
            Object[] objects = jList3.getSelectedValues();
            SpawnData[] selected = Arrays.copyOf(objects, objects.length, SpawnData[].class);

            if (selected != null) {
                ZoneRemoveSpawnAction r = new ZoneRemoveSpawnAction(data.getMap(), getSelected(), selected);
                data.performAction(r);
            }
	}//GEN-LAST:event_jButton6ActionPerformed

	private void jButton7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton7ActionPerformed
	{//GEN-HEADEREND:event_jButton7ActionPerformed
            //EDIT ZONE NAME
            JTextField zoneText = new JTextField();
            if (zoneName != null) {
                zoneText.setText(zoneName);
            }
            Object[] msg = {"Zone Name:", zoneText};

            JOptionPane op = new JOptionPane(
                    msg,
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.OK_CANCEL_OPTION,
                    null,
                    null);

            JDialog dialog = op.createDialog(this, "Edit Zone Name");
            dialog.setVisible(true);

            int result = JOptionPane.OK_OPTION;

            try {
                result = ((Integer) op.getValue()).intValue();
            } catch (Exception e) {
                return;
            }

            if (result == JOptionPane.OK_OPTION) {
                
                String zoneName = null;
                
                if (!zoneText.getText().isEmpty()) {
                    zoneName = zoneText.getText();
                }
                
                ZoneSetNameAction s = new ZoneSetNameAction(data.getMap(), getSelected(), zoneName);
                data.performAction(s);

                updatePanel();
            }
	}//GEN-LAST:event_jButton7ActionPerformed

private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
    //SELECT WALKOVER
    AWTFileDialog fd = new AWTFileDialog();
    String fileSelected = fd.openFile("Select Walk Over", LibraryViewer.mainDirectory, "*.swf");

    if (fileSelected != null) {
        if (LibraryViewer.fileInLibrary(new File(fileSelected))) {
            ZoneSetWalkoverAction s = new ZoneSetWalkoverAction(data.getMap(), getSelected(), new File(fileSelected));
            data.performAction(s);

            updatePanel();
        } else {
            JOptionPane.showMessageDialog(new Frame(), "The selected file must be a file in the library");
        }
    }
}//GEN-LAST:event_jButton9ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JCheckBox jCheckBox2;
    private javax.swing.JCheckBox jCheckBox3;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JList jList1;
    private javax.swing.JList jList2;
    private javax.swing.JList jList3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JToggleButton jToggleButton1;
    private javax.swing.JToggleButton jToggleButton2;
    private javax.swing.JToggleButton jToggleButton3;
    private javax.swing.JToggleButton jToggleButton4;
    // End of variables declaration//GEN-END:variables

    public MapZone[] getSelected() {
        if (tool.getModeType() == ZoneTool.POLYGON) {
            ZoneToolPolygonMode mode = (ZoneToolPolygonMode) tool.getMode();
            return mode.getSelected();
        }
        return null;
    }

    public void updatePanel() {
        if (tool.getModeType() == ZoneTool.POLYGON) {
            ZoneToolPolygonMode mode = (ZoneToolPolygonMode) tool.getMode();
            MapZone[] selected = mode.getSelected();
            if (selected != null && selected.length > 0) {
                enablePanel();

                boolean collisionA = false;
                boolean collisionB = false;

                boolean losA = false;
                boolean losB = false;

                boolean bankA = false;
                boolean bankB = false;

                boolean townA = false;
                boolean townB = false;

                String music = null;
                String walkover = null;
                zoneName = null;

                ArrayList<File> ambient = new ArrayList<File>();
                ArrayList<File> ambientGrey = new ArrayList<File>();

                ArrayList<File> footstep = new ArrayList<File>();
                ArrayList<File> footstepGrey = new ArrayList<File>();

                ArrayList<SpawnData> spawn = new ArrayList<SpawnData>();
                ArrayList<SpawnData> spawnGrey = new ArrayList<SpawnData>();

                ambientModel = new javax.swing.DefaultListModel();
                footstepModel = new javax.swing.DefaultListModel();

                for (int i = 0; i < selected.length; i++) {
                    MapZone select = selected[i];
                    if (select != null) {
                        if (select.collision) {
                            collisionA = true;
                        } else {
                            collisionB = true;
                        }
                        if (select.los) {
                            losA = true;
                        } else {
                            losB = true;
                        }
                        if (select.skillBank) {
                            bankA = true;
                        } else {
                            bankB = true;
                        }
                        if (select.town) {
                            townA = true;
                        } else {
                            townB = true;
                        }

                        String zName = null;
                        if (select.name != null) {
                            zName = select.name;
                            if (zoneName == null) {
                                zoneName = zName;
                            }
                        }
                        if (zoneName != null && !zoneName.equals(zName)) {
                            zoneName = "[Multiple Zone Names]";
                        }

                        String musicName = "Not Selected";
                        if (select.music != null) {
                            musicName = select.music.getName();
                        }
                        if (music == null) {
                            music = musicName;
                        }
                        if (music != null && !music.equals(musicName)) {
                            music = "[Multiple Files]";
                        }
                        
                        String walkoverFile = "Not Set";
                        if (select.walkover != null) {
                            walkoverFile = select.walkover.getName();
                            if(walkoverFile != null){
                                if (walkoverFile.length() > 10) {
                                   walkoverFile = walkoverFile.substring(walkoverFile.length() - 10);
                                }
                            }
                        }
                        if (walkover == null) {
                            walkover = walkoverFile;
                        }
                        if (walkover != null && !walkover.equals(walkoverFile)) {
                            walkover = "Multiple";
                        }

                        ArrayList<File> ambientSounds = select.ambient;

                        if (ambientSounds == null) {
                            ambientSounds = new ArrayList<File>();
                        }
                        if (i == 0) {
                            ambient = (ArrayList<File>) ambientSounds.clone();
                        } else {
                            //FIND ALL MATCHES AND DIFFERENCES
                            setAsDifference(ambientGrey, ambient, ambientSounds);
                        }

                        ArrayList<File> footstepSounds = select.footstep;

                        if (footstepSounds == null) {
                            footstepSounds = new ArrayList<File>();
                        }

                        if (i == 0) {
                            footstep = (ArrayList<File>) footstepSounds.clone();
                        } else {
                            //FIND ALL MATCHES AND DIFFERENCES
                            setAsDifference(footstepGrey, footstep, footstepSounds);
                        }

                        ArrayList<SpawnData> spawnList = select.spawn;

                        if (spawnList == null) {
                            spawnList = new ArrayList<SpawnData>();
                        }

                        if (i == 0) {
                            spawn = (ArrayList<SpawnData>) spawnList.clone();
                        } else {
                            //FIND ALL MATCHES AND DIFFERENCES
                            setAsDifference(spawnGrey, spawn, spawnList);
                        }
                    }
                }

                setCheck(jCheckBox1, collisionA, collisionB);
                setCheck(jCheckBox2, losA, losB);
                setCheck(jCheckBox3, bankA, bankB);
                setCheck(jCheckBox4, townA, townB);

                if (music == null) {
                    music = "Not Selected";
                }
                
                if (walkover == null) {
                    walkover = "Not Set";
                }
                
                jButton9.setText(walkover);
                
                jLabel4.setText(music);

                if (zoneName == null) {
                    jLabel2.setText("Zone Name");
                } else {
                    jLabel2.setText(zoneName);
                }

                //AMBIENT
                for (File f : ambient) {
                    ambientModel.addElement(f);
                }
                for (File f : ambientGrey) {
                    ambientModel.addElement(f);
                }

                jList1.setCellRenderer(new GreyListModel(ambient.size()));
                jList1.setModel(ambientModel);
                //FOOTSTEP
                for (File f : footstep) {
                    footstepModel.addElement(f);
                }
                for (File f : footstepGrey) {
                    footstepModel.addElement(f);
                }

                jList2.setCellRenderer(new GreyListModel(footstep.size()));
                jList2.setModel(footstepModel);

                //SPAWN DATA
                for (SpawnData f : spawn) {
                    spawnModel.addElement(f);
                }
                for (SpawnData f : spawnGrey) {
                    spawnModel.addElement(f);
                }

                jList3.setCellRenderer(new GreyListModel(spawn.size()));
                jList3.setModel(spawnModel);

                return;
            }
        }
        disablePanel();
    }

    private void setAsDifference(ArrayList difference, ArrayList matching, ArrayList files) {
        //ArrayList<File> list = (ArrayList<File>) original.clone();
        if (matching.isEmpty()) {
            for (Object f : files) {
                if (!difference.contains(f)) {
                    difference.add(f);
                }
            }
            return;
        }
        for (int i = 0; i < matching.size(); i++) {
            Object f = matching.get(i);
            if (!files.contains(f)) {
                matching.remove(f);
                i--;
                if (!difference.contains(f)) {
                    difference.add(f);
                }
            }
        }
        for (int i = 0; i < files.size(); i++) {
            Object f = files.get(i);
            if (!matching.contains(f)) {
                matching.remove(f);
                if (!difference.contains(f)) {
                    difference.add(f);
                }
            }
        }
    }

    private void setCheck(JCheckBox checkbox, boolean enabled, boolean disabled) {
        TristateCheckBox triBox = (TristateCheckBox) checkbox;
        if (enabled && disabled) {
            triBox.setState(TristateCheckBox.DONT_CARE);
        } else {
            if (enabled) {
                triBox.setState(TristateCheckBox.SELECTED);
            } else {
                triBox.setState(TristateCheckBox.NOT_SELECTED);
            }
        }
    }

    private void enablePanel() {
        setPanel(true);
    }

    private void disablePanel() {
        setPanel(false);
    }

    private void setPanel(boolean enable) {
        jCheckBox1.setEnabled(enable);
        jCheckBox2.setEnabled(enable);
        jCheckBox3.setEnabled(enable);
        jCheckBox4.setEnabled(enable);
        jButton1.setEnabled(enable);
        jButton2.setEnabled(enable);
        jButton3.setEnabled(enable);
        jButton4.setEnabled(enable);
        jButton5.setEnabled(enable);
        jButton6.setEnabled(enable);
        jButton7.setEnabled(enable);
        jButton8.setEnabled(enable);
        jButton9.setEnabled(enable);
        jList1.setEnabled(enable);
        jList2.setEnabled(enable);
        jList3.setEnabled(enable);
        jLabel2.setEnabled(enable);
        jLabel4.setEnabled(enable);

        setCheck(jCheckBox1, false, false);
        setCheck(jCheckBox2, false, false);
        setCheck(jCheckBox3, false, false);
        setCheck(jCheckBox4, false, false);
        jButton9.setText("Not Set");
        jLabel2.setText("Zone Name");
        jLabel4.setText("Not Selected");
        ambientModel = new javax.swing.DefaultListModel();
        footstepModel = new javax.swing.DefaultListModel();
        spawnModel = new javax.swing.DefaultListModel();
        jList1.setModel(ambientModel);
        jList2.setModel(footstepModel);
        jList3.setModel(spawnModel);
    }

    private void registerCheckBox(JCheckBox checkbox, ActionListener listener) {
        TristateCheckBox triBox = (TristateCheckBox) checkbox;
        triBox.addActionListener(listener);
        triBox.setState(TristateCheckBox.NOT_SELECTED);
    }
}

class GreyListModel extends DefaultListCellRenderer {

    private int greyLimit = 0;

    public GreyListModel(int greyLimit) {
        super();
        this.greyLimit = greyLimit;
    }

    @Override
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        String newValue = value.toString();
        if (newValue.length() > 20) {
            newValue = ".." + newValue.substring(newValue.length() - 20);
        }
        Component comp = super.getListCellRendererComponent(list, newValue, index, isSelected, cellHasFocus);
        comp.setEnabled(index < greyLimit);
        return comp;
    }
}
