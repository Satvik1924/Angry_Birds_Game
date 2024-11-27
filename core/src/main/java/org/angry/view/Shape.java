package org.angry.view;

import org.angry.Model.Matrix;
import org.angry.Model.Body;
public abstract class Shape
{

	public abstract float getWidth();
	public abstract float getHeight();

	public enum Type
	{
		Circle, Poly
	}

	public Body body;
	public float radius;
	public final Matrix u = new Matrix();

	public Shape() {}

    public abstract void initialize();

	public abstract void computeMass( float density );

	public abstract void setOrient( float radians );

	public abstract Type getType();

}
