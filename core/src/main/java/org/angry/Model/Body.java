package org.angry.Model;

import com.badlogic.gdx.math.Ellipse;
import org.angry.view.Shape;

public class Body {
	public final Vector position = new Vector();
	public final Vector velocity = new Vector();
	public final Vector force = new Vector();
	public float angularVelocity;
	public float torque;
	public float orient;
	public float mass, invMass, inertia, invInertia;
	public float staticFriction;
	public float dynamicFriction;
	public float restitution;
	public final Shape shape;

	public Body( Shape shape, int x, int y )
	{
		this.shape = shape;

		position.set( x, y );
		velocity.set( 0, 0 );
		angularVelocity = 0;
		torque = 0;
		orient = ImpulseMath.random( -ImpulseMath.PI, ImpulseMath.PI );
		force.set( 0, 0 );
		shape.body = this;
		shape.initialize();
	}

	public void applyImpulse( Vector impulse, Vector contactVector )
	{
		velocity.addsi( impulse, invMass );
		angularVelocity += invInertia * Vector.cross( contactVector, impulse );
	}

	public void setStatic()
	{
		inertia = 0.0f;
		invInertia = 0.0f;
		mass = 0.0f;
		invMass = 0.0f;
	}

	public void setOrient( float radians )
	{
		orient = radians;
		shape.setOrient( radians );
	}
	public void setTransform(float x, float y, float angle) {
		// Update position and orientation
		position.set(x, y);
		setOrient(angle);
	}
	public Ellipse getPosition() {
		// Assume the shape defines width and height
		float width = shape.getWidth();  // Get the width from the shape
		float height = shape.getHeight(); // Get the height from the shape

		// Create and return an Ellipse centered at the body's position
		return new Ellipse(position.x - width / 2, position.y - height / 2, width, height);
	}
}
