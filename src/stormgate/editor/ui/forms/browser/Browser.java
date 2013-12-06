/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Browser.java
 *
 * Created on Feb 19, 2011, 10:08:08 PM
 */
package stormgate.editor.ui.forms.browser;

import editor.style.LookAndFeel;
import java.util.Timer;
import java.util.TimerTask;
import stormgate.editor.data.EditorData;

/**
 *
 * @author David
 */
public class Browser extends javax.swing.JPanel
{

	Timer timer;

	/** Creates new form Browser */
	public Browser()
	{
		initComponents();
		libraryViewer1.setThumbnail(thumbnails1);
		
		timer = new Timer();
                timer.schedule(new CheckFilesTask(libraryViewer1), 5000, 5000);
	}

	public void resetLibrary()
	{
		libraryViewer1.resetLibrary();
	}

	public void refreshLibrary()
	{
		//libraryViewer1.checkFolders();
                libraryViewer1.refresh();;
	}

	public BrowserControl getController()
	{
		return browserControl1;
	}

	public void setInfo(EditorData data)
	{
		thumbnails1.setData(browserControl1, data);
		browserControl1.setManager(data.getManager());
		//timer.schedule(new CheckFilesTask(libraryViewer1), 30000, 30000);
		//timer.schedule(new CheckFilesTask(libraryViewer1), 100, 100);
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jSplitPane1 = new javax.swing.JSplitPane();
        LookAndFeel.flattenSplitPane(jSplitPane1);
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane1.getVerticalScrollBar().setUnitIncrement(16);
        thumbnails1 = new stormgate.editor.ui.forms.browser.Thumbnails();
        libraryViewer1 = new stormgate.editor.ui.forms.browser.LibraryViewer();
        browserControl1 = new stormgate.editor.ui.forms.browser.BrowserControl();
        browserControl1.setThumbnails(thumbnails1, libraryViewer1);

        setMinimumSize(new java.awt.Dimension(200, 200));
        setPreferredSize(new java.awt.Dimension(741, 200));

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(270);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(228, 100));
        jSplitPane1.setPreferredSize(new java.awt.Dimension(919, 100));
        jSplitPane1.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentResized(java.awt.event.ComponentEvent evt) {
                jSplitPane1ComponentResized(evt);
            }
        });

        jScrollPane1.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane1.setPreferredSize(new java.awt.Dimension(402, 100));
        jScrollPane1.setViewportView(thumbnails1);

        jSplitPane1.setRightComponent(jScrollPane1);
        jSplitPane1.setLeftComponent(libraryViewer1);

        browserControl1.setMinimumSize(new java.awt.Dimension(27, 10));
        browserControl1.setPreferredSize(null);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 708, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(browserControl1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
            .addComponent(browserControl1, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

	private void jSplitPane1ComponentResized(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jSplitPane1ComponentResized
		thumbnails1.realign();
	}//GEN-LAST:event_jSplitPane1ComponentResized
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private stormgate.editor.ui.forms.browser.BrowserControl browserControl1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSplitPane jSplitPane1;
    private stormgate.editor.ui.forms.browser.LibraryViewer libraryViewer1;
    private stormgate.editor.ui.forms.browser.Thumbnails thumbnails1;
    // End of variables declaration//GEN-END:variables

	class CheckFilesTask extends TimerTask
	{

		private LibraryViewer lib;

		public CheckFilesTask(LibraryViewer lib)
		{
			this.lib = lib;
		}

		public void run()
		{
			try {
				lib.refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
