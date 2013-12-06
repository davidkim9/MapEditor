/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.editor.tool.data;

/**
 *
 * @author keviixp
 */
public class SelectedTile
{

	public int x = 0;
	public int y = 0;
	public boolean selectAll = true;
	public boolean[][] selected;

	@Override
	public SelectedTile clone()
	{
		SelectedTile copy = new SelectedTile();
		copy.x = x;
		copy.y = y;
		copy.selectAll = selectAll;
		copy.selected = copyArray(selected);
		return copy;
	}

	private boolean[][] copyArray(boolean[][] selected)
	{
		if (selected != null) {
			boolean[][] copy = new boolean[selected.length][selected[0].length];

			for (int i = 0; i < selected.length; i++) {
				for (int j = 0; j < selected[i].length; j++) {
					copy[i][j] = selected[i][j];
				}
			}

			return copy;
		}
		return null;
	}
}
