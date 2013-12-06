package stormgate.geom;

/**
 *
 * @author David
 */
public class Geometry {
	public static MapPoint closestPointOnLine(MapPoint point, MapPoint a, MapPoint b)
	{
		Pt p = new Pt(point.getX(), point.getY());
		Segment segment = new Segment(new Pt(a.getX(), a.getY()), new Vr(b.getX() - a.getX(), b.getY() - a.getY()));
		Pt r = closestPointOnLine(p, segment);
		return new MapPoint((int) Math.round(r.x), (int) Math.round(r.y));
	}

	public static Pt closestPointOnLine(Pt point, Segment line)
	{
		return closestPointOnLine(point, line, true);
	}

	public static Pt closestPointOnLine(Pt point, Segment line, boolean segment)
	{
		//CALCULATING T FROM A TO B
		// C = p - a
		Vr c = point.cloneVector();
		c.subtract(line.getPoint());

		// V = Normalize(b - a)
		Vr v = line.getVector().cloneVector();
		v.normalize();

		// D = Distance of a to b
		double d = line.getVector().magnitude();

		// T = v * c (dot)
		double t = v.dot(c);

		if(segment){
			//CHECK IF T IS WITHIN A AND B
			if (t <= 0) {
				return line.getPoint();
			}
			if (t > d) {
				return line.getPoint2();
			}
		}

		//T IS INSIDE A AND B
		Pt val = line.getPoint().clonePoint();
		v.scalar(t);
		val.add(v);

		return val;
	}
}
