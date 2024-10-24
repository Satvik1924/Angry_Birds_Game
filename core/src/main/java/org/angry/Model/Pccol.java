package org.angry.Model;

public class Pccol implements ColInterface
{

	public static final Pccol instance = new Pccol();

	@Override
	public void handleCollision(Handel m, Body a, Body b)
	{
		Cpcol.instance.handleCollision(m, b, a);

		if ( m.contactCount > 0 )
		{
			m.normal.negi();
		}
	}

}
