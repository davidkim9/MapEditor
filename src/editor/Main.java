package editor;

import stormgate.editor.ui.forms.EditorForm;
import editor.style.LookAndFeel;

/**
 *
 * @author David Kim
 */
public class Main {

	public static void main(String[] args) {
		new LookAndFeel();
                
		EditorForm editor = new EditorForm();
		editor.setVisible(true);

	}
}