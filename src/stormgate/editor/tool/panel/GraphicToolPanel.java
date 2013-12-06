/*
 * GraphicToolPanel.java
 *
 * Created on Apr 23, 2011, 7:01:14 PM
 */
package stormgate.editor.tool.panel;

import stormgate.action.graphic.GraphicFlipAction;
import stormgate.action.graphic.GraphicHideAction;
import stormgate.action.graphic.GraphicLevelChangeAction;
import stormgate.action.graphic.GraphicReverseModeAction;
import stormgate.action.graphic.GraphicSendToLevelAction;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.GraphicsTool;
import stormgate.image.LibraryResource;

/**
 *
 * @author David
 */
public class GraphicToolPanel extends ToolPanel
{

    private GraphicsTool tool;
    private EditorData data;
    private boolean update = false;

    /** Creates new form GraphicToolPanel */
    public GraphicToolPanel()
    {
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        graphicViewer1 = new stormgate.editor.tool.panel.GraphicViewer();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator1 = new javax.swing.JSeparator();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jComboBox2 = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jCheckBox1 = new javax.swing.JCheckBox();

        setPreferredSize(new java.awt.Dimension(160, 325));

        graphicViewer1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        graphicViewer1.setMaximumSize(new java.awt.Dimension(154, 154));
        graphicViewer1.setMinimumSize(new java.awt.Dimension(154, 154));

        javax.swing.GroupLayout graphicViewer1Layout = new javax.swing.GroupLayout(graphicViewer1);
        graphicViewer1.setLayout(graphicViewer1Layout);
        graphicViewer1Layout.setHorizontalGroup(
            graphicViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 142, Short.MAX_VALUE)
        );
        graphicViewer1Layout.setVerticalGroup(
            graphicViewer1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 152, Short.MAX_VALUE)
        );

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ground Layer", "Entity Layer", "Sky Layer" }));
        jComboBox1.setSelectedIndex(1);
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox1ActionPerformed(evt);
            }
        });

        jButton1.setText("hide");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("unhide all");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jComboBox2.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Ground Layer", "Entity Layer", "Sky Layer" }));
        jComboBox2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jComboBox2ActionPerformed(evt);
            }
        });

        jLabel1.setText("Send to ");

        jButton3.setText("Flip Selected");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jCheckBox1.setText("Reverse Mode");
        jCheckBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton3, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(graphicViewer1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 144, Short.MAX_VALUE)
                    .addComponent(jCheckBox1)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jComboBox2, 0, 96, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 144, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, 53, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 85, Short.MAX_VALUE))
                    .addComponent(jComboBox1, javax.swing.GroupLayout.Alignment.TRAILING, 0, 144, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(graphicViewer1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jCheckBox1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
            //Hide
            GraphicHideAction h = new GraphicHideAction(data.getMap(), tool, true);
            data.performAction(h);
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
            //Unhide All
            GraphicHideAction h = new GraphicHideAction(data.getMap(), tool, false);
            data.performAction(h);
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jComboBox1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jComboBox1ActionPerformed
	{//GEN-HEADEREND:event_jComboBox1ActionPerformed
            //Change Level
            if (!update) {
                int level = jComboBox1.getSelectedIndex();
                GraphicLevelChangeAction l = new GraphicLevelChangeAction(this, tool, level);
                data.performAction(l);
            }
	}//GEN-LAST:event_jComboBox1ActionPerformed

	private void jComboBox2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jComboBox2ActionPerformed
	{//GEN-HEADEREND:event_jComboBox2ActionPerformed
            //Send to level
            if (!update) {
                int level = jComboBox2.getSelectedIndex();
                GraphicSendToLevelAction l = new GraphicSendToLevelAction(data.getMap(), this, tool, level);
                data.performAction(l);
            }
	}//GEN-LAST:event_jComboBox2ActionPerformed

private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
    //Flip
    if (!update) {
        GraphicFlipAction f = new GraphicFlipAction(data.getMap(), tool);
        data.performAction(f);
    }
}//GEN-LAST:event_jButton3ActionPerformed

private void jCheckBox1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox1ActionPerformed
    //Change Level
    if (!update) {
        boolean reverse = jCheckBox1.isSelected();
        GraphicReverseModeAction r = new GraphicReverseModeAction(this, tool, reverse);
        data.performAction(r);
    }
}//GEN-LAST:event_jCheckBox1ActionPerformed

    public void setGraphic(LibraryResource resource)
    {
        graphicViewer1.setResource(resource);
        repaint();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private stormgate.editor.tool.panel.GraphicViewer graphicViewer1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JCheckBox jCheckBox1;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JComboBox jComboBox2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables

    public void setTool(GraphicsTool tool)
    {
        this.tool = tool;
    }

    public void setData(EditorData data)
    {
        this.data = data;
    }

    public void updatePanel()
    {
        //Undo fix
        update = true;
        jComboBox1.setSelectedIndex(tool.getLevel());
        jComboBox2.setSelectedIndex(tool.getLevel());
        update = false;
    }
}
