/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * ImageBlenderDialog.java
 *
 * Created on Apr 26, 2011, 3:59:40 PM
 */
package stormgate.editor.ui.forms.dialog;

import java.awt.Frame;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import stormgate.editor.graphics.blend.ImageBlendType;
import stormgate.editor.graphics.blend.ImageBlender;
import stormgate.editor.graphics.blend.ImageCutCornerBlend;
import stormgate.editor.graphics.blend.ImageCutHalfCornerBlend;
import stormgate.editor.graphics.blend.ImageCutHalfHorizontalBlend;
import stormgate.editor.graphics.blend.ImageCutHalfVerticalBlend;
import stormgate.editor.graphics.blend.ImageCutSideBlend;
import stormgate.editor.graphics.blend.ImageLinearBlend;
import stormgate.editor.graphics.blend.ImageRadialBlend;
import stormgate.editor.graphics.blend.ImageStraightBlend;
import stormgate.editor.ui.forms.fileDialog.AWTFileDialog;

/**
 *
 * @author David
 */
public class ImageBlenderDialog extends javax.swing.JDialog
{
	private String imageA;
	private String imageB;
	private String output;

	/** Creates new form ImageBlenderDialog */
	public ImageBlenderDialog()
	{
		super(new Frame(), true);

		initComponents();

		//blendImages("a.png", "b.png", "c");
	}

	private void blendImages(String fileA, String fileB, String output){
		BufferedImage a = getBufferedImage(fileA);
		BufferedImage b = getBufferedImage(fileB);
		if(a != null && b != null){
			//Rounded
			for(int side = 0; side < 4; side++){
				ImageBlendType radial = new ImageRadialBlend(side);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, radial);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, radial);
			}
			//Straight
			for(int side = 4; side < 8; side++){
				ImageBlendType straight = new ImageStraightBlend(side - 4);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, straight);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, straight);
			}
			//Edges
			for(int side = 8; side < 10; side++){
				ImageBlendType linear = new ImageLinearBlend(side%2==0);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, linear);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, linear);
			}
                        //Transparent
                        //Corner
			for(int side = 10; side < 12; side++){
				ImageBlendType corner = new ImageCutCornerBlend(side%2==0);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, corner);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, corner);
			}
                        //Sides
			for(int side = 12; side < 14; side++){
				ImageBlendType sideBlend = new ImageCutSideBlend(side%2==0);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, sideBlend);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, sideBlend);
			}
                        //Half Horizontal
			for(int side = 14; side < 18; side++){
				ImageBlendType corner = new ImageCutHalfVerticalBlend(side - 14);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, corner);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, corner);
			}
                        //Half Vertical
			for(int side = 18; side < 22; side++){
				ImageBlendType corner = new ImageCutHalfHorizontalBlend(side - 18);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, corner);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, corner);
			}
                        //Half Corner
			for(int side = 22; side < 26; side++){
				ImageBlendType corner = new ImageCutHalfCornerBlend(side - 22);
				makeImage(new File(output + "_1_" +side+ ".png"), a, b, corner);
				makeImage(new File(output + "_2_" +side+ ".png"), b, a, corner);
			}
		}else{
			System.out.println("Images not loaded correctly");
		}
	}

	private void makeImage(File file, BufferedImage a, BufferedImage b, ImageBlendType blend){
		BufferedImage image = ImageBlender.blend(a, b, blend);
		try {
			ImageIO.write(image, "png", file);
		} catch (IOException ex) {

		}
	}

	private Image loadImage(String path)
	{
		Image img = null;

		try {
			img = ImageIO.read(new File(path));
		} catch (IOException e) {
			System.out.println("Cant open image: " + path);
		}

		return img;
	}

	private BufferedImage getBufferedImage(String url)
	{
		Image img = loadImage(url);
		if (img != null) {
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			GraphicsDevice gs = ge.getDefaultScreenDevice();
			GraphicsConfiguration gc = gs.getDefaultConfiguration();
			//Convert to JPG
			BufferedImage bufferedImage = gc.createCompatibleImage(img.getWidth(null), img.getHeight(null), Transparency.TRANSLUCENT);
			Graphics2D g2 = bufferedImage.createGraphics();
			//g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC));
			//g2.setBackground(new Color(255,255,255,150));

			g2.drawImage(img, null, null);

			return bufferedImage;
		}
		return null;
	}

	/** This method is called from within the constructor to
	 * initialize the form.
	 * WARNING: Do NOT modify this code. The content of this method is
	 * always regenerated by the Form Editor.
	 */
	@SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jButton5 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Image Blender");

        jButton1.setText("Select");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jLabel1.setText("Choose File");

        jLabel2.setText("Image A");

        jLabel3.setText("Choose File");

        jButton2.setText("Select");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jLabel4.setText("Image B");

        jButton3.setText("Cancel");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("Blend");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jLabel6.setText("Choose File");

        jLabel7.setText("Output Folder");

        jButton5.setText("Select");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                    .addComponent(jLabel4)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 333, Short.MAX_VALUE)
                    .addComponent(jLabel7)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 262, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton3)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton2)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton5)
                    .addComponent(jLabel6))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 50, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton3)
                    .addComponent(jButton4))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
	{//GEN-HEADEREND:event_jButton1ActionPerformed
		AWTFileDialog fd = new AWTFileDialog();
		imageA = fd.openFile("Open", ".", "*.png");
		jLabel1.setText(imageA);
	}//GEN-LAST:event_jButton1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton2ActionPerformed
	{//GEN-HEADEREND:event_jButton2ActionPerformed
		AWTFileDialog fd = new AWTFileDialog();
		imageB = fd.openFile("Open", ".", "*.png");
		jLabel3.setText(imageB);
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton5ActionPerformed
	{//GEN-HEADEREND:event_jButton5ActionPerformed
		AWTFileDialog fd = new AWTFileDialog();
		output = fd.saveFile("Save As", ".", "*.png");
		//Check file extension
		int strLength = output.length();
		String endString = output.substring(strLength - 4, strLength).toLowerCase();
		if(endString.equals(".png")){
			output = output.substring(0, strLength - 4);
		}
		jLabel6.setText(output + "_*_*.png");
	}//GEN-LAST:event_jButton5ActionPerformed

	private void jButton3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton3ActionPerformed
	{//GEN-HEADEREND:event_jButton3ActionPerformed
		dispose();
	}//GEN-LAST:event_jButton3ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
	{//GEN-HEADEREND:event_jButton4ActionPerformed
		if(imageA != null && imageB != null && output != null){
			try{
				blendImages(imageA, imageB, output);

				dispose();
			}catch(Exception e){
				JOptionPane.showMessageDialog(this, "Error: \n" + e.getMessage());
			}
		}else{
			JOptionPane.showMessageDialog(this, "You must select the input/output files");
		}
	}//GEN-LAST:event_jButton4ActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JSeparator jSeparator1;
    // End of variables declaration//GEN-END:variables
}
