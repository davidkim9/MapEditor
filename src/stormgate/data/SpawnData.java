/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stormgate.data;

/**
 *
 * @author David
 */
public class SpawnData
{

	public int id;
	public boolean type;
	public int count;

	@Override
	public String toString()
	{
		return (type ? "MOB" : "NPC") + " " + id + " : #" + count;
	}
}
