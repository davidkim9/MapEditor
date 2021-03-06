/*
 * MapProperty.java
 *
 * Created on Sep 20, 2011, 3:43:12 PM
 */
package stormgate.editor.ui.forms.dialog;

import java.io.File;
import stormgate.editor.data.EditorData;
import stormgate.editor.ui.forms.browser.LibraryViewer;
import stormgate.editor.ui.forms.fileDialog.FolderChooser;

/**
 *
 * @author David
 */
public class LibrarySelect extends javax.swing.JDialog
{

	File libraryDirectory;

	/** Creates new form MapProperty */
	public LibrarySelect(java.awt.Frame parent, boolean modal)
	{
		super(parent, modal);
		initComponents();

	}

	@Override
	public void setVisible(boolean v)
	{
		libraryDirectory = new File(LibraryViewer.mainDirectory);
		jLabel7.setText(reformatText(libraryDirectory.toString()));

		super.setVisible(v);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jButton2.setText("Browse");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel6.setText("Library Directory");

        jLabel7.setText("library");

        jButton1.setText("Cancel");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton4.setText("Select");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel6)
                .addGap(18, 18, 18)
                .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15)
                .addComponent(jButton2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(174, Short.MAX_VALUE)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton1)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton4))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
	{//GEN-HEADEREND:event_jButton4ActionPerformed
		//SAVE
		LibraryViewer.mainDirectory = libraryDirectory.toString();
		this.setVisible(false);
		this.dispose();
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		//CANCEL
		this.setVisible(false);
		this.dispose();
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
		//Choose Library
		File directory = FolderChooser.showFolderDialog(libraryDirectory);
		if (directory != null) {
			File path = new File(".");
			try {
				path = path.getCanonicalFile();
			} catch (Exception e) {
			}

			libraryDirectory = new File(unfilter(directory.getAbsolutePath(), path.getAbsolutePath() + "\\"));

			//libraryDirectory = directory;
			jLabel7.setText(reformatText(libraryDirectory.toString()));
		}
	}//GEN-LAST:event_jButton2ActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    // End of variables declaration//GEN-END:variables

	private String unfilter(String str, String path)
	{
		if (str.length() > 0) {
			path = path.replaceAll("(\\\\)", "\\\\\\\\\\\\\\\\");
			str = str.replaceAll("(\\\\)", "\\\\\\\\");
			str = str.replaceAll(path, "");
			return str.replaceAll("(\\\\\\\\)", "\\\\");
		}
		return str;
	}

	public String reformatText(String str)
	{
		if (str.length() > 20) {
			str = ".." + str.substring(str.length() - 20);
		}
		return str;
	}
}
