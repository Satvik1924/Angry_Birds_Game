package org.angry.Model;

public class Col {

    public static ColInterface[][] dispatch =
            {
                    {CcCol.instance, Cpcol.instance},
                    {Pccol.instance, Ppcol.instance}
            };
}
