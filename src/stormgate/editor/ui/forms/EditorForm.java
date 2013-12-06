/*
 * EditorForm.java
 *
 * Created on Feb 19, 2011, 1:40:05 PM
 */
package stormgate.editor.ui.forms;

import editor.Version;
import editor.style.LookAndFeel;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JViewport;
import stormgate.common.Common;
import stormgate.compile.Compile;
import stormgate.data.Tile;
import stormgate.editor.controls.CursorHandler;
import stormgate.editor.controls.GlobalKeyHandler;
import stormgate.editor.controls.MouseHandler;
import stormgate.editor.controls.ToolKeyHandler;
import stormgate.editor.data.EditorData;
import stormgate.editor.tool.ToolInterface;
import stormgate.editor.tool.ZoneTool;
import stormgate.editor.ui.forms.dialog.ImageBlenderDialog;
import stormgate.editor.ui.forms.dialog.MapProperty;
import stormgate.editor.ui.forms.dialog.NewMapDialog;
import stormgate.editor.ui.forms.fileDialog.AWTFileDialog;
import stormgate.filter.Filter;
import stormgate.geom.MapPoint;
import stormgate.io.ini.IniFile;
import stormgate.io.xml.XMLProject;
import stormgate.map.Map;

/**
 *
 * @author David
 */
public class EditorForm extends javax.swing.JFrame
{

	private EditorData d;
	private GlobalKeyHandler keyHandler;
	private MouseHandler mouseHandler;
	private ToolKeyHandler toolKeyHandler;
	private Filter filter;
	MapProperty propertyDialog;
	private boolean alertMemory = false;

	/** Creates new form EditorForm */
	public EditorForm()
	{
		loadConfig();
		d = new EditorData(this);

		keyHandler = new GlobalKeyHandler(d);
		mouseHandler = new MouseHandler(d);
		toolKeyHandler = new ToolKeyHandler();

		d.setMouseHandler(mouseHandler);
		d.setKeyHandler(toolKeyHandler);

		try {
			java.net.URL iconURL = getClass().getResource("/stormgate/editor/ui/forms/icons/icon.png");
			if (iconURL != null) {
				java.awt.image.BufferedImage iconImage = javax.imageio.ImageIO.read(iconURL);
				if (iconImage != null) {
					setIconImage(iconImage);
				}
			}
		} catch (Exception e) {
		}

		initComponents();

		CursorHandler cursorHandler = new CursorHandler(graphic);
		d.cursors = cursorHandler;

		d.setGraphic(graphic);

		d.newMap();

		browser2.setInfo(d);

		filter = new Filter(this);
		d.setFilter(filter);
		graphic.setInformation(d.getRenderData());
		graphic1.setInformation(d.getMiniRenderData());

		graphic.addMouseListener(mouseHandler);
		graphic.addMouseMotionListener(mouseHandler);
		graphic.addMouseWheelListener(mouseHandler);
		this.addKeyListener(toolKeyHandler);

		toolbox1.setData(d);

		addGlobalKey();

		propertyDialog = new MapProperty(this, true);
		propertyDialog.setData(d);

		addWindowListener(new CloseCheck(this));
	}
        
	public static void loadConfig() {
		System.out.println("Loading Settings");
		File f = new File("config.ini");
		if (f.exists()) {
			IniFile iniReader = new IniFile();
			iniReader.readFile(f);
			
			String defaultTile = iniReader.getValue("tile");
			if( !defaultTile.isEmpty() ){
				Tile.defaultTile = defaultTile;
			}
		}
	}
        
	public void updateMemory()
	{
		Long used = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		Long max = Runtime.getRuntime().maxMemory();
		//Long total = Runtime.getRuntime().totalMemory();
		int ratio = (int) (used * 100.0 / max);
		if (ratio > 70) {
			if (!alertMemory) {
				//First time warning
				System.out.println("MEMORY USAGE ALERT!");
				JOptionPane.showMessageDialog(this, "Warning! Memory usage of over 70% has been detected. It is a good idea to save the map as it will reallocate the memory");
			}
			alertMemory = true;
		}
		String memoryData = "  Memory Usage: " + ratio + "% - " + used + "/" + max;

		if (alertMemory) {
			memoryData += " MEMORY USAGE OF OVER 70% DETECTED! SAVE MAP IF POSSIBLE";
		}

		jLabel4.setText(memoryData);
	}

	public void resetLibrary()
	{
		browser2.resetLibrary();
	}

	public void refreshLibrary()
	{
		browser2.refreshLibrary();
	}

	public void setPanel(Component panel)
	{
		JViewport viewport = jScrollPane1.getViewport();
		viewport.removeAll();

		if (panel != null) {
			viewport.add(panel);
		}

		jScrollPane1.revalidate();
	}

	private void addGlobalKey()
	{
		addGlobalKey(this);
	}

	private void addGlobalKey(Container current)
	{
		Component[] children = current.getComponents();
		for (int i = 0; i < children.length; i++) {
			Component child = children[i];

			KeyListener[] listeners = child.getKeyListeners();

			for (int j = 0; j < listeners.length; j++) {
				KeyListener keyListener = listeners[j];
				child.removeKeyListener(keyListener);
			}

			child.addKeyListener(new java.awt.event.KeyAdapter()
			{

				@Override
				public void keyPressed(java.awt.event.KeyEvent evt)
				{
					formKeyPressed(evt);
				}

				@Override
				public void keyReleased(java.awt.event.KeyEvent evt)
				{
					formKeyReleased(evt);
				}
			});

			if (child instanceof Container) {
				addGlobalKey((Container) child);
			}
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
        buttonGroup2 = new javax.swing.ButtonGroup();
        jToolBar1 = new javax.swing.JToolBar();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jToolBar2 = new javax.swing.JToolBar();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jSplitPane1 = new javax.swing.JSplitPane();
        LookAndFeel.flattenSplitPane(jSplitPane1);
        graphic = new stormgate.editor.ui.forms.graphic.Graphic(d.getLevels());
        browser2 = new stormgate.editor.ui.forms.browser.Browser();
        jScrollPane1 = new javax.swing.JScrollPane();
        graphic1 = new stormgate.editor.ui.forms.graphic.Graphic(d.getLevels());
        toolbox1 = new stormgate.editor.ui.forms.tool.Toolbox();
        jToolBar3 = new javax.swing.JToolBar();
        jLabel2 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jTextField2 = new javax.swing.JTextField();
        jLabel1 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox();
        jSeparator4 = new javax.swing.JToolBar.Separator();
        jButton7 = new javax.swing.JButton();
        jToolBar4 = new javax.swing.JToolBar();
        jCheckBox4 = new javax.swing.JCheckBox();
        jCheckBox5 = new javax.swing.JCheckBox();
        jButton6 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuNew = new javax.swing.JMenuItem();
        menuOpen = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        menuSave = new javax.swing.JMenuItem();
        menuSaveAs = new javax.swing.JMenuItem();
        jSeparator12 = new javax.swing.JPopupMenu.Separator();
        jMenuItem18 = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        menuExport = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        menuExit = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jSeparator5 = new javax.swing.JPopupMenu.Separator();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenuItem4 = new javax.swing.JMenuItem();
        jMenuItem5 = new javax.swing.JMenuItem();
        jMenu7 = new javax.swing.JMenu();
        jCheckBoxMenuItem1 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem2 = new javax.swing.JCheckBoxMenuItem();
        jSeparator11 = new javax.swing.JPopupMenu.Separator();
        jCheckBoxMenuItem5 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem3 = new javax.swing.JCheckBoxMenuItem();
        jCheckBoxMenuItem4 = new javax.swing.JCheckBoxMenuItem();
        jMenu3 = new javax.swing.JMenu();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem3 = new javax.swing.JRadioButtonMenuItem();
        jMenu4 = new javax.swing.JMenu();
        jRadioButtonMenuItem10 = new javax.swing.JRadioButtonMenuItem();
        jSeparator7 = new javax.swing.JPopupMenu.Separator();
        jRadioButtonMenuItem7 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem8 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem9 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem5 = new javax.swing.JRadioButtonMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        jMenuItem14 = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        jMenuItem6 = new javax.swing.JMenuItem();
        jSeparator10 = new javax.swing.JPopupMenu.Separator();
        jMenuItem7 = new javax.swing.JMenuItem();
        jMenuItem8 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jMenuItem16 = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        jMenuItem10 = new javax.swing.JMenuItem();
        jMenuItem11 = new javax.swing.JMenuItem();
        jMenuItem17 = new javax.swing.JMenuItem();
        jMenuItem12 = new javax.swing.JMenuItem();
        jSeparator9 = new javax.swing.JPopupMenu.Separator();
        jMenuItem13 = new javax.swing.JMenuItem();
        jMenu6 = new javax.swing.JMenu();
        jMenuItem15 = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Apocalypse Online Map Editor");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setName("editorFrame"); // NOI18N
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                formKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                formKeyReleased(evt);
            }
        });

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/new.png"))); // NOI18N
        jButton1.setToolTipText("new");
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton1);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/open.png"))); // NOI18N
        jButton3.setToolTipText("open");
        jButton3.setFocusable(false);
        jButton3.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton3.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton3);

        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/save.png"))); // NOI18N
        jButton4.setToolTipText("save");
        jButton4.setFocusable(false);
        jButton4.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton4);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/arrow_left.png"))); // NOI18N
        jButton2.setFocusable(false);
        jButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton2);

        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/arrow_right.png"))); // NOI18N
        jButton5.setToolTipText("redo");
        jButton5.setFocusable(false);
        jButton5.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton5.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jToolBar1.add(jButton5);

        jToolBar2.setFloatable(false);
        jToolBar2.setRollover(true);

        jLabel3.setText("Compiled on " + Version.date);
        jToolBar2.add(jLabel3);

        jLabel4.setText("jLabel4");
        jToolBar2.add(jLabel4);

        jPanel1.setBorder(javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2));
        jPanel1.setAlignmentX(0.0F);
        jPanel1.setAlignmentY(0.0F);

        jSplitPane1.setBorder(null);
        jSplitPane1.setDividerLocation(285);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setResizeWeight(1.0);
        jSplitPane1.setMinimumSize(new java.awt.Dimension(100, 100));

        graphic.setDoubleBuffered(true);
        graphic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                focusWindow(evt);
            }
        });

        javax.swing.GroupLayout graphicLayout = new javax.swing.GroupLayout(graphic);
        graphic.setLayout(graphicLayout);
        graphicLayout.setHorizontalGroup(
            graphicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 804, Short.MAX_VALUE)
        );
        graphicLayout.setVerticalGroup(
            graphicLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 285, Short.MAX_VALUE)
        );

        jSplitPane1.setLeftComponent(graphic);

        browser2.setMinimumSize(new java.awt.Dimension(200, 10));
        browser2.setPreferredSize(new java.awt.Dimension(741, 202));
        jSplitPane1.setRightComponent(browser2);

        graphic1.setPreferredSize(new java.awt.Dimension(180, 180));

        javax.swing.GroupLayout graphic1Layout = new javax.swing.GroupLayout(graphic1);
        graphic1.setLayout(graphic1Layout);
        graphic1Layout.setHorizontalGroup(
            graphic1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 180, Short.MAX_VALUE)
        );
        graphic1Layout.setVerticalGroup(
            graphic1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 183, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 180, Short.MAX_VALUE)
                    .addComponent(graphic1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(toolbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 804, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(graphic1, javax.swing.GroupLayout.PREFERRED_SIZE, 183, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(toolbox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 268, Short.MAX_VALUE))
        );

        jToolBar3.setFloatable(false);
        jToolBar3.setRollover(true);

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/icon.png"))); // NOI18N
        jLabel2.setToolTipText("Camera Position");
        jLabel2.setMaximumSize(new java.awt.Dimension(30, 16));
        jLabel2.setMinimumSize(new java.awt.Dimension(30, 16));
        jToolBar3.add(jLabel2);

        jTextField1.setText("0");
        jTextField1.setToolTipText("Camera X Position");
        jTextField1.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jTextField1.setMaximumSize(new java.awt.Dimension(70, 20));
        jTextField1.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField1.setPreferredSize(new java.awt.Dimension(50, 20));
        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });
        jToolBar3.add(jTextField1);

        jTextField2.setText("0");
        jTextField2.setToolTipText("Camera Y Position");
        jTextField2.setMaximumSize(new java.awt.Dimension(70, 20));
        jTextField2.setMinimumSize(new java.awt.Dimension(70, 20));
        jTextField2.setPreferredSize(new java.awt.Dimension(50, 20));
        jTextField2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField2ActionPerformed(evt);
            }
        });
        jToolBar3.add(jTextField2);

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/magnifier.png"))); // NOI18N
        jLabel1.setToolTipText("Zoom");
        jLabel1.setMaximumSize(new java.awt.Dimension(20, 16));
        jLabel1.setMinimumSize(new java.awt.Dimension(20, 16));
        jToolBar3.add(jLabel1);

        jComboBox1.setEditable(true);
        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "5%", "10%", "50%", "100%", "200%", "400%", "800%" }));
        jComboBox1.setSelectedIndex(3);
        jComboBox1.setToolTipText("Zoom");
        jComboBox1.setMaximumSize(new java.awt.Dimension(60, 18));
        jComboBox1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                zoomComboBoxActionPerformed(evt);
            }
        });
        jToolBar3.add(jComboBox1);
        jToolBar3.add(jSeparator4);

        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/flash.png"))); // NOI18N
        jButton7.setToolTipText("flash");
        jButton7.setFocusable(false);
        jButton7.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton7.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jToolBar3.add(jButton7);

        jToolBar4.setFloatable(false);
        jToolBar4.setRollover(true);

        jCheckBox4.setText("Grid");
        jCheckBox4.setFocusable(false);
        jCheckBox4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jCheckBox4.setMaximumSize(new java.awt.Dimension(60, 20));
        jCheckBox4.setMinimumSize(new java.awt.Dimension(60, 20));
        jCheckBox4.setPreferredSize(new java.awt.Dimension(60, 20));
        jCheckBox4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox4ActionPerformed(evt);
            }
        });
        jToolBar4.add(jCheckBox4);

        jCheckBox5.setText("Snap");
        jCheckBox5.setFocusable(false);
        jCheckBox5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jCheckBox5.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jCheckBox5.setMaximumSize(new java.awt.Dimension(60, 20));
        jCheckBox5.setMinimumSize(new java.awt.Dimension(60, 20));
        jCheckBox5.setPreferredSize(new java.awt.Dimension(60, 20));
        jCheckBox5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBox5ActionPerformed(evt);
            }
        });
        jToolBar4.add(jCheckBox5);

        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/paintbrush.png"))); // NOI18N
        jButton6.setToolTipText("Image Blender");
        jButton6.setFocusable(false);
        jButton6.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton6.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton6);

        jButton8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/map_edit.png"))); // NOI18N
        jButton8.setToolTipText("Map Properties");
        jButton8.setFocusable(false);
        jButton8.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton8.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton8);

        jButton9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/text_align_justify.png"))); // NOI18N
        jButton9.setToolTipText("Display Log");
        jButton9.setFocusable(false);
        jButton9.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton9.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });
        jToolBar4.add(jButton9);

        jMenu1.setText("File");

        menuNew.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        menuNew.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/new.png"))); // NOI18N
        menuNew.setText("New");
        menuNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuNewActionPerformed(evt);
            }
        });
        jMenu1.add(menuNew);

        menuOpen.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        menuOpen.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/open.png"))); // NOI18N
        menuOpen.setText("Open");
        menuOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuOpenActionPerformed(evt);
            }
        });
        jMenu1.add(menuOpen);
        jMenu1.add(jSeparator1);

        menuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        menuSave.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/save.png"))); // NOI18N
        menuSave.setText("Save");
        menuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveActionPerformed(evt);
            }
        });
        jMenu1.add(menuSave);

        menuSaveAs.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        menuSaveAs.setText("Save As...");
        menuSaveAs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuSaveAsActionPerformed(evt);
            }
        });
        jMenu1.add(menuSaveAs);
        jMenu1.add(jSeparator12);

        jMenuItem18.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/icon.png"))); // NOI18N
        jMenuItem18.setText("Map Properties");
        jMenuItem18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem18ActionPerformed(evt);
            }
        });
        jMenu1.add(jMenuItem18);
        jMenu1.add(jSeparator2);

        menuExport.setText("Export");
        menuExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExportActionPerformed(evt);
            }
        });
        jMenu1.add(menuExport);
        jMenu1.add(jSeparator3);

        menuExit.setText("Exit");
        menuExit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                menuExitActionPerformed(evt);
            }
        });
        jMenu1.add(menuExit);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        jMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Z, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/arrow_left.png"))); // NOI18N
        jMenuItem1.setText("Undo");
        jMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem1ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem1);

        jMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Y, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/arrow_right.png"))); // NOI18N
        jMenuItem2.setText("Redo");
        jMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem2ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem2);
        jMenu2.add(jSeparator5);

        jMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem3.setText("Cut");
        jMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem3ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem3);

        jMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem4.setText("Copy");
        jMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem4ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem4);

        jMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem5.setText("Paste");
        jMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem5ActionPerformed(evt);
            }
        });
        jMenu2.add(jMenuItem5);

        jMenuBar1.add(jMenu2);

        jMenu7.setText("View");

        jCheckBoxMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.CTRL_MASK));
        jCheckBoxMenuItem1.setText("Grid");
        jCheckBoxMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem1ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem1);

        jCheckBoxMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jCheckBoxMenuItem2.setText("Snap To Grid");
        jCheckBoxMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem2ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem2);
        jMenu7.add(jSeparator11);

        jCheckBoxMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        jCheckBoxMenuItem5.setText("Show Disabled Tiles");
        jCheckBoxMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem5ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem5);

        jCheckBoxMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_H, java.awt.event.InputEvent.CTRL_MASK));
        jCheckBoxMenuItem3.setText("Show Hidden");
        jCheckBoxMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem3ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem3);

        jCheckBoxMenuItem4.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_D, java.awt.event.InputEvent.CTRL_MASK));
        jCheckBoxMenuItem4.setText("Show Zones");
        jCheckBoxMenuItem4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jCheckBoxMenuItem4ActionPerformed(evt);
            }
        });
        jMenu7.add(jCheckBoxMenuItem4);

        jMenuBar1.add(jMenu7);

        jMenu3.setText("Tools");

        jRadioButtonMenuItem1.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, 0));
        buttonGroup1.add(jRadioButtonMenuItem1);
        jRadioButtonMenuItem1.setText("Background Tool");
        jRadioButtonMenuItem1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/background.png"))); // NOI18N
        jRadioButtonMenuItem1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem1ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem1);

        jRadioButtonMenuItem2.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, 0));
        buttonGroup1.add(jRadioButtonMenuItem2);
        jRadioButtonMenuItem2.setText("Tile Tool");
        jRadioButtonMenuItem2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/tile.png"))); // NOI18N
        jRadioButtonMenuItem2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem2ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem2);

        jRadioButtonMenuItem3.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_G, 0));
        buttonGroup1.add(jRadioButtonMenuItem3);
        jRadioButtonMenuItem3.setSelected(true);
        jRadioButtonMenuItem3.setText("Graphic Tool");
        jRadioButtonMenuItem3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/palette.png"))); // NOI18N
        jRadioButtonMenuItem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem3ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem3);

        jMenu4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zone.png"))); // NOI18N
        jMenu4.setText("Zones");

        jRadioButtonMenuItem10.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, 0));
        buttonGroup2.add(jRadioButtonMenuItem10);
        jRadioButtonMenuItem10.setText("New Polygon");
        jRadioButtonMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zoneNew.png"))); // NOI18N
        jRadioButtonMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem10ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItem10);
        jMenu4.add(jSeparator7);

        jRadioButtonMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_W, 0));
        buttonGroup2.add(jRadioButtonMenuItem7);
        jRadioButtonMenuItem7.setText("Polygon Selection");
        jRadioButtonMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zoneMode.png"))); // NOI18N
        jRadioButtonMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem7ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItem7);

        jRadioButtonMenuItem8.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        buttonGroup2.add(jRadioButtonMenuItem8);
        jRadioButtonMenuItem8.setText("Point Selection");
        jRadioButtonMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zoneEdit.png"))); // NOI18N
        jRadioButtonMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem8ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItem8);

        jRadioButtonMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, 0));
        buttonGroup2.add(jRadioButtonMenuItem9);
        jRadioButtonMenuItem9.setText("Edit Points");
        jRadioButtonMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/zone.png"))); // NOI18N
        jRadioButtonMenuItem9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem9ActionPerformed(evt);
            }
        });
        jMenu4.add(jRadioButtonMenuItem9);

        jMenu3.add(jMenu4);

        jRadioButtonMenuItem5.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, 0));
        buttonGroup1.add(jRadioButtonMenuItem5);
        jRadioButtonMenuItem5.setText("Entity Tool");
        jRadioButtonMenuItem5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/entity.png"))); // NOI18N
        jRadioButtonMenuItem5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jRadioButtonMenuItem5ActionPerformed(evt);
            }
        });
        jMenu3.add(jRadioButtonMenuItem5);
        jMenu3.add(jSeparator6);

        jMenuItem14.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem14.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/paintbrush.png"))); // NOI18N
        jMenuItem14.setText("Image Blender");
        jMenuItem14.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem14ActionPerformed(evt);
            }
        });
        jMenu3.add(jMenuItem14);

        jMenuBar1.add(jMenu3);

        jMenu5.setText("Library");

        jMenuItem6.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_edit.png"))); // NOI18N
        jMenuItem6.setText("Define Selected Graphic");
        jMenuItem6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem6ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem6);
        jMenu5.add(jSeparator10);

        jMenuItem7.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_I, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_add.png"))); // NOI18N
        jMenuItem7.setText("Import Graphic");
        jMenuItem7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem7ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem7);

        jMenuItem8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_delete.png"))); // NOI18N
        jMenuItem8.setText("Delete Graphic");
        jMenuItem8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem8ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem8);

        jMenuItem9.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_M, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/picture_go.png"))); // NOI18N
        jMenuItem9.setText("Move Graphic");
        jMenu5.add(jMenuItem9);

        jMenuItem16.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/textfield_rename.png"))); // NOI18N
        jMenuItem16.setText("Rename Graphic File");
        jMenuItem16.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem16ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem16);
        jMenu5.add(jSeparator8);

        jMenuItem10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/folder_add.png"))); // NOI18N
        jMenuItem10.setText("Create Folder");
        jMenuItem10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem10ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem10);

        jMenuItem11.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/folder_delete.png"))); // NOI18N
        jMenuItem11.setText("Delete Folder");
        jMenuItem11.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem11ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem11);

        jMenuItem17.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/folder_go.png"))); // NOI18N
        jMenuItem17.setText("Move Folder");
        jMenuItem17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem17ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem17);

        jMenuItem12.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_R, java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem12.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/folder_edit.png"))); // NOI18N
        jMenuItem12.setText("Rename Folder");
        jMenuItem12.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem12ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem12);
        jMenu5.add(jSeparator9);

        jMenuItem13.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenuItem13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/ui/forms/icons/brick.png"))); // NOI18N
        jMenuItem13.setText("Define All Graphics");
        jMenuItem13.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenuItem13ActionPerformed(evt);
            }
        });
        jMenu5.add(jMenuItem13);

        jMenuBar1.add(jMenu5);

        jMenu6.setText("Help");

        jMenuItem15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/stormgate/editor/controls/cursors/finger.png"))); // NOI18N
        jMenuItem15.setText("Screw You!");
        jMenu6.add(jMenuItem15);

        jMenuBar1.add(jMenu6);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 516, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 52, Short.MAX_VALUE)
                .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jToolBar2, javax.swing.GroupLayout.DEFAULT_SIZE, 974, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar4, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jToolBar3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jToolBar2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void menuNewActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuNewActionPerformed
		//NEW
		newMap();
    }//GEN-LAST:event_menuNewActionPerformed

    private void menuOpenActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuOpenActionPerformed
		openMap();
    }//GEN-LAST:event_menuOpenActionPerformed

	public void setZoom()
	{
		int zoomInt = (int) (d.getZoom() * 100);
		jComboBox1.setSelectedItem(zoomInt + "%");
	}

	private void zoomComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_zoomComboBoxActionPerformed
		String zoomString = (String) jComboBox1.getSelectedItem();
		float zoomAmount = 0;
		try {
			zoomString = zoomString.replaceAll("%", "");
			zoomAmount = Integer.parseInt(zoomString);
		} catch (Exception e1) {
			zoomAmount = 100;
		}
		float val = zoomAmount / 100;
		d.setZoom(val);
		setZoom();
		graphic.requestFocusInWindow();
		updateGraphics();
	}//GEN-LAST:event_zoomComboBoxActionPerformed

	public void updateGraphics()
	{
		MapPoint p = d.getCameraLocation();
		jTextField1.setText(p.getX() + "");
		jTextField2.setText(p.getY() + "");
		graphic.repaint();
		graphic.revalidate();
		graphic1.repaint();
		graphic1.revalidate();

	}

	private void formKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyPressed
		//System.out.println(evt.getKeyCode());
		//GLOBAL HOTKEYS
		/*
		MapPoint p = d.map.getCameraLocation();
		p.setX(p.getX()+1);
		p.setY(p.getY()+1);
		//d.map.setCameraLocation(p);
		 */
		keyHandler.keyPressed(evt);
		//updateGraphics();
	}//GEN-LAST:event_formKeyPressed

	private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed
		requestFocusInWindow();
	}//GEN-LAST:event_formMousePressed

	private void focusWindow(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_focusWindow
		requestFocusInWindow();
	}//GEN-LAST:event_focusWindow

	private void jCheckBox5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox5ActionPerformed
		toggleSnap();
	}//GEN-LAST:event_jCheckBox5ActionPerformed

	private void formKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_formKeyReleased
		//GLOBAL HOTKEYS
		keyHandler.keyReleased(evt);
	}//GEN-LAST:event_formKeyReleased

	private void setCameraInput()
	{
		String textX = jTextField1.getText();
		String textY = jTextField2.getText();
		//MapPoint p = d.getCameraLocation();

		int moveX = Common.parseInt(textX);
		int moveY = Common.parseInt(textY);

		//p.setX(moveX);
		//p.setY(moveY);
                
                d.setCamera(moveX, moveY);
                
		updateGraphics();
	}
	private void jTextField2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField2ActionPerformed
		setCameraInput();
	}//GEN-LAST:event_jTextField2ActionPerformed

	private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
		setCameraInput();
	}//GEN-LAST:event_jTextField1ActionPerformed

	private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
		d.undoAction();
	}//GEN-LAST:event_jButton2ActionPerformed

	private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
		d.redoAction();
	}//GEN-LAST:event_jButton5ActionPerformed

	private void jCheckBox4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jCheckBox4ActionPerformed
		toggleGrid();
	}//GEN-LAST:event_jCheckBox4ActionPerformed

	private void jMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem1ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem1ActionPerformed
		d.undoAction();
	}//GEN-LAST:event_jMenuItem1ActionPerformed

	private void jMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem2ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem2ActionPerformed
		d.redoAction();
	}//GEN-LAST:event_jMenuItem2ActionPerformed

	private void jMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem3ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem3ActionPerformed
		//CUT
		d.cut();
	}//GEN-LAST:event_jMenuItem3ActionPerformed

	private void jMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem4ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem4ActionPerformed
		//COPY
		d.copy();
	}//GEN-LAST:event_jMenuItem4ActionPerformed

	private void jMenuItem5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem5ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem5ActionPerformed
		//PASTE
		d.paste();
	}//GEN-LAST:event_jMenuItem5ActionPerformed

	private void jButton6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton6ActionPerformed
	{//GEN-HEADEREND:event_jButton6ActionPerformed
		ImageBlenderDialog blender = new ImageBlenderDialog();
		blender.setVisible(true);
	}//GEN-LAST:event_jButton6ActionPerformed

	private void jButton4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton4ActionPerformed
	{//GEN-HEADEREND:event_jButton4ActionPerformed
		//Save
		saveAction();
	}//GEN-LAST:event_jButton4ActionPerformed

	private void jRadioButtonMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem2ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem2ActionPerformed
		//Tile Tool
		d.changeTool(d.tileTool);
	}//GEN-LAST:event_jRadioButtonMenuItem2ActionPerformed

	private void jRadioButtonMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem3ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem3ActionPerformed
		//Graphic Tool
		d.changeTool(d.graphicTool);
	}//GEN-LAST:event_jRadioButtonMenuItem3ActionPerformed

	private void jRadioButtonMenuItem10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem10ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem10ActionPerformed
		//Zone Tool: New Polygon
		d.changeTool(d.zoneTool);
		d.zoneTool.setNewMode();
	}//GEN-LAST:event_jRadioButtonMenuItem10ActionPerformed

	private void jRadioButtonMenuItem7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem7ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem7ActionPerformed
		//Zone Tool: Polygon Selection
		d.changeTool(d.zoneTool);
		d.zoneTool.setPolygonMode();
	}//GEN-LAST:event_jRadioButtonMenuItem7ActionPerformed

	private void jRadioButtonMenuItem8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem8ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem8ActionPerformed
		//Zone Tool: Point Selection
		d.changeTool(d.zoneTool);
		d.zoneTool.setSelectMode();
	}//GEN-LAST:event_jRadioButtonMenuItem8ActionPerformed

	private void jRadioButtonMenuItem9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem9ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem9ActionPerformed
		//Zone Tool: Edit Points
		d.changeTool(d.zoneTool);
		d.zoneTool.setEditMode();
	}//GEN-LAST:event_jRadioButtonMenuItem9ActionPerformed

	private void jMenuItem14ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem14ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem14ActionPerformed
		ImageBlenderDialog blender = new ImageBlenderDialog();
		blender.setVisible(true);
	}//GEN-LAST:event_jMenuItem14ActionPerformed

	private void jMenuItem6ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem6ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem6ActionPerformed
		//DEFINE GRAPHIC
		browser2.getController().editGraphic();
	}//GEN-LAST:event_jMenuItem6ActionPerformed

	private void jMenuItem7ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem7ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem7ActionPerformed
		//IMPORT GRAPHIC
		browser2.getController().importGraphic();
	}//GEN-LAST:event_jMenuItem7ActionPerformed

	private void jMenuItem8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem8ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem8ActionPerformed
		//DELETE GRAPHIC
		browser2.getController().deleteGraphic();
	}//GEN-LAST:event_jMenuItem8ActionPerformed

	private void jMenuItem10ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem10ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem10ActionPerformed
		//CREATE FOLDER
		browser2.getController().newFolder();
	}//GEN-LAST:event_jMenuItem10ActionPerformed

	private void jMenuItem11ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem11ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem11ActionPerformed
		//DELETE FOLDER
		browser2.getController().deleteFolder();
	}//GEN-LAST:event_jMenuItem11ActionPerformed

	private void jMenuItem12ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem12ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem12ActionPerformed
		//RENAME FOLDER
		browser2.getController().renameFolder();
	}//GEN-LAST:event_jMenuItem12ActionPerformed

	private void jMenuItem13ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem13ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem13ActionPerformed
		//DEFINE ALL GRAPHICS
		browser2.getController().defineAllGraphics();
	}//GEN-LAST:event_jMenuItem13ActionPerformed

	private void jCheckBoxMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMenuItem1ActionPerformed
	{//GEN-HEADEREND:event_jCheckBoxMenuItem1ActionPerformed
		//grid
		toggleGrid();
	}//GEN-LAST:event_jCheckBoxMenuItem1ActionPerformed

	private void jCheckBoxMenuItem2ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMenuItem2ActionPerformed
	{//GEN-HEADEREND:event_jCheckBoxMenuItem2ActionPerformed
		//snap
		toggleSnap();
	}//GEN-LAST:event_jCheckBoxMenuItem2ActionPerformed

	private void jCheckBoxMenuItem3ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMenuItem3ActionPerformed
	{//GEN-HEADEREND:event_jCheckBoxMenuItem3ActionPerformed
		//show hidden
		filter.showHidden = jCheckBoxMenuItem3.getState();
		updateGraphics();
	}//GEN-LAST:event_jCheckBoxMenuItem3ActionPerformed

	private void jCheckBoxMenuItem4ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMenuItem4ActionPerformed
	{//GEN-HEADEREND:event_jCheckBoxMenuItem4ActionPerformed
		//show zones
		filter.zones = jCheckBoxMenuItem4.getState();
		updateGraphics();
	}//GEN-LAST:event_jCheckBoxMenuItem4ActionPerformed

	private void jMenuItem16ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem16ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem16ActionPerformed
		//RENAME GRAPHIC
		browser2.getController().renameGraphic();
	}//GEN-LAST:event_jMenuItem16ActionPerformed

	private void jMenuItem17ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem17ActionPerformed
	{//GEN-HEADEREND:event_jMenuItem17ActionPerformed
		//MOVE FOLDER
		browser2.getController().moveFolder();
	}//GEN-LAST:event_jMenuItem17ActionPerformed

	private void jRadioButtonMenuItem1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem1ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem1ActionPerformed
		//BACKGROUND TOOL
		d.changeTool(d.backgroundTool);
	}//GEN-LAST:event_jRadioButtonMenuItem1ActionPerformed

	private void jRadioButtonMenuItem5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jRadioButtonMenuItem5ActionPerformed
	{//GEN-HEADEREND:event_jRadioButtonMenuItem5ActionPerformed
		//ENTITY TOOL
		d.changeTool(d.entityTool);
	}//GEN-LAST:event_jRadioButtonMenuItem5ActionPerformed

  private void menuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveActionPerformed
	  //Save Map
	  saveAction();
  }//GEN-LAST:event_menuSaveActionPerformed

  private void menuSaveAsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuSaveAsActionPerformed
	  saveAs();
  }//GEN-LAST:event_menuSaveAsActionPerformed

  private void jButton1ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton1ActionPerformed
  {//GEN-HEADEREND:event_jButton1ActionPerformed
	  newMap();
  }//GEN-LAST:event_jButton1ActionPerformed

  private void jButton8ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton8ActionPerformed
  {//GEN-HEADEREND:event_jButton8ActionPerformed
	  propertyDialog.setVisible(true);
  }//GEN-LAST:event_jButton8ActionPerformed

  private void jMenuItem18ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jMenuItem18ActionPerformed
  {//GEN-HEADEREND:event_jMenuItem18ActionPerformed
	  propertyDialog.setVisible(true);
  }//GEN-LAST:event_jMenuItem18ActionPerformed

  private void menuExportActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_menuExportActionPerformed
  {//GEN-HEADEREND:event_menuExportActionPerformed
	  //EXPORT
	  boolean result = Compile.compile(d.getMap().getMapFile());
	  if (result) {
		  stormgate.log.Log.addLog("Build Successful");
	  } else {
		  stormgate.log.Log.addLog("Build Failed");
	  }
  }//GEN-LAST:event_menuExportActionPerformed

  private void jCheckBoxMenuItem5ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jCheckBoxMenuItem5ActionPerformed
  {//GEN-HEADEREND:event_jCheckBoxMenuItem5ActionPerformed
	  //TOGGLE TILE ENABLE
	  filter.tileEnable = jCheckBoxMenuItem5.getState();
	  updateGraphics();
  }//GEN-LAST:event_jCheckBoxMenuItem5ActionPerformed

  private void jButton9ActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_jButton9ActionPerformed
  {//GEN-HEADEREND:event_jButton9ActionPerformed
	  stormgate.log.Log.showLog();
  }//GEN-LAST:event_jButton9ActionPerformed

private void menuExitActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_menuExitActionPerformed
    if (checkSave()) {
            dispose();
            System.exit(0);
    }
}//GEN-LAST:event_menuExitActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private stormgate.editor.ui.forms.browser.Browser browser2;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private stormgate.editor.ui.forms.graphic.Graphic graphic;
    private stormgate.editor.ui.forms.graphic.Graphic graphic1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JCheckBox jCheckBox4;
    private javax.swing.JCheckBox jCheckBox5;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem1;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem2;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem3;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem4;
    private javax.swing.JCheckBoxMenuItem jCheckBoxMenuItem5;
    private javax.swing.JComboBox jComboBox1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenu jMenu3;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenu jMenu6;
    private javax.swing.JMenu jMenu7;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem10;
    private javax.swing.JMenuItem jMenuItem11;
    private javax.swing.JMenuItem jMenuItem12;
    private javax.swing.JMenuItem jMenuItem13;
    private javax.swing.JMenuItem jMenuItem14;
    private javax.swing.JMenuItem jMenuItem15;
    private javax.swing.JMenuItem jMenuItem16;
    private javax.swing.JMenuItem jMenuItem17;
    private javax.swing.JMenuItem jMenuItem18;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JMenuItem jMenuItem4;
    private javax.swing.JMenuItem jMenuItem5;
    private javax.swing.JMenuItem jMenuItem6;
    private javax.swing.JMenuItem jMenuItem7;
    private javax.swing.JMenuItem jMenuItem8;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem10;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem3;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem5;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem7;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem8;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator10;
    private javax.swing.JPopupMenu.Separator jSeparator11;
    private javax.swing.JPopupMenu.Separator jSeparator12;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator4;
    private javax.swing.JPopupMenu.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JPopupMenu.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JPopupMenu.Separator jSeparator9;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    private javax.swing.JToolBar jToolBar1;
    private javax.swing.JToolBar jToolBar2;
    private javax.swing.JToolBar jToolBar3;
    private javax.swing.JToolBar jToolBar4;
    private javax.swing.JMenuItem menuExit;
    private javax.swing.JMenuItem menuExport;
    private javax.swing.JMenuItem menuNew;
    private javax.swing.JMenuItem menuOpen;
    private javax.swing.JMenuItem menuSave;
    private javax.swing.JMenuItem menuSaveAs;
    private stormgate.editor.ui.forms.tool.Toolbox toolbox1;
    // End of variables declaration//GEN-END:variables

	public void updateTool()
	{
		ToolInterface tool = d.getTool();

		buttonGroup2.clearSelection();

		if (tool == d.tileTool) {
			jRadioButtonMenuItem2.setSelected(true);
		} else {
			if (tool == d.graphicTool) {
				jRadioButtonMenuItem3.setSelected(true);
			} else {
				if (tool == d.zoneTool) {
					buttonGroup1.clearSelection();
					int modeType = d.zoneTool.getModeType();
					if (modeType == ZoneTool.NEW) {
						jRadioButtonMenuItem10.setSelected(true);
						//New Polygon
					}
					if (modeType == ZoneTool.POLYGON) {
						jRadioButtonMenuItem7.setSelected(true);
						//Polygon Selection
					}
					if (modeType == ZoneTool.POINT) {
						jRadioButtonMenuItem8.setSelected(true);
						//Point Selection
					}
					if (modeType == ZoneTool.EDIT) {
						jRadioButtonMenuItem9.setSelected(true);
						//Edit Points
					}
				} else {
					if (tool == d.backgroundTool) {
						jRadioButtonMenuItem1.setSelected(true);
					} else {
						if (tool == d.entityTool) {
							jRadioButtonMenuItem5.setSelected(true);
						}
					}
				}
			}
		}
		updateGraphics();
	}

	private void toggleGrid()
	{
		filter.grid = !filter.grid;
		jCheckBox4.setSelected(filter.grid);
		jCheckBoxMenuItem1.setSelected(filter.grid);
		if (!filter.grid) {
			filter.snap = true;
			toggleSnap();
		}
		updateGraphics();
	}

	private void toggleSnap()
	{
		filter.snap = !filter.snap;
		jCheckBox5.setSelected(filter.snap);
		jCheckBoxMenuItem2.setSelected(filter.snap);
		if (filter.snap) {
			filter.grid = false;
			toggleGrid();
		}
	}

	private void saveAs()
	{

		System.out.println("save as");
		AWTFileDialog fd = new AWTFileDialog();
		String fileSelected = fd.saveFile("Save", ".", "*.ao");

		if (fileSelected != null) {
			Map map = d.getMap();

			if (fileSelected.toLowerCase().lastIndexOf(".ao") != fileSelected.length() - 3) {
				fileSelected += ".ao";
			}

			File projectFile = new File(fileSelected);
			File projectFolder = projectFile.getParentFile();
			String path = projectFolder.getAbsolutePath();

			//String mapPath = map.getMapPath();
			//if (mapPath != null) {
			//Copy old tiles over if map already opened
			map.copyTiles(path + "\\map");
			//}

			map.setMapPath(path + "\\map");
			map.setMapFile(fileSelected);

			save();
		}
	}

	public boolean checkSave()
	{
		if (d.mapChanged()) {
			JOptionPane op = new JOptionPane(
					"This map has been modified. Do you want to save the changes?",
					JOptionPane.DEFAULT_OPTION,
					JOptionPane.YES_NO_CANCEL_OPTION,
					null,
					null);

			JDialog dialog = op.createDialog(this, "Save Changes");
			dialog.setVisible(true);

			int result = JOptionPane.YES_OPTION;

			try {
				result = ((Integer) op.getValue()).intValue();
			} catch (Exception e) {
				return false;
			}

			if (result == JOptionPane.YES_OPTION) {
				saveAction();
				return true;
			} else {
				if (result == JOptionPane.CANCEL_OPTION) {
					return false;
				} else {
					return true;
				}
			}
		}
		return true;
	}

	private void save()
	{
		Map map = d.getMap();
		map.saveMapFile();
		map.saveTiles();
		//System.gc();
		d.refreshLibrary();
		d.refresh();
		//d.reset();
		//alertMemory = false;
	}

	public void newMap()
	{
		if (checkSave()) {
			NewMapDialog dialog = new NewMapDialog(this, true);
			dialog.setData(d);
			dialog.setVisible(true);
			//alertMemory = false;
		}
	}

	private void openMap()
	{
		if (checkSave()) {
			AWTFileDialog fd = new AWTFileDialog();
			String fileSelected = fd.openFile("Open", ".", "*.ao");

			if (fileSelected != null) {
				Map loadMap = XMLProject.loadMap(d, fileSelected);
				if (loadMap != null) {
					d.setMap(loadMap);
				} else {
					System.out.println("Map can't be loaded");
					JOptionPane.showMessageDialog(this, "Map can't be loaded, make sure it is a proper map file");
				}
			}

			d.reset();
		}
	}

	public void cleanGraphics()
	{
		graphic.clean();
		graphic1.clean();
	}

	private void saveAction()
	{
		Map map = d.getMap();
		String mapFile = map.getMapFile();
		if (mapFile == null) {
			saveAs();
		} else {
			save();
		}
	}
}

class CloseCheck extends WindowAdapter
{

	EditorForm form;

	public CloseCheck(EditorForm form)
	{
		this.form = form;
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		checkClose();
	}

	public void checkClose()
	{
		if (form.checkSave()) {
			form.dispose();
			System.exit(0);
		}
	}
}
