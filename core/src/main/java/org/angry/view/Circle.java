package org.angry.view;

import org.angry.Model.ImpulseMath;

public class Circle extends Shape
{
	float mass=1;

	public Circle( float r )
	{

		radius = r;

	}

	public Circle( float r ,float mass)
	{
		radius = r;
		this.mass=mass;
	}

	@Override
	public float getWidth() {
		return radius * 2; // Diameter
	}

	@Override
	public float getHeight() {
		return radius * 2; // Diameter
	}
	@Override
	public void initialize()
	{
		computeMass( mass );
	}

	@Override
	public void computeMass( float density )
	{
		body.mass = ImpulseMath.PI * radius * radius * density;
		body.invMass = (body.mass != 0.0f) ? 1.0f / body.mass : 0.0f;
		body.inertia = body.mass * radius * radius;
		body.invInertia = (body.inertia != 0.0f) ? 1.0f / body.inertia : 0.0f;
	}

	@Override
	public void setOrient( float radians )
	{
	}

	@Override
	public Type getType()
	{
		return Type.Circle;
	}

}
